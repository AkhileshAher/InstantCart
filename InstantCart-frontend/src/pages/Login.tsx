import { Eye,EyeOff, Loader2Icon, LockKeyhole, Mail, ShieldCheck} from "lucide-react";
import { useState, type SubmitEvent } from "react";
import { useAuth } from "../context/AuthContext";
import toast from "react-hot-toast";
import logo from "../assets/Logo.png";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [showPassword, setShowPassword] = useState(false);
  const [rememberMe, setRememberMe] = useState(false);

  const [loading, setLoading] = useState(false);

  const { login } = useAuth();

  const handleSubmit = async (e: SubmitEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      await login(email, password);
    } catch (error: any) {
      toast.error(
        error?.response?.data?.message || error?.message || "Login failed",
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen w-full bg-gray-50 flex items-center justify-center p-4 lg:p-8">
      <div className="w-full max-w-6xl bg-white rounded-3xl shadow-xl overflow-hidden flex">
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
                Secure Shopping Experience
              </span>
            </div>

            <h1 className="text-4xl font-semibold leading-tight">
              Welcome back
            </h1>

            <p className="mt-4 text-white/80 max-w-sm leading-relaxed">
              Sign in to access your orders, manage your account, and continue
              shopping with InstantCart.
            </p>
          </div>
        </div>

        <div className="w-full lg:w-[58%] px-6 py-10 sm:px-10 lg:px-14 flex items-center">
          <form onSubmit={handleSubmit} className="w-full max-w-md mx-auto">
            {/* Header */}

            <div className="mb-8">
              <p className="text-sm font-medium text-indigo-500">
                Welcome back to InstantCart
              </p>

              <h2 className="text-3xl sm:text-4xl font-semibold text-gray-900 mt-2">
                Sign in to your account
              </h2>

              <p className="text-sm text-gray-500 mt-3">
                Enter your email and password to continue.
              </p>
            </div>

            <button
              type="button"
              className="w-full h-12 rounded-xl border border-gray-200 bg-white hover:bg-gray-50 transition flex items-center justify-center gap-3 text-sm font-medium text-gray-700"
            >
              <svg
                width="20"
                height="20"
                viewBox="0 0 48 48"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  fill="#FFC107"
                  d="M43.611 20.083H42V20H24v8h11.303c-1.649 4.657-6.08 8-11.303 8-6.627 0-12-5.373-12-12s5.373-12 12-12c3.059 0 5.842 1.154 7.961 3.039l5.657-5.657C34.046 6.053 29.268 4 24 4 12.955 4 4 12.955 4 24s8.955 20 20 20 20-8.955 20-20c0-1.341-.138-2.65-.389-3.917z"
                />

                <path
                  fill="#FF3D00"
                  d="M6.306 14.691l6.571 4.819C14.655 15.108 18.961 12 24 12c3.059 0 5.842 1.154 7.961 3.039l5.657-5.657C34.046 6.053 29.268 4 24 4c-7.682 0-14.344 4.337-17.694 10.691z"
                />

                <path
                  fill="#4CAF50"
                  d="M24 44c5.166 0 9.86-1.977 13.409-5.192l-6.19-5.238C29.211 35.091 26.715 36 24 36c-5.202 0-9.619-3.317-11.283-7.946l-6.522 5.025C9.505 39.556 16.227 44 24 44z"
                />

                <path
                  fill="#1976D2"
                  d="M43.611 20.083H42V20H24v8h11.303c-.792 2.237-2.231 4.166-4.087 5.571l.003-.002 6.19 5.238C36.971 39.205 44 34 44 24c0-1.341-.138-2.65-.389-3.917z"
                />
              </svg>
              Continue with Google
            </button>

            <div className="flex items-center gap-4 my-6">
              <div className="flex-1 h-px bg-gray-200" />

              <span className="text-xs text-gray-400 whitespace-nowrap">
                OR CONTINUE WITH EMAIL
              </span>

              <div className="flex-1 h-px bg-gray-200" />
            </div>

            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                Email address
              </label>

              <div className="flex items-center h-12 px-4 border border-gray-200 rounded-xl bg-gray-50 focus-within:bg-white focus-within:border-indigo-500 focus-within:ring-2 focus-within:ring-indigo-500/10 transition">
                <Mail size={18} className="text-gray-400 mr-3 shrink-0" />

                <input
                  id="email"
                  type="email"
                  value={email}
                  placeholder="you@example.com"
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full bg-transparent outline-none text-sm text-gray-800 placeholder:text-gray-400"
                  autoComplete="email"
                  required
                />
              </div>
            </div>

            <div className="mt-5">
              <div className="flex items-center justify-between mb-2">
                <label
                  htmlFor="password"
                  className="text-sm font-medium text-gray-700"
                >
                  Password
                </label>

                <a
                  href="/forgot-password"
                  className="text-xs font-medium text-indigo-500 hover:text-indigo-600"
                >
                  Forgot password?
                </a>
              </div>

              <div className="flex items-center h-12 px-4 border border-gray-200 rounded-xl bg-gray-50 focus-within:bg-white focus-within:border-indigo-500 focus-within:ring-2 focus-within:ring-indigo-500/10 transition">
                <LockKeyhole
                  size={18}
                  className="text-gray-400 mr-3 shrink-0"
                />

                <input
                  id="password"
                  type={showPassword ? "text" : "password"}
                  value={password}
                  placeholder="Enter your password"
                  onChange={(e) => setPassword(e.target.value)}
                  className="w-full bg-transparent outline-none text-sm text-gray-800 placeholder:text-gray-400"
                  autoComplete="current-password"
                  required
                />

                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="text-gray-400 hover:text-gray-600 transition"
                  aria-label={showPassword ? "Hide password" : "Show password"}
                >
                  {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                </button>
              </div>
            </div>

            <div className="flex items-center justify-between mt-5">
              <label className="flex items-center gap-2 cursor-pointer">
                <input
                  type="checkbox"
                  checked={rememberMe}
                  onChange={(e) => setRememberMe(e.target.checked)}
                  className="h-4 w-4 rounded border-gray-300 text-indigo-500 focus:ring-indigo-500"
                />

                <span className="text-sm text-gray-500">Remember me</span>
              </label>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="mt-7 w-full h-12 rounded-xl bg-indigo-500 hover:bg-indigo-600 disabled:opacity-60 disabled:cursor-not-allowed text-white font-medium transition flex items-center justify-center gap-2"
            >
              {loading ? (
                <>
                  <Loader2Icon size={19} className="animate-spin" />
                  Signing in...
                </>
              ) : (
                "Sign in"
              )}
            </button>

            <div className="mt-6 pt-6 border-t border-gray-100">
              <p className="text-center text-sm text-gray-500">
                Don't have an account?
                <a
                  href="/register"
                  className="ml-1 text-indigo-500 font-medium hover:underline"
                >
                  Create an account
                </a>
              </p>
            </div>

            <div className="mt-5 flex items-center justify-center gap-2 text-xs text-gray-400">
              <ShieldCheck size={15} />
              Your account is protected by secure authentication.
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Login;
