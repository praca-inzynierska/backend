package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.*
import edu.agh.iet.BSc_Thesis.Repositories.TaskSessionRepository
import edu.agh.iet.BSc_Thesis.Repositories.UserRepository
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.getUsername
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/taskSessions")
class TaskSessionController : BaseController() {

    @Autowired
    lateinit var taskSessionRepository: TaskSessionRepository

    @Autowired
    lateinit var userRepository: UserRepository


    @CrossOrigin
    @PostMapping("/create")
    fun addClassSession(@RequestBody taskSessionRequest: TaskSession, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        return if (JwtUtils.isTeacher(token)) {
            val user = JwtUtils.getUserFromToken(token)
            taskSessionRepository.save(taskSessionRequest)
            ResponseEntity(taskSessionRequest, HttpStatus.CREATED)
        } else ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @CrossOrigin
    @PostMapping("/{id}")
    fun editTaskSession(@PathVariable id: Long, @RequestBody newTaskSession: TaskSession, @RequestHeader("Token") token: String): ResponseEntity<HttpStatus> {
        return if (JwtUtils.isTeacher(token)) {
            val teacher = JwtUtils.getUserFromToken(token)
            taskSessionRepository.save(newTaskSession)
            ResponseEntity(HttpStatus.OK)
        } else ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun getClassSession(@PathVariable id: Long, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val user = JwtUtils.getUserFromToken(token)
        val taskSession = taskSessionRepository.getOne(id)
        return ResponseEntity(taskSession, HttpStatus.OK)
    }

    @CrossOrigin
    @GetMapping("")
    fun getClassSessions(@RequestHeader("Token") token: String): List<TaskSession> {
        val user = JwtUtils.getUserFromToken(token)
        return taskSessionRepository.findAll()
    }

}