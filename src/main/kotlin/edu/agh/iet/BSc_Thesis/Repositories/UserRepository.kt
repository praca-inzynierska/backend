package edu.agh.iet.BSc_Thesis.Repositories

import edu.agh.iet.BSc_Thesis.Model.Entities.User
import org.springframework.data.domain.Example
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    @Nullable fun getUserByUsername(username: String): User?

    fun getUserByUsernameAndPassword(username: String, password: String): User

    fun getUserIdByUsername(username: String): Long

    fun getAllByIdIn(ids: List<Long>): List<User>
}
