package com.ispiroglu.esdemo.repository

import com.ispiroglu.esdemo.entity.Mentor
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface MentorRepository: ElasticsearchRepository<Mentor, String> {

    @Query("{\"bool\": {\"must\": [{\"match\": {\"name\": \"?0\"}}]}}")
    fun getByQuery(query: String) : MutableIterable<Mentor>


    fun getMentorsByProfessionsContains(professions: List<String>) : MutableIterable<Mentor>
}