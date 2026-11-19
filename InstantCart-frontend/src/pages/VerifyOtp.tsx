
import {
    ArrowLeft,
    ArrowRight,
    CheckCircle2,
    Loader2,
    Mail,
    ShieldCheck,
} from "lucide-react";
import { useEffect, useRef, useState, type ClipboardEvent, type FormEvent, type KeyboardEvent } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import toast from "react-hot-toast";
import api from "../config/api";

function VerifyOtp() {
    const navigate = useNavigate();
    const location = useLocation();

    const { refreshUserData } = useAuth();

    const email = location.state?.email as string | undefined;

    const [otp, setOtp] = useState(["", "", "", "", "", ""]);
    const [loading, setLoading] = useState(false);

    const inputRefs = useRef<(HTMLInputElement | null)[]>([]);

    useEffect(() => {
        if (!email) {
            navigate("/register", { replace: true });
            return;
        }

        inputRefs.current[0]?.focus();
    }, [email, navigate]);

    const handleOtpChange = (index: number, value: string) => {
        const digit = value.replace(/\D/g, "").slice(-1);

        const updatedOtp = [...otp];
        updatedOtp[index] = digit;

        setOtp(updatedOtp);

        if (digit && index < 5) {
            inputRefs.current[index + 1]?.focus();
        }
    };

    const handleKeyDown = (
        index: number,
        e: KeyboardEvent<HTMLInputElement>
    ) => {
        if (e.key === "Backspace" && !otp[index] && index > 0) {
            inputRefs.current[index - 1]?.focus();
        }

        if (e.key === "ArrowLeft" && index > 0) {
            inputRefs.current[index - 1]?.focus();
        }

        if (e.key === "ArrowRight" && index < 5) {
            inputRefs.current[index + 1]?.focus();
        }
    };

    const handlePaste = (e: ClipboardEvent<HTMLInputElement>) => {
        e.preventDefault();

        const pastedOtp = e.clipboardData
            .getData("text")
            .replace(/\D/g, "")
            .slice(0, 6);

        if (!pastedOtp) return;

        const updatedOtp = Array(6).fill("");

        pastedOtp.split("").forEach((digit, index) => {
            updatedOtp[index] = digit;
        });

        setOtp(updatedOtp);

        const nextIndex = Math.min(pastedOtp.length, 5);
        inputRefs.current[nextIndex]?.focus();
    };

    const handleVerify = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const enteredOtp = otp.join("");

        if (enteredOtp.length !== 6) {
            toast.error("Please enter the complete 6-digit OTP");
            return;
        }

        setLoading(true);

        try {
            await api.post(
                "/api/auth/verify-registration",
                {
                    email,
                    otp: enteredOtp,
                },
                {
                    withCredentials: true,
                }
            );

            toast.success("Email verified successfully!");

            // Fetch the logged-in user using the newly set cookies
            await refreshUserData();

            navigate("/", { replace: true });

        } catch (error: unknown) {
            const err = error as {
                response?: { data?: { message?: string } };
                message?: string;
            };

            toast.error(
                err.response?.data?.message ||
                err.message ||
                "OTP verification failed"
            );

            setOtp(["", "", "", "", "", ""]);
            inputRefs.current[0]?.focus();

        } finally {
            setLoading(false);
        }
    };

    return (
        <main className="flex min-h-screen items-center justify-center bg-[#f5f6fc] p-4">

            <div className="w-full max-w-md overflow-hidden rounded-[28px] border border-white bg-white shadow-[0_24px_80px_-24px_rgba(30,41,59,0.2)]">

                {/* Top section */}
                <div className="relative overflow-hidden bg-gradient-to-br from-indigo-600 via-indigo-600 to-violet-700 px-7 py-9 text-center text-white sm:px-10">

                    <div className="absolute -right-12 -top-16 h-48 w-48 rounded-full border border-white/10" />
                    <div className="absolute -bottom-20 -left-12 h-48 w-48 rounded-full border border-white/10" />

                    <div className="relative mx-auto mb-5 flex h-16 w-16 items-center justify-center rounded-2xl border border-white/20 bg-white/15 shadow-lg backdrop-blur">
                        <Mail size={29} />
                    </div>

                    <h1 className="relative text-2xl font-bold sm:text-3xl">
                        Verify your email
                    </h1>

                    <p className="relative mx-auto mt-3 max-w-xs text-sm leading-6 text-indigo-100">
                        One quick step to secure your account and start shopping.
                    </p>
                </div>

                {/* Form */}
                <div className="px-6 py-8 sm:px-9">

                    <div className="mb-7 text-center">
                        <div className="mx-auto mb-3 flex h-12 w-12 items-center justify-center rounded-2xl bg-indigo-50 text-indigo-600">
                            <ShieldCheck size={23} />
                        </div>

                        <p className="text-sm text-slate-500">
                            We've sent a 6-digit verification code to
                        </p>

                        <p className="mt-1 break-all text-sm font-semibold text-slate-900">
                            {email}
                        </p>
                    </div>

                    <form onSubmit={handleVerify}>

                        <label className="mb-4 block text-center text-xs font-semibold uppercase tracking-[0.15em] text-slate-500">
                            Enter verification code
                        </label>

                        <div className="mb-7 flex justify-center gap-2 sm:gap-3">
                            {otp.map((digit, index) => (
                                <input
                                    key={index}
                                    ref={(element) => {
                                        inputRefs.current[index] = element;
                                    }}
                                    type="text"
                                    inputMode="numeric"
                                    autoComplete={index === 0 ? "one-time-code" : "off"}
                                    maxLength={1}
                                    value={digit}
                                    onChange={(e) =>
                                        handleOtpChange(index, e.target.value)
                                    }
                                    onKeyDown={(e) =>
                                        handleKeyDown(index, e)
                                    }
                                    onPaste={handlePaste}
                                    aria-label={`OTP digit ${index + 1}`}
                                    className={`h-12 w-11 rounded-xl border text-center text-xl font-bold text-slate-900 outline-none transition-all sm:h-14 sm:w-12 ${
                                        digit
                                            ? "border-indigo-500 bg-indigo-50 ring-2 ring-indigo-500/10"
                                            : "border-slate-200 bg-slate-50 focus:border-indigo-500 focus:bg-white focus:ring-4 focus:ring-indigo-500/10"
                                    }`}
                                    required
                                />
                            ))}
                        </div>

                        <button
                            type="submit"
                            disabled={loading || otp.join("").length !== 6}
                            className="flex h-12 w-full items-center justify-center gap-2 rounded-xl bg-indigo-600 text-sm font-semibold text-white shadow-lg shadow-indigo-600/20 transition hover:bg-indigo-700 disabled:cursor-not-allowed disabled:opacity-50"
                        >
                            {loading ? (
                                <>
                                    <Loader2 size={18} className="animate-spin" />
                                    Verifying...
                                </>
                            ) : (
                                <>
                                    Verify email
                                    <ArrowRight size={17} />
                                </>
                            )}
                        </button>
                    </form>

                    <div className="my-6 flex items-center gap-3">
                        <div className="h-px flex-1 bg-slate-200" />
                        <CheckCircle2 size={16} className="text-emerald-500" />
                        <div className="h-px flex-1 bg-slate-200" />
                    </div>

                    <div className="rounded-xl bg-slate-50 p-4 text-center">
                        <p className="text-xs leading-5 text-slate-500">
                            Your verification code is valid for 5 minutes.
                            Please check your inbox and spam folder.
                        </p>
                    </div>

                    <Link
                        to="/register"
                        className="mt-6 flex items-center justify-center gap-2 text-sm font-medium text-slate-500 transition hover:text-indigo-600"
                    >
                        <ArrowLeft size={16} />
                        Back to registration
                    </Link>

                </div>
            </div>
        </main>
    );
}

export default VerifyOtp;
