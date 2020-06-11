package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Controller.UserInfoResponse.Companion.toUserInfoResponse
import edu.agh.iet.BSc_Thesis.Model.Entities.User
import edu.agh.iet.BSc_Thesis.Repositories.UserRepository
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.generateToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("")
class UserController : BaseController() {

    @Autowired
    lateinit var userRepository: UserRepository

    @CrossOrigin
    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): LoginResponse {
        val userToCreate: User = registerRequest.userFromRequest()
        userRepository.save(userToCreate)
        return LoginResponse(generateToken(userToCreate), userToCreate.username)
    }

    @CrossOrigin
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): LoginResponse {
        val user = userRepository.getUserByUsernameAndPassword(username = loginRequest.username, password = loginRequest.password)
        return LoginResponse(generateToken(user), "${user.firstName} ${user.lastName}")
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
        var username: String
)

data class UserInfoResponse(
        var username: String,
        var isTeacher: Boolean
) {
    companion object {
        fun User.toUserInfoResponse(): UserInfoResponse {
            return UserInfoResponse(this.username, this.isTeacher)
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
        return User(username, password, firstName, lastName, isTeacher)
    }
}