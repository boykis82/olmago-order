package team.caltech.olmago.order.service.message.in.event.contract;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ContractChanged {
  private long contractId;
  private LocalDateTime eventOccurDtm;
  private long orderId;
}
