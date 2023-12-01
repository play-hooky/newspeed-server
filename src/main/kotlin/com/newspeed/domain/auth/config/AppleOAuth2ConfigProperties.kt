package com.newspeed.domain.auth.config

import com.newspeed.domain.auth.feign.request.AppleOAuth2TokenRequest
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*


@Component
data class AppleOAuth2ConfigProperties(
    @Value("\${oauth.apple.team-id}") val teamId: String,
    @Value("\${oauth.apple.client-id}") val clientId: String,
    @Value("\${oauth.apple.key-id}") val keyId: String,
    @Value("\${oauth.apple.key}") val key: String,
    @Value("\${oauth.apple.url}") val audience: String,
    @Value("\${oauth.grant-type}") val grantType: String,
    @Value("\${oauth.default-profile-image}") val defaultProfileImage: String,
) {

    fun toAppleOAuth2TokenRequest(
        authorizationCode: String
    ): AppleOAuth2TokenRequest = AppleOAuth2TokenRequest(
        clientId = clientId,
        clientSecret = createClientSecret(),
        authorizationCode = authorizationCode,
        grantType = grantType
    )

    private fun createClientSecret(): String {
        val now = Date()
        val expiration = Date(now.time + 60 * 60 * 1000)

        return Jwts.builder()
            .setHeaderParam("alg", SignatureAlgorithm.ES256.value)
            .setHeaderParam("kid", keyId)
            .setSubject(clientId)
            .setIssuer(teamId)
            .setAudience(audience)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(readPrivateKey(), SignatureAlgorithm.ES256)
            .compact()
    }

    private fun readPrivateKey(): PrivateKey {
        val encodedKey: ByteArray = Base64.getDecoder().decode(key)
        val keySpec = PKCS8EncodedKeySpec(encodedKey)
        val keyFactory = KeyFactory.getInstance("EC")

        return keyFactory.generatePrivate(keySpec)
    }
}
