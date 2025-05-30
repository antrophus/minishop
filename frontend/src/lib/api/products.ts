import { apiClient } from './client';
import { API_ENDPOINTS } from './config';
import type {
  Product,
  ProductsResponse,
  SearchResponse,
  CategoryProductsResponse,
  RelatedProductsResponse,
  CategoriesResponse,
} from './types';

// 상품 관련 API
export const productsApi = {
  // 상품 목록 조회
  getProducts: async (params?: {
    page?: number;
    size?: number;
    sortBy?: string;
    sortDir?: string;
    search?: string;
    categoryId?: number;
    brand?: string;
    activeOnly?: boolean;
  }) => {
    return apiClient.get<ProductsResponse>(API_ENDPOINTS.PRODUCTS.LIST, params);
  },

  // 상품 상세 조회
  getProduct: async (id: number) => {
    return apiClient.get<Product>(API_ENDPOINTS.PRODUCTS.DETAIL(id));
  },

  // 상품 검색
  searchProducts: async (keyword: string, page = 0, size = 20) => {
    return apiClient.get<SearchResponse>(API_ENDPOINTS.PRODUCTS.SEARCH, {
      keyword,
      page,
      size,
    });
  },

  // 카테고리별 상품 조회
  getProductsByCategory: async (
    categoryId: number,
    params?: {
      page?: number;
      size?: number;
      sortBy?: string;
      sortDir?: string;
    }
  ) => {
    return apiClient.get<CategoryProductsResponse>(
      API_ENDPOINTS.PRODUCTS.BY_CATEGORY(categoryId),
      params
    );
  },

  // 관련 상품 조회
  getRelatedProducts: async (productId: number, limit = 8) => {
    return apiClient.get<RelatedProductsResponse>(
      API_ENDPOINTS.PRODUCTS.RELATED(productId),
      { limit }
    );
  },

  // 상품 이미지 조회
  getProductImages: async (productId: number) => {
    return apiClient.get<{ productId: number; images: any[] }>(
      API_ENDPOINTS.PRODUCTS.IMAGES(productId)
    );
  },
};

// 카테고리 관련 API
export const categoriesApi = {
  // 모든 카테고리 조회
  getCategories: async () => {
    return apiClient.get<CategoriesResponse>(API_ENDPOINTS.CATEGORIES.LIST);
  },

  // 특정 카테고리 조회
  getCategory: async (id: number) => {
    return apiClient.get<{ category: import('./types').Category }>(
      API_ENDPOINTS.CATEGORIES.DETAIL(id)
    );
  },

  // 최상위 카테고리 조회 (트리 구조)
  getRootCategories: async () => {
    return apiClient.get<CategoriesResponse>(API_ENDPOINTS.CATEGORIES.ROOT);
  },
};