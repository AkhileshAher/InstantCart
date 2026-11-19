
export type UserRole = "CUSTOMER" | "ADMIN" | "DELIVERY_PARTNER";

export interface User {
  id: number;
  username: string;
  email: string;
  phone?: string;
  role: UserRole;
}

export interface Address {
  id: string;
  label?: string;
  addressLine: string;
  city: string;
  state?: string;
  zip: string;
  lat?: number;
  lng?: number;
  isDefault: boolean;
}

export interface AddressRequest {
  label?: string;
  addressLine: string;
  city: string;
  state?: string;
  zip: string;
  lat?: number;
  lng?: number;
  isDefault?: boolean;
}

export interface Category {
  id: number;
  name: string;
  image?: string;
}

export interface Product {
  id: number;
  categoryId: number;
  categoryName: string;
  name: string;
  description?: string;
  price: number;
  originalPrice?: number;
  discountPercent?: number;
  unit?: string;
  stock: number;
  image?: string;
  isOrganic: boolean;
  isActive: boolean;
  rating?: number;
  reviewCount?: number;
}

// Product information returned by the cart API
export interface CartProduct {
  id: number;
  name: string;
  image?: string;
  price: number;
  discountPercent?: number;
}

// One item inside the frontend cart
export interface CartItem {
  product: CartProduct;
  quantity: number;
}

// Exact backend CartItemResponse
export interface CartItemResponse {
  productId: number;
  name: string;
  image?: string;
  price: number;
  discountPercent?: number;
  quantity: number;
}

export interface DeliveryPartner {
  id: number;
  name: string;
  email: string;
  phone?: string;
  avatar?: string;
  isActive: boolean;
}

export interface OrderItem {
  productId: number;
  name: string;
  unitPrice: number;
  discountPercent?: number;
  quantity: number;
  lineTotal: number;
}

export interface ShippingAddress {
  name?: string;
  phone?: string;
  addressLine?: string;
  city?: string;
  state?: string;
  zip?: string;
}

export interface StatusHistory {
  status: string;
  note?: string;
  changedAt: string;
}

export interface LiveLocation {
  lat?: number;
  lng?: number;
}

export interface Order {
  id: number;
  orderNumber: string;
  status: string;
  paymentMethod: string;
  paymentStatus: string;
  subTotal: number;
  discountAmount: number;
  deliveryFee: number;
  tax: number;
  total: number;
  deliveryOtp?: string;
  shippingAddress: ShippingAddress;
  liveLocation?: LiveLocation;
  deliveryPartner: DeliveryPartner | null;
  items: OrderItem[];
  statusHistory: StatusHistory[];
  createdAt: string;
}

export interface ApiResponse<T> {
  success: boolean;
  timestamp: string;
  status: number;
  message: string;
  data: T;
}
