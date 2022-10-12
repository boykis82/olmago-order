package team.caltech.olmago.order.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(
    name = "ord",
    indexes = {
        @Index(name = "ord_n1", columnList = "cust_id, id DESC")
    }
)
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
  
  @Column(name = "rcv_req_dtm", nullable = false)
  private LocalDateTime receiptRequestDtm;
  
  @Column(name = "rcv_cmpl_dtm")
  private LocalDateTime receiptCompletedDtm;
  
  @Column(name = "cmpl_dtm")
  private LocalDateTime completedDtm;
  
  @Column(name = "cncl_req_dtm")
  private LocalDateTime cancelRequestDtm;
  
  @Column(name = "cncl_dtm")
  private LocalDateTime canceledDtm;
  
  @Column(name = "pay_id")
  private Long paymentId;
  
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
  private List<OrderDetail> orderDetails = new ArrayList<>();
  
  @Builder
  public Order(long customerId, LocalDateTime receiptRequestDtm, OrderType orderType) {
    this.customerId = customerId;
    this.receiptRequestDtm = receiptRequestDtm;
    this.orderType = orderType;
  }
  
  // 오더 접수 요청 후 각 마이크로서비스에서 완료하면 접수완료 이벤트 받아서 처리 (주문상세에 계약id매핑 등)
  public void completeDetailReceipt(String productCode, long contractId, LocalDateTime orderReceiptCompletedDtm) {
    // 건별 접수완료하고
    orderDetails.stream()
        .filter(od -> od.getProductCode().equals(productCode))
        .findAny()
        .orElseThrow(IllegalStateException::new)
        .completeReceipt(contractId, orderReceiptCompletedDtm);
    
    // 모두 접수완료되면 전체 접수완료
    if (orderDetails.stream().allMatch(OrderDetail::receiptCompleted)) {
      this.receiptCompletedDtm = orderReceiptCompletedDtm;
    }
  }
  
  public void completeDetail(long contractId, LocalDateTime orderCompletedDtm) {
    // 건별 완료하고
    orderDetails.stream()
        .filter(od -> od.getContractId().equals(contractId))
        .findAny()
        .orElseThrow(IllegalStateException::new)
        .complete(orderCompletedDtm);
    
    // 모두 완료되면 전체 완료
    if (orderDetails.stream().allMatch(OrderDetail::completed)) {
      this.completedDtm = orderCompletedDtm;
    }
  }
  
  public void requestCancellation(LocalDateTime orderCancelRequestDtm) {
    this.cancelRequestDtm = orderCancelRequestDtm;
    orderDetails.forEach(od -> od.requestCancellation(orderCancelRequestDtm));
  }
  
  public void completeDetailCancellation(long contractId, LocalDateTime orderCanceledDtm) {
    // 건별 취소하고
    orderDetails.stream()
        .filter(od -> od.getContractId().equals(contractId))
        .findAny()
        .orElseThrow(IllegalStateException::new)
        .cancel(orderCanceledDtm);
  
    // 모두 취소되면 전체 취소
    if (orderDetails.stream().allMatch(OrderDetail::canceled)) {
      this.canceledDtm = orderCanceledDtm;
    }
  }

  public boolean completed() {
    return this.completedDtm != null;
  }
  
  public boolean canceled() {
    return this.canceledDtm != null;
  }
  
  public boolean cancelling() {
    return this.cancelRequestDtm != null && this.canceledDtm == null;
  }

  public void addOrderDetails(List<OrderDetail> orderDetails) {
    this.orderDetails = orderDetails;
  }

}
