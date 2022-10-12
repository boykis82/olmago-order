package team.caltech.olmago.order.service.service;

import team.caltech.olmago.order.service.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
  /* from mq */
  void completeOrderDetailReceipt(long orderId, String productCode, long contractId, LocalDateTime eventOccurDtm);
  void completeOrderDetail(long orderId, long contractId, LocalDateTime eventOccurDtm);
  void completeOrderDetailCancellation(long orderId, long contractId, LocalDateTime eventOccurDtm);
  
  /* from web */
  long orderSubscription(SubscriptionOrderDto dto);
  void orderCancellation(CancelOrderDto dto);
  long orderChange(ChangeOrderDto dto);
  long orderTermination(TerminationOrderDto dto);
  
  List<OrderDto> findOrdersByCustomer(long customerId);
}
