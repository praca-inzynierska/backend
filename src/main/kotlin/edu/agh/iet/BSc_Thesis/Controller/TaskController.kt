package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.Task
import edu.agh.iet.BSc_Thesis.Model.Entities.TaskRequest
import edu.agh.iet.BSc_Thesis.Repositories.TaskRepository
import edu.agh.iet.BSc_Thesis.Repositories.UserRepository
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.getClaimsFromToken
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.getUsername
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/tasks")
class TaskController : BaseController() {

    @CrossOrigin
    @PostMapping("/create")
    fun addTask(@RequestBody task: TaskRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val user = userRepository.getUserByUsername(getClaimsFromToken(token).getUsername())!!
        val teacher = teacherRepository.getTeacherByUser_Username(user.username)
        val taskToSave = Task(
                teacher,
                task.name,
                task.subject,
                task.description,
                task.tools,
                task.minutes,
                task.type
        )
        task.teacher = teacher!!.id
        taskRepository.save(taskToSave)
        return ResponseEntity(taskToSave.response(), CREATED)
    }

    @CrossOrigin
    @PostMapping("/{id}")
    fun editTask(
            @PathVariable id: Long,
            @RequestBody task: TaskRequest,
            @RequestHeader("Token") token: String)
            : ResponseEntity<Any> {
        val user = userRepository.getUserByUsername(getClaimsFromToken(token).getUsername())!!
        val teacher = teacherRepository.getTeacherByUser_Username(user.username)
        val editedTask = taskRepository.getOne(id)
        if (editedTask.teacher == teacher) {
            val newTeacher = teacherRepository.getOne(task.teacher)
            editedTask.description = task.description
            editedTask.minutes = task.minutes
            editedTask.name = task.name
            editedTask.subject = task.subject
            editedTask.teacher = newTeacher
            editedTask.tools = task.tools
            editedTask.type = task.type
            taskRepository.save(editedTask)
            return ResponseEntity(editedTask.response(), OK)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        return ResponseEntity(taskRepository.getOne(id).response(), OK)
    }

    @CrossOrigin
    @GetMapping("")
    fun getTasks(@RequestHeader("Token") token: String): ResponseEntity<Any> {
        val user = userRepository.getUserByUsername(getClaimsFromToken(token).getUsername())!!
        val teacher = teacherRepository.getTeacherByUser_Username(user.username)!!
        return ResponseEntity(taskRepository.getTasksByTeacher_Id(teacher.id).map { it.response() }, OK)
    }
}