'use client';

import React, { useState, useEffect } from 'react';
import Link from 'next/link';
import { useSearchParams } from 'next/navigation';
import { SearchBar, CategoryTabs } from '@/components/categories';
import { CategoryUtils } from '@/lib/categories';
import { ChevronLeftIcon } from '@heroicons/react/24/outline';

export default function SearchPage() {
  const searchParams = useSearchParams();
  const initialQuery = searchParams.get('q') || '';
  const initialCategory = searchParams.get('category') || 'all';
  
  const [searchQuery, setSearchQuery] = useState(initialQuery);
  const [selectedCategory, setSelectedCategory] = useState(initialCategory);
  const [isLoading, setIsLoading] = useState(false);

  // 더미 상품 데이터
  const allProducts = [
    {
      id: 1,
      name: '하이드레이팅 토너',
      price: '35,000',
      categoryId: 'skincare',
    },
    {
      id: 2,
      name: '모이스처라이저 크림',
      price: '59,000',
      categoryId: 'skincare',
    },
    {
      id: 3,
      name: '헤어 에센스',
      price: '39,000',
      categoryId: 'haircare',
    },
    {
      id: 4,
      name: '샴푸',
      price: '25,000',
      categoryId: 'haircare',
    },
    {
      id: 5,
      name: '비타민 C',
      price: '45,000',
      categoryId: 'health',
    },
    {
      id: 6,
      name: '프로바이오틱스',
      price: '79,000',
      categoryId: 'health',
    },
    {
      id: 7,
      name: '아로마 디퓨저',
      price: '89,000',
      categoryId: 'lifestyle',
    },
    {
      id: 8,
      name: '캔들',
      price: '29,000',
      categoryId: 'lifestyle',
    },
  ];  // 검색 및 카테고리 필터링
  const filteredProducts = allProducts.filter(product => {
    const matchesCategory = selectedCategory === 'all' || product.categoryId === selectedCategory;
    const matchesSearch = searchQuery === '' || product.name.toLowerCase().includes(searchQuery.toLowerCase());
    return matchesCategory && matchesSearch;
  });

  const handleSearch = (query: string, category: string) => {
    setIsLoading(true);
    setSearchQuery(query);
    setSelectedCategory(category);
    
    // 실제 API 호출을 시뮬레이션
    setTimeout(() => {
      setIsLoading(false);
    }, 500);
  };

  return (
    <div className="min-h-screen bg-white">
      {/* 헤더 */}
      <div className="sticky top-0 bg-white border-b border-gray-200 z-10">
        <div className="flex items-center p-4">
          <Link href="/shop" className="mr-4">
            <ChevronLeftIcon className="w-6 h-6" />
          </Link>
          <h1 className="text-xl font-bold">검색</h1>
        </div>
      </div>

      {/* 검색 바 */}
      <SearchBar
        onSearch={handleSearch}
        placeholder="상품명을 입력하세요"
        showCategoryFilter={true}
      />

      {/* 검색 결과 */}
      <div className="p-4">
        {searchQuery && (
          <div className="mb-4">
            <p className="text-sm text-gray-600">
              '{searchQuery}' 검색 결과 • {filteredProducts.length}개 상품
            </p>
          </div>
        )}

        {isLoading ? (
          <div className="text-center py-12">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
            <p className="mt-4 text-gray-600">검색 중...</p>
          </div>
        ) : filteredProducts.length === 0 ? (
          <div className="text-center py-12">
            <div className="text-4xl mb-4">🔍</div>
            <h3 className="text-lg font-medium text-gray-800 mb-2">검색 결과가 없습니다</h3>
            <p className="text-gray-600">다른 키워드로 검색해보세요.</p>
          </div>
        ) : (
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
            {(filteredProducts || []).map((product) => (
              <Link key={product.id} href={`/product/${product.id}`}>
                <div className="bg-white rounded-lg overflow-hidden border hover:shadow-md transition-shadow">
                  <div className="aspect-square bg-gray-100" />
                  <div className="p-3">
                    <p className="text-xs text-gray-500 mb-1">
                      {CategoryUtils.getCategoryName(product.categoryId)}
                    </p>
                    <h3 className="font-medium text-sm line-clamp-2">{product.name}</h3>
                    <p className="text-sm font-semibold mt-2">₩{product.price}</p>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}