package io.taesu.resilience4jspring.user.interfaces

import io.taesu.resilience4jspring.app.dtos.SuccessResponse
import io.taesu.resilience4jspring.user.service.UserRetrieveFacade
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * Created by itaesu on 2024/02/05.
 *
 * @author Lee Tae Su
 * @version resilience4j-spring
 * @since resilience4j-spring
 */
@RestController
class UserRetrieveController(private val userRetrieveFacade: UserRetrieveFacade) {
    @GetMapping("/api/v1/users/{userKey}")
    fun retrieveUser(@PathVariable userKey: Long) = SuccessResponse(userRetrieveFacade.retrieve(userKey))
}
