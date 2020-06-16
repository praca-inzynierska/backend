package edu.agh.iet.BSc_Thesis.Model.Entities.School

import edu.agh.iet.BSc_Thesis.Model.Entities.User
import javax.persistence.*

@Entity
@Table(name = "teacher")
data class Teacher(
        @Id
        @OneToOne
        val user: User,
        @ElementCollection //TODO: introduce subjects
        val subjects: MutableList<String>
)