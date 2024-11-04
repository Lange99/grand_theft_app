package com.example.chatgptapp
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var messageInput: EditText
    private lateinit var responseText: TextView
    private lateinit var sendButton: Button
    private val client = OkHttpClient()
    private val apiKey = "YOUR_KEY" // Replace with your actual API key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageInput = findViewById(R.id.message_input)
        responseText = findViewById(R.id.response_text)
        sendButton = findViewById(R.id.send_button)

        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            if (message.isNotEmpty()) {
                sendRequest(message)
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendRequest(message: String) {
        val jsonBody = JSONObject().apply {
            put("model", "gpt-3.5-turbo")
            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", message)
                })
            })
            put("max_tokens", 100)
            put("temperature", 0.7)
        }

        val requestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(), jsonBody.toString()
        )

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(requestBody)
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    responseText.text = "Request failed: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        runOnUiThread {
                            responseText.text = "Unexpected code $it"
                        }
                        return
                    }

                    val jsonResponse = JSONObject(it.body?.string() ?: "")
                    val responseMessage = jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")

                    runOnUiThread {
                        responseText.text = responseMessage
                    }
                }
            }
        })
    }
}