package com.udacity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {
    private var fileDownloadedName = ""; private var downloadStatus = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        fab.setOnClickListener {startActivity(Intent(this, MainActivity::class.java))}
        fileDownloadedName = intent.getStringExtra("fileName").toString()
        downloadStatus = intent.getStringExtra("status").toString()
        fileOfDownload.text = fileDownloadedName
        statusOfDownload.text = downloadStatus
    }
}
