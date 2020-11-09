package com.vocalink.crossproduct.infrastructure.adapter;

import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.shared.alert.CPAlert;
import com.vocalink.crossproduct.shared.alert.CPAlertReferenceData;
import com.vocalink.crossproduct.shared.alert.CPAlertStats;
import com.vocalink.crossproduct.shared.cycle.CPCycle;
import com.vocalink.crossproduct.shared.files.CPFileReference;
import com.vocalink.crossproduct.shared.io.CPIODetails;
import com.vocalink.crossproduct.shared.io.CPParticipantIOData;
import com.vocalink.crossproduct.shared.participant.CPParticipant;
import com.vocalink.crossproduct.shared.positions.CPIntraDayPositionGross;
import com.vocalink.crossproduct.shared.positions.CPPositionDetails;
import com.vocalink.crossproduct.shared.reference.CPMessageDirectionReference;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityMapper {

  EntityMapper MAPPER = Mappers.getMapper(EntityMapper.class);

  Cycle toEntity(CPCycle input);

  IntraDayPositionGross toEntity(CPIntraDayPositionGross input);

  IODetails toEntity(CPIODetails input);

  ParticipantIOData toEntity(CPParticipantIOData input);

  PositionDetails toEntity(CPPositionDetails input);

  Participant toEntity(CPParticipant input);

  FileReference toEntity(CPFileReference cpFileReference);

  AlertReferenceData toEntity(CPAlertReferenceData alertReferenceData);

  Alert toEntity(CPAlert input);

  AlertStats toEntity(CPAlertStats alertReferenceData);

  MessageDirectionReference toEntity(CPMessageDirectionReference alertReferenceData);

}
