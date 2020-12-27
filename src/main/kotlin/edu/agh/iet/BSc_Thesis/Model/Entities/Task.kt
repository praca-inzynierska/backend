package edu.agh.iet.BSc_Thesis.Model.Entities

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import edu.agh.iet.BSc_Thesis.Model.Entities.School.Teacher
import edu.agh.iet.BSc_Thesis.Model.Entities.School.TeacherSimpleResponse
import javax.persistence.*

@Entity
@Table(name = "task")
data class Task(
        @ManyToOne
        var teacher: Teacher? = null,
        var name: String = "",
        var subject: String? = "",
        @Lob
        var description: String = "",
        @ElementCollection
        var tools: MutableList<String>,
        var minutes: Long = -1,
        var type: String = "",
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1
) {
    override fun toString(): String {
        return "{name: ${this.name}"
    }

    fun response(): TaskResponse {
        return TaskResponse(
                this.teacher!!.simple(),
                this.name,
                this.subject,
                this.description,
                this.tools,
                this.minutes,
                this.type,
                this.id
        )
    }
}

data class TaskRequest(
        var teacher: Long = -1,
        var name: String = "",
        var subject: String? = "",
        var description: String = "",
        var tools: MutableList<String>,
        var minutes: Long = -1,
        var type: String = ""
)

data class TaskResponse(
        var teacher: TeacherSimpleResponse,
        var name: String,
        var subject: String?,
        var description: String,
        var tools: MutableList<String>,
        var minutes: Long = -1,
        var type: String = "",
        var id: Long
)