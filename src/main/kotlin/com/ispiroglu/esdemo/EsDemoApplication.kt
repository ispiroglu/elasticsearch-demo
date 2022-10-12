package com.ispiroglu.esdemo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.util.Arrays


@SpringBootApplication
//@EnableElasticsearchRepositories
class EsDemoApplication

fun main(args: Array<String>) {
    runApplication<EsDemoApplication>(*args)
}