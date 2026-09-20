package in.akhilesh.instantcart.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import in.akhilesh.instantcart.dto.order.OrderResponse;
import in.akhilesh.instantcart.dto.payment.CreatePaymentOrderRequest;
import in.akhilesh.instantcart.dto.payment.VerifyPaymentRequest;
import in.akhilesh.instantcart.dto.payment.VerifyPaymentResponse;
import in.akhilesh.instantcart.security.JwtPrincipal;
import in.akhilesh.instantcart.service.RazorpayService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class RazorpayController {

    private final RazorpayService razorpayService;

    public RazorpayController(RazorpayService razorpayService) {
        this.razorpayService = razorpayService;
    }

    @PostMapping("/create-pay")
    public ResponseEntity<String> createPay(@RequestBody CreatePaymentOrderRequest paymentRequest) throws RazorpayException {

        String response = razorpayService.createOrder(paymentRequest).toString();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<OrderResponse> verifyPay(
            @RequestBody VerifyPaymentRequest paymentRequest,
            Authentication authentication ) throws RazorpayException {

        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();

        ObjectId userId = principal.getUserId();

        OrderResponse response = razorpayService.verifyPayment(paymentRequest, userId);

        return ResponseEntity.ok(response);
    }

}
