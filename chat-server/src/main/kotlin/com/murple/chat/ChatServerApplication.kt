package com.murple.chat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class ChatServerApplication

fun main(args: Array<String>) {
	runApplication<ChatServerApplication>(*args)
}
