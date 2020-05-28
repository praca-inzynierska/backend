package edu.agh.iet.BSc_Thesis.Config

import edu.agh.iet.BSc_Thesis.Controller.AuthInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter


@Component
class InterceptorConfig : WebMvcConfigurerAdapter() {
    @Autowired
    var authInterceptor: AuthInterceptor? = null
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor as HandlerInterceptor)
    }
}