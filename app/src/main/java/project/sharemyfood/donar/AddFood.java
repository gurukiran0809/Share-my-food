package project.sharemyfood.donar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import project.sharemyfood.R;
import project.sharemyfood.Urls;

public class AddFood extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient mGoogleApiClient;
    String latitude,longitude;
    EditText name,type,cuisine,quantity,cookedTime,validity;
    ImageView food_img;
    Button btn_add_food;
    private Bitmap bitmap;
    private String selectedImagePath = "";
    final private int PICK_IMAGE = 1;
    final private int CAPTURE_IMAGE = 2;
    private String imgPath;
    private int PICK_IMAGE_REQUEST = 1;
    String user_id;
    private Uri mImageUri;
    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        Bundle extras = getIntent().getExtras();
        user_id=extras.getString("user_id");
        name=(EditText)findViewById(R.id.food_name);
        type=(EditText)findViewById(R.id.food_type);
        cuisine=(EditText)findViewById(R.id.food_cuisine);
        quantity=(EditText)findViewById(R.id.food_quantity);
        cookedTime=(EditText)findViewById(R.id.food_cookedat);
        validity=(EditText)findViewById(R.id.food_validity);
        food_img=(ImageView)findViewById(R.id.food_image);
        btn_add_food=(Button)findViewById(R.id.btn_add_food);

        btn_add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equals("")) {
                    name.setError("Enter the Food Name");
                    return;
                }
                if (type.getText().toString().trim().equals("")) {
                    type.setError("Enter the Food Type");
                    return;
                }
                if (cuisine.getText().toString().trim().equals("")) {
                    cuisine.setError("Enter the Food Cuisine");
                    return;
                }
                if (quantity.getText().toString().trim().equals("")) {
                    quantity.setError("Enter the Food Quantity");
                    return;
                }
                if (cookedTime.getText().toString().trim().equals("")) {
                    cookedTime.setError("Enter Cooked Time");
                    return;
                }
                try {
                    uploadImage(imgPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        food_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPhoto();
            }
        });
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }
    public void capturePhoto(View v){
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        startActivityForResult(intent, CAPTURE_IMAGE);
    }

    public void pickPhoto(){
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

//    public String getFilePath(Context c,Uri uri) {
//        String[] projection = { MediaStore.MediaColumns.DATA };
//        @SuppressWarnings("deprecation")
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        if (cursor != null) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } else
//            return null;
//    }
    public String getImagePath() {
        return imgPath;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK) {
            selectedImagePath = getImagePath();
            food_img.setImageBitmap(decodeFile(selectedImagePath));
            try{
                imgPath=selectedImagePath;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            System.out.println(data);
            mImageUri = data.getData();
            System.out.println("21845"+mImageUri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                // Log.d(TAG, String.valueOf(bitmap));


                food_img.setImageBitmap(bitmap);
                String imgPathl = getFilePath(this,mImageUri);
                imgPath=imgPathl;
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

//        finish();
    }

    public void uploadImage(String imgPath) throws Exception {

        String food_name=name.getText().toString().trim();
        String food_type=type.getText().toString().trim();
        String food_cuisine=cuisine.getText().toString().trim();
        String food_quantity=quantity.getText().toString().trim();
        String food_cookedat=cookedTime.getText().toString().trim();
        String food_validity=validity.getText().toString().trim();



        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .addFormDataPart("name",food_name)
                .addFormDataPart("food_type",food_type)
                .addFormDataPart("cuisine",food_cuisine)
                .addFormDataPart("location",latitude+","+longitude)
                .addFormDataPart("quantity",food_quantity)
                .addFormDataPart("cooked_time",food_cookedat)
                .addFormDataPart("validity",food_validity)
                .addFormDataPart("image", user_id+food_name+".png",
                        RequestBody.create(MEDIA_TYPE_PNG, new File(imgPath)))
                .build();

        Request request = new Request.Builder()
                .url(Urls.FOOD_ADD_URL)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);



                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                finish();

            }
        });
    }



    public static String getFilePath(Context context, Uri uri)
    {
        int currentApiVersion;
        try
        {
            currentApiVersion = android.os.Build.VERSION.SDK_INT;
        }
        catch(NumberFormatException e)
        {
            //API 3 will crash if SDK_INT is called
            currentApiVersion = 3;
        }
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {
            String filePath = "";
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst())
            {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }
        else if (currentApiVersion <= Build.VERSION_CODES.HONEYCOMB_MR2 && currentApiVersion >= Build.VERSION_CODES.HONEYCOMB)

        {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null)
            {
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }
        else
        {

            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
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

}
