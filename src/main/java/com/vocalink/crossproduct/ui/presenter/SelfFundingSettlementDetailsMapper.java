package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.ParticipantPosition;
import com.vocalink.crossproduct.domain.PositionDetails;
import com.vocalink.crossproduct.ui.dto.PositionDetailsTotalsDto;
import com.vocalink.crossproduct.ui.dto.PositionDetailsDto;
import com.vocalink.crossproduct.ui.dto.SelfFundingSettlementDetailsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelfFundingSettlementDetailsMapper {

  public SelfFundingSettlementDetailsDto presentFullSelfFundingSettlementDetails(
      List<Cycle> cycles, List<PositionDetails> positionsDetails, Participant participant) {

    Cycle currentCycle = cycles.get(1);
    Cycle previousCycle = cycles.get(0);

    PositionDetails currentPositionDetails = positionsDetails.get(1);
    PositionDetails previousPositionDetails = positionsDetails.get(0);

    ParticipantPosition currentCustomerCreditTransfer = currentPositionDetails
        .getCustomerCreditTransfer();
    ParticipantPosition currentPaymentReturn = currentPositionDetails.getPaymentReturn();

    ParticipantPosition previousCustomerCreditTransfer = previousPositionDetails
        .getCustomerCreditTransfer();
    ParticipantPosition previousPaymentReturn = previousPositionDetails.getPaymentReturn();

    PositionDetailsDto customerCreditTransfer = PositionDetailsDto.builder()
        .currentPosition(currentCustomerCreditTransfer.toDto())
        .previousPosition(previousCustomerCreditTransfer.toDto())
        .build();

    PositionDetailsTotalsDto currentPositionDetailsTotalsDto = PositionDetailsTotalsDto.builder()
        .totalCredit(
            currentCustomerCreditTransfer.getCredit().add(currentPaymentReturn.getCredit()))
        .totalDebit(currentCustomerCreditTransfer.getDebit().add(currentPaymentReturn.getDebit()))
        .totalNetPosition(currentCustomerCreditTransfer.getNetPosition()
            .add(currentPaymentReturn.getNetPosition()))
        .build();

    PositionDetailsTotalsDto previousPositionDetailsTotalsDto = PositionDetailsTotalsDto.builder()
        .totalCredit(
            previousCustomerCreditTransfer.getCredit().add(previousPaymentReturn.getCredit()))
        .totalDebit(previousCustomerCreditTransfer.getDebit().add(previousPaymentReturn.getDebit()))
        .totalNetPosition(previousCustomerCreditTransfer.getNetPosition()
            .add(previousPaymentReturn.getNetPosition()))
        .build();

    PositionDetailsDto paymentReturn = PositionDetailsDto.builder()
        .currentPosition(currentPaymentReturn.toDto())
        .previousPosition(previousPaymentReturn.toDto())
        .build();

    return SelfFundingSettlementDetailsDto.builder()
        .participant(participant.toDto())
        .currentCycle(currentCycle.toDto())
        .previousCycle(previousCycle.toDto())
        .customerCreditTransfer(customerCreditTransfer)
        .paymentReturn(paymentReturn)
        .currentPositionTotals(currentPositionDetailsTotalsDto)
        .previousPositionTotals(previousPositionDetailsTotalsDto)
        .build();
  }

  public SelfFundingSettlementDetailsDto presentOneCycleSelfFundingSettlementDetails(
      List<Cycle> cycles, List<PositionDetails> positionsDetails, Participant participant) {

    Cycle previousCycle = cycles.get(0);
    PositionDetails previousPositionDetails = positionsDetails.get(0);

    ParticipantPosition previousCustomerCreditTransfer = previousPositionDetails
        .getCustomerCreditTransfer();
    ParticipantPosition previousPaymentReturn = previousPositionDetails.getPaymentReturn();

    PositionDetailsDto customerCreditTransfer = PositionDetailsDto.builder()
        .previousPosition(previousCustomerCreditTransfer.toDto())
        .build();

    PositionDetailsTotalsDto previousPositionDetailsTotalsDto = PositionDetailsTotalsDto.builder()
        .totalCredit(
            previousCustomerCreditTransfer.getCredit().add(previousPaymentReturn.getCredit()))
        .totalDebit(previousCustomerCreditTransfer.getDebit().add(previousPaymentReturn.getDebit()))
        .totalNetPosition(previousCustomerCreditTransfer.getNetPosition()
            .add(previousPaymentReturn.getNetPosition()))
        .build();

    PositionDetailsDto paymentReturn = PositionDetailsDto.builder()
        .previousPosition(previousPaymentReturn.toDto())
        .build();

    return SelfFundingSettlementDetailsDto.builder()
        .participant(participant.toDto())
        .previousCycle(previousCycle.toDto())
        .customerCreditTransfer(customerCreditTransfer)
        .paymentReturn(paymentReturn)
        .previousPositionTotals(previousPositionDetailsTotalsDto)
        .build();
  }
}
