'use client';
import React, { useState } from 'react';
import { TrashIcon, ShoppingBagIcon } from '@heroicons/react/24/outline';

export default function WishlistPage() {
  const [wishlistItems, setWishlistItems] = useState([
    {
      id: 1,
      name: '봄 신상품 컬렉션',
      price: '59,000',
    },
    {
      id: 2,
      name: '여름 특가 상품',
      price: '39,000',
    },
  ]);

  const removeFromWishlist = (id: number) => {
    setWishlistItems(wishlistItems.filter(item => item.id !== id));
  };

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-6">위시리스트</h1>

      {wishlistItems.length === 0 ? (
        <div className="text-center py-8">
          <p className="text-gray-600">위시리스트가 비어있습니다.</p>
        </div>
      ) : (
        <div className="grid grid-cols-2 gap-4">
          {wishlistItems.map((item) => (
            <div key={item.id} className="bg-white rounded-lg overflow-hidden shadow">
              <div className="aspect-square bg-gray-100" />
              <div className="p-4">
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