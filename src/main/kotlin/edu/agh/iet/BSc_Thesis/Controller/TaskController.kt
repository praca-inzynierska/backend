package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.Task
import edu.agh.iet.BSc_Thesis.Repositories.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController {

    @Autowired
    lateinit var taskRepository: TaskRepository

    @PostMapping("/create")
    fun addTask(@RequestBody task: Task): String {
        taskRepository.save(task)
        return "success"
    }

    @PostMapping("/{id}")
    fun addTask(@PathVariable id: Long, @RequestBody task: Task): String {
        task.id = id
        taskRepository.save(task)
        return "success"
    }

    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long): Task {
        return taskRepository.getOne(id)
    }

    @GetMapping("")
    fun getTasks(): List<Task> {
        return taskRepository.findAll()
    }

}