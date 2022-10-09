package com.ispiroglu.esdemo.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ProfessionRequest(
    @JsonProperty
    val professions: List<String>
)
