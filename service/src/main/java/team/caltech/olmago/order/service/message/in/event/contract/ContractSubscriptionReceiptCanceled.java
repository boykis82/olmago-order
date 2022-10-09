package team.caltech.olmago.order.service.message.in.event.contract;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ContractSubscriptionReceiptCanceled  {
  private final long contractId;
  private final LocalDateTime eventOccurDtm;
  private final long orderId;
}
