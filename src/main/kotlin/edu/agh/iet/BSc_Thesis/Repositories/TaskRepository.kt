package edu.agh.iet.BSc_Thesis.Repositories

import edu.agh.iet.BSc_Thesis.Model.Entities.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    fun getTasksByTeacher(teacherId: Long): List<Task> {
        return this.findAll().filter { task -> task.teacher!!.user.id == teacherId }
    }
}