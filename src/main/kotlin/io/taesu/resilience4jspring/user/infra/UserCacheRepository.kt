package io.taesu.resilience4jspring.user.infra

import io.taesu.resilience4jspring.user.domain.User
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

/**
 * Created by itaesu on 2024/02/05.
 *
 * @author Lee Tae Su
 * @version resilience4j-spring
 * @since resilience4j-spring
 */
interface UserCacheRepository {
    fun save(user: User, ttl: Duration = Duration.ofSeconds(600L))
    fun findById(userKey: Long): User?
    fun ttl(userKey: Long): Long
}

@Component
class UserCacheRepositoryRedisImpl(private val redisTemplate: RedisTemplate<String, Any>): UserCacheRepository {
    override fun save(user: User, ttl: Duration) {
        redisTemplate.opsForValue().set(resolveKey(user.userKey), user)
    }

    override fun findById(userKey: Long): User? {
        return redisTemplate.opsForValue().get(resolveKey(userKey)) as? User
    }

    override fun ttl(userKey: Long) = redisTemplate.getExpire(resolveKey(userKey))

    private fun resolveKey(userKey: Long) = "User:$userKey"
}
