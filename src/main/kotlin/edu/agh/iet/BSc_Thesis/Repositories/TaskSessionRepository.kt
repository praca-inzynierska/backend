package edu.agh.iet.BSc_Thesis.Repositories

import edu.agh.iet.BSc_Thesis.Model.Entities.TaskSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskSessionRepository : JpaRepository<TaskSession, Long> {
//    fun getTaskSessionsByTeacher(teacherId: Long): List<TaskSession> {
//        return this.findAll()//.filter { taskSession -> taskSession. teacher == teacherId }
//    }
}