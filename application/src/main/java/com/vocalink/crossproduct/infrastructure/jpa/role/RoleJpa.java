package com.vocalink.crossproduct.infrastructure.jpa.role;

import com.vocalink.crossproduct.domain.role.Role;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ROLE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RoleJpa implements Serializable {

  @Id
  private String id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role.Function function;
}
