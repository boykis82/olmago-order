package team.caltech.olmago.order.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "order_dtl")
public class OrderDetail {
  enum ContractType {
    PACKAGE,
    OPTION,
    UNIT
  }
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Version
  private int version;
  
  @Column(name = "contract_id")
  private Long contractId;
  
  @Column(name = "product_code", length= 10)
  private String productCode;
  
  @Column(name = "contract_type", length = 10)
  @Enumerated(EnumType.STRING)
  private ContractType contractType;
  
  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;
  
  @Builder
  public OrderDetail(Order order, String productCode, ContractType contractType,  Long contractId) {
    this.order = order;
    this.productCode = productCode;
    this.contractType = contractType;
    this.contractId = contractId;
  }
  
  public void mapContractId(long contractId) {
    this.contractId = contractId;
  }
}
