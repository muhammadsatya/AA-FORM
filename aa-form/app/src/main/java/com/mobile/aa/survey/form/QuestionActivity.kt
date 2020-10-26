package com.mobile.aa.survey.form

import android.Manifest
import android.app.ActionBar
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ipaulpro.afilechooser.utils.FileUtils
import com.mobile.aa.survey.form.ApiService.ApiRequest
import com.mobile.aa.survey.form.model.Form
import com.mobile.aa.survey.form.model.FormItem
import com.mobile.aa.survey.form.recycler.QuestionRecycler
import kotlinx.android.synthetic.main.activity_question.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Exception
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList

class QuestionActivity : AppCompatActivity() {
    lateinit var progressDialog: ProgressDialog
    private lateinit var recyclerView: RecyclerView
    lateinit var list: ArrayList<Form>
    lateinit var filePath: Uri
    var PICK_FILE_REQUEST: Int = 1
    var PICK_CAMERA_REQUEST: Int = 2
    var PICK_GALERY_REQUEST: Int = 3
    var REQ_PERMISSION_READ = 1000
    var REQ_PERMISSION_WRITE = 2000
    var positionAnswer : Int = 0
    var IDFORM : String = ""
    var TITLEFORM : String = ""
    lateinit var multiPart: MultipartBody.Part

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        recyclerView = findViewById(R.id.recycler_question)
        list = ArrayList<Form>()
        IDFORM = getIntent().getStringExtra("id")
        TITLEFORM =  getIntent().getStringExtra("title")
        tv_title_question.text = TITLEFORM
        getQuestion()
        btn_card_submit.setOnClickListener {
            var countRequired : Int = 0
            for (i in 0 until list.size) {
                val form = list.get(i)
                if (form.required && form.answer == ""){
                    countRequired++
                }
            }
            if (countRequired > 0){
                Toast.makeText(this, "Silahkan di isi terlebih dahulu form pertanyaan yang wajib di isi!", Toast.LENGTH_SHORT).show()
            }else{
                Log.d("Answers", generateJSON())
                val params = HashMap<String, String>()
                params.put("question_id", IDFORM)
                params.put("answer", generateJSON())
                params.put("username", "admin")
                Log.d("Params", params.toString())
                progressDialogShow("", "Loading...")
                ApiRequest("trial").service().post(ApiRequest.API_POST_ANSWER, params).enqueue(object :
                    Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        progressDialogDismiss()
                        Toast.makeText(this@QuestionActivity, "Failed OnFailure", Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        try {
                            val responses = JSONObject(response.body()?.string())
                            Log.d("Responses", responses.toString())
                            progressDialogDismiss()
                            if (responses.getString("status").toLowerCase().equals("success")) {

                            }
                            Toast.makeText(
                                this@QuestionActivity,
                                responses.getString("status"),
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                            progressDialogDismiss()
                            Toast.makeText(
                                this@QuestionActivity,
                                "Failed OnResponse",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })
            }
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQ_PERMISSION_READ)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQ_PERMISSION_WRITE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            REQ_PERMISSION_READ -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()

            }
            REQ_PERMISSION_WRITE ->{
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getQuestion() {
        val params = HashMap<String, String>()
        params.put("id", IDFORM)
        progressDialogShow("", "Loading...")
        ApiRequest("trial").service().get(ApiRequest.API_GET_QUESTION, params).enqueue(object :
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("Error", t.toString())
                Toast.makeText(this@QuestionActivity, t.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    recyclerView.layoutManager = LinearLayoutManager(this@QuestionActivity)
                    progressDialogDismiss()
                    val respon = response.body()?.string()
                    Log.d("ResponQuestion", respon)
                    val jsonArray = JSONArray(respon)
                    for (i: Int in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val form = Form()
                        form.type = form.formType.keys.toList()
                            .get(form.formType.values.toList().indexOf(jsonObject.getString("type")))
                        form.question = jsonObject.getString("question")
                        form.required = jsonObject.getBoolean("required")
                        if (jsonObject.getBoolean("required")){
                            tv_desc_required.visibility = View.VISIBLE
                        }
                        val valItem = ArrayList<FormItem>()
                        if (form.type > 90) {
                            val values = jsonObject.getJSONArray("value")
                            for (i: Int in 0 until values.length()) {
                                val item = FormItem()
                                item.value = values.getString(i)
                                valItem.add(item)
                            }
                        }
                        form.list = valItem
                        list.add(form)
                    }

                    val adapterRecyclerView =
                        object : QuestionRecycler(this@QuestionActivity, list) {
                            override fun OnClick(position: Int) {
                                positionAnswer = position
                                showDialog()
                            }
                        }

                    recyclerView.adapter = adapterRecyclerView
                } catch (e: Exception) {
                    progressDialogDismiss()
                    Log.d("ErrorJSON", e.toString())
                    Toast.makeText(this@QuestionActivity, e.toString(), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun generateJSON(): String {
        val jsonArray = JSONArray()
        for (i in 0 until list.size) {
            val form = list.get(i)
            val jsonObject = JSONObject()
            jsonObject.put("type", form.formType.get(form.type))
            jsonObject.put("question", form.question)
            jsonObject.put("required", form.required)
            jsonObject.put("value", form.list)
            jsonObject.put("answer", form.answer)
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.choose_file_dialog)
        val document = dialog.findViewById(R.id.lin_document) as LinearLayout
        val audio = dialog.findViewById(R.id.lin_audio) as LinearLayout
        val video = dialog.findViewById(R.id.lin_video) as LinearLayout
        val galery = dialog.findViewById(R.id.lin_galery) as LinearLayout

        val cardDocument = dialog.findViewById(R.id.card_document) as CardView
        val cardAudio = dialog.findViewById(R.id.card_audio) as CardView
        val cardVideo = dialog.findViewById(R.id.card_video) as CardView
        val cardGalery = dialog.findViewById(R.id.card_galery) as CardView
        document.setOnClickListener {
            showChooseFile()
            dialog.dismiss()
        }
        audio.setOnClickListener {
            showChooseAudio()
            dialog.dismiss()
        }
        video.setOnClickListener {
            showChooseVideo()
            dialog.dismiss()
        }
        galery.setOnClickListener {
            showChooseGalery()
            dialog.dismiss()
        }

        cardDocument.setOnClickListener {
            showChooseFile()
            dialog.dismiss()
        }
        cardAudio.setOnClickListener {
            showChooseAudio()
            dialog.dismiss()
        }
        cardVideo.setOnClickListener {
            showChooseVideo()
            dialog.dismiss()
        }
        cardGalery.setOnClickListener {
            showChooseGalery()
            dialog.dismiss()
        }

        dialog.show()
        val window: Window
        window = dialog.window
        window.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

    }

    fun showChooseFile() {
        val intent = Intent()
        intent.type = "application/*"
        intent.setAction(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Select file"), PICK_FILE_REQUEST)
    }

    fun showChooseAudio() {
        val intent = Intent()
        intent.type = "audio/*"
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select file"), PICK_GALERY_REQUEST)
    }

    fun showChooseVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select file"), PICK_GALERY_REQUEST)
    }

    fun showChooseGalery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select file"), PICK_GALERY_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("cekReq", requestCode.toString())
        Log.d("cekReslt", resultCode.toString())
        if (resultCode == RESULT_OK && data != null && data.data != null) {
            when (requestCode) {
                PICK_FILE_REQUEST -> {
                    filePath = data.data
                    Log.d("cekResult", filePath.toString())
                   uploadFile(filePath)
                }
                PICK_GALERY_REQUEST -> {
                    filePath = data.data
                    Log.d("cekResult", filePath.toString())
                    uploadFile(filePath)
                }
            }
        } else if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            when (requestCode) {
                PICK_CAMERA_REQUEST -> {
                    Log.d("cekCamera", "Masuk")
                    val extras = data!!.getExtras()
                    val bitmap = extras!!.get("data") as Bitmap
                    filePath = bitmapToFile(bitmap)
                    Log.d("cekResult", filePath.toString())
                    uploadFile(filePath)
                }
            }
        }
    }

    // Method to save an bitmap to a file
    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }

    @NonNull
    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(
            okhttp3.MultipartBody.FORM, descriptionString
        )
    }

    @NonNull
    private fun  prepareFilePart(partName : String,fileUri : Uri) : MultipartBody.Part {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        val file = FileUtils.getFile(this, fileUri)


        // create RequestBody instance from file
       val requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file)

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    fun uploadFile(uriFile : Uri){
        try {
            progressDialogShow("", "Uploading data...")
            val id = IDFORM
            val str = createPartFromString(id)
            val param = HashMap<String, RequestBody>()
            param.put("question_id", str)
            Log.d("cekParam", param.toString())
            ApiRequest("trial").service().uploadFileWithPartMap(param, prepareFilePart("file", uriFile))
                .enqueue(object :
                    Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d("OnFailUreUpload", t.toString())
                        progressDialogDismiss()
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        progressDialogDismiss()
                        val respon = response.body()?.string()
                        Log.d("OnResponseUpload", respon)
                        val jso = JSONObject(respon)
                        Log.d("filename", jso.getString("filename"))
                        list.get(positionAnswer).answer = jso.getString("filename")
                    }
                })
        }catch (e : Exception){
            progressDialogDismiss()
//            Toast.makeText(this, "Please upload file in your internal storage", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "Silahkan upload file dari internal penyimpanan anda", Toast.LENGTH_SHORT).show()
        }
    }

    fun progressDialogShow(title: String, message: String) {
        progressDialog = ProgressDialog.show(this, title, message)
    }

    fun progressDialogDismiss() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss()
    }

    override fun onBackPressed() {
        val intent = Intent(this@QuestionActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    super.onBackPressed()
    }

}
