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
  
  // 계약 가입접수 완료 시 받는 이벤트
  @Transactional
  public void contractSubscriptionReceived(Message<?> message, ContractSubscriptionReceived event) {
    if (messageInBoxProcessor.notExistedMessage(message)) {
      messageInBoxProcessor.saveInBoxMessage(message);
      orderService.completeOrderDetailReceipt(event.getOrderId(), event.getFeeProductCode(), event.getContractId(), event.getEventOccurDtm());
    }
  }
  
  // 계약 가입접수취소 완료 시 받는 이벤트
  @Transactional
  public void contractSubscriptionReceiptCanceled(Message<?> message, ContractSubscriptionReceiptCanceled event) {
    if (messageInBoxProcessor.notExistedMessage(message)) {
      messageInBoxProcessor.saveInBoxMessage(message);
      orderService.completeOrderDetailCancellation(event.getOrderId(), event.getContractId(), event.getEventOccurDtm());
    }
  }
  
  // 계약 가입완료 시 받는 이벤트
  @Transactional
  public void contractSubscriptionCompleted(Message<?> message, ContractSubscriptionCompleted event) {
    if (messageInBoxProcessor.notExistedMessage(message)) {
      messageInBoxProcessor.saveInBoxMessage(message);
      orderService.completeOrderDetail(event.getOrderId(), event.getContractId(), event.getEventOccurDtm());
    }
  }

  // 계약 변경완료 시 받는 이벤트
  @Transactional
  public void contractChanged(Message<?> message, ContractChanged event) {
    if (messageInBoxProcessor.notExistedMessage(message)) {
      messageInBoxProcessor.saveInBoxMessage(message);
      orderService.completeOrderDetail(event.getOrderId(), event.getContractId(), event.getEventOccurDtm());
    }
  }
  
  // 계약 변경취소 완료 시 받는 이벤트
  @Transactional
  public void contractChangeCanceled(Message<?> message, ContractChangeCanceled event) {
    if (messageInBoxProcessor.notExistedMessage(message)) {
      messageInBoxProcessor.saveInBoxMessage(message);
      orderService.completeOrderDetailCancellation(event.getOrderId(), event.getContractId(), event.getEventOccurDtm());
    }
  }
  
  // 계약 해지완료 시 받는 이벤트
  @Transactional
  public void contractTerminationCompleted(Message<?> message, ContractTerminationCompleted event) {
    if (messageInBoxProcessor.notExistedMessage(message)) {
      messageInBoxProcessor.saveInBoxMessage(message);
      orderService.completeOrderDetail(event.getOrderId(), event.getContractId(), event.getEventOccurDtm());
    }
  }
  
  // 계약 해지접수 완료 시 받는 이벤트
  @Transactional
  public void contractTerminationReceived(Message<?> message, ContractTerminationReceived event) {
    if (messageInBoxProcessor.notExistedMessage(message)) {
      messageInBoxProcessor.saveInBoxMessage(message);
      orderService.completeOrderDetail(event.getOrderId(), event.getContractId(), event.getEventOccurDtm());
    }
  }
  
  // 계약 해지접수취소 완료 시 받는 이벤트
  @Transactional
  public void contractTerminationReceiptCanceled(Message<?> message, ContractTerminationReceiptCanceled event) {
    if (messageInBoxProcessor.notExistedMessage(message)) {
      messageInBoxProcessor.saveInBoxMessage(message);
      orderService.completeOrderDetailCancellation(event.getOrderId(), event.getContractId(), event.getEventOccurDtm());
    }
  }
}
