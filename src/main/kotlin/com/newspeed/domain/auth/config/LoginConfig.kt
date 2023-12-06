package com.newspeed.domain.auth.config

import com.newspeed.domain.auth.application.AuthInterceptor
import com.newspeed.domain.user.application.RoleArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class LoginConfig(
    private val authInterceptor: AuthInterceptor,
    private val roleArgumentResolver: RoleArgumentResolver
): WebMvcConfigurer {
    override fun addArgumentResolvers(
        resolvers: MutableList<HandlerMethodArgumentResolver>
    ) {
        resolvers.add(roleArgumentResolver)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/user/login")
    }
}