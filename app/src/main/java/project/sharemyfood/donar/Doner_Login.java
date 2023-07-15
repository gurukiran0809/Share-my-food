package project.sharemyfood.donar;

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
import project.sharemyfood.accepter.Accepter_Home;
import project.sharemyfood.accepter.Accepter_Login;

public class Doner_Login extends AppCompatActivity {

    private EditText usernameText,passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donar_login);
        TextView reg_link=(TextView)findViewById(R.id.reg_link_doner);
        reg_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Doner_Login.this,DonerRegistration.class);
                startActivity(intent);
            }
        });
        Button btn_login=(Button)findViewById(R.id.btn_doner_login);
        usernameText=(EditText)findViewById(R.id.doner_username);
        passwordText=(EditText)findViewById(R.id.doner_password);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usernameText.getText().toString().trim().equals(""))
                {
                    usernameText.setError("Enter the username");
                    return;
                }
                if(passwordText.getText().toString().trim().equals(""))
                {
                    passwordText.setError("Enter the username");
                    return;
                }
                else
                {

                    String username=usernameText.getText().toString().trim();
                    String password=passwordText.getText().toString().trim();
                    donerLogin(username,password);
                }
            }
        });
    }


        private void donerLogin(final String username, final String password)
        {
            String API= Urls.USER_LOGIN_URL;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(Doner_Login.this, response, Toast.LENGTH_LONG).show();
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                Toast.makeText(Doner_Login.this, response.toString(),Toast.LENGTH_SHORT).show();
                                String user_id=jsonObject.get("user_id").toString();
                                if( jsonObject.get("status").equals("success"))

                                {

                                    Intent intent=new Intent(getApplicationContext(),DonerHome.class);
                                    intent.putExtra("user_id",user_id);
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
                            Toast.makeText(Doner_Login.this,error.toString(),Toast.LENGTH_LONG).show();
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

