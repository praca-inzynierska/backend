package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.User
import edu.agh.iet.BSc_Thesis.Repositories.UserRepository
import edu.agh.iet.BSc_Thesis.Util.JwtUtils.generateToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
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
        return LoginResponse(generateToken(userToCreate))
    }

    @CrossOrigin
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): LoginResponse {
        val user = userRepository.getUserByUsernameAndPassword(username = loginRequest.username, password = loginRequest.password)
        return LoginResponse(generateToken(user))
    }

}

data class LoginResponse(
        var token: String
)

data class LoginRequest(
        var username: String,
        var password: String
)

data class RegisterRequest(
        var username: String,
        var password: String,
        var isTeacher: Boolean
) {
    fun userFromRequest(): User {
        return User(username, password, isTeacher)
    }
}