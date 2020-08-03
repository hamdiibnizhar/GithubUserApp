package com.azhariharisalhamdi.consumerapp;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.azhariharisalhamdi.consumerapp.adapter.SectionsPagerAdapter;
import com.azhariharisalhamdi.consumerapp.database.UserHelper;
import com.azhariharisalhamdi.consumerapp.models.DetailUsers;
import com.azhariharisalhamdi.consumerapp.models.User;
import com.azhariharisalhamdi.consumerapp.rest.BaseApiClient;
import com.azhariharisalhamdi.consumerapp.rest.DetailUserApi;
import com.azhariharisalhamdi.consumerapp.settings.SettingsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.azhariharisalhamdi.consumerapp.database.DatabaseContract.UserColumns.AVATAR_URL;
import static com.azhariharisalhamdi.consumerapp.database.DatabaseContract.UserColumns.FOLLOWER;
import static com.azhariharisalhamdi.consumerapp.database.DatabaseContract.UserColumns.FOLLOWING;
import static com.azhariharisalhamdi.consumerapp.database.DatabaseContract.UserColumns.USERNAME;
import static com.azhariharisalhamdi.consumerapp.database.DatabaseHelper.DATABASE_NAME;

public class DetailSelectedActivity extends AppCompatActivity {
    public static final String USER_DATA_DETAIL = "user_data_detail";

    String TAG = "detail_user";
    DetailUserApi detailApi;
    ImageView mImageView;
    public User temp_user;
    TextView nameTv, blogTv, RepoGistTv, companyTv, locationTv, followerTv, followingTv;
    User GithubUser = new User();
    private User received;
    private ProgressBar progressBar;
    private FloatingActionButton favButton;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private UserHelper userHelper;
    private User user_data;
    private int position;
    private boolean has_favorited = false;

    private boolean isFavorited = false;

    public static final String EXTRA_USER = "extra_user";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;
    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_selected);
        //set activity titile
        setTitle(R.string.detail_activity_label);

        mImageView = (ImageView) findViewById(R.id.avatar);
        nameTv = (TextView) findViewById(R.id.name);
        blogTv = (TextView) findViewById(R.id.blog);
        RepoGistTv = (TextView) findViewById(R.id.repository);
        companyTv = (TextView) findViewById(R.id.company);
        locationTv = (TextView) findViewById(R.id.location);
        followerTv = (TextView) findViewById(R.id.follower);
        followingTv = (TextView) findViewById(R.id.following);
        progressBar = findViewById(R.id.progressBar);
        favButton = findViewById(R.id.floatingActionButton);

        userHelper = UserHelper.getInstance(getApplicationContext());
        userHelper.open();
//        user_data = getIntent().getParcelableExtra(EXTRA_USER);
//
//        if (user_data != null) {
//            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
//            isFavorited = true;
//        } else {
//            user_data = new User();
//        }

        //get parcel data intent
        received = getIntent().getParcelableExtra(USER_DATA_DETAIL);
        if(received != null) progressBar.setVisibility(View.VISIBLE);
        assert received != null;
        Log.d(TAG, "oncreate, Username : "+received.getUsername());

        if(userHelper.doesDatabaseExist(this, DATABASE_NAME)) {
            Log.d(TAG, "database exist");
            isFavorited = userHelper.isUserFavorited(received.getUsername());
            if (isFavorited) {
                favButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.outline_favorite_24));
                Log.d(TAG, "user's favorited");
            } else {
                favButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.outline_favorite_border_24));
                Log.d(TAG, "user's not favorited");
            }
        }else{
            Log.d(TAG, "database not exist");
            favButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.outline_favorite_border_24));
            isFavorited = false;
        }

        userHelper.close();

        getDetailUser_Async(received.getUsername());

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        getSupportActionBar().setElevation(0);

        favButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isFavorited){
                    Log.d(TAG, "user's not to be favorited");
                    favButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.outline_favorite_border_24));
                    isFavorited = false;
                    userHelper.open();
                    deteleUserData(temp_user);
                    userHelper.close();
                }else{
                    Log.d(TAG, "user's to be favorited");
                    favButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.outline_favorite_24));
                    isFavorited = true;
                    userHelper.open();
                    storeUserData(temp_user);
                    userHelper.close();
                }
            }
        });
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        userHelper.close();
//    }
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//        userHelper.close();
//    }
//
//    @Override
//    protected void onPause(){
//        super.onPause();
//        userHelper.close();
//    }

    private void storeUserData(User githubUser){
        ContentValues values = new ContentValues();
        values.put(USERNAME, githubUser.getUsername());
        values.put(AVATAR_URL, githubUser.getAvatar());
        values.put(FOLLOWER, githubUser.getFollowers());
        values.put(FOLLOWING, githubUser.getFollowing());
        long result = userHelper.insert(values);
        if (result > 0) {
//            Toast.makeText(DetailSelectedActivity.this, R.string.success_store_user, Toast.LENGTH_SHORT).show();
            showSnackbarMessage(getResources().getString(R.string.success_store_user));
        } else {
//            Toast.makeText(DetailSelectedActivity.this, R.string.fail_change_db, Toast.LENGTH_SHORT).show();
            showSnackbarMessage(getResources().getString(R.string.fail_change_db));
        }
    }

    private void deteleUserData(User githubUser){
        long result = userHelper.deleteByUsername(githubUser.getUsername());
        if (result > 0) {
//            Toast.makeText(DetailSelectedActivity.this, R.string.success_delete_user, Toast.LENGTH_SHORT).show();
            showSnackbarMessage(getResources().getString(R.string.success_delete_user));
        } else {
//            Toast.makeText(DetailSelectedActivity.this, R.string.fail_change_db, Toast.LENGTH_SHORT).show();
            showSnackbarMessage(getResources().getString(R.string.fail_change_db));
        }
    }

    public String getUserName(){
        return received.getUsername();
    }

    public void setUI(User GithubUser)
    {
        Log.d(TAG, "oncreate, Username : "+GithubUser.getUsername());

        if(GithubUser.getName() != "-") nameTv.setText(GithubUser.getUsername()+"\n("+GithubUser.getName()+")");
        else nameTv.setText(GithubUser.getUsername());

        blogTv.setText(GithubUser.getBlog());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(this).load(GithubUser.getAvatar()).apply(options).into(mImageView);
        RepoGistTv.setText(Integer.toString(GithubUser.getPublic_repo())+"/"+Integer.toString(GithubUser.getPublic_gists()));
        companyTv.setText(GithubUser.getCompany());
        locationTv.setText(GithubUser.getLocation());
        followerTv.setText(Integer.toString(GithubUser.getFollowers()));
        followingTv.setText(Integer.toString(GithubUser.getFollowing()));
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(viewPager, message, Snackbar.LENGTH_SHORT).show();
    }

    public void getDetailUser_Async(String username){
        detailApi = BaseApiClient.getClient().create(DetailUserApi.class);
        Call<DetailUsers> call = detailApi.UserDetailedGithub(username);
        Log.d(TAG, "username : "+username);
        User mtemp_user = new User();
        Log.d(TAG, "call : "+call.request().toString());
        call.enqueue(new Callback<DetailUsers>() {
            @Override
            public void onResponse(Call<DetailUsers> call, Response<DetailUsers> response) {
                if(response.isSuccessful()){
                    temp_user = new User();
                    Log.d(TAG, "successful");
                    DetailUsers mRespons = response.body();
                    Log.d(TAG, "total_count : "+mRespons.getUsername());
                    temp_user.setUsername(mRespons.getUsername());
                    temp_user.setAvatar(mRespons.getAvatar_url());

                    if(mRespons.getName() != null) temp_user.setName(mRespons.getName());
                    else temp_user.setName("-");

                    if(mRespons.getCompany() != null) temp_user.setCompany(mRespons.getCompany());
                    else temp_user.setCompany("-");

                    if(mRespons.getBlog() != null) temp_user.setBlog(mRespons.getBlog());
                    else temp_user.setBlog("-");

                    if(mRespons.getLocation() != null) temp_user.setLocation(mRespons.getLocation());
                    else temp_user.setLocation("-");

                    temp_user.setPublic_gists(mRespons.getPublic_gists());
                    temp_user.setPublic_repo(mRespons.getPublic_repos());
                    temp_user.setFollowers(mRespons.getFollowers());
                    temp_user.setFollowing(mRespons.getFollowing());

                    progressBar.setVisibility(View.INVISIBLE);
                    setUI(temp_user);
                }else{
                    Log.d(TAG, "not successful");
                }
            }

            @Override
            public void onFailure(Call<DetailUsers> call, Throwable t) {
                Log.d(TAG, "fails " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (data != null) {
//            // Akan dipanggil jika request codenya ADD
//            if (requestCode == FavoriteUserActivity.REQUEST_ADD) {
//                if (resultCode == FavoriteUserActivity.RESULT_ADD) {
//                    User user = data.getParcelableExtra(FavoriteUserActivity.EXTRA_USER);
//
//                    adapter.addItem(user);
//                    rvNotes.smoothScrollToPosition(adapter.getItemCount() - 1);
//
//                    showSnackbarMessage("Satu item berhasil ditambahkan");
//                }
//            }
//            // Update dan Delete memiliki request code sama akan tetapi result codenya berbeda
//            else if (requestCode == FavoriteUserActivity.REQUEST_UPDATE) {
//                if (resultCode == FavoriteUserActivity.RESULT_UPDATE) {
//
//                    User user = data.getParcelableExtra(FavoriteUserActivity.EXTRA_NOTE);
//                    int position = data.getIntExtra(FavoriteUserActivity.EXTRA_POSITION, 0);
//
//                    adapter.updateItem(position, user);
//                    rvNotes.smoothScrollToPosition(position);
//
//                    showSnackbarMessage("Satu item berhasil diubah");
//                }
//                else if (resultCode == FavoriteUserActivity.RESULT_DELETE) {
//                    int position = data.getIntExtra(FavoriteUserActivity.EXTRA_POSITION, 0);
//
//                    adapter.removeItem(position);
//
//                    showSnackbarMessage("Satu item berhasil dihapus");
//                }
//            }
//        }
//    }

    public User getDetailUser_Sync(String username){
        detailApi = BaseApiClient.getClient().create(DetailUserApi.class);
        Call<DetailUsers> call = detailApi.UserDetailedGithub(username);
        Log.d(TAG, "username : "+username);
        User mtemp_user = new User();
        Log.d(TAG, "call : "+call.request().toString());
        DetailUsers mRespons = null;
        try {
            mRespons = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(call.isExecuted()){
            temp_user = new User();
            Log.d(TAG, "successful");
            Log.d(TAG, "total_count : "+mRespons.getUsername());
            temp_user.setUsername(mRespons.getUsername());
            temp_user.setAvatar(mRespons.getAvatar_url());

            if(mRespons.getName() != null) temp_user.setName(mRespons.getName());
            else temp_user.setName("-");

            if(mRespons.getCompany() != null) temp_user.setCompany(mRespons.getCompany());
            else temp_user.setCompany("-");

            if(mRespons.getBlog() != null) temp_user.setBlog(mRespons.getBlog());
            else temp_user.setBlog("-");

            if(mRespons.getLocation() != null) temp_user.setLocation(mRespons.getLocation());
            else temp_user.setLocation("-");

            temp_user.setPublic_gists(mRespons.getPublic_gists());
            temp_user.setPublic_repo(mRespons.getPublic_repos());
            temp_user.setFollowers(mRespons.getFollowers());
            temp_user.setFollowing(mRespons.getFollowing());
        }
        mtemp_user = temp_user;
        return temp_user;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.other_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
//            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            Intent mIntent = new Intent(DetailSelectedActivity.this, SettingsActivity.class);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }


}
