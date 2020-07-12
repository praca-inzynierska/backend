package edu.agh.iet.BSc_Thesis.Model.Entities.School

import edu.agh.iet.BSc_Thesis.Model.Entities.User
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "student")
data class Student(

        @OneToOne
        val user: User,
        @ElementCollection
        val grades: MutableList<Long>,
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1
) : Serializable

data class StudentRequest(
        val userId: Long
)