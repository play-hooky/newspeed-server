package com.newspeed

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class NewspeedServerApplication

fun main(args: Array<String>) {
	runApplication<NewspeedServerApplication>(*args)
}
