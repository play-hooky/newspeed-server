package com.newspeed.domain.auth.api

import com.newspeed.template.CustomWebMvcTestTemplate
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@CustomWebMvcTestTemplate
@DisplayName("인증 인가 관련 페이지를 제공하는 컨트롤러 내")
class AuthViewControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Nested
    inner class `login GET api는 ` {

        @Test
        fun `권한이 없어도 로그인 페이지를 제공한다`() {
            mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
        }
    }
}