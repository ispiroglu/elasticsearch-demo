package com.ispiroglu.esdemo.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CustomScoreProfessionRequest(
    @JsonProperty
    val factors: HashMap<String, Float>
)
