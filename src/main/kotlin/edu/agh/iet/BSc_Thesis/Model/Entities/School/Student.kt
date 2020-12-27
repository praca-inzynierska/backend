package edu.agh.iet.BSc_Thesis.Model.Entities.School

import edu.agh.iet.BSc_Thesis.Model.Entities.User
import edu.agh.iet.BSc_Thesis.Model.Entities.UserResponse
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "student")
data class Student(

        @OneToOne(cascade = [CascadeType.ALL])
        val user: User,
        @ElementCollection
        val grades: MutableList<Long>,
        var schoolName: String = "",
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1
) : Serializable {
        fun response() : StudentResponse{
                return StudentResponse(
                        this.user.toUserResponse(),
                        this.grades,
                        this.schoolName,
                        this.id
                )
        }
}

data class StudentRequest(
        val userId: Long
)

data class StudentResponse(
        val user: UserResponse,
        val grades: MutableList<Long>,
        val schoolName: String,
        val id: Long
)