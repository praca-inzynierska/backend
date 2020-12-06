package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.School.School
import edu.agh.iet.BSc_Thesis.Model.Entities.School.SchoolRequest
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/school")
class SchoolController : BaseController() {

    @CrossOrigin
    @PostMapping("/create")
    fun addSchool(@RequestBody schoolRequest: SchoolRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        if (isTeacher(token)) {
            val school = School(schoolRequest.name, mutableListOf())
            schoolRepository.save(school)
            return ResponseEntity(school.response(), OK)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @PostMapping("/edit/{id}")
    fun editSchool(@PathVariable id: Long, @RequestBody newSchool: School, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        if (isTeacher(token)) {
            schoolRepository.save(newSchool)
            return ResponseEntity(newSchool.response(), OK)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun getSchool(@PathVariable id: Long, @RequestHeader("Token") token: String, @RequestParam simple: Boolean): ResponseEntity<Any> {
        return ResponseEntity(schoolRepository.getOne(id).response(), OK)
    }

    @CrossOrigin
    @GetMapping("")
    fun getSchools(@RequestHeader("Token") token: String): ResponseEntity<Any> {
        if (isTeacher(token)) {
            return ResponseEntity(schoolRepository.findAll().map { it.response() }, OK)
        } else return ResponseEntity(UNAUTHORIZED)
    }
}