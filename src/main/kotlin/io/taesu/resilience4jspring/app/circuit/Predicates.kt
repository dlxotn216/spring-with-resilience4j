package io.taesu.resilience4jspring.app.circuit

import io.lettuce.core.RedisException
import io.taesu.resilience4jspring.app.exception.InvalidStateException
import org.springframework.dao.DataAccessException
import java.util.function.Predicate

/**
 * Created by itaesu on 2024/02/21.
 *
 * @author Lee Tae Su
 * @version resilience4j-spring
 * @since resilience4j-spring
 */
class DefaultIgnoreExceptionPredicate: Predicate<Throwable> {
    override fun test(throwable: Throwable): Boolean {
        return (throwable is InvalidStateException)
    }
}

class LettuceCacheIgnoreExceptionPredicate: Predicate<Throwable> {
    private val redisConnectionExceptionClass = listOf(
        org.springframework.dao.DataAccessException::class.java,    // QueryTimeoutException, RedisConnectionFailureException의 상위 클래스
        io.lettuce.core.RedisException::class.java,                 // RedisConnectionException, RedisCommandTimeoutException의 상위 클래스
    )

    override fun test(throwable: Throwable): Boolean {
        return redisConnectionExceptionClass.any { it.isInstance(throwable) }
    }
}
