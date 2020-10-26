package com.mobile.aa.survey.form.recycler

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobile.aa.survey.form.R
import com.mobile.aa.survey.form.model.FormItem
import kotlinx.android.synthetic.main.list_checkbox.view.*

abstract class CheckboxRecycler(val context: Context, val itemList: ArrayList<FormItem>): RecyclerView.Adapter<FormRecycler.ViewHolder>() {

    var answerList: java.util.ArrayList<String> = java.util.ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormRecycler.ViewHolder {
        return FormRecycler.ViewHolder(
            LayoutInflater.from(this.context).inflate(
                R.layout.list_checkbox,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FormRecycler.ViewHolder, position: Int) {
        val item = itemList.get(position)

        holder.itemView.checkbox.setText(item.value)

        holder.itemView.checkbox.setOnCheckedChangeListener(
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

            try {
                if (isChecked){
                    itemList.get(position).answer = holder.itemView.checkbox.text.toString()
                    answerList.add(holder.itemView.checkbox.text.toString())
                }else{
                    try {
                        itemList.get(position).answer = ""
                        answerList.remove(holder.itemView.checkbox.text.toString())
                    }catch (e: Exception){
                        Log.d("ErrorRemoveCheckBox", e.toString())
                    }
                }
            }catch (e: Exception){
                Log.d("ErrorCheckBox", e.toString())
            }
                OnChecked(answerList)
        })
    }

    abstract fun OnChecked(item:  ArrayList<String>)

}