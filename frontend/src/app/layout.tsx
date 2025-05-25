import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Navigation from "@/components/Navigation";

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
          <main className="pb-20">{children}</main>
          <Navigation />
        </div>
      </body>
    </html>
  );
} 