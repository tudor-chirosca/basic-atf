package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.ParticipantReference;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDetailsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDto;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.reference.FileStatusesTypeDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import java.time.LocalDate;
import java.util.List;

public interface Presenter {

  SettlementDashboardDto presentAllParticipantsSettlement(
      List<Cycle> cycles,
      List<Participant> participants);

  SettlementDashboardDto presentFundingParticipantSettlement(
      List<Cycle> cycles,
      List<Participant> participants,
      Participant fundingParticipant,
      List<IntraDayPositionGross> intraDays);

  ParticipantSettlementDetailsDto presentParticipantSettlementDetails(List<Cycle> cycles,
      List<PositionDetails> positionsDetails,
      Participant participant,
      Participant fundingParticipant,
      IntraDayPositionGross intradayPositionGross);

  ClientType getClientType();

  IODashboardDto presentInputOutput(List<Participant> participants, List<ParticipantIOData> ioData,
      LocalDate date);

  IODetailsDto presentIoDetails(Participant participant, IODetails ioDetails,
      LocalDate date);

  AlertReferenceDataDto presentAlertReference(AlertReferenceData alertsReference);

  AlertStatsDto presentAlertStats(AlertStats alertStats);

  PageDto<AlertDto> presentAlert(Page<Alert> alerts);

  List<ParticipantReferenceDto> presentParticipantReferences(List<ParticipantReference> participants);

  List<FileStatusesTypeDto> presentFileReferencesFor(List<FileReference> fileReferences);

  List<MessageDirectionReferenceDto> presentMessageDirectionReferences(
      List<MessageDirectionReference> messageDirectionReferences);

  PageDto<FileDto> presentFiles(Page<File> files);

  List<CycleDto> presentCycleDateReferences(List<Cycle> cycles);

  FileDetailsDto presentFileDetails(File file);

  PageDto<BatchDto> presentBatches(Page<Batch> batches);

  BatchDetailsDto presentBatchDetails(Batch batch);

}
