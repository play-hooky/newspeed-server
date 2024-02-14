package com.newspeed.util

import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.test.context.junit.jupiter.SpringExtension

class DatabaseClearExtension: BeforeEachCallback {
    override fun beforeEach(
        extension: ExtensionContext
    ) {
        val databaseCleaner = getDatabaseCleaner(extension)
        databaseCleaner.clear()
    }

    private fun getDatabaseCleaner(
        extensionContext: ExtensionContext
    ): DatabaseCleaner = SpringExtension.getApplicationContext(extensionContext)
        .getBean(DatabaseCleaner::class.java)
}