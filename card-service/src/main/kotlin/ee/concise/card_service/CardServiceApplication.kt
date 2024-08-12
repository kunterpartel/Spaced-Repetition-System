package ee.concise.card_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["ee.concise.*"])
class CardServiceApplication

fun main(args: Array<String>) {
	runApplication<CardServiceApplication>(*args)
}
