package com.vocalink.crossproduct.infrastructure.jpa.permission;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "UI_PERMISSION")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UIPermissionJpa implements Serializable {

  @Id
  @NonNull
  @Column(name = "id", nullable = false)
  private String id;

  @NonNull
  @Column(name = "key", nullable = false)
  private String key;
}
