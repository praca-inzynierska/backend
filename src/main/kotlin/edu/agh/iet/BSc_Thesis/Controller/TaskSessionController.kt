package edu.agh.iet.BSc_Thesis.Controller

import com.fasterxml.jackson.databind.util.JSONPObject
import edu.agh.iet.BSc_Thesis.Model.Entities.*
import edu.agh.iet.BSc_Thesis.Repositories.*
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
    override lateinit var taskSessionRepository: TaskSessionRepository

    @Autowired
    override lateinit var classSessionRepository: ClassSessionRepository

    @Autowired
    override lateinit var taskRepository: TaskRepository

    @Autowired
    override lateinit var studentRepository: StudentRepository

    @Autowired
    lateinit var toolStateRepository: ToolStateRepository

    @CrossOrigin
    @PostMapping("/create")
    fun addTaskSession(@RequestBody taskSessionRequest: TaskSessionRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        return if (JwtUtils.isTeacher(token)) {
            val task = taskRepository.getOne(taskSessionRequest.taskId)
            val classSession = classSessionRepository.getOne(taskSessionRequest.classSessionId)
//            val students = studentRepository.getAllByIdIn(taskSessionRequest.studentIds).toMutableList()
            val students = classSession.students
            val taskSession = TaskSession(
                    task,
                    classSession,
                    students,
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

//    @CrossOrigin
//    @PostMapping("/{id}/create/tool_state/{type}/{name}")      // tworzy
//    fun addToolState(@PathVariable id: Long, @PathVariable type: String, @PathVariable name: String, @RequestBody toolStateRequest: ToolStateRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
//        val taskSession = taskSessionRepository.getOne(id)
////        val status = toolStateRepository.getOne(toolStateRequest.status)
////        val name = ""
//        val toolState = ToolState(
//                taskSession,
////                status,
//                name,
//                type
//        )
//        toolStateRepository.save(toolState)
//        return ResponseEntity(toolState.response(), HttpStatus.CREATED)
//    }

//    @CrossOrigin
//    @GetMapping("/{id}/tool_state/{type}")
//    fun getToolState(@PathVariable id: Long, @PathVariable type: String, @RequestHeader("Token") token: String): ResponseEntity<Any> {
//        val taskSession = taskSessionRepository.getOne(id)
//        val toolStates = toolStateRepository.getAllByTaskSessionId(id)
//
//        if(type.equals("whiteboard")) {
//            for(toolState in toolStates) {
//                if(toolState.type.equals("whiteboard")) {
//                    return ResponseEntity(toolState.response(), HttpStatus.OK)
//                }
//            }
//        }
//
//        return ResponseEntity(HttpStatus.NOT_FOUND)
//    }
//
    @CrossOrigin
    @GetMapping("/{id}/tool_state")
    fun getToolStates(@PathVariable id: Long, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val taskSession = taskSessionRepository.getOne(id)
        val toolStates = toolStateRepository.getAllByTaskSessionId(id)

        return ResponseEntity(toolStates.map { it.response() }, HttpStatus.OK)

        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @CrossOrigin
    @PostMapping("/{id}/tool_state/{type}")      // tworzy
    fun addToolState(@PathVariable id: Long, @PathVariable type: String, @RequestBody toolStateRequest: ToolStateRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        val taskSession = taskSessionRepository.getOne(id)
        println(toolStateRequest.status)
        val status = toolStateRequest.status
//        val name = ""
        val toolState = ToolState(
                taskSession,
                status,
                toolStateRequest.name,
                toolStateRequest.type
        )

        val tmp = toolStateRepository.findAll()
        for (tmps in tmp) {
            var tid = tmps.taskSessionId
            if (tid != null) {
                if(tid.id == id){
                    val toolStateExact = tmps
                    toolStateRepository.delete(toolStateExact)
                }
            }
        }
        toolStateRepository.save(toolState)
        return ResponseEntity(HttpStatus.CREATED)
    }

}