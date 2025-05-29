'use client';

import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { authApi } from '@/lib/api';

interface EmailVerificationWaitingProps {
  email: string;
  onBack: () => void;
  onVerificationComplete: () => void;
}

export default function EmailVerificationWaiting({ 
  email, 
  onBack, 
  onVerificationComplete 
}: EmailVerificationWaitingProps) {
  const [isResending, setIsResending] = useState(false);
  const [resendMessage, setResendMessage] = useState('');
  const [isChecking, setIsChecking] = useState(false);
  const [countdown, setCountdown] = useState(60);

  // 60초마다 인증 상태 확인
  useEffect(() => {
    const checkVerificationStatus = async () => {
      if (isChecking) return;
      
      setIsChecking(true);
      try {
        const response = await authApi.checkEmailVerification(email);
        if (response.success && response.data?.verified) {
          onVerificationComplete();
        }
      } catch (error) {
        console.error('인증 상태 확인 실패:', error);
      } finally {
        setIsChecking(false);
      }
    };

    const interval = setInterval(checkVerificationStatus, 5000); // 5초마다 확인
    return () => clearInterval(interval);
  }, [email, onVerificationComplete, isChecking]);

  // 재발송 버튼 활성화 카운트다운
  useEffect(() => {
    if (countdown > 0) {
      const timer = setTimeout(() => setCountdown(countdown - 1), 1000);
      return () => clearTimeout(timer);
    }
  }, [countdown]);

  const handleResendEmail = async () => {
    if (isResending || countdown > 0) return;

    setIsResending(true);
    setResendMessage('');
    
    try {
      const response = await authApi.resendEmailVerification(email);
      
      if (response.success) {
        setResendMessage('인증 이메일을 다시 발송했습니다.');
        setCountdown(60); // 60초 후 다시 버튼 활성화
      } else {
        setResendMessage(response.error || '이메일 재발송에 실패했습니다.');
      }
    } catch (error) {
      setResendMessage('이메일 재발송 중 오류가 발생했습니다.');
    } finally {
      setIsResending(false);
    }
  };

  return (
    <div className="min-h-screen bg-white px-6 py-12">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="max-w-md mx-auto space-y-8 text-center"
      >
        {/* 이메일 아이콘 */}
        <motion.div
          initial={{ scale: 0 }}
          animate={{ scale: 1 }}
          transition={{ delay: 0.2, type: "spring", stiffness: 200 }}
          className="w-24 h-24 mx-auto bg-blue-100 rounded-full flex items-center justify-center"
        >
          <svg className="w-12 h-12 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 4.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
          </svg>
        </motion.div>

        {/* 메인 메시지 */}
        <div className="space-y-4">
          <h1 className="text-3xl font-bold text-gray-900">
            이메일을 확인해주세요
          </h1>
          <p className="text-gray-600 leading-relaxed">
            <span className="font-semibold text-gray-900">{email}</span>로<br />
            인증 이메일을 발송했습니다.
          </p>
        </div>

        {/* 인증 상태 표시 */}
        {isChecking && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="flex items-center justify-center space-x-2 text-blue-600"
          >
            <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-blue-600"></div>
            <span className="text-sm">인증 확인 중...</span>
          </motion.div>
        )}

        {/* 안내 메시지 */}
        <div className="bg-gray-50 rounded-2xl p-6 space-y-4">
          <h3 className="font-semibold text-gray-900">📧 이메일 인증 안내</h3>
          <ul className="text-sm text-gray-600 space-y-2 text-left">
            <li>• 이메일에서 "이메일 인증하기" 버튼을 클릭하세요</li>
            <li>• 인증 링크는 24시간 후 만료됩니다</li>
            <li>• 이메일이 보이지 않으면 스팸함도 확인해주세요</li>
            <li>• 인증 완료 시 자동으로 다음 단계로 진행됩니다</li>
          </ul>
        </div>

        {/* 재발송 버튼 */}
        <div className="space-y-3">
          <button
            onClick={handleResendEmail}
            disabled={isResending || countdown > 0}
            className={`w-full py-4 rounded-2xl font-semibold transition-colors ${
              isResending || countdown > 0
                ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                : 'bg-blue-600 text-white hover:bg-blue-700'
            }`}
          >
            {isResending 
              ? '이메일 발송 중...' 
              : countdown > 0 
                ? `이메일 재발송 (${countdown}초 후 가능)`
                : '인증 이메일 재발송'
            }
          </button>

          {resendMessage && (
            <motion.p
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              className={`text-sm ${
                resendMessage.includes('발송') ? 'text-green-600' : 'text-red-600'
              }`}
            >
              {resendMessage}
            </motion.p>
          )}
        </div>

        {/* 이메일 변경/뒤로가기 */}
        <div className="space-y-3">
          <button
            onClick={onBack}
            className="text-gray-600 hover:text-gray-900 transition-colors"
          >
            다른 이메일로 가입하기
          </button>
        </div>

        {/* 고객센터 안내 */}
        <div className="border-t pt-6">
          <p className="text-xs text-gray-400 leading-relaxed">
            이메일을 받지 못하셨나요?<br />
            <span className="text-gray-600">고객센터: support@mylittleshop.site</span>
          </p>
        </div>
      </motion.div>
    </div>
  );
}