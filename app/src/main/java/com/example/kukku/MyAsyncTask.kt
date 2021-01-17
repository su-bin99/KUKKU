package com.example.kukku

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.provider.DocumentsContract
import android.util.Log
import org.jsoup.Jsoup
import java.lang.ref.WeakReference
import java.net.URL
import android.view.LayoutInflater
import org.jsoup.nodes.Document
import java.lang.System.exit
import java.net.SocketTimeoutException


class MyAsyncTask(context : Context) : AsyncTask<URL, Unit, Array<String>>() {
    private val activityReference = WeakReference(context)

    override fun doInBackground(vararg params: URL?): Array<String>? {
        var doc :Document?= null
        var array:Array<String> ?= null
        try{
            doc = Jsoup.connect(params[0].toString()).get()
        }catch(e:RuntimeException){
            e.printStackTrace()
            return array
        }catch (e:SocketTimeoutException){
            e.printStackTrace()
            return array
        }

        val num = doc.select("td.table_bg_white")
        array = arrayOf(num[1].text(), num[2].text())
        return array
    }
}