
import axios, {
  type InternalAxiosRequestConfig,
} from "axios";

const BASE_URL = import.meta.env.VITE_BASE_URL;

declare module "axios" {
  interface InternalAxiosRequestConfig {
    _retry?: boolean;
  }
}

const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

// Separate instance prevents refresh interceptor recursion.
const refreshApi = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

let refreshPromise: Promise<boolean> | null = null;

const getRequestPath = (
  config: InternalAxiosRequestConfig
): string => {
  return (config.url ?? "").split("?")[0].replace(/\/+$/, "");
};

// Public endpoints that do not require authentication.
const isPublicRequest = (
  config: InternalAxiosRequestConfig
): boolean => {
  const method = (config.method ?? "get").toLowerCase();
  const path = getRequestPath(config);

  // Authentication endpoints
  const authEndpoints = [
    "/api/auth/login",
    "/api/auth/register",
    "/api/auth/verify-registration",
    "/api/auth/refresh",
    "/api/auth/logout",
  ];

  if (authEndpoints.includes(path)) {
    return true;
  }

  // Public product and category GET requests
  if (
    method === "get" &&
    (
      path === "/api/products" ||
      path.startsWith("/api/products/") ||
      path === "/api/categories"
    )
  ) {
    return true;
  }

  return false;
};

// Do not refresh the token for these requests.
const shouldSkipRefresh = (
  config: InternalAxiosRequestConfig
): boolean => {
  return isPublicRequest(config);
};

api.interceptors.response.use(
  (response) => response,

  async (error) => {
    const originalRequest = error.config as
      | InternalAxiosRequestConfig
      | undefined;

    if (!originalRequest) {
      return Promise.reject(error);
    }

    // Only handle unauthorized errors.
    if (error.response?.status !== 401) {
      return Promise.reject(error);
    }

    // Public APIs and auth endpoints should not trigger refresh.
    if (shouldSkipRefresh(originalRequest)) {
      return Promise.reject(error);
    }

    // Prevent infinite retry loops.
    if (originalRequest._retry) {
      return Promise.reject(error);
    }

    originalRequest._retry = true;

    try {
      if (!refreshPromise) {
        refreshPromise = refreshApi
          .post("/api/auth/refresh")
          .then(() => true)
          .catch(() => false)
          .finally(() => {
            refreshPromise = null;
          });
      }

      const refreshed = await refreshPromise;

      if (!refreshed) {
        window.dispatchEvent(
          new Event("auth:session-expired")
        );

        return Promise.reject(error);
      }

      // Retry original request with the refreshed cookie.
      return api(originalRequest);

    } catch {
      window.dispatchEvent(
        new Event("auth:session-expired")
      );

      return Promise.reject(error);
    }
  }
);

export default api;
