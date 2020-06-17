package edu.agh.iet.BSc_Thesis.Model.Entities.School

import edu.agh.iet.BSc_Thesis.Model.Entities.User
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "teacher")
data class Teacher(
        @OneToOne
        val user: User,
        @ElementCollection //TODO: introduce subjects
        val subjects: MutableList<String>,
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1
) : Serializable