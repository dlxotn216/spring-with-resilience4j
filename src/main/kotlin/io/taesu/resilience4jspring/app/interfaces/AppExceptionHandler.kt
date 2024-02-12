package io.taesu.resilience4jspring.app.interfaces

import io.taesu.resilience4jspring.app.dtos.ErrorCode
import io.taesu.resilience4jspring.app.dtos.FailResponse
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Created by itaesu on 2024/02/05.
 *
 * @author Lee Tae Su
 * @version resilience4j-spring
 * @since resilience4j-spring
 */
@RestControllerAdvice
class AppExceptionHandler: ResponseEntityExceptionHandler() {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException::class)
    fun handle(e: EntityNotFoundException): FailResponse {
        return FailResponse(ErrorCode.ENTITY_NOT_FOUND)
    }
}
