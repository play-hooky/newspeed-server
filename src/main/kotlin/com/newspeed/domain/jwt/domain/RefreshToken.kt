package com.newspeed.domain.jwt.domain

import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import javax.persistence.Id

@RedisHash(value = "refreshToken", timeToLive = 2592000)
class RefreshToken(
    @Id
    var id: Long? = null,

    @Indexed
    var userId: Long,

    @Indexed
    var token: String
)