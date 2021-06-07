package com.vocalink.crossproduct.infrastructure.jpa.info;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_SCHEMA_VERSION")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DatabaseInfo {

  @Id
  @Column(name = "version", nullable = false)
  private String version;

  @Column(name = "release_date", nullable = false)
  private String releaseDate;
}
