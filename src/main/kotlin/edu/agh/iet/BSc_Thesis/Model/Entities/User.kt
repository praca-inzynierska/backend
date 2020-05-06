package edu.agh.iet.BSc_Thesis.Model.Entities

import javax.persistence.*;

@Entity
@Table(name = "user")
data class User(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,
        var username: String = "",
        var password: String = "",
        var isTeacher: Int = 0
)