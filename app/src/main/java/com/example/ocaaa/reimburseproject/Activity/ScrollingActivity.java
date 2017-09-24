package com.example.ocaaa.reimburseproject.Activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScrollingActivity extends AppCompatActivity {

    private TextView category;
    private TextView cost;
    private TextView date;
    private TextView details;
    private TextView status;
    private TextView id;
    private ImageView receipt;
    private ImageView iconcategory;
    private Button back;
    private Button delete;
    private RelativeLayout relativeLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Snackbar snackbar;
    private User user;

    private Reimburse reimburse;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(" ");

        receipt = (ImageView) findViewById(R.id.ivReimburseDetailPic);

        snackbar = Snackbar.make(coordinatorLayout, R.string.fetch_reimburse_data_msg,Snackbar.LENGTH_INDEFINITE);

        String rid = getIntent().getStringExtra("reimburse_id");
        user = (User) getIntent().getSerializableExtra("user");

        getReimburseData(rid, user.getToken());


        category = (TextView) findViewById(R.id.tvReimburseDetailCategory);
        cost = (TextView) findViewById(R.id.tvReimburseDetailCost);
        date = (TextView) findViewById(R.id.tvReimburseDetailDate);
        details = (TextView) findViewById(R.id.tvReimburseDetailDetails);
        status = (TextView) findViewById(R.id.tvReimburseDetailStatus);
        back = (Button) findViewById(R.id.btReimburseDetailBack);
        delete = (Button) findViewById(R.id.btReimburseDetailDelete);
        iconcategory = (ImageView) findViewById(R.id.ivReimburseDetailCategory);
        id = (TextView) findViewById(R.id.tvReimburseDetailID);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                deleteReimburse(reimburse.getId(), user.getToken());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Reimburse detail");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
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
    public void insertData(Reimburse reimburse){
        if(reimburse.getImage()!=null)
            receipt.setImageBitmap(reimburse.getImage());
        else
            Log.e("IMAGE", "NOT EXIST");
        category.setText(reimburse.getCategory());
        details.setText(reimburse.getDetails());
        receipt.setImageBitmap(reimburse.getImage());
        status.setText(reimburse.getStatusText());
        status.setTextColor(reimburse.getColor());
        iconcategory.setImageResource(reimburse.getIcon());
        id.setText(id.getText()+reimburse.getId());

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
