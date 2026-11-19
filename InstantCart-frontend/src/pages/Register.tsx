
import {
    Eye,
    EyeOff,
    Loader2,
    Mail,
    Phone,
    ShieldCheck,
    ShoppingBag,
    UserRound,
    ArrowRight,
    LockKeyhole,
} from "lucide-react";
import { useState, type FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import toast from "react-hot-toast";
import logo from "../assets/Logo.png";

function Register() {
    const navigate = useNavigate();
    const { register } = useAuth();

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [phone, setPhone] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const normalizedEmail = email.trim().toLowerCase();
        const normalizedPhone = phone.trim();

        if (password !== confirmPassword) {
            toast.error("Passwords do not match");
            return;
        }

        if (password.length < 8) {
            toast.error("Password must contain at least 8 characters");
            return;
        }

        if (!/^\+91[6-9]\d{9}$/.test(normalizedPhone)) {
            toast.error("Enter a valid Indian phone number with +91");
            return;
        }

        setLoading(true);

        try {
            await register(
                name.trim(),
                normalizedEmail,
                password,
                normalizedPhone
            );

            toast.success("Verification code sent to your email!");

            navigate("/verify-otp", {
                state: { email: normalizedEmail },
                replace: true,
            });
        } catch (error: unknown) {
            const err = error as {
                response?: { data?: { message?: string } };
                message?: string;
            };

            toast.error(
                err.response?.data?.message ||
                err.message ||
                "Registration failed"
            );
        } finally {
            setLoading(false);
        }
    };

    const inputWrapper =
        "flex h-12 items-center gap-3 rounded-xl border border-slate-200 bg-slate-50/80 px-3.5 transition-all focus-within:border-indigo-500 focus-within:bg-white focus-within:ring-4 focus-within:ring-indigo-500/10";

    const inputClass =
        "w-full min-w-0 bg-transparent text-sm text-slate-800 outline-none placeholder:text-slate-400";

    const labelClass =
        "mb-1.5 block text-xs font-semibold tracking-wide text-slate-700";

    return (
        <main className="flex min-h-screen items-center justify-center bg-[#f5f6fc] p-3 sm:p-6">
            <div className="grid w-full max-w-6xl overflow-hidden rounded-[28px] border border-white bg-white shadow-[0_24px_80px_-24px_rgba(30,41,59,0.22)] lg:min-h-[720px] lg:grid-cols-[0.9fr_1.1fr]">

                {/* Left branding panel */}
                <section className="relative hidden overflow-hidden bg-slate-950 p-10 text-white lg:flex lg:flex-col lg:justify-between xl:p-14">

                    <div className="absolute inset-0">
                        <img
                            src={logo}
                            alt=""
                            className="h-full w-full object-cover opacity-35"
                        />
                        <div className="absolute inset-0 bg-gradient-to-br from-indigo-950/90 via-slate-950/80 to-slate-950/95" />
                    </div>

                    <div className="relative z-10">
                        <div className="flex items-center gap-3">
                            <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-white/10 ring-1 ring-white/20 backdrop-blur">
                                <ShoppingBag size={22} />
                            </div>
                            <span className="text-xl font-bold tracking-tight">
                                InstantCart
                            </span>
                        </div>
                    </div>

                    <div className="relative z-10 max-w-md">
                        <div className="mb-6 inline-flex items-center gap-2 rounded-full border border-white/15 bg-white/10 px-4 py-2 text-xs font-medium text-indigo-100 backdrop-blur">
                            <ShieldCheck size={15} />
                            Shopping made simpler and smarter
                        </div>

                        <h1 className="text-4xl font-bold leading-[1.2] xl:text-5xl">
                            Everything you need,
                            <span className="mt-2 block text-indigo-300">
                                just a cart away.
                            </span>
                        </h1>

                        <p className="mt-6 max-w-sm text-sm leading-7 text-slate-300">
                            Create your InstantCart account and discover a
                            smoother way to shop, manage orders, and enjoy
                            a seamless shopping experience.
                        </p>

                        <div className="mt-10 flex items-center gap-3">
                            <div className="flex -space-x-2">
                                <div className="h-9 w-9 rounded-full border-2 border-slate-900 bg-indigo-400" />
                                <div className="h-9 w-9 rounded-full border-2 border-slate-900 bg-violet-400" />
                                <div className="h-9 w-9 rounded-full border-2 border-slate-900 bg-cyan-400" />
                            </div>
                            <p className="text-xs text-slate-300">
                                Your shopping journey starts here.
                            </p>
                        </div>
                    </div>

                    <p className="relative z-10 text-xs text-slate-400">
                        © {new Date().getFullYear()} InstantCart. All rights reserved.
                    </p>
                </section>

                {/* Signup form */}
                <section className="flex items-center justify-center px-5 py-8 sm:px-10 lg:px-12 xl:px-16">
                    <div className="w-full max-w-md">

                        <div className="mb-8">
                            <div className="mb-6 flex items-center gap-2 lg:hidden">
                                <img
                                    src={logo}
                                    alt="InstantCart"
                                    className="h-10 w-10 rounded-xl object-cover"
                                />
                                <span className="text-lg font-bold text-slate-900">
                                    InstantCart
                                </span>
                            </div>

                            <div className="mb-3 inline-flex items-center gap-2 rounded-full bg-indigo-50 px-3 py-1.5 text-xs font-semibold text-indigo-600">
                                <ShieldCheck size={14} />
                                Create your account
                            </div>

                            <h2 className="text-3xl font-bold tracking-tight text-slate-900">
                                Get started today
                            </h2>

                            <p className="mt-2 text-sm leading-6 text-slate-500">
                                Join InstantCart and make your shopping experience better.
                            </p>
                        </div>

                        <form onSubmit={handleSubmit} className="space-y-4">

                            {/* Name */}
                            <div>
                                <label htmlFor="name" className={labelClass}>
                                    Full name
                                </label>
                                <div className={inputWrapper}>
                                    <UserRound size={18} className="shrink-0 text-slate-400" />
                                    <input
                                        id="name"
                                        type="text"
                                        value={name}
                                        onChange={(e) => setName(e.target.value)}
                                        placeholder="Enter your full name"
                                        autoComplete="name"
                                        className={inputClass}
                                        required
                                        maxLength={100}
                                    />
                                </div>
                            </div>

                            {/* Email */}
                            <div>
                                <label htmlFor="email" className={labelClass}>
                                    Email address
                                </label>
                                <div className={inputWrapper}>
                                    <Mail size={18} className="shrink-0 text-slate-400" />
                                    <input
                                        id="email"
                                        type="email"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        placeholder="you@example.com"
                                        autoComplete="email"
                                        className={inputClass}
                                        required
                                    />
                                </div>
                            </div>

                            {/* Phone */}
                            <div>
                                <label htmlFor="phone" className={labelClass}>
                                    Phone number
                                </label>
                                <div className={inputWrapper}>
                                    <Phone size={18} className="shrink-0 text-slate-400" />
                                    <input
                                        id="phone"
                                        type="tel"
                                        value={phone}
                                        onChange={(e) => setPhone(e.target.value)}
                                        placeholder="+919876543210"
                                        autoComplete="tel"
                                        className={inputClass}
                                        required
                                    />
                                </div>
                            </div>

                            {/* Password */}
                            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                                <div>
                                    <label htmlFor="password" className={labelClass}>
                                        Password
                                    </label>
                                    <div className={inputWrapper}>
                                        <LockKeyhole size={17} className="shrink-0 text-slate-400" />
                                        <input
                                            id="password"
                                            type={showPassword ? "text" : "password"}
                                            value={password}
                                            onChange={(e) => setPassword(e.target.value)}
                                            placeholder="Min. 8 characters"
                                            autoComplete="new-password"
                                            className={inputClass}
                                            minLength={8}
                                            required
                                        />
                                        <button
                                            type="button"
                                            onClick={() => setShowPassword(!showPassword)}
                                            className="shrink-0 text-slate-400 hover:text-indigo-600"
                                            aria-label="Toggle password visibility"
                                        >
                                            {showPassword ? <EyeOff size={17} /> : <Eye size={17} />}
                                        </button>
                                    </div>
                                </div>

                                <div>
                                    <label htmlFor="confirmPassword" className={labelClass}>
                                        Confirm password
                                    </label>
                                    <div className={inputWrapper}>
                                        <LockKeyhole size={17} className="shrink-0 text-slate-400" />
                                        <input
                                            id="confirmPassword"
                                            type={showConfirmPassword ? "text" : "password"}
                                            value={confirmPassword}
                                            onChange={(e) => setConfirmPassword(e.target.value)}
                                            placeholder="Re-enter password"
                                            autoComplete="new-password"
                                            className={inputClass}
                                            required
                                        />
                                        <button
                                            type="button"
                                            onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                            className="shrink-0 text-slate-400 hover:text-indigo-600"
                                            aria-label="Toggle confirm password visibility"
                                        >
                                            {showConfirmPassword ? <EyeOff size={17} /> : <Eye size={17} />}
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <p className="text-xs leading-5 text-slate-400">
                                By creating an account, you'll receive a verification
                                code at your email address.
                            </p>

                            <button
                                type="submit"
                                disabled={loading}
                                className="group flex h-12 w-full items-center justify-center gap-2 rounded-xl bg-indigo-600 text-sm font-semibold text-white shadow-lg shadow-indigo-600/20 transition hover:bg-indigo-700 hover:shadow-indigo-600/30 disabled:cursor-not-allowed disabled:opacity-60"
                            >
                                {loading ? (
                                    <>
                                        <Loader2 size={18} className="animate-spin" />
                                        Creating account...
                                    </>
                                ) : (
                                    <>
                                        Create account
                                        <ArrowRight size={17} className="transition-transform group-hover:translate-x-1" />
                                    </>
                                )}
                            </button>
                        </form>

                        <div className="my-6 flex items-center gap-3">
                            <div className="h-px flex-1 bg-slate-200" />
                            <span className="text-xs text-slate-400">OR</span>
                            <div className="h-px flex-1 bg-slate-200" />
                        </div>

                        <p className="text-center text-sm text-slate-500">
                            Already have an account?
                            <Link
                                to="/login"
                                className="ml-1.5 font-semibold text-indigo-600 hover:text-indigo-700"
                            >
                                Sign in
                            </Link>
                        </p>

                        <div className="mt-8 flex items-center justify-center gap-2 text-xs text-slate-400">
                            <ShieldCheck size={14} />
                            Your account and information are protected.
                        </div>
                    </div>
                </section>
            </div>
        </main>
    );
}

export default Register;
