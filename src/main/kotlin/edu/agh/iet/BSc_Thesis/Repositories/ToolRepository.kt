package edu.agh.iet.BSc_Thesis.Repositories

import edu.agh.iet.BSc_Thesis.Model.Entities.Task
import edu.agh.iet.BSc_Thesis.Model.Entities.Tool
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ToolRepository : JpaRepository<Tool, String> {

}