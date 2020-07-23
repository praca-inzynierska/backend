package edu.agh.iet.BSc_Thesis.Model.Entities.School

import edu.agh.iet.BSc_Thesis.Controller.UserInfoResponse
import edu.agh.iet.BSc_Thesis.Model.Entities.User
import edu.agh.iet.BSc_Thesis.Model.Entities.UserResponse
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "teacher")
data class Teacher(
        @OneToOne(cascade = [CascadeType.ALL])
        val user: User,
        @ElementCollection //TODO: introduce subjects
        val subjects: MutableList<String>,
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1
) : Serializable {
        fun simple(): TeacherSimpleResponse{
                return TeacherSimpleResponse(user.toUserResponse())
        }
}

data class TeacherSimpleResponse(
        val user: UserResponse
)