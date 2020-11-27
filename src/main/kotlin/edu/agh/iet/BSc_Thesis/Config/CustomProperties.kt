package edu.agh.iet.BSc_Thesis.Config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "custom")
class CustomProperties {
    public var frontendUrl: String? = null
}