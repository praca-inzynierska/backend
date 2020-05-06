package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.User
import edu.agh.iet.BSc_Thesis.Repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/login")
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository

    @CrossOrigin
    @RequestMapping(value = ["/**"], method = [RequestMethod.OPTIONS])
    fun corsHeaders(response: HttpServletResponse) {
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with")
        response.addHeader("Access-Control-Max-Age", "3600")
    }

    @CrossOrigin
    @PostMapping("")
    fun addTask(@RequestBody user: User): LoginResponse {
        userRepository.save(user)
        return LoginResponse("token" + user.username)
    }

}

data class LoginResponse (
        var token: String
)