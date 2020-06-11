package edu.agh.iet.BSc_Thesis.Model.Entities

import javax.persistence.*;

@Entity
@Table(name = "tool")
data class Tool(

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,
        var name: String = "",
        var type: String = "",
        var tag: String = ""
)