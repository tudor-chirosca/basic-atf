package com.vocalink.crossproduct.infrastructure.adapter;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseAdapter<I, O> {
  private ModelMapper modelMapper;
  private final Type outputType = ((ParameterizedType) getClass().getGenericSuperclass())
      .getActualTypeArguments()[1];

  protected O toEntity(I input) {
    return modelMapper.map(input, outputType);
  }

  @Autowired
  public void setModelMapper(ModelMapper modelMapper){
    this.modelMapper = modelMapper;
  }
}
