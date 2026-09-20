import { useEffect, useRef, useState } from "react";
import { PackageIcon, NavigationIcon } from "lucide-react";
import OtpModal from "../../components/Delivery/OtpModal";
import CancelModal from "../../components/Delivery/CancelModal";
import DeliveryOrderCard from "../../components/Delivery/DeliveryOrderCard";
import Loading from "../../components/Loading";
import type { Order } from "../../types";
import toast from "react-hot-toast";
import api from "../../config/api";

export default function DeliveryDashboard() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [tab, setTab] = useState<"ACTIVE" | "DELIVERED">("ACTIVE");
  const [tracking, setTracking] = useState(false);
  const [otpModal, setOtpModal] = useState<string | null>(null);
  const [otp, setOtp] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [cancelModal, setCancelModal] = useState<string | null>(null);
  const [cancelReason, setCancelReason] = useState("");
  const watchIdRef = useRef<number | null>(null);

  const fetchOrders = async () => {
    setLoading(true);

    try {
      const res = await api.get("/delivery-partners/my-orders", {
        params: { status: tab },
      });

      const filteredOrders =
        tab === "ACTIVE"
          ? res.data.filter(
              (order: Order) =>
                order.status === "ASSIGNED" ||
                order.status === "OUT_FOR_DELIVERY",
            )
          : res.data.filter((order: Order) => order.status === "DELIVERED");

      setOrders(filteredOrders);
    } catch (error: any) {
      toast.error(
        error?.response?.data?.message || "Failed to load deliveries",
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, [tab]);

  useEffect(() => {
    const activeOrders = orders.filter(
      (order) => order.status === "OUT_FOR_DELIVERY",
    );

    if (activeOrders.length === 0 || !tracking) {
      if (watchIdRef.current !== null) {
        navigator.geolocation.clearWatch(watchIdRef.current);
        watchIdRef.current = null;
      }
      return;
    }

    const sendLocation = async (position: GeolocationPosition) => {
      const { latitude: lat, longitude: lng } = position.coords;

      for (const order of activeOrders) {
        try {
          await api.put(`/delivery-partners/location/${order.id}`, {
            lat,
            lng,
          });
        } catch (error) {
          console.error(`Failed to send location for order ${order.id}`, error);
        }
      }
    };

    watchIdRef.current = navigator.geolocation.watchPosition(
      sendLocation,
      () => toast.error("Unable to access your location"),
      { enableHighAccuracy: true, maximumAge: 10000 },
    );

    return () => {
      if (watchIdRef.current !== null) {
        navigator.geolocation.clearWatch(watchIdRef.current);
        watchIdRef.current = null;
      }
    };
  }, [orders, tracking]);

  const handleUpdateStatus = async (orderId: string, status: string) => {
    try {
      await api.patch(
        `/delivery-partners/status-update/${orderId}`,
        {},
        { params: { status } },
      );

      toast.success(
        status === "OUT_FOR_DELIVERY"
          ? "Order is out for delivery"
          : status === "CANCELLED"
            ? "Delivery Cancelled"
            : "Status Updated To " + status,
      );

      await fetchOrders();
    } catch (error: any) {
      toast.error(error?.response?.data?.message || "Failed to update status");
    }
  };

  const handleComplete = async () => {
    if (!otpModal || !otp) return;

    setSubmitting(true);

    try {
      await api.put(
        `/delivery-partners/${otpModal}/deliver`,
        {},
        {
          params: {
            otp,
          },
        },
      );

      toast.success("Delivery Completed");
      setOtpModal(null);
      setOtp("");
      await fetchOrders();
    } catch (error: any) {
      toast.error(
        error?.response?.data?.message ||
          error?.message ||
          "Failed to complete delivery",
      );
    } finally {
      setSubmitting(false);
    }
  };

  const handleCancel = async () => {
    if (!cancelModal) return;

    setSubmitting(true);

    try {
      await api.patch(
        `/delivery-partners/status-update/${cancelModal}`,
        { reason: cancelReason },
        { params: { status: "CANCELLED" } },
      );

      toast.success("Delivery Cancelled");
      setCancelModal(null);
      setCancelReason("");
      await fetchOrders();
    } catch (error: any) {
      toast.error(
        error?.response?.data?.message ||
          error?.message ||
          "Failed to cancel delivery",
      );
    } finally {
      setSubmitting(false);
    }
  };

  const handleTrackingToggle = () => {
    setTracking((prev) => {
      const newValue = !prev;

      if (prev && watchIdRef.current !== null) {
        navigator.geolocation.clearWatch(watchIdRef.current);
        watchIdRef.current = null;
      }

      return newValue;
    });
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-2 flex-wrap">
        {(["ACTIVE", "DELIVERED"] as const).map((t) => (
          <button
            key={t}
            onClick={() => setTab(t)}
            className={`px-4 py-2 text-sm font-medium rounded-xl transition-colors ${
              tab === t
                ? "bg-app-green text-white"
                : "bg-white text-zinc-600 hover:bg-app-cream border border-app-border"
            }`}
          >
            {t === "ACTIVE" ? "Active" : "Delivered"}
          </button>
        ))}

        <div className="ml-auto">
          <button
            onClick={handleTrackingToggle}
            className={`px-4 py-2 text-sm font-medium rounded-xl transition-colors flex items-center gap-1.5 ${
              tracking
                ? "bg-green-600 text-white"
                : "bg-white text-zinc-600 border border-app-border hover:bg-app-cream"
            }`}
          >
            <NavigationIcon
              className={`w-3.5 h-3.5 ${tracking ? "animate-pulse" : ""}`}
            />
            {tracking ? "Sharing Location" : "Share Location"}
          </button>
        </div>
      </div>

      {loading ? (
        <Loading />
      ) : orders.length === 0 ? (
        <div className="text-center py-16 bg-white rounded-2xl border border-app-border">
          <PackageIcon className="size-12 text-app-border mx-auto mb-3" />
          <p className="text-lg font-semibold text-zinc-900 mb-1">
            No {tab === "ACTIVE" ? "active" : "delivered"} deliveries
          </p>
          <p className="text-sm text-zinc-500">
            {tab === "ACTIVE"
              ? "You'll see new assignments here"
              : "Delivered orders will appear here"}
          </p>
        </div>
      ) : (
        <div className="space-y-4">
          {orders.map((order) => (
            <DeliveryOrderCard
              key={order.id}
              order={order}
              tab={tab}
              handleUpdateStatus={handleUpdateStatus}
              setOtpModal={setOtpModal}
              setCancelModal={setCancelModal}
            />
          ))}
        </div>
      )}

      {otpModal && (
        <OtpModal
          orderId={otpModal}
          setOtpModal={setOtpModal}
          otp={otp}
          setOtp={setOtp}
          handleComplete={handleComplete}
          submitting={submitting}
        />
      )}

      {cancelModal && (
        <CancelModal
          setCancelModal={setCancelModal}
          cancelReason={cancelReason}
          setCancelReason={setCancelReason}
          handleCancel={handleCancel}
          submitting={submitting}
        />
      )}
    </div>
  );
}
