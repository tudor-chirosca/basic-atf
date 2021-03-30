package com.vocalink.crossproduct.infrastructure.jpa.activities;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "user_activity")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserActivityJpa implements Serializable {

  private static final String serialVersionUID = "67db453b-7909-486d-b8cd-217d74e7d8b0";

  @Id
  @Type(type="uuid-char")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  @Column(name = "name", nullable = false)
  private String name;
  @Column(name = "description", nullable = false)
  private String description;
}
