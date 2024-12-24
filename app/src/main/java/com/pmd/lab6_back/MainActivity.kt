package com.pmd.lab6_back

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private lateinit var lvStudents: ListView
    private lateinit var btnAddStudent: Button
    private lateinit var adapter: ArrayAdapter<String>
    private val studentViewModel: StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ініціалізація компонентів
        lvStudents = findViewById(R.id.lvStudents)
        btnAddStudent = findViewById(R.id.btnAddStudent)

        // Створення адаптера для відображення студентів
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf()
        )
        lvStudents.adapter = adapter

        // Спостерігаємо за змінами в даних студентів
        studentViewModel.students.observe(this, Observer { students ->
            // Оновлюємо адаптер при зміні списку студентів
            adapter.clear()
            adapter.addAll(students.map { it.name })
            adapter.notifyDataSetChanged()
        })

        // Завантажуємо студентів з репозиторію
        studentViewModel.loadStudents(this)

        // Обробка кліку на елемент списку
        lvStudents.setOnItemClickListener { _, _, position, _ ->
            val selectedStudent = studentViewModel.students.value?.get(position)
            if (selectedStudent != null) {
                val intent = Intent(this, StudentDetailActivity::class.java).apply {
                    putExtra("studentId", selectedStudent.id)
                }
                startActivity(intent)
            }
        }

        // Обробка кнопки "Додати студента"
        btnAddStudent.setOnClickListener {
            val newStudent = Student(
                id = studentViewModel.students.value?.size?.plus(1) ?: 1,
                name = "Новий Студент",
                age = 18,
                major = "Нова спеціальність",
                email = "new.student@example.com"
            )
            studentViewModel.addStudent(this, newStudent)  // Передаємо контекст
        }
    }

    override fun onResume() {
        super.onResume()
        studentViewModel.loadStudents(this)
    }
}
