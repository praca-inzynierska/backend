package edu.agh.iet.BSc_Thesis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.sql.DriverManager


@SpringBootApplication
@EnableJpaRepositories
class SecureCommunicatorApplication

fun main(args: Array<String>) {
	println("Hello")
	runApplication<SecureCommunicatorApplication>(*args)
}
