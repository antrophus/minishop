'use client';
import BottomNavBar from "@/components/BottomNavBar";
import { usePathname, useRouter } from "next/navigation";

export default function ClientNavWrapper() {
  const pathname = usePathname();
  const router = useRouter();

  const pathToTab = (path: string) => {
    if (path === "/" || path.startsWith("/home")) return "home";
    if (path.startsWith("/shop")) return "shop";
    if (path.startsWith("/wishlist")) return "wishlist";
    if (path.startsWith("/bag")) return "bag";
    if (path.startsWith("/account")) return "account";
    return "home";
  };

  const selected = pathToTab(pathname);

  return (
    <BottomNavBar
      selected={selected}
      onSelect={(key) => {
        if (key === "home") router.push("/");
        else if (key === "shop") router.push("/shop");
        else if (key === "wishlist") router.push("/wishlist");
        else if (key === "bag") router.push("/bag");
        else if (key === "account") router.push("/account");
      }}
    />
  );
} 