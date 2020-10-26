package com.mobile.aa.survey.form.recycler

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.aa.survey.form.R
import com.mobile.aa.survey.form.model.Form
import com.mobile.aa.survey.form.model.FormItem
import kotlinx.android.synthetic.main.list_form.view.*
import kotlin.collections.ArrayList

class FormRecycler(val context: Context, val itemList: ArrayList<Form>): RecyclerView.Adapter<FormRecycler.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.list_form, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.switch_required.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                itemList.get(position).required = isChecked
            }
        })
        holder.itemView.et_question.setText(itemList.get(position).question)
        holder.itemView.et_question.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                itemList.get(position).question = holder.itemView.et_question.text.toString()
            }
        })
        holder.itemView.spin_type.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, ArrayList(itemList.get(position).formType.values))
        holder.itemView.spin_type.setSelection(itemList.get(position).formType.keys.indexOf(itemList.get(position).type), false)
        holder.itemView.recycler_question.adapter = ChoiceRecycler(context, itemList.get(position).list)
        holder.itemView.recycler_question.layoutManager = LinearLayoutManager(context)
        holder.itemView.spin_type.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                Log.d("selected", "selecy")
                if(itemList.get(position).formType.keys.toList().get(pos) != itemList.get(position).type){
                    itemList.get(position).list.clear()
                }
                itemList.get(position).type = itemList.get(position).formType.keys.toList().get(pos)
                if(itemList.get(position).formType.keys.toList().get(pos) > 90) {
                    holder.itemView.recycler_question.visibility = View.VISIBLE
                    holder.itemView.tv_hint_type.visibility = View.GONE
//                    itemList.get(position).list.clear()
                    if(itemList.get(position).list.size == 0) {
                        var formItem = FormItem()
                        formItem.type = 0
                        formItem.value = "Add Item"
                        itemList.get(position).list.add(formItem)
                        holder.itemView.recycler_question.adapter!!.notifyDataSetChanged()
                    }
                } else {
                    holder.itemView.recycler_question.visibility = View.GONE
                    holder.itemView.tv_hint_type.visibility = View.VISIBLE
                }
            }

        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

    }
}