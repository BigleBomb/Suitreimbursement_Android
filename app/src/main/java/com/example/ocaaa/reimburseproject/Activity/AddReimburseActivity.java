package com.example.ocaaa.reimburseproject.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ocaaa.reimburseproject.APIController.APIClient;
import com.example.ocaaa.reimburseproject.APIController.APIInterface;
import com.example.ocaaa.reimburseproject.Model.Project;
import com.example.ocaaa.reimburseproject.Model.ReimburseResponse;
import com.example.ocaaa.reimburseproject.Model.User;
import com.example.ocaaa.reimburseproject.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReimburseActivity extends AppCompatActivity {


    private static final int CAMERA_REQUEST = 0;
    private static final int IMAGE_REQUEST = 1;
    private Button btUpload;
    private EditText etBiaya;
    private ImageButton btCamera;
    private EditText etKeterangan;
    private EditText etTanggal;
    private ImageView ivStruk;
    private Button btSave;
    private Button btCancel;
    private TextView tvJenis;
    private File picture;
    private Snackbar snackbar;
    private RelativeLayout relativeLayout;

    private boolean running;

    private Uri imageUri;

    String imagePath;

    String user_id;
    String project_id;
    String tanggal;
    String token;
    String kategori;
    private User user;
    private Project project;
    private ReimburseResponse RR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reimburse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        relativeLayout = (RelativeLayout) findViewById(R.id.addReimburseRelativeLayout);

        snackbar = Snackbar.make(relativeLayout, getResources().getString(R.string.submit_reimburse_msg), Snackbar.LENGTH_INDEFINITE);

        kategori = getIntent().getStringExtra("category");

        user = (User) getIntent().getSerializableExtra("user");
        project = (Project) getIntent().getSerializableExtra("project");

        user_id = user.getId();
        project_id = project.getId();
        token = user.getToken();

        tvJenis = (TextView) findViewById(R.id.tvJenis);
        tvJenis.setText(getCategoryText(kategori));

        ivStruk = (ImageView) findViewById(R.id.ivStruk);

        btUpload = (Button) findViewById(R.id.btUpload);
        btSave = (Button) findViewById(R.id.btSave);
        btCancel = (Button) findViewById(R.id.btCancel);
        btCamera = (ImageButton) findViewById(R.id.btCamera);

        etTanggal = (EditText) findViewById(R.id.etTanggal);
        etBiaya = (EditText) findViewById(R.id.etBiaya);
        etKeterangan = (EditText) findViewById(R.id.etKeterangan);

        etTanggal.setTextColor(Color.BLACK);
        etBiaya.setTextColor(Color.BLACK);
        etKeterangan.setTextColor(Color.BLACK);

        btCamera.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AddReimburseActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        });

        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        int s=monthOfYear+1;
                        etTanggal.setText(year+"-"+s+"-"+dayOfMonth);
                        tanggal = etTanggal.getText().toString();
                    }
                };

                Time date = new Time();
                DatePickerDialog datePicker = new DatePickerDialog(AddReimburseActivity.this, dpd, date.year ,date.month, date.monthDay);
                Calendar cal=Calendar.getInstance();

                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                datePicker.updateDate(year, month, day);
                datePicker.show();

            }
        });

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST);
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etBiaya.getText().toString().isEmpty()){
                    etBiaya.setError(getResources().getString(R.string.empty_cost_msg));
                    etBiaya.requestFocus();
                }
                else if(etTanggal.getText().toString().isEmpty()){
                    etTanggal.setError(getResources().getString(R.string.empty_date_msg));
                    etTanggal.requestFocus();
                }
                else if(etKeterangan.getText().toString().isEmpty()){
                    etKeterangan.setError(getResources().getString(R.string.empty_details_msg));
                    etKeterangan.requestFocus();
                }
                else if(picture == null){
                    Toast.makeText(AddReimburseActivity.this, R.string.empty_file_msg, Toast.LENGTH_SHORT).show();
                }
                else
                    addReimburse();

            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        if(!running)
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeNewPicture();
                } else {
                    Toast.makeText(AddReimburseActivity.this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public int getCategoryText(String category) {
        switch(category){
            case "Transportasi":
                return R.string.categoryTransportation;
            case "Konsumsi":
                return R.string.categoryConsumption;
            case "Akomodasi":
                return R.string.categoryAccommodation;
        }
        return 0;
    }

    private void addReimburse(){
        running = true;

        snackbar.setText(R.string.submit_reimburse_msg);
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        APIInterface apiService =
                APIClient.getClient().create(APIInterface.class);

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(imageUri)),
                        picture
                );

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", picture.getName(), requestFile);

        RequestBody uid = RequestBody.create(okhttp3.MultipartBody.FORM, user_id);
        RequestBody pid = RequestBody.create(okhttp3.MultipartBody.FORM, project_id);
        RequestBody date = RequestBody.create(okhttp3.MultipartBody.FORM, tanggal);
        RequestBody category = RequestBody.create(okhttp3.MultipartBody.FORM, kategori);
        RequestBody cost = RequestBody.create(okhttp3.MultipartBody.FORM, etBiaya.getText().toString());
        RequestBody details = RequestBody.create(okhttp3.MultipartBody.FORM, etKeterangan.getText().toString());

        Call<ReimburseResponse> call = apiService.addReimburse(
                uid,
                pid,
                date,
                category,
                cost,
                body,
                details,
                token
        );
        call.enqueue(new Callback<ReimburseResponse>() {
            @Override
            public void onResponse(Call<ReimburseResponse>call, Response<ReimburseResponse> response) {
                Log.e("[STATUS]", response.body().getStatus());
                RR = response.body();
                snackbar.setText(R.string.reimburse_new_success);
                running = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }

            @Override
            public void onFailure(Call<ReimburseResponse> call, Throwable t) {
                snackbar.setDuration(Snackbar.LENGTH_SHORT);
                snackbar.setText(R.string.reimburse_new_failed);
                Log.e("[ERROR]", t.toString());
                running = false;
            }
        });
    }

    private void takeNewPicture() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues(3);

        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Uri imageFilePathUri;
            if (imageUri != null) {
                Cursor imageCursor = getContentResolver().query(
                        imageUri, filePathColumn, null, null, null);
                if (imageCursor != null && imageCursor.moveToFirst()) {
                    int columnIndex = imageCursor.getColumnIndex(filePathColumn[0]);
                    imagePath = imageCursor.getString(columnIndex);
                    imageCursor.close();
                    imageFilePathUri = imagePath != null ? Uri
                            .parse(imagePath) : null;
                    Log.e("PATH", imagePath);
                    Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath);
                    ivStruk.setImageBitmap(bitmapImage);
                    picture = new File(imagePath);
                }
            }
        }

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                String path = getRealPathFromURI(imageUri);
                ivStruk.setImageBitmap(selectedImage);
                picture = new File(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddReimburseActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
    }

    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
