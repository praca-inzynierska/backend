package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Task
import org.springframework.web.bind.annotation.*
import kotlin.random.Random

@RestController
@RequestMapping("/tasks")
class TaskController {

    val taskStores = mutableMapOf<Long, Task>()

    @PostMapping("/create")
    fun addTask(@RequestBody task: Task): String {
        task.id = 1
        return "success" + task.id
    }

    @PostMapping("/{id}")
    fun addTask(@PathVariable id: Long, @RequestBody task: Task): String {
        task.id = id
        taskStores[task.id]
        return "success"
    }

    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long): Task? {
        return taskStores[id]
    }

    @GetMapping("/")
    fun getTasks(): MutableMap<Long, Task> {
        return taskStores
    }

}