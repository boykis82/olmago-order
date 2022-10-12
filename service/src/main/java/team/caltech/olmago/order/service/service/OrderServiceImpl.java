package team.caltech.olmago.order.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.caltech.olmago.order.common.message.MessageEnvelope;
import team.caltech.olmago.order.domain.Order;
import team.caltech.olmago.order.domain.OrderDetail;
import team.caltech.olmago.order.domain.OrderRepository;
import team.caltech.olmago.order.domain.OrderType;
import team.caltech.olmago.order.service.dto.*;
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
  private final ObjectMapper objectMapper;
  
  public static final String CONTRACT_COMMAND_BINDING = "contract-command-0";

  // 가입 접수 완료
  @Transactional
  public void completeOrderDetailReceipt(long orderId, String productCode, long contractId, LocalDateTime eventOccurDtm) {
    orderRepository.findOrderWithDetailsById(orderId)
        .orElseThrow(IllegalStateException::new)
        .completeDetailReceipt(productCode, contractId, eventOccurDtm);
  }
  
  // 가입 완료
  @Transactional
  public void completeOrderDetail(long orderId, long contractId, LocalDateTime eventOccurDtm) {
    orderRepository.findOrderWithDetailsById(orderId)
        .orElseThrow(IllegalStateException::new)
        .completeDetail(contractId, eventOccurDtm);
  }

  // 가입 주문
  @Transactional
  public long orderSubscription(SubscriptionOrderDto dto) {
    Order order = createSubscriptionOrder(dto);
    orderRepository.save(order);
    messageStore.saveMessage(wrapCommand(createSubscriptionCommand(dto, order)));
    return order.getId();
  }
  
  private Order createSubscriptionOrder(SubscriptionOrderDto dto) {
    LocalDateTime now = LocalDateTime.now();
    Order order = Order.builder()
        .orderType(OrderType.RECEIVE_SUBSCRIPTION)
        .receiptRequestDtm(now)
        .customerId(dto.getCustomerId())
        .build();
    order.addOrderDetails(createOrderDetails(dto, order, now));
    return order;
  }
  
  private List<OrderDetail> createOrderDetails(SubscriptionOrderDto dto, Order order, LocalDateTime receiptRequestDtm) {
    List<OrderDetail> orderDetails = new ArrayList<>();
    if (dto.getPkgProdCd() != null && !dto.getPkgProdCd().isEmpty()) {
      orderDetails.add(OrderDetail.builder().order(order).productCode(dto.getPkgProdCd()).contractType(OrderDetail.ContractType.PACKAGE).receiptRequestDtm(receiptRequestDtm).build());
    }
    if (dto.getOptProdCd() != null && !dto.getOptProdCd().isEmpty()) {
      orderDetails.add(OrderDetail.builder().order(order).productCode(dto.getOptProdCd()).contractType(OrderDetail.ContractType.OPTION).receiptRequestDtm(receiptRequestDtm).build());
    }
    orderDetails.addAll(
        dto.getUnitProds().stream()
            .map(up -> OrderDetail.builder().order(order).productCode(up.getProdCd()).contractType(OrderDetail.ContractType.UNIT).receiptRequestDtm(receiptRequestDtm).build())
            .collect(Collectors.toList())
    );
    return orderDetails;
  }
  
  private ReceiveContractSubscriptionCmd createSubscriptionCommand(SubscriptionOrderDto dto, Order order) {
    return ReceiveContractSubscriptionCmd.builder()
        .orderId(order.getId())
        .customerId(order.getCustomerId())
        .optProdCd(dto.getOptProdCd())
        .pkgProdCd(dto.getPkgProdCd())
        .subRcvDtm(order.getReceiptRequestDtm())
        .unitProdCds(dto.getUnitProds().stream().map(SubscriptionOrderDto.Product::getProdCd).collect(Collectors.toList()))
        .build();
  }
  
  // 주문 취소 요청
  @Transactional
  public void orderCancellation(CancelOrderDto dto) {
    Order order = orderRepository.findById(dto.getOrderId()).orElseThrow(IllegalArgumentException::new);
    if (order.canceled() || order.completed() || order.cancelling()) {
      throw new IllegalStateException("주문이 이미 취소됐거나 완료됐습니다!");
    }
    LocalDateTime cnclReqDtm = LocalDateTime.now();
    order.requestCancellation(cnclReqDtm);
    messageStore.saveMessage(createCancelCommand(order, cnclReqDtm));
  }
  
  // 주문 취소 완료
  @Transactional
  public void completeOrderDetailCancellation(long orderId, long contractId, LocalDateTime eventOccurDtm) {
    Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
    if (!order.cancelling()) {
      throw new IllegalStateException("주문이 취소 요청 상태가 아닙니다.!");
    }
    order.completeDetailCancellation(contractId, eventOccurDtm);
  }
  
  private MessageEnvelope createCancelCommand(Order order, LocalDateTime cnclDtm) {
    if (order.getOrderType() == OrderType.RECEIVE_TERMINATION) {
      return wrapCommand(CancelContractTerminationCmd.builder().orderId(order.getId()).cancelTerminationReceiptDateTime(cnclDtm).build());
    }
    else if (order.getOrderType() == OrderType.RECEIVE_CHANGE_PRODUCT) {
      return wrapCommand(CancelContractChangeCmd.builder().orderId(order.getId()).canceledChangeReceiptDateTime(cnclDtm).build());
    }
    else if (order.getOrderType() == OrderType.RECEIVE_SUBSCRIPTION) {
      return wrapCommand(CancelContractSubscriptionCmd.builder().orderId(order.getId()).cancelSubscriptionReceiptDateTime(cnclDtm).build());
    }
    else {
      throw new IllegalStateException("알 수 없는 주문 유형입니다!");
    }
  }
  
  // 변경 주문
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
        .receiptRequestDtm(LocalDateTime.now())
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
  
  private ReceiveContractChangeCmd createChangeCommand(ChangeOrderDto dto, Order order) {
    return ReceiveContractChangeCmd.builder()
        .orderId(order.getId())
        .customerId(order.getCustomerId())
        .optionContractId(dto.getOptionContractId())
        .beforeOptionProductCode(dto.getBeforeOptionProductCode())
        .afterOptionProductCode(dto.getAfterOptionProductCode())
        .packageContractId(dto.getPackageContractId())
        .beforePackageProductCode(dto.getBeforePackageProductCode())
        .afterPackageProductCode(dto.getAfterPackageProductCode())
        .changeReceivedDateTime(order.getReceiptRequestDtm())
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

  private Order createTerminationOrder(TerminationOrderDto dto) {
    Order order = Order.builder()
        .orderType(OrderType.RECEIVE_TERMINATION)
        .receiptRequestDtm(LocalDateTime.now())
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
  
  private ReceiveContractTerminationCmd createTerminationCommand(TerminationOrderDto dto, Order order) {
    return ReceiveContractTerminationCmd.builder()
        .orderId(order.getId())
        .optionContractId(dto.getOptionContractId())
        .packageContractId(dto.getPackageContractId())
        .terminationReceivedDateTime(order.getReceiptRequestDtm())
        .unitContractIds(Collections.unmodifiableList(dto.getUnitContractIds()))
        .build();
  }
  
  @Override
  public List<OrderDto> findOrdersByCustomer(long customerId) {
    return orderRepository.findOrderWithDetailsByCustomer(customerId).stream().map(OrderDto::new).collect(Collectors.toList());
  }
  
  private MessageEnvelope wrapCommand(Object e) {
    try {
      return MessageEnvelope.wrapCommand(CONTRACT_COMMAND_BINDING, e.getClass().getSimpleName(), objectMapper.writeValueAsString(e));
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }
}
