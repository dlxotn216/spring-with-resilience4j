package io.taesu.resilience4jspring.user.domain

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by itaesu on 2024/02/05.
 *
 * @author Lee Tae Su
 * @version resilience4j-spring
 * @since resilience4j-spring
 */
@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @Column(name = "user_key", unique = true, nullable = false)
    val userKey: Long = 0L,
    @Column(name = "user_id", unique = true, nullable = false)
    val userId: String,
    @Column(name = "name", nullable = false)
    var name: String,
)

interface UserEntityRepository: JpaRepository<User, Long>
