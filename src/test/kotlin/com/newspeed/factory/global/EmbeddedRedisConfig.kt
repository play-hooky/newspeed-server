package com.newspeed.factory.global

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ClassPathResource
import redis.embedded.RedisServer
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@Profile("test")
@Configuration
class EmbeddedRedisConfig(
    @Value("\${spring.redis.arm-server-file}") private val armRedisServerFile: String,
    @Value("\${spring.redis.port}") private val port: Int
) {

    private lateinit var redisServer: RedisServer

    @PostConstruct
    fun startRedis() {
        redisServer = getExecutableRedisServer(port)
        redisServer.start()
    }

    @PreDestroy
    fun stopRedis() {
        redisServer.stop()
    }

    private fun isArmArchitecture(): Boolean =
        System.getProperty("os.arch").contains("aarch64")

    private fun getExecutableRedisServer(
        port: Int
    ): RedisServer = if (isArmArchitecture()) RedisServer(getArcMacRedisServerFile(), port) else RedisServer(port)

    private fun getArcMacRedisServerFile(): File = ClassPathResource(armRedisServerFile).file
}