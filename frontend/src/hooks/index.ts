/**
 * Custom Hooks Export
 * 
 * 모든 커스텀 훅들을 중앙에서 관리하고 내보냅니다.
 */

// 장바구니 훅
export { useCart } from './useCart';
export type { CartItem, UseCartReturn } from './useCart';

// 즐겨찾기 훅
export { useFavorites } from './useFavorites';
export type { FavoriteItem, UseFavoritesReturn } from './useFavorites';

// 기타 훅들이 있다면 여기에 추가
// export { useAuth } from './useAuth';
// export { useProducts } from './useProducts';
