package edu.agh.iet.BSc_Thesis.Model.Entities

import javax.persistence.*

@Entity
@Table(name = "tool_state")
data class ToolState(
        @OneToOne
        var taskSeesionId: TaskSession? = null,                 // task session id      ?onetoone?

//        var status: Any = "",                           // zawartosc, TODO:-> zmienic na object
        var name: String = "",
        var type: String = "",
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1
) {
        fun response(): ToolStateResponse {
                return ToolStateResponse(
                        this.taskSeesionId,
//                        this.status,
                        this.name,
                        this.type,
                        this.id
                )
        }
}

data class ToolStateRequest(
        var taskSessionId: Long = -1,
//        var status: Any,
        var name: String,
        var type: String
)

data class ToolStateResponse(
        var taskSessionId: TaskSession?,
//        var status: Any,
        var name: String,
        var type: String,
        var id: Long
)