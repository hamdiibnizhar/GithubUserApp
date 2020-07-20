package com.azhariharisalhamdi.githubuserapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.azhariharisalhamdi.githubuserapp.adapter.ListUsersAdapter;
import com.azhariharisalhamdi.githubuserapp.models.Item;
import com.azhariharisalhamdi.githubuserapp.models.Users;
import com.azhariharisalhamdi.githubuserapp.rest.BaseApiClient;
import com.azhariharisalhamdi.githubuserapp.rest.UsersApi;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    String TAG = "Search";
    ArrayList<User> temp_userlist = new ArrayList<>();
    ArrayList<User> userList = new ArrayList<>();
    UsersApi userApi;

    private RecyclerView recyclerViewUser;
    private TextInputLayout usernameInput;
    private TextInputEditText textInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        usernameInput = findViewById(R.id.username_input);
        textInput = findViewById(R.id.textInput);
        recyclerViewUser = findViewById(R.id.recyclerViewUser);
        recyclerViewUser.setHasFixedSize(true);

        textInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                ArrayList<User> test = getUser_Async(textInput.getText().toString().trim());
                userList.addAll(test);
                showRecyclerList(userList);
                return true;
            }
            return false;
        });
        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    userList.addAll(getUser_Async(usernameInput.getEditText().getText().toString()));
                    showRecyclerList(userList);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0){
                    userList.addAll(getUser_Async(usernameInput.getEditText().getText().toString()));
                    showRecyclerList(userList);
                }
            }
        });
        showRecyclerList(userList);
    }

    public ArrayList<User> getUser_Async(String username){
        userApi = BaseApiClient.getClient().create(UsersApi.class);
        Call call = userApi.UserGithub(username);
        Log.d(TAG, "username : "+username);
        ArrayList<User> mtemp_userlist = new ArrayList<User>();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "successful");
                    temp_userlist.clear();
                    Users mRespons = (Users) response.body();
                    Log.d(TAG, "total_count : "+mRespons.getTotal_count());
                    ArrayList<Item> items = mRespons.getItems();
                    for(int i = 0; i<items.size(); i++){
                        User temp_user = new User();
                        temp_user.setUsername(items.get(i).getUsername());
                        temp_user.setAvatar(items.get(i).getAvatar_url());
                        temp_userlist.add(temp_user);
                    }
                }else{
                    Log.d(TAG, "not successful");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(TAG, "fails " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
        mtemp_userlist = temp_userlist;
        return mtemp_userlist;
    }

    private void showRecyclerList(ArrayList<User> userlist){
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(this));
        ListUsersAdapter listUsersAdapter = new ListUsersAdapter(userlist);
        recyclerViewUser.setAdapter(listUsersAdapter);
    }
}