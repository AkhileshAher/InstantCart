package in.akhilesh.instantcart.repository;

import in.akhilesh.instantcart.entity.Order;
import in.akhilesh.instantcart.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Order> findByIdAndUserId(Long id, Long userId);

    List<Order> findAllByOrderByCreatedAtDesc();
    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);

    List<Order> findByDeliveryPartnerIdOrderByCreatedAtDesc(Long partnerId);
    Optional<Order> findByIdAndDeliveryPartnerId(Long id, Long partnerId);
}
