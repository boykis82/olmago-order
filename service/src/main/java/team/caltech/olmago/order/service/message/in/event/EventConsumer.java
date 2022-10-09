package team.caltech.olmago.order.service.message.in.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import team.caltech.olmago.order.service.message.in.event.contract.*;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Configuration
public class EventConsumer {
  private final ContractEventHandler contractEventHandler;

  @Bean
  public Consumer<Message<ContractSubscriptionReceived>> contractSubscriptionReceived() {
    return m -> contractEventHandler.contractSubscriptionReceived(m, m.getPayload());
  }
  
  @Bean
  public Consumer<Message<ContractSubscriptionCompleted>> contractSubscriptionCompleted() {
    return m -> contractEventHandler.contractSubscriptionCompleted(m, m.getPayload());
  }
  
  @Bean
  public Consumer<Message<ContractChanged>> contractChanged() {
    return m -> contractEventHandler.contractChanged(m, m.getPayload());
  }
  
  @Bean
  public Consumer<Message<ContractTerminationCompleted>> contractTerminationCompleted() {
    return m -> contractEventHandler.contractTerminationCompleted(m, m.getPayload());
  }
}
