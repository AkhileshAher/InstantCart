
import {
  createContext,
  useContext,
  useEffect,
  useState,
  type ReactNode,
} from "react";

import type {
  CartItem,
  CartItemResponse,
  Product,
  ApiResponse,
} from "../types";

import api from "../config/api";
import { useAuth } from "./AuthContext";

interface CartContextType {
  items: CartItem[];

  addToCart: (
    product: Product,
    quantity?: number
  ) => Promise<void>;

  removeFromCart: (productId: number) => Promise<void>;

  updateQuantity: (
    productId: number,
    quantity: number
  ) => Promise<void>;

  clearCart: () => Promise<void>;

  cartCount: number;
  cartTotal: number;

  isCartOpen: boolean;
  setIsCartOpen: (open: boolean) => void;

  loadCart: () => Promise<void>;
}

const CartContext = createContext<CartContextType | undefined>(
  undefined
);

export function CartProvider({
  children,
}: {
  children: ReactNode;
}) {
  const { user, loading: authLoading } = useAuth();

  const [items, setItems] = useState<CartItem[]>([]);
  const [isCartOpen, setIsCartOpen] = useState(false);

  const loadCart = async (): Promise<void> => {
    if (!user) {
      setItems([]);
      return;
    }

    const response = await api.get<ApiResponse<CartItemResponse[]>>(
      "/api/cart"
    );

    const cartData = response.data.data ?? [];

    const mappedItems: CartItem[] = cartData.map((item) => ({
      product: {
        id: item.productId,
        name: item.name,
        image: item.image,
        price: item.price,
        discountPercent: item.discountPercent,
      },
      quantity: item.quantity,
    }));

    setItems(mappedItems);
  };

  useEffect(() => {
    if (authLoading) return;

    if (!user) {
      setItems([]);
      return;
    }

    void loadCart().catch((error: unknown) => {
      console.error("Failed to load cart:", error);
    });
  }, [user, authLoading]);

  const addToCart = async (
    product: Product,
    quantity = 1
  ): Promise<void> => {
    if (!user) {
      throw new Error("Please login to add products to cart");
    }

    await api.post("/api/cart", {
      productId: product.id,
      quantity,
    });

    await loadCart();
    setIsCartOpen(true);
  };

  const removeFromCart = async (
    productId: number
  ): Promise<void> => {
    await api.delete(`/api/cart/${productId}`);
    await loadCart();
  };

  const updateQuantity = async (
    productId: number,
    quantity: number
  ): Promise<void> => {
    if (quantity <= 0) {
      await removeFromCart(productId);
      return;
    }

    await api.put(`/api/cart/${productId}`, {
      quantity,
    });

    await loadCart();
  };

  const clearCart = async (): Promise<void> => {
    await api.delete("/api/cart");

    setItems([]);
    setIsCartOpen(false);
  };

  const cartCount = items.reduce(
    (total, item) => total + item.quantity,
    0
  );

  const cartTotal = items.reduce(
    (total, item) => total + item.product.price * item.quantity,
    0
  );

  return (
    <CartContext.Provider
      value={{
        items,
        addToCart,
        removeFromCart,
        updateQuantity,
        clearCart,
        cartCount,
        cartTotal,
        isCartOpen,
        setIsCartOpen,
        loadCart,
      }}
    >
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const context = useContext(CartContext);

  if (!context) {
    throw new Error(
      "useCart must be used within CartProvider"
    );
  }

  return context;
}
