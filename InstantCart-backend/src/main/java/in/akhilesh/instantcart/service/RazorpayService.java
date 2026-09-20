package in.akhilesh.instantcart.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import in.akhilesh.instantcart.config.RazorpayProperties;
import in.akhilesh.instantcart.dto.order.OrderResponse;
import in.akhilesh.instantcart.dto.payment.CreatePaymentOrderRequest;
import in.akhilesh.instantcart.dto.payment.VerifyPaymentRequest;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    private final RazorpayProperties properties;
    private final ProductService productService;
    private final OrderService orderService;

    public RazorpayService(RazorpayProperties properties, ProductService productService, OrderService orderService) {
        this.properties = properties;
        this.productService = productService;
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    public Order createOrder(CreatePaymentOrderRequest paymentRequest) throws RazorpayException {
        RazorpayClient razorpayClient =
                new RazorpayClient(properties.keyId(), properties.keySecret());

        JSONObject request = new JSONObject();
        request.put("description", "Payment For Placing Order ");
        Double amount = productService.checkoutTotalPrice(paymentRequest.getItems());
        request.put("amount", Math.round(amount * 100));
        request.put("currency", "INR");

        return razorpayClient.orders.create(request);
    }

    public OrderResponse verifyPayment(VerifyPaymentRequest request, ObjectId userId) throws RazorpayException {
        JSONObject attributes = new JSONObject();
        attributes.put("razorpay_order_id", request.getRazorpay_order_id());
        attributes.put("razorpay_payment_id",request.getRazorpay_payment_id());
        attributes.put("razorpay_signature",request.getRazorpay_signature());

        boolean verified = Utils.verifyPaymentSignature(attributes, properties.keySecret());
        if (!verified) {
            throw new RuntimeException("Payment verification failed");
        }
        return orderService.createOrder(
                userId,
                request.getOrderRequest(),
                true
        );
    }

}
