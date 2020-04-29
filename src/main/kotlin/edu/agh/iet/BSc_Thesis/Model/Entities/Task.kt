package edu.agh.iet.BSc_Thesis.Model.Entities

import javax.persistence.*;

@Entity
@Table(name = "task")
data class Task(

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,
        var teacher: Long = -1,
        var name: String = "",
        var subject: String? = "",
        var description: String = "",
        @ElementCollection
        var tools: MutableList<String?>?,
        var minutes: Long = -1,
        var type: String = ""
){
    override fun toString(): String{
        return "{name: ${this.name}";
    }
}