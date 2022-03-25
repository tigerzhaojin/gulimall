package com.tz.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.sun.glass.ui.mac.MacPlatformFactory;
import com.tz.common.to.es.SkuEsModel;
import com.tz.common.utils.R;
import com.tz.mall.search.config.MallElasticSearchConfig;
import com.tz.mall.search.constant.EsConstant;
import com.tz.mall.search.feign.ProductFeign;
import com.tz.mall.search.service.MallSearchService;
import com.tz.mall.search.vo.SearchParam;
import com.tz.mall.search.vo.SearchResult;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MallSearchServiceImpl implements MallSearchService {
    @Autowired
    RestHighLevelClient client;
    @Autowired
    ProductFeign productFeign;
    @Override
    public SearchResult search(SearchParam param) {
//        1.准备检索请求

        SearchRequest searchRequest = buildSearchRequest(param);
        try {
//        2.执行检索请求
            SearchResponse response =
                    client.search(searchRequest, MallElasticSearchConfig.COMMON_OPTIONS);
//        3.分析响应数据，封装成指定的返回格式
            SearchResult searchResult= buildSearchResult(response,param);
            return searchResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SearchRequest buildSearchRequest(SearchParam param) {

//        构建es的检索DSL语句
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//      1  构建boolquery
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//       1.1 构建must，模糊匹配
        if (!StringUtils.isEmpty(param.getKeyword())){
            boolQuery.must(QueryBuilders.matchQuery("skuTitle",param.getKeyword()));
        }
//        1.2 构建filter
//        1.2.1 根据catelogid
        if (param.getCatalog3Id()!=null){
//            前面保存的时候拼错了（catalogId），此处将错就错
            boolQuery.filter(QueryBuilders.termQuery("catalogId",param.getCatalog3Id()));
        }
//        1.2.2 根据品牌Id
        if (param.getBrandIds()!=null && param.getBrandIds().size()>0){
            boolQuery.filter(QueryBuilders.termsQuery("brandId",param.getBrandIds()));
        }

//        1.2.3 根据是否有库存
        if (param.getHasStock()!=null){
            boolQuery.filter(QueryBuilders.termsQuery("hasStock",param.getHasStock()==1));
        }
//        1.2.4 按照价格区间查询.  1_500/_500/500_
        if (!StringUtils.isEmpty(param.getSkuPrice())){
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = param.getSkuPrice().split("_");
            if (s.length==2){
                rangeQuery.gte(s[0]).lte(s[1]);
            } else if (s.length==1){
                if (param.getSkuPrice().startsWith("_")){
                    rangeQuery.lte(s[0]);
                }
                if (param.getSkuPrice().endsWith("_")) {
                    rangeQuery.gte(s[0]);
                }
            }
            boolQuery.filter(rangeQuery);
        }
//        1.2.5 按照attrs查询，嵌入式的.
        if (param.getAttrs()!=null && param.getAttrs().size()>0){

            // 参数形式：attrs=1_5寸:8寸&attrs=2_8G:16G
            for (String attr : param.getAttrs()) {
                BoolQueryBuilder nestedboolQuery = QueryBuilders.boolQuery();
                String[] s = attr.split("_");
                String attrId = s[0];
                String[] attrValue = s[1].split(":");
                nestedboolQuery.must(QueryBuilders.termsQuery("attrs.attrId",attrId));
                nestedboolQuery.must(QueryBuilders.termsQuery("attrs.attrValue",attrValue));
                NestedQueryBuilder nestedQuery =
                        QueryBuilders.nestedQuery("attrs", nestedboolQuery, ScoreMode.None);

                boolQuery.filter(nestedQuery);
            }
        }
        sourceBuilder.query(boolQuery);
//        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~检索拼接完成~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        /**
         * 排序、分页、高亮*/

        /**2.1排序*/
        if (!StringUtils.isEmpty(param.getSort())){
            String sort = param.getSort();
            //排序数据约定 sort=hotscore_asc/desc
            String[] s = sort.split("_");
            SortOrder order=s[1].equalsIgnoreCase("asc")?SortOrder.ASC:SortOrder.DESC;
            sourceBuilder.sort(s[0],order);
        }
        /**2.2分页
         * pageNum=1 from:0 size:5 [0,1,2,3,4]
         * pageNum=2 from:0 size:5 [0,1,2,3,4]
         * from (pageNum-1)*5
         * */
        sourceBuilder.from((param.getPageNum()-1)*EsConstant.PRODUCT_PAGESIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);

        /**2.3高亮
         * */
        if (!StringUtils.isEmpty(param.getKeyword())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }
        /**2.3聚合分析
         * */
//        2.3.1 品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
//       品牌子聚合 品牌名称和图片. 要加上.keyword
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName.keyword").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg.keyword").size(1));
        sourceBuilder.aggregation(brand_agg);
//        2.3.2分类聚合
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
//       分类子聚合
        catalog_agg.subAggregation(AggregationBuilders.terms("cataLog_name_agg").field("catalogName.keyword").size(1));
        sourceBuilder.aggregation(catalog_agg);
//        2.3.3属性聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
//        构建子聚合
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
//        构建子聚合的子聚合，加上.keyword
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName.keyword").size(1));
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attr_agg.subAggregation(attr_id_agg);
        sourceBuilder.aggregation(attr_agg);
//        测试构建DSL语句
        String s = sourceBuilder.toString();
        System.out.println("构建完成的DSL语句："+s);

        /**2.4聚合分析
         * */




        SearchRequest request = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX},sourceBuilder);
        return request;
    }

    private SearchResult buildSearchResult(SearchResponse response,SearchParam param) {
        SearchResult result = new SearchResult();
        SearchHits hits = response.getHits();
//     查询到的商品信息遍历每条数据
        List<SkuEsModel> esModels=new ArrayList<>();
        if (hits.getHits()!=null && hits.getHits().length>0){
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel esModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
//                设置高亮
                if (!StringUtils.isEmpty(param.getKeyword())){
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String s = skuTitle.getFragments()[0].string();
                    esModel.setSkuTitle(s);
                }
                esModels.add(esModel);
            }
        }
        result.setProducts(esModels);
//~~~~~~~~~~~~~~~~~~当前商品所涉及到的所有属性~~~~~~~~~~~~~~~~~~~~~~

//~~~~~~~~~~~~~~~~~~当前商品所涉及到的所有品牌~~~~~~~~~~~~~~~~~~~~~~
        List<SearchResult.BrandVo> brandVos=new ArrayList<>();
        ParsedLongTerms brand_agg= response.getAggregations().get("brand_agg");
        List<? extends Terms.Bucket> brandBucket = brand_agg.getBuckets();
        for (Terms.Bucket bucket : brandBucket) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            String keyAsString = bucket.getKeyAsString();
            brandVo.setBrandId(Long.parseLong(keyAsString));
            R r = productFeign.brandInfo(Long.parseLong(keyAsString));
            Map<String,Object> brandMap=(Map<String,Object>) r.get("brand");
            brandVo.setBrandName(brandMap.get("name").toString());
            brandVo.setBrandImg(brandMap.get("logo").toString());
            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);
//~~~~~~~~~~~~~~~~~~当前商品所涉及到的所有分类~~~~~~~~~~~~~~~~~~~~~~

        List<SearchResult.CatelogVo> catelogVos=new ArrayList<>();
        ParsedLongTerms catalog_agg = response.getAggregations().get("catalog_agg");
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            SearchResult.CatelogVo catelogVo = new SearchResult.CatelogVo();
            String keyAsString = bucket.getKeyAsString();
            catelogVo.setCatalogId(Long.parseLong(keyAsString));
//            调用远程接口，从数据库中取catelogname，es中有问题
            R r = productFeign.catelogInfo(Long.parseLong(keyAsString));
            Map<String, Object> category = (Map<String, Object>) r.get("category");
            catelogVo.setCatalogName(category.get("name").toString());
            catelogVos.add(catelogVo);
        }
        result.setCatelogs(catelogVos);
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~分页  总页数\总页码\当前页码~~~~~~~~~~~~~~~~~~~~~~~~~
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        Integer totalPages=(int)total%EsConstant.PRODUCT_PAGESIZE==0?
                (int)total/EsConstant.PRODUCT_PAGESIZE:(int)total/EsConstant.PRODUCT_PAGESIZE+1;
        result.setTotalPages(totalPages);
        result.setPageNum(param.getPageNum());

        List<Integer> pageNavs=new ArrayList<>();
        for (int i=1;i<=totalPages;i++){
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);
//  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~设置完毕~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        return result;
    }
}
