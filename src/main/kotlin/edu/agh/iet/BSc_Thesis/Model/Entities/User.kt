package edu.agh.iet.BSc_Thesis.Model.Entities

import java.io.Serializable
import javax.persistence.*;

@Entity
@Table(name = "user")
data class User(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,
        var username: String = "",
        var password: String = "",
        var isTeacher: Boolean = false
) : Serializable {
        constructor(username: String,
                    password: String,
                    isTeacher: Boolean = false) : this(id = -1, username = username, password = password, isTeacher = isTeacher)
}