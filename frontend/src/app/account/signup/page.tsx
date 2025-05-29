'use client';

import React, { useState, useEffect } from 'react';
import Link from 'next/link';
import { motion } from 'framer-motion';
import { authApi, type SignUpRequest } from '@/lib/api';

type SignUpStep = 'email' | 'password' | 'success';

export default function SignUpPage() {
  const [currentStep, setCurrentStep] = useState<SignUpStep>('email');
  const [isLoading, setIsLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  
  // 이메일 인증 관련 상태
  const [isVerificationSent, setIsVerificationSent] = useState(false);
  const [isEmailVerified, setIsEmailVerified] = useState(false);
  const [verificationTimer, setVerificationTimer] = useState(0);
  const [isCheckingVerification, setIsCheckingVerification] = useState(false);
  
  const [userData, setUserData] = useState({
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
    agreeTerms: false,
    agreeMarketing: false
  });

  // URL 파라미터 확인 (이메일 인증 완료 후 리다이렉트)
  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const emailParam = urlParams.get('email');
    const verifiedParam = urlParams.get('verified');
    
    if (emailParam && verifiedParam === 'true') {
      // 이메일 인증이 완료된 상태로 접근
      setUserData(prev => ({ ...prev, email: emailParam }));
      setIsEmailVerified(true);
      setCurrentStep('password');
      setSuccessMessage('이메일 인증이 완료되었습니다! 비밀번호를 설정해주세요.');
      
      // 사용자 정보 가져오기 (이름 정보 포함)
      const fetchUserInfo = async () => {
        try {
          console.log('사용자 정보 조회 시작:', emailParam);
          const response = await authApi.getUserInfo(emailParam);
          console.log('사용자 정보 조회 응답:', response);
          if (response.success && response.data) {
            console.log('받은 사용자 데이터:', response.data);
            // API 응답 구조 확인: response.data.data에 실제 사용자 정보가 있을 수 있음
            const userData = response.data.data || response.data;
            console.log('실제 사용자 데이터:', userData);
            setUserData(prev => ({ 
              ...prev, 
              email: emailParam,
              name: userData?.name || ''
            }));
            console.log('업데이트된 userData:', { 
              email: emailParam,
              name: userData?.name || ''
            });
          }
        } catch (error) {
          console.error('사용자 정보 조회 실패:', error);
        }
      };
      
      fetchUserInfo();
    }
  }, []);

  // 인증 확인 타이머
  useEffect(() => {
    let interval: NodeJS.Timeout;
    
    if (isVerificationSent && !isEmailVerified) {
      interval = setInterval(async () => {
        if (!isCheckingVerification && userData.email) {
          setIsCheckingVerification(true);
          try {
            const response = await authApi.checkEmailVerification(userData.email);
            if (response.success && response.data?.verified) {
              setIsEmailVerified(true);
              setCurrentStep('password');
              setSuccessMessage('이메일 인증이 완료되었습니다! 이제 비밀번호를 설정해주세요.');
              clearInterval(interval);
            }
          } catch (error) {
            console.error('인증 상태 확인 실패:', error);
          } finally {
            setIsCheckingVerification(false);
          }
        }
      }, 3000); // 3초마다 확인
    }
    
    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isVerificationSent, isEmailVerified, userData.email, isCheckingVerification]);

  // 재발송 타이머
  useEffect(() => {
    if (verificationTimer > 0) {
      const timer = setTimeout(() => setVerificationTimer(verificationTimer - 1), 1000);
      return () => clearTimeout(timer);
    }
  }, [verificationTimer]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setUserData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
    
    if (errorMessage) {
      setErrorMessage('');
    }
  };

  // 이메일 인증 요청
  const handleSendVerification = async () => {
    if (!userData.email || !userData.name) {
      setErrorMessage('이름과 이메일을 모두 입력해주세요.');
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(userData.email)) {
      setErrorMessage('올바른 이메일 형식을 입력해주세요.');
      return;
    }

    setIsLoading(true);
    setErrorMessage('');

    try {
      const response = await authApi.requestEmailVerification({
        email: userData.email.trim().toLowerCase(),
        name: userData.name.trim(),
      });

      if (response.success) {
        setIsVerificationSent(true);
        setVerificationTimer(60); // 60초 재발송 타이머
        setSuccessMessage('인증 이메일을 발송했습니다. 이메일을 확인해주세요.');
      } else {
        setErrorMessage(response.error || response.data?.message || '이메일 발송에 실패했습니다.');
      }
    } catch (error) {
      console.error('이메일 인증 요청 오류:', error);
      setErrorMessage('네트워크 오류가 발생했습니다.');
    } finally {
      setIsLoading(false);
    }
  };
  // 이메일 재발송
  const handleResendVerification = async () => {
    if (verificationTimer > 0) return;

    setIsLoading(true);
    try {
      const response = await authApi.resendEmailVerification(userData.email);
      if (response.success) {
        setVerificationTimer(60);
        setSuccessMessage('인증 이메일을 다시 발송했습니다.');
      } else {
        setErrorMessage(response.error || '이메일 재발송에 실패했습니다.');
      }
    } catch (error) {
      setErrorMessage('이메일 재발송 중 오류가 발생했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  // 비밀번호 설정 및 최종 회원가입
  const handlePasswordSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!userData.password || userData.password.length < 8) {
      setErrorMessage('비밀번호는 8자 이상이어야 합니다.');
      return;
    }
    
    if (userData.password !== userData.confirmPassword) {
      setErrorMessage('비밀번호가 일치하지 않습니다.');
      return;
    }
    
    if (!userData.agreeTerms) {
      setErrorMessage('이용약관에 동의해주세요.');
      return;
    }

    setIsLoading(true);
    setErrorMessage('');

    try {
      const response = await authApi.completeRegistration({
        email: userData.email,
        password: userData.password,
        name: userData.name, // name도 함께 전달
      });

      if (response.success) {
        console.log('회원가입 완료 성공, 현재 userData:', userData);
        setCurrentStep('success');
      } else {
        setErrorMessage(response.error || response.data?.message || '회원가입 완료에 실패했습니다.');
      }
    } catch (error) {
      console.error('회원가입 완료 오류:', error);
      setErrorMessage('네트워크 오류가 발생했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  // 이메일 단계로 돌아가기
  const handleBackToEmail = () => {
    setCurrentStep('email');
    setIsVerificationSent(false);
    setIsEmailVerified(false);
    setVerificationTimer(0);
    setSuccessMessage('');
    setErrorMessage('');
  };

  // 이메일 인증 단계 렌더링
  const renderEmailStep = () => (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
      className="max-w-md mx-auto space-y-8"
    >
      {/* Header */}
      <div className="text-center space-y-4">
        <h1 className="text-3xl font-bold">이메일 인증</h1>
        <p className="text-gray-600">
          먼저 이메일 주소를 인증해주세요
        </p>
      </div>

      {/* 성공 메시지 표시 */}
      {successMessage && (
        <motion.div
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          className="bg-green-50 border border-green-200 rounded-xl p-4"
        >
          <div className="flex items-center space-x-2">
            <svg className="w-5 h-5 text-green-500" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
            </svg>
            <p className="text-green-700 text-sm">{successMessage}</p>
          </div>
        </motion.div>
      )}
      {errorMessage && (
        <motion.div
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          className="bg-red-50 border border-red-200 rounded-xl p-4"
        >
          <div className="flex items-center space-x-2">
            <svg className="w-5 h-5 text-red-500" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
            </svg>
            <p className="text-red-700 text-sm">{errorMessage}</p>
          </div>
        </motion.div>
      )}

      {successMessage && (
        <motion.div
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          className="bg-green-50 border border-green-200 rounded-xl p-4"
        >
          <div className="flex items-center space-x-2">
            <svg className="w-5 h-5 text-green-500" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
            </svg>
            <p className="text-green-700 text-sm">{successMessage}</p>
          </div>
        </motion.div>
      )}
      {/* 인증 진행 상태 */}
      {isVerificationSent && (
        <div className="bg-blue-50 rounded-2xl p-6 space-y-4">
          <div className="flex items-center space-x-2">
            {isCheckingVerification ? (
              <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-blue-600"></div>
            ) : (
              <div className="w-5 h-5 bg-blue-600 rounded-full flex items-center justify-center">
                <svg className="w-3 h-3 text-white" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                </svg>
              </div>
            )}
            <p className="text-blue-700 font-medium">
              {isCheckingVerification ? '인증 확인 중...' : '이메일 인증 대기 중'}
            </p>
          </div>
          <p className="text-blue-600 text-sm">
            {userData.email}로 발송된 이메일에서 인증 링크를 클릭해주세요.
          </p>
        </div>
      )}

      {/* 입력 폼 */}
      <div className="space-y-6">
        {/* 이름 입력 */}
        <div>
          <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
            이름
          </label>
          <input
            id="name"
            name="name"
            type="text"
            value={userData.name}
            onChange={handleInputChange}
            disabled={isVerificationSent}
            className={`w-full px-4 py-3 rounded-xl border focus:ring-2 focus:ring-black focus:border-transparent ${
              isVerificationSent ? 'bg-gray-100 cursor-not-allowed' : 'border-gray-300'
            }`}
            placeholder="홍길동"
            required
          />
        </div>

        {/* 이메일 입력 및 인증 버튼 */}
        <div>
          <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
            이메일
          </label>
          <div className="flex space-x-3">
            <input
              id="email"
              name="email"
              type="email"
              value={userData.email}
              onChange={handleInputChange}
              disabled={isVerificationSent}
              className={`flex-1 px-4 py-3 rounded-xl border focus:ring-2 focus:ring-black focus:border-transparent ${
                isVerificationSent ? 'bg-gray-100 cursor-not-allowed' : 'border-gray-300'
              }`}
              placeholder="your@email.com"
              required
            />
            <button
              type="button"
              onClick={isVerificationSent ? handleResendVerification : handleSendVerification}
              disabled={isLoading || !userData.email || !userData.name || verificationTimer > 0}
              className={`px-6 py-3 rounded-xl font-semibold transition-colors ${
                isLoading || !userData.email || !userData.name || verificationTimer > 0
                  ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                  : isVerificationSent
                    ? 'bg-blue-600 text-white hover:bg-blue-700'
                    : 'bg-black text-white hover:bg-gray-800'
              }`}
            >
              {isLoading ? (
                <div className="flex items-center space-x-2">
                  <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
                </div>
              ) : verificationTimer > 0 ? (
                `${verificationTimer}초`
              ) : isVerificationSent ? (
                '재발송'
              ) : (
                '인증'
              )}
            </button>
          </div>
        </div>
      </div>
      {/* 안내 메시지 */}
      <div className="bg-gray-50 rounded-2xl p-6 space-y-3">
        <h3 className="font-semibold text-gray-900">📧 이메일 인증 안내</h3>
        <ul className="text-sm text-gray-600 space-y-1">
          <li>• 이메일 주소가 확인되면 자동으로 다음 단계로 진행됩니다</li>
          <li>• 인증 링크는 24시간 후 만료됩니다</li>
          <li>• 스팸함도 확인해주세요</li>
        </ul>
      </div>

      {/* Footer */}
      <p className="text-center text-gray-600">
        이미 계정이 있으신가요?{' '}
        <Link href="/account/signin" className="text-black font-semibold hover:underline">
          로그인
        </Link>
      </p>
    </motion.div>
  );

  // 비밀번호 설정 단계 렌더링
  const renderPasswordStep = () => (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
      className="max-w-md mx-auto space-y-8"
    >
      {/* Header */}
      <div className="text-center space-y-4">
        <div className="w-16 h-16 mx-auto bg-green-100 rounded-full flex items-center justify-center">
          <svg className="w-8 h-8 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
          </svg>
        </div>
        <h1 className="text-3xl font-bold">비밀번호 설정</h1>
        <p className="text-gray-600">
          {userData.name ? `${userData.name}님, ` : ''}마지막 단계입니다!
        </p>
      </div>

      {/* 인증된 이메일 표시 */}
      <div className="bg-green-50 rounded-2xl p-4">
        <div className="flex items-center space-x-2">
          <svg className="w-5 h-5 text-green-600" fill="currentColor" viewBox="0 0 20 20">
            <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
          </svg>
          <div>
            <p className="text-green-800 font-medium">{userData.email}</p>
            <p className="text-green-600 text-sm">인증 완료</p>
          </div>
        </div>
      </div>

      {/* 에러 메시지 */}
      {errorMessage && (
        <motion.div
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          className="bg-red-50 border border-red-200 rounded-xl p-4"
        >
          <div className="flex items-center space-x-2">
            <svg className="w-5 h-5 text-red-500" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
            </svg>
            <p className="text-red-700 text-sm">{errorMessage}</p>
          </div>
        </motion.div>
      )}
      {/* 비밀번호 설정 폼 */}
      <form onSubmit={handlePasswordSubmit} className="space-y-6">
        <div className="space-y-4">
          {/* 비밀번호 입력 */}
          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-1">
              비밀번호
            </label>
            <input
              id="password"
              name="password"
              type="password"
              value={userData.password}
              onChange={handleInputChange}
              className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-black focus:border-transparent"
              placeholder="••••••••"
              required
            />
            <p className="text-xs text-gray-500 mt-1">
              8자 이상, 영문/숫자/특수문자 포함
            </p>
          </div>

          {/* 비밀번호 확인 */}
          <div>
            <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700 mb-1">
              비밀번호 확인
            </label>
            <input
              id="confirmPassword"
              name="confirmPassword"
              type="password"
              value={userData.confirmPassword}
              onChange={handleInputChange}
              className={`w-full px-4 py-3 rounded-xl border focus:ring-2 focus:ring-black focus:border-transparent ${
                userData.confirmPassword && userData.password !== userData.confirmPassword
                  ? 'border-red-300 focus:ring-red-500'
                  : 'border-gray-300'
              }`}
              placeholder="••••••••"
              required
            />
            {userData.confirmPassword && userData.password !== userData.confirmPassword && (
              <p className="text-xs text-red-500 mt-1">
                비밀번호가 일치하지 않습니다
              </p>
            )}
          </div>
        </div>

        {/* 약관 동의 */}
        <div className="space-y-3">
          <div className="flex items-start space-x-3">
            <input
              id="agreeTerms"
              name="agreeTerms"
              type="checkbox"
              checked={userData.agreeTerms}
              onChange={handleInputChange}
              className="mt-1 h-4 w-4 text-black focus:ring-black border-gray-300 rounded"
              required
            />
            <label htmlFor="agreeTerms" className="text-sm text-gray-700">
              <span className="text-red-500">*</span> 
              <Link href="/terms" className="underline hover:text-black">
                이용약관
              </Link>
              {' '}및{' '}
              <Link href="/privacy" className="underline hover:text-black">
                개인정보처리방침
              </Link>
              에 동의합니다
            </label>
          </div>
          
          <div className="flex items-start space-x-3">
            <input
              id="agreeMarketing"
              name="agreeMarketing"
              type="checkbox"
              checked={userData.agreeMarketing}
              onChange={handleInputChange}
              className="mt-1 h-4 w-4 text-black focus:ring-black border-gray-300 rounded"
            />
            <label htmlFor="agreeMarketing" className="text-sm text-gray-700">
              마케팅 정보 수신에 동의합니다 (선택)
            </label>
          </div>
        </div>
        {/* 버튼들 */}
        <div className="space-y-3">
          <button
            type="submit"
            disabled={!userData.password || !userData.confirmPassword || userData.password !== userData.confirmPassword || !userData.agreeTerms}
            className={`w-full py-4 rounded-2xl font-semibold text-lg transition-colors ${
              userData.password && userData.confirmPassword && userData.password === userData.confirmPassword && userData.agreeTerms
                ? 'bg-black text-white hover:bg-gray-800'
                : 'bg-gray-300 text-gray-500 cursor-not-allowed'
            }`}
          >
            회원가입 완료
          </button>
          
          <button
            type="button"
            onClick={handleBackToEmail}
            className="w-full py-3 text-gray-600 hover:text-gray-900 transition-colors"
          >
            이메일 변경하기
          </button>
        </div>
      </form>
    </motion.div>
  );

  // 성공 단계 렌더링
  const renderSuccessStep = () => (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
      className="max-w-md mx-auto space-y-8 text-center"
    >
      {/* 성공 아이콘 */}
      <motion.div
        initial={{ scale: 0 }}
        animate={{ scale: 1 }}
        transition={{ delay: 0.2, type: "spring", stiffness: 200 }}
        className="w-24 h-24 mx-auto bg-green-100 rounded-full flex items-center justify-center"
      >
        <svg className="w-12 h-12 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
        </svg>
      </motion.div>

      {/* 메인 메시지 */}
      <div className="space-y-4">
        <h1 className="text-3xl font-bold text-gray-900">
          회원가입 완료! 🎉
        </h1>
        <p className="text-gray-600 leading-relaxed">
          <span className="font-semibold text-gray-900">
            {userData.name || '회원'}
          </span>님,<br />
          My Little Shop에 오신 것을 환영합니다!
        </p>
        {/* 디버깅용 정보 표시 */}
        <div className="text-xs text-gray-400 mt-2">
          디버그: 이름="{userData.name}", 이메일="{userData.email}"
        </div>
      </div>

      {/* 액션 버튼들 */}
      <div className="space-y-3">
        <Link
          href="/shop"
          className="w-full block py-4 bg-black text-white text-center rounded-2xl font-semibold hover:bg-gray-800 transition-colors"
        >
          쇼핑 시작하기
        </Link>
        
        <Link
          href="/account/signin"
          className="w-full block py-4 bg-gray-100 text-gray-900 text-center rounded-2xl font-semibold hover:bg-gray-200 transition-colors"
        >
          로그인하기
        </Link>
      </div>
    </motion.div>
  );

  // 메인 렌더링
  return (
    <div className="min-h-screen bg-white px-6 py-12">
      {currentStep === 'email' && renderEmailStep()}
      {currentStep === 'password' && renderPasswordStep()}
      {currentStep === 'success' && renderSuccessStep()}
    </div>
  );
}