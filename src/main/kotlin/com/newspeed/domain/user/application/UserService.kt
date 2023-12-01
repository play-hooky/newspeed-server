package com.newspeed.domain.user.application

import com.newspeed.domain.auth.domain.OAuth2User
import com.newspeed.domain.user.domain.User
import com.newspeed.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) {

    fun saveIfNotExist(
        oAuth2User: OAuth2User
    ): User = userRepository.findByPlatformAndEmail(oAuth2User.platform, oAuth2User.email)
        ?: userRepository.save(oAuth2User.toUser())
}