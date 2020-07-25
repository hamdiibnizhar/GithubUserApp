package com.azhariharisalhamdi.githubuserapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.azhariharisalhamdi.githubuserapp.DetailSelectedActivity;
import com.azhariharisalhamdi.githubuserapp.R;
import com.azhariharisalhamdi.githubuserapp.User;
import com.azhariharisalhamdi.githubuserapp.adapter.FollowersUserAdapter;
import com.azhariharisalhamdi.githubuserapp.adapter.ListUsersAdapter;
import com.azhariharisalhamdi.githubuserapp.models.DetailUsers;
import com.azhariharisalhamdi.githubuserapp.models.FollowerUsers;
import com.azhariharisalhamdi.githubuserapp.rest.BaseApiClient;
import com.azhariharisalhamdi.githubuserapp.rest.DetailUserApi;
import com.azhariharisalhamdi.githubuserapp.rest.FollowerUserApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowerFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String TAG = "FollowerFragment";

    private static final String ARG_SECTION_NUMBER = "section_number";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    User temp_user;
    ArrayList<User> temp_userlist, userList;

    private RecyclerView recyclerViewUser;

    public FollowerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowerFragment newInstance(String param1, String param2) {
        FollowerFragment fragment = new FollowerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static FollowerFragment newInstance(int index) {
        FollowerFragment fragment = new FollowerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getDetailUser_Async(((DetailSelectedActivity) getActivity()).getUserName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView =  inflater.inflate(R.layout.fragment_follower, container, false);

        recyclerViewUser = RootView.findViewById(R.id.followerRecyclerViewUser);
        recyclerViewUser.setHasFixedSize(true);
        getDetailUser_Async(((DetailSelectedActivity) getActivity()).getUserName());
        return RootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        final TextView textView = view.findViewById(R.id.section_label);
//        int index = 1;
//        if (getArguments() != null) {
//            index = getArguments().getInt(ARG_SECTION_NUMBER);
//        }
//        textView.setText(getString(R.string.content_tab_text) + " " + index);
    }

    public void getDetailUser_Async(String username){
        FollowerUserApi followerApi = BaseApiClient.getClient().create(FollowerUserApi.class);
        Call<ArrayList<FollowerUsers>> call = followerApi.UserGithub(username);
        Log.d(TAG, "username : "+username);
        User mtemp_user = new User();
        Log.d(TAG, "call : "+call.request().toString());
        call.enqueue(new Callback<ArrayList<FollowerUsers>>() {
            @Override
            public void onResponse(Call<ArrayList<FollowerUsers>> call, Response<ArrayList<FollowerUsers>> response) {
                if(response.isSuccessful()){
                    temp_user = new User();
                    temp_userlist = new ArrayList<>();
                    Log.d(TAG, "successful");
                    ArrayList<FollowerUsers> mRespons = response.body();
                    Log.d(TAG, "size : "+mRespons.size());

                    for(int i = 0; i<mRespons.size(); i++){
                        User newUser = new User();
                        newUser.setUsername(mRespons.get(i).getUsername());
                        newUser.setAvatar(mRespons.get(i).getAvatar_url());
                        temp_userlist.add(newUser);
                    }
                    userList = new ArrayList<>();
                    userList.addAll(temp_userlist);
                    showRecyclerList(userList);
                }else{
                    Log.d(TAG, "not successful");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FollowerUsers>> call, Throwable t) {
                Log.d(TAG, "fails " + t.getMessage());
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showRecyclerList(ArrayList<User> userlist){
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        FollowersUserAdapter listUsersAdapter = new FollowersUserAdapter(userlist);
        recyclerViewUser.setAdapter(listUsersAdapter);
    }
}