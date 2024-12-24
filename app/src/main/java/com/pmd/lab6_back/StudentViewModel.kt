package com.pmd.lab6_back

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log

class StudentViewModel : ViewModel() {

    private val _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>> get() = _students

    // Завантаження студентів
    fun loadStudents(context: Context) {
        viewModelScope.launch {
            try {
                val students = StudentRepository.loadStudents(context)
                _students.value = students
            } catch (e: Exception) {
                // Обробка помилки
                Log.e("StudentViewModel", "Error loading students from JSON: ${e.message}")
            }
        }
    }

    // Додавання нового студента
    fun addStudent(context: Context, student: Student) {
        val currentStudents = _students.value?.toMutableList() ?: mutableListOf()
        currentStudents.add(student)
        _students.value = currentStudents
        StudentRepository.saveStudentsToJson(context, currentStudents)
    }

    // Видалення студента
    fun removeStudent(context: Context, studentId: Int) {
        val currentStudents = _students.value?.toMutableList() ?: mutableListOf()
        val studentToRemove = currentStudents.find { it.id == studentId }
        if (studentToRemove != null) {
            currentStudents.remove(studentToRemove)
            _students.value = currentStudents
            StudentRepository.saveStudentsToJson(context, currentStudents)
            Log.d("StudentViewModel", "Student removed: $studentId")
        }
    }

    // Оновлення студента
    fun updateStudent(context: Context, updatedStudent: Student) {
        val currentStudents = _students.value?.toMutableList() ?: mutableListOf()
        val index = currentStudents.indexOfFirst { it.id == updatedStudent.id }
        if (index != -1) {
            currentStudents[index] = updatedStudent
            _students.value = currentStudents
            StudentRepository.saveStudentsToJson(context, currentStudents)
            Log.d("StudentViewModel", "Student updated: $updatedStudent")
        } else {
            Log.d("StudentViewModel", "Student not found for update: ${updatedStudent.id}")
        }
    }
}
