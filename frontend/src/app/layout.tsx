import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import ClientNavWrapper from "@/components/ClientNavWrapper";

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
  // 라우트 → 탭 key 매핑
  const pathToTab = (path: string) => {
    if (path.startsWith("/shop")) return "home";
    if (path.startsWith("/account")) return "user";
    if (path.startsWith("/message")) return "message";
    if (path.startsWith("/camera")) return "camera";
    if (path.startsWith("/settings")) return "settings";
    return "home";
  };


  return (
    <html lang="ko">
      <body className={inter.className}>
        <div className="max-w-md mx-auto min-h-screen bg-white">
          <main className="pb-20">{children}</main>
          <ClientNavWrapper />
        </div>
      </body>
    </html>
  );
} 