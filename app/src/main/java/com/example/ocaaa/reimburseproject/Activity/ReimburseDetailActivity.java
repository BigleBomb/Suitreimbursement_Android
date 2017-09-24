package com.example.ocaaa.reimburseproject.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ocaaa.reimburseproject.APIController.APIClient;
import com.example.ocaaa.reimburseproject.APIController.APIInterface;
import com.example.ocaaa.reimburseproject.Model.Reimburse;
import com.example.ocaaa.reimburseproject.Model.ReimburseResponse;
import com.example.ocaaa.reimburseproject.Model.User;
import com.example.ocaaa.reimburseproject.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReimburseDetailActivity extends AppCompatActivity {

    private TextView category;
    private TextView cost;
    private TextView date;
    private TextView details;
    private TextView status;
    private ImageView receipt;
    private Button back;
    private Button delete;
    private RelativeLayout relativeLayout;

    private File file;

    private Reimburse reimburse;
    private User user;

    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimburse_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarReimburseDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutReimburseDetail);

        snackbar = Snackbar.make(relativeLayout, R.string.fetch_reimburse_data_msg,Snackbar.LENGTH_INDEFINITE);

        String rid = getIntent().getStringExtra("reimburse_id");
        user = (User) getIntent().getSerializableExtra("user");

        getReimburseData(rid, user.getToken());

        category = (TextView) findViewById(R.id.tvReimburseDetailCategory);
        cost = (TextView) findViewById(R.id.tvReimburseDetail_Cost);
        date = (TextView) findViewById(R.id.tvReimburseDetailDate);
        details = (TextView) findViewById(R.id.tvReimburseDetailDetails);
        receipt = (ImageView) findViewById(R.id.ivReimburseDetailReceipt);
        back = (Button) findViewById(R.id.btReimburseDetailBack);
        delete = (Button) findViewById(R.id.btReimburseDetailDelete);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteReimburse(reimburse.getId(), user.getToken());
            }
        });

//        receipt.setOnClickListener(new View.OnClickListener() {
//            Uri uri;
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(file), "image/*");
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getReimburseData(String id, String token){

        snackbar.show();

        APIInterface apiService =
                APIClient.getClient().create(APIInterface.class);

        Call<ReimburseResponse> call = apiService.getReimburse(id, token);
        call.enqueue(new Callback<ReimburseResponse>() {
            @Override
            public void onResponse(Call<ReimburseResponse>call, Response<ReimburseResponse> response) {
                if(response.body().getMessage() == null){
                    reimburse = response.body().getReimburse();
                    Log.e("REIMBURSE", response.body().getReimburse().getCategory());
                    insertData(reimburse);
                    if(snackbar.isShown())
                        snackbar.dismiss();
                }else{
                    Log.e("REIMBURSE", response.body().getMessage());
                    snackbar.setText(response.body().getMessage());
                }

            }

            @Override
            public void onFailure(Call<ReimburseResponse> call, Throwable t) {
                snackbar.setText(getResources().getString(R.string.error_msg_fetch_reimburse_detail));
            }
        });
    }

    public void deleteReimburse(final String id, final String token){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.delete_reimburse_msg_title);
        alertDialogBuilder.setMessage(R.string.delete_reimburse_msg);
        alertDialogBuilder.setPositiveButton(R.string.confirmation_positive,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        APIInterface apiService =
                                APIClient.getClient().create(APIInterface.class);

                        Call<ReimburseResponse> call = apiService.deleteReimburse(id, token);
                        call.enqueue(new Callback<ReimburseResponse>() {
                            @Override
                            public void onResponse(Call<ReimburseResponse>call, Response<ReimburseResponse> response) {
                                if(response.body().getStatus() == "true"){
                                    snackbar.setText(R.string.reimburse_deleted_success);
                                    snackbar.show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 2000);
                                }else{
                                    snackbar.setText(R.string.reimburse_deleted_failed);
                                    snackbar.setDuration(Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ReimburseResponse> call, Throwable t) {
                                snackbar.setText(getResources().getString(R.string.reimburse_delete_error_msg));
                            }
                        });
                    }
                });

        alertDialogBuilder.setNegativeButton(R.string.confirmation_negative,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void insertData(Reimburse reimburse){
        category.setText(reimburse.getCategory());
        details.setText(reimburse.getDetails());
        receipt.setImageBitmap(reimburse.getImage());

        int number = reimburse.getCost();
        String str = String.format("%,d", number).replace(",", ".");
        cost.setText("Rp. "+str);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(reimburse.getDate());
            sdf.applyPattern("h:mm a\nd MMMM, y");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date.setText(sdf.format(d));
    }
}
