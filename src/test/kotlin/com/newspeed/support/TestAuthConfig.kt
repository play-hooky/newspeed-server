package com.newspeed.support

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("test")
@Configuration
class TestAuthConfig(
) {
}