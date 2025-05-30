'use client';

import React, { useState } from 'react';
import { MagnifyingGlassIcon } from '@heroicons/react/24/outline';
import { CategorySelect } from './CategoryTabs';

interface SearchBarProps {
  onSearch: (query: string, category: string) => void;
  placeholder?: string;
  showCategoryFilter?: boolean;
  className?: string;
}

export function SearchBar({ 
  onSearch, 
  placeholder = "상품 검색", 
  showCategoryFilter = true,
  className = "" 
}: SearchBarProps) {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('all');

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    onSearch(searchQuery, selectedCategory);
  };

  const handleCategoryChange = (categoryId: string) => {
    setSelectedCategory(categoryId);
    onSearch(searchQuery, categoryId);
  };

  return (
    <div className={`bg-white border-b border-gray-200 ${className}`}>
      <div className="p-4">
        {/* 검색 입력 */}
        <form onSubmit={handleSearch} className="relative mb-3">
          <input
            type="search"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder={placeholder}
            className="w-full px-4 py-3 pl-12 bg-gray-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white"
          />
          <MagnifyingGlassIcon className="absolute left-4 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
          <button 
            type="submit"
            className="absolute right-3 top-1/2 transform -translate-y-1/2 px-3 py-1 bg-blue-600 text-white text-sm rounded-md hover:bg-blue-700"
          >
            검색
          </button>
        </form>        {/* 카테고리 필터 */}
        {showCategoryFilter && (
          <CategorySelect
            selectedCategory={selectedCategory}
            onCategoryChange={handleCategoryChange}
            className="w-full"
          />
        )}
      </div>
    </div>
  );
}