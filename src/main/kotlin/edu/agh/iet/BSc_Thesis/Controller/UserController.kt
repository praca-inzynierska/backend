package edu.agh.iet.BSc_Thesis.Controller

import at.favre.lib.crypto.bcrypt.BCrypt
import edu.agh.iet.BSc_Thesis.Controller.UserInfoResponse.Companion.toUserInfoResponse
import edu.agh.iet.BSc_Thesis.Model.Entities.School.Student
import edu.agh.iet.BSc_Thesis.Model.Entities.School.Teacher
import edu.agh.iet.BSc_Thesis.Model.Entities.User
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.generateToken
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.http.HttpResponse


@RestController
@RequestMapping("")
class UserController : BaseController() {

    @CrossOrigin
    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): LoginResponse {
        val userToCreate: User = registerRequest.userFromRequest()
        userToCreate.password = BCrypt.withDefaults().hashToString(12, userToCreate.password.toCharArray())
//        userRepository.save(userToCreate)
        if (registerRequest.isTeacher) {
            val teacherToCreate = Teacher(userToCreate, mutableListOf())
            teacherRepository.save(teacherToCreate)
        } else {
            val studentToCreate = Student(userToCreate, mutableListOf())
            studentRepository.save(studentToCreate)
        }
        return LoginResponse(generateToken(userToCreate), userToCreate.username, registerRequest.isTeacher)
    }

    @CrossOrigin
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val password: String = BCrypt.withDefaults().hashToString(12, loginRequest.password.toCharArray())
        val user = userRepository.getUserByUsername(username = loginRequest.username)
        if (user != null && BCrypt.verifyer().verify(loginRequest.password.toCharArray(), user.password).verified) {
            val token = generateToken(user)
            return ResponseEntity(LoginResponse(token, "${user.firstName} ${user.lastName}", isTeacher(token)), HttpStatus.OK)
        } else return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @CrossOrigin
    @GetMapping("/users/info")
    fun userInfo(@RequestHeader("Token") token: String): UserInfoResponse {
        val user = JwtUtils.getUserFromToken(token)
        return user.toUserInfoResponse()
    }

}

data class LoginResponse(
        var token: String,
        var username: String,
        var isTeacher: Boolean
)

data class UserInfoResponse(
        var username: String
) {
    companion object {
        fun User.toUserInfoResponse(): UserInfoResponse {
            return UserInfoResponse(this.username)
        }
    }
}

data class LoginRequest(
        var username: String,
        var password: String
)

data class RegisterRequest(
        var username: String,
        var password: String,
        var firstName: String,
        var lastName: String,
        var isTeacher: Boolean
) {
    fun userFromRequest(): User {
        return User(username, firstName, lastName, password)
    }
}