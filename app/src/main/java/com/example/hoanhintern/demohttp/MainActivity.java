package com.example.hoanhintern.demohttp;

import android.app.Activity;
import android.app.VoiceInteractor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    TextView tv;
    Button bt,bt1;
    ImageView iv;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            tv = (TextView)findViewById(R.id.textView);
            bt = (Button)findViewById(R.id.button);
            iv = (ImageView)findViewById(R.id.imageView);
            bt1 = (Button)findViewById(R.id.button2);
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageRequest imageRequest = new ImageRequest("https://api.github.com/users/mojombo", new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            iv.setImageBitmap(response);
                        }
                    }, 0, 0, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onErrorResponse: " + error.getMessage());
                        }
                    });
                    VolleySingleton.getInstance(MainActivity.this).getRequestQueue().add(imageRequest);
                }
            });
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parse();

                }
            });






    }

    private void parse() {

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://raw.githubusercontent.com/SaiNitesh/REST-Web-services/master/RESTfulWS/json_file.json", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                                JSONArray jsonArray = response.getJSONArray("countryItems");
                                for (int i = 0;i<jsonArray.length();i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String nm = jsonObject.getString("nm");
                                    String cty = jsonObject.getString("cty");
                                    String hse = jsonObject.getString("hse");
                                    String yrs = jsonObject.getString("yrs");

//                                    tv.append(nm+cty+hse+yrs+"\n");
                                }
                                } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                    },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getStackTrace();
                       Log.d("Error",error.toString());

                    }
                });

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://api.github.com/users", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String name = response.getJSONObject(i).getString("login");
                        int id = response.getJSONObject(i).getInt("id");
                        String node = response.getJSONObject(i).getString("node_id");
                        tv.append("\n"+name+"\t"+id+"\t"+node+"\n");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
            }
        });

        String url = "https://www.google.com.vn/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
            }
        });




        VolleySingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);
        VolleySingleton.getInstance(this).getRequestQueue().add(jsonArrayRequest);
        VolleySingleton.getInstance(this).getRequestQueue().add(stringRequest);
//            requestQueue = Volley.newRequestQueue(this);
//            requestQueue.add(jsonObjectRequest);
    }
}
