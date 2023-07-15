package project.sharemyfood.donar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import project.sharemyfood.R;

public class DonerHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doner_home);
        Bundle extras = getIntent().getExtras();
        final String user_id=extras.getString("user_id");
        Button btn_add=(Button)findViewById(R.id.btn_doner_add_food);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AddFood.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
            }
        });
        Button btn_food_list=(Button)findViewById(R.id.btn_doner_foods);
        btn_food_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),DonerFoodList.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
            }
        });
        Button btn_show=(Button)findViewById(R.id.btn_doner_show_map);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AddFood.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
            }
        });
    }
}
