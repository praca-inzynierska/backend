package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.*
import edu.agh.iet.BSc_Thesis.Repositories.ToolStateRepository
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.ZoneOffset

@RestController
@RequestMapping("/taskSessions")
class TaskSessionController : BaseController() {

    @Autowired
    lateinit var toolStateRepository: ToolStateRepository

    @CrossOrigin
    @PostMapping("/create")
    fun addTaskSession(@RequestBody taskSessionRequest: TaskSessionRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        return if (isTeacher(token)) {
            val task = taskRepository.getOne(taskSessionRequest.taskId)
            val classSession = classSessionRepository.getOne(taskSessionRequest.classSessionId)
//            val students = studentRepository.getAllByIdIn(taskSessionRequest.studentIds).toMutableList()
            val students = classSession.students
            val taskSession = TaskSession(
                    task,
                    students,
                    classSession,
                    -1,
                    needsHelp = false,
                    readyToRate = false,
                    deadline = LocalDateTime.now()                          // TODO: naprawic to
                            .plusMinutes(100)
                            .toEpochSecond(ZoneOffset.UTC)
            )
            if(task.tools.contains("whiteboard")) {
                val toolState = ToolState(
                        taskSession,
                        "",
                        taskSession.id.toString(),          // + hash
                        "whiteboard"
                )
                toolStateRepository.save(toolState)
            }
            classSession.addTaskSession(taskSession)
            classSessionRepository.save(classSession)
            taskSessionRepository.save(taskSession)
            ResponseEntity(taskSession.response(), HttpStatus.CREATED)
        } else ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @CrossOrigin
    @PostMapping("/{id}")
    fun editTaskSession(@PathVariable id: Long, @RequestBody newTaskSession: TaskSession, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val user = JwtUtils.getUserFromToken(token)
        val oldTaskSession = taskSessionRepository.getOne(id)
        return if (isTeacher(token) && oldTaskSession.hasMember(user)) {
            taskSessionRepository.save(newTaskSession)
            ResponseEntity(newTaskSession.response(), HttpStatus.OK)
        } else ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("/delete/{id}")
    fun deleteTaskSession(@PathVariable id: Long, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val user = JwtUtils.getUserFromToken(token)
        val taskSessionToDelete = taskSessionRepository.getOne(id)
        return if (isTeacher(token) && taskSessionToDelete.hasMember(user)) {
            taskSessionToDelete.classSession.deleteTaskSession(taskSessionToDelete)
            classSessionRepository.save(taskSessionToDelete.classSession)
            taskSessionRepository.delete(taskSessionToDelete)
            ResponseEntity(HttpStatus.OK)
        } else ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun getTaskSession(@PathVariable id: Long, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val taskSession = taskSessionRepository.getOne(id)
        return if (taskSession.hasMember(JwtUtils.getUserFromToken(token)))
            ResponseEntity(taskSession.response(), HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @CrossOrigin
    @GetMapping("")
    fun getTaskSessions(@RequestHeader("Token") token: String): ResponseEntity<List<TaskSessionResponse>> {
        var user = JwtUtils.getUserFromToken(token)
        return ResponseEntity(taskSessionRepository.findAll()
                .filter { taskSession -> taskSession.hasMember(user) }
                .map { it.response() }, HttpStatus.OK)
    }

    @CrossOrigin
    @GetMapping("/{id}/tool_state/{type}")      // get tool_state
    fun getToolStates(@PathVariable id: Long, @PathVariable type: String, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val taskSession = taskSessionRepository.getOne(id)
        val toolStates = toolStateRepository.findAll()

        for(toolState in toolStates) {
            var tsId = toolState.taskSessionId
            if (tsId != null) {
                if (tsId.id == id && toolState.type == type) {
                    return ResponseEntity(toolState, HttpStatus.OK)
                }
            }
        }
        if (toolStates.filter{ toolState -> toolState.type == "chat"  }.none { toolState -> toolState.id == id }) {
            return ResponseEntity(ToolState(taskSessionId = taskSession, status =  "[]", name = type, type = type), HttpStatus.OK)
        }
        if(toolStates.isEmpty()) {
            return ResponseEntity(ToolState(taskSessionId = taskSession, status =  "[]", name = type, type = type), HttpStatus.OK)
        }

        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @CrossOrigin
    @PostMapping("/{id}/tool_state/{type}")      // create and update tool_state
    fun addToolState(@PathVariable id: Long, @PathVariable type: String, @RequestBody toolStateRequest: ToolStateRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val taskSession = taskSessionRepository.getOne(id)
        val status = toolStateRequest.status
        val toolState = ToolState(
                taskSession,
                status,
                toolStateRequest.name,
                toolStateRequest.type
        )

        val toolStates = toolStateRepository.findAll()
        for (ts in toolStates) {
            var tsId = ts.taskSessionId
            if (tsId != null) {
                if(tsId.id == id && ts.type == type){
                    val toolStateExact = ts
                    toolStateRepository.delete(toolStateExact)
                }
            }
        }
        toolStateRepository.save(toolState)
        return ResponseEntity(HttpStatus.CREATED)
    }

}