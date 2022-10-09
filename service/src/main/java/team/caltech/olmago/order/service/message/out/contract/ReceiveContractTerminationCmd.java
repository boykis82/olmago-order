package team.caltech.olmago.order.service.message.out.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReceiveContractTerminationCmd {
  private long orderId;
  private LocalDateTime terminationReceivedDateTime;
  private Long packageContractId;
  private Long optionContractId;
  private List<Long> unitContractIds = new ArrayList<>();
}
