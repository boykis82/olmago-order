package team.caltech.olmago.order.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "order")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Version
  private int version;
  
  @Column(name = "ord_typ", length = 30, nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderType orderType;
  
  @Column(name = "cust_id", nullable = false)
  private long customerId;
  
  @Column(name = "ord_rcv_dtm", nullable = false)
  private LocalDateTime orderReceivedDtm;
  
  @Column(name = "ord_cmpl_dtm")
  private LocalDateTime orderCompletedDtm;
  
  @Column(name = "ord_cncl_dtm")
  private LocalDateTime orderCanceledDtm;
  
  @Column(name = "pay_id")
  private Long paymentId;
  
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
  private List<OrderDetail> orderDetails = new ArrayList<>();
  
  @Builder
  public Order(long customerId, LocalDateTime orderReceivedDtm, OrderType orderType) {
    this.customerId = customerId;
    this.orderReceivedDtm = orderReceivedDtm;
    this.orderType = orderType;
  }
  
  public void complete(LocalDateTime orderCompletedDtm) {
    this.orderCompletedDtm = orderCompletedDtm;
  }
  
  public void mapOrderDetailWithContract(String productCode, long contractId) {
    orderDetails.stream()
        .filter(od -> od.getProductCode().equals(productCode))
        .findAny()
        .orElseThrow(RuntimeException::new)
        .mapContractId(contractId);
  }
  
  public void mapWithPayment(long paymentId) {
    this.paymentId = paymentId;
  }
  
  public void cancel(LocalDateTime orderCanceledDtm) {
    this.orderCanceledDtm = orderCanceledDtm;
  }
  
  public void addOrderDetails(List<OrderDetail> orderDetails) {
    this.orderDetails = orderDetails;
  }
  
  public Long findPackageContractId() {
    return orderDetails.stream()
        .filter(od -> od.getContractType() == OrderDetail.ContractType.PACKAGE)
        .map(OrderDetail::getContractId)
        .findAny()
        .orElse(0L);
  }
  
  public Long findOptionContractId() {
    return orderDetails.stream()
        .filter(od -> od.getContractType() == OrderDetail.ContractType.OPTION)
        .map(OrderDetail::getContractId)
        .findAny()
        .orElse(0L);
  }
  
  public List<Long> findUnitContractIds() {
    return orderDetails.stream()
        .filter(od -> od.getContractType() == OrderDetail.ContractType.UNIT)
        .map(OrderDetail::getContractId)
        .collect(Collectors.toList());
  }
}
