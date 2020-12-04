package edu.agh.iet.BSc_Thesis.Model.Entities

import javax.persistence.*

@Entity
@Table(name = "tool_state")
data class ToolState(
        @OneToOne
        var taskSessionId: TaskSession? = null,                 // task session id      ?onetoone?
        @Column(name="status",columnDefinition="LONGTEXT")
        var status: String = "",                           // zawartosc, TODO:-> zmienic na object
        var name: String = "",
        var type: String = "",
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1
) {
        fun response(): ToolStateResponse {
                return ToolStateResponse(
                        this.taskSessionId,
                        this.status,
                        this.name,
                        this.type,
                        this.id
                )
        }
}

data class ToolStateRequest(
        var taskSessionId: Long = -1,
        var status: String,
        var name: String,
        var type: String
)

data class ToolStateResponse(
        var taskSessionId: TaskSession?,
        var status: String,
        var name: String,
        var type: String,
        var id: Long
)