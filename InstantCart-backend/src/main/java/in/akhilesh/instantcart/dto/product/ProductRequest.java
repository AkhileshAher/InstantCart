package in.akhilesh.instantcart.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "Name is Required")
    @Size(min = 2,message = "Atleast 2 Characters are required")
    private String name;

    private String description = "";

    @NotNull(message = "Price is Required")
    @PositiveOrZero(message = "Price cannot be Negative")
    private Double price;

    private Double originalPrice = 0.0;

//    @NotBlank(message = "Image is Required")
    private String image;

    @NotBlank(message = "Category is Required")
    private String category;

    private String unit = "piece";

    @NotNull(message = "Stock is Required")
    @PositiveOrZero(message = "Stock Cannot be Negative")
    private Integer stock = 0;

    private Boolean isOrganic= false;
}
