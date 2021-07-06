package com.vocalink.crossproduct.ui.dto.transaction;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.ui.validations.NotEqual;
import com.vocalink.crossproduct.ui.validations.ValidCycleOrDateRange;
import com.vocalink.crossproduct.ui.validations.ValidFromDate;
import com.vocalink.crossproduct.ui.validations.ValidLimit;
import com.vocalink.crossproduct.ui.validations.ValidRegexSearch;
import com.vocalink.crossproduct.ui.validations.ValidSort;
import com.vocalink.crossproduct.ui.validations.ValidStatus;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.ToString;

@Getter
@ValidStatus(status = "status", reasonCode = "reasonCode", statuses = {"PRE-RJCT", "POST-RJCT"})
@ValidSort(sort = "sort", sortingKeys =
    {"instructionId", "createdAt", "senderBic", "messageType", "amount", "status"})
@ValidCycleOrDateRange(cycleId = "cycleId", dateFrom = "dateFrom", dateTo = "dateTo")
@ToString
public class TransactionEnquirySearchRequest {

  private final int offset;
  @ValidLimit
  private final int limit;
  private final List<String> sort;
  @ValidFromDate
  private final ZonedDateTime dateFrom;
  private final ZonedDateTime dateTo;
  private final String cycleId;
  private final String messageType;
  private final String sendingBic;
  private final String receivingBic;
  @Pattern(regexp="[A-Z]{6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3})?", message= "Invalid debtor regex!")
  private final String debtor;
  @Pattern(regexp="[A-Z]{6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3})?", message= "Invalid creditor regex!")
  private final String creditor;
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
      @JsonProperty(value = "cycleId") String cycleId,
      @JsonProperty(value = "messageType") String messageType,
      @JsonProperty(value = "sendingBic") String sendingBic,
      @JsonProperty(value = "receivingBic") String receivingBic,
      @JsonProperty(value = "debtor") String debtor,
      @JsonProperty(value = "creditor") String creditor,
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
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.cycleId = cycleId;
    this.debtor = debtor;
    this.creditor = creditor;
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
