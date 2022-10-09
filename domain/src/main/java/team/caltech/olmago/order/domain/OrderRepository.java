package team.caltech.olmago.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  @Query(
      "SELECT o FROM order o join fetch o.orderDetails WHERE o.id = :id"
  )
  Optional<Order> findOrderWithDetailsById(@Param("id") long id);
}
