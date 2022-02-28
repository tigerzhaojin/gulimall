package com.tz.mall.search.service;

import com.tz.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

public interface ProductSaveService {
    public boolean productUp(List<SkuEsModel> skuEsModels) throws IOException;

}
