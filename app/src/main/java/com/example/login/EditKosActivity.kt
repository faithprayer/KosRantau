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
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.login.api.KosApi
import com.example.login.databinding.ActivityEditKosBinding
import com.example.login.models.Kos
import com.google.gson.Gson


import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.activity_edit_kos.*
import kotlinx.android.synthetic.main.adapter_kos.*
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
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
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EditKosActivity : AppCompatActivity() {
    private var kosId: Int = 0
    private lateinit var binding: ActivityEditKosBinding
    private var PELANGGAN_ID_1 = "pelanggan_notification_01"
    private val notificationId2 = 102
    private var editKos: EditText? = null
    private var editPengguna: EditText? = null
    private var editTanggalPesan: EditText? = null
    private var editTanggalMasuk: EditText? =null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditKosBinding.inflate(layoutInflater)
        val view : View = binding!!.root
        setContentView(view)

        queue = Volley.newRequestQueue(this)

        editKos = findViewById(R.id.edit_kos)
        editPengguna = findViewById(R.id.edit_pengguna)
        editTanggalPesan = findViewById(R.id.edit_tanggalPesan)
        editTanggalMasuk = findViewById(R.id.edit_tanggalMasuk)



        layoutLoading = findViewById(R.id.layout_loading)

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener { finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)


        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getIntExtra("id", -1)
        if(id == -1) {
            tvTitle.setText("Tambah Pesanan")
            btnSave.setOnClickListener {
                val nama_kos = binding!!.editKos.text.toString()
                val nama_pemesan = binding!!.editPengguna.text.toString()
                val tanggal_pesan = binding!!.editTanggalPesan.text.toString()
                val tanggal_masuk = binding!!.editTanggalMasuk.text.toString()



                edit_kos.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_kos.error = it
                    }
                    .check()

                edit_pengguna.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_pengguna.error = it
                    }
                    .check()

                edit_tanggalMasuk.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_tanggalMasuk.error = it
                    }
                    .check()

                edit_tanggalPesan.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_tanggalPesan.error = it
                    }
                    .check()
                createPdf(nama_kos, nama_pemesan, tanggal_pesan, tanggal_masuk)
                createPesanan() }

        } else {
            tvTitle.setText("Edit Pesanan")
            getPesananById(id)
            btnSave.setOnClickListener {

                edit_kos.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_kos.error = it
                    }
                    .check()

                edit_pengguna.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_pengguna.error = it
                    }
                    .check()

                edit_tanggalMasuk.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_tanggalMasuk.error = it
                    }
                    .check()

                edit_tanggalPesan.validator()
                    .nonEmpty()
                    .addErrorCallback {
                        edit_tanggalPesan.error = it
                    }
                    .check()
                updatePesanan(id) }
        }
    }
    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(FileNotFoundException::class)
    private fun createPdf(namaKos: String, namaPemesan: String, tanggalPesan: String, tanggalMasuk: String) {
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "Data Pemesanan Kos.pdf")
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
        val dataPemesanan = Paragraph("Infromasi Pemesanan").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph(
            """
                Berikut adalah
                Data Pemesanan Kos
                """.trimIndent()).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)

        val width = floatArrayOf(100f, 100f)
        val table = Table(width)

        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Nama Kos")))
        table.addCell(Cell().add(Paragraph(namaKos)))
        table.addCell(Cell().add(Paragraph("Nama Pemesan")))
        table.addCell(Cell().add(Paragraph(namaPemesan)))
        table.addCell(Cell().add(Paragraph("Tanggal Pemesanan")))
        table.addCell(Cell().add(Paragraph(tanggalPesan)))
        table.addCell(Cell().add(Paragraph("Tanggal Masuk")))
        table.addCell(Cell().add(Paragraph(tanggalMasuk)))
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        table.addCell(Cell().add(Paragraph("Tanggal Buat PDF")))
        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
        table.addCell(Cell().add(Paragraph("Pukul Pembuatan")))
        table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))

        val barcodeQRCode = BarcodeQRCode(
            """
                $namaKos
                $namaPemesan
                $tanggalPesan
                $tanggalMasuk
                ${LocalDate.now().format(dateTimeFormatter)}
                ${LocalTime.now().format(timeFormatter)}
                """.trimIndent())
        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(HorizontalAlignment.CENTER)

        document.add(image)
        document.add(dataPemesanan)
        document.add(group)
        document.add(table)
        document.add(qrCodeImage)

        document.close()
        Toast.makeText(this, "Pdf Created", Toast.LENGTH_LONG).show()
    }

    private fun getPesananById(id: Int) {
        setLoading(true)
        val stringRequest: StringRequest = object : StringRequest(Method.GET, KosApi.GET_BY_ID_URL + id, Response.Listener { response ->
            Toast.makeText(this@EditKosActivity, "Data Berhasil diambil!!", Toast.LENGTH_SHORT).show()
            setLoading(false)
        }, Response.ErrorListener { error ->
            setLoading(false)
            try {
                val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                val errors = JSONObject(responseBody)
                Toast.makeText(this@EditKosActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
            } catch (e:Exception) {
                Toast.makeText(this@EditKosActivity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createPesanan() {
        setLoading(true)
        val kos = Kos(
            0,
            editKos!!.text.toString(),
            editPengguna!!.text.toString(),
            editTanggalPesan!!.text.toString(),
            editTanggalMasuk!!.text.toString()
        )
        val stringRequest: StringRequest = object : StringRequest(Method.POST, KosApi.ADD_URL, Response.Listener { response ->
            val gson = Gson()
            var kos = gson.fromJson(response, Kos::class.java)
            if(kos != null)
                Toast.makeText(this@EditKosActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
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
                params["namaKos"] = edit_kos.text.toString()
                params["namaPengguna"] = edit_pengguna.text.toString()
                params["tanggalPesan"] = edit_tanggalPesan.text.toString()
                params["tanggalMasuk"] = edit_tanggalMasuk.text.toString()
                return params
            }
        }
        queue!!.add(stringRequest)

    }

    private fun updatePesanan(id: Int) {
        setLoading(true)
        val kos = Kos(
            id,
            editKos!!.text.toString(),
            editPengguna!!.text.toString(),
            editTanggalPesan!!.text.toString(),
            editTanggalMasuk!!.text.toString()
        )
        val stringRequest: StringRequest = object : StringRequest(Method.PUT, KosApi.UPDATE_URL + id, Response.Listener { response ->
            val gson = Gson()
            val kos = gson.fromJson(response, Kos::class.java)
            if(kos != null)
                Toast.makeText(this@EditKosActivity, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@EditKosActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@EditKosActivity, e.message, Toast.LENGTH_SHORT).show()
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
                params["namaKos"] = edit_kos.text.toString()
                params["namaPengguna"] = edit_pengguna.text.toString()
                params["tanggalPesan"] = edit_tanggalPesan.text.toString()
                params["tanggalMasuk"] = edit_tanggalMasuk.text.toString()
                return params
            }
//            @Throws(AuthFailureError::class)
//            override fun getBody(): ByteArray {
//                val gson = Gson()
//                val requestBody = gson.toJson(kos)
//                return requestBody.toByteArray(StandardCharsets.UTF_8)
//            }
//
//            override fun getBodyContentType(): String {
//                return "application/x-www-form-urlencoded"
//            }
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

            val notificationManager:NotificationManager =
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
                    .addLine("Nama Penyewa : "+ editPengguna!!.text.toString())
                    .addLine("Tanggal Pesan : "+ editTanggalPesan!!.text.toString())
                    .addLine("Tanggal Masuk : "+ editTanggalMasuk!!.text.toString())
            )
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId2, builder.build())
        }
    }

}