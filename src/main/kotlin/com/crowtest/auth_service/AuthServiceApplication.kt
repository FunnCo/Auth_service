package com.crowtest.auth_service

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.internals.Topic
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate

@SpringBootApplication (scanBasePackages = ["com.crowtest.auth_service", "com.crowtest.auth_service.controller"])
class AuthServiceApplication


fun main(args: Array<String>) {
    runApplication<AuthServiceApplication>(*args)
}
