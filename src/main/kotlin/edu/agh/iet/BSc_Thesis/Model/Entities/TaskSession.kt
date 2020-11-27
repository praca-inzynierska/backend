package edu.agh.iet.BSc_Thesis.Model.Entities

import edu.agh.iet.BSc_Thesis.Model.Entities.School.Student
import edu.agh.iet.BSc_Thesis.Model.Entities.School.StudentResponse
import javax.persistence.*;

@Entity
@Table(name = "task_session")
data class TaskSession(
        @ManyToOne
        var task: Task? = null,
        @OneToMany
        var students: MutableList<Student>,
        @ManyToOne
        var classSession: ClassSession,
        var grade: Int = -1,
        var needsHelp: Boolean = false,
        var readyToRate: Boolean = false,
        var deadline: Long,
//        var whiteBoardStatus: String = "",      // dodane, mapa <string, toolstate> narzedzia
//        var toolsMap: Map<String, ToolState> = emptyMap<String, ToolState>(),
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1
) {
    fun response(): TaskSessionResponse {
        return TaskSessionResponse(
                this.id,
                this.classSession.id,
                this.task!!.response(),
                this.students.map { it.response() }.toMutableList(),
                this.grade,
                this.needsHelp,
                this.readyToRate,
                this.deadline
        )
    }

    fun hasMember(user: User): Boolean {
        return (this.students
                .map { it.user.id }
                .contains(user.id)
                || this.classSession.teacher.id == user.id)
    }
}

data class TaskSessionRequest(
        var taskId: Long = -1,
        var classSessionId: Long = -1,
        var studentIds: MutableList<Long>
)

data class TaskSessionResponse(
        var id: Long,
        var classSessionId: Long,
        var task: TaskResponse,
        var students: MutableList<StudentResponse>,
        var grade: Int,
        var needsHelp: Boolean,
        var readyToRate: Boolean,
        var deadline: Long
)