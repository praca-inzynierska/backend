package edu.agh.iet.BSc_Thesis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
		exclude = [DataSourceAutoConfiguration::class]
)
class SecureCommunicatorApplication

fun main(args: Array<String>) {
	println("Hello")
	runApplication<SecureCommunicatorApplication>(*args)
}
