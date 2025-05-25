'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import { motion } from 'framer-motion';

export default function SignInPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // TODO: 로그인 처리
  };

  return (
    <div className="min-h-screen bg-white px-6 py-12">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="max-w-md mx-auto space-y-8"
      >
        <div className="text-center space-y-4">
          <h1 className="text-3xl font-bold">환영합니다</h1>
          <p className="text-gray-600">
            계정에 로그인하고 다양한 혜택을 누려보세요
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="space-y-4">
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
                이메일
              </label>
              <input
                id="email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-black focus:border-transparent"
                placeholder="your@email.com"
              />
            </div>
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-1">
                비밀번호
              </label>
              <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-black focus:border-transparent"
                placeholder="••••••••"
              />
            </div>
          </div>

          <button
            type="submit"
            className="w-full bg-black text-white py-4 rounded-2xl font-semibold text-lg"
          >
            로그인
          </button>
        </form>

        <div className="relative">
          <div className="absolute inset-0 flex items-center">
            <div className="w-full border-t border-gray-300" />
          </div>
          <div className="relative flex justify-center text-sm">
            <span className="px-2 bg-white text-gray-500">또는</span>
          </div>
        </div>

        <div className="space-y-4">
          <button className="w-full flex items-center justify-center py-4 px-4 rounded-2xl border-2 border-gray-300 hover:border-gray-400 transition-colors">
            <img src="/images/google.svg" alt="Google" className="w-5 h-5 mr-3" />
            Google로 계속하기
          </button>
          <button className="w-full flex items-center justify-center py-4 px-4 rounded-2xl border-2 border-gray-300 hover:border-gray-400 transition-colors">
            <img src="/images/apple.svg" alt="Apple" className="w-5 h-5 mr-3" />
            Apple로 계속하기
          </button>
        </div>

        <p className="text-center text-gray-600">
          계정이 없으신가요?{' '}
          <Link href="/account/signup" className="text-black font-semibold">
            회원가입
          </Link>
        </p>
      </motion.div>
    </div>
  );
} 