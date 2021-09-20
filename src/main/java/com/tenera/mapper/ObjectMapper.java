package com.tenera.mapper;

public interface ObjectMapper<X, Y> {

    Y mapSourceToTargetObject(X sourceObject, Y targetObject);
}
