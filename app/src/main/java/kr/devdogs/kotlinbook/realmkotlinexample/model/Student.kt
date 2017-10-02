package kr.devdogs.kotlinbook.kotlinrealmexample.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Student(@PrimaryKey open var studentId:Int? = null,
                         open var name:String? = null,
                         open var age:Int? = null,
                         open var grade:Int? = null) : RealmObject()