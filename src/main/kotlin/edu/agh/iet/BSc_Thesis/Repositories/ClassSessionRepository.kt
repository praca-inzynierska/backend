package edu.agh.iet.BSc_Thesis.Repositories

import edu.agh.iet.BSc_Thesis.Model.Entities.ClassSession
import edu.agh.iet.BSc_Thesis.Model.Entities.TaskSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface ClassSessionRepository : JpaRepository<ClassSession, Long>, JpaSpecificationExecutor<ClassSession> {
}