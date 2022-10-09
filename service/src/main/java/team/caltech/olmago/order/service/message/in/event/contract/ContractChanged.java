package team.caltech.olmago.order.service.message.in.event.contract;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class ContractChanged {
  private final long contractId;
  private final LocalDateTime eventOccurDtm;
  private final long orderId;
}
