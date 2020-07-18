package edu.agh.iet.BSc_Thesis.Model.Entities

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tool")
data class Tool(
        @Id
        @Column(length = 64)
        var name: String = "",
        var type: String = "",
        var tag: String = ""
)