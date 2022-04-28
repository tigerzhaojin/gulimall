package com.tz.mall.ware.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tz.common.utils.R;
import com.tz.common.vo.MemberRespVo;
import com.tz.mall.ware.feign.MemberFeignService;
import com.tz.mall.ware.vo.FareVo;
import com.tz.mall.ware.vo.MemberAddressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.Query;

import com.tz.mall.ware.dao.WareInfoDao;
import com.tz.mall.ware.entity.WareInfoEntity;
import com.tz.mall.ware.service.WareInfoService;
import org.springframework.util.StringUtils;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {
    @Autowired
    MemberFeignService memberFeignService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareInfoEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            queryWrapper.eq("id",key)
                    .or().like("name",key)
                    .or().like("address",key)
                    .or().like("areacode",key);
        }
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public FareVo getFare(Long addrId) {
        FareVo fareVo = new FareVo();
        R info = memberFeignService.info(addrId);
        Object data = info.get("memberReceiveAddress");
        ObjectMapper objectMapper = new ObjectMapper();
        MemberAddressVo memberAddressVo = objectMapper.convertValue(data, MemberAddressVo.class);
        if (memberAddressVo!=null){
            String phone = memberAddressVo.getPhone();

            fareVo.setMemberAddressVo(memberAddressVo);
            phone=phone.substring(phone.length()-1,phone.length());
            fareVo.setFare(new BigDecimal(phone));
            return fareVo;
        } else {
            return null;
        }


    }

}