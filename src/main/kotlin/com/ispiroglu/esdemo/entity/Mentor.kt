package com.ispiroglu.esdemo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType


@Document(indexName = "mentors") // Should
data class Mentor(
    @Id
    val id: String,
    @Field(name = "name", type = FieldType.Text)
    val name: String,
    @Field(name = "experience", type = FieldType.Text)
    val experience: Int,
    @Field(name = "professions", type = FieldType.Text) // Auto kurtarır mı bizi ?
    val professions: String
    // .net go java micros

//    @Field("mentees", type = FieldType.Nested)
//    val mentees: List<Mentee>
)
