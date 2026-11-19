
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  ArrowLeftIcon,
  CheckIcon,
  ChevronRightIcon,
  CreditCardIcon,
  MapPinIcon,
} from "lucide-react";
import toast from "react-hot-toast";

import { useCart } from "../context/CartContext";
import type { Address, ApiResponse, Order } from "../types";

import CheckoutAddress from "../components/Checkout/CheckoutAddress";
import CheckoutPayment from "../components/Checkout/CheckoutPayment";
import CheckoutReview from "../components/Checkout/CheckoutReview";

import api from "../config/api";

type CheckoutStep = "address" | "payment" | "review";

function Checkout() {
  const navigate = useNavigate();
  const currency = "₹";

  const { items, cartTotal, clearCart } = useCart();

  const [step, setStep] = useState<CheckoutStep>("address");
  const [loading, setLoading] = useState(false);
  const [addresses, setAddresses] = useState<Address[]>([]);

  const [address, setAddress] = useState<Address>({
    id: 0,
    label: "",
    addressLine: "",
    city: "",
    state: "",
    zip: "",
    isDefault: false,
  });

  const [paymentMethod, setMethodPayment] =
    useState<string>("COD");

  // Keep these calculations consistent with your backend.
  const deliveryFee = cartTotal >= 500 ? 0 : 40;
  const tax = cartTotal * 0.05;
  const total = cartTotal + deliveryFee + tax;

  const steps: {
    key: CheckoutStep;
    label: string;
    icon: typeof MapPinIcon;
  }[] = [
    {
      key: "address",
      label: "Address",
      icon: MapPinIcon,
    },
    {
      key: "payment",
      label: "Payment",
      icon: CreditCardIcon,
    },
    {
      key: "review",
      label: "Review",
      icon: CheckIcon,
    },
  ];

  // Fetch saved addresses
  useEffect(() => {
    const fetchAddresses = async () => {
      setLoading(true);

      try {
        const response = await api.get<ApiResponse<Address[]>>(
          "/api/addresses"
        );

        setAddresses(response.data.data ?? []);
      } catch (error: unknown) {
        const message =
          error instanceof Error
            ? error.message
            : "Failed to load addresses";

        toast.error(message);
      } finally {
        setLoading(false);
      }
    };

    void fetchAddresses();
  }, []);

  // Select default address when addresses are loaded
  useEffect(() => {
    if (addresses.length === 0) return;

    const defaultAddress =
      addresses.find((item) => item.isDefault) ?? addresses[0];

    setAddress(defaultAddress);
  }, [addresses]);

  // Handle successful order/payment
  const handlePaymentSuccess = async (order: Order) => {
    await clearCart();
    navigate(`/orders/${order.id}`);
  };

  // Place order
  const handlePlaceOrder = async () => {
    if (!address.id) {
      toast.error("Please select a delivery address");
      setStep("address");
      return;
    }

    if (!paymentMethod) {
      toast.error("Please select a payment method");
      setStep("payment");
      return;
    }

    setLoading(true);

    try {
      const orderData = {
        addressId: address.id,
        paymentMethod,
      };

      const response = await api.post<ApiResponse<Order>>(
        "/api/orders",
        orderData
      );

      const order = response.data.data;

      await clearCart();

      toast.success("Order placed successfully!");

      navigate(`/orders/${order.id}`);
    } catch (error: unknown) {
      const message =
        error instanceof Error
          ? error.message
          : "Failed to place order";

      toast.error(message);
    } finally {
      setLoading(false);
      window.scrollTo(0, 0);
    }
  };

  if (items.length === 0) {
    return (
      <div className="min-h-screen bg-app-cream flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-xl font-semibold text-app-green mb-2">
            Your cart is Empty
          </h2>

          <p className="text-sm text-app-text-light mb-4">
            Add some products to checkout
          </p>

          <button
            onClick={() => navigate("/products")}
            className="px-5 py-2.5 bg-app-green text-white text-sm font-medium rounded-xl hover:bg-app-green-light transition-colors"
          >
            Browse products
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-app-cream">
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

        {/* Back Button */}
        <button
          onClick={() => navigate(-1)}
          className="flex items-center gap-2 text-sm text-app-text-light hover:text-app-green mb-6 transition-colors"
        >
          <ArrowLeftIcon className="size-4" />
          Back
        </button>

        <h1 className="text-2xl font-semibold text-app-green mb-8">
          Checkout
        </h1>

        {/* Checkout Steps */}
        <div className="flex items-center gap-2 mb-8 flex-wrap">
          {steps.map((item, index) => (
            <div
              key={item.key}
              className="flex items-center gap-2"
            >
              <button
                onClick={() => setStep(item.key)}
                className={`flex items-center gap-2 px-4 py-3 rounded-xl text-sm font-medium transition-colors ${
                  step === item.key
                    ? "bg-app-green text-white"
                    : "bg-white text-app-text-light"
                }`}
              >
                <item.icon className="size-4" />
                {item.label}
              </button>

              {index < steps.length - 1 && (
                <ChevronRightIcon className="size-4 text-app-text-light" />
              )}
            </div>
          ))}
        </div>

        <div className="grid md:grid-cols-3 gap-6">

          {/* Main Checkout Form */}
          <div className="md:col-span-2">

            {step === "address" && (
              <CheckoutAddress
                address={addresses}
                setAddress={setAddress}
                setStep={setStep}
                selectedAddress={address}
              />
            )}

            {step === "payment" && (
              <CheckoutPayment
                paymentMethod={paymentMethod}
                setPaymentMethod={setMethodPayment}
                setStep={setStep}
              />
            )}

            {step === "review" && (
              <CheckoutReview
                address={address}
                items={items}
                handlePlaceOrder={handlePlaceOrder}
                loading={loading}
                total={total}
                paymentMethod={paymentMethod}
                onPaymentSuccess={handlePaymentSuccess}
              />
            )}
          </div>

          {/* Order Summary */}
          <div className="bg-white rounded-2xl p-5 h-fit sticky top-24">
            <h3 className="text-sm font-semibold text-app-green mb-4">
              Order Summary
            </h3>

            <div className="text-sm space-y-2">

              <div className="flex justify-between">
                <span className="text-app-text-light">
                  Subtotal ({items.length} items)
                </span>

                <span>
                  {currency}{cartTotal.toFixed(2)}
                </span>
              </div>

              <div className="flex justify-between">
                <span className="text-app-text-light">
                  Delivery
                </span>

                <span>
                  {deliveryFee === 0 ? (
                    <span className="text-app-success">
                      Free
                    </span>
                  ) : (
                    `${currency}${deliveryFee.toFixed(2)}`
                  )}
                </span>
              </div>

              <div className="flex justify-between">
                <span className="text-app-text-light">
                  Tax
                </span>

                <span>
                  {currency}{tax.toFixed(2)}
                </span>
              </div>

              <div className="flex justify-between pt-3 border-t border-app-border text-base font-semibold">
                <span>Total</span>

                <span className="text-app-green">
                  {currency}{total.toFixed(2)}
                </span>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Checkout;
