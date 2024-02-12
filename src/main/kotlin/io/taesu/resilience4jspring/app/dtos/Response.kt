package io.taesu.resilience4jspring.app.dtos

/**
 * Created by itaesu on 2024/02/05.
 *
 * @author Lee Tae Su
 * @version resilience4j-spring
 * @since resilience4j-spring
 */
class SuccessResponse<T>(
    val result: T
)

class FailResponse(
    val errorCode: ErrorCode
)

enum class ErrorCode {
    ENTITY_NOT_FOUND
}

