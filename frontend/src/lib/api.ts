// API 기본 설정
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

// API 응답 타입 정의
export interface ApiResponse<T = any> {
  success: boolean;
  data?: T;
  message?: string;
  error?: string;
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

// 회원가입 완료 요청 타입
export type CompleteRegistrationRequest = {
  email: string;
  password: string;
  name: string;
};

// 이메일 인증 상태 응답 타입
export interface EmailVerificationStatus {
  email: string;
  verified: boolean;
  verifiedAt?: string;
}
// API 요청 함수
class ApiClient {
  private baseURL: string;

  constructor(baseURL: string) {
    this.baseURL = baseURL;
  }

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
  // 이메일 인증 요청 (회원가입 전)
  async requestEmailVerification(data: EmailVerificationRequest): Promise<ApiResponse<any>> {
    return this.request<any>('/auth/email-verification', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  // 회원가입 완료 (이메일 인증 후)
  async completeRegistration(data: CompleteRegistrationRequest): Promise<ApiResponse<any>> {
    return this.request<any>('/auth/complete-registration', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  // 회원가입 (기존 방식)
  async signUp(userData: SignUpRequest): Promise<ApiResponse<SignUpResponse>> {
    return this.request<SignUpResponse>('/auth/register', {
      method: 'POST',
      body: JSON.stringify({
        ...userData,
        username: userData.email, // 이메일을 username으로도 사용
      }),
    });
  }

  // 이메일 인증 상태 확인
  async checkEmailVerification(email: string): Promise<ApiResponse<EmailVerificationStatus>> {
    return this.request<EmailVerificationStatus>(`/auth/verification-status?email=${encodeURIComponent(email)}`);
  }

  // 이메일 인증 재발송
  async resendEmailVerification(email: string): Promise<ApiResponse<string>> {
    return this.request<string>('/auth/resend-verification', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: `email=${encodeURIComponent(email)}`,
    });
  }

  // 로그인
  async signIn(emailOrUsername: string, password: string): Promise<ApiResponse<any>> {
    return this.request<any>('/auth/login', {
      method: 'POST',
      body: JSON.stringify({
        emailOrUsername,
        password,
      }),
    });
  }

  // 사용자 정보 조회
  async getUserInfo(email: string): Promise<ApiResponse<{email: string, name: string, emailVerified: boolean}>> {
    return this.request<{email: string, name: string, emailVerified: boolean}>(`/auth/user-info?email=${encodeURIComponent(email)}`, {
      method: 'GET',
    });
  }
}
// API 클라이언트 인스턴스 생성
export const apiClient = new ApiClient(API_BASE_URL);

// 편의 함수들
export const authApi = {
  requestEmailVerification: (data: EmailVerificationRequest) => apiClient.requestEmailVerification(data),
  completeRegistration: (data: CompleteRegistrationRequest) => apiClient.completeRegistration(data),
  signUp: (userData: SignUpRequest) => apiClient.signUp(userData),
  checkEmailVerification: (email: string) => apiClient.checkEmailVerification(email),
  resendEmailVerification: (email: string) => apiClient.resendEmailVerification(email),
  signIn: (emailOrUsername: string, password: string) => apiClient.signIn(emailOrUsername, password),
  getUserInfo: (email: string) => apiClient.getUserInfo(email),
};