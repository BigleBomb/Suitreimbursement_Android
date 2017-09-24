package com.example.ocaaa.reimburseproject.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ocaaa.reimburseproject.APIController.APIClient;
import com.example.ocaaa.reimburseproject.APIController.APIInterface;
import com.example.ocaaa.reimburseproject.Activity.LoginActivity;
import com.example.ocaaa.reimburseproject.Activity.ProjectDetailActivity;
import com.example.ocaaa.reimburseproject.Activity.ReimburseDetailActivity;
import com.example.ocaaa.reimburseproject.Activity.ScrollingActivity;
import com.example.ocaaa.reimburseproject.Adapter.ReimburseAdapter;
import com.example.ocaaa.reimburseproject.Model.Reimburse;
import com.example.ocaaa.reimburseproject.Model.ReimburseList;
import com.example.ocaaa.reimburseproject.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LIST = "list";
//    private static final String ARG_PARAM2 = "param2";

    private ListView listView;
    private ArrayList<Reimburse> reimburseList = new ArrayList<>();
    private ReimburseAdapter adapter;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;

    private APIInterface apiService;

    // TODO: Rename and change types of parameters
    private ArrayList<Reimburse> mParam1;
    private String mParam2;

    private FloatingActionButton fab;

    private ProjectDetailActivity currActivity = ((ProjectDetailActivity)getActivity());

    private OnFragmentInteractionListener mListener;

    public CategoryFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment CategoryFragment.
//     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(ArrayList<Reimburse> reimburse) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(LIST, reimburse);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reimburseList = getArguments().getParcelableArrayList(LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_category, container, false);

        apiService =
                APIClient.getClient().create(APIInterface.class);

        listView = (ListView) view.findViewById(R.id.lvReimburseDetail);
        listView.setEmptyView(view.findViewById(R.id.emptyView));

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState==0)
                    ((ProjectDetailActivity)getActivity()).fabVisibility(true);
                else
                    ((ProjectDetailActivity)getActivity()).fabVisibility(false);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ScrollingActivity.class);
//                Intent intent = new Intent(getActivity(), ReimburseDetailActivity.class);
                intent.putExtra("reimburse_id", reimburseList.get(position).getId());
                intent.putExtra("user", ((ProjectDetailActivity)getActivity()).getUser());
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srlReimburse);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                Toast.makeText(getActivity(), "Refreshing...", Toast.LENGTH_SHORT).show();
//                if(swipeRefreshLayout.isRefreshing())
//                        swipeRefreshLayout.setRefreshing(false);
                getReimburseListFromProjectByUserID(
                        ((ProjectDetailActivity)getActivity()).getProject().getId(),
                        ((ProjectDetailActivity)getActivity()).getUser().getId(),
                        ((ProjectDetailActivity)getActivity()).getUser().getToken()
                );
            }
        });

        adapter = new ReimburseAdapter(view.getContext(), reimburseList);
        listView.setAdapter(adapter);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        return view;
    }

    public void updateList(ArrayList<Reimburse> reimburse){
        reimburseList = reimburse;
        adapter = new ReimburseAdapter(getContext(), reimburseList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void getReimburseListFromProjectByUserID(String pid, String uid, String token){

        Call<ReimburseList> call = apiService.getReimburseFromProjectByUserId(pid, uid, token);
        call.enqueue(new Callback<ReimburseList>() {
            @Override
            public void onResponse(Call<ReimburseList>call, Response<ReimburseList> response) {
                if (response.body().getMessage() == null) {
                    reimburseList = response.body().getReimburseList();
                    Log.e("[RESPONSE]", "Number of reimburses received: " + reimburseList.size());
                    int totalcost = 0;
                    for(int i=0; i<reimburseList.size(); i++){
                        totalcost += reimburseList.get(i).getCost();
                    }
                    adapter = new ReimburseAdapter(getActivity(), reimburseList);
                    String str = String.format("%,d", totalcost).replace(",", ".");
                    TextView total_cost = (TextView) getActivity().findViewById(R.id.tvProjectCost);
                    total_cost.setText("Rp. "+str);
                    updateList(reimburseList);
                    swipeRefreshLayout.setRefreshing(false);
//                    if(snackbar.isShown())
//                        snackbar.dismiss();
//                    TabLayout.Tab tl = tabLayout.getTabAt(0);
//                    if (tl != null) {
//                        tl.select();
//                    }
//                    fab.show();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
//                    if(snackbar.isShown())
//                        snackbar.dismiss();
//                    TabLayout.Tab tl = tabLayout.getTabAt(0);
//                    if (tl != null) {
//                        tl.select();
//                    }
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    SharedPreferences userdata = getActivity().getSharedPreferences("USER_DATA", 0);
                    SharedPreferences.Editor editor = userdata.edit();
                    editor.remove("USER_DATA");
                    editor.apply();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ReimburseList> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), R.string.error_msg_fetch_reimburse, Toast.LENGTH_SHORT).show();
//                snackbar.setText(getResources().getString(R.string.error_msg_fetch_reimburse));
                Log.e("[ERROR]", t.toString());
            }
        });
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
