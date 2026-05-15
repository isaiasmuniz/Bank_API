package com.muniz.isaias.bank_Api_restFull.mapper;

import com.github.dozermapper.core.DozerBeanMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class ObjectMapper {
    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <O, D> D parseObject(O oringin, Class<D> destination){
        return mapper.map(oringin, destination);
    }
}
