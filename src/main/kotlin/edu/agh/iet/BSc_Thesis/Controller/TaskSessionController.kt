package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.*
import edu.agh.iet.BSc_Thesis.Repositories.*
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/taskSessions")
class TaskSessionController : BaseController() {

    @Autowired
    lateinit var taskSessionRepository: TaskSessionRepository

    @Autowired
    lateinit var classSessionRepository: ClassSessionRepository

    @Autowired
    lateinit var taskRepository: TaskRepository

    @Autowired
    lateinit var studentRepository: StudentRepository

    @Autowired
    lateinit var toolStateRepository: ToolStateRepository

    @CrossOrigin
    @PostMapping("/create")
    fun addTaskSession(@RequestBody taskSessionRequest: TaskSessionRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        return if (JwtUtils.isTeacher(token)) {
            val task = taskRepository.getOne(taskSessionRequest.taskId)
            val classSession = classSessionRepository.getOne(taskSessionRequest.classSessionId)
            val students = studentRepository.getAllByIdIn(taskSessionRequest.studentIds).toMutableList()
            val taskSession = TaskSession(
                    task,
                    classSession,
                    students,
                    -1,
                    needsHelp = false,
                    readyToRate = false
                    )
            if(task.tools.contains("whiteboard")) {
                val toolState = ToolState(
                        taskSession,
                        "wb-" + taskSession.id.toString(),          // + hash
                        "whiteboard"
                )
                toolStateRepository.save(toolState)
            }
            taskSessionRepository.save(taskSession)
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

    @CrossOrigin
    @PostMapping("/{id}/create/tool_state/{name}")      // tworzy
    fun addToolState(@PathVariable id: Long, @PathVariable name: String, @RequestBody toolStateRequest: ToolStateRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val taskSession = taskSessionRepository.getOne(id)
//        val status = toolStateRepository.getOne(toolStateRequest.status)
//        val name = ""
        val toolState = ToolState(
                taskSession,
//                status,
                name
        )
        toolStateRepository.save(toolState)
        return ResponseEntity(toolState.response(), HttpStatus.CREATED)
    }

    @CrossOrigin
    @GetMapping("/{id}/tool_state/{type}")
    fun getToolState(@PathVariable id: Long, @PathVariable type: String, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val taskSession = taskSessionRepository.getOne(id)
        val toolStates = toolStateRepository.getAllByTaskSessionID(id)

        if(type.equals("whiteboard")) {
            for(toolState in toolStates) {
                if(toolState.type.equals("whiteboard")) {
                    return ResponseEntity(toolState.response(), HttpStatus.OK)
                }
            }
        }

        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @CrossOrigin
    @GetMapping("/{id}/tool_state")
    fun getToolStates(@PathVariable id: Long, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val taskSession = taskSessionRepository.getOne(id)
        val toolStates = toolStateRepository.getAllByTaskSessionID(id)

        return ResponseEntity(toolStates.map { it.response() }, HttpStatus.OK)

        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

}