'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import { HeartIcon, ShoppingBagIcon, CreditCardIcon, PlusIcon, MinusIcon } from '@heroicons/react/24/outline';
import { HeartIcon as HeartSolidIcon } from '@heroicons/react/24/solid';
import { useFavorites } from '@/hooks/useFavorites';
import { useCart } from '@/hooks/useCart';
import { type Product } from '@/lib/api';

interface ProductCardProps {
  product: Product;
}

export default function ProductCard({ product }: ProductCardProps) {
  const [quantity, setQuantity] = useState(1);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [successMessage, setSuccessMessage] = useState<string>('');
  const { isFavorite, toggleFavorite, isLoading: favoritesLoading, error: favoritesError } = useFavorites();
  const { addToCart, isLoading: cartLoading, error: cartError } = useCart();

  const handleAddToCart = async () => {
    setIsSubmitting(true);
    try {
      await addToCart(product, quantity);
      setSuccessMessage('장바구니에 추가되었습니다!');
      setTimeout(() => setSuccessMessage(''), 3000);
    } catch (error) {
      console.error('장바구니 추가 실패:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleBuyNow = async () => {
    setIsSubmitting(true);
    try {
      await addToCart(product, quantity);
      window.location.href = '/bag';
    } catch (error) {
      console.error('구매하기 실패:', error);
      setIsSubmitting(false);
    }
  };

  const handleToggleFavorite = async () => {
    try {
      await toggleFavorite(product.id);
    } catch (error) {
      console.error('즐겨찾기 토글 실패:', error);
    }
  };

  const incrementQuantity = () => {
    if (quantity < (product.stockQuantity || 1)) {
      setQuantity(quantity + 1);
    }
  };

  const decrementQuantity = () => {
    if (quantity > 1) {
      setQuantity(quantity - 1);
    }
  };

  const handleQuantityChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = parseInt(e.target.value);
    if (!isNaN(value) && value >= 1 && value <= (product.stockQuantity || 1)) {
      setQuantity(value);
    }
  };

  return (
    <div className="bg-white rounded-lg overflow-hidden shadow-md hover:shadow-lg transition-shadow relative">
      <Link href={`/product/${product.id}`} className="block relative">
        <div className="aspect-square bg-gray-100 relative">
          {product.mainImage ? (
            <img 
              src={product.mainImage.url} 
              alt={product.mainImage.alt}
              className="w-full h-full object-cover"
            />
          ) : (
            <div className="w-full h-full flex items-center justify-center text-gray-400">
              No Image
            </div>
          )}
          
          {/* 할인 배지 */}
          {product.discount && (
            <div className="absolute top-2 left-2 bg-red-500 text-white px-2 py-1 rounded text-sm">
              -{product.discount.rate}%
            </div>
          )}

          {/* 상품 라벨들 - 이미지 좌하단 */}
          <div className="absolute bottom-2 left-2 flex gap-1">
            {product.featured && (
              <span className="bg-yellow-100 text-yellow-800 text-xs px-2 py-1 rounded">
                추천
              </span>
            )}
            {product.bestseller && (
              <span className="bg-green-100 text-green-800 text-xs px-2 py-1 rounded">
                베스트
              </span>
            )}
            {product.newArrival && (
              <span className="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">
                신상품
              </span>
            )}
          </div>

          {/* 즐겨찾기 버튼 - 각 상품 이미지의 우상단 */}
          <button
            onClick={(e) => {
              e.preventDefault();
              e.stopPropagation();
              handleToggleFavorite();
            }}
            disabled={favoritesLoading}
            className="absolute top-2 right-2 p-2 bg-white/80 rounded-full hover:bg-white transition-colors disabled:opacity-50 z-10"
          >
            {isFavorite(product.id) ? (
              <HeartSolidIcon className="w-5 h-5 text-red-500" />
            ) : (
              <HeartIcon className="w-5 h-5 text-gray-600" />
            )}
          </button>
        </div>
      </Link>
      
      <div className="p-3">
        <p className="text-xs text-gray-500 mb-1">{product.category?.name || '미분류'}</p>
        <Link href={`/product/${product.id}`} className="relative z-10">
          <h3 className="font-medium mb-2 line-clamp-2 hover:text-blue-600 transition-colors">
            {product.name}
          </h3>
        </Link>
        
        {/* 가격 정보 */}
        <div className="mb-3">
          <div className="mb-1">
            <p className="text-lg font-semibold text-blue-600">
              ₩{(product.price || 0).toLocaleString()}
            </p>
            {product.originalPrice && (
              <p className="text-sm text-gray-500 line-through">
                ₩{product.originalPrice.toLocaleString()}
              </p>
            )}
          </div>
          <p className="text-xs text-gray-500">재고: {product.stockQuantity}개</p>
        </div>
        
        {/* 수량 선택 */}
        <div className="flex items-center justify-between mb-3 relative z-10">
          <span className="text-sm font-medium">수량</span>
          <div className="flex items-center border rounded-md">
            <button
              onClick={decrementQuantity}
              disabled={quantity <= 1}
              className="w-8 h-8 flex items-center justify-center hover:bg-gray-100 disabled:opacity-50"
            >
              <MinusIcon className="w-4 h-4" />
            </button>
            <input
              type="number"
              value={quantity}
              onChange={handleQuantityChange}
              min="1"
              max={product.stockQuantity || 1}
              className="w-12 text-center text-sm border-0 focus:outline-none"
            />
            <button
              onClick={incrementQuantity}
              disabled={quantity >= (product.stockQuantity || 1)}
              className="w-8 h-8 flex items-center justify-center hover:bg-gray-100 disabled:opacity-50"
            >
              <PlusIcon className="w-4 h-4" />
            </button>
          </div>
        </div>
        
        {/* 성공 메시지 */}
        {successMessage && (
          <div className="mb-2 p-2 bg-green-50 border border-green-200 rounded text-center">
            <p className="text-green-700 text-xs">{successMessage}</p>
          </div>
        )}

        {/* 에러 메시지 */}
        {(cartError || favoritesError) && (
          <div className="mb-2 p-2 bg-red-50 border border-red-200 rounded text-center">
            <p className="text-red-600 text-xs">{cartError || favoritesError}</p>
          </div>
        )}
        
        {/* 버튼 영역 */}
        <div className="space-y-2 relative z-10">
          <button
            onClick={handleAddToCart}
            disabled={product.stockQuantity === 0 || isSubmitting || cartLoading}
            className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed text-sm"
          >
            <ShoppingBagIcon className="w-4 h-4 mr-1" />
            {isSubmitting || cartLoading ? '처리중...' : '장바구니 담기'}
          </button>
          
          <button
            onClick={handleBuyNow}
            disabled={product.stockQuantity === 0 || isSubmitting || cartLoading}
            className="w-full bg-gray-800 text-white py-2 rounded-md hover:bg-gray-900 flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed text-sm"
          >
            <CreditCardIcon className="w-4 h-4 mr-1" />
            {isSubmitting || cartLoading ? '처리중...' : '즉시 구매'}
          </button>
        </div>

        {/* 재고 부족 시 메시지 */}
        {product.stockQuantity === 0 && (
          <div className="mt-2 p-2 bg-gray-50 border border-gray-200 rounded text-center">
            <p className="text-gray-600 text-xs">품절된 상품입니다</p>
          </div>
        )}
      </div>
    </div>
  );
}
