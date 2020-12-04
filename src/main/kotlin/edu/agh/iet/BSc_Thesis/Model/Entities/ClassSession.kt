package edu.agh.iet.BSc_Thesis.Model.Entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import edu.agh.iet.BSc_Thesis.Model.Entities.School.Student
import edu.agh.iet.BSc_Thesis.Model.Entities.School.StudentResponse
import edu.agh.iet.BSc_Thesis.Model.Entities.School.Teacher
import edu.agh.iet.BSc_Thesis.Model.Entities.School.TeacherSimpleResponse
import javax.persistence.*


@Entity
@Table(name = "class_session")
data class ClassSession(

        @ManyToMany
        var students: MutableList<Student>,
        @OneToOne
        var teacher: Teacher,
        @OneToMany(cascade = [CascadeType.ALL])
        @JsonIgnoreProperties("classSession")
        var taskSessions: MutableList<TaskSession> = mutableListOf(),
        var startDate: Long,
        var endDate: Long,
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1
) {
    fun addTaskSession(taskSession: TaskSession) {
        taskSessions.add(taskSession)
    }

    fun deleteTaskSession(taskSession: TaskSession) {
        taskSessions.remove(taskSession)
    }

    fun simple(): ClassSessionSimpleResponse {
        return ClassSessionSimpleResponse(
                this.students.map { it.id }.toMutableList(),
                this.teacher.id,
                this.taskSessions.map { it.id }.toMutableList(),
                this.startDate,
                this.endDate,
                this.id
        )
    }

    fun response(): ClassSessionResponse {
        return ClassSessionResponse(
                this.students.map { it.response() }.toMutableList(),
                this.teacher.simple(),
                this.taskSessions,
                this.startDate,
                this.endDate,
                this.id
        )
    }

    fun hasMember(user: User): Boolean {
        return (this.students
                .map { it.user.id }
                .contains(user.id)
                || this.teacher.user.id == user.id)
    }
}

data class ClassSessionRequest(
        var students: MutableList<Long>,
        var startDate: Long,
        var endDate: Long
)

data class ClassSessionResponse(
        val students: MutableList<StudentResponse>,
        val teacher: TeacherSimpleResponse,
        val taskSessions: MutableList<TaskSession>,
        val startDate: Long,
        val endDate: Long,
        val id: Long
)

data class ClassSessionSimpleResponse(
        val students: MutableList<Long>,
        val teacher: Long,
        val taskSessions: MutableList<Long>,
        val startDate: Long,
        val endDate: Long,
        val id: Long
)