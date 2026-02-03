# JavaPro

Maven项目示例，使用JDK 17。

## 项目结构

```
.
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── App.java
    │   └── resources
    │       └── application.properties
    └── test
        └── java
            └── com
                └── example
                    └── AppTest.java
```

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

### 编译项目
```bash
mvn clean compile
```

### 运行测试
```bash
mvn test
```

### 打包项目
```bash
mvn clean package
```

### 运行应用
```bash
mvn exec:java -Dexec.mainClass="com.example.App"
```

或者直接运行编译后的类：
```bash
java -cp target/classes com.example.App
```

## 开发

项目使用标准的Maven目录结构：
- `src/main/java` - 源代码
- `src/main/resources` - 资源文件
- `src/test/java` - 测试代码
