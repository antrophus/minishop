/**
 * 기타 API 함수들
 * 상품, 인증 외의 추가적인 API 기능들을 여기에 구현
 */

import { apiClient } from './client';
import type { ApiResponse } from './types';

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
 * 향후 추가될 API들을 위한 placeholder
 */

// 장바구니 API (향후 구현)
export const cartApi = {
  // 장바구니 조회
  getCart: async () => {
    // TODO: 구현 예정
    console.log('Cart API - 구현 예정');
  },

  // 상품 추가
  addItem: async (productId: number, quantity: number) => {
    // TODO: 구현 예정
    console.log('Cart API - addItem 구현 예정', { productId, quantity });
  },
};

// 위시리스트 API (향후 구현)
export const wishlistApi = {
  // 위시리스트 조회
  getWishlist: async () => {
    // TODO: 구현 예정
    console.log('Wishlist API - 구현 예정');
  },

  // 위시리스트에 추가
  addToWishlist: async (productId: number) => {
    // TODO: 구현 예정
    console.log('Wishlist API - addToWishlist 구현 예정', { productId });
  },
};

// 주문 API (향후 구현)
export const ordersApi = {
  // 주문 목록 조회
  getOrders: async () => {
    // TODO: 구현 예정
    console.log('Orders API - 구현 예정');
  },

  // 주문 생성
  createOrder: async (orderData: any) => {
    // TODO: 구현 예정
    console.log('Orders API - createOrder 구현 예정', { orderData });
  },
};
