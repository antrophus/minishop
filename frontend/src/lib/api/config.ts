/**
 * API 설정 통합 모듈
 * 모든 API 엔드포인트와 URL을 중앙에서 관리합니다.
 */

// 환경 변수에서 API 설정 로드
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';
const API_CONTEXT_PATH = process.env.NEXT_PUBLIC_API_CONTEXT_PATH || '/api';
const AUTH_CONTEXT_PATH = process.env.NEXT_PUBLIC_AUTH_CONTEXT_PATH || '/auth';

/**
 * API 설정 상수
 */
export const API_CONFIG = {
  // 기본 URL
  BASE_URL: API_BASE_URL,
  
  // Context Path
  API_CONTEXT_PATH,
  AUTH_CONTEXT_PATH,
  
  // 전체 URL 조합
  API_URL: `${API_BASE_URL}${API_CONTEXT_PATH}`,
  AUTH_URL: `${API_BASE_URL}${AUTH_CONTEXT_PATH}`,
} as const;

/**
 * API 엔드포인트 정의
 */
export const API_ENDPOINTS = {
  // 상품 관련 API
  PRODUCTS: {
    LIST: '/products',
    DETAIL: (id: number) => `/products/${id}`,
    SEARCH: '/products/search',
    BY_CATEGORY: (categoryId: number) => `/products/category/${categoryId}`,
    RELATED: (id: number) => `/products/${id}/related`,
    IMAGES: (id: number) => `/products/${id}/images`,
  },
  
  // 카테고리 관련 API
  CATEGORIES: {
    LIST: '/categories',
    DETAIL: (id: number) => `/categories/${id}`,
    ROOT: '/categories/root',
  },
  
  // 장바구니 관련 API (향후 구현)
  CART: {
    LIST: '/cart',
    ADD: '/cart/items',
    UPDATE: (id: number) => `/cart/items/${id}`,
    REMOVE: (id: number) => `/cart/items/${id}`,
  },
  
  // 위시리스트 관련 API (향후 구현)
  WISHLIST: {
    LIST: '/wishlist',
    ADD: '/wishlist',
    REMOVE: (productId: number) => `/wishlist/${productId}`,
  },
  
  // 주문 관련 API (향후 구현)
  ORDERS: {
    LIST: '/orders',
    DETAIL: (id: number) => `/orders/${id}`,
    CREATE: '/orders',
  },
} as const;

/**
 * 인증 관련 엔드포인트
 */
export const AUTH_ENDPOINTS = {
  // 회원가입 및 인증
  REGISTER: '/register',
  EMAIL_VERIFICATION: '/email-verification',
  COMPLETE_REGISTRATION: '/complete-registration',
  VERIFY_EMAIL: '/verify-email',
  RESEND_VERIFICATION: '/resend-verification',
  VERIFICATION_STATUS: '/verification-status',
  USER_INFO: '/user-info',
  
  // 로그인 및 프로필
  LOGIN: '/login',
  PROFILE: '/profile',
  
  // 비밀번호 관리
  PASSWORD: {
    CHANGE: '/password/change',
    RESET_REQUEST: '/password/reset-request',
    RESET_CONFIRM: '/password/reset-confirm',
  },
  
  // 개발용
  CREATE_TEST_USER: '/create-test-user',
} as const;

/**
 * 완전한 URL 생성 헬퍼 함수
 */
export const createApiUrl = (endpoint: string): string => {
  return `${API_CONFIG.API_URL}${endpoint}`;
};

export const createAuthUrl = (endpoint: string): string => {
  return `${API_CONFIG.AUTH_URL}${endpoint}`;
};

/**
 * 환경별 설정 확인 함수
 */
export const getEnvironmentInfo = () => {
  return {
    isDevelopment: process.env.NODE_ENV === 'development',
    isProduction: process.env.NODE_ENV === 'production',
    apiBaseUrl: API_CONFIG.BASE_URL,
    apiUrl: API_CONFIG.API_URL,
    authUrl: API_CONFIG.AUTH_URL,
  };
};

/**
 * API 상태 확인용 엔드포인트 (향후 구현 시 사용)
 */
export const HEALTH_CHECK_ENDPOINTS = {
  API_HEALTH: '/health',
  AUTH_HEALTH: '/auth/health',
} as const;