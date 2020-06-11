package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.ClassSession
import edu.agh.iet.BSc_Thesis.Model.Entities.ClassSessionRequest
import edu.agh.iet.BSc_Thesis.Model.Entities.ClassSessionSpecifications.hasParticipantOfId
import edu.agh.iet.BSc_Thesis.Repositories.ClassSessionRepository
import edu.agh.iet.BSc_Thesis.Repositories.UserRepository
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.isTeacher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/classSessions")
class ClassSessionController : BaseController() {

    @Autowired
    lateinit var classSessionRepository: ClassSessionRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @CrossOrigin
    @PostMapping("/create")
    fun addClassSession(@RequestBody classSessionRequest: ClassSessionRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        if (isTeacher(token)) {
            val user = JwtUtils.getUserFromToken(token)
            val newClassSession = classSessionRequest.toNewClassSession(user.id)
            classSessionRepository.save(newClassSession)
            return ResponseEntity(newClassSession, CREATED)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @PostMapping("/{id}")
    fun editClassSession(@PathVariable id: Long, @RequestBody newClassSession: ClassSession, @RequestHeader("Token") token: String): ResponseEntity<HttpStatus> {
        if (isTeacher(token)) {
            val teacher = JwtUtils.getUserFromToken(token)
            val editedClassSession = classSessionRepository.getOne(id)
            if (editedClassSession.teacher == teacher.id) {
                classSessionRepository.save(newClassSession)
                return ResponseEntity(OK)
            } else return ResponseEntity(UNAUTHORIZED)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun getClassSession(@PathVariable id: Long, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val user = JwtUtils.getUserFromToken(token)
        val classSession = classSessionRepository.getOne(id)
        if (classSession.teacher == user.id || classSession.students.contains(user.id)){
            return ResponseEntity(classSession, OK)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("")
    fun getClassSessions(@RequestHeader("Token") token: String): List<ClassSession> {
        val user = JwtUtils.getUserFromToken(token)
        return classSessionRepository.findAll(hasParticipantOfId(user.id))
    }
}