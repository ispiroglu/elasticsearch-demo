package com.ispiroglu.esdemo.controller

import com.ispiroglu.esdemo.dto.CustomScoreProfessionRequest
import com.ispiroglu.esdemo.dto.ProfessionRequest
import com.ispiroglu.esdemo.entity.Mentor
import com.ispiroglu.esdemo.service.MentorService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/mentors")
class MentorController(
    private val mentorService: MentorService
) {
    @GetMapping("/profession")
    fun getByProfession(@RequestBody profession: ProfessionRequest) =
        mentorService.getMentorsByProfessionWithScore(profession.professions.joinToString(" "))

    @GetMapping("/profession/factor")
    fun getByProfessionByFactor(@RequestBody request: CustomScoreProfessionRequest) =
        mentorService.getMentorsByProfessionWithScoreByFieldFactor(request.factors)

    @GetMapping()
    fun getAll(): MutableIterable<Mentor> = mentorService.getAll()

    @GetMapping("/save")
    fun save() = mentorService.saveMentor()

    @GetMapping("/delete")
    fun delete() = mentorService.deleteAll()
}