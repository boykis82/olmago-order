package team.caltech.olmago.order.service.message.in.event.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractSubscriptionReceived {
  private long contractId;
  private LocalDateTime eventOccurDtm;
  private long orderId;
  private String feeProductCode;
}
