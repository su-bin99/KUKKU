package com.example.kukku

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    lateinit var myDBHelper : MyDBHelper
    val textArray = arrayListOf<String>("교양", "전공","시간표", "지도")
//    val textArray = arrayListOf<String>("교양", "전공","시간표", "지도",  "개발자")

    val iconArray = arrayListOf<Int>(R.drawable.icon_gyoyang, R.drawable.icon_major,
        R.drawable.icon_timetable, R.drawable.icon_map )
//    val iconArray = arrayListOf<Int>(R.drawable.icon_gyoyang, R.drawable.icon_major,
//        R.drawable.icon_timetable, R.drawable.icon_map, R.drawable.icon_programmer )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDB()
        init()
    }

    @SuppressLint("WrongConstant")
    fun init(){
        myDBHelper = MyDBHelper(this)
        viewPager.adapter = MyFragStateAdapter(this)
        viewPager.isUserInputEnabled = false
        TabLayoutMediator(tabLayout, viewPager){
                tab: TabLayout.Tab, position: Int ->
            tab.text = textArray[position]
            tab.setIcon(iconArray[position])
        }.attach()


        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.actionbar)
    }

    fun initDB(){
        //this.getDAtabasePath에서 requireActivity().get으로 바꿈
        val dbfile = getDatabasePath("mydb.db")
        if(!dbfile.parentFile.exists()){
            dbfile.parentFile.mkdir()
        }
        if(!dbfile.exists()){
            val file = resources.openRawResource(R.raw.mydb)
            val fileSize = file.available()
            val buffer: ByteArray = ByteArray(fileSize)
            file.read(buffer)
            dbfile.createNewFile()
            val output = FileOutputStream(dbfile)
            output.write(buffer)
            output.close()
        }
    }
}