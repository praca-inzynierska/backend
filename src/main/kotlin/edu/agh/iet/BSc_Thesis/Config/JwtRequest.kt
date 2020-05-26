package edu.agh.iet.BSc_Thesis.Config

import java.io.Serializable


class JwtRequest : Serializable {
    var username: String? = null
    var password: String? = null

    //need default constructor for JSON Parsing
    constructor() {}

    constructor(username: String?, password: String?) {
        this.username = username
        this.password = password
    }

    companion object {
        private const val serialVersionUID = 5926468583005150707L
    }
}