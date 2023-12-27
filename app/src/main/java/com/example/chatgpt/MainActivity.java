package com.example.chatgpt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.privacysandbox.tools.core.model.Method;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.os.Bundle;
//import android.os.Message;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.example.chatgpt.Message;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLanguage;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity<JsonObjectRequest, Response, NetworkResponse> extends AppCompatActivity {



 private String apiurl = "https://api.openai.com/v1/completions";
 String accessToken="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";
 private static final int REQUEST_CODE_SPEECH_INPUT = 1;
 List<Message> messageList;
 MessageAdapter messageAdapter;
 RecyclerView recview;
 EditText editText;
 Button button;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);

  recview=findViewById(R.id.recycler_view);
  recview.setLayoutManager(new LinearLayoutManager(this));
  editText=findViewById(R.id.edit_text);
  button=findViewById(R.id.button);




  messageList=new ArrayList<>();
   messageAdapter = new MessageAdapter(messageList);
  recview.setAdapter(messageAdapter);


  button.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View view) {
    ProcessAI();
   }
  });



 }

 private void ProcessAI() {

  String text=editText.getText().toString();
  messageList.add(new Message(text,true));
  messageAdapter.notifyItemInserted(messageList.size() - 1);
  recview.scrollToPosition(messageList.size() - 1);
  editText.getText().clear();



  JSONObject requestBody=new JSONObject();
  try {
   requestBody.put("model", "text-davinci-003");
   requestBody.put("prompt", text);
   requestBody.put("max_tokens", 100);
   requestBody.put("temperature", 1);
   requestBody.put("top_p", 1);
   requestBody.put("frequency_penalty", 0.0);
   requestBody.put("presence_penalty", 0.0);

  }catch (Exception ex)
  {
   ex.printStackTrace();
  }

  JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, apiurl, requestBody, new Response.Listener<JSONObject>() {
   @Override
   public void onResponse(JSONObject response) {
    try {
     JSONArray js = response.getJSONArray("choices");
     JSONObject jsonObject=js.getJSONObject(0);
     String text=jsonObject.getString("text");
     messageList.add(new Message(text.replaceFirst("\n", "").replaceFirst("\n", ""), false));
     messageAdapter.notifyItemInserted(messageList.size() - 1);
     recview.scrollToPosition(messageList.size()-1);
    }catch (Exception ex)
    {
     ex.printStackTrace();
    }
   }
  }, new Response.ErrorListener() {
   @Override
   public void onErrorResponse(VolleyError error) {
    Log.e("API Error",error.toString());
    messageList.add(new Message(error.toString().replaceFirst("\n", "").replaceFirst("\n", ""), false));
    messageAdapter.notifyItemInserted(messageList.size() - 1);
    recview.scrollToPosition(messageList.size()-1);
   }
  })
  {
   public Map<String,String> getHeaders() throws AuthFailureError
   {
    Map<String,String> headers= new HashMap<>();
    headers.put("Authorization","Bearer "+accessToken);
    headers.put("Content-Type","application/json");
    return  headers;
   }
   @Override
   protected Response < JSONObject > parseNetworkResponse(NetworkResponse response) {
    return super.parseNetworkResponse(response);
   }
  };

  int timeoutMs = 25000; // 25 seconds timeout
  RetryPolicy policy = new DefaultRetryPolicy(timeoutMs, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
  request.setRetryPolicy(policy);
  // Add the request to the RequestQueue
  MySingleton.getInstance(this).addToRequestQueue(request);
 }
}













