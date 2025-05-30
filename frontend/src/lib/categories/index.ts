// 카테고리 모듈 메인 export
export * from './types';
export * from './utils';

// 편의성을 위한 re-export
export { CategoryUtils } from './utils';
export { CATEGORIES, CATEGORY_IDS, CATEGORY_NAMES } from './types';
export type { Category, Product } from './types';
