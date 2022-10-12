package team.caltech.olmago.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SubscriptionOrderDto {
  @Getter
  public static class Product {
    String prodCd;
  }
  
  private long customerId;
  private String pkgProdCd;
  private String optProdCd;
  private List<Product> unitProds;
}
