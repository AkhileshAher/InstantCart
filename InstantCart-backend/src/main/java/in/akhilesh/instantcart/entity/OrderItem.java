package in.akhilesh.instantcart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String productName;

    private Double unitPrice;

    private Integer discountPercent = 0;

    private Integer quantity;

    private Double lineTotal;

    public static OrderItem from(Product p, int quantity) {
        OrderItem item = new OrderItem();
        item.setProduct(p);
        item.setProductName(p.getName());
        item.setUnitPrice(p.getPrice());
        item.setDiscountPercent(p.getDiscountPercent() == null ? 0 : p.getDiscountPercent());
        item.setQuantity(quantity);
        item.setLineTotal(item.getUnitPrice()
                * (1 - item.getDiscountPercent() / 100.0) * quantity);
        return item;
    }
}