package team.caltech.olmago.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SubscriptionOrderDto {
  private long customerId;
  private String pkgProdCd;
  private String optProdCd;
  private List<String> unitProdCds = new ArrayList<>();
}
