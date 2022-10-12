package team.caltech.olmago.order.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(
    name = "ord_dtl",
    indexes = {
        @Index(name = "ord_dtl_n1", columnList = "order_id, id")
    }
)
public class OrderDetail {
  
  public enum ContractType {
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
  
  @Column(name = "product_code", length= 10, nullable = false)
  private String productCode;
  
  @Column(name = "contract_type", length = 10, nullable = false)
  @Enumerated(EnumType.STRING)
  private ContractType contractType;
  
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

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;
  
  @Builder
  public OrderDetail(Order order, String productCode, ContractType contractType, LocalDateTime receiptRequestDtm, Long contractId) {
    this.order = order;
    this.productCode = productCode;
    this.contractType = contractType;
    this.receiptRequestDtm = receiptRequestDtm;
    this.contractId = contractId;
  }
  
  public void completeReceipt(long contractId, LocalDateTime receiptCompletedDtm) {
    this.contractId = contractId;
    this.receiptCompletedDtm = receiptCompletedDtm;
  }
  
  public void complete(LocalDateTime orderDetailCompletedDtm) {
    this.completedDtm = orderDetailCompletedDtm;
  }
  
  public void requestCancellation(LocalDateTime orderDetailCancelRequestDtm) {
    this.cancelRequestDtm = orderDetailCancelRequestDtm;
  }
  
  public void cancel(LocalDateTime orderDetailCanceledDtm) {
    this.canceledDtm = orderDetailCanceledDtm;
  }
  
  public boolean receiptCompleted() {
    return this.receiptCompletedDtm != null;
  }
  
  public boolean completed() {
    return this.completedDtm != null;
  }
  
  public boolean canceled() {
    return this.canceledDtm != null;
  }

}
