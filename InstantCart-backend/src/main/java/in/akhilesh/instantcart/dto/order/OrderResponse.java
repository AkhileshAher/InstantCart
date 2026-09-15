package in.akhilesh.instantcart.dto.order;

import in.akhilesh.instantcart.dto.delivery.DeliveryPartnerResponse;
import in.akhilesh.instantcart.dto.user.UserResponse;
import in.akhilesh.instantcart.entity.DeliveryPartner;
import in.akhilesh.instantcart.entity.LiveLocation;
import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.entity.enums.OrderStatus;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private String id;
    private String userId;
    private UserResponse user;
    private List<OrderItem> items;
    private ShippingAddress shippingAddress;
    private String paymentMethod = "card";
    private Double subTotal;
    private Double deliveryFee = 0.0;
    private Double tax = 0.0;
    private Double total;
    private OrderStatus status = OrderStatus.PLACED;
    private List<OrderStatusHistory> statusHistory;
    private String deliveryPartnerId;
    private DeliveryPartnerResponse deliveryPartner;
    private String deliveryOtp = "";
    private LiveLocation liveLocation;
    private Boolean isPaid = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
