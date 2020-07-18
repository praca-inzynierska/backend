package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.Task
import edu.agh.iet.BSc_Thesis.Model.Entities.TaskRequest
import edu.agh.iet.BSc_Thesis.Repositories.TaskRepository
import edu.agh.iet.BSc_Thesis.Repositories.TeacherRepository
import edu.agh.iet.BSc_Thesis.Repositories.ToolRepository
import edu.agh.iet.BSc_Thesis.Repositories.UserRepository
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.getClaimsFromToken
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.getUsername
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.isTeacher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/tasks")
class TaskController : BaseController() {

    @Autowired
    lateinit var taskRepository: TaskRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var teacherRepository: TeacherRepository

    @Autowired
    lateinit var toolRepository: ToolRepository

    @CrossOrigin
    @PostMapping("/create")
    fun addTask(@RequestBody task: TaskRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val user = userRepository.getUserByUsername(getClaimsFromToken(token).getUsername())!!
        val teacher = teacherRepository.getTeacherByUser_Username(user.username)
        val tools = toolRepository.findAllById(task.tools)
        val taskToSave = Task(
                teacher,
                task.name,
                task.subject,
                task.description,
                tools,
                task.minutes,
                task.type
        )
        task.teacher = teacher!!.id
        taskRepository.save(taskToSave)
        return ResponseEntity(CREATED)
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
            val newTools = toolRepository.findAllById(task.tools)
            val newTeacher = teacherRepository.getOne(task.teacher)
            editedTask.description = task.description
            editedTask.minutes = task.minutes
            editedTask.name = task.name
            editedTask.subject = task.subject
            editedTask.teacher = newTeacher
            editedTask.tools = newTools
            editedTask.type = task.type
            taskRepository.save(editedTask)
            return ResponseEntity(OK)
        } else return ResponseEntity(UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long, @RequestHeader("Token") token: String): Task {
        return taskRepository.getOne(id)
    }

    @CrossOrigin
    @GetMapping("")
    fun getTasks(@RequestHeader("Token") token: String): List<Task> {
        val user = userRepository.getUserByUsername(getClaimsFromToken(token).getUsername())!!
        val teacher = teacherRepository.getTeacherByUser_Username(user.username)!!
        return taskRepository.getTasksByTeacher_Id(teacher.id)
    }
}