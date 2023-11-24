package com.newspeed.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NewspeedUserServerApplication

fun main(args: Array<String>) {
	runApplication<NewspeedUserServerApplication>(*args)
}
