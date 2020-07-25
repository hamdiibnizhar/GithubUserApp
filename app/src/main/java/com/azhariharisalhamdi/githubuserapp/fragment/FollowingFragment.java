package com.azhariharisalhamdi.githubuserapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.azhariharisalhamdi.githubuserapp.DetailSelectedActivity;
import com.azhariharisalhamdi.githubuserapp.R;
import com.azhariharisalhamdi.githubuserapp.User;
import com.azhariharisalhamdi.githubuserapp.adapter.FollowingUserAdapter;
import com.azhariharisalhamdi.githubuserapp.models.FollowingUsers;
import com.azhariharisalhamdi.githubuserapp.rest.BaseApiClient;
import com.azhariharisalhamdi.githubuserapp.rest.FollowingUserApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String TAG = "FollowingFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    User temp_user;
    ArrayList<User> temp_userlist, userList;

    private RecyclerView recyclerViewUser;

    public FollowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowingFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
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