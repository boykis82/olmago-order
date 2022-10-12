package team.caltech.olmago.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  @Query(
      "SELECT DISTINCT o FROM Order o join fetch o.orderDetails WHERE o.id = :id"
  )
  Optional<Order> findOrderWithDetailsById(@Param("id") long id);
  
  @Query(
      "SELECT DISTINCT o FROM Order o join fetch o.orderDetails WHERE o.customerId = :customerId ORDER BY o.receiptRequestDtm DESC"
  )
  List<Order> findOrderWithDetailsByCustomer(@Param("customerId") long customerId);
}
