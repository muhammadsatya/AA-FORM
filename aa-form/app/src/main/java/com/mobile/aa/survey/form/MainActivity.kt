package com.mobile.aa.survey.form

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.aa.survey.form.ApiService.ApiRequest
import com.mobile.aa.survey.form.recycler.listFormRecycler
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    lateinit var list: ArrayList<listFormRecycler.Item>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_form)
        recyclerView = findViewById(R.id.recycler_list_form)
        list = ArrayList<listFormRecycler.Item>()
        getListQuestion()
    }


    fun getListQuestion() {
        val params = HashMap<String, String>()
        params.put("id", "")
        ApiRequest("trial").service().get(ApiRequest.API_GET_QUESTION, params).enqueue(object :
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("Error", t.toString())
                Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                    val responses = JSONArray(response.body()?.string())
                    if (responses.getJSONObject(0).getString(ApiRequest.REST_STATUS).equals(ApiRequest.REST_STATUS_SUCCESS)) {
                        val datas = responses.getJSONObject(0).getJSONArray(ApiRequest.REST_MESSAGE)
                        val respon = response.body()?.string()
                        Log.d("ResponQuestion", datas.toString())
                        for (i: Int in 0 until datas.length()) {
                            val data = datas.getJSONObject(i)
                            val item = listFormRecycler.Item()
                            item.id = data.getString("question_id")
                            item.title = data.getString("question_title")
                            item.deskripsi = data.getString("question_desc")
                            item.totalquestion = data.getString("total_question")
                            list.add(item)
                        }
                        val adapterRecyclerView =
                            object : listFormRecycler(this@MainActivity, list) {
                                override fun OnClick(item: Item) {
                                    val intent =
                                        Intent(this@MainActivity, QuestionActivity::class.java)
                                    intent.putExtra("id", item.id)
                                    intent.putExtra("title", item.title)
                                    startActivity(intent)
                                    finish()
                                }
                            }

                        recyclerView.adapter = adapterRecyclerView
                    }
                } catch (e: Exception) {
                    Log.d("ErrorJSON", e.toString())
                    Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
