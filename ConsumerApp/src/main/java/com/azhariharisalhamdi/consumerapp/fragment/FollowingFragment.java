package com.azhariharisalhamdi.consumerapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azhariharisalhamdi.consumerapp.DetailSelectedActivity;
import com.azhariharisalhamdi.consumerapp.R;
import com.azhariharisalhamdi.consumerapp.adapter.FollowingUserAdapter;
import com.azhariharisalhamdi.consumerapp.models.FollowingUsers;
import com.azhariharisalhamdi.consumerapp.models.User;
import com.azhariharisalhamdi.consumerapp.rest.BaseApiClient;
import com.azhariharisalhamdi.consumerapp.rest.FollowingUserApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String TAG = "FollowingFragment";

    private String mParam1;
    private String mParam2;

    User temp_user;
    ArrayList<User> temp_userlist, userList;

    private RecyclerView recyclerViewUser;

    public FollowingFragment() {
    }

    public static FollowingFragment newInstance(String param1, String param2) {
        FollowingFragment fragment = new FollowingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        View RootView =  inflater.inflate(R.layout.fragment_follower, container, false);

        recyclerViewUser = RootView.findViewById(R.id.followerRecyclerViewUser);
        recyclerViewUser.setHasFixedSize(true);
        getDetailUser_Async(((DetailSelectedActivity) getActivity()).getUserName());

        return RootView;
    }

    public void getDetailUser_Async(String username){
        FollowingUserApi followerApi = BaseApiClient.getClient().create(FollowingUserApi.class);
        Call<ArrayList<FollowingUsers>> call = followerApi.UserGithub(username);
        Log.d(TAG, "username : "+username);
        User mtemp_user = new User();
        Log.d(TAG, "call : "+call.request().toString());
        call.enqueue(new Callback<ArrayList<FollowingUsers>>() {
            @Override
            public void onResponse(Call<ArrayList<FollowingUsers>> call, Response<ArrayList<FollowingUsers>> response) {
                if(response.isSuccessful()){
                    temp_user = new User();
                    temp_userlist = new ArrayList<>();
                    Log.d(TAG, "successful");
                    ArrayList<FollowingUsers> mRespons = response.body();
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
            public void onFailure(Call<ArrayList<FollowingUsers>> call, Throwable t) {
                Log.d(TAG, "fails " + t.getMessage());
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showRecyclerList(ArrayList<User> userlist){
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        FollowingUserAdapter listUsersAdapter = new FollowingUserAdapter(userlist);
        recyclerViewUser.setAdapter(listUsersAdapter);
    }
}