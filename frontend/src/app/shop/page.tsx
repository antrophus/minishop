'use client';

import React from 'react';
import Link from 'next/link';

export default function ShopPage() {
  const products = [
    {
      id: 1,
      name: '봄 신상품 컬렉션',
      price: '59,000',
      category: '여성복',
    },
    {
      id: 2,
      name: '여름 특가 상품',
      price: '39,000',
      category: '남성복',
    },
    {
      id: 3,
      name: '가을 트렌치코트',
      price: '129,000',
      category: '여성복',
    },
    {
      id: 4,
      name: '겨울 니트 컬렉션',
      price: '49,000',
      category: '남성복',
    },
  ];

  return (
    <div className="min-h-screen bg-white">
      {/* 검색 바 */}
      <div className="sticky top-0 bg-white p-4 border-b border-gray-200">
        <div className="relative">
          <input
            type="search"
            placeholder="상품 검색"
            className="w-full px-4 py-2 bg-gray-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
      </div>

      {/* 카테고리 필터 */}
      <div className="px-4 py-3 overflow-x-auto scrollbar-hide">
        <div className="flex space-x-2">
          {['전체', '여성복', '남성복', '액세서리', '신발'].map((category) => (
            <button
              key={category}
              className="px-4 py-1 text-sm rounded-full border border-gray-300 whitespace-nowrap hover:bg-gray-100"
            >
              {category}
            </button>
          ))}
        </div>
      </div>

      {/* 상품 목록 */}
      <div className="grid grid-cols-2 gap-4 p-4">
        {products.map((product) => (
          <Link key={product.id} href={`/product/${product.id}`}>
            <div className="bg-white rounded-lg overflow-hidden">
              <div className="aspect-square bg-gray-100" />
              <div className="p-3">
                <p className="text-xs text-gray-500">{product.category}</p>
                <h3 className="font-medium mt-1 line-clamp-2">{product.name}</h3>
                <p className="text-sm font-semibold mt-1">₩{product.price}</p>
              </div>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
} 