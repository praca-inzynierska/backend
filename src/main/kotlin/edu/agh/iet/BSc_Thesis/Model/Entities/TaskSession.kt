package edu.agh.iet.BSc_Thesis.Model.Entities

import javax.persistence.*;

@Entity
@Table(name = "taskSession")
data class TaskSession(

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,
        var task: Long = -1,
        var classSession: Long = -1,
        @OneToMany                  //TODO change to oneToMany
        var students: MutableList<User>,
        var grade: Int = -1,
        var needsHelp: Boolean = false,
        var readyToRate: Boolean = false
)