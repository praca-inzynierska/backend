package edu.agh.iet.BSc_Thesis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.sql.DriverManager


@SpringBootApplication
class SecureCommunicatorApplication

fun main(args: Array<String>) {
	println("Hello")
	runApplication<SecureCommunicatorApplication>(*args)
}
