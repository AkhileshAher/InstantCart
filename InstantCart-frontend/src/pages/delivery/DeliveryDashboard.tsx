
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

    const [tab, setTab] = useState<"ACTIVE" | "COMPLETED">("ACTIVE");

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
                params: {
                    status: tab
                }
            });

            setOrders(res.data);

        } catch (error: any) {

            toast.error(
                error?.response?.data?.message ||
                "Failed to load deliveries"
            );

        } finally {

            setLoading(false);

        }

    };


    useEffect(() => {

        fetchOrders();

    }, [tab]);


    // Send location only for OUT_FOR_DELIVERY orders

    useEffect(() => {

        const activeOrders = orders.filter((o) =>
            o.status === "OUT_FOR_DELIVERY"
        );

        if (activeOrders.length === 0 || !tracking) {

            if (watchIdRef.current !== null) {

                navigator.geolocation.clearWatch(
                    watchIdRef.current
                );

                watchIdRef.current = null;

            }

            return;

        }


        const sendLocation = async (pos: GeolocationPosition) => {

            const { latitude: lat, longitude: lng } = pos.coords;

            for (const order of activeOrders) {

                try {

                    await api.put(
                        `/delivery-partners/location/${order.id}`,
                        { lat, lng }
                    );

                } catch (error) {

                    console.log(
                        "Failed to send location",
                        error
                    );

                }

            }

        };


        watchIdRef.current = navigator.geolocation.watchPosition(
            sendLocation,
            () => {

                toast.error(
                    "Unable to access your location"
                );

            },
            {
                enableHighAccuracy: true,
                maximumAge: 10000
            }
        );


        return () => {

            if (watchIdRef.current !== null) {

                navigator.geolocation.clearWatch(
                    watchIdRef.current
                );

                watchIdRef.current = null;

            }

        };

    }, [orders, tracking]);


    const handleUpdateStatus = async (
        orderId: string,
        status: string
    ) => {

        try {

            await api.patch(
                `/delivery-partners/status-update/${orderId}`,
                {},
                {
                    params: {
                        status
                    }
                }
            );

            toast.success(
                status === "OUT_FOR_DELIVERY"
                    ? "Order is out for delivery"
                    : "Status Updated To " + status
            );

            await fetchOrders();

        } catch (error: any) {

            toast.error(
                error?.response?.data?.message ||
                "Failed to update status"
            );

        }

    };


    const handleComplete = async (orderId: string) => {

        if (!otpModal || !otp) return;

        setSubmitting(true);
        console.log(orderId);
        console.log(orders);
        try {

            await api.put(
                `/delivery-partners/${orderId}/deliver`,
                {},
                {
                    params: {
                        otp
                    }
                }
            );

            toast.success("Delivery Completed");

            setOtpModal(null);

            setOtp("");

            await fetchOrders();

        } catch (error: any) {

            toast.error(
                error?.response?.data?.message ||
                error?.message ||
                "Failed to complete delivery"
            );

        } finally {

            setSubmitting(false);

        }

    };


    const handleCancel = async (
        orderId: string,
        status: string
    ) => {

        if (!cancelModal) return;

        setSubmitting(true);

        try {

            await api.patch(
                `/delivery-partners/status-update/${orderId}`,
                {
                    reason: cancelReason
                },
                {
                    params: {
                        status
                    }
                }
            );

            toast.success("Delivery Cancelled");

            setCancelModal(null);

            setCancelReason("");

            await fetchOrders();

        } catch (error: any) {

            toast.error(
                error?.response?.data?.message ||
                error?.message ||
                "Failed to cancel delivery"
            );

        } finally {

            setSubmitting(false);

        }

    };


    return (

        <div className="space-y-6">

            {/* Tabs + Tracking toggle */}

            <div className="flex items-center gap-2 flex-wrap">

                {(["ACTIVE", "COMPLETED"] as const).map((t) => (

                    <button
                        key={t}
                        onClick={() => setTab(t)}
                        className={`px-4 py-2 text-sm font-medium rounded-xl transition-colors ${
                            tab === t
                                ? "bg-app-green text-white"
                                : "bg-white text-zinc-600 hover:bg-app-cream border border-app-border"
                        }`}
                    >

                        {t === "ACTIVE" ? "Active" : "Completed"}

                    </button>

                ))}

                <div className="ml-auto">

                    <button
                        onClick={() => setTracking((prev) => !prev)}
                        className={`px-4 py-2 text-sm font-medium rounded-xl transition-colors flex items-center gap-1.5 ${
                            tracking
                                ? "bg-green-600 text-white"
                                : "bg-white text-zinc-600 border border-app-border hover:bg-app-cream"
                        }`}
                    >

                        <NavigationIcon
                            className={`w-3.5 h-3.5 ${
                                tracking ? "animate-pulse" : ""
                            }`}
                        />

                        {tracking
                            ? "Sharing Location"
                            : "Share Location"}

                    </button>

                </div>

            </div>


            {/* Orders */}

            {loading ? (

                <Loading />

            ) : orders.length === 0 ? (

                <div className="text-center py-16 bg-white rounded-2xl border border-app-border">

                    <PackageIcon className="size-12 text-app-border mx-auto mb-3" />

                    <p className="text-lg font-semibold text-zinc-900 mb-1">
                        No {tab.toLowerCase()} deliveries
                    </p>

                    <p className="text-sm text-zinc-500">

                        {tab === "ACTIVE"
                            ? "You'll see new assignments here"
                            : "Completed deliveries will appear here"}

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


            {/* OTP Modal */}

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


            {/* Cancel Modal */}

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
