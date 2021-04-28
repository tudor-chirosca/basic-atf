package com.vocalink.crossproduct.ui.config;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum ControllerFeatures implements Feature {

  @Label("Enable/disable files download")
  FILE_DOWNLOAD;

  public boolean isActive() {
    return FeatureContext.getFeatureManager().isActive(this);
  }
}
