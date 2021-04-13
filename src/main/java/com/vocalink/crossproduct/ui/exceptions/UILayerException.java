package com.vocalink.crossproduct.ui.exceptions;

public class UILayerException extends RuntimeException {

  private final String location;


  public UILayerException(String message) {
    super(message);
    this.location = null;
  }

  public UILayerException(Throwable cause, String location) {
    super(cause);
    this.location = location;
  }

  public UILayerException(String message, Throwable cause, String location) {
    super(message, cause);
    this.location = location;
  }

  public String getLocation() {
    return this.location;
  }
}
