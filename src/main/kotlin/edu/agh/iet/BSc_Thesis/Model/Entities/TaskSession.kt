package edu.agh.iet.BSc_Thesis.Model.Entities

import edu.agh.iet.BSc_Thesis.Model.Entities.School.Student
import edu.agh.iet.BSc_Thesis.Model.Entities.School.StudentResponse
import javax.persistence.*;

@Entity
@Table(name = "task_session")
data class TaskSession(
        @ManyToOne
        var task: Task? = null,
        @ManyToOne
        var classSession: ClassSession? = null,
        @OneToMany                  //TODO change to oneToMany
        var students: MutableList<Student>,
        var grade: Int = -1,
        var needsHelp: Boolean = false,
        var readyToRate: Boolean = false,
//        var deadline: Long,
//        var whiteBoardStatus: String = "",      // dodane, mapa <string, toolstate> narzedzia
//        var toolsMap: Map<String, ToolState> = emptyMap<String, ToolState>(),
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1
) {
        fun response(): TaskSessionResponse {
                return TaskSessionResponse(
                        this.id,
                        this.task!!.response(),
                        this.classSession!!.response(),
                        this.students.map { it.response() }.toMutableList(),
                        this.grade,
                        this.needsHelp,
                        this.readyToRate
                )
        }
}

data class TaskSessionRequest(
        var taskId: Long = -1,
        var classSessionId: Long = -1
//        var studentIds: MutableList<Long>
)

data class TaskSessionResponse(
        var id: Long,
        var task: TaskResponse,
        var classSession: ClassSessionResponse,
        var students: MutableList<StudentResponse>,
        var grade: Int,
        var needsHelp: Boolean,
        var readyToRate: Boolean
)