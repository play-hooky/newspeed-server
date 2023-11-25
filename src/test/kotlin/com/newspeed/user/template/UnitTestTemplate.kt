package com.newspeed.user.template

import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension


@DisplayNameGeneration(ReplaceUnderscores::class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension::class)
interface UnitTestTemplate