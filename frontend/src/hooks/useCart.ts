import { useState, useEffect, useCallback } from 'react';
import { cartApi } from '@/lib/api';
import { authUtils } from '@/lib/api/auth';
import type { CartItem as ApiCartItem, Product } from '@/lib/api/types';

export interface CartItem {
  id: number;
  productId: number;
  productName: string;
  price: number;
  quantity: number;
  imageUrl?: string;
  addedAt: string;
}

export interface UseCartReturn {
  cartItems: CartItem[];
  isLoading: boolean;
  error: string | null;
  addToCart: (product: Product, quantity?: number) => Promise<void>;
  removeFromCart: (cartItemId: number) => Promise<void>;
  updateQuantity: (cartItemId: number, quantity: number) => Promise<void>;
  clearCart: () => Promise<void>;
  getTotalItems: () => number;
  getTotalPrice: () => number;
  refreshCart: () => Promise<void>;
}

// API CartItem을 local CartItem으로 변환
const transformApiCartItem = (apiItem: ApiCartItem): CartItem => ({
  id: apiItem.id,
  productId: apiItem.product.id,
  productName: apiItem.product.name,
  price: apiItem.product.price ?? 0,
  quantity: apiItem.quantity,
  imageUrl: apiItem.product.mainImage?.url,
  addedAt: apiItem.createdAt,
});

export const useCart = (): UseCartReturn => {
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // 현재 사용자 정보 가져오기
  const getCurrentUser = useCallback(() => {
    return authUtils.getUserFromToken();
  }, []);

  // 에러 처리 헬퍼
  const handleError = useCallback((error: any, defaultMessage: string) => {
    console.error('장바구니 오류:', error);
    const errorMessage = error?.response?.data?.message || error?.message || defaultMessage;
    setError(errorMessage);
  }, []);

  // 장바구니 데이터 로드
  const loadCart = useCallback(async () => {
    const user = getCurrentUser();
    if (!user?.userId) {
      setCartItems([]);
      return;
    }

    // userId가 유효한 숫자인지 확인
    const userId = parseInt(user.userId);
    if (isNaN(userId)) {
      console.error('유효하지 않은 사용자 ID:', user.userId);
      setCartItems([]);
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      const response = await cartApi.getCart(userId);
      
      // 응답 데이터 안전하게 처리
      if (response.success && response.data && Array.isArray(response.data)) {
        const transformedItems = response.data.map(transformApiCartItem);
        setCartItems(transformedItems);
      } else {
        console.warn('유효하지 않은 장바구니 응답:', response);
        setCartItems([]);
      }
    } catch (error) {
      handleError(error, '장바구니를 불러오는데 실패했습니다.');
      setCartItems([]);
    } finally {
      setIsLoading(false);
    }
  }, [getCurrentUser, handleError]);

  // 컴포넌트 마운트 시 장바구니 로드
  useEffect(() => {
    loadCart();
  }, [loadCart]);

  // 장바구니 새로고침
  const refreshCart = useCallback(async () => {
    await loadCart();
  }, [loadCart]);

  // 장바구니에 상품 추가
  const addToCart = useCallback(async (product: Product, quantity: number = 1) => {
    const user = getCurrentUser();
    if (!user?.userId) {
      setError('로그인이 필요합니다.');
      return;
    }

    const userId = parseInt(user.userId);
    if (isNaN(userId)) {
      setError('유효하지 않은 사용자 정보입니다.');
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      await cartApi.addItem(userId, product.id, quantity);
      await loadCart();
    } catch (error) {
      handleError(error, '상품을 장바구니에 추가하는데 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  }, [getCurrentUser, loadCart, handleError]);

  // 장바구니에서 상품 제거
  const removeFromCart = useCallback(async (cartItemId: number) => {
    const user = getCurrentUser();
    if (!user?.userId) {
      setError('로그인이 필요합니다.');
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      await cartApi.removeItem(cartItemId);
      await loadCart();
    } catch (error) {
      handleError(error, '상품을 장바구니에서 제거하는데 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  }, [getCurrentUser, loadCart, handleError]);

  // 상품 수량 업데이트
  const updateQuantity = useCallback(async (cartItemId: number, quantity: number) => {
    const user = getCurrentUser();
    if (!user?.userId) {
      setError('로그인이 필요합니다.');
      return;
    }

    if (quantity <= 0) {
      await removeFromCart(cartItemId);
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      await cartApi.updateQuantity(cartItemId, quantity);
      await loadCart();
    } catch (error) {
      handleError(error, '수량을 업데이트하는데 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  }, [getCurrentUser, removeFromCart, loadCart, handleError]);

  // 장바구니 비우기
  const clearCart = useCallback(async () => {
    const user = getCurrentUser();
    if (!user?.userId) {
      setError('로그인이 필요합니다.');
      return;
    }

    const userId = parseInt(user.userId);
    if (isNaN(userId)) {
      setError('유효하지 않은 사용자 정보입니다.');
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      await cartApi.clearCart(userId);
      setCartItems([]);
    } catch (error) {
      handleError(error, '장바구니를 비우는데 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  }, [getCurrentUser, handleError]);

  // 총 상품 개수
  const getTotalItems = useCallback(() => {
    return cartItems.reduce((total, item) => total + item.quantity, 0);
  }, [cartItems]);

  // 총 가격
  const getTotalPrice = useCallback(() => {
    return cartItems.reduce((total, item) => total + (item.price * item.quantity), 0);
  }, [cartItems]);

  return {
    cartItems,
    isLoading,
    error,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
    getTotalItems,
    getTotalPrice,
    refreshCart,
  };
};
