package ru.practicum.sprint_11_koh_33

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.sprint_11_koh_31.R
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val adapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val uri: Uri = Uri.parse("https://myserver.com:5051/api/v1/path?text=android&take=1#last")

        Log.d(TAG, "uri.scheme ${uri.scheme}")
        Log.d(TAG, "uri.host ${uri.host}")
        Log.d(TAG, "uri.authority ${uri.authority}")
        Log.d(TAG, "uri.pathSegments ${uri.pathSegments}")
        Log.d(TAG, "uri.lastPathSegment ${uri.lastPathSegment}")
        Log.d(TAG, "uri.queryParameterNames ${uri.queryParameterNames}")
        Log.d(TAG, "uri.getQueryParameter(\"text\") ${uri.getQueryParameter("text")}")
        Log.d(TAG, "uri.fragment ${uri.fragment}")

        val itemsRv: RecyclerView = findViewById(R.id.items)
        itemsRv.adapter = adapter

        val jsonConfiguration = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/BenDenen/sprint-11-koh-35/")
            .addConverterFactory(
                jsonConfiguration.asConverterFactory("application/json".toMediaType())
            )
//            .addConverterFactory(
//                GsonConverterFactory.create(
//                    GsonBuilder()
//                        .registerTypeAdapter(Date::class.java, CustomDateTypeAdapter())
//                        .registerTypeAdapter(NewsItem::class.java, NewsItemAdapter())
//                        .create()
//                )
//            )
            .build()
        val serverApi = retrofit.create(Sprint11ServerApi::class.java)

        serverApi.getNews1("news_2.json").enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                Log.i(TAG, "onResponse: ${response.code()} ${response.body()}")

                adapter.items = response.body()?.data?.items ?: emptyList()
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: $t")
            }

        })

    }

    companion object {
        const val TAG = "SPRINT_11"
    }
}


interface Sprint11ServerApi {

    @GET("refs/heads/master/jsons/{name}")
    fun getNews1(@Path("name") name:String): Call<NewsResponse>

}