package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.*
import edu.agh.iet.BSc_Thesis.Repositories.*
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.getUsername
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.ZoneOffset

@RestController
@RequestMapping("/taskSessions")
class TaskSessionController : BaseController() {

    @CrossOrigin
    @PostMapping("/create")
    fun addTaskSession(@RequestBody taskSessionRequest: TaskSessionRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        return if (JwtUtils.isTeacher(token)) {
            val task = taskRepository.getOne(taskSessionRequest.taskId)
            val classSession = classSessionRepository.getOne(taskSessionRequest.classSessionId)
            val students = studentRepository.getAllByIdIn(taskSessionRequest.studentIds).toMutableList()
            val taskSession = TaskSession(
                    task,
                    students,
                    -1,
                    needsHelp = false,
                    readyToRate = false,
            deadline = LocalDateTime.now()
                    .plusMinutes(task.minutes)
                    .toEpochSecond(ZoneOffset.UTC))
            classSession.addTaskSession(taskSession)
            classSessionRepository.save(classSession)
            ResponseEntity(taskSession.response(), HttpStatus.CREATED)
        } else ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @CrossOrigin
    @PostMapping("/{id}")
    fun editTaskSession(@PathVariable id: Long, @RequestBody newTaskSession: TaskSession, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        return if (JwtUtils.isTeacher(token)) {
            taskSessionRepository.save(newTaskSession)
            ResponseEntity(newTaskSession.response(), HttpStatus.OK)
        } else ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun getTaskSession(@PathVariable id: Long, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val taskSession = taskSessionRepository.getOne(id)
        return ResponseEntity(taskSession.response(), HttpStatus.OK)
    }

    @CrossOrigin
    @GetMapping("")
    fun getTaskSessions(@RequestHeader("Token") token: String): ResponseEntity<List<TaskSessionResponse>> {
        return ResponseEntity(taskSessionRepository.findAll().map { it.response() }, HttpStatus.OK)
    }

}