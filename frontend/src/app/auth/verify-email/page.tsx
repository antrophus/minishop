'use client';

import React, { useEffect, useState } from 'react';
import { useSearchParams, useRouter } from 'next/navigation';
import { motion } from 'framer-motion';
import Link from 'next/link';

export default function VerifyEmailPage() {
  const searchParams = useSearchParams();
  const router = useRouter();
  const [status, setStatus] = useState<'loading' | 'success' | 'error'>('loading');
  const [message, setMessage] = useState('');

  useEffect(() => {
    const token = searchParams.get('token');
    
    if (!token) {
      setStatus('error');
      setMessage('인증 토큰이 없습니다.');
      return;
    }

    // 백엔드 API 호출 - 브라우저가 직접 백엔드의 HTML 응답을 받도록 함
    const verifyEmail = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/auth/verify-email?token=${token}`, {
          method: 'GET',
          credentials: 'include'
        });

        if (response.ok) {
          setStatus('success');
          setMessage('이메일 인증이 완료되었습니다!');
          
          // 3초 후 로그인 페이지로 리다이렉트
          setTimeout(() => {
            router.push('/account/signin?verified=true');
          }, 3000);
        } else {
          const errorText = await response.text();
          setStatus('error');
          setMessage(errorText || '인증에 실패했습니다.');
        }
      } catch (error) {
        console.error('이메일 인증 오류:', error);
        setStatus('error');
        setMessage('네트워크 오류가 발생했습니다.');
      }
    };

    verifyEmail();
  }, [searchParams, router]);
  return (
    <div className="min-h-screen bg-white px-6 py-12">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="max-w-md mx-auto space-y-8 text-center"
      >
        {/* Status Icon */}
        <motion.div
          initial={{ scale: 0 }}
          animate={{ scale: 1 }}
          transition={{ delay: 0.2, type: "spring", stiffness: 200 }}
          className={`w-24 h-24 mx-auto rounded-full flex items-center justify-center ${
            status === 'loading' ? 'bg-blue-100' :
            status === 'success' ? 'bg-green-100' : 'bg-red-100'
          }`}
        >
          {status === 'loading' && (
            <div className="animate-spin rounded-full h-12 w-12 border-4 border-blue-600 border-t-transparent"></div>
          )}
          {status === 'success' && (
            <svg className="w-12 h-12 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
            </svg>
          )}
          {status === 'error' && (
            <svg className="w-12 h-12 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          )}
        </motion.div>

        {/* Title */}
        <div className="space-y-4">
          <h1 className="text-3xl font-bold text-gray-900">
            {status === 'loading' && '이메일 인증 중...'}
            {status === 'success' && '인증 완료! 🎉'}
            {status === 'error' && '인증 실패'}
          </h1>
          <p className="text-gray-600 leading-relaxed">
            {message || '잠시만 기다려주세요...'}
          </p>
        </div>
        {/* Success Actions */}
        {status === 'success' && (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.5 }}
            className="space-y-4"
          >
            <div className="bg-green-50 rounded-2xl p-6">
              <p className="text-green-700 text-sm">
                My Little Shop에 오신 것을 환영합니다!<br />
                3초 후 자동으로 로그인 페이지로 이동합니다.
              </p>
            </div>
            
            <Link
              href="/account/signin"
              className="w-full block py-4 bg-black text-white text-center rounded-2xl font-semibold hover:bg-gray-800 transition-colors"
            >
              지금 로그인하기
            </Link>
          </motion.div>
        )}

        {/* Error Actions */}
        {status === 'error' && (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.5 }}
            className="space-y-4"
          >
            <div className="bg-red-50 rounded-2xl p-6">
              <p className="text-red-700 text-sm">
                인증 링크가 만료되었거나 유효하지 않을 수 있습니다.<br />
                새로운 인증 이메일을 요청해보세요.
              </p>
            </div>
            
            <div className="space-y-3">
              <Link
                href="/account/signup"
                className="w-full block py-4 bg-black text-white text-center rounded-2xl font-semibold hover:bg-gray-800 transition-colors"
              >
                다시 회원가입하기
              </Link>
              
              <Link
                href="/account/signin"
                className="w-full block py-4 bg-gray-100 text-gray-900 text-center rounded-2xl font-semibold hover:bg-gray-200 transition-colors"
              >
                로그인 페이지로
              </Link>
            </div>
          </motion.div>
        )}

        {/* Footer */}
        <div className="border-t pt-6">
          <p className="text-xs text-gray-400 leading-relaxed">
            문제가 지속되면 고객센터로 문의해주세요.<br />
            <span className="text-gray-600">support@mylittleshop.site</span>
          </p>
        </div>
      </motion.div>
    </div>
  );
}