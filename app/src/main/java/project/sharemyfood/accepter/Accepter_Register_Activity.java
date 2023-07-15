package project.sharemyfood.accepter;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import project.sharemyfood.R;
import project.sharemyfood.Urls;

public class Accepter_Register_Activity extends AppCompatActivity  implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient mGoogleApiClient;

    EditText careNameText,careOwnernameText,caretype,careAddress,careContact,carePassword;
    Button btn_accepter_register;
    String latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepter__register);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        careNameText=(EditText)findViewById(R.id.care_reg_name);
        careOwnernameText=(EditText)findViewById(R.id.care_owner_reg_name);
        careAddress=(EditText)findViewById(R.id.care_reg_address);
        careContact=(EditText)findViewById(R.id.care_reg_contact);
        caretype=(EditText)findViewById(R.id.care_reg_type);
        carePassword=(EditText)findViewById(R.id.care_reg_password);
        btn_accepter_register=(Button)findViewById(R.id.btn_accepter_reg);

        btn_accepter_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(careNameText.getText().toString().trim().equals(""))
                {
                    careNameText.setError("Enter the care name");
                    return;
                }if(careOwnernameText.getText().toString().trim().equals(""))
                {
                    careOwnernameText.setError("Enter the owner name");
                    return;
                }if(careAddress.getText().toString().trim().equals(""))
                {
                    careAddress.setError("Enter the Address");
                    return;
                }if(careContact.getText().toString().trim().equals(""))
                {
                    careContact.setError("Enter the Care Contact");
                    return;
                }if(caretype.getText().toString().trim().equals(""))
                {
                    caretype.setError("Enter the Care Type");
                    return;
                }if(carePassword.getText().toString().trim().equals(""))
                {
                    carePassword.setError("Enter the password");
                    return;
                }
                else
                {
                    String name=careNameText.getText().toString().trim();
                    String ownername=careOwnernameText.getText().toString().trim();
                    String address=careAddress.getText().toString().trim();
                    String contact=careContact.getText().toString().trim();
                    String type=caretype.getText().toString().trim();
                    String password=carePassword.getText().toString().trim();

                    registerCare(name,ownername,address,contact,type,password);
                }
            }
        });


    }

    private void registerCare(final String name, final String ownername, final String address, final String contact, final String type, final String password) {
        String API= Urls.CARE_REGISTER_URL;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Accepter_Register_Activity.this, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            Toast.makeText(Accepter_Register_Activity.this, response.toString(),Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Accepter_Register_Activity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("care_name",name);
                params.put("care_type",type);
                params.put("owner_name",ownername);
                params.put("address",address);
                params.put("location",latitude+","+longitude);
                params.put("contact", contact);
                params.put("password",password);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Toast.makeText(getApplicationContext(), String.valueOf(mLastLocation.getLatitude()), Toast.LENGTH_SHORT).show();
            //  mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            latitude=String.valueOf(mLastLocation.getLatitude());
            longitude=String.valueOf(mLastLocation.getLongitude());
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

//    private void reset() {
//
//        careNameText.setText("");
//        careOwnernameText.setText("");
//        caretype.setText("");
//        careAddress.setText("");
//        careContact.setText("");
//        carePassword.setText("");
//    }
}
