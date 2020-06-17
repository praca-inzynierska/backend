package edu.agh.iet.BSc_Thesis.Model.Entities.School

import org.springframework.data.jpa.domain.Specification
import javax.persistence.*

@Entity
@Table(name = "school")
data class School(

        val name: String,
        @OneToMany(targetEntity = SchoolClass::class)
        val classes: MutableList<SchoolClass>,
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1
)

data class SchoolRequest(
        val name: String
)

@Entity
@Table(name = "schoolClass")
data class SchoolClass(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,
        val classNumber: Long,
        @OneToMany(targetEntity = Student::class)
        val students: MutableList<Student>
)

object SchoolSpecifications {
    fun hasStudentOfId(id: Long): Specification<School> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.isMember(id, root.get("students"))
        }
    }
}