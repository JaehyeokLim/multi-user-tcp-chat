# Multi-User TCP Chat
[블로그 글](https://jaehyeoklim.tistory.com/1)
## 개요

Java의 `Socket`과 `Thread`를 기반으로 **여러 사용자가 동시에 채팅할 수 있는 TCP 서버**를 구현한 프로젝트입니다.

클라이언트는 명령어 기반으로 서버와 상호작용하며, 애노테이션 + 리플렉션 기반의 명령 디스패처 구조를 갖고 있습니다.

---

## 목표

- Java 네트워크 통신 구조 학습
- 멀티스레드 기반의 클라이언트 처리
- 커맨드 기반 요청 설계 및 실행 구조 구성
- 서버 안정성과 자원 누수 방지 설계

---

## 마일스톤

- [x]  다중 클라이언트 연결 수용
- [x]  사용자 등록, 채널 입장, 메시지 송수신
- [x]  명령어 기반 구조 + 애노테이션 핸들링
- [x]  서버 종료 시 graceful shutdown 구현
- [x]  공통 유틸(Logger, ResourceCloser) 설계

---

## 요구사항

### 기본 기능

- 클라이언트 접속 및 퇴장
- 사용자 등록
- 로그인, 검증
- 메시지 전송 및 수신
- 명령어 기반 요청 처리

### 심화 기능

- `@Mapping` 애노테이션 기반 명령 매핑 구조
- `CommandDispatcher` 클래스 통한 파싱 + 실행
- `SessionManager`로 세션 추적 및 제어
- `Logger`, `ResourceCloser`로 자원 관리 통일
- `ShutdownHook`으로 안전한 서버 종료 처리
- `CommandPreprocessor`로 클라이언트 명령 사전 검증
- `PasswordEncoder`로 사용자 비밀번호 암호화 (`JBCrypt` 기반)

---

## 사용 기술

- **Java 24**
- **Socket / ServerSocket API**
- **ExecutorService / Thread 기반 멀티스레딩**
- **애노테이션 + 리플렉션**
- **JBCrypt 비밀번호 암호화**
- **도메인 기반 유저/채널/세션 구조**

---

## 주요 구성 요소

### 클라이언트 모듈

| 클래스 | 설명 |
| --- | --- |
| `ClientMain` | 프로그램 진입점, 안내 메시지 출력 |
| `Client` | 서버 연결 및 스트림 핸들링 |
| `ReadHandler` / `WriteHandler` | 메시지 송수신 처리 스레드 |
| `CommandPreprocessor` | 입력 명령어 파싱 및 유효성 검증 |

### 서버 모듈

| 클래스 | 설명 |
| --- | --- |
| `ServerMain` / `Server` | 서버 실행 및 클라이언트 수용 |
| `Session` / `SessionManager` | 연결 상태 관리 및 유저 추적 |
| `CommandController` | 실제 명령 로직 (`/join`, `/send`, `/register`, `/login`) |
| `CommandDispatcher` | 클라이언트 명령 파싱 → 메서드 실행 매핑 |
| `Mapping` | 명령어를 메서드와 연결하는 커스텀 애노테이션 |
| `FileRepository` | 유저 데이터 저장 및 불러오기 |
| `PasswordEncoder` | `JBCrypt` 기반 비밀번호 해싱 및 검증 |
| `ShutdownHook` | 안전한 서버 종료 처리 |

### 공통 모듈 (common)

| 클래스 | 설명 |
| --- | --- |
| `Logger` | 포맷 통일 로그 출력 유틸 |
| `ResourceCloser` | 스트림, 소켓, 스레드 해제 유틸 |

---

## 프로젝트 구조 (3-모듈 분리)

```
multi-user-tcp-chat/
├── client/
│   └── com.jaehyeoklim.tcp.chat.client
│       ├── command/            # CommandPreprocessor
│       ├── handler/            # ReadHandler, WriteHandler
│       ├── main/               # ClientMain
│       └── network/            # Client
│
├── server/
│   └── com.jaehyeoklim.tcp.chat.server
│       ├── command/            # CommandController, Dispatcher, Mapping
│       ├── domain/             # User, Message
│       ├── main/               # Server, ServerMain
│       ├── repository/         # FileRepository
│       ├── session/            # Session, SessionManager
│       └── util/               # PasswordEncoder, ShutdownHook, UUIDGenerator
│
├── common/
│   └── com.jaehyeoklim.tcp.chat.util
│       ├── Logger
│       └── ResourceCloser

```

---

## 동작 화면

> 콘솔 기반 입출력 흐름
> <img width="1367" height="617" alt="스크린샷 2025-07-24 오후 4 35 49" src="https://github.com/user-attachments/assets/cb0900cd-e137-4e3c-97e2-eeb25ba1f5e8" />
> <img width="1369" height="618" alt="스크린샷 2025-07-24 오후 4 36 14" src="https://github.com/user-attachments/assets/2a0d0b7e-c36e-4196-a4ee-314435cba21b" />
> <img width="1369" height="503" alt="스크린샷 2025-07-24 오후 4 43 19" src="https://github.com/user-attachments/assets/77aa1d67-f852-4616-84de-c1a54de65e77" />

