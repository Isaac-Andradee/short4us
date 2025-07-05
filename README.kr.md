# Short4Us: Spring Boot 기반의 URL 단축기

[![GitHub Stars](https://img.shields.io/github/stars/Isaac-Andradee/short4us.svg)](https://github.com/Isaac-Andradee/short4us/stargazers)
[![GitHub Issues](https://img.shields.io/github/issues/Isaac-Andradee/short4us.svg)](https://github.com/Isaac-Andradee/short4us/issues)
[![Current Version](https://img.shields.io/github/v/release/Isaac-Andradee/short4us)](https://github.com/Isaac-Andradee/short4us/releases)
[![Build Status](https://img.shields.io/github/actions/workflow/status/Isaac-Andradee/short4us/.github%2Fworkflows%2Fmaven-build-main.yml)](https://github.com/Isaac-Andradee/short4us/actions/workflows/maven-build-main.yml)
[![Contributions](https://img.shields.io/github/contributors/Isaac-Andradee/short4us.svg)](https://github.com/Isaac-Andradee/short4us/graphs/contributors)
[![Top Language](https://img.shields.io/github/languages/top/Isaac-Andradee/short4us)](https://github.com/search?q=repo%3AIsaac-Andradee%2Fshort4us++language%3AJava&type=code)

Short4Us는 Spring Boot와 마이크로서비스 아키텍처로 개발된 URL 단축 시스템입니다. Docker Swarm을 통해 완전히 확장 가능하며, 긴 URL을 빠르고 안정적으로 단축할 수 있도록 설계되었습니다.
![Screenshot](https://github.com/user-attachments/assets/d419bea8-d448-4e3a-b950-19a833c06f82)

## 아키텍처 구성

Short4Us는 여러 개의 마이크로서비스로 구성됩니다:

1. **Shortener Service**: 긴 URL을 짧게 만들고, 고유 키를 생성하는 역할.
2. **Resolver Service**: 단축된 URL을 원래 URL로 리디렉션.
3. **KeyGen Service**: 고유한 단축 키 생성 담당.
4. **Eureka Server**:  서비스 등록 및 탐색용 디스커버리 서버.
5. **API Gateway**: 요청을 각 서비스로 라우팅하는 역할.

데이터 저장은 고성능 캐시용 Redis, 영속 저장용 MongoDB Atlas를 사용하며, 수평 확장을 고려한 구조입니다.


> [!TIP]
> 서비스 및 설계 흐름도 보기 [here](https://github.com/Isaac-Andradee/short4us/blob/main/SYSTEM-DESIGN.md)

## 사전 준비사항

Short4Us를 실행하려면 다음이 필요합니다:

- Docker 및 Docker Compose
- Docker Swarm
- MongoDB Atlas 계정 및 클러스터
- Docker Hub 계정 (이미지 푸시용)
- Java 21 및 Maven 3.6+ 이상

## MongoDB Atlas 설정 방법

애플리케이션을 베포하기 전에 MongoDB Atlas 클러스터를 먼저 설정해야합니다.

1. **MongoDB Atlas 계정을 생성하세요**:  [MongoDB Atlas](https://www.mongodb.com/atlas) 방문 후 계정 생성

2. *클러스터 생성**:
    - M0 무료 티어로 생성 가능
    - 클라우드 공급자, 리전 선택
    - 클러스터 이름 지정 (예: "short4us-cluster")

3. **데이터베이스 접근 설정**:
    - Atlas 대시보드에서 "Database Access" 메로 이동하세요
    - 읽기/쓰기 권한이 있는 데이터베이스 사용를 생합니다
    - 생성한 사용자와 비밀번호를 기록해 두세요

4. **네트워크 접근 설정**:
    - Atlas 대시보드에서 "NetWork Access" 메뉴로 이동하세요
    - 본인의 IP 주소를 추가하거나, 테스트 용로는 0.0.0.0/0 을 추가할 수 있습니다(운영환경에서는 권장하지 않음)

5. **접근 문자열 획득**:
    - "Cluster" 메뉴로 이동한 후  "Connect" 버튼을 클릭하세요
    - "Connect your application" 옵션을 선택합니다.
    - 제공되는 연결 문자열(connection string)을 복사합니다. (예시: `mongodb+srv://username:password@cluster.xxxxx.mongodb.net/`)

## 환경 변수

매플리케이션 설정을 위해 프로젝트 루트 디렉토리에 .env 파일을 생성하고, 아래와 같은 환경 변수를 입력하세요.
```
# Eureka 설정
EUREKA_SERVER_HOST=eureka-server
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/
EUREKA_SERVER_PORT=8761

# MongoDB 설정
MONGODB_URI=mongodb+srv://username:password@cluster.xxxxx.mongodb.net/short4us?retryWrites=true&w=majority
MONGODB_DATABASE=short4us
MONGO_USERNAME=username
MONGO_PASSWORD=password

# Redis 설정
REDIS_NODE_1=redis-node-1:6379
REDIS_NODE_2=redis-node-2:6379
REDIS_NODE_3=redis-node-3:6379
REDIS_NODE_4=redis-node-4:6379
REDIS_NODE_5=redis-node-5:6379
REDIS_NODE_6=redis-node-6:6379
REDIS_PORT=6379
REDIS_PASSWORD=
REDIS_TIMEOUT=6000
SPRING_CACHE_TYPE=redis

# 서비스 포트
SHORTENER_SERVICE_PORT=8080
RESOLVER_SERVICE_PORT=8083
KEYGEN_SERVICE_PORT=8081

# 도메인 설정
SHORT_DOMAIN=http://localhost/
```

> [!IMPORTANT]
> MONGODB_URI 값에는 실제 MongoDB Atlas 연결 문자열을 넣어야 하며, 사용자명과 비밀번호, 클러스터 정보가 포함되어야 합니다.

## 설치 가이드

### 1. 레포지토리 클론

```bash
git clone https://github.com/Isaac-Andradee/short4us.git
cd short4us
```

### 2. 애플리케이션 빌드

Maven을 사용하여 모든 마이크로서비스를 빌드합니다.

```bash
mvn clean package
```

### 3. Docker 이미지 빌드 및 푸시

Docker Hub에 이미지를 푸시하기 위해 직접 Docker 이미지를 빌드해야 합니다 :

```bash
# Docker Hub에 로그인
docker login

# 이미지 빌드 및 태그 지정 ('yourusername' 을 본인의 Docker Hub 사용자명으로 변경)
docker build -t yourusername/eureka:latest ./eureka
docker build -t yourusername/shortener-service ./shortener-service
docker build -t yourusername/resolver-service ./resolver-service
docker build -t yourusername/keygen-service ./keygen-service

# 이미지 푸시
docker push yourusername/eureka:latest
docker push yourusername/shortener-service:latest
docker push yourusername/resolver-service:latest
docker push yourusername/keygen-service:latest
```

### 4. stack.yml 업데이트

Docker 이미지를 푸시한 후`stack.yml` 파일을 수정하여 Docker Hub에서 푸시한 이미지를 사용하도록 설정하세요:

```yaml
# sha256 해시 대신 본인의 Docker Hub 이미지로 교체
# 예사:
eureka-server:
  image: yourusername/eureka:latest
  # ... 기타 설정

shortener-service:
  image: yourusername/shortener-service:latest
  # ... 기타 설정

resolver-service:
  image: yourusername/resolver-service:latest
  # ... 기타 설정

keygen-service:
  image: yourusername/keygen-service:latest
  # ... 기타 설정
```

### 5. 환경 변수 설정

프로젝트 루트 디렉토리에 .env 파일을 생성하고, 앞서 안내된 환경 변수들을 입력합니다.
특히 MongoDB Atlas 연결 문자열을 정확히 입력해야 합니다.

### 6. Docker Swarm 배포

```bash
# Docker Swarm 초기화 (이미 초기화되어 있다면 생략 가능)
docker swarm init

# 스택 배포
docker stack deploy -c stack.yml short4us

# IMPORTANT: Redis 클러스터 초기화 필수! Redis 노드 중 하나에 접속하여 클러스터를 수동으로 생성해야 합니다.
docker exec -it $(docker ps | grep redis-node-1 | awk '{print $1}') sh -c "redis-cli --cluster create redis-node-1:6379 redis-node-2:6379 redis-node-3:6379 redis-node-4:6379 redis-node-5:6379 redis-node-6:6379 --cluster-replicas 1"
```

### 7. 배포 확인

모든 서비스가 정상적으로 실행되고 있는지 확인하려면 다음과 같은 명령어를 사용하세요.

```bash
docker service ls
```

eureka-server, shortener-service, resolver-service, keygen-service, redis-node 등의 서비스가 실행 중인지 확인하면 됩니다.

## 사용 방법

Short4Us는 URL을 단축기 위한 REST API을 제공합니다.

### URL 단축하기

애플리케이은 기본적으로 `http://localhost:80/` 에서 실행됩니다. Postman 또는 터미널에서 다음과 같이 테스트 할 수 있습니다.

```bash
curl -X POST http://localhost/shorten \
  -H "Content-Type: application/json" \
  -d '{"longUrl":"https://www.example.com/some/very/long/url/that/you/want/to/shorten", "alias": "optional-custom-alias"}'
```

### 단축 URL 접속

웹 브라우저에서 아래와 같이 단축된 URL에서 접속하면 됩니다.

```
http://localhost/{short-key}
```

서비스는 해당 요청을 원래의 URL로 자동 리디렉션합니다.

## 기술 아키텍쳐

- **Spring Boot**: 모든 서비스의 기반 프레임워
- **Spring Cloud**: 분산 구성 및 서비스 디스커버리 기능
- **MongoDB Atlas**: 클라우드 기반 영구 저장소
- **Redis Cluster**: 고성능 분산 캐시
- **Nginx**: API 게이트웨이 역할로 요청을 라우팅
- **Docker & Docker Swarm**: 컨테이너화 및 오스트레이션 도구

## 기여하기

기여하기를 환영합니다! 다음 절차를 따라주세요 : 

1. 레포지토리 포크
2. 기능 브랜치를 생성합니다 (`git checkout -b feature/amazing-feature`)
3. 변경사항을 커밋합니다 (`git commit -m 'Add an amazing feature'`)
4. 브랜치를 푸시합니다 (`git push origin feature/amazing-feature`)
5. Pull Request 생성합니다

## 문의하기

Isaac Andrade - [@Isaac-Andradee](isaac.andra84@gmail.com)
