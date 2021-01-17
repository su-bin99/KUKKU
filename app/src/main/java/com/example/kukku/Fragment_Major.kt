package com.example.kukku

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_gyoyang.view.*
import kotlinx.android.synthetic.main.fragment_major.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

/**
 * A simple [Fragment] subclass.
 */
class Fragment_Major : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var myDBHelper : MyDBHelper
    private var root: View? = null

    var mymajor : String = "전공 선택"
    var myfield: String = "이수구분 선택"
    var mygrade: Int = 9

    val base_url = "https://kupis.konkuk.ac.kr/sugang/acd/cour/aply/CourInwonInqTime.jsp?ltYy=2020&ltShtm=B01014&sbjtId="
    var classArray : ArrayList<Class> = ArrayList()
    lateinit var adapter : Adapter_Classrow
    lateinit var progressDialog : ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_major, container, false)
        return root
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    override fun onPause() {
        super.onPause()
        classArray.clear()
    }

    fun init(){
        initspinner()
        root!!.recyclerView.layoutManager = LinearLayoutManager(requireActivity(),
            LinearLayoutManager.VERTICAL, false)

        root!!.allClassBtn.setOnClickListener {
            val temp = findClass(myfield,mymajor, mygrade)
            val dialog = showProgress(context!!)

            GlobalScope.launch{
                if(temp != null)classArray = updatestuNum(temp)
                adapter = Adapter_Classrow(classArray)
                initAdapterListener(adapter)
                requireActivity().runOnUiThread(){
                    root!!.recyclerView.adapter = adapter
                }
                dialog.dismiss()
            }
        }

        root!!.emptyClassBtn.setOnClickListener {
            val temp = findClass(myfield,mymajor, mygrade)
            val dialog = showProgress(context!!)

            GlobalScope.launch{
                if(temp != null)classArray = setEmptyClass(temp)
                adapter = Adapter_Classrow(classArray)
                initAdapterListener(adapter)
                requireActivity().runOnUiThread(){
                    root!!.recyclerView.adapter = adapter
                }
                dialog.dismiss()
            }
        }

        myDBHelper = MyDBHelper(requireActivity())

        adapter = Adapter_Classrow(classArray)
        root!!.recyclerView.adapter = adapter
    }

    fun initAdapterListener( adapter : Adapter_Classrow){
        adapter.itemClickListener = object:Adapter_Classrow.OnItemClickListener{
            override fun OnItemClick(
                holder: Adapter_Classrow.MyViewHolder,
                view: View,
                position: Int
            ) {
                var classNumView: TextView = view.findViewById(R.id.classNum)
                var classNameView: TextView = view.findViewById(R.id.name_class)
                var classTimeView: TextView = view.findViewById(R.id.classtime)
                val dialog = layoutInflater.inflate(R.layout.dialog_addclass, null)

                val dlgBuilder = AlertDialog.Builder(context!!)
                dlgBuilder.setView(dialog)
                    .setPositiveButton("확인"){
                            _,_->
                        val num = classNumView.text.toString().toInt()
                        val name = classNameView.text.toString()
                        val time = classTimeView.text.toString()
                        val times = time.split(", ")

                        var Days :ArrayList<String> = arrayListOf("","","","","")
                        var starttimes :ArrayList<Int> = arrayListOf(0,0,0,0,0)
                        var endtimes :ArrayList<Int> = arrayListOf(0,0,0,0,0)

                        if (times[0].substring(0,1).equals("월") || times[0].substring(0,0).equals("화") || times[0].substring(0,0).equals("수")
                            || times[0].substring(0,0).equals("목") || times[0].substring(0,0).equals("금")){
                            for( i in 0..times.size-1){
                                Days.set(i, times[i].substring(0,1))
                                starttimes.set(i, times[i].substring(1,3).toInt())
                                endtimes.set(i, times[i].substring(4,6).toInt())
                            }
                        }

                        val newClass = MyClass(num,name,
                            starttimes[0], starttimes[1], starttimes[2], starttimes[3], starttimes[4],
                            endtimes[0], endtimes[1], endtimes[2], endtimes[3], endtimes[4] ,
                            Days[0], Days[1], Days[2], Days[3], Days[4])

                        if(myDBHelper.addMyClass(newClass)){
                            Toast.makeText(requireActivity(), "<" + name+">" +"을 시간표에 추가했습니다.", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireActivity(), "insert실패", Toast.LENGTH_SHORT).show()
                        }

                    }
                    .setNegativeButton("취소"){
                            _,_->
                    }
                    .show()
            }
        }
    }

    fun updatestuNum(array : ArrayList<Class>) : ArrayList<Class>{
        val AsyncList :ArrayList<MyAsyncTask> = ArrayList()
        for( i in 0 until array.size){
            AsyncList.add(MyAsyncTask(requireActivity()))
            val myClass = array[i]
            val stuNum = AsyncList[i].execute(URL(base_url+myClass.ClassNum))
            myClass.NowPersonnel = stuNum.get().get(0).toInt()
            myClass.Personnel = stuNum.get().get(1).toInt()
        }
        return array
    }

    fun setEmptyClass(array : ArrayList<Class>) : ArrayList<Class>{
        val AsyncList :ArrayList<MyAsyncTask> = ArrayList()
        val newArray : ArrayList<Class> = ArrayList()
        for( i in 0 until array.size) {
            AsyncList.add(MyAsyncTask(requireActivity()))
        }
        for( i in 0 until array.size){
            val myClass = array[i]
            val stuNum = AsyncList[i].execute(URL(base_url+myClass.ClassNum))
            myClass.NowPersonnel = stuNum.get().get(0).toInt()
            myClass.Personnel = stuNum.get().get(1).toInt()
            if(myClass.Personnel - myClass.NowPersonnel > 0) newArray.add(myClass)
        }
        return newArray
    }

    fun initspinner(){
        context?.let {
            ArrayAdapter.createFromResource(
                it, R.array.major,android.R.layout.simple_spinner_item
            ).also{adapter ->
                majorSpinner.adapter = adapter
            }

            ArrayAdapter.createFromResource(
                it, R.array.majorField,android.R.layout.simple_spinner_item
            ).also{adapter ->
                classificationSpinner.adapter = adapter
            }

            ArrayAdapter.createFromResource(
                it, R.array.grade,android.R.layout.simple_spinner_item
            ).also{adapter ->
                gradeSpinner.adapter = adapter
            }
        }
        majorSpinner.onItemSelectedListener= this
        classificationSpinner.onItemSelectedListener = this
        gradeSpinner.onItemSelectedListener= this
    }

    fun findClass(classification:String,major:String, grade:Int ):ArrayList<Class>?{
        return myDBHelper.findMajor(classification, major, grade)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedItem = parent!!.getItemAtPosition(position).toString()
        when(parent){
            majorSpinner->  mymajor = selectedItem
            classificationSpinner->  myfield = selectedItem
            gradeSpinner-> {
                when(selectedItem){
                    "전체"-> mygrade = 9
                    "1학년" -> mygrade = 1
                    "2학년" -> mygrade = 2
                    "3학년" -> mygrade = 3
                    "4학년" -> mygrade = 4
                }
            }
        }
    }

    fun showProgress(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.show()
        return progressDialog
    }
}
