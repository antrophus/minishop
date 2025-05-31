/**
 * API 유틸리티 함수들
 * 에러 처리, 로딩 상태, 사용자 인증 등의 공통 로직
 */

import type { ApiResponse } from './types';

/**
 * 로컬 스토리지에서 사용자 ID 가져오기
 */
export const getCurrentUserId = (): number | null => {
  try {
    if (typeof window === 'undefined' || !window.localStorage) {
      return null;
    }
    
    const userDataStr = localStorage.getItem('userData');
    if (!userDataStr) {
      return null;
    }
    
    const userData = JSON.parse(userDataStr);
    return userData?.userId || userData?.id || null;
  } catch (error) {
    console.warn('사용자 데이터 파싱 실패:', error);
    return null;
  }
};

/**
 * 사용자가 로그인되어 있는지 확인
 */
export const isUserAuthenticated = (): boolean => {
  try {
    if (typeof window === 'undefined' || !window.localStorage) {
      return false;
    }
    
    const token = localStorage.getItem('authToken');
    const userId = getCurrentUserId();
    
    return !!(token && userId);
  } catch (error) {
    console.warn('인증 상태 확인 실패:', error);
    return false;
  }
};

/**
 * API 응답에서 에러 메시지 추출
 */
export const getErrorMessage = (response: ApiResponse<any>): string => {
  if (response.error) {
    return response.error;
  }
  
  if (response.message && !response.success) {
    return response.message;
  }
  
  return '알 수 없는 오류가 발생했습니다.';
};

/**
 * API 요청 래퍼 - 에러 처리와 로딩 상태 포함
 */
export const withApiWrapper = async <T>(
  apiCall: () => Promise<ApiResponse<T>>,
  options: {
    requireAuth?: boolean;
    errorMessage?: string;
    onSuccess?: (data: T) => void;
    onError?: (error: string) => void;
  } = {}
): Promise<{ data: T | null; error: string | null; loading: boolean }> => {
  const { requireAuth = false, errorMessage, onSuccess, onError } = options;
  
  // 인증 필요 시 확인
  if (requireAuth && !isUserAuthenticated()) {
    const authError = '로그인이 필요한 서비스입니다.';
    onError?.(authError);
    return { data: null, error: authError, loading: false };
  }
  
  try {
    const response = await apiCall();
    
    if (response.success && response.data) {
      onSuccess?.(response.data);
      return { data: response.data, error: null, loading: false };
    } else {
      const error = errorMessage || getErrorMessage(response);
      onError?.(error);
      return { data: null, error, loading: false };
    }
  } catch (error) {
    const errorMsg = errorMessage || '네트워크 오류가 발생했습니다.';
    onError?.(errorMsg);
    return { data: null, error: errorMsg, loading: false };
  }
};

/**
 * API 응답이 성공인지 확인하고 데이터 반환
 */
export const unwrapApiResponse = <T>(response: ApiResponse<T>): T => {
  if (!response.success || !response.data) {
    throw new Error(getErrorMessage(response));
  }
  return response.data;
};

/**
 * 사용자 인증 관련 상태 및 유틸리티
 */
export const authUtils = {
  /**
   * 현재 사용자 ID 가져오기 (필수)
   */
  requireUserId: (): number => {
    const userId = getCurrentUserId();
    if (!userId) {
      throw new Error('로그인이 필요한 서비스입니다.');
    }
    return userId;
  },
  
  /**
   * 로그아웃 처리
   */
  logout: (): void => {
    try {
      if (typeof window !== 'undefined' && window.localStorage) {
        localStorage.removeItem('authToken');
        localStorage.removeItem('userData');
      }
    } catch (error) {
      console.warn('로그아웃 처리 실패:', error);
    }
  },
  
  /**
   * 사용자 데이터 업데이트
   */
  updateUserData: (userData: any): void => {
    try {
      if (typeof window !== 'undefined' && window.localStorage) {
        localStorage.setItem('userData', JSON.stringify(userData));
      }
    } catch (error) {
      console.warn('사용자 데이터 업데이트 실패:', error);
    }
  }
};

/**
 * 디버깅용 API 로깅
 */
export const logApiCall = (
  apiName: string, 
  method: string, 
  params?: any, 
  response?: any
): void => {
  if (process.env.NODE_ENV === 'development') {
    console.group(`🔗 API: ${apiName}.${method}`);
    if (params) console.log('📤 Parameters:', params);
    if (response) console.log('📥 Response:', response);
    console.groupEnd();
  }
};