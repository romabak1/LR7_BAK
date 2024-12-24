package com.pmd.lab6_back

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class StudentDetailActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etMajor: EditText
    private lateinit var etAge: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSaveChanges: Button
    private lateinit var btnRemoveStudent: Button

    private val studentViewModel: StudentViewModel by viewModels()
    private var studentId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_detail)

        // Ініціалізація компонентів
        etName = findViewById(R.id.etStudentName)
        etMajor = findViewById(R.id.etStudentMajor)
        etAge = findViewById(R.id.etStudentAge)
        etEmail = findViewById(R.id.etStudentEmail)
        btnSaveChanges = findViewById(R.id.btnSaveChanges)
        btnRemoveStudent = findViewById(R.id.btnRemoveStudent)

        // Отримання ID студента з Intent
        studentId = intent.getIntExtra("studentId", -1)

        // Завантаження студента за ID
        studentViewModel.students.observe(this) { students ->
            val student = students.find { it.id == studentId }

            if (student != null) {
                // Відображення даних студента в полях для редагування
                etName.setText(student.name)
                etMajor.setText(student.major)
                etAge.setText(student.age.toString())
                etEmail.setText(student.email)

                // Обробка кнопки "Зберегти зміни"
                btnSaveChanges.setOnClickListener {
                    val updatedStudent = student.copy(
                        name = etName.text.toString(),
                        major = etMajor.text.toString(),
                        age = etAge.text.toString().toIntOrNull() ?: student.age,
                        email = etEmail.text.toString()
                    )

                    studentViewModel.updateStudent(applicationContext, updatedStudent)
                    Toast.makeText(this, "Дані оновлено", Toast.LENGTH_SHORT).show()
                    finish() // Повернення до списку
                }

                // Обробка кнопки "Видалити студента"
                btnRemoveStudent.setOnClickListener {
                    studentViewModel.removeStudent(applicationContext, studentId)
                    Toast.makeText(this, "Студента видалено", Toast.LENGTH_SHORT).show()
                    finish() // Повернення до списку
                }
            } else {
                Toast.makeText(this, "Студента не знайдено", Toast.LENGTH_SHORT).show()
                finish() // Закрити активність, якщо студент не знайдений
            }
        }

        // Завантаження студентів при ініціалізації активності
        studentViewModel.loadStudents(applicationContext)
    }
}
