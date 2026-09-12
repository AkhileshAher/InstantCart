package in.akhilesh.instantcart.entity;

import in.akhilesh.instantcart.dto.order.OrderItem;
import in.akhilesh.instantcart.dto.order.OrderStatusHistory;
import in.akhilesh.instantcart.dto.order.ShippingAddress;
import in.akhilesh.instantcart.entity.enums.OrderStatus;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "orders")
public class Order {

    @Id
    private ObjectId id;

    private ObjectId userId;

    private User user;

    private List<OrderItem> items;

    private ShippingAddress shippingAddress;

    private String paymentMethod = "card";

    private Double subTotal;

    private Double deliveryFee = 0.0;

    private Double tax = 0.0;

    private Double total;

    private OrderStatus status = OrderStatus.PLACED;

    private List<OrderStatusHistory> statusHistory;

    private ObjectId deliveryPartnerId;

    private DeliveryPartner deliveryPartner;

    private String deliveryOtp = "";

    private LiveLocation liveLocation;

    private Boolean isPaid = false;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}