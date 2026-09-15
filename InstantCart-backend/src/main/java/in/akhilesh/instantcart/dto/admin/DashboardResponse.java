package in.akhilesh.instantcart.dto.admin;

import in.akhilesh.instantcart.dto.order.OrderResponse;
import in.akhilesh.instantcart.entity.Order;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    @PositiveOrZero(message = "Total Orders cannot be less than zero")
    private long totalOrders;
    @PositiveOrZero(message = "Total Users cannot be less than zero")
    private long totalUsers;
    @PositiveOrZero(message = "Total Products cannot be less than zero")
    private long totalProducts;
    @PositiveOrZero(message = "Total out Of Stocks Products cannot be less than zero")
    private long outOfStockProducts;

    private List<OrderResponse> recentOrders;

    @PositiveOrZero(message = "Total Partners cannot be less than zero")
    private long totalPartners;

    @PositiveOrZero(message = "Total out of Stock cannot be less than zero")
    private long outOfStock;
}
