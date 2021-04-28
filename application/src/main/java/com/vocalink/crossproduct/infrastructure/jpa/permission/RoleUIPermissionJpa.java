package com.vocalink.crossproduct.infrastructure.jpa.permission;

import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.infrastructure.jpa.role.RoleJpa;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ROLE_UI_PERMISSION")
@EqualsAndHashCode
@NoArgsConstructor
public class RoleUIPermissionJpa implements Serializable {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  @Embeddable
  private static class RoleUIPermissionPK implements Serializable {
    @ManyToOne
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "FK_ROLE_ROLEUIPERMISSION"), nullable = false)
    private RoleJpa role;

    @ManyToOne
    @JoinColumn(name = "ui_permission_id", foreignKey = @ForeignKey(name = "FK_UIPERMISSION_ROLEUIPERMISSION"), nullable = false)
    private UIPermissionJpa uiPermission;

    @Enumerated(EnumType.STRING)
    @Column(name = "participant_type", nullable = false)
    private ParticipantType participantType;
  }

  @EmbeddedId
  private RoleUIPermissionPK id;

  public RoleUIPermissionJpa(RoleJpa role, UIPermissionJpa permission, ParticipantType participantType) {
    this.id = new RoleUIPermissionPK(role, permission, participantType);
  }

  public RoleJpa getRole(){
    return this.id.getRole();
  }

  public UIPermissionJpa getUIPermission(){
    return this.id.getUiPermission();
  }

  public ParticipantType getParticipantType(){
    return this.id.getParticipantType();
  }
}
