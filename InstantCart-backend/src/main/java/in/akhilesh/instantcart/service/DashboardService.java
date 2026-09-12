package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.admin.DashboardResponse;
import in.akhilesh.instantcart.entity.Order;
import in.akhilesh.instantcart.repository.DeliveryPartnerRepository;
import in.akhilesh.instantcart.repository.OrderRepository;
import in.akhilesh.instantcart.repository.ProductRepository;
import in.akhilesh.instantcart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final DeliveryPartnerRepository deliveryPartnerRepository;

    public DashboardResponse getDashboard() {

        long totalUsers = userRepository.count();
        long totalProducts = productRepository.count();
        long totalOrders = orderRepository.count();
        long totalPartners = deliveryPartnerRepository.count();
        long outOfStock = productRepository
                        .findByStockGreaterThan(0)
                        .stream()
                        .filter(p -> p.getStock() == 0)
                        .count();

        DashboardResponse response = new DashboardResponse();
        response.setTotalUsers(totalUsers);
        response.setTotalProducts(totalProducts);
        response.setTotalOrders(totalOrders);
        response.setTotalPartners(totalPartners);
        response.setOutOfStock(outOfStock);

        response.setRecentOrders(orderRepository.findAll()
                        .stream()
                        .sorted(
                                Comparator.comparing(
                                        Order::getCreatedAt
                                ).reversed()
                        )
                        .limit(10)
//                        .map(/* map */)
                        .toList()
        );

        return response;
    }
}