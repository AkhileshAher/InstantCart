package in.akhilesh.instantcart.dto.admin;

import in.akhilesh.instantcart.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private long totalOrders;
    private long totalUsers;
    private long totalProducts;
    private long outOfStockProducts;
    private List<Order> recentOrders;
    private long totalPartners;
    private long outOfStock;
}
