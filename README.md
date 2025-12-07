# Weather MCP Server

一个基于 Spring Boot 的 MCP (Model Context Protocol) 天气服务器，可以让 Claude Desktop 查询天气信息。

## 📋 功能特性

- ✅ 实现 MCP 协议规范
- ✅ 提供天气查询工具
- ✅ 支持中国主要城市
- ✅ RESTful API 接口
- ✅ 易于扩展和定制

## 🚀 快速开始

### 前置要求

- Java 17 或更高版本
- Maven 3.6+
- Claude Desktop 应用

### 1. 克隆项目

```bash
git clone https://github.com/LeroyPine/AI-test.git
cd AI-test
```

### 2. 编译项目

```bash
mvn clean package
```

### 3. 运行服务

```bash
java -jar target/weather-mcp-server-1.0.0.jar
```

服务将在 `http://localhost:8080` 启动。

### 4. 配置 Claude Desktop

编辑 Claude Desktop 配置文件：

**macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`

**Windows**: `%APPDATA%\Claude\claude_desktop_config.json`

添加以下配置：

```json
{
  "mcpServers": {
    "weather": {
      "command": "java",
      "args": [
        "-jar",
        "/完整路径/weather-mcp-server-1.0.0.jar"
      ]
    }
  }
}
```

### 5. 重启 Claude Desktop

完全退出并重新启动 Claude Desktop 应用。

## 💬 使用示例

在 Claude Desktop 中，你可以这样询问：

- "北京今天天气怎么样？"
- "查询上海的天气"
- "深圳现在什么天气？"

## 📁 项目结构

```
src/main/java/com/leroypine/weathermcp/
├── WeatherMcpApplication.java      # 主应用入口
├── config/
│   └── McpConfig.java              # MCP 配置
├── controller/
│   └── McpController.java          # MCP 协议端点
├── service/
│   └── WeatherService.java         # 天气服务（模拟数据）
└── model/                          # 数据模型
    ├── WeatherInfo.java
    ├── McpRequest.java
    ├── McpInitializeResponse.java
    └── ...
```

## 🔧 扩展开发

### 接入真实天气API

当前实现使用模拟数据。要使用真实天气数据：

1. 注册天气 API 服务（推荐）：
   - [和风天气](https://dev.qweather.com/)
   - [心知天气](https://www.seniverse.com/)
   - [OpenWeatherMap](https://openweathermap.org/api)

2. 在 `application.yml` 中添加 API 密钥：

```yaml
weather:
  api:
    key: your-api-key
    url: https://devapi.qweather.com
```

3. 修改 `WeatherService.java` 中的 `getWeather` 方法，调用真实 API。

### 添加更多工具

在 `McpController.java` 的 `listTools()` 方法中添加新的工具定义，然后在 `callTool()` 中实现相应逻辑。

## 🛠️ 技术栈

- Spring Boot 3.2.0
- Java 17
- Maven
- Lombok
- WebFlux (用于异步 HTTP 调用)

## 📝 MCP 协议说明

本项目实现了 MCP 协议的以下端点：

- `POST /initialize` - 初始化 MCP 连接
- `POST /tools/list` - 列出可用工具
- `POST /tools/call` - 调用工具

## ⚠️ 注意事项

1. 当前版本使用**模拟天气数据**，仅用于演示
2. 生产环境请接入真实天气 API
3. 确保防火墙允许 8080 端口
4. 建议为 API 密钥使用环境变量

## 📄 许可证

MIT License

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📧 联系方式

如有问题，请在 GitHub Issues 中提出。

---

## 🎯 完整代码获取

由于 GitHub 文件数量限制，完整的项目代码请参考项目中的文档，或查看已上传的核心文件。

所有 Model 类和 Service 类的完整代码都在对应的目录中。

## 🏃 快速测试

编译完成后，你可以直接运行：

```bash
# 启动服务
java -jar target/weather-mcp-server-1.0.0.jar

# 在另一个终端测试 API
curl -X POST http://localhost:8080/initialize \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"initialize","id":1}'
```
