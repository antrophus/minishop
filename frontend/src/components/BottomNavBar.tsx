import { HomeIcon, ShoppingCartIcon, HeartIcon, ShoppingBagIcon, UserIcon } from '@heroicons/react/24/solid';

const TABS = [
  { key: 'home', icon: HomeIcon, label: '홈' },
  { key: 'shop', icon: ShoppingCartIcon, label: '쇼핑' },
  { key: 'wishlist', icon: HeartIcon, label: '위시리스트' },
  { key: 'bag', icon: ShoppingBagIcon, label: '장바구니' },
  { key: 'account', icon: UserIcon, label: '계정' },
];

export default function BottomNavBar({ selected, onSelect }: { selected: string, onSelect: (key: string) => void }) {
  const selectedIdx = TABS.findIndex(tab => tab.key === selected);
  const tabCount = TABS.length;

  return (
    <nav className="fixed bottom-0 left-0 right-0 z-50 flex justify-center pointer-events-none">
      <div className="navigation relative w-full flex justify-center items-end" style={{ height: 88 }}>
        <ul className="flex w-full p-0 m-0 relative pointer-events-auto" style={{ height: 70, background: '#3f3b3b', boxShadow: '0 2px 16px rgba(0,0,0,0.15)' }}>
          {TABS.map((tab, idx) => {
            const Icon = tab.icon;
            const isActive = selectedIdx === idx;
            return (
              <li
                key={tab.key}
                className={`relative flex-1 h-[70px] flex flex-col items-center z-10 transition-all duration-500 ${isActive ? 'active' : ''} cursor-pointer pointer-events-auto`}
                onClick={() => onSelect(tab.key)}
              >
                <a className="flex flex-col items-center justify-center w-full h-full font-medium relative">
                  <span
                    className={`icon block text-center transition-all duration-500 ${isActive ? 'translate-y-[-35px]' : ''}`}
                    style={{ color: '#f0f0f3' }}
                  >
                    <Icon className="w-7 h-7" />
                  </span>
                  <span
                    className={`text absolute font-normal text-xs tracking-wide transition-all duration-500 ${isActive ? 'opacity-100 translate-y-[15px]' : 'opacity-0 translate-y-[15px]'}`}
                    style={{ color: '#f0f0f3' }}
                  >
                    {tab.label}
                  </span>
                </a>
              </li>
            );
          })}
          {/* indicator */}
          <div
            className="indicator absolute top-[-35px] left-0 h-[70px] rounded-[100%] transition-transform duration-500"
            style={{
              width: `calc(100% / ${tabCount})`,
              background: 'tomato',
              border: '6px solid #f0f2f8',
              boxSizing: 'border-box',
              boxShadow: '0 2px 16px rgba(0,0,0,0.15)',
              transform: `translateX(${selectedIdx * 100}%)`,
            }}
          >
            {/* 왼쪽 곡선 */}
            <span
              className="absolute"
              style={{
                left: -22,
                top: '70%',
                width: 20,
                height: 24,
                background: 'transparent',
                borderTopRightRadius: 20,
                boxShadow: '1px -10px 0 0 #f0f2f8',
                content: '""',
                display: 'block',
                position: 'absolute',
                transform: 'translateY(-50%)',
              }}
            />
            {/* 오른쪽 곡선 */}
            <span
              className="absolute"
              style={{
                right: -22,
                top: '70%',
                width: 20,
                height: 24,
                background: 'transparent',
                borderTopLeftRadius: 20,
                boxShadow: '-1px -10px 0 0 #f0f2f8',
                content: '""',
                display: 'block',
                position: 'absolute',
                transform: 'translateY(-50%)',
              }}
            />
          </div>
        </ul>
      </div>
    </nav>
  );
} 