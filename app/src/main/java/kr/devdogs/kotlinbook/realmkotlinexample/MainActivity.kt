package kr.devdogs.kotlinbook.realmkotlinexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import io.realm.Realm
import io.realm.Sort

import kr.devdogs.kotlinbook.kotlinrealmexample.model.Student

class MainActivity : AppCompatActivity() {
    var textView:TextView? = null
    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.view_txt) as TextView
        Realm.init(applicationContext)
        realm = Realm.getDefaultInstance()

        val student1 = Student(1, "박중수", 26, 4)
        val student2 = Student(2, "박영환", 27, 4)

        insertOrUpdateV1(student1)
        insertOrUpdateV2(student2)

        deleteById(1)

        val studentList = findAll()
        val oneStudent = findOneById(1)

        val sb = StringBuilder()

        sb.append("== List ==\n")
        studentList?.let {
            for (student in it) {
                sb.append(student.studentId)
                        .append(". ")
                        .append(student.name)
                        .append(" - ")
                        .append(student.age)
                        .append("살 - ")
                        .append(student.grade)
                        .append("학년\n")
            }
        }

        oneStudent?.let {
            sb.append("\n\n== Select One ==\n")
                    .append(oneStudent.studentId)
                    .append(". ")
                    .append(oneStudent.name)
                    .append(" - ")
                    .append(oneStudent.age)
                    .append("살 - ")
                    .append(oneStudent.grade)
                    .append("학년\n")
        }

        textView?.text = sb.toString()
    }

    fun insertOrUpdateV1(student: Student) {
        realm?.beginTransaction()
        if (student.studentId == 0) {
            val maxId = realm?.where(Student::class.java)?.max("studentId")
            val nextId = (maxId?.toInt() ?: 0) + 1
            student.studentId = nextId
        }

        realm?.insertOrUpdate(student)
        realm?.commitTransaction()
    }

    fun insertOrUpdateV2(student: Student) {
        realm?.executeTransaction { realm ->
            student.studentId?:let {
                val maxId = realm.where(Student::class.java).max("studentId")
                val nextId = (maxId?.toInt() ?: 0) + 1
                student.studentId = nextId
            }

            realm.insertOrUpdate(student)
        }
    }

    fun findAll(): List<Student>? {
        val results = realm?.where(Student::class.java)
                ?.findAll()
                ?.sort("studentId", Sort.DESCENDING)

        return results
    }

    fun findOneById(studentId: Int): Student? {
        val results = realm?.where(Student::class.java)
                ?.equalTo("studentId", studentId)
                ?.findFirst()

        return results
    }

    private fun deleteById(studentId: Int) {
        realm?.executeTransaction {
            val targetStudent = it.where(Student::class.java)
                    .equalTo("studentId", studentId)
                    .findFirst()

            targetStudent.deleteFromRealm()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
    }
}
