package team.caltech.olmago.order.service.message.in.common;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageInBoxRepository extends JpaRepository<MessageInBox, String> {
}
