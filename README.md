# AIGC LocalWeb

多模块Spring Boot项目，使用JDK 17和Spring Boot 3.3.0。包含worker和server两个模块。

## 项目结构

```
.
├── pom.xml                    # 父POM文件
├── README.md
├── worker/                    # Worker模块
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── sakura
│       │   │           └── worker
│       │   │               ├── WorkerApplication.java      # Spring Boot主类
│       │   │               ├── controller/                 # 控制器层
│       │   │               │   └── WorkerController.java
│       │   │               ├── service/                    # 服务层
│       │   │               │   └── WorkerService.java
│       │   │               ├── repository/               # 数据访问层
│       │   │               │   └── WorkerTaskRepository.java
│       │   │               ├── entity/                    # 实体类
│       │   │               │   └── WorkerTask.java
│       │   │               ├── dto/                        # 数据传输对象
│       │   │               │   ├── TaskRequest.java
│       │   │               │   └── TaskResponse.java
│       │   │               ├── exception/                 # 异常处理
│       │   │               │   ├── WorkerException.java
│       │   │               │   └── GlobalExceptionHandler.java
│       │   │               └── config/                     # 配置类
│       │   │                   └── WorkerConfig.java
│       │   └── resources
│       │       ├── application.yml
│       │       └── application-dev.yml
│       └── test
│           └── java
│               └── com
│                   └── sakura
│                       └── worker
│                           └── service
│                               └── WorkerServiceTest.java
└── server/                    # Server模块
    ├── pom.xml
    └── src
        ├── main
        │   ├── java
        │   │   └── com
        │   │       └── sakura
        │   │           └── server
        │   │               ├── ServerApplication.java      # Spring Boot主类
        │   │               ├── controller/                 # 控制器层
        │   │               │   └── ServerController.java
        │   │               ├── service/                    # 服务层
        │   │               │   └── ServerService.java
        │   │               ├── exception/                 # 异常处理
        │   │               │   ├── ServerException.java
        │   │               │   └── GlobalExceptionHandler.java
        │   │               └── config/                     # 配置类
        │   │                   └── ServerConfig.java
        │   └── resources
        │       ├── application.yml
        │       └── application-dev.yml
        └── test
            └── java
                └── com
                    └── sakura
                        └── server
                            └── service
                                └── ServerServiceTest.java
```

## 模块说明

### Worker模块
- **功能**: 负责处理工作任务
- **主类**: `com.sakura.worker.WorkerApplication`
- **端口**: 8081
- **核心组件**:
  - `WorkerController` - REST API控制器
  - `WorkerService` - 业务逻辑服务
  - `WorkerTaskRepository` - 数据访问层
  - `WorkerTask` - 任务实体类

### Server模块
- **功能**: 负责提供Web服务
- **主类**: `com.sakura.server.ServerApplication`
- **端口**: 8080
- **依赖**: 依赖Worker模块
- **核心组件**:
  - `ServerController` - REST API控制器
  - `ServerService` - 业务逻辑服务（调用Worker服务）

## 技术栈

- **JDK**: 17
- **Spring Boot**: 3.3.0
- **构建工具**: Maven 3.6+
- **数据库**: H2 (内存数据库，用于开发测试)

## 环境要求

- JDK 17
- Maven 3.6+

## 配置JDK 17

如果Maven使用的是其他版本的Java，需要配置使用JDK 17：

### 方法1：设置JAVA_HOME环境变量（推荐）

Windows:
```cmd
set JAVA_HOME=C:\Program Files\Java\jdk-17
```

然后重新运行Maven命令。

### 方法2：在命令行中临时指定

Windows:
```cmd
set JAVA_HOME=C:\Program Files\Java\jdk-17
mvn clean compile
```

### 方法3：配置Maven settings.xml

编辑 `%MAVEN_HOME%\conf\settings.xml` 或在用户目录下的 `.m2/settings.xml`，添加：
```xml
<profiles>
  <profile>
    <id>jdk-17</id>
    <activation>
      <activeByDefault>true</activeByDefault>
    </activation>
    <properties>
      <maven.compiler.source>17</maven.compiler.source>
      <maven.compiler.target>17</maven.compiler.target>
      <maven.compiler.compilerVersion>17</maven.compiler.compilerVersion>
    </properties>
  </profile>
</profiles>
```

**注意**：请将JDK路径替换为您实际的JDK 17安装路径。

## 构建和运行

### 编译所有模块
```bash
mvn clean compile
```

### 编译指定模块
```bash
mvn clean compile -pl worker
mvn clean compile -pl server
```

### 运行所有测试
```bash
mvn test
```

### 运行指定模块的测试
```bash
mvn test -pl worker
mvn test -pl server
```

### 打包所有模块
```bash
mvn clean package
```

### 打包指定模块
```bash
mvn clean package -pl worker
mvn clean package -pl server
```

### 运行Worker模块
```bash
mvn spring-boot:run -pl worker
```

或者直接运行：
```bash
java -jar worker/target/worker-1.0-SNAPSHOT.jar
```

### 运行Server模块
```bash
mvn spring-boot:run -pl server
```

或者直接运行：
```bash
java -jar server/target/server-1.0-SNAPSHOT.jar
```

## API接口

### Worker模块接口

- `POST /api/worker/task` - 处理任务
- `GET /api/worker/status` - 获取Worker状态

### Server模块接口

- `POST /api/server/request` - 处理请求
- `GET /api/server/health` - 健康检查

## 开发

项目使用标准的Spring Boot多模块结构：

### 目录结构说明

- **controller/** - REST API控制器层，处理HTTP请求
- **service/** - 业务逻辑服务层
- **repository/** - 数据访问层（JPA Repository）
- **entity/** - JPA实体类
- **dto/** - 数据传输对象（Data Transfer Object）
- **exception/** - 自定义异常和全局异常处理
- **config/** - Spring配置类

### 配置文件

- `application.yml` - 主配置文件
- `application-dev.yml` - 开发环境配置文件

### 模块依赖关系

- Server模块依赖Worker模块
- Worker模块是独立模块，不依赖其他业务模块

## 开发建议

1. 使用Spring Boot DevTools进行热部署开发
2. 使用H2控制台查看数据库：`http://localhost:8081/h2-console` (Worker) 或 `http://localhost:8080/h2-console` (Server)
3. 遵循RESTful API设计规范
4. 使用DTO进行数据传输，避免直接暴露实体类
5. 使用全局异常处理器统一处理异常
