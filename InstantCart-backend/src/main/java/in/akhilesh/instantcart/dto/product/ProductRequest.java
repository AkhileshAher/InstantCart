package in.akhilesh.instantcart.dto.product;

import lombok.Data;
import java.util.UUID;

@Data
public class ProductRequest {
    private String name;
    private String description = "";
    private Double price;
    private Double originalPrice = 0.0;
    private String image;
    private String category;
    private String unit = "piece";
    private Integer stock = 0;
    private Boolean isOrganic= false;
}
