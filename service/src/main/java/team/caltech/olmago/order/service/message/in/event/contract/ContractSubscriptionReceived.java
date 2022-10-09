package team.caltech.olmago.order.service.message.in.event.contract;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ContractSubscriptionReceived {
  private final long contractId;
  private final LocalDateTime eventOccurDtm;
  private final long orderId;
  private final String feeProductCode;
  private final List<String> productCodes;
}
