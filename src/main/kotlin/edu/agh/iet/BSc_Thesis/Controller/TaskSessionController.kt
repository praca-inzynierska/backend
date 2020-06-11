package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.Task
import edu.agh.iet.BSc_Thesis.Model.Entities.TaskSession
import edu.agh.iet.BSc_Thesis.Repositories.TaskSessionRepository
import edu.agh.iet.BSc_Thesis.Repositories.UserRepository
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.getUsername
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("")
class TaskSessionController : BaseController() {

    @Autowired
    lateinit var taskSessionRepository: TaskSessionRepository

    @Autowired
    lateinit var userRepository: UserRepository


    @CrossOrigin
    @PostMapping("/create")
    fun addTask(@RequestBody task: Task, @RequestHeader("Token") token: String): String {

    }

    @CrossOrigin
    @PostMapping("/{id}")
    fun addTask(@PathVariable id: Long, @RequestBody task: Task, @RequestHeader("Token") token: String): String {

    }

    @CrossOrigin
    @GetMapping("/taskSession/{id}")
    fun getTaskSession(@PathVariable id: Long, @RequestHeader("Token") token: String): TaskSession {
        return taskSessionRepository.getOne(id)
    }

    @CrossOrigin
    @GetMapping("/taskSessions")
    fun getTaskSessions(@PathVariable id: Long, @RequestHeader("Token") token: String): List<TaskSession> {
        val user = userRepository.getUserByUsername(JwtUtils.getClaimsFromToken(token).getUsername())!!
        return taskSessionRepository.getTaskSessionsByTeacher(user.id)
    }
}