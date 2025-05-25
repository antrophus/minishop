'use client';

import React, { useState } from 'react';
import { motion } from 'framer-motion';
import Link from 'next/link';

export default function OnboardingPage() {
  const [currentSlide, setCurrentSlide] = useState(0);

  const slides = [
    {
      title: '최신 트렌드',
      description: '매일 업데이트되는 새로운 스타일을 만나보세요',
      image: '/images/onboarding1.jpg'
    },
    {
      title: '스마트한 쇼핑',
      description: '간편한 결제와 배송으로 쇼핑이 더욱 즐거워집니다',
      image: '/images/onboarding2.jpg'
    },
    {
      title: '특별한 혜택',
      description: '신규 회원을 위한 다양한 할인 혜택을 제공합니다',
      image: '/images/onboarding3.jpg'
    }
  ];

  return (
    <div className="min-h-screen bg-white">
      {/* 슬라이드 영역 */}
      <div className="relative h-[70vh]">
        <div className="absolute inset-0 bg-gradient-to-b from-gray-900/0 to-gray-900/60">
          <div className="aspect-square bg-gray-100" />
        </div>
        
        {/* 슬라이드 인디케이터 */}
        <div className="absolute bottom-8 left-0 right-0">
          <div className="flex justify-center space-x-2">
            {slides.map((_, index) => (
              <button
                key={index}
                onClick={() => setCurrentSlide(index)}
                className={`w-2 h-2 rounded-full transition-all duration-300 ${
                  currentSlide === index ? 'bg-white w-4' : 'bg-white/50'
                }`}
              />
            ))}
          </div>
        </div>
      </div>

      {/* 콘텐츠 영역 */}
      <div className="px-6 pt-8 pb-10">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="text-center space-y-4"
        >
          <h1 className="text-3xl font-bold">{slides[currentSlide].title}</h1>
          <p className="text-gray-600">
            {slides[currentSlide].description}
          </p>
        </motion.div>

        <div className="mt-12 space-y-4">
          <Link href="/account/signin">
            <button className="w-full bg-black text-white py-4 rounded-2xl font-semibold text-lg">
              시작하기
            </button>
          </Link>
        </div>
      </div>
    </div>
  );
} 