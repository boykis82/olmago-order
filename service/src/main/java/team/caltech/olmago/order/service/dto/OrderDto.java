package team.caltech.olmago.order.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.caltech.olmago.order.domain.Order;
import team.caltech.olmago.order.domain.OrderDetail;
import team.caltech.olmago.order.service.config.ObjectMapperConfig;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderDto {
  @Getter
  @NoArgsConstructor
  public static class OrderDetailDto {
    private Long id;
    private Long contractId;
    private String productCode;
    private String contractType;
    @JsonFormat(pattern = ObjectMapperConfig.DATETIME_FORMATTER)
    private LocalDateTime receiptRequestDtm;
    @JsonFormat(pattern = ObjectMapperConfig.DATETIME_FORMATTER)
    private LocalDateTime receiptCompletedDtm;
    @JsonFormat(pattern = ObjectMapperConfig.DATETIME_FORMATTER)
    private LocalDateTime completedDtm;
    @JsonFormat(pattern = ObjectMapperConfig.DATETIME_FORMATTER)
    private LocalDateTime cancelRequestDtm;
    @JsonFormat(pattern = ObjectMapperConfig.DATETIME_FORMATTER)
    private LocalDateTime canceledDtm;
    
    public OrderDetailDto(OrderDetail entity) {
      this.id = entity.getId();
      this.contractId = entity.getContractId();
      this.productCode = entity.getProductCode();
      this.contractType = entity.getContractType().name();
      this.receiptRequestDtm = entity.getReceiptRequestDtm();
      this.receiptCompletedDtm = entity.getReceiptCompletedDtm();
      this.completedDtm = entity.getCompletedDtm();
      this.cancelRequestDtm = entity.getCancelRequestDtm();
      this.canceledDtm = entity.getCanceledDtm();
    }
  }
  
  private long id;
  private String orderType;
  private long customerId;
  @JsonFormat(pattern = ObjectMapperConfig.DATETIME_FORMATTER)
  private LocalDateTime receiptRequestDtm;
  @JsonFormat(pattern = ObjectMapperConfig.DATETIME_FORMATTER)
  private LocalDateTime receiptCompletedDtm;
  @JsonFormat(pattern = ObjectMapperConfig.DATETIME_FORMATTER)
  private LocalDateTime completedDtm;
  @JsonFormat(pattern = ObjectMapperConfig.DATETIME_FORMATTER)
  private LocalDateTime cancelRequestDtm;
  @JsonFormat(pattern = ObjectMapperConfig.DATETIME_FORMATTER)
  private LocalDateTime canceledDtm;
  private Long paymentId;
  private List<OrderDetailDto> orderDetails;
  
  public OrderDto(Order entity) {
    this.id = entity.getId();
    this.orderType = entity.getOrderType().name();
    this.customerId = entity.getCustomerId();
    this.receiptRequestDtm = entity.getReceiptRequestDtm();
    this.receiptCompletedDtm = entity.getReceiptCompletedDtm();
    this.completedDtm = entity.getCompletedDtm();
    this.cancelRequestDtm = entity.getCancelRequestDtm();
    this.canceledDtm = entity.getCanceledDtm();
    this.paymentId = entity.getPaymentId();
    this.orderDetails = entity.getOrderDetails().stream()
        .map(OrderDetailDto::new)
        .collect(Collectors.toList());
  }
}
