package com.mobile.aa.survey.form.recycler

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.aa.survey.form.R
import com.mobile.aa.survey.form.model.FormItem
import kotlinx.android.synthetic.main.list_choice.view.*

class ChoiceRecycler(val context: Context, val itemList: ArrayList<FormItem>) : RecyclerView.Adapter<ChoiceRecycler.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoiceRecycler.ViewHolder {
        return ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.list_choice, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ChoiceRecycler.ViewHolder, position: Int) {
        if(itemList.size == 0){
            loadAddItem()
        } else {
            var item = itemList.get(position)
            if(item.type == FormItem().TYPE_ADD){
                holder.itemView.card_add.visibility = View.VISIBLE
                holder.itemView.et_value.visibility = View.GONE
                holder.itemView.tv_close.visibility = View.GONE
            } else {
                holder.itemView.card_add.visibility = View.GONE
                holder.itemView.et_value.visibility = View.VISIBLE
                holder.itemView.tv_close.visibility = View.VISIBLE

                holder.itemView.et_value.setText(itemList.get(position).value)
                holder.itemView.et_value.addTextChangedListener(object : TextWatcher{
                    override fun afterTextChanged(s: Editable?) {

                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        itemList.get(position).value = holder.itemView.et_value.text.toString()
                    }
                })
                holder.itemView.tv_close.setOnClickListener {
                    itemList.removeAt(position)
                    notifyDataSetChanged()
                }
            }
            holder.itemView.card_add.setOnClickListener {
                val item = itemList.get(position)
                item.type = FormItem().TYPE_EDIT
                item.value = ""
                itemList.set(position, item)
                loadAddItem()
            }
        }
    }

    fun loadAddItem(){
        var formItem = FormItem()
        formItem.type = 0
        formItem.value = "Add Item"
        itemList.add(formItem)
        notifyDataSetChanged()
    }
}