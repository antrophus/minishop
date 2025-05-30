'use client';
import React, { useState } from 'react';
import { TrashIcon, ShoppingBagIcon } from '@heroicons/react/24/outline';
import { CategoryTabs } from '@/components/categories';
import { CategoryUtils } from '@/lib/categories';

export default function WishlistPage() {
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [wishlistItems, setWishlistItems] = useState([
    {
      id: 1,
      name: '모이스처라이저 크림',
      price: '59,000',
      categoryId: 'skincare',
    },
    {
      id: 2,
      name: '헤어 에센스',
      price: '39,000',
      categoryId: 'haircare',
    },
    {
      id: 3,
      name: '비타민 C 세럼',
      price: '79,000',
      categoryId: 'skincare',
    },
    {
      id: 4,
      name: '아로마 디퓨저',
      price: '49,000',
      categoryId: 'lifestyle',
    },
  ]);

  // 선택된 카테고리에 따라 위시리스트 필터링
  const filteredItems = selectedCategory === 'all' 
    ? wishlistItems 
    : wishlistItems.filter(item => item.categoryId === selectedCategory);

  const removeFromWishlist = (id: number) => {
    setWishlistItems(wishlistItems.filter(item => item.id !== id));
  };

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-6">위시리스트</h1>

      {/* 카테고리 필터 */}
      <CategoryTabs
        selectedCategory={selectedCategory}
        onCategoryChange={setSelectedCategory}
        className="mb-6"
      />

      {filteredItems.length === 0 ? (
        <div className="text-center py-8">
          <p className="text-gray-600">
            {selectedCategory === 'all' 
              ? '위시리스트가 비어있습니다.' 
              : `${CategoryUtils.getCategoryName(selectedCategory)} 카테고리에 저장된 상품이 없습니다.`
            }
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-2 gap-4">
          {filteredItems.map((item) => (
            <div key={item.id} className="bg-white rounded-lg overflow-hidden shadow">
              <div className="aspect-square bg-gray-100" />
              <div className="p-4">
                <p className="text-xs text-gray-500 mb-1">
                  {CategoryUtils.getCategoryName(item.categoryId)}
                </p>
                <h3 className="font-medium">{item.name}</h3>
                <p className="text-sm text-gray-600 mt-1">₩{item.price}</p>
                <div className="mt-4 flex space-x-2">
                  <button
                    className="flex-1 bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 flex items-center justify-center"
                  >
                    <ShoppingBagIcon className="w-5 h-5 mr-1" />
                    <span>담기</span>
                  </button>
                  <button
                    onClick={() => removeFromWishlist(item.id)}
                    className="p-2 text-red-500 border border-red-500 rounded-md hover:bg-red-50"
                  >
                    <TrashIcon className="w-5 h-5" />
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
} 