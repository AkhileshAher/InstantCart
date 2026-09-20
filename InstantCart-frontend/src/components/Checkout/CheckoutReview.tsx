import { CheckIcon, TruckIcon } from "lucide-react";
import type { Address } from "../../types";
import toast from "react-hot-toast";
import api from "../../config/api";
import { useState } from "react";

interface CheckoutReviewProps {
  address: Address;
  items: any[];
  handlePlaceOrder: () => void;
  loading: boolean;
  total: number;
  paymentMethod: string;
  onPaymentSuccess: (order: any) => void;
}

export default function CheckoutReview({
  address,
  items,
  handlePlaceOrder,
  loading,
  total,
  paymentMethod,
  onPaymentSuccess,
}: CheckoutReviewProps) {
  const currency = "₹";

  const [processing, setProcessing] = useState(false);

  const payload = {
    items: items.map((item) => ({
      productId: item.product.id,
      quantity: item.quantity,
    })),
  };

  const handlePay = async () => {
    if (processing || loading) return;
    setProcessing(true);

    try {
      if (paymentMethod === "cod") {
        await handlePlaceOrder();
        return;
      }

      if (paymentMethod === "card") {
        const response = await api.post(`/payments/create-pay`, payload);

        const options = {
          key: "rzp_test_Td0Wic3t3B5luP",
          amount: response.data.amount,
          currency: response.data.currency,
          name: "InstantCart",
          description: response.data.description,
          order_id: response.data.id,
          handler: paymentHandler,
          modal: {
            ondismiss: function () {
              setProcessing(false);
              toast.error("Payment Cancelled !!");
              return;
            },
          },
          theme: {
            color: "#7c3aed",
          },
        };

        const razorpay = new window.Razorpay(options);
        razorpay.open();
      }
    } catch (error) {
      setProcessing(false);
      toast.error(error?.response?.data?.message || "Something went wrong");
    }
  };

  const paymentHandler = async (paymentResponse: any) => {
    try {
      const response = await api.post("/payments/verify", {
        razorpay_order_id: paymentResponse.razorpay_order_id,
        razorpay_payment_id: paymentResponse.razorpay_payment_id,
        razorpay_signature: paymentResponse.razorpay_signature,

        orderRequest: {
          items: items.map((item) => ({
            productId: item.product.id,
            quantity: item.quantity,
          })),

          shippingAddress: {
            label: address.label,
            address: address.address,
            city: address.city,
            state: address.state,
            zip: address.zip,
            lat: address.lat,
            lng: address.lng,
          },

          paymentMethod: "card",
        },
      });

      onPaymentSuccess(response.data);
      toast.success("Payment successful and order placed!");
    } catch (error: any) {
      setProcessing(false);
      toast.error(
        error?.response?.data?.message || "Payment verification failed",
      );
    }
  };

  const isProcessing = processing || loading;

  return (
    <div className="bg-white rounded-2xl p-6 animate-fade-in">
      <h2 className="text-lg font-semibold text-app-green mb-5 flex items-center gap-2">
        <CheckIcon className="size-5" /> Review Your Order
      </h2>

      {/* Delivery Info */}
      <div className="mb-5 p-4 bg-app-cream rounded-xl">
        <div className="flex items-center gap-2 mb-2">
          <TruckIcon className="size-4 text-app-green" />
          <span className="text-sm font-semibold text-app-green">
            Delivery Address
          </span>
        </div>
        <p className="text-sm text-app-text-light">
          {address.label} — {address.address}, {address.city}, {address.state}{" "}
          {address.zip}
        </p>
      </div>

      {/* Items */}
      <div className="space-y-3 mb-5">
        {items.map((item) => (
          <div key={item.product.id} className="flex items-center gap-3">
            <img
              src={item.product.image}
              alt={item.product.name}
              className="size-12 rounded-lg object-cover"
            />
            <div className="flex-1">
              <p className="text-sm font-medium text-app-green">
                {item.product.name}
              </p>
              <p className="text-xs text-app-text-light">
                Qty: {item.quantity}
              </p>
            </div>
            <span className="text-sm font-semibold">
              {currency}
              {(item.product.price * item.quantity).toFixed(2)}
            </span>
          </div>
        ))}
      </div>

      <button
        onClick={handlePay}
        disabled={isProcessing}
        className="w-full py-3 bg-app-orange text-white font-semibold rounded-xl hover:bg-app-orange-dark transition-colors disabled:opacity-60 active:scale-[0.98]"
      >
        {isProcessing
          ? "Placing Order..."
          : `Place Order — ${currency}${total.toFixed(2)}`}
      </button>
    </div>
  );
}
