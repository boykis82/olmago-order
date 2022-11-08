package team.caltech.olmago.order.service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.caltech.olmago.common.message.MessageEnvelope;
import team.caltech.olmago.common.message.MessageEnvelopeRepository;
import team.caltech.olmago.order.service.dto.*;
import team.caltech.olmago.order.service.service.OrderService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/olmago/orders/api/v1")
public class OrderController {
  private final OrderService orderService;
  private final MessageEnvelopeRepository messageEnvelopeRepository;
  
  @GetMapping
  public ResponseEntity<List<OrderDto>> findOrdersByCustomer(@RequestParam("customerId") long customerId) {
    return ResponseEntity.ok(orderService.findOrdersByCustomer(customerId));
  }
  
  @PostMapping("/subscribe")
  public ResponseEntity<Long> orderSubscription(@RequestBody SubscriptionOrderDto dto) {
    return ResponseEntity.ok(orderService.orderSubscription(dto));
  }
  
  @PutMapping("/{orderId}/cancel")
  public ResponseEntity<Void> cancelOrder(@PathVariable("orderId") long orderId, @RequestBody CancelOrderDto dto) {
    orderService.orderCancellation(dto);
    return ResponseEntity.ok().build();
  }
  
  @PostMapping("/change")
  public ResponseEntity<Long> orderChange(@RequestBody ChangeOrderDto dto) {
    return ResponseEntity.ok(orderService.orderChange(dto));
  }
  
  @PostMapping("/terminate")
  public ResponseEntity<Long> orderTermination(@RequestBody TerminationOrderDto dto) {
    return ResponseEntity.ok(orderService.orderTermination(dto));
  }
  
  @GetMapping("/messages")
  public ResponseEntity<List<MessageEnvelope>> findAllMessageEnvelope() {
    return ResponseEntity.ok(messageEnvelopeRepository.findAll());
  }
}
