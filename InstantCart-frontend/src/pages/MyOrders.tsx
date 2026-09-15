import { useEffect, useState } from "react";
import type { Order } from "../types";
import { Link, useSearchParams } from "react-router-dom";
import { useCart } from "../context/CartContext";
import { statusColors } from "../assets/data";
import Loading from "../components/Loading";
import { CalendarIcon, ChevronRightIcon, PackageIcon} from "lucide-react";
import api from "../config/api";
import toast from "react-hot-toast";

type OrderStatus = "PLACED" | "PACKED" | "ASSIGNED" | "CONFIRMED" | "CANCELLED" | "OUT_FOR_DELIVERY" | "DELIVERED";

type Tab = "ALL" | OrderStatus;

function MyOrders() {

    const currency = "₹";

    const [orders, setOrders] = useState<Order[]>([]);
    const [loading, setLoading] = useState(true);
    const [activeTab, setActiveTab] = useState<Tab>("ALL");

    const [searchParams, setSearchParams] =useSearchParams();

    const { clearCart } = useCart();

    const tabs: Tab[] = ["ALL","PLACED","ASSIGNED","CANCELLED","CONFIRMED","PACKED","OUT_FOR_DELIVERY","DELIVERED"];

    const fetchOrders = async () => {
        setLoading(true);
        try {
            const response = await api.get("/orders", {
                params: activeTab === "ALL" ? {} : { status: activeTab}
            });

            console.log(response.data);
            setOrders(response.data);
        } catch (error: any) {
            toast.error(error?.response?.data?.message || error?.message || "Failed to fetch orders");
            setOrders([]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const shouldClearCart = searchParams.get("clearCart");
        if (shouldClearCart) {
            clearCart();
            setSearchParams({}, {replace: true});
        }
        fetchOrders();
    }, [activeTab]);

    return (
        <div className="min-h-screen bg-app-cream mb-20">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <h1 className="text-2xl font-semibold text-app-green mb-6">
                    My Orders
                </h1>
                {/* Tabs */}
                <div className="flex gap-2 mb-6 overflow-x-auto pb-2">
                    {tabs.map((tab) => (
                        <button key={tab} type="button" onClick={() => setActiveTab(tab)}
                            className={`
                                px-4 py-2
                                text-sm
                                font-medium
                                rounded-xl
                                whitespace-nowrap
                                transition-colors
                                ${
                                    activeTab === tab
                                        ? "bg-app-green text-white"
                                        : "bg-white text-app-text-light hover:bg-app-cream"
                                }
                            `}
                        >
                            {tab === "ALL"
                                ? "ALL"
                                : tab}
                        </button>

                    ))}

                </div>

                {/* Loading */}

                {loading ? (

                    <Loading />

                ) : orders.length === 0 ? (

                    /* Empty state */

                    <div className="text-center py-16">

                        <PackageIcon
                            className="size-16 text-app-border mx-auto mb-4"
                        />

                        <h2 className="text-lg font-medium text-app-green mb-2">
                            No orders yet
                        </h2>

                        <p className="text-sm text-app-text-light mb-4">
                            Start shopping to see your orders here
                        </p>

                        <Link
                            to="/products"
                            className="inline-flex px-4 py-2 bg-app-green text-white text-sm rounded-lg"
                        >
                            Start Shopping
                        </Link>

                    </div>

                ) : (

                    /* Orders */

                    <div className="space-y-4">

                        {orders.map((order) => (

                            <Link
                                key={order.id}
                                to={`/orders/${order.id}`}
                                className="block max-w-4xl bg-white rounded-2xl p-5 hover:shadow transition-all"
                            >

                                {/* Header */}

                                <div className="flex items-start justify-between mb-3">

                                    <div>

                                        <p className="text-sm font-medium text-app-green">
                                            Order #
                                            {order.id
                                                .slice(-8)
                                                .toUpperCase()}
                                        </p>

                                        <div className="flex items-center gap-2 mt-1">

                                            <CalendarIcon className="size-3 text-app-text-light" />

                                            <span className="text-xs text-app-text-light">
                                                {new Date(
                                                    order.createdAt
                                                ).toLocaleDateString(
                                                    "en-US",
                                                    {
                                                        month: "short",
                                                        day: "numeric",
                                                        year: "numeric"
                                                    }
                                                )}
                                            </span>

                                        </div>

                                    </div>

                                    <div className="flex items-center gap-2">

                                        <span
                                            className={`
                                                px-4
                                                py-1
                                                text-xs
                                                font-medium
                                                rounded-full
                                                ${
                                                    statusColors[
                                                        order.status
                                                    ] ||
                                                    "bg-gray-100 text-gray-700"
                                                }
                                            `}
                                        >
                                            {order.status}
                                        </span>

                                        <ChevronRightIcon className="size-4 text-app-text-light" />

                                    </div>

                                </div>

                                {/* Product Images */}

                                <div className="flex items-center gap-2 mb-3">

                                    {order.items
                                        .slice(0, 4)
                                        .map((item, index) => (

                                            <img
                                                key={`${order.id}-${index}`}
                                                src={item.avatar}
                                                alt={item.name}
                                                className="size-12 sm:size-16 rounded-lg object-cover border border-app-border"
                                            />

                                        ))}

                                    {order.items.length > 4 && (

                                        <div className="size-12 sm:size-16 rounded-lg bg-app-cream flex items-center justify-center text-xs font-semibold text-app-text-light">
                                            +{order.items.length - 4}
                                        </div>

                                    )}

                                </div>

                                {/* Footer */}

                                <div className="flex justify-between items-center pt-3 text-sm border-t border-app-border">

                                    <span className="text-app-text-light">
                                        {order.items.length}{" "}
                                        {order.items.length === 1
                                            ? "item"
                                            : "items"}
                                    </span>

                                    <span className="font-semibold text-app-green">
                                        {currency}
                                        {order.total.toFixed(2)}
                                    </span>

                                </div>

                            </Link>

                        ))}

                    </div>

                )}

            </div>

        </div>
    );
}

export default MyOrders;

