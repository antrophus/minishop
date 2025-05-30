'use client';

import React from 'react';
import { CategoryUtils } from '@/lib/categories/utils';
import { Category } from '@/lib/categories/types';

interface CategoryTabsProps {
  selectedCategory: string;
  onCategoryChange: (categoryId: string) => void;
  className?: string;
}

export function CategoryTabs({ selectedCategory, onCategoryChange, className = '' }: CategoryTabsProps) {
  const categories = CategoryUtils.getAllCategories();

  return (
    <div className={`flex space-x-1 overflow-x-auto pb-2 ${className}`}>
      {categories.map((category: Category) => (
        <button
          key={category.id}
          onClick={() => onCategoryChange(category.id)}
          className={`flex-shrink-0 px-4 py-2 rounded-2xl text-sm font-medium transition-colors ${
            selectedCategory === category.id
              ? 'bg-black text-white'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          }`}
        >
          <span className="mr-1">{category.icon}</span>
          {category.name}
        </button>
      ))}
    </div>
  );
}

interface CategorySelectProps {
  selectedCategory: string;
  onCategoryChange: (categoryId: string) => void;
  className?: string;
  includeAll?: boolean;
}

export function CategorySelect({ 
  selectedCategory, 
  onCategoryChange, 
  className = '',
  includeAll = true 
}: CategorySelectProps) {
  const options = CategoryUtils.getCategoryOptions();
  const filteredOptions = includeAll ? options : options.filter(opt => opt.value !== 'all');

  return (
    <select
      value={selectedCategory}
      onChange={(e) => onCategoryChange(e.target.value)}
      className={`px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-black focus:border-transparent ${className}`}
    >
      {filteredOptions.map(option => (
        <option key={option.value} value={option.value}>
          {option.label}
        </option>
      ))}
    </select>
  );
}
