package edu.agh.iet.BSc_Thesis.Repositories

import edu.agh.iet.BSc_Thesis.Model.Entities.User
import org.springframework.data.domain.Example
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun getUserByUsername(username: String): User {
        val userToFind: User = User(username = username)
        return this.findOne(Example.of(userToFind)).get()
    }

    fun getUserByUsernameAndPassword(username: String, password: String): User {
        val userToFind: User = User(username = username, password = password)
        return this.findOne(Example.of(userToFind)).get()
    }
}
