package edu.agh.iet.BSc_Thesis.Model.Entities

import edu.agh.iet.BSc_Thesis.Model.Entities.School.Teacher
import javax.persistence.*;

@Entity
@Table(name = "task")
data class Task(

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,
        @ManyToOne
        var teacher: Teacher? = null,
        var name: String = "",
        var subject: String? = "",
        var description: String = "",
        @OneToMany
        var tools: MutableList<Tool>,
        var minutes: Long = -1,
        var type: String = ""
){
    override fun toString(): String{
        return "{name: ${this.name}";
    }
}