package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.Task
import edu.agh.iet.BSc_Thesis.Repositories.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/tasks")
class TaskController {

    @Autowired
    lateinit var taskRepository: TaskRepository

    @CrossOrigin
    @RequestMapping(value = ["/**"], method = [RequestMethod.OPTIONS])
    fun corsHeaders(response: HttpServletResponse) {
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with")
        response.addHeader("Access-Control-Max-Age", "3600")
    }

    @CrossOrigin
    @PostMapping("/create")
    fun addTask(@RequestBody task: Task): String {
        taskRepository.save(task)
        return "success"
    }

    @CrossOrigin
    @PostMapping("/{id}")
    fun addTask(@PathVariable id: Long, @RequestBody task: Task): String {
        task.id = id
        taskRepository.save(task)
        return "success"
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long): Task {
        return taskRepository.getOne(id)
    }

    @CrossOrigin
    @GetMapping("")
    fun getTasks(): List<Task> {
        return taskRepository.findAll()
    }

}