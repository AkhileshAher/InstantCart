import {createContext,useContext,useEffect,useState,type ReactNode} from "react";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import type { User } from "../types";
import api from "../config/api";

type UserRole = "CUSTOMER" | "VENDOR" | "DELIVERY";

interface AuthContextType {
    user: User | null;

    loading: boolean;

    login: (email: string,password: string) => Promise<void>;

    deliveryLogin : (email: string, password: string) => Promise<void>;

    register: (name: string,email: string,password: string,phone: string,role: UserRole) => Promise<void>;

    logout: (redirectTo?: string) => Promise<void>;

    refreshUser: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({children}: {children: ReactNode}) {

    const navigate = useNavigate();
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const checkAuth = async () => {
            try {
                const response = await api.get("/auth/me");
                console.log("Authenticated user:", response);
                setUser(response.data);
            } catch (error) {
                setUser(null);
            } finally {
                setLoading(false);
            }
        };

        checkAuth();

    }, []);

    const login = async (email: string,password: string) => {
        try {
            await api.post("/auth/login", {email,password});
            const response = await api.get("/auth/me");
            console.log(response);
            setUser(response.data);
            toast.success("Login successful");
            navigate("/");
        } catch (error: any) {
            const message =error?.response?.data?.message || error?.message || "Login failed";
            toast.error(message);
            throw error;
        }
    };

    const deliveryLogin = async (email: string, password: string) => {
         try {
            await api.post("/auth/delivery-login", {email,password});
            const response = await api.get("/auth/me");
            console.log("Delivery User",response);
            setUser(response.data);
            toast.success("Login successful");
            navigate("/delivery");
        } catch (error: any) {
            const message =error?.response?.data?.message || error?.message || "Login failed";
            toast.error(message);
            throw error;
        }
    };

    const register = async (name: string,email: string,password: string,phone: string,role: UserRole) => {
        try {
            await api.post("/auth/register", {name,email,password,phone,role});
            const response = await api.get("/auth/me");
            console.log(response);
            setUser(response.data);
            toast.success("Registration successful");
            navigate("/");
        } catch (error: any) {
            const message = error?.response?.data?.message || error?.message || "Registration failed";
            toast.error(message);
            throw error;
        }
    };


    const logout = async (redirectTo = "/login") => {
        try {
            await api.post("/auth/logout");
        } catch (error) {
            console.error("Logout request failed", error);
        } finally {
            setUser(null);
            toast.success("Logged out successfully");
            navigate(redirectTo);
        }
    };

    const refreshUser = async () => {
        try {
            const response = await api.get("/auth/me");
            setUser(response.data);
        } catch (error) {
            setUser(null);
        }
    };

    return (
        <AuthContext.Provider 
            value={{
                user,
                loading,
                login,
                register,
                logout,
                refreshUser,
                deliveryLogin
            }}
        >
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) throw new Error("useAuth must be used within AuthProvider");
    return context;
}
