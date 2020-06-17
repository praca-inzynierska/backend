package edu.agh.iet.BSc_Thesis.Repositories

import edu.agh.iet.BSc_Thesis.Model.Entities.School.Student
import edu.agh.iet.BSc_Thesis.Model.Entities.School.Teacher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface TeacherRepository : JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
    fun getTeacherByUser_Username(username: String): Teacher?
}