package com.crowtest.auth_service.kafka

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class TestProducer(val kafkaTemplate: KafkaTemplate<String, String>) {

    val logger = LoggerFactory.getLogger(this::class.java.name)

    fun sendMessage(msg: String){
        logger.info("sent $msg")
        kafkaTemplate.send("topic1", msg)
    }
}