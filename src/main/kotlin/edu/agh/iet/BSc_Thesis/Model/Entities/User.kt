package edu.agh.iet.BSc_Thesis.Model.Entities

import java.io.Serializable
import javax.persistence.*;

@Entity
@Table(name = "user")
data class User(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,
        @Column(unique = true)
        var username: String = "",
        var firstName: String = "",
        var lastName: String = "",
        var password: String = "",
        var isTeacher: Boolean = false
) : Serializable {
    constructor(username: String,
                password: String,
                firstName: String,
                lastName: String,
                isTeacher: Boolean = false)
            : this(id = -1,
            username = username,
            password = password,
            firstName = firstName,
            lastName = lastName,
            isTeacher = isTeacher)

}