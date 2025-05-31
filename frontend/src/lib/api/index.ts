/**
 * API 모듈 통합 export
 * 모든 API 관련 기능을 한 곳에서 import할 수 있도록 합니다.
 */

// 설정
export { API_CONFIG, API_ENDPOINTS, AUTH_ENDPOINTS, createApiUrl, createAuthUrl, getEnvironmentInfo } from './config';

// 타입들
export type {
  // 공통 타입
  ApiResponse,
  
  // 상품 관련 타입
  Product,
  Category,
  ProductImage,
  ProductAttribute,
  ProductsResponse,
  SearchResponse,
  CategoryProductsResponse,
  RelatedProductsResponse,
  CategoriesResponse,
  
  // 인증 관련 타입
  ProfileApiResponse,
  SignUpRequest,
  SignUpResponse,
  EmailVerificationRequest,
  EmailVerificationStatus,
  CompleteRegistrationRequest,
  UserProfile,
  SignInResponse,
  
  // 장바구니 관련 타입
  CartItem,
  Cart,
  AddCartItemRequest,
  UpdateQuantityRequest,
  CartItemResponse,
  
  // 위시리스트 관련 타입
  WishlistItem,
  WishlistResponse,
} from './types';

// 클라이언트
export { ApiClient, apiClient, authApiClient } from './client';

// 인증 API (하위 호환성을 위해 authApi로 export)
export { AuthApi, authApiMethods as authApi, authUtils } from './auth';

// 상품 API
export { productsApi, categoriesApi } from './products';

// 기타 API
export { systemApi, cartApi, wishlistApi } from './api';

// API 유틸리티
export { 
  getCurrentUserId, 
  isUserAuthenticated, 
  getErrorMessage, 
  withApiWrapper, 
  unwrapApiResponse, 
  authUtils as apiAuthUtils,
  logApiCall 
} from './utils';

// 내부 사용을 위한 import
import { authApiMethods, authUtils } from './auth';
import { productsApi, categoriesApi } from './products';
import { systemApi, cartApi, wishlistApi } from './api';
import { apiClient, authApiClient } from './client';

/**
 * 편의를 위한 통합 API 객체
 */
export const api = {
  // 인증 관련
  auth: authApiMethods,
  authUtils,
  
  // 상품 관련
  products: productsApi,
  categories: categoriesApi,
  
  // 구현된 API들
  system: systemApi,
  cart: cartApi,
  wishlist: wishlistApi,
  
  // 일반 API 클라이언트
  client: apiClient,
  authClient: authApiClient,
};

/**
 * 기본 export (하위 호환성을 위해)
 */
export default api;