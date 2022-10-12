package team.caltech.olmago.order.service.message.out.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class ReceiveContractSubscriptionCmd {
  @Getter
  @AllArgsConstructor
  public static class Product {
    String prodCd;
  }
  private long customerId;
  private long orderId;
  private String pkgProdCd;
  private String optProdCd;
  private List<Product> unitProds;
  private LocalDateTime subRcvDtm;
  
  @Builder
  public ReceiveContractSubscriptionCmd(long customerId, long orderId, String pkgProdCd, String optProdCd, List<String> unitProdCds, LocalDateTime subRcvDtm) {
    this.customerId = customerId;
    this.orderId = orderId;
    this.pkgProdCd = pkgProdCd;
    this.optProdCd = optProdCd;
    this.unitProds = unitProdCds.stream().map(Product::new).collect(Collectors.toList());
    this.subRcvDtm = subRcvDtm;
  }
}
