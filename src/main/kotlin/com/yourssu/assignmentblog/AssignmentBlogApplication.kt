package com.yourssu.assignmentblog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class AssignmentBlogApplication

fun main(args: Array<String>) {
    runApplication<AssignmentBlogApplication>(*args)
}
