'use client';

import React, { useState, useEffect } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { 
  UserCircleIcon, 
  ShoppingBagIcon, 
  HeartIcon,
  BellIcon,
  CreditCardIcon,
  MapPinIcon,
  QuestionMarkCircleIcon,
  ChevronRightIcon,
  ArrowLeftStartOnRectangleIcon
} from '@heroicons/react/24/outline';
import { authApi, authUtils, type UserProfile } from '@/lib/api';

export default function AccountPage() {
  const router = useRouter();
  const [userProfile, setUserProfile] = useState<UserProfile | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    const loadUserProfile = async () => {
      try {
        // 토큰 확인
        const token = authUtils.getAuthToken();
        console.log('저장된 토큰:', token ? '토큰 있음' : '토큰 없음');
        console.log('로그인 상태:', authUtils.isLoggedIn());

        // 로그인 상태 확인
        if (!authUtils.isLoggedIn()) {
          console.log('로그인되지 않음, 로그인 페이지로 이동');
          router.push('/account/signin');
          return;
        }

        // 사용자 프로필 가져오기
        console.log('프로필 API 호출 시작...');
        const response = await authApi.getCurrentUserProfile();
        console.log('프로필 API 응답:', response);
        
        if (response.success && response.data) {
          // 백엔드에서 {success: true, data: {실제데이터}} 형태로 응답하므로
          // response.data.data에서 실제 사용자 정보를 추출
          const actualUserData = (response.data as any).data || response.data;
          console.log('실제 사용자 데이터:', actualUserData);
          setUserProfile(actualUserData as UserProfile);
        } else {
          console.error('프로필 로딩 실패:', response);
          setError(response.error || '사용자 정보를 가져오는데 실패했습니다.');
          // 토큰이 만료되었거나 유효하지 않은 경우 로그인 페이지로 이동
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

  const handleLogout = () => {
    authUtils.removeAuthToken();
    router.push('/onboarding');
  };

  const menuItems = [
    {
      title: '주문 내역',
      icon: ShoppingBagIcon,
      href: '/orders',
      badge: '2'
    },
    {
      title: '위시리스트',
      icon: HeartIcon,
      href: '/wishlist',
      badge: '5'
    },
    {
      title: '알림 설정',
      icon: BellIcon,
      href: '/notifications'
    },
    {
      title: '결제 수단 관리',
      icon: CreditCardIcon,
      href: '/payment-methods'
    },
    {
      title: '배송지 관리',
      icon: MapPinIcon,
      href: '/addresses'
    },
    {
      title: '고객 센터',
      icon: QuestionMarkCircleIcon,
      href: '/support'
    }
  ];

  // 로딩 중 화면
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

  // 에러 화면
  if (error) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
            {error}
          </div>
          <Link href="/account/signin" className="mt-4 inline-block text-blue-600 hover:underline">
            로그인 페이지로 이동
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* 프로필 섹션 */}
      <div className="bg-white px-6 py-8">
        <div className="flex items-center space-x-4">
          <div className="w-20 h-20 bg-gray-100 rounded-full flex items-center justify-center">
            <UserCircleIcon className="w-12 h-12 text-gray-400" />
          </div>
          <div>
            <h1 className="text-xl font-semibold">
              {userProfile?.name || '사용자'}
            </h1>
            <p className="text-gray-600">{userProfile?.email}</p>
            {userProfile?.emailVerified && (
              <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800 mt-1">
                ✅ 인증 완료
              </span>
            )}
            {/* 디버깅 정보
            <div className="text-xs text-gray-400 mt-2">
              디버그: name=&quot;{userProfile?.name}&quot;, email=&quot;{userProfile?.email}&quot;, id={userProfile?.id}
            </div> */}
          </div>
        </div>
        <Link href="/account/edit">
          <button className="mt-6 w-full py-3 px-4 border border-gray-300 rounded-2xl text-sm font-medium hover:bg-gray-50 transition-colors">
            프로필 수정
          </button>
        </Link>
      </div>

      {/* 메뉴 리스트 */}
      <div className="px-6 py-4">
        <div className="bg-white rounded-3xl overflow-hidden">
          {menuItems.map((item, index) => (
            <Link key={item.title} href={item.href}>
              <div className={`flex items-center justify-between p-4 ${
                index !== menuItems.length - 1 ? 'border-b border-gray-100' : ''
              }`}>
                <div className="flex items-center space-x-4">
                  <item.icon className="w-6 h-6 text-gray-600" />
                  <span className="font-medium">{item.title}</span>
                </div>
                <div className="flex items-center space-x-3">
                  {item.badge && (
                    <span className="bg-blue-100 text-blue-600 px-2 py-1 rounded-full text-xs font-medium">
                      {item.badge}
                    </span>
                  )}
                  <ChevronRightIcon className="w-5 h-5 text-gray-400" />
                </div>
              </div>
            </Link>
          ))}
        </div>
      </div>

      {/* 로그아웃 버튼 */}
      <div className="px-6 py-4">
        <button 
          onClick={handleLogout}
          className="w-full flex items-center justify-center space-x-2 py-4 text-red-600 bg-white rounded-2xl hover:bg-red-50 transition-colors"
        >
          <ArrowLeftStartOnRectangleIcon className="w-5 h-5" />
          <span className="font-medium">로그아웃</span>
        </button>
      </div>
    </div>
  );
} 