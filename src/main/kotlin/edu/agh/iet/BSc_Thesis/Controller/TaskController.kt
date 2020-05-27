package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.Task
import edu.agh.iet.BSc_Thesis.Repositories.TaskRepository
import edu.agh.iet.BSc_Thesis.Repositories.UserRepository
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.getClaimsFromToken
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.getUsername
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.validateToken
import io.jsonwebtoken.Claims
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/tasks")
class TaskController : BaseController() {

    @Autowired
    lateinit var taskRepository: TaskRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @CrossOrigin
    @PostMapping("/create")
    fun addTask(@RequestBody task: Task, @RequestHeader("Token") token: String): String {
        val user = userRepository.getUserByUsername(getClaimsFromToken(token).getUsername())
        task.teacher = user.id
        taskRepository.save(task)
        return "success"
    }

    @CrossOrigin
    @PostMapping("/{id}")
    fun addTask(@PathVariable id: Long, @RequestBody task: Task, @RequestHeader("Token") token: String): String {
        task.id = id
        val user = userRepository.getUserByUsername(getClaimsFromToken(token).getUsername())
        task.teacher = user.id
        taskRepository.save(task)
        return "success"
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long, @RequestHeader("Token") token: String): Task {
        return taskRepository.getOne(id)
    }

    @CrossOrigin
    @GetMapping("")
    fun getTasks(@RequestHeader("Token") token: String): List<Task> {
        val user = userRepository.getUserByUsername(getClaimsFromToken(token).getUsername())
        return taskRepository.getTasksByTeacher(user.id)
    }
}