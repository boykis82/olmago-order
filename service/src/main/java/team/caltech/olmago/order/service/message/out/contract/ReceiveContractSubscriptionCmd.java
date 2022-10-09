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
public class ReceiveContractSubscriptionCmd {
  private long customerId;
  private long orderId;
  private String pkgProdCd;
  private String optProdCd;
  private List<String> unitProdCds = new ArrayList<>();
  private LocalDateTime subRcvDtm;
}
