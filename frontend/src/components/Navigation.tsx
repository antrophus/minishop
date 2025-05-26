'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { HomeIcon, ShoppingBagIcon, HeartIcon, UserIcon, ShoppingCartIcon } from '@heroicons/react/24/outline';

export default function Navigation() {
  const pathname = usePathname();

  const navItems = [
    { href: '/', icon: HomeIcon, label: '홈' },
    { href: '/shop', icon: ShoppingCartIcon, label: '쇼핑' },
    { href: '/wishlist', icon: HeartIcon, label: '위시리스트' },
    { href: '/bag', icon: ShoppingBagIcon, label: '장바구니' },
    { href: '/account', icon: UserIcon, label: '계정' },
  ];

  return (
    <nav className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200">
      <div className="max-w-md mx-auto px-4">
        <div className="flex justify-between py-2">
          {navItems.map((item) => {
            const Icon = item.icon;
            const isActive = pathname === item.href;
            return (
              <Link
                key={item.href}
                href={item.href}
                className={`flex flex-col items-center p-2 ${
                  isActive ? 'text-blue-600' : 'text-gray-600'
                }`}
              >
                <Icon className="w-6 h-6" />
                <span className="text-xs mt-1">{item.label}</span>
              </Link>
            );
          })}
        </div>
      </div>
    </nav>
  );
} 