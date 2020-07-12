package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.ClassSession
import edu.agh.iet.BSc_Thesis.Model.Entities.ClassSessionRequest
import edu.agh.iet.BSc_Thesis.Model.Entities.ClassSessionSpecifications.hasParticipantOfId
import edu.agh.iet.BSc_Thesis.Repositories.ClassSessionRepository
import edu.agh.iet.BSc_Thesis.Repositories.TeacherRepository
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

    @Autowired
    lateinit var teacherRepository: TeacherRepository

    @CrossOrigin
    @PostMapping("/create")
    fun addClassSession(@RequestBody classSessionRequest: ClassSessionRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        if (isTeacher(token)) {
            val user = JwtUtils.getUserFromToken(token)
            val teacher = teacherRepository.getTeacherByUser_Username(user.username)!!
            val students = userRepository.findAllById(classSessionRequest.students)
            val newClassSession = ClassSession(students, teacher, startDate = classSessionRequest.startDate, endDate = classSessionRequest.endDate)
            classSessionRepository.save(newClassSession)
            return ResponseEntity(newClassSession, CREATED)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @PostMapping("/edit/{id}")
    fun editClassSession(@PathVariable id: Long, @RequestBody newClassSession: ClassSession, @RequestHeader("Token") token: String): ResponseEntity<HttpStatus> {
        if (isTeacher(token)) {
            val user = JwtUtils.getUserFromToken(token)
            val teacher = teacherRepository.getTeacherByUser_Username(user.username)!!
            val editedClassSession = classSessionRepository.getOne(id)
            if (editedClassSession.teacher == teacher) {
                classSessionRepository.save(newClassSession)
                return ResponseEntity(OK)
            } else return ResponseEntity(UNAUTHORIZED)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun getClassSession(@PathVariable id: Long, @RequestHeader("Token") token: String, @RequestParam simple: Boolean): ResponseEntity<Any> {
        val user = JwtUtils.getUserFromToken(token)
        val teacher = teacherRepository.getTeacherByUser_Username(user.username)!!
        val classSession = classSessionRepository.getOne(id)
        if (classSession.teacher == teacher || classSession.students.contains(user)) {
            return if (simple) ResponseEntity(classSession.simple(), OK)
            else ResponseEntity(classSession, OK)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("")
    fun getClassSessions(@RequestHeader("Token") token: String): List<ClassSession> {
        val user = JwtUtils.getUserFromToken(token)
        return classSessionRepository.findAll(hasParticipantOfId(user.id))
    }
}