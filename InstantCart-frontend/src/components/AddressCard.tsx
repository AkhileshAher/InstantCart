import { CheckIcon, MapPinIcon, PencilIcon, Trash2Icon } from "lucide-react"
import type { Address } from "../types"
import { useState } from "react"
import Alert from "./Alert"
import api from "../config/api"
import toast from "react-hot-toast"

interface AddressCardProps {
    addr: Address,
    onEditHandler: (addr: Address) => void
    setAddresses: (addresses: Address[]) => void
}

const AddressCard = ({ addr, onEditHandler }: AddressCardProps) => {
    const [showAlert, setShowAlert] = useState<boolean>(false);

    const handleDelete = async (id: string) => {
            setShowAlert(true);
    }

      const confirmDelete = async (id: string) => {
        try {
            await api.delete(`/address/${id}`);
            setShowAlert(false);
            window.location.reload();
            toast.success("Address deleted !");
        } catch (error) {
            toast.error(error.response?.data?.message || error?.message);
        }
    };


    return (
        <>
        {showAlert && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40">
                    <Alert
                        message="Do you really want to delete Address? This action cannot be undone."
                        onConfirm={() => confirmDelete(addr.id)}
                        onCancel={() => setShowAlert(false)}
                    />
                </div>
            )}


        <div key={addr.id} className="max-w-3xl bg-white rounded-2xl p-6 flex items-center justify-between">
            {/* left */}
            <div className="flex gap-4">
                <div className="size-10 rounded-xl bg-app-cream flex items-center justify-center shrink-0">
                    <MapPinIcon className="size-5 text-app-green" />
                </div>
                <div className="">
                    <div className="flex items-center gap-2 mb-1">
                        <p className="text-sm font-semibold text-app-green">{addr.label}</p>
                        {addr.isDefault && (
                            <span className="flex items-center justify-center gap-1 px-2.5 py-0.5 text-[10px] font-medium bg-app-green text-white rounded-full">
                                <CheckIcon className="size-2.5" /> Default
                            </span>
                        )}
                    </div>
                    <p className="text-sm text-app-text-light">
                        {addr.address}, {addr.city}, <br />
                        {addr.state}, {addr.zip}
                    </p>
                </div>
            </div>

            {/* right - action buttons */}
            <div className="flex items-center gap-1">

                <button onClick={() => onEditHandler(addr)} className="p-2 text-app-text-light hover:text-app-green hover:bg-app-cream rounded-lg transition-colors">
                    <PencilIcon className="size-4" />
                </button>

                <button onClick={() => handleDelete(addr.id)} className="p-2 text-app-text-light hover:text-app-error hover:bg-red-50 rounded-lg transition-colors">
                    <Trash2Icon className="size-4" />
                </button>

            </div>

        </div>
        </>
    )
}

export default AddressCard
