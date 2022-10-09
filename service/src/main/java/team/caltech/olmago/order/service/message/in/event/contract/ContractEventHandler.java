package team.caltech.olmago.order.service.message.in.event.contract;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import team.caltech.olmago.order.service.message.in.common.MessageInBoxProcessor;
import team.caltech.olmago.order.service.service.OrderService;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class ContractEventHandler {
  private final MessageInBoxProcessor messageInBoxProcessor;
  private final OrderService orderService;
  
  @Transactional
  public void contractSubscriptionReceived(Message<?> message, ContractSubscriptionReceived event) {
    if (messageInBoxProcessor.notExistedMessage(message)) {
      messageInBoxProcessor.saveInBoxMessage(message);
      orderService.mapWithOrderDetail(event.getOrderId(), event.getFeeProductCode(), event.getContractId());
    }
  }
  
  @Transactional
  public void contractSubscriptionCompleted(Message<?> message, ContractSubscriptionCompleted event) {
    if (messageInBoxProcessor.notExistedMessage(message)) {
      messageInBoxProcessor.saveInBoxMessage(message);
      orderService.completeOrder(event.getOrderId(), event.getEventOccurDtm());
    }
  }

  @Transactional
  public void contractChanged(Message<?> message, ContractChanged event) {
    if (messageInBoxProcessor.notExistedMessage(message)) {
      messageInBoxProcessor.saveInBoxMessage(message);
      orderService.completeOrder(event.getOrderId(), event.getEventOccurDtm());
    }
  }
  
  @Transactional
  public void contractTerminationCompleted(Message<?> message, ContractTerminationCompleted event) {
    if (messageInBoxProcessor.notExistedMessage(message)) {
      messageInBoxProcessor.saveInBoxMessage(message);
      orderService.completeOrder(event.getOrderId(), event.getEventOccurDtm());
    }
  }
}
