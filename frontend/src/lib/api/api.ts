/**
 * 기타 API 함수들
 * 상품, 인증 외의 추가적인 API 기능들을 여기에 구현
 */

import { apiClient } from './client';
import type { 
  ApiResponse,
  CartItem,
  AddCartItemRequest,
  UpdateQuantityRequest,
  CartItemResponse,
  WishlistItem,
  WishlistResponse
} from './types';

/**
 * 시스템 관련 API
 */
export const systemApi = {
  /**
   * API 헬스 체크
   */
  healthCheck: async (): Promise<ApiResponse<{ status: string; timestamp: string }>> => {
    return apiClient.get('/health');
  },

  /**
   * 서버 시간 조회
   */
  getServerTime: async (): Promise<ApiResponse<{ timestamp: string }>> => {
    return apiClient.get('/time');
  },
};

/**
 * 장바구니 API
 */
export const cartApi = {
  /**
   * 사용자 장바구니 조회
   */
  getCart: async (userId: number): Promise<ApiResponse<CartItem[]>> => {
    return apiClient.get(`/cart/${userId}`);
  },

  /**
   * 장바구니에 상품 추가
   */
  addItem: async (userId: number, productId: number, quantity: number): Promise<ApiResponse<CartItemResponse>> => {
    const request: AddCartItemRequest = { productId, quantity };
    return apiClient.post(`/cart/${userId}/items`, request);
  },

  /**
   * 장바구니 아이템 수량 수정
   */
  updateQuantity: async (cartItemId: number, quantity: number): Promise<ApiResponse<CartItemResponse>> => {
    const request: UpdateQuantityRequest = { quantity };
    return apiClient.put(`/cart/items/${cartItemId}`, request);
  },

  /**
   * 장바구니 아이템 삭제
   */
  removeItem: async (cartItemId: number): Promise<ApiResponse<void>> => {
    return apiClient.delete(`/cart/items/${cartItemId}`);
  },

  /**
   * 장바구니 비우기
   */
  clearCart: async (userId: number): Promise<ApiResponse<void>> => {
    return apiClient.delete(`/cart/${userId}/clear`);
  },

  /**
   * 장바구니 아이템 수 조회
   */
  getItemCount: async (userId: number): Promise<ApiResponse<number>> => {
    return apiClient.get(`/cart/${userId}/count`);
  },

  /**
   * 장바구니 총액 조회
   */
  getTotal: async (userId: number): Promise<ApiResponse<number>> => {
    return apiClient.get(`/cart/${userId}/total`);
  },
};

/**
 * 위시리스트 API
 */
export const wishlistApi = {
  /**
   * 사용자 위시리스트 조회
   */
  getWishlist: async (userId: number): Promise<ApiResponse<WishlistItem[]>> => {
    return apiClient.get(`/wishlist/${userId}`);
  },

  /**
   * 위시리스트에 상품 추가
   */
  addToWishlist: async (userId: number, productId: number): Promise<ApiResponse<WishlistResponse>> => {
    return apiClient.post(`/wishlist/${userId}/items/${productId}`);
  },

  /**
   * 위시리스트에서 상품 제거
   */
  removeFromWishlist: async (userId: number, productId: number): Promise<ApiResponse<void>> => {
    return apiClient.delete(`/wishlist/${userId}/items/${productId}`);
  },

  /**
   * 상품이 위시리스트에 있는지 확인
   */
  isInWishlist: async (userId: number, productId: number): Promise<ApiResponse<boolean>> => {
    return apiClient.get(`/wishlist/${userId}/check/${productId}`);
  },

  /**
   * 위시리스트 아이템 수 조회
   */
  getCount: async (userId: number): Promise<ApiResponse<number>> => {
    return apiClient.get(`/wishlist/${userId}/count`);
  },

  /**
   * 위시리스트 비우기
   */
  clearWishlist: async (userId: number): Promise<ApiResponse<void>> => {
    return apiClient.delete(`/wishlist/${userId}/clear`);
  },
};
