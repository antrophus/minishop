// API 기본 설정 및 공통 유틸리티
import { API_CONFIG, AUTH_ENDPOINTS } from './config';
import type { ApiResponse } from './types';

/**
 * API 클라이언트 클래스
 * JWT 토큰 자동 처리, 에러 핸들링, 다양한 응답 형식 지원
 */
class ApiClient {
  private baseURL: string;

  constructor(baseURL: string) {
    this.baseURL = baseURL;
  }

  /**
   * 공통 API 요청 메서드
   */
  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<ApiResponse<T>> {
    try {
      const url = `${this.baseURL}${endpoint}`;
      const config: RequestInit = {
        headers: {
          'Content-Type': 'application/json',
          ...options.headers,
        },
        ...options,
      };

      // JWT 토큰이 필요한 API에 자동으로 토큰 추가
      const token = this.getAuthToken();
      const isAuthRequired = this.isAuthRequiredEndpoint(endpoint);
      
      console.log('API 요청 디버깅:', {
        url,
        endpoint,
        hasToken: !!token,
        isAuthRequired,
        tokenPrefix: token ? token.substring(0, 20) + '...' : null
      });
      
      if (token && isAuthRequired) {
        config.headers = {
          ...config.headers,
          'Authorization': `Bearer ${token}`,
        };
        console.log('JWT 토큰 추가됨');
      }

      const response = await fetch(url, config);
      
      // JSON 응답인지 확인
      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        const data = await response.json();
        
        if (!response.ok) {
          return {
            success: false,
            error: typeof data === 'string' ? data : data.message || `HTTP ${response.status}`,
          };
        }
        
        return {
          success: true,
          data,
        };
      } else {
        // 텍스트 응답 처리
        const text = await response.text();
        
        if (!response.ok) {
          return {
            success: false,
            error: text || `HTTP ${response.status}`,
          };
        }
        
        return {
          success: true,
          data: text as any,
          message: text,
        };
      }
    } catch (error) {
      console.error('API 요청 실패:', error);
      return {
        success: false,
        error: error instanceof Error ? error.message : '네트워크 오류가 발생했습니다.',
      };
    }
  }

  /**
   * JWT 토큰 가져오기 (안전한 localStorage 접근)
   */
  private getAuthToken(): string | null {
    try {
      if (typeof window === 'undefined' || !window.localStorage) return null;
      return localStorage.getItem('authToken');
    } catch (error) {
      console.warn('localStorage 접근 실패:', error);
      return null;
    }
  }

  /**
   * 인증이 필요한 엔드포인트인지 확인
   */
  private isAuthRequiredEndpoint(endpoint: string): boolean {
    const authRequiredEndpoints = [
      '/profile', 
      '/password',
      '/cart',
      '/wishlist'
    ];
    return authRequiredEndpoints.some(path => endpoint.includes(path));
  }

  /**
   * HTTP 메서드별 편의 함수들
   */
  async get<T>(endpoint: string, params?: Record<string, any>): Promise<ApiResponse<T>> {
    const searchParams = params ? new URLSearchParams(params).toString() : '';
    const url = searchParams ? `${endpoint}?${searchParams}` : endpoint;
    return this.request<T>(url);
  }

  async post<T>(endpoint: string, data?: any): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, {
      method: 'POST',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  async put<T>(endpoint: string, data?: any): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, {
      method: 'PUT',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  async delete<T>(endpoint: string): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, {
      method: 'DELETE',
    });
  }

  /**
   * form-data 전송을 위한 특별 메서드
   */
  async postForm<T>(endpoint: string, formData: FormData): Promise<ApiResponse<T>> {
    return this.request<T>(endpoint, {
      method: 'POST',
      headers: {}, // Content-Type을 자동으로 설정하도록 비워둠
      body: formData,
    });
  }

  /**
   * application/x-www-form-urlencoded 전송을 위한 메서드
   */
  async postUrlEncoded<T>(endpoint: string, data: Record<string, string>): Promise<ApiResponse<T>> {
    const body = new URLSearchParams(data).toString();
    return this.request<T>(endpoint, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body,
    });
  }
}

// API 클라이언트 인스턴스 생성 (상품, 주문 등 일반 API용)
export const apiClient = new ApiClient(API_CONFIG.API_URL);

// 인증 API 클라이언트 인스턴스 생성 (인증 전용)
export const authApiClient = new ApiClient(API_CONFIG.AUTH_URL);

export { ApiClient };
export type { ApiResponse };