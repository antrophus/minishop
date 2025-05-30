'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { motion } from 'framer-motion';
import { 
  ArrowLeftIcon,
  UserCircleIcon 
} from '@heroicons/react/24/outline';
import { authApi, authUtils, type UserProfile } from '@/lib/api';

export default function EditProfilePage() {
  const router = useRouter();
  const [userProfile, setUserProfile] = useState<UserProfile | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState<string>('');
  const [successMessage, setSuccessMessage] = useState<string>('');

  const [formData, setFormData] = useState({
    name: '',
    phone: '',
    address: '',
    gender: ''
  });

  useEffect(() => {
    const loadUserProfile = async () => {
      try {
        if (!authUtils.isLoggedIn()) {
          router.push('/account/signin');
          return;
        }

        const response = await authApi.getCurrentUserProfile();
        if (response.success && response.data) {
          // 백엔드에서 {success: true, data: {실제데이터}} 형태로 응답하므로
          // response.data.data에서 실제 사용자 정보를 추출
          const actualUserData = (response.data as any).data || response.data;
          console.log('프로필 편집 - 실제 사용자 데이터:', actualUserData);
          
          setUserProfile(actualUserData as UserProfile);
          setFormData({
            name: actualUserData.name || '',
            phone: actualUserData.phone || '',
            address: actualUserData.address || '',
            gender: actualUserData.gender || ''
          });
        } else {
          setError(response.error || '사용자 정보를 가져오는데 실패했습니다.');
          if (response.error?.includes('인증') || response.error?.includes('401')) {
            authUtils.removeAuthToken();
            router.push('/account/signin');
          }
        }
      } catch (error) {
        console.error('프로필 로딩 오류:', error);
        setError('네트워크 오류가 발생했습니다.');
      } finally {
        setIsLoading(false);
      }
    };

    loadUserProfile();
  }, [router]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    if (error) {
      setError('');
    }
    if (successMessage) {
      setSuccessMessage('');
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.name.trim()) {
      setError('이름을 입력해주세요.');
      return;
    }

    setIsSaving(true);
    setError('');

    try {
      const response = await authApi.updateCurrentUserProfile(formData);
      console.log('프로필 업데이트 응답:', response);
      
      if (response.success) {
        setSuccessMessage('프로필이 성공적으로 업데이트되었습니다.');
        
        setTimeout(() => {
          router.push('/account');
        }, 1500);
      } else {
        setError(response.error || '프로필 업데이트에 실패했습니다.');
      }
    } catch (error) {
      console.error('프로필 업데이트 오류:', error);
      setError('프로필 업데이트 중 오류가 발생했습니다.');
    } finally {
      setIsSaving(false);
    }
  };

  const handleBack = () => {
    router.back();
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-black mx-auto"></div>
          <p className="mt-4 text-gray-600">사용자 정보를 불러오는 중...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* 헤더 */}
      <div className="bg-white px-6 py-4 border-b">
        <div className="flex items-center space-x-4">
          <button onClick={handleBack} className="p-2 -ml-2 rounded-full hover:bg-gray-100">
            <ArrowLeftIcon className="w-6 h-6" />
          </button>
          <h1 className="text-xl font-semibold">프로필 수정</h1>
        </div>
      </div>

      <div className="px-6 py-8">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="max-w-md mx-auto space-y-8"
        >
          {/* 프로필 사진 섹션 */}
          <div className="text-center">
            <div className="w-24 h-24 mx-auto bg-gray-100 rounded-full flex items-center justify-center">
              <UserCircleIcon className="w-16 h-16 text-gray-400" />
            </div>
            <button className="mt-4 text-blue-600 font-medium hover:underline">
              사진 변경
            </button>
          </div>

          {/* 성공 메시지 */}
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

          {/* 에러 메시지 */}
          {error && (
            <motion.div
              initial={{ opacity: 0, y: -10 }}
              animate={{ opacity: 1, y: 0 }}
              className="bg-red-50 border border-red-200 rounded-xl p-4"
            >
              <div className="flex items-center space-x-2">
                <svg className="w-5 h-5 text-red-500" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
                </svg>
                <p className="text-red-700 text-sm">{error}</p>
              </div>
            </motion.div>
          )}

          {/* 수정 폼 */}
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* 이메일 (수정 불가) */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                이메일
              </label>
              <input
                type="email"
                value={userProfile?.email || ''}
                disabled
                className="w-full px-4 py-3 rounded-xl border border-gray-300 bg-gray-100 text-gray-500 cursor-not-allowed"
              />
              <p className="text-xs text-gray-500 mt-1">이메일은 변경할 수 없습니다.</p>
            </div>

            {/* 이름 */}
            <div>
              <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
                이름 <span className="text-red-500">*</span>
              </label>
              <input
                id="name"
                name="name"
                type="text"
                value={formData.name}
                onChange={handleInputChange}
                disabled={isSaving}
                className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-black focus:border-transparent disabled:bg-gray-100 disabled:cursor-not-allowed"
                placeholder="홍길동"
                required
              />
            </div>

            {/* 전화번호 */}
            <div>
              <label htmlFor="phone" className="block text-sm font-medium text-gray-700 mb-1">
                전화번호
              </label>
              <input
                id="phone"
                name="phone"
                type="tel"
                value={formData.phone}
                onChange={handleInputChange}
                disabled={isSaving}
                className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-black focus:border-transparent disabled:bg-gray-100 disabled:cursor-not-allowed"
                placeholder="010-1234-5678"
              />
            </div>

            {/* 주소 */}
            <div>
              <label htmlFor="address" className="block text-sm font-medium text-gray-700 mb-1">
                주소
              </label>
              <input
                id="address"
                name="address"
                type="text"
                value={formData.address}
                onChange={handleInputChange}
                disabled={isSaving}
                className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-black focus:border-transparent disabled:bg-gray-100 disabled:cursor-not-allowed"
                placeholder="서울시 강남구..."
              />
            </div>

            {/* 성별 */}
            <div>
              <label htmlFor="gender" className="block text-sm font-medium text-gray-700 mb-1">
                성별
              </label>
              <select
                id="gender"
                name="gender"
                value={formData.gender}
                onChange={handleInputChange}
                disabled={isSaving}
                className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-2 focus:ring-black focus:border-transparent disabled:bg-gray-100 disabled:cursor-not-allowed"
              >
                <option value="">선택하지 않음</option>
                <option value="MALE">남성</option>
                <option value="FEMALE">여성</option>
                <option value="OTHER">기타</option>
              </select>
            </div>

            {/* 저장 버튼 */}
            <button
              type="submit"
              disabled={isSaving || !formData.name.trim()}
              className={`w-full py-4 rounded-2xl font-semibold text-lg transition-colors ${
                isSaving || !formData.name.trim()
                  ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                  : 'bg-black text-white hover:bg-gray-800'
              }`}
            >
              {isSaving ? (
                <div className="flex items-center justify-center space-x-2">
                  <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                  <span>저장 중...</span>
                </div>
              ) : (
                '변경사항 저장'
              )}
            </button>
          </form>
        </motion.div>
      </div>
    </div>
  );
}
