package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.ClassSession
import edu.agh.iet.BSc_Thesis.Model.Entities.ClassSessionSpecifications
import edu.agh.iet.BSc_Thesis.Model.Entities.School.School
import edu.agh.iet.BSc_Thesis.Model.Entities.School.SchoolRequest
import edu.agh.iet.BSc_Thesis.Repositories.SchoolRepository
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/school")
class SchoolController : BaseController() {
    @Autowired
    lateinit var schoolRepository: SchoolRepository

    @CrossOrigin
    @PostMapping("/create")
    fun addSchool(@RequestBody schoolRequest: SchoolRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        if (JwtUtils.isTeacher(token)) {
            val school: School = School(schoolRequest.name, mutableListOf())
            schoolRepository.save(school)
            return ResponseEntity(school, OK)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @PostMapping("/edit/{id}")
    fun editSchool(@PathVariable id: Long, @RequestBody newSchool: School, @RequestHeader("Token") token: String): ResponseEntity<HttpStatus> {
        if (JwtUtils.isTeacher(token)) {
            schoolRepository.save(newSchool)
            return ResponseEntity(OK)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun getSchool(@PathVariable id: Long, @RequestHeader("Token") token: String, @RequestParam simple: Boolean): ResponseEntity<Any> {
        return ResponseEntity(schoolRepository.getOne(id), OK)
    }

    @CrossOrigin
    @GetMapping("")
    fun getSchools(@RequestHeader("Token") token: String): ResponseEntity<Any> {
        if (JwtUtils.isTeacher(token)) {
            return ResponseEntity(schoolRepository.findAll(), OK)
        } else return ResponseEntity(UNAUTHORIZED)
    }
}