package edu.agh.iet.BSc_Thesis.Model.Entities.School

import org.springframework.data.jpa.domain.Specification
import javax.persistence.*

@Entity
@Table(name = "school")
data class School(

        val name: String,
        @OneToMany(targetEntity = SchoolClass::class, cascade = [CascadeType.ALL])
        val classes: MutableList<SchoolClass>,
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1
) {
    fun response(): SchoolResponse {
        return SchoolResponse(
                this.name,
                this.classes.map { it.response() }.toMutableList(),
                this.id
        )
    }

    fun addSchoolClass(schoolClass: SchoolClass) {
        schoolClass.students.forEach { student -> student.schoolName = this.name }
        this.classes.add(schoolClass)
    }
}

data class SchoolRequest(
        val name: String
)

data class SchoolResponse(
        val name: String,
        val classes: MutableList<SchoolClassResponse>,
        val id: Long
)

@Entity
@Table(name = "school_class")
data class SchoolClass(
        val classNumber: Long,
        @OneToMany(targetEntity = Student::class, cascade = [CascadeType.ALL])
        val students: MutableList<Student>,
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1
) {
    fun response(): SchoolClassResponse {
        return SchoolClassResponse(
                id,
                classNumber,
                students.map { it.response() }.toMutableList()
        )
    }
}

data class SchoolClassResponse(
        val id: Long,
        val classNumber: Long,
        val students: MutableList<StudentResponse>
)

object SchoolSpecifications {
    fun hasStudentOfId(id: Long): Specification<School> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.isMember(id, root.get("students"))
        }
    }
}