# Frontend 작업 계획 및 진행 상황 (2025-05-18 기준 최신화)

## 실제 프로젝트 구조

```
src/
  app/
    layout.tsx         # 전체 레이아웃 및 Navigation 포함
    page.tsx           # 메인(홈) 페이지
    shop/
      page.tsx         # 상품 목록/검색/필터 페이지
    product/
      [id]/
        page.tsx       # 상품 상세 페이지 (동적 라우트)
    bag/
      page.tsx         # 장바구니 페이지
    wishlist/
      page.tsx         # 위시리스트 페이지
    account/
      page.tsx         # 계정 메인
      signin/
        page.tsx       # 로그인
    onboarding/
      page.tsx         # 온보딩
  components/
    Navigation.tsx     # 하단 네비게이션 바(모바일)
  (추후 store/, hooks/, services/ 등 추가 예정)
```

- **Next.js App Router 구조**를 사용하며, 각 주요 도메인별 폴더가 app/ 하위에 존재
- **아토믹 디자인**은 components/ 폴더에서 Atoms/Molecules/Organisms 등으로 확장 가능(현재 Navigation.tsx만 존재)
- **상태관리(Zustand 등)**, **공통 훅(hooks)**, **서비스 로직** 등은 추후 src/ 하위에 별도 폴더로 분리 예정

## 사용 주요 라이브러리 및 버전
- next: ^14.2.29
- react: ^18.2.0
- tailwindcss: ^3.4.1
- @heroicons/react, framer-motion 등

## 주요 UI/UX 구조 및 예시

- **layout.tsx**: 모바일 최대폭 레이아웃, 하단 Navigation 고정
- **Navigation.tsx**: 홈/쇼핑/위시리스트/장바구니/계정 등 주요 메뉴 아이콘 네비게이션
- **page.tsx(홈)**: 온보딩 배너, 카테고리, 추천상품 등 섹션화
- **shop/page.tsx**: 검색바, 카테고리 필터, 상품 목록 그리드
- **product/[id]/page.tsx**: 이미지 슬라이더, 색상/사이즈 선택, 장바구니 버튼 등 상세 UI
- **bag/page.tsx**: 장바구니 목록, 수량 조절, 총 결제금액 계산
- **wishlist/page.tsx**: 위시리스트 목록, 장바구니 담기/삭제

## 아토믹 디자인 적용 예시(확장 가이드)
- Atoms: Button, Input, Icon 등 (components/atoms/)
- Molecules: SearchBar, ProductCard 등 (components/molecules/)
- Organisms: ProductList, CartList 등 (components/organisms/)
- Templates: MainLayout 등 (components/templates/)
- Pages: app/ 하위 각 page.tsx

## 스타일링 가이드
- TailwindCSS 유틸리티 우선, 필요시 @apply로 공통 추출
- 반응형 중단점: sm(640px), md(768px), lg(1024px), xl(1280px), 2xl(1536px)
- 디자인 토큰: tailwind.config.js의 theme.extend.colors 등에서 관리(현재 커스텀 색상 미정)

## 작업 표준 단계 (Task Master Subtask)
1. **목표 이해 및 준비**: get_task로 목표/요구사항 확인, UI/UX 요구 파악
2. **초기 탐색 및 계획 수립**: 실제 코드 구조/컴포넌트/상태관리/디자인 시스템 파악
3. **계획 기록**: update_subtask로 구조/상태관리/스타일링 전략 기록
4. **계획 검증**: get_task로 계획 반영 여부 및 UI/UX 적합성 검토
5. **구현 시작**: set_task_status로 'in-progress', 실제 코드 작업
6. **진행상황/교훈 기록**: update_subtask로 진행상황, 이슈, 코드 스니펫 등 기록
7. **작업 완료 및 문서 갱신**: 코드/문서/규칙 리뷰 및 필요시 갱신
8. **작업 완료 처리**: set_task_status로 'done', 최종 UI/크로스 브라우저 테스트
9. **커밋 및 다음 작업 진행**: git 커밋, 다음 subtask 이동

## 현재까지의 작업 내역
- **2025-05-03**: 환경설정(Next.js + React + TailwindCSS + TypeScript) 및 기본 구조 완료
- **다음 예정**: 라우팅/네비게이션(Task #8), 상태관리(Zustand, Task #9), 상품 목록(Task #15), 상품 상세(Task #16) 등

## 발견된 이슈 및 해결책
- **Next.js API 연동 시 CORS**: 백엔드 연동 시 next.config.js 또는 프록시 설정 필요
- **TailwindCSS와 타사 컴포넌트 충돌**: 필요시 래퍼 컴포넌트로 스타일 오버라이드

## 추가 개발 참고사항
- 컴포넌트 재사용성, 명확한 Props, 비즈니스/프레젠테이션 분리, 접근성(ARIA, 키보드 네비, 색상 대비 등) 항상 고려
- 성능: React.memo, useMemo, useCallback, 코드 스플리팅(React.lazy, Suspense, 라우트 기반 분할)
- 이미지: WebP, srcset, 지연로딩 등

---

> 본 문서는 실제 코드베이스 구조와 작업 흐름에 맞게 최신화되었습니다. 추가 폴더(store, hooks 등) 생성 시 본 문서도 함께 갱신 바랍니다. 