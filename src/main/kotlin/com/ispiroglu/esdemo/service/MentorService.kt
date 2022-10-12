package com.ispiroglu.esdemo.service

import com.ispiroglu.esdemo.entity.Mentor
import com.ispiroglu.esdemo.repository.MentorRepository
import io.github.serpro69.kfaker.Faker
import org.elasticsearch.common.lucene.search.function.CombineFunction
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery.ScoreMode
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.index.query.QueryBuilders.matchQuery
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate
import org.springframework.data.elasticsearch.core.SearchHits
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service

@Service
class MentorService(
    private val mentorRepository: MentorRepository,
    private val template: ElasticsearchRestTemplate,
) {
    // This list can be retrieved from a db
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

    // This method is for generating dummy data on elasticsearch
    // @PostConstruct
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

    /*
    * matchQuery works like or operator. e.g. -> "go java microservice"
    * searching operation works like does 'profession' column have 'go' substring? OR have 'java' substring OR 'microservice' substring
    * There are some other like matchAll, matchPhrase.
    * in matchPhrase example -> searching operation will search all of 'go java microservice' substring on 'profession' column
    * */
    fun getMentorsByProfessionWithScore(query: String): SearchHits<Mentor> {
        val nativeQuery = NativeSearchQueryBuilder().withPageable(Pageable.ofSize(50)).withQuery(matchQuery("professions", query)).build()
        return template.search(nativeQuery, Mentor::class.java)
    }

    /*
    * for customizing the score method we need to build our query with fieldValueFactor
    * there are other scoring and boosting options such as MULTIPLY
    * */
    fun getMentorsByProfessionWithScoreByFieldFactor(factors: HashMap<String, Float>): SearchHits<Mentor> {
        val functionList: MutableList<FilterFunctionBuilder> = mutableListOf()
        for (factor in factors) {
            functionList.add(
                FunctionScoreQueryBuilder.FilterFunctionBuilder(
                    ScoreFunctionBuilders.fieldValueFactorFunction(factor.key).factor(factor.value).missing(1.0)
                )
            )
        }

        val professionList = factors.keys.toList().joinToString(" ")

        val query = QueryBuilders.functionScoreQuery(matchQuery("professions", professionList), functionList.toTypedArray()).scoreMode(ScoreMode.SUM)
            .boostMode(CombineFunction.SUM);

        val search = NativeSearchQueryBuilder().withPageable(Pageable.ofSize(50)).withQuery(query).build()
        return template.search(search, Mentor::class.java)
    }
}