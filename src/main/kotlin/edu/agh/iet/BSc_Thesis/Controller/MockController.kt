package edu.agh.iet.BSc_Thesis.Controller

import edu.agh.iet.BSc_Thesis.Model.Entities.ClassSession
import edu.agh.iet.BSc_Thesis.Model.Entities.School.School
import edu.agh.iet.BSc_Thesis.Model.Entities.School.SchoolClass
import edu.agh.iet.BSc_Thesis.Model.Entities.School.Student
import edu.agh.iet.BSc_Thesis.Model.Entities.School.Teacher
import edu.agh.iet.BSc_Thesis.Model.Entities.Task
import edu.agh.iet.BSc_Thesis.Model.Entities.TaskSession
import edu.agh.iet.BSc_Thesis.Model.Entities.User
import org.hibernate.HibernateException
import org.hibernate.Session
import org.hibernate.Transaction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZoneOffset


@RestController
@RequestMapping("/mock")
class MockController : BaseController() {

    @CrossOrigin
    @GetMapping("")
    fun mockData(): ResponseEntity<Any> {

        classSessionRepository.deleteAll()
        taskSessionRepository.deleteAll()
        taskRepository.deleteAll()
        schoolRepository.deleteAll()

        teacherRepository.deleteAll()
        studentRepository.deleteAll()

        userRepository.deleteAll()

        //data
        var names: List<String> = listOf("Adamina", "Adela", "Adelajda", "Adria", "Adriana", "Adrianna", "Agata", "Agnieszka",
                "Aida", "Alberta", "Albertyna", "Albina", "Aldona", "Aleksa", "Aleksandra", "Aleksja", "Alesja", "Alfreda",
                "Alicja", "Alina", "Alojza", "Amalia", "Amanda", "Amelia", "Amina", "Amira", "Anastazja", "Anatolia", "Andrea",
                "Andrzeja", "Andżelika", "Aneta", "Anetta", "Angela", "Angelika", "Angelina", "Aniela", "Anita", "Anna", "Antonina",
                "Anzelma", "Apollina", "Apolonia", "Arabella", "Ariadna", "Arleta", "Arnolda", "Astryda", "Atena", "Augusta", "Augustyna",
                "Aurelia", "Babeta", "Balbina", "Barbara", "Bartłomieja", "Beata", "Beatrycja", "Beatrycze", "Beatryksa", "Benedykta",
                "Beniamina", "Benigna", "Berenika", "Bernarda", "Bernadeta", "Berta", "Betina", "Bianka", "Bibiana", "Blanka", "Błażena",
                "Bogdana", "Bogna", "Boguchwała", "Bogumiła", "Bogusława", "Bojana", "Bolesława", "Bona", "Bożena", "Bożenna", "Bożysława",
                "Brenda", "Bromira", "Bronisława", "Brunhilda", "Brygida", "Cecylia", "Celestyna", "Celina", "Cezaria", "Cezaryna", "Celestia",
                "Chociemira", "Chwalisława", "Ciechosława", "Ciesława", "Cinosława", "Cina", "Czesława", "Dajmira", "Dagna", "Dagmara", "Dalia",
                "Dalila", "Dalmira", "Damroka", "Dana", "Daniela", "Danisława", "Danuta", "Dargomira", "Dargosława", "Daria", "Dąbrówka", "Delfina",
                "Delia", "Deresa", "Desreta", "Delinda", "Diana", "Dilara", "Dobiesława", "Dobrochna", "Domasława", "Dominika", "Donata", "Dorosława",
                "Dorota", "Dymfna")
        var surnames = listOf("Abażur", "Bławatek", "Cekin", "Dębska", "Ekler", "Figa", "Gałgan", "Hiacynt", "Igielna", "Janczar", "Klim", "Lanca", "Mewa", "Noteć", "Ofirska", "Placek")
        var schoolNames = listOf("1 LO", "2 LO", "3 LO", "4 LO", "5 LO")

        //users
        var students = names.foldIndexed(mutableListOf<Student>(), { index, list, name ->
            var lastName = surnames[index % surnames.size]
            var userToAdd = User("${name.toLowerCase()}_${lastName.toLowerCase()}", name, lastName, "asd")
            var studentToAdd = Student(userToAdd, mutableListOf())
            list.add(studentToAdd)
            list
        }).map { studentRepository.save(it) }
        var teachers = listOf(
                Teacher(User("lzajac", "Lukasz", "Zajac", "asd"),
                        mutableListOf("matematyka", "biologia")),
                Teacher(User("jbodera", "Jakub", "Bodera", "asd"),
                        mutableListOf("fizyka", "geografia")),
                Teacher(User("jcieloch", "Jakub", "Cieloch", "asd"),
                        mutableListOf("wf", "chemia"))
        ).map {
            teacherRepository.save(it) }


        //school structure
        var numberOfClasses = 4
        var schools = schoolNames.map { School(it, mutableListOf()) }
        var classes = 0.toLong().rangeTo(numberOfClasses * schoolNames.size)
                .map { SchoolClass(it % numberOfClasses, mutableListOf()) }
        students.forEachIndexed { index, student ->
            classes[index % classes.size].students.add(student)
        }
        classes.forEachIndexed { index, schoolClass ->
            schools[index % schools.size].classes.add(schoolClass)
        }

        schools = schools.map { schoolRepository.save(it) }

        //tasks
        var task0: Task = Task(
                teachers.get(0),
                "Nazwa zadania 0",
                teachers.get(0).subjects.get(0),
                "Opis zadania 0",
                mutableListOf("whiteboard", "textChat"),
                60,
                "whiteboard")
        var task1: Task = Task(
                teachers.get(1),
                "Nazwa zadania 1",
                teachers.get(1).subjects.get(0),
                "Opis zadania 0",
                mutableListOf("whiteboard", "textChat"),
                60,
                "whiteboard")
        var task2: Task = Task(
                teachers.get(2),
                "Nazwa zadania 2",
                teachers.get(2).subjects.get(0),
                "Opis zadania 0",
                mutableListOf("whiteboard", "textChat"),
                60,
                "whiteboard")
        val tasks = mutableListOf(task0, task1, task2)
                .map { taskRepository.save(it) }

        //classSessions
        val classSessions = 0.rangeTo(3).map {
            val students = classes.subList(it * 3, (1 + it) * 3)
                    .map { it.students }
                    .flatten().toMutableList()
            val teacher = teachers[it % teachers.size]
            val start = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            val end = LocalDateTime.now().plusHours(2).toEpochSecond(ZoneOffset.UTC)


            classSessionRepository.save(ClassSession(students, teacher, mutableListOf(), start, end))
        }

        for (classSession in classSessions) {
            classSession.students.chunked(3).mapIndexed { index, studentsChunk ->
                val taskSession = TaskSession(
                        tasks[index % tasks.size],
                        classSession,
                        studentsChunk.toMutableList(),
                        deadline = LocalDateTime.now()
                                .plusMinutes(tasks[index % tasks.size].minutes)
                                .toEpochSecond(ZoneOffset.UTC))
                classSession.addTaskSession(taskSession)
                classSessionRepository.save(classSession)
            }
        }

        return ResponseEntity(HttpStatus.OK)
    }
}