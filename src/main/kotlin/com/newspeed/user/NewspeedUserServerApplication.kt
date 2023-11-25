package com.newspeed.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class NewspeedUserServerApplication

fun main(args: Array<String>) {
	runApplication<NewspeedUserServerApplication>(*args)
}
