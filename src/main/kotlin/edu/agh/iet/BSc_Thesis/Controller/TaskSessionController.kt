package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.Task
import edu.agh.iet.BSc_Thesis.Repositories.TaskSessionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class TaskSessionController : BaseController() {

    @Autowired
    lateinit var taskSessionRepository: TaskSessionRepository

    @CrossOrigin
    @PostMapping("/create")
    fun addTask(@RequestBody task: Task, @RequestHeader("Token") token: String): String {

    }

    @CrossOrigin
    @PostMapping("/{id}")
    fun addTask(@PathVariable id: Long, @RequestBody task: Task, @RequestHeader("Token") token: String): String {

    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long, @RequestHeader("Token") token: String): Task {

    }

    @CrossOrigin
    @GetMapping("")
    fun getTasks(@RequestHeader("Token") token: String): List<Task> {

    }
}