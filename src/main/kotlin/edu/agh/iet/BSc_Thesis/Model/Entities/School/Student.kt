package edu.agh.iet.BSc_Thesis.Model.Entities.School

import edu.agh.iet.BSc_Thesis.Model.Entities.User
import javax.persistence.*

@Entity
@Table(name = "student")
data class Student(
        @Id
        @OneToOne
        val user: User,
        @ElementCollection
        val grades: MutableList<Long>
)

data class StudentRequest(
        val id: Long
)