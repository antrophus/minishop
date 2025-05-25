'use client';
import React, { useState } from 'react';
import { HeartIcon } from '@heroicons/react/24/outline';
import { HeartIcon as HeartSolidIcon } from '@heroicons/react/24/solid';
import { ChevronLeftIcon, ShareIcon } from '@heroicons/react/24/outline';
import Link from 'next/link';

export default function ProductPage({ params }: { params: { id: string } }) {
  const [isWishlist, setIsWishlist] = useState(false);
  const [selectedSize, setSelectedSize] = useState('');
  const [currentImageIndex, setCurrentImageIndex] = useState(0);

  const product = {
    name: '봄 신상품 컬렉션',
    price: '59,000',
    description: '봄을 맞이하는 새로운 스타일의 컬렉션입니다. 가볍고 편안한 착용감으로 일상적인 스타일링이 가능합니다.',
    sizes: ['S', 'M', 'L', 'XL'],
    images: ['/images/product1.jpg', '/images/product2.jpg', '/images/product3.jpg'],
    colors: ['#000000', '#FFFFFF', '#C4C4C4'],
  };

  return (
    <div className="min-h-screen bg-white">
      {/* 상단 네비게이션 */}
      <div className="fixed top-0 left-0 right-0 z-50 bg-white">
        <div className="flex items-center justify-between p-4 max-w-md mx-auto">
          <Link href="/shop" className="p-2">
            <ChevronLeftIcon className="w-6 h-6" />
          </Link>
          <div className="flex items-center space-x-4">
            <button className="p-2">
              <ShareIcon className="w-6 h-6" />
            </button>
            <button
              onClick={() => setIsWishlist(!isWishlist)}
              className="p-2"
            >
              {isWishlist ? (
                <HeartSolidIcon className="w-6 h-6 text-red-500" />
              ) : (
                <HeartIcon className="w-6 h-6" />
              )}
            </button>
          </div>
        </div>
      </div>

      {/* 상품 이미지 슬라이더 */}
      <div className="relative pt-16">
        <div className="aspect-square bg-gray-100">
          {/* 이미지가 있을 경우에만 표시 */}
        </div>
        <div className="absolute bottom-4 left-0 right-0 flex justify-center space-x-2">
          {product.images.map((_, index) => (
            <button
              key={index}
              className={`w-2 h-2 rounded-full ${
                currentImageIndex === index ? 'bg-blue-600' : 'bg-gray-300'
              }`}
              onClick={() => setCurrentImageIndex(index)}
            />
          ))}
        </div>
      </div>

      {/* 상품 정보 */}
      <div className="p-4">
        <div className="space-y-4">
          <div>
            <h1 className="text-2xl font-bold">{product.name}</h1>
            <p className="text-xl font-semibold mt-2">₩{product.price}</p>
          </div>

          <div>
            <p className="text-gray-600">{product.description}</p>
          </div>

          {/* 색상 선택 */}
          <div>
            <h2 className="text-lg font-semibold mb-3">색상</h2>
            <div className="flex space-x-3">
              {product.colors.map((color, index) => (
                <button
                  key={index}
                  className={`w-8 h-8 rounded-full border-2 ${
                    index === 0 ? 'border-blue-500' : 'border-transparent'
                  }`}
                  style={{ backgroundColor: color }}
                />
              ))}
            </div>
          </div>

          {/* 사이즈 선택 */}
          <div>
            <h2 className="text-lg font-semibold mb-3">사이즈</h2>
            <div className="grid grid-cols-4 gap-2">
              {product.sizes.map((size) => (
                <button
                  key={size}
                  onClick={() => setSelectedSize(size)}
                  className={`py-3 border rounded-lg ${
                    selectedSize === size
                      ? 'border-blue-500 bg-blue-50 text-blue-600'
                      : 'border-gray-300'
                  }`}
                >
                  {size}
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* 장바구니 버튼 */}
        <div className="left-0 right-0 p-4 bg-white border-t border-gray-200">
          <div className="max-w-md mx-auto">
            <button
              className="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700"
            >
              장바구니에 담기
            </button>
          </div>
        </div>
      </div>
    </div>
  );
} 