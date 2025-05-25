'use client';
import React from 'react';
import Image from 'next/image';
import Link from 'next/link';

export default function Home() {
  const featuredProducts = [
    {
      id: 1,
      name: '봄 신상품 컬렉션',
      image: '/images/product1.jpg',
      price: '59,000',
    },
    {
      id: 2,
      name: '여름 특가 상품',
      image: '/images/product2.jpg',
      price: '39,000',
    },
    // 더미 데이터
  ];

  return (
    <div className="p-4">
      {/* 온보딩 배너 */}
      <div className="relative h-48 rounded-lg bg-gray-100 mb-6">
        <div className="absolute inset-0 flex items-center justify-center">
          <h1 className="text-2xl font-bold text-gray-800">신규 가입 특별 할인</h1>
        </div>
      </div>

      {/* 카테고리 섹션 */}
      <section className="mb-8">
        <h2 className="text-xl font-semibold mb-4">카테고리</h2>
        <div className="grid grid-cols-4 gap-4">
          {['여성복', '남성복', '액세서리', '신발'].map((category) => (
            <Link
              key={category}
              href={`/shop?category=${category}`}
              className="flex flex-col items-center"
            >
              <div className="w-16 h-16 rounded-full bg-gray-200 mb-2" />
              <span className="text-sm">{category}</span>
            </Link>
          ))}
        </div>
      </section>

      {/* 추천 상품 섹션 */}
      <section>
        <h2 className="text-xl font-semibold mb-4">추천 상품</h2>
        <div className="grid grid-cols-2 gap-4">
          {featuredProducts.map((product) => (
            <Link key={product.id} href={`/product/${product.id}`}>
              <div className="rounded-lg overflow-hidden">
                <div className="aspect-square relative bg-gray-200">
                  {/* 이미지가 있을 경우에만 표시 */}
                  {/* <Image
                    src={product.image}
                    alt={product.name}
                    fill
                    className="object-cover"
                  /> */}
                </div>
                <div className="mt-2">
                  <h3 className="font-medium">{product.name}</h3>
                  <p className="text-sm text-gray-600">₩{product.price}</p>
                </div>
              </div>
            </Link>
          ))}
        </div>
      </section>
    </div>
  );
} 