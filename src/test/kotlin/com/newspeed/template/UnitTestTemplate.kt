package com.newspeed.template

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension


@DisplayNameGeneration(ReplaceUnderscores::class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension::class)
@ExtendWith(MockKExtension::class)
interface UnitTestTemplate