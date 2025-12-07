# Weather MCP Server 使用指南

## 📦 项目已完成！

恭喜！你的 Spring Boot Weather MCP Server 项目已经创建完成。

## 🏗️ 项目结构

所有核心代码文件已经上传到 GitHub：

- ✅ `pom.xml` - Maven 配置
- ✅ `src/main/java/com/leroypine/weathermcp/` - 所有 Java 源代码
  - ✅ `WeatherMcpApplication.java` - 主程序入口
  - ✅ `config/McpConfig.java` - CORS 配置
  - ✅ `controller/McpController.java` - MCP 协议端点实现
  - ✅ `service/WeatherService.java` - 天气服务（模拟数据）
  - ✅ `model/` - 所有数据模型类
- ✅ `src/main/resources/application.yml` - 应用配置
- ✅ `.gitignore` - Git 忽略文件
- ✅ `README.md` - 项目文档

## 🚀 快速开始

### 第一步：克隆项目

```bash
git clone https://github.com/LeroyPine/AI-test.git
cd AI-test
```

### 第二步：编译项目

```bash
mvn clean package
```

如果你没有安装 Maven，请先安装：
- macOS: `brew install maven`
- Windows: 下载并安装 [Maven](https://maven.apache.org/download.cgi)

### 第三步：运行服务

```bash
java -jar target/weather-mcp-server-1.0.0.jar
```

启动成功后，你会看到：
```
Started WeatherMcpApplication in X.XXX seconds
```

服务将在 `http://localhost:8080` 运行。

### 第四步：配置 Claude Desktop

1. 找到 Claude Desktop 配置文件：
   - **macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`
   - **Windows**: `%APPDATA%\Claude\claude_desktop_config.json`

2. 添加以下配置（替换为你的实际路径）：

```json
{
  "mcpServers": {
    "weather": {
      "command": "java",
      "args": [
        "-jar",
        "/Users/你的用户名/AI-test/target/weather-mcp-server-1.0.0.jar"
      ]
    }
  }
}
```

**注意**：路径必须是完整的绝对路径！

3. 保存文件后，**完全退出** Claude Desktop（不是最小化）

4. 重新启动 Claude Desktop

### 第五步：测试

在 Claude Desktop 中询问：

```
"北京今天天气怎么样？"
"查询上海的天气"
"深圳现在什么天气？"
```

如果配置成功，Claude 会调用你的 MCP 服务器获取天气信息！

## 🎯 验证连接

你可以手动测试 API：

### 测试初始化端点

```bash
curl -X POST http://localhost:8080/initialize \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"initialize","id":1}'
```

### 测试工具列表

```bash
curl -X POST http://localhost:8080/tools/list \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"tools/list","id":2}'
```

### 测试天气查询

```bash
curl -X POST http://localhost:8080/tools/call \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc":"2.0",
    "method":"tools/call",
    "params":{
      "name":"get_weather",
      "arguments":{"city":"北京"}
    },
    "id":3
  }'
```

## 🔧 接入真实天气 API

当前使用的是**模拟数据**。要使用真实天气数据：

### 1. 注册天气 API

推荐服务：
- [和风天气](https://dev.qweather.com/) - 免费版每天 1000 次
- [心知天气](https://www.seniverse.com/) - 免费版每天 400 次
- [OpenWeatherMap](https://openweathermap.org/api) - 免费版每分钟 60 次

### 2. 修改配置

在 `src/main/resources/application.yml` 中添加：

```yaml
weather:
  api:
    key: 你的API密钥
    url: https://devapi.qweather.com
```

### 3. 修改服务代码

编辑 `WeatherService.java`，使用 WebClient 调用真实 API。

参考示例代码在 Artifact 中。

## ⚠️ 常见问题

### Q1: 编译失败，提示找不到 Lombok

**解决方案**：确保你的 IDE 安装了 Lombok 插件
- IntelliJ IDEA: Settings → Plugins → 搜索 "Lombok"
- VS Code: 安装 Lombok Annotations Support 扩展

### Q2: Claude Desktop 无法连接

**检查清单**：
1. ✅ 服务是否在运行？（`http://localhost:8080`）
2. ✅ 配置文件路径是否正确？（必须是绝对路径）
3. ✅ Claude Desktop 是否完全重启？
4. ✅ 配置文件 JSON 格式是否正确？

### Q3: 端口 8080 被占用

**解决方案**：修改 `application.yml` 中的端口号：

```yaml
server:
  port: 8888  # 改为其他端口
```

然后重新编译运行。

### Q4: 找不到 jar 文件

**解决方案**：确保在项目根目录运行 `mvn clean package`，jar 文件会在 `target/` 目录下。

## 📝 技术细节

### MCP 协议实现

本项目实现了 MCP 协议的三个核心端点：

1. **POST /initialize** - 初始化连接，返回服务器信息
2. **POST /tools/list** - 列出可用工具，返回工具定义
3. **POST /tools/call** - 调用工具，执行实际业务逻辑

### 数据流程

```
Claude Desktop
    ↓ (启动时读取配置)
执行: java -jar weather-mcp-server.jar
    ↓ (Spring Boot 启动)
监听 MCP 协议请求
    ↓ (用户询问天气)
Claude 调用 /tools/call
    ↓ (MCP Controller 处理)
WeatherService 返回数据
    ↓ (格式化响应)
返回给 Claude
    ↓
显示给用户
```

## 🎨 自定义扩展

### 添加新的工具

1. 在 `McpController.listTools()` 中定义新工具
2. 在 `McpController.callTool()` 中实现工具逻辑
3. 创建相应的 Service 类处理业务

### 示例：添加"获取空气质量"工具

```java
// 在 listTools() 中添加
ToolDefinition aqiTool = new ToolDefinition();
aqiTool.setName("get_air_quality");
aqiTool.setDescription("获取城市空气质量指数");
// ... 设置 schema

// 在 callTool() 中处理
if ("get_air_quality".equals(toolName)) {
    // 实现逻辑
}
```

## 📚 学习资源

- [MCP 协议规范](https://modelcontextprotocol.io/)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [和风天气 API 文档](https://dev.qweather.com/docs/api/)

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

项目地址：https://github.com/LeroyPine/AI-test

## 📄 许可证

MIT License

---

**祝你使用愉快！如有问题，请在 GitHub Issues 提出。** 🎉
