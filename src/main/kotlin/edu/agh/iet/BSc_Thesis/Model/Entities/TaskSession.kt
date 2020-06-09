package edu.agh.iet.BSc_Thesis.Model.Entities

import javax.persistence.*;

@Entity
@Table(name = "taskSession")
data class TaskSession(

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,
        var task: Long = -1,
        @ElementCollection
        var students: MutableList<Long?>?,
        var grade: Int,
        var needsHelp: Boolean,
        var readyToRate: Boolean
)