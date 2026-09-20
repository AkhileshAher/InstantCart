package in.akhilesh.instantcart.repository;

import in.akhilesh.instantcart.entity.Order;
import in.akhilesh.instantcart.entity.enums.OrderStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, ObjectId> {

    List<Order> findByUserId(ObjectId userId);

    List<Order> findByUserIdAndStatus(ObjectId userId, OrderStatus status);

    List<Order> findByDeliveryPartnerId(ObjectId deliveryPartnerId);

    List<Order> findByStatus(String status);

    Optional<Order> findByIdAndUserId(ObjectId orderId, ObjectId userId);

    Optional<Order> findByIdAndDeliveryPartnerId(ObjectId orderId, ObjectId deliveryPartnerId);

    List<Order> findByDeliveryPartnerIdAndStatus(ObjectId deliveryPartnerId, OrderStatus orderStatus);

    List<Order> findByDeliveryPartnerIdAndStatusIn(ObjectId deliveryPartnerId, List<OrderStatus> assigned);
}