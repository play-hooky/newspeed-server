package com.newspeed.domain.content.application

import com.newspeed.domain.content.dto.ContentHostDTO
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.stereotype.Service

@Service
class InstagramCrawlService(
    private val driver: WebDriver,
    private val driverWait: WebDriverWait
) {

    fun getContentHostDTO(
        url: String
    ): ContentHostDTO {
        driver.get(url)

         val tag = By.cssSelector("img[alt$='프로필 사진']")
        driverWait.until{
            drivers -> drivers.findElements(tag).size > 1
        }

        val image = driver.findElements(tag).get(1)
        val nickname = image.getAttribute("alt")
        val profileImage = image.getAttribute("src")

        return ContentHostDTO(
            nickname = nickname,
            profileImgUrl = profileImage
        )
    }
}