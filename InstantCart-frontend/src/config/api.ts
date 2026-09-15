import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    withCredentials: true
})

api.interceptors.request.use((config)  =>{
    return config;
})

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if(error.response?.status === 401) {
            if(!window.location.pathname.includes("/login") && !window.location.pathname.includes("/register")) {
                    window.location.href = "/login"
                }
        } else {
            return Promise.reject(error);
        }
    }
);

export default api;