package edu.agh.iet.BSc_Thesis.Model.Entities

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import edu.agh.iet.BSc_Thesis.Model.Entities.School.Teacher
import javax.persistence.*

@Entity
@Table(name = "task")
data class Task(
        @ManyToOne
        var teacher: Teacher? = null,
        var name: String = "",
        var subject: String? = "",
        var description: String = "",
        @OneToMany
        var tools: MutableList<Tool>,
        var minutes: Long = -1,
        var type: String = "",
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1
) {
    override fun toString(): String {
        return "{name: ${this.name}"
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