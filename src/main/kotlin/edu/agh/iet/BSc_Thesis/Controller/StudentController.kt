package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.School.Student
import edu.agh.iet.BSc_Thesis.Model.Entities.School.StudentRequest
import edu.agh.iet.BSc_Thesis.Model.Entities.User
import edu.agh.iet.BSc_Thesis.Repositories.StudentRepository
import edu.agh.iet.BSc_Thesis.Repositories.UserRepository
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

class StudentController : BaseController() {

    @CrossOrigin
    @PostMapping("/create")
    fun addStudent(@RequestBody studentRequest: StudentRequest, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        if (JwtUtils.isTeacher(token)) {
            val user: User = userRepository.getOne(studentRequest.userId)
            val student = Student(user, mutableListOf())
            studentRepository.save(student)
            return ResponseEntity(student.response(), HttpStatus.OK)
        } else return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @CrossOrigin
    @PostMapping("/edit/{id}")
    fun editStudent(@PathVariable id: Long, @RequestBody newStudent: Student, @RequestHeader("Token") token: String): ResponseEntity<Any> {
        if (JwtUtils.isTeacher(token)) {
            studentRepository.save(newStudent)
            return ResponseEntity(newStudent.response(), HttpStatus.OK)
        } else return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun getStudent(@PathVariable id: Long, @RequestHeader("Token") token: String, @RequestParam simple: Boolean): ResponseEntity<Any> {
        return ResponseEntity(studentRepository.getOne(id).response(), HttpStatus.OK)
    }

    @CrossOrigin
    @GetMapping("")
    fun getStudents(@RequestHeader("Token") token: String): ResponseEntity<Any> {
        if (JwtUtils.isTeacher(token)) {
            return ResponseEntity(studentRepository.findAll().map { it.response() }, HttpStatus.OK)
        } else return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }
}