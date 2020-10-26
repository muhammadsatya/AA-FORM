package com.mobile.aa.survey.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobile.aa.survey.form.ApiService.ApiRequest
import com.mobile.aa.survey.form.model.Form
import com.mobile.aa.survey.form.recycler.FormRecycler
import kotlinx.android.synthetic.main.activity_create.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class CreateActivity : AppCompatActivity() {
    lateinit var itemList: ArrayList<Form>
    lateinit var adapter: FormRecycler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        itemList = ArrayList<Form>()
        itemList.add(Form())
        adapter = FormRecycler(this, itemList)
        recycler_form.adapter = adapter
        recycler_form.layoutManager = LinearLayoutManager(this)

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_save) {
            val params = HashMap<String, String>()
            params.put("username", "admin")
            params.put("title", tv_title_question.text.toString())
            params.put("desc", tv_desc_question.text.toString())
            params.put("question", generateJSON())
            ApiRequest("trial").service().post(ApiRequest.API_POST_QUESTION, params).enqueue(object:
                Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    result.onFailed()
                    Toast.makeText(this@CreateActivity, "Failed", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    try {
                        val responses = JSONObject(response.body()?.string())
                        if(responses.getString("status").toLowerCase().equals("success")){
                            tv_title_question.setText("")
                            tv_desc_question.setText("")
                            itemList.clear()
                            itemList.add(Form())
                            adapter.notifyDataSetChanged()
                        }
                        Toast.makeText(this@CreateActivity, responses.getString("status"), Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@CreateActivity, "Failed", Toast.LENGTH_LONG).show()
                    }
                }
            })
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.question -> {
//                text.setText("question")
                itemList.add(Form())
                adapter.notifyItemInserted(itemList.size)
                return@OnNavigationItemSelectedListener true
            }
            R.id.photo -> {
//                text.setText("photo")
                return@OnNavigationItemSelectedListener true
            }
            R.id.video -> {
//                text.setText("video")
                return@OnNavigationItemSelectedListener true
            }
            R.id.description -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun generateJSON():String{
        val jsonArray = JSONArray()
        for(i in 0 until itemList.size){
            val form = itemList.get(i)
            val jsonObject = JSONObject()
            jsonObject.put("type", form.formType.get(form.type))
            jsonObject.put("question", form.question)
            jsonObject.put("required", form.required)
            jsonObject.put("value", form.list)
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }
}
