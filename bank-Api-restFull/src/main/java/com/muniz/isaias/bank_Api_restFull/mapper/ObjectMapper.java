package com.muniz.isaias.bank_Api_restFull.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.muniz.isaias.bank_Api_restFull.dto.TransactionDTO;
import com.muniz.isaias.bank_Api_restFull.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ObjectMapper {
    private static final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <O, D> D parseObject(O origin, Class<D> destination){
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListOfObjects(List<O> origin, Class<D> destination){
        List<D> destinationObjects = new ArrayList<>();

        for (Object o: origin){
            destinationObjects.add(mapper.map(o, destination));
        }
        return destinationObjects;
    }
}
