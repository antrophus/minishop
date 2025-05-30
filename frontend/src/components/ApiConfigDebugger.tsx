'use client';

import { getEnvironmentInfo, API_CONFIG } from '@/lib/api';

/**
 * 개발 환경에서만 표시되는 API 설정 정보 컴포넌트
 */
export function ApiConfigDebugger() {
  const envInfo = getEnvironmentInfo();

  // 프로덕션 환경에서는 표시하지 않음
  if (envInfo.isProduction) {
    return null;
  }

  return (
    <div className="fixed bottom-4 right-4 bg-black bg-opacity-80 text-white p-4 rounded-lg text-xs max-w-sm z-50">
      <h4 className="font-bold mb-2">🔧 API Config (Dev Only)</h4>
      <div className="space-y-1">
        <div><strong>Environment:</strong> {process.env.NODE_ENV}</div>
        <div><strong>API Base:</strong> {API_CONFIG.BASE_URL}</div>
        <div><strong>API URL:</strong> {API_CONFIG.API_URL}</div>
        <div><strong>Auth URL:</strong> {API_CONFIG.AUTH_URL}</div>
        <div><strong>API Context:</strong> {API_CONFIG.API_CONTEXT_PATH}</div>
        <div><strong>Auth Context:</strong> {API_CONFIG.AUTH_CONTEXT_PATH}</div>
      </div>
    </div>
  );
}