package edu.agh.iet.BSc_Thesis.Config

import edu.agh.iet.BSc_Thesis.Repositories.UserRepository
import edu.agh.iet.BSc_Thesis.Util.JwtUtils
import org.apache.catalina.core.ApplicationContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


@Component
class StaticContextInitializer {
    @Autowired
    private val userRepository: UserRepository? = null

    @PostConstruct
    fun init() {
        JwtUtils.userRepository = userRepository!!
    }
}