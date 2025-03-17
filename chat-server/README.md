# 채팅 서버 및 REST API 구현

## Skill
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=flat-square&logo=kotlin&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat-square&logo=spring&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-007396?style=flat-square&logo=websocket&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=flat-square&logo=junit5&logoColor=white)
![MockK](https://img.shields.io/badge/MockK-FF6600?style=flat-square&logo=mockk&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)
![Docker Compose](https://img.shields.io/badge/Docker%20Compose-2496ED?style=flat-square&logo=docker&logoColor=white)

## Features
- 사용자 조회, 생성, 수정, 삭제 
- 실시간 채팅 
  1. 특정 사용자에게만 메세지 전송(mention) 구현 e.g. @{사용자명1} @{사용자명2}  
  2. 채팅방 입장, 퇴장 안내

## API EndPoint & Description
- `POST /api/v1/user` : 사용자 생성 
- `GET /api/v1/user/{id}` : 사용자 조회
- `PUT /api/v1/user/{id}` : 사용자 정보 수정 
  - 입력 필드의 값을 업데이트하고, 입력하지 않은 필드의 기존 데이터는 유지
    - 관련 테스트코드 : UpdateUserTests.updateUser - Only some values are changed() 
- `DELETE /api/v1/user/{id}` : 사용자 삭제

## 실행 방법
**1. Docker Compose 실행**

아래 명령어를 사용하여 채팅 서버 & REST API & PostgreSQL 컨테이너를 백그라운드에서 실행합니다.
```shell
docker compose up -d --build
```

**2. 채팅 클라이언트 실행**

아래 명령어를 사용하여 채팅 클라이언트 컨테이너를 실행합니다.

```shell
docker build . --build-arg CHAT_SERVER=ws://localhost:8080 -t murple-test-chat-client

docker run -it --rm -p [원하는포트:80]  murple-test-chat-client
```

**3. 채팅 진행**
- 특정 사용자 대상 전송 : 사용자 생성 api로 생성 후 @{생성한 사용자 이름} 로 메세지 전송 시 세션에 부여된 userId(PK)로 인해 해당 사용자에게만 메세지가 전송됩니다.
