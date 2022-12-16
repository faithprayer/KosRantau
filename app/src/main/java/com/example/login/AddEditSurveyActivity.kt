package com.example.login

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.login.api.SurveyApi
import com.example.login.databinding.ActivityAddEditSurveyBinding
import com.example.login.models.Survey
import com.google.gson.Gson
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.activity_add_edit_survey.*
import kotlinx.android.synthetic.main.activity_add_edit_survey.edit_kos
import kotlinx.android.synthetic.main.activity_edit_kos.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class AddEditSurveyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditSurveyBinding
    private var PELANGGAN_ID_1 = "pelanggan_notification_01"
    private val notificationId2 = 102
    private var editKos: EditText? = null
    private var editPenyewa: EditText? = null
    private var editNotlp: EditText? = null
    private var editTanggalSurvey: EditText? = null
    private var editJamSurvey: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditSurveyBinding.inflate(layoutInflater)
        val view : View = binding!!.root
        setContentView(view)

        queue = Volley.newRequestQueue(this)

        editKos = findViewById(R.id.edit_kos)
        editPenyewa = findViewById(R.id.edit_penyewa)
        editNotlp = findViewById(R.id.edit_noTlp)
        editTanggalSurvey = findViewById(R.id.edit_tanggalSurvey)
        editJamSurvey = findViewById(R.id.edit_jamSurvey)

        layoutLoading = findViewById(R.id.layout_loading)

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener { finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)

        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getIntExtra("id", -1)

        if(id == -1) {
            tvTitle.setText("Tambah Survey")
            btnSave.setOnClickListener{
                val namaKos_survei = binding!!.editKos.text.toString()
                val namaPengguna_survei = binding!!.editPenyewa.text.toString()
                val nomor_telepon_survei = binding!!.editNoTlp.text.toString()
                val tanggalSurvei = binding!!.editTanggalSurvey.text.toString()
                val jamSurvei = binding!!.editJamSurvey.text.toString()

                edit_kos.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_kos.error = it
                    }
                    .check()
                edit_penyewa.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_penyewa.error = it
                    }
                    .check()
                edit_noTlp.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_noTlp.error = it
                    }
                    .check()
                edit_tanggalSurvey.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_tanggalSurvey.error = it
                    }
                    .check()
                edit_jamSurvey.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_jamSurvey.error = it
                    }
                    .addSuccessCallback {
                        createSurvey()
                        createPdf( namaKos_survei, namaPengguna_survei, nomor_telepon_survei, tanggalSurvei, jamSurvei)
                    }
                    .check()
            }
        } else {
            tvTitle.setText("Edit Survey")
            getSurveyById(id)
            btnSave.setOnClickListener {
                edit_kos.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_kos.error = it
                    }
                    .check()
                edit_penyewa.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_penyewa.error = it
                    }
                    .check()
                edit_noTlp.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_noTlp.error = it
                    }
                    .check()
                edit_tanggalSurvey.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_tanggalSurvey.error = it
                    }
                    .check()
                edit_jamSurvey.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_jamSurvey.error = it
                    }
                    .addSuccessCallback {
                        updateSurvey(id)
                    }
                    .check()
            }
        }
    }

    private fun getSurveyById(id: Int) {
        setLoading(true)
        val stringRequest: StringRequest = object : StringRequest(Method.GET, SurveyApi.GET_BY_ID_URL + id, Response.Listener { response ->
            Toast.makeText(this@AddEditSurveyActivity, "Data Berhasil diambil!!", Toast.LENGTH_SHORT).show()
            setLoading(false)
        }, Response.ErrorListener { error ->
            setLoading(false)
            try {
                val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                val errors = JSONObject(responseBody)
                Toast.makeText(this@AddEditSurveyActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
            } catch (e:Exception) {
                Toast.makeText(this@AddEditSurveyActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    private fun createSurvey() {
        setLoading(true)

        if(editKos!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditSurveyActivity, "Nama Kos tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else if(editPenyewa!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditSurveyActivity, "Nama Penyewa tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else if(editNotlp!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditSurveyActivity, "No Telepon tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else if(editTanggalSurvey!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditSurveyActivity, "Tanggal Survey tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else if(editJamSurvey!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditSurveyActivity, "Jam Survey tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else{
            val survey = Survey(
                0,
                editKos!!.text.toString(),
                editPenyewa!!.text.toString(),
                editNotlp!!.text.toString(),
                editTanggalSurvey!!.text.toString(),
                editJamSurvey!!.text.toString()
            )
            val stringRequest: StringRequest = object : StringRequest(Method.POST, SurveyApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var survey = gson.fromJson(response, Survey::class.java)
                if(survey != null)
                    MotionToast.Companion.darkToast(this@AddEditSurveyActivity,
                        "Pembuatan Berhasil",
                        "Data Berhasil Dibuat",
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this,www.sanju.motiontoast.R.font.helvetica_regular))
                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                createNotificationChannels()
                sendNotifications()
                finish()
                setLoading(false)
            }, Response.ErrorListener {
                setLoading(false)
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["namaKos_survei"] = edit_kos.text.toString()
                    params["namaPengguna_survei"] = edit_penyewa.text.toString()
                    params["nomor_telepon_survei"] = edit_noTlp.text.toString()
                    params["tanggalSurvei"] = edit_tanggalSurvey.text.toString()
                    params["jamSurvei"] = edit_jamSurvey.text.toString()
                    return params
                }
            }
            queue!!.add(stringRequest)
        }
        setLoading(false)
    }
    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(FileNotFoundException::class)
    private fun createPdf( namaKos_survei: String, namaPengguna_survei: String, nomor_telepon_survei:String, tanggalSurvei: String, jamSurvei: String) {
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "Data Survey Kos.pdf")
        FileOutputStream(file)

        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(5f, 5f, 5f, 5f)
        @SuppressLint("UseCompatLoadingForDrawables") val d = getDrawable(R.drawable.kos1)

        val bitmap = (d as BitmapDrawable?)!!.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapData = stream.toByteArray()
        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)
        val dataPemesanan = Paragraph("Infromasi Survey").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph(
            """
                Berikut adalah
                Data Survey Kos
                """.trimIndent()).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)

        val width = floatArrayOf(100f, 100f)
        val table = Table(width)

        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Nama Kos")))
        table.addCell(Cell().add(Paragraph(namaKos_survei)))
        table.addCell(Cell().add(Paragraph("Nama Penyewa")))
        table.addCell(Cell().add(Paragraph(namaPengguna_survei)))
        table.addCell(Cell().add(Paragraph("No WA yang bisa dihubungi")))
        table.addCell(Cell().add(Paragraph(nomor_telepon_survei)))
        table.addCell(Cell().add(Paragraph("Tanggal Survey")))
        table.addCell(Cell().add(Paragraph(tanggalSurvei)))
        table.addCell(Cell().add(Paragraph("Jam Survey")))
        table.addCell(Cell().add(Paragraph(jamSurvei)))
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        table.addCell(Cell().add(Paragraph("Tanggal Buat PDF")))
        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
        table.addCell(Cell().add(Paragraph("Pukul Pembuatan")))
        table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))

        val barcodeQRCode = BarcodeQRCode(
            """
                $namaKos_survei
                $namaPengguna_survei
                $nomor_telepon_survei
                $tanggalSurvei
                $jamSurvei
                ${LocalDate.now().format(dateTimeFormatter)}
                ${LocalTime.now().format(timeFormatter)}
                """.trimIndent())
        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(
            HorizontalAlignment.CENTER)

        document.add(image)
        document.add(dataPemesanan)
        document.add(group)
        document.add(table)
        document.add(qrCodeImage)

        document.close()
        Toast.makeText(this, "Pdf Created", Toast.LENGTH_LONG).show()
    }

    private fun updateSurvey(id: Int) {
        setLoading(true)
        val survey = Survey(
            id,
            editKos!!.text.toString(),
            editPenyewa!!.text.toString(),
            editNotlp!!.text.toString(),
            editTanggalSurvey!!.text.toString(),
            editJamSurvey!!.text.toString()
        )
        val stringRequest: StringRequest = object : StringRequest(Method.PUT, SurveyApi.UPDATE_URL + id, Response.Listener { response ->
            val gson = Gson()
            val kos = gson.fromJson(response, Survey::class.java)
            if(survey != null)
                MotionToast.Companion.darkToast(this@AddEditSurveyActivity,
                    "Pengeditan Berhasil",
                    "Data Berhasil Diedit",
                    MotionToast.TOAST_SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,www.sanju.motiontoast.R.font.helvetica_regular))
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            createNotificationChannels()
            sendNotifications()
            finish()
            setLoading(false)
        }, Response.ErrorListener { error ->
            setLoading(false)
            try {
                val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                val errors = JSONObject(responseBody)
                Toast.makeText(this@AddEditSurveyActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@AddEditSurveyActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["namaKos_survei"] = edit_kos.text.toString()
                params["namaPengguna_survei"] = edit_penyewa.text.toString()
                params["nomor_telepon_survei"] = edit_noTlp.text.toString()
                params["tanggalSurvei"] = edit_tanggalSurvey.text.toString()
                params["jamSurvei"] = edit_jamSurvey.text.toString()
                return params
            }
        }
        queue!!.add(stringRequest)

    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }

    private fun createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notif"
            val descriptionText = "Desc"

            val channel1 = NotificationChannel(
                PELANGGAN_ID_1,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotifications() {
        val builder = NotificationCompat.Builder(this,PELANGGAN_ID_1)
            .setSmallIcon(R.drawable.ic_person)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setColor(Color.BLUE)
            .setContentTitle("Pelanggan Kos")
            .setContentText("Data Pelanggan Penyewa Kos")
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine("Nama Kos : "+ editKos!!.text.toString())
                    .addLine("Nama Penyewa"+ editPenyewa!!.text.toString())
                    .addLine("No Telepon : "+ editNotlp!!.text.toString())
                    .addLine("Tanggal Survey : "+ editTanggalSurvey!!.text.toString())
                    .addLine("Jam Survey : "+ editJamSurvey!!.text.toString())
            )
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId2, builder.build())
        }
    }
}