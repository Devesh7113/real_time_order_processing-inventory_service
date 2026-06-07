package com.real_time_order_processing.inventory.config;

import com.real_time_order_processing.inventory.tools.HelpDeskTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MCPServerConfig
{
    @Bean
    ToolCallbackProvider helpDeskToolProvider(HelpDeskTools helpDeskTools)
    {
        return MethodToolCallbackProvider.builder().toolObjects(helpDeskTools).build();
    }
}
