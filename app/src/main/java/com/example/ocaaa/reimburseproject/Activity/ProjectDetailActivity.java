package com.example.ocaaa.reimburseproject.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ocaaa.reimburseproject.APIController.APIClient;
import com.example.ocaaa.reimburseproject.APIController.APIInterface;
import com.example.ocaaa.reimburseproject.Adapter.ViewPagerAdapter;
import com.example.ocaaa.reimburseproject.Fragment.CategoryFragment;
import com.example.ocaaa.reimburseproject.Model.Project;
import com.example.ocaaa.reimburseproject.Model.Reimburse;
import com.example.ocaaa.reimburseproject.Model.ReimburseList;
import com.example.ocaaa.reimburseproject.Model.User;
import com.example.ocaaa.reimburseproject.R;
import com.example.ocaaa.reimburseproject.Adapter.ReimburseAdapter;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ocaaa on 12/06/17.
 */

public class ProjectDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView reimburseListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView toolbarTitle;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    private CategoryFragment categoryFragment;

    private RelativeLayout relativeLayout;

    private Snackbar snackbar;

    private User user;
    private Project project;

    private String user_id;
    private String project_id;
    private String token;
    private int totalcost;

    private ArrayList<Reimburse> reimburses;
    private ArrayList<Reimburse> list;

    private ReimburseAdapter adapter;
    private ViewPagerAdapter viewPagerAdapter;

    private APIInterface apiService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProjectDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apiService =
                APIClient.getClient().create(APIInterface.class);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        snackbar = Snackbar
                .make(relativeLayout, getResources().getString(R.string.fetch_reimburse_msg), Snackbar.LENGTH_INDEFINITE);

        snackbar.show();


        user = (User) getIntent().getSerializableExtra("user");
        project = (Project) getIntent().getSerializableExtra("project");

        user_id = user.getId();
        token = user.getToken();
        project_id = project.getId();

        toolbarTitle = (TextView) findViewById(R.id.tvToolbar);
        toolbarTitle.setText(project.getProject_name());

        getReimburseListFromProjectByUserID(project_id, user_id, token);

        adapter = new ReimburseAdapter(ProjectDetailActivity.this, list);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.categoryAll));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.categoryTransportation));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.categoryConsumption));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.categoryAccommodation));

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), ProjectDetailActivity.this);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager, true);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
//                if(list != null)
//                    changeCategory(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        categoryFragment = (CategoryFragment) viewPagerAdapter.getRegisteredFragment(0);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
    }

    public User getUser() {
        return user;
    }

    public Project getProject() {
        return project;
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public void changeCategory(int category){
        ArrayList<Reimburse> templist = new ArrayList<>();
        TextView etv = (TextView) findViewById(R.id.emptyView);
        if (category == 0) {
            for(int i=0; i<list.size(); i++){
                templist.add(list.get(i));
            }
            etv.setText(R.string.emptyReimburseRequest);
            adapter = new ReimburseAdapter(ProjectDetailActivity.this, templist);
            reimburseListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else if (category == 1){
            for(int i=0; i<list.size(); i++){
                if(getResources().getString(list.get(i).getCategoryText()).equalsIgnoreCase(getResources().getString(R.string.categoryTransportation))){
                    templist.add(list.get(i));
                }
            }
            etv.setText(R.string.emptyReimburseRequestCategory);
            adapter = new ReimburseAdapter(ProjectDetailActivity.this, templist);
            reimburseListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else if (category == 2){
            for(int i=0; i<list.size(); i++){
                if(getResources().getString(list.get(i).getCategoryText()).equalsIgnoreCase(getResources().getString(R.string.categoryConsumption))){
                    templist.add(list.get(i));
                }
            }
            etv.setText(R.string.emptyReimburseRequestCategory);
            adapter = new ReimburseAdapter(ProjectDetailActivity.this, templist);
            reimburseListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else if (category == 3){
            for(int i=0; i<list.size(); i++){
                if(getResources().getString(list.get(i).getCategoryText()).equalsIgnoreCase(getResources().getString(R.string.categoryAccommodation))){
                    templist.add(list.get(i));
                }
            }
            etv.setText(R.string.emptyReimburseRequestCategory);
            adapter = new ReimburseAdapter(ProjectDetailActivity.this, templist);
            reimburseListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        getReimburseListFromProjectByUserID(project_id, user_id, token);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent(ProjectDetailActivity.this, AddReimburseActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("project", project);
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
                animateFAB();
                intent.putExtra("category", "Transportasi");
                startActivity(intent);

                Log.d("Raj", "Fab 1");
                break;
            case R.id.fab2:
                animateFAB();
                intent.putExtra("category", "Konsumsi");
                startActivity(intent);

                Log.d("Raj", "Fab 2");
                break;
            case R.id.fab3:
                animateFAB();
                intent.putExtra("category", "Akomodasi");
                startActivity(intent);
                Log.d("Raj", "Fab 3");
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void animateFAB() {

        if (isFabOpen) {

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;
            Log.d("Raj", "open");

        }
    }

    public void getReimburseListFromProjectByUserID(String pid, String uid, String token){

        Call<ReimburseList> call = apiService.getReimburseFromProjectByUserId(pid, uid, token);
        call.enqueue(new Callback<ReimburseList>() {
            @Override
            public void onResponse(Call<ReimburseList>call, Response<ReimburseList> response) {
                if (response.body().getMessage() == null) {
                    reimburses = response.body().getReimburseList();
                    Log.e("[RESPONSE]", "Number of reimburses received: " + reimburses.size());
                    list = reimburses;
                    totalcost = 0;
                    for(int i=0; i<reimburses.size(); i++){
                        totalcost += reimburses.get(i).getCost();
                    }
                    adapter = new ReimburseAdapter(ProjectDetailActivity.this, list);
                    CategoryFragment categoryFragment = (CategoryFragment) viewPagerAdapter.getRegisteredFragment(0);
                    categoryFragment.updateList(reimburses);
//                    reimburseListView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
                    String str = String.format("%,d", totalcost).replace(",", ".");
                    TextView total_cost = (TextView) findViewById(R.id.tvProjectCost);
                    total_cost.setText("Rp. "+str);
//                    swipeRefreshLayout.setRefreshing(false);
                    if(snackbar.isShown())
                        snackbar.dismiss();
                    TabLayout.Tab tl = tabLayout.getTabAt(0);
                    if (tl != null) {
                        tl.select();
                    }
                    fab.show();
                } else {
//                    swipeRefreshLayout.setRefreshing(false);
                    if(snackbar.isShown())
                        snackbar.dismiss();
                    TabLayout.Tab tl = tabLayout.getTabAt(0);
                    if (tl != null) {
                        tl.select();
                    }
                    Toast.makeText(ProjectDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    ProjectDetailActivity.this.finish();
                    SharedPreferences userdata = getSharedPreferences("USER_DATA", 0);
                    SharedPreferences.Editor editor = userdata.edit();
                    editor.remove("USER_DATA");
                    editor.apply();
                    Intent intent = new Intent(ProjectDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ReimburseList> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                snackbar.setText(getResources().getString(R.string.error_msg_fetch_reimburse));
                Log.e("[ERROR]", t.toString());
            }
        });
    }

    public void fabVisibility(boolean status){
        if (status){
            fab.show();
            if(isFabOpen){
                fab1.show();
                fab2.show();
                fab3.show();
            }
        }
        else {
            fab.hide();
            if(isFabOpen){
                fab1.hide();
                fab2.hide();
                fab3.hide();
            }
        }
    }
}