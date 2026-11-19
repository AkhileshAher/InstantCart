package in.akhilesh.instantcart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_partner_id")
    private DeliveryPartner deliveryPartner;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("changedAt ASC")
    private List<OrderStatusHistory> statusHistory = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "live_lat")),
            @AttributeOverride(name = "lng", column = @Column(name = "live_lng"))
    })
    private LiveLocation liveLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PLACED;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = PaymentMethod.CARD;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    private Double subTotal = 0.0;
    private Double discountAmount = 0.0;

    private Double deliveryFee = 0.0;
    private Double tax = 0.0;
    private Double total = 0.0;

    private String deliveryOtp;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void addStatusHistory(OrderStatus newStatus, String note) {
        OrderStatusHistory h = new OrderStatusHistory();
        h.setOrder(this);
        h.setStatus(newStatus);
        h.setNote(note);
        statusHistory.add(h);
        this.status = newStatus;
    }

    public void calculateTotals() {
        this.subTotal = items.stream()
                .mapToDouble(i -> i.getUnitPrice() * i.getQuantity()).sum();
        this.discountAmount = items.stream()
                .mapToDouble(i -> i.getUnitPrice() * i.getQuantity() - i.getLineTotal())
                .sum();
        double afterDiscount = subTotal - discountAmount;
        this.total = afterDiscount + deliveryFee + tax;
    }

}