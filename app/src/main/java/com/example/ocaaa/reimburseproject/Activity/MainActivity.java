package com.example.ocaaa.reimburseproject.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ocaaa.reimburseproject.APIController.APIClient;
import com.example.ocaaa.reimburseproject.APIController.APIInterface;
import com.example.ocaaa.reimburseproject.Model.Project;
import com.example.ocaaa.reimburseproject.Model.ProjectList;
import com.example.ocaaa.reimburseproject.Model.User;
import com.example.ocaaa.reimburseproject.Adapter.ProjectAdapter;
import com.example.ocaaa.reimburseproject.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList<Project> list = new ArrayList<>();
    private ListView listProject;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProjectAdapter adapter;
    private LinearLayout linearLayout;

    private Snackbar snackbar;

    private AlertDialog alertDialog;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        listProject = (ListView) findViewById(R.id.lv_Main);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);

        listProject.setEmptyView(findViewById(R.id.emptyViewMain));

        linearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);

        user = (User) getIntent().getSerializableExtra("user");

        snackbar = Snackbar.make(linearLayout, getResources().getString(R.string.fetch_project_msg), Snackbar.LENGTH_INDEFINITE);
        snackbar.show();

        getListReimburse(user.getId(), user.getToken());

        adapter = new ProjectAdapter(MainActivity.this, list);

        listProject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ProjectDetailActivity.class);
                intent.putExtra("project", list.get(position));
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);

                        snackbar.show();

                        getListReimburse(user.getId(), user.getToken());
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.logout_msg);
        alertDialogBuilder.setPositiveButton(R.string.confirmation_positive,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences userdata = getSharedPreferences("USER_DATA", 0);
                        SharedPreferences.Editor editor = userdata.edit();
                        editor.remove("USER_DATA");
                        editor.apply();
                        Toast.makeText(MainActivity.this, R.string.logged_out_msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton(R.string.confirmation_negative,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alertDialog = alertDialogBuilder.create();

        switch (item.getItemId()) {
            case R.id.menu_logout:
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        getListReimburse(user.getId(), user.getToken());
        adapter.notifyDataSetChanged();
    }

    public void getListReimburse(String id, String token){

        APIInterface apiService =
                APIClient.getClient().create(APIInterface.class);

        Call<ProjectList> call = apiService.getProject(id, token);
        call.enqueue(new Callback<ProjectList>() {
            @Override
            public void onResponse(Call<ProjectList>call, Response<ProjectList> response) {
                Log.e("[STATUS]", response.body().getStatus());
                if (response.body().getMessage() == null) {
                    ArrayList<Project> projects = response.body().getProjectList();
                    list = projects;
                    Log.e("[RESPONSE]", "Number of project received: " + projects.size());
                    adapter = new ProjectAdapter(MainActivity.this, list);
                    listProject.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                    if(snackbar.isShown())
                        snackbar.dismiss();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    if(snackbar.isShown())
                        snackbar.dismiss();
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    MainActivity.this.finish();
                    SharedPreferences userdata = getSharedPreferences("USER_DATA", 0);
                    SharedPreferences.Editor editor = userdata.edit();
                    editor.remove("USER_DATA");
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ProjectList> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                snackbar.setText(getResources().getString(R.string.error_msg_fetch_project));
                Log.e("[ERROR]", t.toString());
            }
        });
    }
}
