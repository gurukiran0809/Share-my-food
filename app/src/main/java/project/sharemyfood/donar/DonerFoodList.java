package project.sharemyfood.donar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import project.sharemyfood.FoodObject;
import project.sharemyfood.R;
import project.sharemyfood.Urls;

public class DonerFoodList extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ArrayList<FoodObject> items = new ArrayList<>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doner_food_list);
        listView=(ListView)findViewById(R.id.food_list);
        Bundle extras = getIntent().getExtras();
        final String user_id=extras.getString("user_id");
        getFood(user_id);
    }

    public void getFood(final String user_id){

        final String GET_FOOD_URL=Urls.GET_FOOD_URL+"/"+user_id;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,GET_FOOD_URL , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            JSONArray foods = response.getJSONArray("foods");

                            for (int i =0; i<foods.length(); i++){
                                JSONObject object = foods.getJSONObject(i);
                                String  id=object.getString("id");
                                String user_idl=object.getString("user_id");
                                String name=object.getString("name");
                                String food_type=object.getString("foodType");
                                String cuisine=object.getString("cuisine");
                                String location=object.getString("location");
                                String quantity=object.getString("quantity");
                                String cooked_time=object.getString("cooked_time");
                                String validity=object.getString("validity");
                                String imgsrc=object.getString("imgsrc");

                                items.add(new FoodObject(id,user_id,name,food_type,cuisine,location,quantity,cooked_time,validity,imgsrc));
                                System.out.println(items);
                                ArrayAdapter<FoodObject> arrayAdapter = new ArrayAdapter<FoodObject>(getApplicationContext(), R.layout.list_content, R.id.food_text, items);
                                listView.setAdapter(arrayAdapter);

                            }
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                        // adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonRequest);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
