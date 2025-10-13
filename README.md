# 🛠 넥서스커뮤니티 스카우터 개발 프로젝트

고객사 서버 리소스 및 소켓, 연결자수 확인 및 쿼리 트레이싱.<br>
기존 배포되어있는 소스에 추가 적인 개발을 하기위해 만들어졌습니다.

---

## 🚀 기술 스택

- JDK 1.8 사용
- build java 17
- build tool gradle 8.5
- Slack WebHook 플러그인 추가완료
- Server/Collector (scouter-server)
- Agent-Host (scouter-host)
- Agent-Java
- install bash script 포함

---

## 📌 버전 히스토리

| 버전   | 주요 기능 |
|--------|-----------|
| v0.1   | server 설치,설정,실행,방화벽 허용 정책 적용 <br> server 설치,설정,실행,방화벽 허용 정책 적용 <br> agent.host 설치,설정,실행 <br> agent.java 설정 | 
| **v0.2**   | 서비스 설치 기본 디렉터리 변경 (/home/scoutersvc/scouter) <br> 경로 변경에 따른 설정 파일 및 스크립트 수정 <br> 방화벽 동작 기능 제거 <br> find 명령어 사용 시 사용자가 직접 선택 후 동작하도록 변경 <br> agent.java 톰캣 다중 인스턴스 환경에서 동작 가능하도록 수정 <br> 신규 오류 건 처리 |

## 🧩 향후 계획 (v0.2 ~ )

- NEXUS 제품 데이터 전송 방식 모니터링 개발
- 기본 제공 메트릭 외 추가 확장 가능성 확인 및 개발
