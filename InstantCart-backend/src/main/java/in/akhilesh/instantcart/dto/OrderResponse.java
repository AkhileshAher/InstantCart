package in.akhilesh.instantcart.dto;

import in.akhilesh.instantcart.entity.*;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id, String orderNumber, OrderStatus status,
        PaymentMethod paymentMethod, PaymentStatus paymentStatus,
        Double subTotal, Double discountAmount, Double deliveryFee, Double tax, Double total,
        String deliveryOtp, ShippingAddress shippingAddress, LiveLocation liveLocation,
        DeliveryPartnerResponse deliveryPartner,
        List<OrderItemResponse> items, List<StatusHistoryResponse> statusHistory,
        LocalDateTime createdAt) {

        /** showOtp is true only for the customer who owns the order. */
        public static OrderResponse from(Order o, boolean showOtp) {
            return new OrderResponse(
                    o.getId(), o.getOrderNumber(), o.getStatus(),
                    o.getPaymentMethod(), o.getPaymentStatus(),
                    o.getSubTotal(), o.getDiscountAmount(), o.getDeliveryFee(), o.getTax(), o.getTotal(),
                    showOtp ? o.getDeliveryOtp() : null,
                    o.getShippingAddress(), o.getLiveLocation(),
                    o.getDeliveryPartner() == null ? null : DeliveryPartnerResponse.from(o.getDeliveryPartner()),
                    o.getItems().stream().map(OrderItemResponse::from).toList(),
                    o.getStatusHistory().stream().map(StatusHistoryResponse::from).toList(),
                    o.getCreatedAt());
        }
    }