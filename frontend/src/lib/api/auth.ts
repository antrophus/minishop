/**
 * 인증 관련 API 함수들
 * 회원가입, 로그인, 프로필 관리, 토큰 관리 등
 */
import { authApiClient } from './client';
import { AUTH_ENDPOINTS } from './config';
import type {
  ApiResponse,
  EmailVerificationRequest,
  CompleteRegistrationRequest,
  SignUpRequest,
  SignUpResponse,
  EmailVerificationStatus,
  SignInResponse,
  UserProfile
} from './types';

/**
 * 인증 API 클래스
 */
class AuthApi {
  /**
   * 이메일 인증 요청 (회원가입 전)
   */
  async requestEmailVerification(data: EmailVerificationRequest): Promise<ApiResponse<any>> {
    return authApiClient.post(AUTH_ENDPOINTS.EMAIL_VERIFICATION, data);
  }

  /**
   * 회원가입 완료 (이메일 인증 후)
   */
  async completeRegistration(data: CompleteRegistrationRequest): Promise<ApiResponse<any>> {
    return authApiClient.post(AUTH_ENDPOINTS.COMPLETE_REGISTRATION, data);
  }

  /**
   * 회원가입 (기존 방식)
   */
  async signUp(userData: SignUpRequest): Promise<ApiResponse<SignUpResponse>> {
    return authApiClient.post<SignUpResponse>(AUTH_ENDPOINTS.REGISTER, {
      ...userData,
      username: userData.email, // 이메일을 username으로도 사용
    });
  }

  /**
   * 이메일 인증 상태 확인
   */
  async checkEmailVerification(email: string): Promise<ApiResponse<EmailVerificationStatus>> {
    return authApiClient.get<EmailVerificationStatus>(
      `${AUTH_ENDPOINTS.VERIFICATION_STATUS}?email=${encodeURIComponent(email)}`
    );
  }

  /**
   * 이메일 인증 재발송
   */
  async resendEmailVerification(email: string): Promise<ApiResponse<string>> {
    return authApiClient.postUrlEncoded<string>(
      AUTH_ENDPOINTS.RESEND_VERIFICATION,
      { email }
    );
  }

  /**
   * 로그인
   */
  async signIn(emailOrUsername: string, password: string): Promise<ApiResponse<SignInResponse>> {
    return authApiClient.post<SignInResponse>(AUTH_ENDPOINTS.LOGIN, {
      emailOrUsername,
      password,
    });
  }

  /**
   * 사용자 정보 조회
   */
  async getUserInfo(email: string): Promise<ApiResponse<{email: string, name: string, emailVerified: boolean}>> {
    return authApiClient.get<{email: string, name: string, emailVerified: boolean}>(
      `${AUTH_ENDPOINTS.USER_INFO}?email=${encodeURIComponent(email)}`
    );
  }

  /**
   * 현재 사용자 프로필 조회 (JWT 토큰 필요)
   */
  async getCurrentUserProfile(): Promise<ApiResponse<UserProfile>> {
    return authApiClient.get<UserProfile>(AUTH_ENDPOINTS.PROFILE);
  }

  /**
   * 현재 사용자 프로필 업데이트 (JWT 토큰 필요)
   */
  async updateCurrentUserProfile(
    profileData: Partial<Pick<UserProfile, 'name' | 'phone' | 'address' | 'gender'>>
  ): Promise<ApiResponse<UserProfile>> {
    return authApiClient.post<UserProfile>(AUTH_ENDPOINTS.PROFILE, profileData);
  }
}

/**
 * 인증 관련 유틸리티 함수들
 */
export const authUtils = {
  /**
   * 토큰 저장 (안전한 localStorage 접근)
   */
  setAuthToken: (token: string) => {
    try {
      if (typeof window !== 'undefined' && window.localStorage) {
        localStorage.setItem('authToken', token);
      }
    } catch (error) {
      console.warn('localStorage 접근 실패:', error);
    }
  },

  /**
   * 토큰 가져오기 (안전한 localStorage 접근)
   */
  getAuthToken: () => {
    try {
      if (typeof window !== 'undefined' && window.localStorage) {
        return localStorage.getItem('authToken');
      }
      return null;
    } catch (error) {
      console.warn('localStorage 접근 실패:', error);
      return null;
    }
  },

  /**
   * 토큰 제거 (로그아웃) (안전한 localStorage 접근)
   */
  removeAuthToken: () => {
    try {
      if (typeof window !== 'undefined' && window.localStorage) {
        localStorage.removeItem('authToken');
      }
    } catch (error) {
      console.warn('localStorage 접근 실패:', error);
    }
  },

  /**
   * 로그인 상태 확인
   */
  isLoggedIn: () => {
    return authUtils.getAuthToken() !== null;
  },

  /**
   * 토큰에서 사용자 정보 추출 (JWT 디코딩)
   */
  getUserFromToken: () => {
    const token = authUtils.getAuthToken();
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return {
        userId: payload.sub,
        email: payload.email,
        username: payload.username,
        name: payload.name,
        exp: payload.exp,
      };
    } catch (error) {
      console.error('토큰 디코딩 실패:', error);
      return null;
    }
  },

  /**
   * 토큰 만료 확인
   */
  isTokenExpired: () => {
    const user = authUtils.getUserFromToken();
    if (!user) return true;

    const now = Math.floor(Date.now() / 1000);
    return user.exp < now;
  },
};

// API 인스턴스 생성
const authApi = new AuthApi();

// 편의 함수들 export
export const authApiMethods = {
  requestEmailVerification: (data: EmailVerificationRequest) => authApi.requestEmailVerification(data),
  completeRegistration: (data: CompleteRegistrationRequest) => authApi.completeRegistration(data),
  signUp: (userData: SignUpRequest) => authApi.signUp(userData),
  checkEmailVerification: (email: string) => authApi.checkEmailVerification(email),
  resendEmailVerification: (email: string) => authApi.resendEmailVerification(email),
  signIn: (emailOrUsername: string, password: string) => authApi.signIn(emailOrUsername, password),
  getUserInfo: (email: string) => authApi.getUserInfo(email),
  getCurrentUserProfile: () => authApi.getCurrentUserProfile(),
  updateCurrentUserProfile: (profileData: Partial<Pick<UserProfile, 'name' | 'phone' | 'address' | 'gender'>>) => 
    authApi.updateCurrentUserProfile(profileData),
};

export { AuthApi };
export default authApi;