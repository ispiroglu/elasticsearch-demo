package com.ispiroglu.esdemo.service

import com.ispiroglu.esdemo.entity.Mentor
import com.ispiroglu.esdemo.repository.MentorRepository
import io.github.serpro69.kfaker.Faker
import org.elasticsearch.common.lucene.search.function.CombineFunction
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.index.query.QueryBuilders.matchQuery
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate
import org.springframework.data.elasticsearch.core.SearchHits
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery.ScoreMode
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class MentorService(
    private val mentorRepository: MentorRepository,
    private val template: ElasticsearchRestTemplate,
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


//    @PostConstruct
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

    fun getMentorsByProfessionWithScore(query: String): SearchHits<Mentor> {

        val nativeQuery = NativeSearchQueryBuilder().withPageable(Pageable.ofSize(50)).withQuery(matchQuery("professions", query)).build()

        return template.search(nativeQuery, Mentor::class.java)
    }

    fun getMentorsByProfessionWithScoreByFieldFactor(factors: HashMap<String, Float>): SearchHits<Mentor> {

        val functionList: MutableList<FilterFunctionBuilder> = mutableListOf()
        for (factor in factors) {
            functionList.add(
                FunctionScoreQueryBuilder.FilterFunctionBuilder(
                    ScoreFunctionBuilders.fieldValueFactorFunction(factor.key).factor(factor.value).missing(1.0)
                )
            )
            println("factor -> $factor")
        }

        val professionList = factors.keys.toList().joinToString(" ")

        val query = QueryBuilders.functionScoreQuery(matchQuery("professions", professionList), functionList.toTypedArray())
            .scoreMode(ScoreMode.SUM).boostMode(CombineFunction.SUM);

        val search = NativeSearchQueryBuilder().withPageable(Pageable.ofSize(50)).withQuery(query).build()
        return template.search(search, Mentor::class.java)
    }
}