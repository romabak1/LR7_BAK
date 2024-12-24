package com.pmd.lab6_back

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

object StudentRepository {

    // Завантаження студентів з JSON
    fun loadStudents(context: Context): List<Student> {
        return try {
            val fileInputStream: FileInputStream = context.openFileInput("students.json")
            val inputStreamReader = InputStreamReader(fileInputStream)
            val students = Gson().fromJson(inputStreamReader, Array<Student>::class.java).toList()
            inputStreamReader.close()
            students
        } catch (e: Exception) {
            emptyList() // Повертаємо порожній список у разі помилки
        }
    }

    // Збереження студентів у JSON
    fun saveStudentsToJson(context: Context, students: List<Student>) {
        try {
            val json = Gson().toJson(students)
            val outputStream: FileOutputStream = context.openFileOutput("students.json", Context.MODE_PRIVATE)
            val outputStreamWriter = OutputStreamWriter(outputStream)
            outputStreamWriter.write(json)
            outputStreamWriter.close()
        } catch (e: Exception) {
            Log.e("StudentRepository", "Error saving students to JSON: ${e.message}")
        }
    }
}
