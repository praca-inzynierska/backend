package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.Task
import edu.agh.iet.BSc_Thesis.Repositories.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/tasks")
class TaskController : BaseController() {

    @Autowired
    lateinit var taskRepository: TaskRepository


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
    fun getTasks(@RequestHeader("Token") token: String): List<Task> {
        print(token)
        return taskRepository.findAll()
    }

}