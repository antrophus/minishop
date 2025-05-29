'use client';

import { usePathname } from 'next/navigation';
import ClientNavWrapper from './ClientNavWrapper';

export default function ConditionalNavWrapper({
  children
}: {
  children: React.ReactNode;
}) {
  const pathname = usePathname();
  
  // bottombar를 표시하지 않을 페이지들
  const hideNavPages = [
    '/onboarding',
    '/account/signin',
    '/account/signup',
    '/account/forgot-password',
    '/welcome'
  ];
  
  const shouldHideNav = hideNavPages.some(page => pathname?.startsWith(page));

  if (shouldHideNav) {
    return (
      <main className="min-h-screen">
        {children}
      </main>
    );
  }

  return (
    <>
      <main className="pb-20">
        {children}
      </main>
      <ClientNavWrapper />
    </>
  );
}
