'use client';

import React, { useState } from 'react';
import { motion } from 'framer-motion';
import Link from 'next/link';

// Figma Get Started 스타일 온보딩 페이지
export default function GetStartedPage() {
  const [currentStep, setCurrentStep] = useState(0);

  const steps = [
    {
      icon: '🎨',
      title: 'Welcome to Mini Shop',
      subtitle: 'Your creative shopping journey starts here',
      description: 'Discover unique products, connect with creators, and build your perfect collection.',
      action: 'Get Started'
    },
    {
      icon: '🛍️',
      title: 'Explore Collections',
      subtitle: 'Curated just for you',
      description: 'Browse through carefully selected items from independent creators and brands worldwide.',
      action: 'Browse Now'
    },
    {
      icon: '✨',
      title: 'Join the Community',
      subtitle: 'Connect & Create',
      description: 'Share your style, get inspired by others, and become part of our creative community.',
      action: 'Join Community'
    }
  ];

  const currentStepData = steps[currentStep];

  const handleNext = () => {
    if (currentStep < steps.length - 1) {
      setCurrentStep(currentStep + 1);
    }
  };

  const handlePrev = () => {
    if (currentStep > 0) {
      setCurrentStep(currentStep - 1);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-50 via-white to-purple-50 flex flex-col">
      {/* Header */}
      <div className="flex justify-between items-center p-6">
        <div className="flex items-center space-x-2">
          <div className="w-8 h-8 bg-black rounded-lg flex items-center justify-center">
            <span className="text-white font-bold text-sm">M</span>
          </div>
          <span className="font-semibold text-lg">Mini Shop</span>
        </div>
        
        {currentStep < steps.length - 1 && (
          <button 
            onClick={() => setCurrentStep(steps.length - 1)}
            className="text-gray-500 text-sm hover:text-gray-700 transition-colors"
          >
            Skip
          </button>
        )}
      </div>

      {/* Progress Indicator */}
      <div className="px-6 mb-8">
        <div className="flex space-x-2">
          {steps.map((_, index) => (
            <div
              key={index}
              className={`h-1 flex-1 rounded-full transition-all duration-300 ${
                index <= currentStep ? 'bg-black' : 'bg-gray-200'
              }`}
            />
          ))}
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex flex-col items-center justify-center px-6 text-center">
        <motion.div
          key={currentStep}
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: -30 }}
          transition={{ duration: 0.4, ease: "easeOut" }}
          className="max-w-md mx-auto"
        >
          {/* Icon */}
          <div className="mb-8">
            <div className="w-24 h-24 mx-auto bg-white rounded-3xl shadow-lg flex items-center justify-center text-4xl border border-gray-100">
              {currentStepData.icon}
            </div>
          </div>

          {/* Content */}
          <div className="space-y-4 mb-12">
            <h1 className="text-3xl font-bold text-gray-900">
              {currentStepData.title}
            </h1>
            <h2 className="text-lg font-medium text-gray-600">
              {currentStepData.subtitle}
            </h2>
            <p className="text-gray-500 leading-relaxed">
              {currentStepData.description}
            </p>
          </div>
        </motion.div>
      </div>

      {/* Action Buttons */}
      <div className="p-6 space-y-4">
        {currentStep === steps.length - 1 ? (
          // Final step - Login/Signup buttons
          <div className="space-y-3">
            <Link href="/account/signup">
              <button className="w-full bg-black text-white py-4 rounded-2xl font-semibold text-lg hover:bg-gray-800 transition-colors">
                Create Account
              </button>
            </Link>
            <Link href="/account/signin">
              <button className="w-full border-2 border-gray-200 text-gray-700 py-4 rounded-2xl font-semibold text-lg hover:border-gray-300 transition-colors">
                Sign In
              </button>
            </Link>
          </div>
        ) : (
          // Navigation buttons
          <div className="flex space-x-3">
            {currentStep > 0 && (
              <button
                onClick={handlePrev}
                className="flex-1 border-2 border-gray-200 text-gray-700 py-4 rounded-2xl font-semibold text-lg hover:border-gray-300 transition-colors"
              >
                Back
              </button>
            )}
            <button
              onClick={handleNext}
              className={`py-4 rounded-2xl font-semibold text-lg transition-colors ${
                currentStep === 0 
                  ? 'w-full bg-black text-white hover:bg-gray-800' 
                  : 'flex-1 bg-black text-white hover:bg-gray-800'
              }`}
            >
              {currentStepData.action}
            </button>
          </div>
        )}
      </div>

      {/* Footer */}
      <div className="text-center pb-6">
        <p className="text-xs text-gray-400">
          By continuing, you agree to our Terms of Service and Privacy Policy
        </p>
      </div>
    </div>
  );
}
