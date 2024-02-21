package io.taesu.resilience4jspring.user.service

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.taesu.resilience4jspring.user.domain.User
import io.taesu.resilience4jspring.user.domain.UserEntityRepository
import io.taesu.resilience4jspring.user.infra.UserCacheRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant

/**
 * Created by itaesu on 2024/02/05.
 *
 * @author Lee Tae Su
 * @version resilience4j-spring
 * @since resilience4j-spring
 */
@Service
class UserRetrieveService(
    private val userCacheRepository: UserCacheRepository,
    private val userEntityRepository: UserEntityRepository,
) {
    // @CircuitBreaker(name = "USERS", fallbackMethod = "retrieveFromOrigin")
    @CircuitBreaker(name = "LETTUCE_CACHE", fallbackMethod = "retrieveFromOrigin")
    fun retrieve(userKey: Long): User {
        return userCacheRepository.findById(userKey) ?: retrieveAndPut(userKey)
    }

    private fun retrieveAndPut(userKey: Long): User {
        return retrieveFromOrigin(userKey).apply {
            userCacheRepository.save(this)
        }
    }

    fun retrieveFromOrigin(userKey: Long, e: Exception? = null): User {
        e?.let { log.warn("Failed to retrieve user from cache, fallback to origin", it) }
        return userEntityRepository.getReferenceById(userKey)
    }

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }
}
