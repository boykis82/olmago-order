package team.caltech.olmago.order.service.service;

import team.caltech.olmago.order.service.dto.CancelOrderDto;
import team.caltech.olmago.order.service.dto.ChangeOrderDto;
import team.caltech.olmago.order.service.dto.SubscriptionOrderDto;
import team.caltech.olmago.order.service.dto.TerminationOrderDto;

import java.time.LocalDateTime;

public interface OrderService {
  /* from mq */
  void mapWithOrderDetail(long orderId, String productCode, long contractId);
  void completeOrder(long orderId, LocalDateTime eventOccurDtm);

  /* from web */
  long orderSubscription(SubscriptionOrderDto dto);
  void cancelOrder(CancelOrderDto dto);
  long orderChange(ChangeOrderDto dto);
  long orderTermination(TerminationOrderDto dto);
}
