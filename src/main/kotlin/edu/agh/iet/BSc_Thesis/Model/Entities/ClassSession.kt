package edu.agh.iet.BSc_Thesis.Model.Entities

import org.springframework.data.jpa.domain.Specification
import javax.persistence.*


@Entity
@Table(name = "classSession")
data class ClassSession(

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,
        @ElementCollection
        var students: MutableList<Long>,
        var teacher: Long,
        @ElementCollection
        var taskSessions: MutableList<Long>,
        var startDate: Long,
        var endDate: Long
) {
    fun addTaskSession(taskSession: TaskSession) {
        taskSessions.add(taskSession.id)
    }
}

data class ClassSessionRequest(
        var students: MutableList<Long>,
        var startDate: Long,
        var endDate: Long
) {
    fun toNewClassSession(teacher: Long): ClassSession {
        return ClassSession(
                students = students,
                startDate = startDate,
                endDate = endDate,
                teacher = teacher,
                taskSessions = mutableListOf())
    }
}

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