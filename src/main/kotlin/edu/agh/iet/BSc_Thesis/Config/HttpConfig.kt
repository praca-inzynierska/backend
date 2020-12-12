package edu.agh.iet.BSc_Thesis.Config

import org.apache.catalina.connector.Connector
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory

import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class HttpConfig {
    @Value("\${server.http.port}")
    private val httpPort = 0

    @Bean // (it only works for springboot 2.x)
    fun servletContainer(): ServletWebServerFactory {
        val factory = TomcatServletWebServerFactory()
        factory.addAdditionalTomcatConnectors(createStanderConnecter())
        return factory
    }

    private fun createStanderConnecter(): Connector {
        val connector =  //new Connector("org.apache.coyote.http11.Http11NioProtocol");
                Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL)
        connector.setPort(httpPort)
        return connector
    }
}