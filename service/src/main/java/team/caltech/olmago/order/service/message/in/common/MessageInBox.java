package team.caltech.olmago.order.service.message.in.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MessageInBox {
  @Id
  private String id;
  
  @Column(nullable = false)
  private LocalDateTime receivedDateTime;
  
  @Column(nullable = false)
  private String eventType;
  
  @Column(nullable = false)
  private String payload;
}
