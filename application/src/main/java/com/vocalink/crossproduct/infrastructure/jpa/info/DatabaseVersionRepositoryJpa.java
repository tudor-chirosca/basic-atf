package com.vocalink.crossproduct.infrastructure.jpa.info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DatabaseVersionRepositoryJpa extends JpaRepository<DatabaseInfo, String> {

  @Query("select di from DatabaseInfo di where di.releaseDate = (select max(di.releaseDate) from di)")
  DatabaseInfo getLatestInfo();
}
