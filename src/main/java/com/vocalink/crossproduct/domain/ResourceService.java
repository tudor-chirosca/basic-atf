package com.vocalink.crossproduct.domain;

import java.io.IOException;
import java.io.InputStream;

public interface ResourceService extends CrossproductRepository {

  InputStream getResource(String path, String id) throws IOException;
}
