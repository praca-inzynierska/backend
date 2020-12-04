package edu.agh.iet.BSc_Thesis.Repositories

import edu.agh.iet.BSc_Thesis.Model.Entities.ToolState
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ToolStateRepository : JpaRepository<ToolState, Long> {
    fun getAllByTaskSessionId(taskSessionId: Long): List<ToolState>
}