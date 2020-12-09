package edu.agh.iet.BSc_Thesis

import edu.agh.iet.BSc_Thesis.Config.CustomProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@SpringBootApplication
@ConfigurationPropertiesScan("edu.agh.iet.BSc_Thesis.Config")
@EnableConfigurationProperties(CustomProperties::class)
@EnableJpaRepositories
class SecureCommunicatorApplication

fun main(args: Array<String>) {
	println("Hello")
	runApplication<SecureCommunicatorApplication>(*args)
}
