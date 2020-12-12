package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Controller.UserInfoResponse.Companion.toUserInfoResponse
import edu.agh.iet.BSc_Thesis.Model.Entities.School.Student
import edu.agh.iet.BSc_Thesis.Model.Entities.School.Teacher
import edu.agh.iet.BSc_Thesis.Model.Entities.User
import edu.agh.iet.BSc_Thesis.Repositories.StudentRepository
import edu.agh.iet.BSc_Thesis.Repositories.TeacherRepository
import edu.agh.iet.BSc_Thesis.Repositories.UserRepository
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.generateToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("")
class UserController : BaseController() {

    @CrossOrigin
    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): LoginResponse {
        val userToCreate: User = registerRequest.userFromRequest()
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
    fun login(@RequestBody loginRequest: LoginRequest): LoginResponse {
        val user = userRepository.getUserByUsernameAndPassword(username = loginRequest.username, password = loginRequest.password)
        val token = generateToken(user)
        return LoginResponse(token, "${user.firstName} ${user.lastName}", isTeacher(token))
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