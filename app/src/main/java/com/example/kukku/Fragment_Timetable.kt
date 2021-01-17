package com.example.kukku

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_gyoyang.view.*
import kotlinx.android.synthetic.main.row.view.*

/**
 * A simple [Fragment] subclass.
 */
class Fragment_Timetable : Fragment() {
    lateinit var myDBHelper : MyDBHelper
    private var root: View? = null
    lateinit var adapter : Adapter_myClassrow
    var classArray : ArrayList<MyClass> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_timetable, container, false)
        return root
    }

    override fun onResume() {
        super.onResume()
        init()
    }
    fun init() {
        root!!.recyclerView.layoutManager = LinearLayoutManager(requireActivity(),
            LinearLayoutManager.VERTICAL, false)
        myDBHelper = MyDBHelper(requireActivity())
        val temp = myDBHelper.getMyAllClass()
        if(temp!= null) classArray = temp
        adapter = Adapter_myClassrow(classArray)
        root!!.recyclerView.adapter = adapter
        initAdapterListener( adapter )
    }

    fun initAdapterListener( adapter : Adapter_myClassrow){

        adapter.itemClickListener = object:Adapter_myClassrow.OnItemClickListener{
            override fun OnItemClick(
                holder: Adapter_myClassrow.MyViewHolder,
                view: View,
                position: Int
            ) {
                val dialog = layoutInflater.inflate(R.layout.dialog_deleteclass, null)
                val dlgBuilder = AlertDialog.Builder(context!!)
                val show = dlgBuilder.setView(dialog)
                    .setPositiveButton("확인") { _, _ ->
                        val classnumStr = classArray[position].ClassNum.toString()
                        myDBHelper.deleteMyClass(classnumStr)
                        holder.button.isChecked = false
                        adapter.items.removeAt(position)
                        adapter.notifyItemRemoved(position)
                    }
                    .setNegativeButton("취소") { _, _ ->

                    }
                    .show()
            }

        }


        adapter.itemClickListener2 = object:Adapter_myClassrow.OnItemClickListener2{
            @SuppressLint("ResourceAsColor")
            override fun OnItemClick1(holder: Adapter_myClassrow.MyViewHolder, position: Int) {
                for( num in 0 .. classArray.size-1){
                    val thisclass = classArray[position]
                    // val color = colorset.get(num)
                    if(thisclass.StartTime1!= 0 ) {

                        for (i in thisclass.StartTime1 - 2..thisclass.EndTime1 - 2) {
                            val changeView: TextView = root!!.findViewById(mon_textViewId[i])
                            if (i == thisclass.StartTime1 - 2) changeView.text = ""
                            changeView.textSize = 13.0F
                            changeView.setBackgroundColor(Color.WHITE)
                        }
                        for (i in thisclass.StartTime2 - 2..thisclass.EndTime2 - 2) {
                            val changeView: TextView = root!!.findViewById(tue_textViewId[i])
                            if (i == thisclass.StartTime1 - 2) changeView.text = ""
                            changeView.textSize = 13.0F
                            changeView.setBackgroundColor(Color.WHITE)
                        }
                        for (i in thisclass.StartTime3 - 2..thisclass.EndTime3 - 2) {
                            val changeView: TextView = root!!.findViewById(wed_textViewId[i])
                            if (i == thisclass.StartTime1 - 2) changeView.text = ""
                            changeView.textSize = 13.0F
                            changeView.setBackgroundColor(Color.WHITE)
                        }
                        for (i in thisclass.StartTime4 - 2..thisclass.EndTime4 - 2) {
                            val changeView: TextView = root!!.findViewById(thu_textViewId[i])
                            if (i == thisclass.StartTime1 - 2) changeView.text = ""
                            changeView.textSize = 13.0F
                            changeView.setBackgroundColor(Color.WHITE)
                        }
                        for (i in thisclass.StartTime5 - 2..thisclass.EndTime5 - 2) {
                            val changeView: TextView = root!!.findViewById(fri_textViewId[i])
                            if (i == thisclass.StartTime1 - 2) changeView.text = ""
                            changeView.textSize = 13.0F
                            changeView.setBackgroundColor(Color.WHITE)
                        }
                    }
                }
            }

            @SuppressLint("ResourceAsColor")
            override fun OnItemClick2(holder: Adapter_myClassrow.MyViewHolder, position: Int) {
                val thisclass = classArray[position]
                val color = colorset.get(position%10)
                // val color = R.color.color1
                if(thisclass.StartTime1!= 0 ){
                    outthisloop@for( j in 0 .. 0){
                        for(i in thisclass.StartTime1-2 .. thisclass.EndTime1-2){
                            val changeView :TextView = root!!.findViewById(mon_textViewId[i])
                            if(changeView.text != "") {
                                Toast.makeText(requireActivity(), "시간이 겹치므로 추가할 수 없습니다. ", Toast.LENGTH_SHORT).show()
                                holder.button.isChecked = false
                                break@outthisloop
                            }
                            if(i == thisclass.StartTime1-2) changeView.text = thisclass.ClassName
                            changeView.textSize= 10.0F
                            changeView.background = resources.getDrawable(color)
                        }
                        for(i in thisclass.StartTime2-2 .. thisclass.EndTime2-2){
                            val changeView :TextView = root!!.findViewById(tue_textViewId[i])
                            if(i == thisclass.StartTime1-2) changeView.text = thisclass.ClassName
                            changeView.textSize= 10.0F
                            changeView.background = resources.getDrawable(color)
                        }
                        for(i in thisclass.StartTime3-2 .. thisclass.EndTime3-2){
                            val changeView :TextView = root!!.findViewById(wed_textViewId[i])
                            if(i == thisclass.StartTime1-2) changeView.text = thisclass.ClassName
                            changeView.textSize= 10.0F
                            changeView.background = resources.getDrawable(color)
                        }
                        for(i in thisclass.StartTime4-2 .. thisclass.EndTime4-2){
                            val changeView :TextView = root!!.findViewById(thu_textViewId[i])
                            if(i == thisclass.StartTime1-2) changeView.text = thisclass.ClassName
                            changeView.textSize= 10.0F
                            changeView.background = resources.getDrawable(color)
                        }
                        for(i in thisclass.StartTime5-2 .. thisclass.EndTime5-2){
                            val changeView :TextView = root!!.findViewById(fri_textViewId[i])
                            if(i == thisclass.StartTime1-2) changeView.text = thisclass.ClassName
                            changeView.textSize= 10.0F
                            changeView.background = resources.getDrawable(color)
                        }
                    }
                }else{
                    Toast.makeText(requireActivity(), "E러닝 과목은 시간표에 추가되지 않습니다.", Toast.LENGTH_SHORT).show()
                    holder.button.isChecked = false
                }
            }
        }
    }

    val colorset = arrayListOf<Int>(R.color.color1, R.color.color2, R.color.color3, R.color.color4,
        R.color.color5, R.color.color6, R.color.color7, R.color.color8, R.color.color9, R.color.color10)
    val mon_textViewId = arrayListOf<Int>(
        R.id.mon_time2, R.id.mon_time3, R.id.mon_time4, R.id.mon_time5
        , R.id.mon_time6 , R.id.mon_time7, R.id.mon_time8, R.id.mon_time9, R.id.mon_time10
        , R.id.mon_time11, R.id.mon_time12, R.id.mon_time13, R.id.mon_time14, R.id.mon_time15
        , R.id.mon_time16, R.id.mon_time17, R.id.mon_time18, R.id.mon_time19, R.id.mon_time20
        , R.id.mon_time21)

    val tue_textViewId = arrayListOf<Int>(
        R.id.tue_time2, R.id.tue_time3, R.id.tue_time4, R.id.tue_time5
        , R.id.tue_time6 , R.id.tue_time7, R.id.tue_time8, R.id.tue_time9, R.id.tue_time10
        , R.id.tue_time11, R.id.tue_time12, R.id.tue_time13, R.id.tue_time14, R.id.tue_time15
        , R.id.tue_time16, R.id.tue_time17, R.id.tue_time18, R.id.tue_time19, R.id.tue_time20
        , R.id.tue_time21)

    val wed_textViewId = arrayListOf<Int>(
        R.id.wed_time2, R.id.wed_time3, R.id.wed_time4, R.id.wed_time5
        , R.id.wed_time6 , R.id.wed_time7, R.id.wed_time8, R.id.wed_time9, R.id.wed_time10
        , R.id.wed_time11, R.id.wed_time12, R.id.wed_time13, R.id.wed_time14, R.id.wed_time15
        , R.id.wed_time16, R.id.wed_time17, R.id.wed_time18, R.id.wed_time19, R.id.wed_time20
        , R.id.wed_time21)

    val thu_textViewId = arrayListOf<Int>(
        R.id.thu_time2, R.id.thu_time3, R.id.thu_time4, R.id.thu_time5
        , R.id.thu_time6 , R.id.thu_time7, R.id.thu_time8, R.id.thu_time9, R.id.thu_time10
        , R.id.thu_time11, R.id.thu_time12, R.id.thu_time13, R.id.thu_time14, R.id.thu_time15
        , R.id.thu_time16, R.id.thu_time17, R.id.thu_time18, R.id.thu_time19, R.id.thu_time20
        , R.id.thu_time21)

    val fri_textViewId = arrayListOf<Int>(
        R.id.fri_time2, R.id.fri_time3, R.id.fri_time4, R.id.fri_time5
        , R.id.fri_time6 , R.id.fri_time7, R.id.fri_time8, R.id.fri_time9, R.id.fri_time10
        , R.id.fri_time11, R.id.fri_time12, R.id.fri_time13, R.id.fri_time14, R.id.fri_time15
        , R.id.fri_time16, R.id.fri_time17, R.id.fri_time18, R.id.fri_time19, R.id.fri_time20
        , R.id.fri_time21)

}
