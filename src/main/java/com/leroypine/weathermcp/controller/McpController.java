package com.leroypine.weathermcp.controller;

import com.leroypine.weathermcp.model.*;
import com.leroypine.weathermcp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class McpController {

    @Autowired
    private WeatherService weatherService;

    @PostMapping("/initialize")
    public ResponseEntity<McpInitializeResponse> initialize(@RequestBody McpRequest request) {
        McpInitializeResponse response = new McpInitializeResponse();
        response.setProtocolVersion("2024-11-05");
        
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setName("weather-mcp-server");
        serverInfo.setVersion("1.0.0");
        response.setServerInfo(serverInfo);
        
        ServerCapabilities capabilities = new ServerCapabilities();
        capabilities.setTools(true);
        response.setCapabilities(capabilities);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tools/list")
    public ResponseEntity<McpToolsListResponse> listTools(@RequestBody(required = false) McpRequest request) {
        McpToolsListResponse response = new McpToolsListResponse();
        
        ToolDefinition weatherTool = new ToolDefinition();
        weatherTool.setName("get_weather");
        weatherTool.setDescription("获取指定城市的天气信息。支持中国主要城市，如：北京、上海、广州、深圳、杭州等。");
        
        ToolInputSchema schema = new ToolInputSchema();
        schema.setType("object");
        schema.setProperties(Map.of(
            "city", Map.of(
                "type", "string",
                "description", "城市名称，例如：北京、上海、广州"
            )
        ));
        schema.setRequired(List.of("city"));
        weatherTool.setInputSchema(schema);
        
        response.setTools(List.of(weatherTool));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tools/call")
    public ResponseEntity<McpToolCallResponse> callTool(@RequestBody McpToolCallRequest request) {
        McpToolCallResponse response = new McpToolCallResponse();
        
        if ("get_weather".equals(request.getParams().getName())) {
            Map<String, Object> arguments = request.getParams().getArguments();
            String city = (String) arguments.get("city");
            
            try {
                WeatherInfo weather = weatherService.getWeather(city);
                
                ToolCallContent content = new ToolCallContent();
                content.setType("text");
                content.setText(formatWeatherInfo(weather));
                
                response.setContent(List.of(content));
                response.setIsError(false);
            } catch (Exception e) {
                ToolCallContent errorContent = new ToolCallContent();
                errorContent.setType("text");
                errorContent.setText("获取天气信息失败: " + e.getMessage());
                
                response.setContent(List.of(errorContent));
                response.setIsError(true);
            }
        } else {
            ToolCallContent errorContent = new ToolCallContent();
            errorContent.setType("text");
            errorContent.setText("未知的工具: " + request.getParams().getName());
            
            response.setContent(List.of(errorContent));
            response.setIsError(true);
        }
        
        return ResponseEntity.ok(response);
    }

    private String formatWeatherInfo(WeatherInfo weather) {
        return String.format(
            "城市：%s\n" +
            "天气：%s\n" +
            "温度：%s°C\n" +
            "湿度：%s%%\n" +
            "风向：%s\n" +
            "风力：%s\n" +
            "空气质量：%s\n" +
            "更新时间：%s",
            weather.getCity(),
            weather.getCondition(),
            weather.getTemperature(),
            weather.getHumidity(),
            weather.getWindDirection(),
            weather.getWindPower(),
            weather.getAirQuality(),
            weather.getUpdateTime()
        );
    }
}