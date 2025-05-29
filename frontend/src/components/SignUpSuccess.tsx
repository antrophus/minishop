'use client';

import React from 'react';
import Link from 'next/link';
import { motion } from 'framer-motion';

interface SignUpSuccessProps {
  userEmail: string;
  userName: string;
}

export default function SignUpSuccess({ userEmail, userName }: SignUpSuccessProps) {
  return (
    <div className="min-h-screen bg-white px-6 py-12">
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
            가입 완료! 🎉
          </h1>
          <p className="text-gray-600 leading-relaxed">
            <span className="font-semibold text-gray-900">{userName}</span>님,<br />
            My Little Shop에 오신 것을 환영합니다!
          </p>
        </div>
        {/* 인증 완료 정보 */}
        <div className="bg-green-50 rounded-2xl p-6 space-y-3">
          <div className="flex items-center justify-center space-x-2 text-green-700">
            <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
            </svg>
            <span className="font-semibold">이메일 인증 완료</span>
          </div>
          <p className="text-sm text-green-700">
            {userEmail} 주소가 성공적으로 인증되었습니다.
          </p>
        </div>

        {/* 다음 단계 안내 */}
        <div className="space-y-4">
          <h3 className="font-semibold text-gray-900">이제 무엇을 할 수 있나요?</h3>
          <div className="space-y-3">
            <div className="flex items-start space-x-3 text-left">
              <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center flex-shrink-0 mt-0.5">
                <span className="text-blue-600 font-semibold text-sm">1</span>
              </div>
              <div>
                <p className="font-medium text-gray-900">프로필 완성</p>
                <p className="text-sm text-gray-600">더 나은 쇼핑 경험을 위해 프로필을 완성해보세요</p>
              </div>
            </div>
            
            <div className="flex items-start space-x-3 text-left">
              <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center flex-shrink-0 mt-0.5">
                <span className="text-blue-600 font-semibold text-sm">2</span>
              </div>
              <div>
                <p className="font-medium text-gray-900">상품 둘러보기</p>
                <p className="text-sm text-gray-600">엄선된 상품들을 만나보세요</p>
              </div>
            </div>
            
            <div className="flex items-start space-x-3 text-left">
              <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center flex-shrink-0 mt-0.5">
                <span className="text-blue-600 font-semibold text-sm">3</span>
              </div>
              <div>
                <p className="font-medium text-gray-900">첫 주문하기</p>
                <p className="text-sm text-gray-600">신규 회원 특별 혜택을 놓치지 마세요</p>
              </div>
            </div>
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
            href="/account"
            className="w-full block py-4 bg-gray-100 text-gray-900 text-center rounded-2xl font-semibold hover:bg-gray-200 transition-colors"
          >
            프로필 완성하기
          </Link>
        </div>

        {/* 웰컴 메시지 */}
        <div className="border-t pt-6">
          <p className="text-sm text-gray-600 leading-relaxed">
            My Little Shop과 함께하는 특별한 쇼핑 여정을 응원합니다! ✨<br />
            궁금한 점이 있으시면 언제든 고객센터로 문의해주세요.
          </p>
        </div>
      </motion.div>
    </div>
  );
}