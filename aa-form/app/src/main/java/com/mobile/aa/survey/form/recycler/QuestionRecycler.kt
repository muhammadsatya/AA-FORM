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

abstract class QuestionRecycler(val context: Context, private val listViewType: List<Form>) : RecyclerView.Adapter<QuestionRecycler.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            Form().TYPE_SHORT -> ViewHolderItem(inflater.inflate(R.layout.list_question_textshort, null))
            Form().TYPE_LONG -> ViewHolderItem(inflater.inflate(R.layout.list_question_textlong, null))
            Form().TYPE_FILE -> ViewHolderItem(inflater.inflate(R.layout.list_question_file, null))
            Form().TYPE_DATE -> ViewHolderItem(inflater.inflate(R.layout.list_question_date, null))
            Form().TYPE_TIME -> ViewHolderItem(inflater.inflate(R.layout.list_question_time, null))
            Form().TYPE_MULTIPLE -> ViewHolderItem(inflater.inflate(R.layout.list_question_multiple_choise, null))
            Form().TYPE_CHECKBOX -> ViewHolderItem(inflater.inflate(R.layout.list_question_checkbox, null))
            else -> ViewHolderItem(inflater.inflate(R.layout.list_question_drop_down, null))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = listViewType.get(position).type
        val question = listViewType.get(position).question
        val required = listViewType.get(position).required
        val valItem = listViewType.get(position).list
        when (viewType) {
            Form().TYPE_SHORT -> {
                holder.itemView.tv_short_question.text = question
                if (required){
                    holder.itemView.tv_short_required.visibility = View.VISIBLE
                }else{
                    holder.itemView.tv_short_required.visibility = View.GONE
                }

                holder.itemView.et_short_question.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {

                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        listViewType.get(position).answer = holder.itemView.et_short_question.text.toString()
                    }
                })
            }
            Form().TYPE_LONG  ->{
                holder.itemView.tv_long_question.text = question
                if (required){
                    holder.itemView.tv_long_required.visibility = View.VISIBLE
                }else{
                    holder.itemView.tv_long_required.visibility = View.GONE
                }
                holder.itemView.et_long_question.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {

                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        listViewType.get(position).answer = holder.itemView.et_long_question.text.toString()
                    }
                })
            }
            Form().TYPE_FILE ->{
                holder.itemView.tv_file_question.text = question
                if (required){
                    holder.itemView.tv_file_required.visibility = View.VISIBLE
                }else{
                    holder.itemView.tv_file_required.visibility = View.GONE
                }
                holder.itemView.card_upload_file.setOnClickListener {
                    OnClick(position)
                }

            }
            Form().TYPE_DATE ->{
                holder.itemView.tv_date_question.text = question
                if (required){
                    holder.itemView.tv_date_required.visibility = View.VISIBLE
                }else{
                    holder.itemView.tv_date_required.visibility = View.GONE
                }
                holder.itemView.et_date_question.setOnClickListener {
                    var calendarDate = Calendar.getInstance()
                    var dateFormattera = SimpleDateFormat("EEE, dd MMM yyyy")
                    val mDatePicker: DatePickerDialog
                    mDatePicker = DatePickerDialog(context,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            // TODO Auto-generated method stub
                            calendarDate!!.set(year, monthOfYear, dayOfMonth)
                            listViewType.get(position).answer = dateFormattera.format(calendarDate!!.getTime())
                            holder.itemView.et_date_question.setText(dateFormattera.format(calendarDate!!.getTime()))
                        },
                        calendarDate!!.get(Calendar.YEAR),
                        calendarDate!!.get(Calendar.MONTH),
                        calendarDate!!.get(Calendar.DAY_OF_MONTH)
                    )
                    mDatePicker.show()
                }
            }
            Form().TYPE_TIME ->{
                holder.itemView.tv_time_question.text = question
                if (required){
                    holder.itemView.tv_time_required.visibility = View.VISIBLE
                }else{
                    holder.itemView.tv_time_required.visibility = View.GONE
                }
                holder.itemView.et_time_hour_question.setOnClickListener {
                    val c = Calendar.getInstance()
                    val hour = c.get(Calendar.HOUR)
                    val minute = c.get(Calendar.MINUTE)
                    val tpd = TimePickerDialog(context,TimePickerDialog.OnTimeSetListener(function = { view, h: Int, m: Int ->
                        val jam = h.toString()
                        val menit = m.toString()

                        var jj = "0"
                        var mm = "0"

                        if (jam.length < 2){
                            jj = "0"+jam
                        }else{
                            jj = h.toString()
                        }

                        if (menit.length < 2){
                            mm = "0"+menit
                        }else{
                            mm = m.toString()
                        }

                        holder.itemView.et_time_hour_question.setText(jj)
                        holder.itemView.et_time_minute_question.setText(mm)
                        listViewType.get(position).answer = jj +" : " + mm
                    }),hour,minute,false)


                    tpd.show()
                }

                holder.itemView.et_time_minute_question.setOnClickListener {
                    val c = Calendar.getInstance()
                    val hour = c.get(Calendar.HOUR)
                    val minute = c.get(Calendar.MINUTE)
                    val tpd = TimePickerDialog(context,TimePickerDialog.OnTimeSetListener(function = { view, h, m ->

                        val jam = h.toString()
                        val menit = m.toString()

                        var jj = "0"
                        var mm = "0"

                        if (jam.length < 2){
                            jj = "0"+jam
                        }else{
                            jj = h.toString()
                        }

                        if (menit.length < 2){
                            mm = "0"+menit
                        }else{
                            mm = m.toString()
                        }


                        holder.itemView.et_time_hour_question.setText(jj)
                        holder.itemView.et_time_minute_question.setText(mm)
                        listViewType.get(position).answer = jj +" : " + mm

                    }),hour,minute,false)

                    tpd.show()
                }
            }
            Form().TYPE_MULTIPLE ->{
                holder.itemView.tv_multiple_question.text = question
                if (required){
                    holder.itemView.tv_multiple_required.visibility = View.VISIBLE
                }else{
                    holder.itemView.tv_multiple_required.visibility = View.GONE
                }
                for (i:Int in 0 until valItem.size){
                    val radio = RadioButton(context)
                    radio.setText(valItem.get(i).value)
                    holder.itemView.radioGroup.addView(radio)
                }

                holder.itemView.radioGroup.setOnCheckedChangeListener(
                    RadioGroup.OnCheckedChangeListener { group, checkedId ->
                        val radio: RadioButton = holder.itemView.findViewById(checkedId)
                        listViewType.get(position).answer = radio.text.toString()
                        Toast.makeText(context, radio.text.toString(), Toast.LENGTH_LONG).show()
                    })

            }
            Form().TYPE_CHECKBOX -> {
                holder.itemView.tv_checkbox_question.text = question
                if (required){
                    holder.itemView.tv_checkbox_required.visibility = View.VISIBLE
                }else{
                    holder.itemView.tv_checkbox_required.visibility = View.GONE
                }
                val adapterCheck = object: CheckboxRecycler(context, valItem){
                    override fun OnChecked(item: ArrayList<String>) {
                        val list = TextUtils.join(",", item)
                        Toast.makeText(context, list, Toast.LENGTH_LONG).show()
                        listViewType.get(position).answer = TextUtils.join(",", item)
                    }
                }
                holder.itemView.recycler_checkbox.adapter = adapterCheck
                holder.itemView.recycler_checkbox.layoutManager = LinearLayoutManager(context)

            }
            else ->{
                holder.itemView.tv_drop_down_question.text = question
                if (required){
                    holder.itemView.tv_drop_down_required.visibility = View.VISIBLE
                }else{
                    holder.itemView.tv_drop_down_required.visibility = View.GONE
                }
                holder.itemView.spin_drop_down_question.adapter = ArrayAdapter<FormItem>(context, android.R.layout.simple_spinner_dropdown_item, valItem)
                holder.itemView.spin_drop_down_question.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, posisi: Int, id: Long) {
                        listViewType.get(position).answer = holder.itemView.spin_drop_down_question.selectedItem.toString()
                       // Toast.makeText(context, listViewType.get(position).answer, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }



    override fun getItemCount(): Int = listViewType.size

    override fun getItemViewType(position: Int): Int = listViewType.get(position).type

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderItem(itemView: View) : ViewHolder(itemView)

    abstract fun OnClick(position: Int)

}