package io.taesu.resilience4jspring.user.service

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.taesu.resilience4jspring.user.domain.User
import io.taesu.resilience4jspring.user.domain.UserEntityRepository
import io.taesu.resilience4jspring.user.infra.UserCacheRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.log10
import kotlin.random.Random

/**
 * Created by itaesu on 2024/02/05.
 *
 * @author Lee Tae Su
 * @version resilience4j-spring
 * @since resilience4j-spring
 */
@Service
class UserRetrieveFacade(
    private val userCacheRepository: UserCacheRepository,
    private val userEntityRepository: UserEntityRepository,
) {
    fun retrieveEffectively(userKey: Long, expiryGap: Int): User {
        val ttl = userCacheRepository.ttl(userKey)
        val threshold = Random.nextInt(13) * expiryGap
        return if (ttl - threshold > 0) {
            return userCacheRepository.findById(userKey) ?: return retrieveAndPut(userKey)
        } else {
            retrieveAndPut(userKey)
        }
    }

    fun retrieveWithPer(userKey: Long, expiryGap: Int): User {
        val ttl = userCacheRepository.ttl(userKey)
        val timeToCompute = Duration.ofMillis(3500).toSeconds()
        val beta = 1
        val expiry = Duration.ofSeconds(600L).toSeconds()
        return if ((ttl - (timeToCompute * beta * log10(Random.nextDouble(0.0, 1.0)))) > expiry) {
            return userCacheRepository.findById(userKey) ?: return retrieveAndPut(userKey)
        } else {
            retrieveAndPut(userKey)
        }
    }

    @CircuitBreaker(name = "USERS", fallbackMethod = "retrieveFromOrigin")
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

class UserRetrieveResponse(
    val userKey: Long,
    val userId: String,
    val name: String,
) {
    companion object {
        fun from(user: User): UserRetrieveResponse {
            return UserRetrieveResponse(
                userKey = user.userKey,
                userId = user.userId,
                name = user.name,
            )
        }
    }
}

fun main() {
    println(30 * 60 * 1000)
    println(LocalDateTime.of(2024, 2, 9, 12, 0, 0).toInstant(ZoneOffset.ofHours(9)).toEpochMilli())
    println(LocalDateTime.of(2024, 2, 9, 12, 10, 0).toInstant(ZoneOffset.ofHours(9)).toEpochMilli())
    println(LocalDateTime.of(2024, 2, 9, 12, 29, 55).toInstant(ZoneOffset.ofHours(9)).toEpochMilli())
    println(LocalDateTime.of(2024, 2, 9, 12, 30, 0).toInstant(ZoneOffset.ofHours(9)).toEpochMilli())
    println(IntRange(1, 200).map { Random.nextDouble(0.0, 1.0) }.count { it <= 0.02 })

}
