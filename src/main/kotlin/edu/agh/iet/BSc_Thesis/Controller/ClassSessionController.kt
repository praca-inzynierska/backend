package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.ClassSession
import edu.agh.iet.BSc_Thesis.Model.Entities.ClassSessionRequest
import edu.agh.iet.BSc_Thesis.Model.Entities.ClassSessionResponse
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/classSessions")
class ClassSessionController : BaseController() {

    @CrossOrigin
    @PostMapping("/create")
    fun addClassSession(@RequestBody classSessionRequest: ClassSessionRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        if (isTeacher(token)) {
            val user = JwtUtils.getUserFromToken(token)
            val teacher = teacherRepository.getTeacherByUser_Username(user.username)!!
            val students = studentRepository.findAllById(classSessionRequest.students)
            val newClassSession = ClassSession(students, teacher, startDate = classSessionRequest.startDate, endDate = classSessionRequest.endDate)
            classSessionRepository.save(newClassSession)
            return ResponseEntity(newClassSession.response(), CREATED)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @PostMapping("/edit/{id}")
    fun editClassSession(@PathVariable id: Long, @RequestBody newClassSession: ClassSession, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        if (isTeacher(token)) {
            val user = JwtUtils.getUserFromToken(token)
            val teacher = teacherRepository.getTeacherByUser_Username(user.username)!!
            val editedClassSession = classSessionRepository.getOne(id)
            if (editedClassSession.teacher == teacher) {
                classSessionRepository.save(newClassSession)
                return ResponseEntity(newClassSession.response(), OK)
            } else return ResponseEntity(UNAUTHORIZED)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun getClassSession(@PathVariable id: Long, @RequestHeader("Token") token: String, @RequestParam simple: Boolean = false): ResponseEntity<Any> {
        val user = JwtUtils.getUserFromToken(token)
        val classSession = classSessionRepository.getOne(id)
        if (classSession.hasMember(user)) {
            return if (simple) ResponseEntity(classSession.simple(), OK)
            else ResponseEntity(classSession.response(), OK)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("")
    fun getClassSessions(@RequestHeader("Token") token: String): List<ClassSessionResponse> {
        val user = JwtUtils.getUserFromToken(token)
        return classSessionRepository.findAll().filter {
            it.hasMember(user)
        }.map { it.response() }
    }
}