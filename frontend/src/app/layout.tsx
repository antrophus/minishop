import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import ConditionalNavWrapper from "@/components/ConditionalNavWrapper";
import { ApiConfigDebugger } from "@/components/ApiConfigDebugger";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "모바일 쇼핑몰",
  description: "React와 TypeScript로 만든 모바일 쇼핑몰",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body className={inter.className}>
        <div className="max-w-md mx-auto min-h-screen bg-white">
          <ConditionalNavWrapper>
            {children}
          </ConditionalNavWrapper>
        </div>
        {/* 개발 환경에서만 표시되는 API 설정 디버거 */}
        <ApiConfigDebugger />
      </body>
    </html>
  );
} 