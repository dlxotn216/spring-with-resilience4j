package io.taesu.resilience4jspring

import io.taesu.resilience4jspring.user.domain.User
import io.taesu.resilience4jspring.user.domain.UserEntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.Transactional

@ConfigurationPropertiesScan
@SpringBootApplication
class Resilience4jSpringApplication: ApplicationRunner {
    @Autowired
    private lateinit var userEntityRepository: UserEntityRepository

    @Transactional
    override fun run(args: ApplicationArguments?) {
        userEntityRepository.save(
            User(
                userId = "taesu", name = "Lee Tae Su"
            )
        )
    }
}

fun main(args: Array<String>) {
    runApplication<Resilience4jSpringApplication>(*args)
}
