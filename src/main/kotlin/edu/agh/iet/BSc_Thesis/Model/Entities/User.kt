package edu.agh.iet.BSc_Thesis.Model.Entities

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
        @Column(unique = true)
        var username: String = "",
        var firstName: String = "",
        var lastName: String = "",
        var password: String = "",
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1
) : Serializable