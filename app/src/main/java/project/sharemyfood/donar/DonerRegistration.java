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
import project.sharemyfood.accepter.Accepter_Register_Activity;

public class DonerRegistration extends AppCompatActivity {

    private EditText doner_name_text,doner_email_text,doner_contact_text,doner_password_text,doner_address_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doner_registration);
        doner_address_text=(EditText)findViewById(R.id.doner_reg_address);
        doner_contact_text=(EditText)findViewById(R.id.doner_reg_contact);
        doner_name_text=(EditText)findViewById(R.id.doner_reg_name);
        doner_email_text=(EditText)findViewById(R.id.doner_reg_email);
        doner_password_text=(EditText)findViewById(R.id.doner_reg_password);
        Button btn_register=(Button)findViewById(R.id.btn_doner_reg);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(doner_name_text.getText().toString().trim().equals(""))
                {
                    doner_name_text.setError("Enter the username");
                    return;
                }if(doner_address_text.getText().toString().trim().equals(""))
                {
                    doner_address_text.setError("Enter the address");
                    return;
                }
                if(doner_email_text.getText().toString().trim().equals(""))
                {
                    doner_email_text.setError("Enter the email");
                    return;
                }
                if(doner_contact_text.getText().toString().trim().equals(""))
                {
                    doner_contact_text.setError("Enter the contact");
                    return;
                }
                if(doner_password_text.getText().toString().trim().equals(""))
                {
                    doner_password_text.setError("Enter the password");
                    return;
                }
                else
                {
                    registerDoner(doner_name_text.getText().toString().trim(),doner_address_text.getText().toString().trim(),doner_email_text.getText().toString().trim(),doner_contact_text.getText().toString().trim(),doner_password_text.getText().toString().trim());
                }
            }
        });

    }

    private void registerDoner(final String name, final String address, final String email, final String contact, final String password) {
        String API= Urls.USER_REGISTER_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(DonerRegistration.this, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            Toast.makeText(DonerRegistration.this, response.toString(),Toast.LENGTH_SHORT).show();
                            if( jsonObject.get("status").equals("success"))
                            {
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DonerRegistration.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",name);
                params.put("contact", contact);
                params.put("email",email);
                params.put("address",address);
                params.put("password",password);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
