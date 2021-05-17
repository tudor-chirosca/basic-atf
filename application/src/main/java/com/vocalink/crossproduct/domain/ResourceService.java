package com.vocalink.crossproduct.domain;

import java.io.IOException;
import java.io.OutputStream;

public interface ResourceService extends CrossproductRepository {

  void writeResourceToOutputStream(String path, String id, OutputStream outputStream) throws IOException;

}
