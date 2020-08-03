package com.azhariharisalhamdi.consumerapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azhariharisalhamdi.consumerapp.adapter.ListUsersAdapter;
import com.azhariharisalhamdi.consumerapp.models.Item;
import com.azhariharisalhamdi.consumerapp.models.User;
import com.azhariharisalhamdi.consumerapp.models.Users;
import com.azhariharisalhamdi.consumerapp.rest.BaseApiClient;
import com.azhariharisalhamdi.consumerapp.rest.UsersApi;
import com.azhariharisalhamdi.consumerapp.settings.SettingsActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    String TAG = "Search";
    ArrayList<User> temp_userlist, userList;
    UsersApi userApi;

    private RecyclerView recyclerViewUser;
    private TextInputLayout usernameInput;
    private TextInputEditText textInput;
    private ProgressBar progressBar;
    private String finalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle(R.string.search_activity_label);

        progressBar = findViewById(R.id.progressBar);
        usernameInput = findViewById(R.id.username_input);
        textInput = findViewById(R.id.textInput);
        recyclerViewUser = findViewById(R.id.recyclerViewUser);
        recyclerViewUser.setHasFixedSize(true);
        progressBar.setVisibility(View.INVISIBLE);

        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    progressBar.setVisibility(View.VISIBLE);
                    getUser_Async(usernameInput.getEditText().getText().toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0){
                    progressBar.setVisibility(View.VISIBLE);
                    finalText = usernameInput.getEditText().getText().toString();
                    if(finalText.length() != 0)
                        getUser_Async(usernameInput.getEditText().getText().toString());
                    else
                        showRecyclerList(userList);
                }
            }
        });

        textInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                progressBar.setVisibility(View.VISIBLE);
                finalText = usernameInput.getEditText().getText().toString();
                if(finalText.length() != 0)
                    getUser_Async(usernameInput.getEditText().getText().toString());
                else
                    showRecyclerList(userList);
                return true;
            }
            return false;
        });

    }

    public void getUser_Async(String username){
        UsersApi userApi = BaseApiClient.getClient().create(UsersApi.class);
        Call call = userApi.UserGithub(username);
        Log.d(TAG, "username : "+username);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "successful");
                    temp_userlist = new ArrayList<>();
                    Users mRespons = (Users) response.body();
                    Log.d(TAG, "total_count : "+mRespons.getTotal_count());
                    ArrayList<Item> items = mRespons.getItems();
                    for(int i = 0; i<items.size(); i++){
                        User temp_user = new User();
                        temp_user.setUsername(items.get(i).getUsername());
                        temp_user.setAvatar(items.get(i).getAvatar_url());
                        temp_userlist.add(temp_user);
                    }
                    userList = new ArrayList<>();
                    userList.addAll(temp_userlist);
                    showRecyclerList(userList);
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
    }

    private void showRecyclerList(ArrayList<User> userlist){
        progressBar.setVisibility(View.INVISIBLE);
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(this));
        if(finalText.length() == 0)
            userlist.clear();
        ListUsersAdapter listUsersAdapter = new ListUsersAdapter(userlist);
        recyclerViewUser.setAdapter(listUsersAdapter);

        listUsersAdapter.setOnItemClickCallback(new ListUsersAdapter.OnItemClickCallback(){
            @Override
            public void onItemClicked(User data) {
                changeActivity(data);
            }
        });
    }

    private void showSelectedUser(User user) {
        Toast.makeText(this, "Kamu memilih " + user.getUsername(), Toast.LENGTH_SHORT).show();
    }

    public void changeActivity(User user){
        Intent moveWithObjectIntent = new Intent(SearchActivity.this, DetailSelectedActivity.class);
        moveWithObjectIntent.putExtra(DetailSelectedActivity.USER_DATA_DETAIL, user);
        startActivity(moveWithObjectIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
//                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                Intent mIntent = new Intent(SearchActivity.this, SettingsActivity.class);
                startActivity(mIntent);
                return true;
            case R.id.favorite:
                Intent favoriteIntent = new Intent(SearchActivity.this, FavoriteUserActivity.class);
                startActivity(favoriteIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}