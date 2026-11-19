package in.akhilesh.instantcart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description = "";

    @Column(nullable = false)
    private Double price;

    private Double originalPrice = 0.0;

    private Integer discountPercent = 0;

    private String image;

    private String unit = "piece";

    private Integer stock = 0;

    private Boolean isOrganic = false;

    private Boolean isActive = true;

    private Float rating = 0f;

    private Integer reviewCount = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}