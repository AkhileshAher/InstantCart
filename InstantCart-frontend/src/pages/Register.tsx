import {   Eye,EyeOff,Loader2Icon,Mail,Phone,ShieldCheck,Store,UserRound} from "lucide-react";
import { useState, type SubmitEvent } from "react";
import { useAuth } from "../context/AuthContext";
import toast from "react-hot-toast";
import logo from "../assets/Logo.png";

type UserRole = "CUSTOMER" | "VENDOR";

function Register() {

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [phone, setPhone] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [role, setRole] = useState<UserRole>("CUSTOMER");

    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const [loading, setLoading] = useState(false);

    const { register } = useAuth();

    const handleSubmit = async (e: SubmitEvent) => {

        e.preventDefault();

        if (password !== confirmPassword) {
            toast.error("Passwords do not match");
            return;
        }

        if (password.length < 8) {
            toast.error("Password must contain at least 8 characters");
            return;
        }

        if (!/^\+91[6-9]\d{9}$/.test(phone)) {
            toast.error("Enter a valid Indian phone number");
            return;
        }

        setLoading(true);

        try {

            await register(
                name,
                email,
                password,
                phone,
                role
            );

        } catch (error: any) {

            toast.error(
                error?.response?.data?.message ||
                error?.message ||
                "Registration failed"
            );

        } finally {

            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen w-full bg-gray-50 flex items-center justify-center p-4 lg:p-8">

            <div className="w-full max-w-6xl bg-white rounded-3xl shadow-xl overflow-hidden flex">

                {/* Left side */}

                <div className="hidden lg:block lg:w-[42%] relative">

                    <img
                        src={logo}
                        alt="InstantCart"
                        className="absolute inset-0 w-full h-full object-cover"
                    />

                    <div className="absolute inset-0 bg-black/35" />

                    <div className="absolute inset-0 flex flex-col justify-end p-10 text-white">

                        <div className="mb-3 inline-flex items-center gap-2">
                            <ShieldCheck size={20} />
                            <span className="text-sm font-medium">
                                Secure Account Creation
                            </span>
                        </div>

                        <h1 className="text-4xl font-semibold leading-tight">
                            Join InstantCart
                        </h1>

                        <p className="mt-4 text-white/80 max-w-sm leading-relaxed">
                            Shop smarter or grow your business with InstantCart.
                            Create your account and get started in minutes.
                        </p>

                    </div>

                </div>

                {/* Right side */}

                <div className="w-full lg:w-[58%] px-6 py-8 sm:px-10 lg:px-12">

                    <form
                        onSubmit={handleSubmit}
                        className="max-w-xl mx-auto"
                    >

                        {/* Header */}

                        <div className="mb-8">

                            <p className="text-sm font-medium text-indigo-500">
                                Welcome to InstantCart
                            </p>

                            <h2 className="text-3xl sm:text-4xl font-semibold text-gray-900 mt-2">
                                Create your account
                            </h2>

                            <p className="text-sm text-gray-500 mt-3">
                                Enter your details to get started.
                            </p>

                        </div>

                        {/* Name + Email */}

                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">

                            <div>
                                <label
                                    htmlFor="name"
                                    className="block text-sm font-medium text-gray-700 mb-2"
                                >
                                    Full name
                                </label>

                                <div className="flex items-center h-12 px-4 border border-gray-200 rounded-xl bg-gray-50 focus-within:border-indigo-500 focus-within:ring-2 focus-within:ring-indigo-500/10 transition">

                                    <UserRound
                                        size={18}
                                        className="text-gray-400 mr-3"
                                    />

                                    <input
                                        id="name"
                                        type="text"
                                        value={name}
                                        placeholder="Enter your name"
                                        onChange={(e) =>
                                            setName(e.target.value)
                                        }
                                        className="w-full bg-transparent outline-none text-sm text-gray-800 placeholder:text-gray-400"
                                        required
                                    />

                                </div>
                            </div>

                            <div>
                                <label
                                    htmlFor="email"
                                    className="block text-sm font-medium text-gray-700 mb-2"
                                >
                                    Email address
                                </label>

                                <div className="flex items-center h-12 px-4 border border-gray-200 rounded-xl bg-gray-50 focus-within:border-indigo-500 focus-within:ring-2 focus-within:ring-indigo-500/10 transition">

                                    <Mail
                                        size={18}
                                        className="text-gray-400 mr-3"
                                    />

                                    <input
                                        id="email"
                                        type="email"
                                        value={email}
                                        placeholder="you@example.com"
                                        onChange={(e) =>
                                            setEmail(e.target.value)
                                        }
                                        className="w-full bg-transparent outline-none text-sm text-gray-800 placeholder:text-gray-400"
                                        required
                                    />

                                </div>
                            </div>

                        </div>

                        {/* Phone */}

                        <div className="mt-5">

                            <label
                                htmlFor="phone"
                                className="block text-sm font-medium text-gray-700 mb-2"
                            >
                                Phone number
                            </label>

                            <div className="flex items-center h-12 px-4 border border-gray-200 rounded-xl bg-gray-50 focus-within:border-indigo-500 focus-within:ring-2 focus-within:ring-indigo-500/10 transition">

                                <Phone
                                    size={18}
                                    className="text-gray-400 mr-3"
                                />

                                <input
                                    id="phone"
                                    type="tel"
                                    value={phone}
                                    placeholder="+917412345678"
                                    onChange={(e) =>
                                        setPhone(e.target.value)
                                    }
                                    className="w-full bg-transparent outline-none text-sm text-gray-800 placeholder:text-gray-400"
                                    required
                                />

                            </div>

                            <p className="text-xs text-gray-400 mt-2">
                                Use format: +91XXXXXXXXXX
                            </p>

                        </div>

                        {/* Role */}

                        <div className="mt-6">

                            <label className="block text-sm font-medium text-gray-700 mb-3">
                                Choose account type
                            </label>

                            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">

                                {/* Customer */}

                                <button
                                    type="button"
                                    onClick={() => setRole("CUSTOMER")}
                                    className={`text-left rounded-2xl border p-4 transition ${
                                        role === "CUSTOMER"
                                            ? "border-indigo-500 bg-indigo-50 ring-2 ring-indigo-500/10"
                                            : "border-gray-200 bg-white hover:border-gray-300"
                                    }`}
                                >

                                    <div className="flex items-start justify-between">

                                        <div className="flex items-center gap-3">

                                            <div
                                                className={`h-10 w-10 rounded-xl flex items-center justify-center ${
                                                    role === "CUSTOMER"
                                                        ? "bg-indigo-500 text-white"
                                                        : "bg-gray-100 text-gray-500"
                                                }`}
                                            >
                                                <UserRound size={19} />
                                            </div>

                                            <div>
                                                <h3 className="font-semibold text-gray-900">
                                                    Customer
                                                </h3>

                                                <p className="text-xs text-gray-500 mt-1">
                                                    Shop and manage orders
                                                </p>
                                            </div>

                                        </div>

                                        <div
                                            className={`mt-1 h-4 w-4 rounded-full border flex items-center justify-center ${
                                                role === "CUSTOMER"
                                                    ? "border-indigo-500"
                                                    : "border-gray-300"
                                            }`}
                                        >
                                            {role === "CUSTOMER" && (
                                                <div className="h-2 w-2 rounded-full bg-indigo-500" />
                                            )}
                                        </div>

                                    </div>

                                </button>

                                {/* Vendor */}

                                <button
                                    type="button"
                                    onClick={() => setRole("VENDOR")}
                                    className={`text-left rounded-2xl border p-4 transition ${
                                        role === "VENDOR"
                                            ? "border-indigo-500 bg-indigo-50 ring-2 ring-indigo-500/10"
                                            : "border-gray-200 bg-white hover:border-gray-300"
                                    }`}
                                >

                                    <div className="flex items-start justify-between">

                                        <div className="flex items-center gap-3">

                                            <div
                                                className={`h-10 w-10 rounded-xl flex items-center justify-center ${
                                                    role === "VENDOR"
                                                        ? "bg-indigo-500 text-white"
                                                        : "bg-gray-100 text-gray-500"
                                                }`}
                                            >
                                                <Store size={19} />
                                            </div>

                                            <div>
                                                <h3 className="font-semibold text-gray-900">
                                                    Vendor
                                                </h3>

                                                <p className="text-xs text-gray-500 mt-1">
                                                    Sell and manage products
                                                </p>
                                            </div>

                                        </div>

                                        <div
                                            className={`mt-1 h-4 w-4 rounded-full border flex items-center justify-center ${
                                                role === "VENDOR"
                                                    ? "border-indigo-500"
                                                    : "border-gray-300"
                                            }`}
                                        >
                                            {role === "VENDOR" && (
                                                <div className="h-2 w-2 rounded-full bg-indigo-500" />
                                            )}
                                        </div>

                                    </div>

                                </button>

                            </div>

                        </div>

                        {/* Password */}

                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mt-5">

                            <div>

                                <label
                                    htmlFor="password"
                                    className="block text-sm font-medium text-gray-700 mb-2"
                                >
                                    Password
                                </label>

                                <div className="flex items-center h-12 px-4 border border-gray-200 rounded-xl bg-gray-50 focus-within:border-indigo-500 focus-within:ring-2 focus-within:ring-indigo-500/10 transition">

                                    <input
                                        id="password"
                                        type={
                                            showPassword
                                                ? "text"
                                                : "password"
                                        }
                                        value={password}
                                        placeholder="Minimum 8 characters"
                                        onChange={(e) =>
                                            setPassword(e.target.value)
                                        }
                                        className="w-full bg-transparent outline-none text-sm text-gray-800 placeholder:text-gray-400"
                                        required
                                    />

                                    <button
                                        type="button"
                                        onClick={() =>
                                            setShowPassword(!showPassword)
                                        }
                                        className="text-gray-400 hover:text-gray-600"
                                    >
                                        {showPassword ? (
                                            <EyeOff size={18} />
                                        ) : (
                                            <Eye size={18} />
                                        )}
                                    </button>

                                </div>

                            </div>

                            {/* Confirm Password */}

                            <div>

                                <label
                                    htmlFor="confirmPassword"
                                    className="block text-sm font-medium text-gray-700 mb-2"
                                >
                                    Confirm password
                                </label>

                                <div className="flex items-center h-12 px-4 border border-gray-200 rounded-xl bg-gray-50 focus-within:border-indigo-500 focus-within:ring-2 focus-within:ring-indigo-500/10 transition">

                                    <input
                                        id="confirmPassword"
                                        type={
                                            showConfirmPassword
                                                ? "text"
                                                : "password"
                                        }
                                        value={confirmPassword}
                                        placeholder="Re-enter password"
                                        onChange={(e) =>
                                            setConfirmPassword(e.target.value)
                                        }
                                        className="w-full bg-transparent outline-none text-sm text-gray-800 placeholder:text-gray-400"
                                        required
                                    />

                                    <button
                                        type="button"
                                        onClick={() =>
                                            setShowConfirmPassword(
                                                !showConfirmPassword
                                            )
                                        }
                                        className="text-gray-400 hover:text-gray-600"
                                    >
                                        {showConfirmPassword ? (
                                            <EyeOff size={18} />
                                        ) : (
                                            <Eye size={18} />
                                        )}
                                    </button>

                                </div>

                            </div>

                        </div>

                        {/* Submit */}

                        <button
                            type="submit"
                            disabled={loading}
                            className="mt-7 w-full h-12 rounded-xl bg-indigo-500 hover:bg-indigo-600 disabled:opacity-60 disabled:cursor-not-allowed text-white font-medium transition flex items-center justify-center gap-2"
                        >

                            {loading ? (
                                <>
                                    <Loader2Icon
                                        size={19}
                                        className="animate-spin"
                                    />

                                    Creating account...
                                </>
                            ) : (
                                "Create account"
                            )}

                        </button>

                        {/* Login */}

                        <p className="text-center text-sm text-gray-500 mt-5">

                            Already have an account?

                            <a
                                href="/login"
                                className="ml-1 text-indigo-500 font-medium hover:underline"
                            >
                                Sign in
                            </a>

                        </p>

                    </form>

                </div>

            </div>

        </div>
    );
}

export default Register;

