package com.mobile.aa.survey.form.recycler

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.aa.survey.form.R
import com.mobile.aa.survey.form.model.Form
import com.mobile.aa.survey.form.model.FormItem
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.list_form_question.view.*
import kotlinx.android.synthetic.main.list_question_checkbox.view.*
import kotlinx.android.synthetic.main.list_question_date.view.*
import kotlinx.android.synthetic.main.list_question_drop_down.view.*
import kotlinx.android.synthetic.main.list_question_file.view.*
import kotlinx.android.synthetic.main.list_question_multiple_choise.view.*
import kotlinx.android.synthetic.main.list_question_textlong.view.*
import kotlinx.android.synthetic.main.list_question_textshort.view.*
import kotlinx.android.synthetic.main.list_question_time.view.*
import java.text.SimpleDateFormat
import java.util.*

abstract class listFormRecycler(val context: Context, private val listViewType: List<Item>) : RecyclerView.Adapter<listFormRecycler.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return  ViewHolderItem(inflater.inflate(R.layout.list_form_question, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listViewType.get(position)
        holder.itemView.tv_title_form.text = item.title
        holder.itemView.tv_id_form.text = item.id
        holder.itemView.card_list_form.setOnClickListener {
            OnClick(item)
        }
    }



    override fun getItemCount(): Int = listViewType.size

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderItem(itemView: View) : ViewHolder(itemView)

    abstract fun OnClick(item:Item)


    class Item {
        var id: String = ""
        var title: String = ""
        var deskripsi: String = ""
        var totalquestion: String = ""
    }

}