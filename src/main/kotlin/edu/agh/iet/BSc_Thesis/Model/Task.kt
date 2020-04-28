package edu.agh.iet.BSc_Thesis.Model

class Task(
        var id: Long = -1,
        var teacherId: Long = -1,
        var subject: String? = "",
        var name: String = "",
        var description: String = "",
        var tools: Array<String> = emptyArray(),
        var time: Long = -1,
        var type: String = ""
)