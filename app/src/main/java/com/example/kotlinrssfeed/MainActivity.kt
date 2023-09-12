package com.example.kotlinrssfeed


import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinrssfeed.Adapter.FeedAdapter
import com.example.kotlinrssfeed.Model.RSSObject
import com.example.kotlinrssfeed.common.HTTPDataHandler
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private val RSS_link = "https://techcrunch.com/feed/"
    private val RSS_to_json_api ="https://api.rss2json.com/v1/api.json?rss_url="
    lateinit var recyclerView: RecyclerView
    val resultLiveData = MutableLiveData<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val  toolbar : Toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recyclerView)
        toolbar.title = "NEWS"
        setSupportActionBar(toolbar)
        val linearLayoutManager = LinearLayoutManager(baseContext,LinearLayoutManager.VERTICAL,false)
        recyclerView.layoutManager = linearLayoutManager
        loadRSS()


    }

    private fun loadRSS() {
        val loadRSSAsync = object : AsyncTask<String, String, String>(){
            internal var mDialog = ProgressDialog(this@MainActivity)
            override fun onPreExecute() {
                mDialog.setMessage("Pleace wait...")
                mDialog.show()
            }
            override fun onPostExecute(result: String?) {
                mDialog.dismiss()
                var rssObject:RSSObject
                rssObject = Gson().fromJson<RSSObject>(result,RSSObject::class.java!!)
                val adapter = FeedAdapter(rssObject, baseContext)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()

            }
            override fun doInBackground(vararg p0: String?): String {
                val result:String
                val http = HTTPDataHandler()
                result = http.GetHTTPDataHandler(p0[0])
                return result
            }


        }

        val url_get_data = StringBuilder(RSS_to_json_api)
        url_get_data.append(RSS_link)
        loadRSSAsync.execute(url_get_data.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_refresh)
            loadRSS()
        return true
    }


}