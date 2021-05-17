package com.vocalink.crossproduct.ui.dto.transaction;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.DAYS_LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.ui.validations.NotEqual;
import com.vocalink.crossproduct.ui.validations.ValidDirection;
import com.vocalink.crossproduct.ui.validations.ValidFromDate;
import com.vocalink.crossproduct.ui.validations.ValidLimit;
import com.vocalink.crossproduct.ui.validations.ValidRegexSearch;
import com.vocalink.crossproduct.ui.validations.ValidSort;
import com.vocalink.crossproduct.ui.validations.ValidStatus;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;

@Getter
@NotEqual(first = "sendingBic", second = "receivingBic", message = "send_bic and recv_bic should not be the same")
@ValidStatus(status = "status", reasonCode = "reasonCode", statuses = {"PRE-RJCT", "POST-RJCT"})
@ValidSort(sort = "sort", sortingKeys =
    {"instructionId", "createdAt", "senderBic", "messageType", "amount", "status"})
public class TransactionEnquirySearchRequest {

  private final int offset;
  @ValidLimit
  private final int limit;
  private final List<String> sort;
  @ValidFromDate
  private final ZonedDateTime dateFrom;
  private final ZonedDateTime dateTo;
  private final ZonedDateTime cycleDay;
  private final String cycleName;
  @ValidDirection
  private final String messageDirection;
  private final String messageType;
  private final String sendingBic;
  private final String receivingBic;
  private final String status;
  private final String reasonCode;
  @ValidRegexSearch(regExp = "^(\\*?)[a-zA-Z0-9_.]+(\\*?)$")
  private final String id;
  private final String sendingAccount;
  private final String receivingAccount;
  private final ZonedDateTime valueDate;
  private final BigDecimal txnFrom;
  private final BigDecimal txnTo;

  @JsonCreator
  public TransactionEnquirySearchRequest(
      @JsonProperty(value = "offset") Integer offset,
      @JsonProperty(value = "limit") Integer limit,
      @JsonProperty(value = "sort") List<String> sort,
      @JsonProperty(value = "dateFrom") ZonedDateTime dateFrom,
      @JsonProperty(value = "dateTo") ZonedDateTime dateTo,
      @JsonProperty(value = "cycleDay") ZonedDateTime cycleDay,
      @JsonProperty(value = "cycleName") String cycleName,
      @JsonProperty(value = "messageDirection") String messageDirection,
      @JsonProperty(value = "messageType") String messageType,
      @JsonProperty(value = "sendingBic") String sendingBic,
      @JsonProperty(value = "receivingBic") String receivingBic,
      @JsonProperty(value = "status") String status,
      @JsonProperty(value = "reasonCode") String reasonCode,
      @JsonProperty(value = "id") String id,
      @JsonProperty(value = "sendingAccount") String sendingAccount,
      @JsonProperty(value = "receivingAccount") String receivingAccount,
      @JsonProperty(value = "valueDate") ZonedDateTime valueDate,
      @JsonProperty(value = "txnFrom") BigDecimal txnFrom,
      @JsonProperty(value = "txnTo") BigDecimal txnTo) {

    this.offset = offset == null ? parseInt(getDefault(OFFSET)) : offset;
    this.limit = limit == null ? parseInt(getDefault(LIMIT)) : limit;
    this.dateFrom = dateFrom == null
        ? ZonedDateTime.now(ZoneId.of("UTC")).minusDays(parseLong(getDefault(DAYS_LIMIT))) : dateFrom;
    this.dateTo = dateTo;
    this.cycleDay = cycleDay;
    this.cycleName = cycleName;
    this.messageDirection = messageDirection;
    this.messageType = messageType;
    this.sendingBic = sendingBic;
    this.receivingBic = receivingBic;
    this.status = status;
    this.reasonCode = reasonCode;
    this.id = id;
    this.sendingAccount = sendingAccount;
    this.receivingAccount = receivingAccount;
    this.valueDate = valueDate;
    this.txnFrom = txnFrom;
    this.txnTo = txnTo;
    this.sort = sort;
  }
}
