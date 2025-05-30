'use client';
import React, { useState, useEffect } from 'react';
import { HeartIcon } from '@heroicons/react/24/outline';
import { HeartIcon as HeartSolidIcon } from '@heroicons/react/24/solid';
import { ChevronLeftIcon, ShareIcon } from '@heroicons/react/24/outline';
import Link from 'next/link';
import { productsApi, type Product } from '@/lib/api';

export default function ProductPage({ params }: { params: { id: string } }) {
  const [isWishlist, setIsWishlist] = useState(false);
  const [selectedSize, setSelectedSize] = useState('');
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [product, setProduct] = useState<Product | null>(null);
  const [relatedProducts, setRelatedProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    loadProduct();
  }, [params.id]);

  const loadProduct = async () => {
    try {
      setLoading(true);
      setError('');

      const productId = parseInt(params.id);
      if (isNaN(productId)) {
        setError('잘못된 상품 ID입니다.');
        return;
      }

      // 상품 상세 정보 로딩
      const productResponse = await productsApi.getProduct(productId);
      if (productResponse.success && productResponse.data) {
        setProduct(productResponse.data);
        
        // 관련 상품 로딩
        const relatedResponse = await productsApi.getRelatedProducts(productId);
        if (relatedResponse.success && relatedResponse.data) {
          setRelatedProducts(relatedResponse.data.relatedProducts);
        }
      } else {
        setError(productResponse.message || '상품을 찾을 수 없습니다.');
      }
    } catch (err) {
      setError('상품 정보를 불러오는 중 오류가 발생했습니다.');
      console.error('상품 로딩 오류:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-white flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        <span className="ml-4">상품을 불러오는 중...</span>
      </div>
    );
  }

  if (error || !product) {
    return (
      <div className="min-h-screen bg-white flex items-center justify-center">
        <div className="text-center">
          <p className="text-red-600 mb-4">{error}</p>
          <Link href="/shop" className="text-blue-600 hover:underline">
            쇼핑 페이지로 돌아가기
          </Link>
        </div>
      </div>
    );
  }

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
          {product.images && product.images.length > 0 ? (
            <img 
              src={product.images[currentImageIndex]?.url || product.mainImage?.url} 
              alt={product.images[currentImageIndex]?.alt || product.name}
              className="w-full h-full object-cover"
            />
          ) : (
            <div className="w-full h-full flex items-center justify-center text-gray-400">
              No Image
            </div>
          )}
        </div>
        {product.images && product.images.length > 1 && (
          <div className="absolute bottom-4 left-0 right-0 flex justify-center space-x-2">
            {(product.images || []).map((_, index) => (
              <button
                key={index}
                className={`w-2 h-2 rounded-full ${
                  currentImageIndex === index ? 'bg-blue-600' : 'bg-gray-300'
                }`}
                onClick={() => setCurrentImageIndex(index)}
              />
            ))}
          </div>
        )}
      </div>

      {/* 상품 정보 */}
      <div className="p-4">
        <div className="space-y-4">
          <div>
            <p className="text-sm text-gray-500 mb-1">
              {product.category?.name || '미분류'}
            </p>
            <h1 className="text-2xl font-bold">{product.name}</h1>
            <div className="mt-2 flex items-center gap-2">
              <p className="text-xl font-semibold text-blue-600">
                ₩{(product.price || 0).toLocaleString()}
              </p>
              {product.originalPrice && (
                <p className="text-lg text-gray-500 line-through">
                  ₩{product.originalPrice.toLocaleString()}
                </p>
              )}
              {product.discount && (
                <span className="bg-red-500 text-white px-2 py-1 rounded text-sm">
                  -{product.discount.rate}%
                </span>
              )}
            </div>
            <div className="mt-2 flex gap-2">
              {product.featured && (
                <span className="bg-yellow-100 text-yellow-800 text-xs px-2 py-1 rounded">추천</span>
              )}
              {product.bestseller && (
                <span className="bg-green-100 text-green-800 text-xs px-2 py-1 rounded">베스트셀러</span>
              )}
              {product.newArrival && (
                <span className="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">신상품</span>
              )}
            </div>
          </div>

          <div>
            <p className="text-gray-600">{product.description}</p>
          </div>

          {/* 상품 정보 */}
          <div className="space-y-2 text-sm">
            <div className="flex justify-between">
              <span className="text-gray-600">브랜드:</span>
              <span>{product.brand}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-600">제조사:</span>
              <span>{product.manufacturer}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-600">원산지:</span>
              <span>{product.countryOfOrigin}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-600">재고:</span>
              <span className={`${(product.stockQuantity || 0) > 0 ? 'text-green-600' : 'text-red-600'}`}>
                {(product.stockQuantity || 0) > 0 ? `${product.stockQuantity || 0}개` : '품절'}
              </span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-600">배송비:</span>
              <span>
                {(product.shippingFee || 0) > 0 ? `₩${(product.shippingFee || 0).toLocaleString()}` : '무료'}
              </span>
            </div>
          </div>

          {/* 수량 선택 */}
          <div>
            <h2 className="text-lg font-semibold mb-3">수량</h2>
            <div className="flex items-center space-x-4">
              <button 
                className="w-10 h-10 border rounded-lg flex items-center justify-center"
                onClick={() => {
                  const current = parseInt(selectedSize) || 1;
                  if (current > (product.minimumOrderQuantity || 1)) {
                    setSelectedSize((current - 1).toString());
                  }
                }}
              >
                -
              </button>
              <input 
                type="number" 
                value={selectedSize || '1'} 
                onChange={(e) => setSelectedSize(e.target.value)}
                min={product.minimumOrderQuantity || 1}
                max={product.maximumOrderQuantity || 99}
                className="w-16 text-center border rounded-lg py-2"
              />
              <button 
                className="w-10 h-10 border rounded-lg flex items-center justify-center"
                onClick={() => {
                  const current = parseInt(selectedSize) || 1;
                  const max = product.maximumOrderQuantity || 99;
                  if (current < max) {
                    setSelectedSize((current + 1).toString());
                  }
                }}
              >
                +
              </button>
              <div className="text-sm text-gray-500">
                최소 {product.minimumOrderQuantity || 1}개 
                {product.maximumOrderQuantity && ` / 최대 ${product.maximumOrderQuantity}개`}
              </div>
            </div>
          </div>
        </div>

        {/* 관련 상품 추천 */}
        {relatedProducts && relatedProducts.length > 0 && (
          <div className="mt-8 border-t pt-8">
            <h2 className="text-lg font-semibold mb-4">
              {product.category?.name || '관련'} 카테고리 다른 상품
            </h2>
            <div className="grid grid-cols-2 gap-4">
              {(relatedProducts || []).map((relatedProduct) => (
                <Link key={relatedProduct.id} href={`/product/${relatedProduct.id}`}>
                  <div className="bg-white rounded-lg overflow-hidden border hover:shadow-md transition-shadow">
                    <div className="aspect-square bg-gray-100">
                      {relatedProduct.mainImage ? (
                        <img 
                          src={relatedProduct.mainImage.url} 
                          alt={relatedProduct.mainImage.alt}
                          className="w-full h-full object-cover"
                        />
                      ) : (
                        <div className="w-full h-full flex items-center justify-center text-gray-400">
                          No Image
                        </div>
                      )}
                    </div>
                    <div className="p-3">
                      <h3 className="font-medium text-sm line-clamp-2">{relatedProduct.name}</h3>
                      <p className="text-sm text-gray-600 mt-1">
                        ₩{(relatedProduct.price || 0).toLocaleString()}
                      </p>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          </div>
        )}

        {/* 장바구니 버튼 */}
        <div className="left-0 right-0 p-4 bg-white border-t border-gray-200 mt-8">
          <div className="max-w-md mx-auto">
            <button
              className={`w-full py-3 rounded-lg font-semibold transition-colors ${
                (product.stockQuantity || 0) > 0
                  ? 'bg-blue-600 text-white hover:bg-blue-700'
                  : 'bg-gray-300 text-gray-500 cursor-not-allowed'
              }`}
              disabled={(product.stockQuantity || 0) === 0}
            >
              {(product.stockQuantity || 0) > 0 ? '장바구니에 담기' : '품절'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
