# 토이 프로젝트 2&3 : 쇼핑몰 만들기 프로젝트 2&3단계
### [프로젝트 개요] 
- **프로젝트 명** : 스프링을 통한 쇼핑몰 기본&심화 기능 구현
- **상세 내용 :** [프로젝트 RFP 노션 링크](https://www.notion.so/Toy-Project-2-3-1b69047c353d8136b5b3f9614a04bb65?source=copy_link)
- **수행 및 결과물 제출 기한** : 6/16 (월) ~ 6/27 (금) 14:00
- **멘토진 코드리뷰 기한** : 6/30 (월) ~ 7/13 (일), 2주 간 진행 


### [프로젝트 진행 및 제출 방법]
- 본 패스트캠퍼스 Github의 Repository를 각 조별의 Github Repository를 생성 후 Fork합니다.
    - 패스트캠퍼스 깃헙은 Private 형태 (Public 불가)
- 조별 레포의 최종 branch → 패스트캠퍼스 업스트림 Repository의 main branch의 **PR 상태**로 제출합니다.
    - **PR TITLE : N조 최종 제출**
    - Pull Request 링크를 LMS로도 제출해 주셔야 최종 제출 완료 됩니다. (제출자: 조별 대표자 1인)
    - LMS를 통한 과제 미제출 시 점수가 부여되지 않습니다. 
- PR 제출 시 유의사항
    - 프로젝트 진행 결과 및 과업 수행 내용은 README.md에 상세히 작성 부탁 드립니다. 
    - 멘토님들께서 어플리케이션 실행을 위해 확인해야 할 환경설정 값 등도 반드시 PR 부가 설명란 혹은 README.md에 작성 부탁 드립니다.
    - **Pull Request에서 제출 후 절대 병합(Merge)하지 않도록 주의하세요!**
    - 수행 및 제출 과정에서 문제가 발생한 경우, 바로 질의응답 멘토님이나 강사님에게 얘기하세요! (강사님께서 필요시 개별 힌트 제공)

## 🛠️ 개발 환경용 Tomcat 설정 (IntelliJ 기준)

이 프로젝트는 Spring Framework 기반이며, 로컬 개발 시 `IntelliJ + Tomcat` 설정을 통해 WAR 없이 바로 실행할 수 있습니다.

---

### ✅ 1. Tomcat 설치 (최초 1회만)

1. IntelliJ → `Preferences (Settings)` → `Build, Execution, Deployment > Application Servers`
2. `➕` 클릭 → `Tomcat Server` 선택
3. 설치된 Tomcat 디렉토리 선택 (없다면 [Tomcat 공식 사이트](https://tomcat.apache.org/)에서 다운로드)

---

### ✅ 2. Run Configuration 생성

1. 메뉴 상단 `Run > Edit Configurations...`
2. `➕` 클릭 → `Tomcat Server > Local` 선택
3. 이름: `Tomcat Dev`
4. VM 옵션에 `-Dspring.profiles.active=dev` 추가

#### ▶ Server 탭
- `HTTP port`: 기본값 8080 (필요 시 변경)
- `On 'Update' action`: `Update classes and resources`
- `On frame deactivation`: `Update classes and resources`

#### ▶ Deployment 탭
1. `+` 버튼 클릭 → `Artifact > [your-project]:war exploded` 선택
2. `Application context`: `/` (또는 원하는 경로)

---

### ✅ 3. Artifact 설정 (war exploded)

1. 메뉴 `File > Project Structure (⌘ + ;)`
2. 좌측 `Artifacts` 탭 → `+` → `Web Application: Exploded`
3. 모듈 및 `WEB-INF` 자동 포함 확인
4. Output directory는 기본값 유지

---

### ✅ 4. 실행

- 상단 메뉴에서 `Run ▶️` 클릭 또는 `Shift + F10`
- 브라우저에서 [http://localhost:8080](http://localhost:8080) 접속
- JSP나 컨트롤러 변경 시 핫디플로이 가능

---

### 💡 개발 편의 팁

- JSP 수정은 실시간 반영됨
- Java 클래스 수정 시 `Build > Build Project (⌘ + F9 / Ctrl + F9)`
- 변경사항 반영 안 되면: `Run > Reload Changed Classes` 또는 재시작
- DB는 docker-compose로 띄운 MySQL을 연동하거나 외부 DB 사용 가능

---
