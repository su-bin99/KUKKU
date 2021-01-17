package com.example.kukku

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask
import android.widget.Toast
import org.jsoup.Jsoup
import java.lang.ref.WeakReference
import java.net.URL

class MyDBHelper(val context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        val DB_VERSION = 1
        val DB_NAME = "mydb.db"
        val TABLE_NAME = "summerclass"
        val TABLE_NAME2 = "myclass"

        //DB에서의 속성 정보들
        val NUM = "num"
        val GRADE = "grade"
        val CLASSIFICATION = "classification"
        val CLASSNUM = "classnum"
        val CLASSNAME = "classname"
        val SCORE = "score"
        val MAJOR = "major"
        val TIME = "time"
        val PROFESSOR = "professor"
        val NOWPERSONNEL = "nowPersonnel"
        val PERSONNEL = "personnel"
        val CLASSTYPE = "classtype"
        val FIELD = "field"

        val startTimeArray = arrayListOf<String>("starttime1", "starttime2", "starttime3", "starttime4", "starttime5")
        val endTimeArray = arrayListOf<String>("endtime1", "endtime2", "endtime3", "endtime4", "endtime5")
        val daysArray = arrayListOf<String>("day1", "day2", "day3", "day4", "day5")
    }

    fun sqlToClass(strsql:String): ArrayList<Class>?{
        var classArray :ArrayList<Class> = ArrayList()

        val db = this.readableDatabase
        val cursor = db.rawQuery(strsql, null)
        val count = cursor.count    //가져온 데이터 갯수
        val columncount = cursor.columnCount    //컬럼갯수
        cursor.moveToFirst()


        if(count > 0){
            do{
                var tempArray :ArrayList<String> = ArrayList()

                for(i in 0 until columncount){
                    if(i == 0 ) tempArray.clear()
                    var str = cursor.getString(i)
                    if(str == null) {
                        if(i == 0 || i == 1 || i== 3 || i== 5 || i== 9 || i== 10) tempArray.add(0.toString())
                        else tempArray.add(" ")
                    }
                    else tempArray.add(cursor.getString(i))
                }


                classArray?.add(Class(tempArray[0].toInt(), tempArray[1].toInt(), tempArray[2],
                    tempArray[3].toInt(), tempArray[4], tempArray[5].toInt(), tempArray[6],
                    tempArray[7], tempArray[8],
                    tempArray[9].toInt(),
                    tempArray[10].toInt(),
                    tempArray[11], tempArray[12]))

            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return classArray
    }

    fun sqlToMyClass(strsql:String): ArrayList<MyClass>?{
        var classArray :ArrayList<MyClass> = ArrayList()

        val db = this.readableDatabase
        val cursor = db.rawQuery(strsql, null)
        val count = cursor.count    //가져온 데이터 갯수
        val columncount = cursor.columnCount    //컬럼갯수
        cursor.moveToFirst()

        if(count > 0){
            do{
                var tempArray :ArrayList<String> = ArrayList()

                for(i in 0 until columncount){
                    if(i == 0 ) tempArray.clear()
                    var str = cursor.getString(i)
                    if(str == null) {
                        if(i == 0 || i == 2 || i== 3 || i== 4 || i== 5 || i== 6 || i== 7
                            || i== 8 || i== 9 || i== 10 || i== 11 ) tempArray.add(0.toString())
                        else tempArray.add(" ")
                    }
                    else tempArray.add(cursor.getString(i))
                }
                classArray?.add(MyClass(tempArray[0].toInt(), tempArray[1], tempArray[2].toInt(), tempArray[3].toInt(),
                    tempArray[4].toInt(), tempArray[5].toInt(), tempArray[6].toInt(), tempArray[7].toInt(),
                    tempArray[8].toInt(), tempArray[9].toInt(), tempArray[10].toInt(), tempArray[11].toInt(),
                    tempArray[12], tempArray[13], tempArray[14], tempArray[15], tempArray[16] ))

            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return classArray
    }


    fun findGyoYang(field : String, Grade: Int) : ArrayList<Class>?{
        lateinit var strsql :String
        if(field == "이수구분 선택") {
            Toast.makeText(context, "이수구분을 선택하세요", Toast.LENGTH_SHORT).show()
            return null
        }else if(Grade == 9){
            if(field == "일선"  || field == "심교")
                strsql = "select * from " + TABLE_NAME + " where " +
                    CLASSIFICATION + " = \'" + field + "\' "
            else  strsql = "select * from " + TABLE_NAME + " where " +
                    FIELD + " = \'" + field + "\' "
        }else if(Grade != 9){
            if(field == "일선"  || field == "심교")
                strsql = "select * from " + TABLE_NAME + " where " +
                        CLASSIFICATION + " = \'" + field + "\' and "+ GRADE + " = " + Grade
            else  strsql = "select * from " + TABLE_NAME + " where " +
                    FIELD + " = \'" + field + "\' and "+ GRADE + " = " + Grade
        }
        return sqlToClass(strsql)
    }

    fun findMajor(classification:String, major:String ,grade :Int): ArrayList<Class>?{
        lateinit var strsql :String
        if(classification == "이수구분 선택" && major == "전공 선택"){
            Toast.makeText(context, "전공과 이수구분을 선택하세요", Toast.LENGTH_SHORT).show()
            return null
        }else if(classification == "이수구분 선택" ){
            Toast.makeText(context, "이수구분을 선택하세요", Toast.LENGTH_SHORT).show()
            return null
        }else if(major =="전공 선택"){
            Toast.makeText(context, "전공을 선택하세요", Toast.LENGTH_SHORT).show()
            return null
        }else if(grade == 9){
            if(classification == "전필"  || classification == "전선"
                || classification == "기교" || classification == "지교")
                strsql = "select * from " + TABLE_NAME + " where " +
                        CLASSIFICATION + " = \'" + classification + "\' and "+
                        MAJOR +  " like \'%" + major +"%\' "
            else  strsql = "select * from " + TABLE_NAME + " where " +
                    FIELD + " = \'" + classification + "\' and "+
                    MAJOR +  " like \'%" + major +"%\' "
        }else if(grade !=9){
            if(classification == "전필"  || classification == "전선"
                || classification == "기교" || classification == "지교")
                strsql = "select * from " + TABLE_NAME + " where " +
                        CLASSIFICATION + " = \'" + classification + "\' and "+
                        MAJOR + " like \'%" + major +"%\' and "+
                        GRADE + " = " + grade
            else  strsql = "select * from " + TABLE_NAME + " where " +
                    FIELD + " = \'" + classification + "\' and "+
                    MAJOR + " like \'%" + major +"%\' and "+
                    GRADE + " = " + grade
        }
        return sqlToClass(strsql)
    }

    fun addMyClass(myclass : MyClass): Boolean{
        val values = ContentValues()
        values.put(CLASSNAME, myclass.ClassName)
        values.put(CLASSNUM, myclass.ClassNum)
        val days = arrayListOf<String>(myclass.Day1, myclass.Day2, myclass.Day3, myclass.Day4, myclass.Day5)
        val starttimes = arrayListOf<Int>(myclass.StartTime1,myclass.StartTime2, myclass.StartTime3, myclass.StartTime4, myclass.StartTime5)
        val endtimes = arrayListOf<Int>(myclass.EndTime1, myclass.EndTime2, myclass.EndTime3, myclass.EndTime4, myclass.EndTime5)
        for(i in 0..4){
            if(days[i] != ""){
                values.put(daysArray[i], days[i])
                values.put(startTimeArray[i], starttimes[i])
                values.put(endTimeArray[i], endtimes[i])
            }else break
        }
        val db = this.writableDatabase
        if(db.insert(TABLE_NAME2, null, values) > 0){
            //insert실패하면 -1반환함
            db.close()
            return true
        }
        else{
            db.close()
            return false
        }
    }

    fun deleteMyClass(classNum : String) :Boolean{

        val strsql = "select * from "+ TABLE_NAME2 + " where "+
                CLASSNUM + " = \'" + classNum + "\'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(strsql, null)
        if( cursor.moveToFirst()){
            db.delete(TABLE_NAME2, CLASSNUM +"=?", arrayOf(classNum))
            cursor.close()
            db.close()
            return true
        }
        cursor.close()
        db.close()
        return false
    }

    fun getMyAllClass(): ArrayList<MyClass>?{
        val strsql = "select * from "+ TABLE_NAME2

        return sqlToMyClass(strsql)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists " + TABLE_NAME+"("+
                NUM + " INTEGER, "+
                GRADE + " INTEGER, "+
                CLASSIFICATION + " TEXT, "+
                CLASSNUM + " INTEGER, "+
                CLASSNAME + " TEXT, "+
                SCORE + " INTEGER, "+
                MAJOR + " TEXT, "+
                TIME + " TEXT, "+
                PROFESSOR + " TEXT, "+
                NOWPERSONNEL + " INTEGER, "+
                PERSONNEL + " INTEGER, "+
                CLASSTYPE + " TEXT, "+
                FIELD + " TEXT )"

        val create_table2 = "create table if not exists " + TABLE_NAME2+"("+
                CLASSNUM + " INTEGER, "+
                CLASSNAME + " TEXT, "+
                SCORE + " INTEGER, "+


        db?.execSQL(create_table)
        db?.execSQL(create_table2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists " + TABLE_NAME
        db?.execSQL(drop_table) // 저 질의 구문을 실행시켜주는 함수 !
        onCreate(db)
    }
}
