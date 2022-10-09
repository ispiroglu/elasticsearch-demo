package com.ispiroglu.esdemo.service

import com.ispiroglu.esdemo.entity.Mentor
import com.ispiroglu.esdemo.repository.MentorRepository
import io.github.serpro69.kfaker.Faker
import org.elasticsearch.index.query.QueryBuilders.matchQuery
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate
import org.springframework.data.elasticsearch.core.SearchHits
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class MentorService(
    private val mentorRepository: MentorRepository,
    private val elasticsearchOperations: ElasticsearchOperations,
    private val template: ElasticsearchRestTemplate
) {

    private val professionList = mutableListOf<String>(
        "go",
        "java",
        "microservice",
        "kotlin",
        "docker",
        "kubernetes",
        "javascript",
        "design patterns",
        "agile",
        "scrum",
        "angular",
        "react",
        "linux",
        "devops",
        "ci-cd",
        "jenkins",
        "elastic",
        "postgre",
        "noSQL",
        "mongoDB",
        "mysql",
        "intellij",
        "REST",
        "GraphQL"
    )

    fun deleteAll() = mentorRepository.deleteAll()
    fun getAll(): MutableIterable<Mentor> = mentorRepository.findAll()

    fun getMentorByQuery(query: String): MutableIterable<Mentor> = mentorRepository.findAll()


    @PostConstruct
    fun saveMentor() {
        val faker = Faker()
        val mentors = mutableListOf<Mentor>()

        for (i in 1..60) mentors.add(faker.randomProvider.randomClassInstance<Mentor> {
            namedParameterGenerator("id") { faker.random.randomString() }
            namedParameterGenerator("name") { faker.name.name() }
            namedParameterGenerator("experience") { faker.random.nextInt(min = 0, max = 10) }
            namedParameterGenerator("professions") { faker.random.randomSublist(professionList, 6, true).joinToString(separator = " ") }
        })
        mentorRepository.saveAll(mentors)
    }

    fun getMentorByProfession(profession: List<String>) = mentorRepository.getMentorsByProfessionsContains(profession)


    // INFO
//    fun getMentorsByProfessionWithScore(query: String): SearchHits<Mentor> {
//        val nativeQuery = NativeSearchQueryBuilder().withQuery(matchQuery("professions", query)).build()
//
//        return elasticsearchOperations. search(
//            nativeQuery, Mentor::class.java, IndexCoordinates.of("mentors"))
//
//    }

    fun getMentorsByProfessionWithScore(query: String): SearchHits<Mentor> {

        val nativeQuery = NativeSearchQueryBuilder().withPageable(Pageable.ofSize(50)).withQuery(matchQuery("professions", query)).build()

//        return elasticsearchOperations. search(
//            nativeQuery, Mentor::class.java, IndexCoordinates.of("mentors"), ),



        return template.search(nativeQuery, Mentor::class.java)
    }
}