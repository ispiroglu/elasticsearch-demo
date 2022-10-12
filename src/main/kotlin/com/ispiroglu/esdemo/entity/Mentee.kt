package com.ispiroglu.esdemo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
//
//@Document(indexName = "mentee") // Should
//data class Mentee(
//    @Id
//    val id: String,
//    @Field(name = "name", type = FieldType.Text)
//    val name: String,
//    @Field(name = "professions", type = FieldType.Text) // Auto kurtarır mı bizi ?
//    val professions: String,
//    @Field(name = "mentor", type = FieldType.Nested)
//    val mentor: Mentor
//)
