package com.example.kukku

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class Adapter_myClassrow(val items:ArrayList<MyClass>)
    : RecyclerView.Adapter<Adapter_myClassrow.MyViewHolder>(){
    var context : Context?= null

    interface OnItemClickListener{
        fun OnItemClick(holder : MyViewHolder , view : View, position : Int)
    }
    interface OnItemClickListener2{
        fun OnItemClick1(holder : MyViewHolder , position : Int)
        fun OnItemClick2(holder : MyViewHolder , position : Int)
    }

    var itemClickListener : OnItemClickListener ?= null
    var itemClickListener2 : OnItemClickListener2 ?= null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val a = R.id.classNum
        var clNumText: TextView = itemView.findViewById(R.id.classNum)
        var clNameText : TextView = itemView.findViewById(R.id.name_class)
        var button : ToggleButton = itemView.findViewById(R.id.checkBox)

        init {
            itemView.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, adapterPosition)

            }
            button.setOnCheckedChangeListener{ _, isChecked -> // { buttonView, isChecked ->
                if(isChecked){
                    itemClickListener2?.OnItemClick2(this, adapterPosition)
                }
                else{
                    itemClickListener2?.OnItemClick1(this, adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Adapter_myClassrow.MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_timetable, parent, false)
        context = parent.getContext()
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: Adapter_myClassrow.MyViewHolder, position: Int) {
//        holder.clNumText.text = items[position].ClassNum.toString()
//        holder.clNameText.text = items[position].ClassName
        val myclass = items[position]
        holder.clNameText.text = myclass.ClassName
        holder.clNumText.text = myclass.ClassNum.toString()

    }


}