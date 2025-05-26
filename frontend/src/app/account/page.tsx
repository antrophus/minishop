'use client';

import React from 'react';
import Link from 'next/link';
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

export default function AccountPage() {
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

  return (
    <div className="min-h-screen bg-gray-50">
      {/* 프로필 섹션 */}
      <div className="bg-white px-6 py-8">
        <div className="flex items-center space-x-4">
          <div className="w-20 h-20 bg-gray-100 rounded-full flex items-center justify-center">
            <UserCircleIcon className="w-12 h-12 text-gray-400" />
          </div>
          <div>
            <h1 className="text-xl font-semibold">홍길동</h1>
            <p className="text-gray-600">hong@example.com</p>
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
        <Link href="/onboarding">
          <button className="w-full flex items-center justify-center space-x-2 py-4 text-red-600 bg-white rounded-2xl hover:bg-red-50 transition-colors">
            <ArrowLeftStartOnRectangleIcon className="w-5 h-5" />
            <span className="font-medium">로그아웃</span>
          </button>
        </Link>
      </div>
    </div>
  );
} 