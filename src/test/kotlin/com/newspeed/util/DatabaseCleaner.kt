package com.newspeed.util

import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Component
class DatabaseCleaner {
    private lateinit var tableNames: List<String>

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var beanFactory: ListableBeanFactory

    @PostConstruct
    private fun findDatabaseTableNames() {
        val tables = entityManager.createNativeQuery("SHOW TABLES").resultList
        tableNames = tables
            .map { (it as Array<Any>)[0] as String }
    }

    @Transactional
    fun clear() {
        entityManager.clear()
        truncate()
        deleteAllRedis()
    }

    private fun truncate() {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS FALSE").executeUpdate()
        tableNames.forEach {
            val tableName = if (it == "user") "\"user\"" else it
            entityManager.createNativeQuery("TRUNCATE TABLE $tableName").executeUpdate()
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS TRUE").executeUpdate()
    }

    private fun deleteAllRedis() {
        val redisRepositories = beanFactory.getBeansOfType(CrudRepository::class.java)
        redisRepositories
            .values
            .forEach {
            it.deleteAll()
        }
    }
}