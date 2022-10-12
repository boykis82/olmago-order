package team.caltech.olmago.order.service.message.out.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CancelContractSubscriptionCmd {
  private long orderId;
  private LocalDateTime cancelSubscriptionReceiptDateTime;
}
