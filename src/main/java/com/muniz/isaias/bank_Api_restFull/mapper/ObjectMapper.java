package com.muniz.isaias.bank_Api_restFull.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

public class ObjectMapper {
    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <O, D> D parseObject(O oringin, Class<D> destination){
        return mapper.map(oringin, destination);
    }

    public static <O, D> List<D> parseListOfObjects(List<O> origin, Class<D> destination){
        List<D> destinationObjects = new ArrayList<>();

        for (Object o: origin){
            destinationObjects.add(mapper.map(o, destination));
        }
        return destinationObjects;
    }
}
