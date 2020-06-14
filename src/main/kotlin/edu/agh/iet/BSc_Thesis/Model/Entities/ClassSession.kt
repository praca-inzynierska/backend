package edu.agh.iet.BSc_Thesis.Model.Entities

import org.springframework.data.jpa.domain.Specification
import javax.persistence.*


@Entity
@Table(name = "classSession")
data class ClassSession(

        @OneToMany
        var students: MutableList<User>,
        var teacher: Long,
        @OneToMany
        var taskSessions: MutableList<TaskSession> = mutableListOf(),
        var startDate: Long,
        var endDate: Long,
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1
) {
    fun addTaskSession(taskSession: TaskSession) {
        taskSessions.add(taskSession)
    }
}

data class ClassSessionRequest(
        var students: MutableList<Long>,
        var startDate: Long,
        var endDate: Long
)

object ClassSessionSpecifications {
    fun hasParticipantOfId(id: Long): Specification<ClassSession> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.or(
                    criteriaBuilder.isMember(id, root.get("students")),
                    criteriaBuilder.equal(root.get<Long>("teacher"), id)
            )
        }
    }
}