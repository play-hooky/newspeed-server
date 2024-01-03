package com.newspeed.template

import com.newspeed.global.config.JpaConfig
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import


@Retention(AnnotationRetention.RUNTIME)
@DataJpaTest
@Import(JpaConfig::class)
annotation class RepositoryTestTemplate