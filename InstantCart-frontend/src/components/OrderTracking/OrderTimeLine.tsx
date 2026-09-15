import { ClockIcon, CheckIcon, TruckIcon, PackageIcon } from "lucide-react";

export default function OrderTimeLine({ order }: { order: any }) {
    const allStatuses = [
        "PLACED",
        "PACKED",
        "CONFIRMED",
        "ASSIGNED",
        "OUT_FOR_DELIVERY",
        "DELIVERED"
    ];

    const currentIdx = allStatuses.indexOf(order.status);

    const statusIcons: any = {
        PLACED: ClockIcon,
        PACKED: PackageIcon,
        CONFIRMED: CheckIcon,
        ASSIGNED: TruckIcon,
        OUT_FOR_DELIVERY: TruckIcon,
        DELIVERED: CheckIcon,
    };

    return (
        <div className="bg-white rounded-2xl p-6 shadow-sm">
            <h2 className="text-lg font-semibold mb-6">
                Order Timeline
            </h2>

            <div>
                {allStatuses.map((status, i) => {
                    const Icon = statusIcons[status];

                    const isCompleted = i <= currentIdx;
                    const isCurrent = i === currentIdx;

                    const historyEntry = order.statusHistory?.find(
                        (h: any) => h.status === status
                    );

                    const validDate = historyEntry?.changedAt
                        ? new Date(historyEntry.changedAt)
                        : null;

                    const hasValidDate =
                        validDate && !isNaN(validDate.getTime());

                    const label = status === "OUT_FOR_DELIVERY"
                        ? "Out for Delivery"
                        : status.charAt(0) + status.slice(1).toLowerCase();

                    return (
                        <div key={status} className="flex gap-4">
                            <div className="flex flex-col items-center">
                                <div
                                    className={`w-10 h-10 rounded-full flex items-center justify-center ${
                                        isCompleted
                                            ? "bg-app-green text-white"
                                            : "bg-gray-200 text-gray-400"
                                    } ${
                                        isCurrent
                                            ? "ring-4 ring-app-green/20"
                                            : ""
                                    }`}
                                >
                                    <Icon size={20} />
                                </div>

                                {i < allStatuses.length - 1 && (
                                    <div
                                        className={`w-0.5 h-12 ${
                                            i < currentIdx
                                                ? "bg-app-green"
                                                : "bg-gray-200"
                                        }`}
                                    />
                                )}
                            </div>

                            <div className="pb-8">
                                <h3
                                    className={`font-medium ${
                                        isCompleted
                                            ? "text-gray-900"
                                            : "text-gray-400"
                                    }`}
                                >
                                    {label}
                                </h3>

                                {isCompleted &&
                                    hasValidDate && (
                                        <p className="text-xs text-gray-500 mt-1">
                                            {validDate.toLocaleString("en-US", {
                                                month: "short",
                                                day: "numeric",
                                                year: "numeric",
                                                hour: "2-digit",
                                                minute: "2-digit"
                                            })}
                                        </p>
                                    )}
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}
