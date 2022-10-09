package team.caltech.olmago.order.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.caltech.olmago.order.common.message.MessageEnvelope;
import team.caltech.olmago.order.domain.Order;
import team.caltech.olmago.order.domain.OrderDetail;
import team.caltech.olmago.order.domain.OrderRepository;
import team.caltech.olmago.order.domain.OrderType;
import team.caltech.olmago.order.service.dto.CancelOrderDto;
import team.caltech.olmago.order.service.dto.ChangeOrderDto;
import team.caltech.olmago.order.service.dto.SubscriptionOrderDto;
import team.caltech.olmago.order.service.dto.TerminationOrderDto;
import team.caltech.olmago.order.service.message.out.MessageStore;
import team.caltech.olmago.order.service.message.out.contract.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final MessageStore messageStore;
  
  public static final String CONTRACT_COMMAND_BINDING = "contract-command-0";

  @Transactional
  public void mapWithOrderDetail(long orderId, String productCode, long contractId) {
    orderRepository.findById(orderId)
        .orElseThrow(RuntimeException::new)
        .mapOrderDetailWithContract(productCode, contractId);
  }
  
  @Transactional
  public void completeOrder(long orderId, LocalDateTime eventOccurDtm) {
    orderRepository.findById(orderId)
        .orElseThrow(RuntimeException::new)
        .complete(eventOccurDtm);
  }

  @Transactional
  public long orderSubscription(SubscriptionOrderDto dto) {
    Order order = createSubscriptionOrder(dto);
    orderRepository.save(order);
    messageStore.saveMessage(wrapCommand(createSubscriptionCommand(dto, order)));
    return order.getId();
  }
  
  private Order createSubscriptionOrder(SubscriptionOrderDto dto) {
    Order order = Order.builder()
        .orderType(OrderType.RECEIVE_SUBSCRIPTION)
        .orderReceivedDtm(LocalDateTime.now())
        .customerId(dto.getCustomerId())
        .build();
    
    List<OrderDetail> orderDetails = new ArrayList<>();
    if (dto.getPkgProdCd() != null && !dto.getPkgProdCd().isEmpty()) {
      orderDetails.add(OrderDetail.builder().order(order).productCode(dto.getPkgProdCd()).build());
    }
    if (dto.getOptProdCd() != null && !dto.getOptProdCd().isEmpty()) {
      orderDetails.add(OrderDetail.builder().order(order).productCode(dto.getOptProdCd()).build());
    }
    orderDetails.addAll(
        dto.getUnitProdCds().stream()
            .map(pc -> OrderDetail.builder().order(order).productCode(pc).build())
            .collect(Collectors.toList())
    );
    order.addOrderDetails(orderDetails);
    return order;
  }
  
  private static ReceiveContractSubscriptionCmd createSubscriptionCommand(SubscriptionOrderDto dto, Order order) {
    return ReceiveContractSubscriptionCmd.builder()
        .orderId(order.getId())
        .customerId(order.getCustomerId())
        .optProdCd(dto.getOptProdCd())
        .pkgProdCd(dto.getPkgProdCd())
        .subRcvDtm(order.getOrderReceivedDtm())
        .unitProdCds(Collections.unmodifiableList(dto.getUnitProdCds()))
        .build();
  }
  
  @Transactional
  public void cancelOrder(CancelOrderDto dto) {
    Order order = orderRepository.findOrderWithDetailsById(dto.getOrderId())
        .orElseThrow(RuntimeException::new);
    LocalDateTime cnclDtm = LocalDateTime.now();
    order.cancel(cnclDtm);
    
    if (order.getOrderType() == OrderType.RECEIVE_TERMINATION) {
      messageStore.saveMessage(wrapCommand(CancelContractTerminationCmd.builder().orderId(order.getId()).cancelTerminationReceiptDateTime(cnclDtm).build()));
    }
    else if (order.getOrderType() == OrderType.RECEIVE_CHANGE_PRODUCT) {
      messageStore.saveMessage(wrapCommand(CancelContractChangeCmd.builder().orderId(order.getId()).canceledChangeReceiptDateTime(cnclDtm).build()));
    }
  }
  
  @Transactional
  public long orderChange(ChangeOrderDto dto) {
    Order order = createChangeOrder(dto);
    orderRepository.save(order);
    messageStore.saveMessage(wrapCommand(createChangeCommand(dto, order)));
    return order.getId();
  }
  
  private Order createChangeOrder(ChangeOrderDto dto) {
    Order order = Order.builder()
        .orderType(OrderType.RECEIVE_CHANGE_PRODUCT)
        .orderReceivedDtm(LocalDateTime.now())
        .customerId(dto.getCustomerId())
        .build();
    
    List<OrderDetail> orderDetails = new ArrayList<>();
    if (dto.isChangingPackageContract()) {
      orderDetails.add(OrderDetail.builder().order(order).productCode(dto.getAfterPackageProductCode()).contractId(dto.getPackageContractId()).build());
    }
    if (dto.isChangingOptionContract()) {
      orderDetails.add(OrderDetail.builder().order(order).productCode(dto.getAfterOptionProductCode()).contractId(dto.getOptionContractId()).build());
    }
    order.addOrderDetails(orderDetails);
    return order;
  }
  
  private static ReceiveContractChangeCmd createChangeCommand(ChangeOrderDto dto, Order order) {
    return ReceiveContractChangeCmd.builder()
        .orderId(order.getId())
        .customerId(order.getCustomerId())
        .optionContractId(dto.getOptionContractId())
        .beforeOptionProductCode(dto.getBeforeOptionProductCode())
        .afterOptionProductCode(dto.getAfterOptionProductCode())
        .packageContractId(dto.getPackageContractId())
        .beforePackageProductCode(dto.getBeforePackageProductCode())
        .afterPackageProductCode(dto.getAfterPackageProductCode())
        .changeReceivedDateTime(order.getOrderReceivedDtm())
        .keepingBeforeContract(dto.isKeepingBeforeContract())
        .build();
  }
  
  @Transactional
  public long orderTermination(TerminationOrderDto dto) {
    Order order = createTerminationOrder(dto);
    orderRepository.save(order);
    messageStore.saveMessage(wrapCommand(createTerminationCommand(dto, order)));
    return order.getId();
  }
  
  private static Order createTerminationOrder(TerminationOrderDto dto) {
    Order order = Order.builder()
        .orderType(OrderType.RECEIVE_TERMINATION)
        .orderReceivedDtm(LocalDateTime.now())
        .customerId(dto.getCustomerId())
        .build();
    
    List<OrderDetail> orderDetails = new ArrayList<>();
    if (dto.getPackageContractId() != null) {
      orderDetails.add(OrderDetail.builder().order(order).contractId(dto.getPackageContractId()).build());
    }
    if (dto.getOptionContractId() != null) {
      orderDetails.add(OrderDetail.builder().order(order).contractId(dto.getOptionContractId()).build());
    }
    orderDetails.addAll(
        dto.getUnitContractIds().stream()
            .map(id -> OrderDetail.builder().order(order).contractId(id).build())
            .collect(Collectors.toList())
    );
    order.addOrderDetails(orderDetails);
    return order;
  }
  
  private static ReceiveContractTerminationCmd createTerminationCommand(TerminationOrderDto dto, Order order) {
    return ReceiveContractTerminationCmd.builder()
        .orderId(order.getId())
        .optionContractId(dto.getOptionContractId())
        .packageContractId(dto.getPackageContractId())
        .terminationReceivedDateTime(order.getOrderReceivedDtm())
        .unitContractIds(Collections.unmodifiableList(dto.getUnitContractIds()))
        .build();
  }
  
  private MessageEnvelope wrapCommand(Object e) {
    try {
      return MessageEnvelope.wrapCommand(CONTRACT_COMMAND_BINDING, e.getClass().getSimpleName(), e);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }
}
