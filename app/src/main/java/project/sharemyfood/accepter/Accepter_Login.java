package project.sharemyfood.accepter;

import android.Manifest;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import project.sharemyfood.R;
import project.sharemyfood.Urls;


public class Accepter_Login extends AppCompatActivity {


    EditText usernameText, passwordText;
    public String API;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepter_login);
        Button reg_link = (Button) findViewById(R.id.btn_accepter_signup);
        reg_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accepter_Login.this, Accepter_Register_Activity.class);
                startActivity(intent);
            }
        });

        System.out.println(API);
        Button btn_login = (Button) findViewById(R.id.btn_accepter_login);
        usernameText = (EditText) findViewById(R.id.accepter_login_contact);
        passwordText = (EditText) findViewById(R.id.accepter_login_password);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usernameText.getText().toString().trim().equals("")) {
                    usernameText.setError("Enter the username");
                    return;
                }
                if (passwordText.getText().toString().trim().equals("")) {
                    passwordText.setError("Enter the username");
                    return;
                } else {
                    String username=usernameText.getText().toString().trim();
                    String password=passwordText.getText().toString().trim();
                    accepterLogin(username,password );
                }
            }
        });
    }

    private void accepterLogin(final String username, final String password)
    {
        String API=Urls.CARE_LOGIN_URL;
        System.out.println("*************"+username+"**************"+password);
        System.out.println("************"+API);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Accepter_Login.this, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            Toast.makeText(Accepter_Login.this, response.toString(),Toast.LENGTH_SHORT).show();
                            if( jsonObject.get("status").equals("success"))
                            {
                                Intent intent=new Intent(getApplicationContext(),Accepter_Home.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Accepter_Login.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("contact", username);
                params.put("password",password);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



}

