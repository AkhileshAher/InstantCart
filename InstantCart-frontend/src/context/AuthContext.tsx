import {
  createContext,
  useContext,
  useEffect,
  useState,
  type ReactNode,
} from "react";

import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";

import type { User } from "../types";
import api from "../config/api";

interface AuthContextType {
  user: User | null;
  loading: boolean;

  login: (email: string, password: string) => Promise<void>;

  deliveryLogin: (email: string, password: string) => Promise<void>;

  register: (
    name: string,
    email: string,
    password: string,
    phone: string,
  ) => Promise<void>;

  verifyRegistration: (email: string, otp: string) => Promise<void>;

  logout: (redirectTo?: string) => Promise<void>;

  refreshUser: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const navigate = useNavigate();

  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  // Fetch currently authenticated user
  const refreshUser = async () => {
    try {
      const response = await api.get("/api/auth/me");

      setUser(response.data);
    } catch {
      setUser(null);
    }
  };

  useEffect(() => {
  const handleSessionExpired = () => {
    setUser(null);
  };

  window.addEventListener(
    "auth:session-expired",
    handleSessionExpired
  );

  return () => {
    window.removeEventListener(
      "auth:session-expired",
      handleSessionExpired
    );
  };
}, []);

  // Restore authentication on application startup
  useEffect(() => {
    const initializeAuth = async () => {
      try {
        await refreshUser();
      } finally {
        setLoading(false);
      }
    };

    void initializeAuth();
  }, []);

  // Customer login
  const login = async (email: string, password: string) => {
    try {
      await api.post("/api/auth/login", {
        email,
        password,
      });

      await refreshUser();

      const response = await api.get("/api/auth/me");

      if (!response.data) {
        throw new Error("Unable to fetch user profile");
      }

      setUser(response.data);

      toast.success("Login successful");

      navigate("/");
    } catch (error: any) {
      const message =
        error?.response?.data?.message ||
        error?.response?.data ||
        error?.message ||
        "Login failed";

    //   toast.error(typeof message === "string" ? message : "Login failed");

      throw error;
    }
  };

  // Delivery login
  const deliveryLogin = async (email: string, password: string) => {
    try {
      await api.post("/api/auth/login", {
        email,
        password,
      });

      const response = await api.get("/api/auth/me");

      if (response.data.role !== "DELIVERY") {
        throw new Error("This account is not a delivery account");
      }

      setUser(response.data);

      toast.success("Login successful");

      navigate("/delivery");
    } catch (error: any) {
      const message =
        error?.response?.data?.message ||
        error?.message ||
        "Delivery login failed";

    //   toast.error(message);

      throw error;
    }
  };

  // Registration: backend sends OTP
  const register = async (
    name: string,
    email: string,
    password: string,
    phone: string,
  ) => {
    try {
      const response = await api.post("/api/auth/register", {
        username: name,
        email,
        password,
        phone,
      });

      toast.success(
        typeof response.data === "string"
          ? response.data
          : "OTP sent to your email",
      );
    } catch (error: any) {
      const message =
        error?.response?.data?.message ||
        error?.response?.data ||
        error?.message ||
        "Registration failed";

    //   toast.error(
        // typeof message === "string" ? message : "Registration failed",
    //   );

      throw error;
    }
  };

  // Verify OTP and establish authenticated session
  const verifyRegistration = async (email: string, otp: string) => {
    try {
      await api.post("/api/auth/verify-registration", {
        email,
        otp,
      });

      const response = await api.get("/api/auth/me");

      setUser(response.data);

      toast.success("Registration successful");

      navigate("/");
    } catch (error: any) {
      const message =
        error?.response?.data?.message ||
        error?.response?.data ||
        error?.message ||
        "OTP verification failed";

    //   toast.error(
    //     typeof message === "string" ? message : "OTP verification failed",
    //   );

      throw error;
    }
  };

  // Logout
  const logout = async (redirectTo = "/login") => {
    try {
      await api.post("/api/auth/logout");
    } catch (error) {
      console.error("Logout request failed:", error);
    } finally {
      setUser(null);
      toast.success("Logged out successfully");
      navigate(redirectTo);
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        loading,
        login,
        deliveryLogin,
        register,
        verifyRegistration,
        logout,
        refreshUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }

  return context;
}
