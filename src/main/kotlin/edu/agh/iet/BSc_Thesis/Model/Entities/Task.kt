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
        @OneToMany
        var tools: List<Tool> = emptyList(),
        var minutes: Long = -1,
        var type: String = ""
){
    override fun toString(): String{
        return "{name: ${this.name}";
    }

    @Entity         //to refactor  - to new class etc
    data class Tool(
            @Id
            val id: String
    )
}