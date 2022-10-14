package team.caltech.olmago.order.service.message.in.event.contract;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class ContractSubscriptionCompleted  {
  private long contractId;
  private LocalDateTime eventOccurDtm;
  private long orderId;
  private List<String> productCodes;
}
