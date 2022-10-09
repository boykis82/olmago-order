package team.caltech.olmago.order.service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.caltech.olmago.order.service.dto.CancelOrderDto;
import team.caltech.olmago.order.service.dto.ChangeOrderDto;
import team.caltech.olmago.order.service.dto.SubscriptionOrderDto;
import team.caltech.olmago.order.service.dto.TerminationOrderDto;
import team.caltech.olmago.order.service.service.OrderService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/olmago/api/v1/orders")
public class OrderController {
  private final OrderService orderService;
  
  @PostMapping("/subscribe")
  public ResponseEntity<Long> orderSubscription(@RequestBody SubscriptionOrderDto dto) {
    return ResponseEntity.ok(orderService.orderSubscription(dto));
  }
  
  @PutMapping("{orderId}/cancel")
  public ResponseEntity<Void> cancelOrder(@RequestParam("orderId") long orderId, @RequestBody CancelOrderDto dto) {
    orderService.cancelOrder(dto);
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
}
