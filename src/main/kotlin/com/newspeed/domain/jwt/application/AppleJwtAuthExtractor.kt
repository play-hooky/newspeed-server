package com.newspeed.domain.jwt.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.newspeed.domain.auth.domain.AuthPayload
import com.newspeed.domain.auth.domain.toAuthPayload
import com.newspeed.domain.auth.feign.AppleOAuth2TokenClient
import com.newspeed.domain.auth.feign.response.AppleOAuth2PublicKeyResponse
import com.newspeed.domain.auth.feign.response.AppleOAuth2TokenHeader
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec

@Component
class AppleJwtAuthExtractor(
    private val objectMapper: ObjectMapper,
    private val appleOAuth2TokenClient: AppleOAuth2TokenClient
): JwtExtractor {
    override fun extract(
        token: String
    ): AuthPayload = getClaims(token)
        .toAuthPayload()

    override fun getClaims(jwt: String): Claims {
        val appleOAuth2TokenHeader = parseJwtHeaders(jwt)
        val applePublicKey = getValidPublicKey(appleOAuth2TokenHeader)

        return Jwts.parserBuilder()
            .setSigningKey(generatePublicKey(applePublicKey))
            .build()
            .parseClaimsJws(jwt)
            .body
    }

    private fun parseJwtHeaders(
        jwt: String
    ): AppleOAuth2TokenHeader {
        val encodedHeader = jwt.split("\\.".toRegex()).getOrElse(0) { "" }
        val decodedHeader = String(Base64Utils.decodeFromUrlSafeString(encodedHeader))

        return objectMapper.readValue(decodedHeader, AppleOAuth2TokenHeader::class.java)
    }

    private fun generatePublicKey(
        applePublicKey: AppleOAuth2PublicKeyResponse.Key
    ): PublicKey {
        val publicKeyModulus = Base64Utils.decodeFromUrlSafeString(applePublicKey.modulus)
        val publicKeyExponent = Base64Utils.decodeFromUrlSafeString(applePublicKey.exponent)
        val publicKeySpec = RSAPublicKeySpec(BigInteger(1, publicKeyModulus), BigInteger(1, publicKeyExponent))

        return KeyFactory
            .getInstance(applePublicKey.keyType)
            .generatePublic(publicKeySpec)
    }

    private fun getValidPublicKey(
        appleOAuth2TokenHeader: AppleOAuth2TokenHeader
    ): AppleOAuth2PublicKeyResponse.Key {
        val applePublicKeys = appleOAuth2TokenClient.getApplePublicKeys()

        return applePublicKeys
            .keys
            .findLast{ (appleOAuth2TokenHeader.algorithm == it.algorithm) and (appleOAuth2TokenHeader.keyId == it.keyId) }!!
    }
}