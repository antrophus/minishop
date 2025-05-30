// 상품 관련 타입 정의
export interface Product {
  id: number;
  name: string;
  description: string;
  price: number | null;
  memberPrice: number | null;
  originalPrice?: number;
  stockQuantity: number | null;
  status: string;
  isActive: boolean;
  featured: boolean;
  bestseller: boolean;
  newArrival: boolean;
  brand: string;
  sku: string;
  createdAt: string;
  updatedAt?: string;
  manufacturer?: string;
  barcode?: string;
  minimumOrderQuantity: number | null;
  maximumOrderQuantity: number | null;
  subscriptionAvailable: boolean;
  shippingFee: number | null;
  countryOfOrigin: string;
  weight: number;
  category: Category | null;
  mainImage?: ProductImage;
  images?: ProductImage[];
  discount?: {
    amount: number;
    rate: number;
  };
  attributes?: ProductAttribute[];
}

export interface Category {
  id: number;
  name: string;
  parentId?: number;
  parentName?: string;
  parent?: Category;
  children?: Category[];
  productCount?: number;
}

export interface ProductImage {
  id: number;
  url: string;
  alt: string;
  isMain: boolean;
  sortOrder: number;
}

export interface ProductAttribute {
  attributeCode: string;
  attributeName: string;
  value: string;
}

export interface ProductsResponse {
  products: Product[];
  currentPage: number;
  totalPages: number;
  totalElements: number;
  size: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

export interface SearchResponse {
  keyword: string;
  products: Product[];
  totalElements: number;
}

export interface CategoryProductsResponse {
  category: Category;
  products: Product[];
  currentPage: number;
  totalPages: number;
  totalElements: number;
}

export interface RelatedProductsResponse {
  productId: number;
  relatedProducts: Product[];
}

export interface CategoriesResponse {
  categories: Category[];
}

// =============================================================================
// 공통 API 응답 타입
// =============================================================================
export interface ApiResponse<T = any> {
  success: boolean;
  data?: T;
  message?: string;
  error?: string;
}

// =============================================================================
// 인증 관련 타입 정의
// =============================================================================

// 백엔드 프로필 API 응답 타입 (중첩 구조)
export interface ProfileApiResponse {
  success: boolean;
  data: UserProfile;
}

// 회원가입 요청 타입
export interface SignUpRequest {
  name: string;
  email: string;
  username: string;
  password: string;
  phone?: string;
  address?: string;
  gender?: string;
}

// 회원가입 응답 타입
export interface SignUpResponse {
  id: number;
  email: string;
  username: string;
  name: string;
  createdAt: string;
  success: boolean;
  message: string;
}

// 이메일 인증 요청 타입
export interface EmailVerificationRequest {
  email: string;
  name: string;
}

// 이메일 인증 상태 타입
export interface EmailVerificationStatus {
  email: string;
  name: string;
  emailVerified: boolean;
}

// 회원가입 완료 요청 타입
export interface CompleteRegistrationRequest {
  email: string;
  password: string;
  name: string;
}

// 사용자 프로필 타입
export interface UserProfile {
  id: number;
  email: string;
  username: string;
  name: string;
  phone?: string;
  address?: string;
  gender?: string;
  emailVerified: boolean;
  active: boolean;
  createdAt: string;
  lastLoginAt?: string;
}

// 로그인 응답 타입 (백엔드 UserLoginResponse와 일치)
export interface SignInResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
  userId: number;
  email: string;
  username: string;
  name: string;
}