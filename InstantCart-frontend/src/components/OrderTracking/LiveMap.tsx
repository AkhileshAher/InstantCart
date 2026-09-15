import { useEffect } from "react";
import L from "leaflet";
import {
    MapContainer,
    Marker,
    Popup,
    TileLayer,
    useMap
} from "react-leaflet";

import "leaflet/dist/leaflet.css";

export default function LiveMap({
    order,
    liveLocation
}: {
    order: any;
    liveLocation: any;
}) {

    const truckIcon = new L.Icon({
        iconUrl: "https://cdn-icons-png.flaticon.com/512/3097/3097144.png",
        iconSize: [40, 40],
        iconAnchor: [20, 40]
    });

    const destinationIcon = new L.Icon({
        iconUrl: "https://cdn-icons-png.flaticon.com/512/684/684908.png",
        iconSize: [35, 35],
        iconAnchor: [17, 35]
    });

    function MapUpdater({
        center
    }: {
        center: [number, number]
    }) {
        const map = useMap();

        useEffect(() => {
            map.setView(center, map.getZoom());
        }, [center, map]);

        return null;
    }

    if (
        order.status === "DELIVERED" ||
        order.status === "CANCELLED"
    ) {
        return null;
    }

    const destination =
        order.shippingAddress?.lat != null &&
        order.shippingAddress?.lng != null
            ? [
                order.shippingAddress.lat,
                order.shippingAddress.lng
            ] as [number, number]
            : null;

    const location =
        liveLocation?.lat != null &&
        liveLocation?.lng != null
            ? [
                liveLocation.lat,
                liveLocation.lng
            ] as [number, number]
            : null;

    const center = location || destination;

    if (!center) {
        return (
            <div className="bg-white rounded-2xl p-6 shadow-sm">
                <p className="text-gray-500">
                    Delivery location is not available yet.
                </p>
            </div>
        );
    }

    return (
        <div className="bg-white rounded-2xl p-4 shadow-sm">
            <h2 className="text-lg font-semibold mb-4">
                Live Delivery Location
            </h2>

            <div className="h-[400px] rounded-xl overflow-hidden">
                <MapContainer
                    center={center}
                    zoom={15}
                    scrollWheelZoom={false}
                    className="h-full w-full"
                >
                    <TileLayer
                        attribution='&copy; OpenStreetMap contributors'
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />

                    <MapUpdater center={center} />

                    {location && (
                        <Marker
                            position={location}
                            icon={truckIcon}
                        >
                            <Popup>
                                Delivery partner location
                            </Popup>
                        </Marker>
                    )}

                    {destination && (
                        <Marker
                            position={destination}
                            icon={destinationIcon}
                        >
                            <Popup>
                                Delivery destination
                            </Popup>
                        </Marker>
                    )}
                </MapContainer>
            </div>
        </div>
    );
}

