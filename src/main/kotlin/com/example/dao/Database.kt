package com.example.dao

import java.sql.Connection
import java.sql.DriverManager

class Database {
    var conn: Connection

    init{

        Class.forName("org.sqlite.JDBC")
        conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\as083\\assignment1.db")
    }
}