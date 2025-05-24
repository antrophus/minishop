import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

// API 서버 주소 (백엔드 포트에 맞게 수정)
const API_BASE = 'http://localhost:8080/api/auth';

// 유틸: fetch wrapper (production-safe)
async function apiFetch(path: string, options: RequestInit = {}) {
  const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
  let body = options.body;
  // body가 객체면 JSON.stringify로 변환
  if (body && typeof body !== 'string') {
    body = JSON.stringify(body);
  }
  const res = await fetch(API_BASE + path, {
    ...options,
    headers,
    body,
    // credentials: 'include'는 JWT 인증만 쓸 때는 불필요
  });
  const text = await res.text();
  let data;
  try { data = JSON.parse(text); } catch { data = text; }
  return { status: res.status, data };
}

function App() {
  // 상태
  const [form, setForm] = useState({
    email: '', username: '', password: '', name: '', phone: '', address: '', gender: '',
    loginId: '', loginPw: '',
    currentPassword: '',
    newPassword: '',
    resetToken: '',
    resetNewPassword: '',
  });
  const [jwt, setJwt] = useState('');
  const [log, setLog] = useState<string[]>([]);

  // 로그 추가
  const addLog = (msg: string) => setLog(l => [msg, ...l]);

  // 입력 핸들러
  const handleInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm(f => ({ ...f, [e.target.name]: e.target.value }));
  };

  // 회원가입
  const register = async () => {
    const res = await apiFetch('/register', {
      method: 'POST',
      body: JSON.stringify({
        email: form.email.trim(), username: form.username, password: form.password,
        name: form.name, phone: form.phone, address: form.address, gender: form.gender
      })
    });
    addLog(`[회원가입] status=${res.status} result=${JSON.stringify(res.data)}`);
  };

  // 중복 이메일/아이디 체크 (회원가입 시도 후 메시지로 확인)

  // 로그인
  const login = async () => {
    const res = await apiFetch('/login', {
      method: 'POST',
      body: JSON.stringify({ emailOrUsername: form.loginId, password: form.loginPw })
    });
    if (res.status === 200 && res.data.accessToken) setJwt(res.data.accessToken);
    addLog(`[로그인] status=${res.status} result=${JSON.stringify(res.data)}`);
  };

  // 비밀번호 변경
  const changePassword = async () => {
    if (!jwt) return addLog('로그인 후 시도하세요.');
    const res = await apiFetch('/password/change', {
      method: 'POST',
      body: JSON.stringify({ currentPassword: form.currentPassword, newPassword: form.newPassword }),
      headers: { Authorization: `Bearer ${jwt}` }
    });
    addLog(`[비밀번호 변경] status=${res.status} result=${JSON.stringify(res.data)}`);
  };

  // 비밀번호 재설정 요청
  const requestReset = async () => {
    if (!jwt) return addLog('로그인 후 시도하세요.');
    const res = await apiFetch('/password/reset-request', {
      method: 'POST',
      body: JSON.stringify({ email: form.email }),
      headers: { Authorization: `Bearer ${jwt}` }
    });
    // 백엔드 응답이 문자열(토큰)일 때만 추출
    if (res.status === 200 && typeof res.data === 'string' && res.data.startsWith('비밀번호 재설정 토큰:')) {
      const token = res.data.replace('비밀번호 재설정 토큰: ', '').trim();
      setForm(f => ({ ...f, resetToken: token }));
      addLog(`[비밀번호 재설정 요청] 토큰 발급 성공: ${token}`);
    } else {
      addLog(`[비밀번호 재설정 요청] status=${res.status} result=${JSON.stringify(res.data)}`);
    }
  };

  // 비밀번호 재설정 확인
  const confirmReset = async () => {
    if (!jwt) return addLog('로그인 후 시도하세요.');
    const res = await apiFetch('/password/reset-confirm', {
      method: 'POST',
      body: JSON.stringify({ token: form.resetToken, newPassword: form.resetNewPassword }),
      headers: { Authorization: `Bearer ${jwt}` }
    });
    if (res.status === 200 && typeof res.data === 'string') {
      addLog(`[비밀번호 재설정 확인] 성공: ${res.data}`);
    } else {
      addLog(`[비밀번호 재설정 확인] status=${res.status} result=${JSON.stringify(res.data)}`);
    }
  };

  return (
    <div style={{ maxWidth: 600, margin: '0 auto', fontFamily: 'sans-serif' }}>
      <h2>통합 인증/회원/비밀번호 테스트 UI</h2>
      <section style={{ border: '1px solid #ccc', padding: 16, marginBottom: 16 }}>
        <h3>1. 회원가입</h3>
        <input name="email" placeholder="이메일" value={form.email} onChange={handleInput} />
        <input name="username" placeholder="유저네임" value={form.username} onChange={handleInput} />
        <input name="password" placeholder="비밀번호" type="password" value={form.password} onChange={handleInput} />
        <input name="name" placeholder="이름" value={form.name} onChange={handleInput} />
        <input name="phone" placeholder="전화번호" value={form.phone} onChange={handleInput} />
        <input name="address" placeholder="주소" value={form.address} onChange={handleInput} />
        <input name="gender" placeholder="성별" value={form.gender} onChange={handleInput} />
        <button onClick={register}>회원가입</button>
      </section>
      <section style={{ border: '1px solid #ccc', padding: 16, marginBottom: 16 }}>
        <h3>2. 로그인</h3>
        <input name="loginId" placeholder="이메일 또는 유저네임" value={form.loginId} onChange={handleInput} />
        <input name="loginPw" placeholder="비밀번호" type="password" value={form.loginPw} onChange={handleInput} />
        <button onClick={login}>로그인</button>
        <div>JWT: <span style={{ fontSize: 12, wordBreak: 'break-all' }}>{jwt}</span></div>
      </section>
      <section style={{ border: '1px solid #ccc', padding: 16, marginBottom: 16 }}>
        <h3>3. 비밀번호 변경</h3>
        <input name="currentPassword" placeholder="현재 비밀번호" type="password" value={form.currentPassword} onChange={handleInput} />
        <input name="newPassword" placeholder="새 비밀번호" type="password" value={form.newPassword} onChange={handleInput} />
        <button onClick={changePassword}>비밀번호 변경</button>
      </section>
      <section style={{ border: '1px solid #ccc', padding: 16, marginBottom: 16 }}>
        <h3>4. 비밀번호 재설정(토큰 기반)</h3>
        <button onClick={requestReset}>비밀번호 재설정 토큰 발급</button>
        <input name="resetToken" placeholder="재설정 토큰" value={form.resetToken} onChange={handleInput} />
        <input name="resetNewPassword" placeholder="새 비밀번호" type="password" value={form.resetNewPassword} onChange={handleInput} />
        <button onClick={confirmReset}>비밀번호 재설정</button>
      </section>
      <section style={{ border: '1px solid #ccc', padding: 16, marginBottom: 16 }}>
        <h3>로그 메시지</h3>
        <div style={{ maxHeight: 200, overflow: 'auto', background: '#f9f9f9', fontSize: 13, color: '#333' }}>
          {log.map((msg, i) => <div key={i}>{msg}</div>)}
        </div>
      </section>
    </div>
  )
}

export default App
