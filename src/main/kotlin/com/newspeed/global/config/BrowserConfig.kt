package com.newspeed.global.config

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import java.time.Duration
import java.time.temporal.ChronoUnit

@Configuration
class BrowserConfig(
    @Value("\${browser.name}")
    private val browserName: String,
    @Value("\${browser.driver.path}")
    private val browserDriverPath: Resource
) {

    @Bean
    fun chromeDriver(): ChromeDriver {
        System.setProperty(browserName, browserDriverPath.file.path)

        val options = ChromeOptions()
        options.addArguments("--start-maximized")
        options.addArguments("--headless")
        options.addArguments("--disable-gpu")
        options.addArguments("--no-sandbox")
        options.addArguments("--disable-popup-blocking")
        options.addArguments("--disable-default-apps")
        options.addArguments("--remote-allow-origins=*")

        return ChromeDriver(options)
    }

    @Bean
    fun webDriverWait(
        driver: ChromeDriver
    ): WebDriverWait = WebDriverWait(
        driver,
        Duration.of(60, ChronoUnit.SECONDS)
    )
}