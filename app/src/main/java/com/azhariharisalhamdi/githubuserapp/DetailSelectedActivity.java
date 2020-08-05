package com.azhariharisalhamdi.githubuserapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import com.azhariharisalhamdi.githubuserapp.adapter.SectionsPagerAdapter;
import com.azhariharisalhamdi.githubuserapp.database.DatabaseContract;
import com.azhariharisalhamdi.githubuserapp.database.UserHelper;
import com.azhariharisalhamdi.githubuserapp.models.DetailUsers;
import com.azhariharisalhamdi.githubuserapp.models.User;
import com.azhariharisalhamdi.githubuserapp.rest.BaseApiClient;
import com.azhariharisalhamdi.githubuserapp.rest.DetailUserApi;
import com.azhariharisalhamdi.githubuserapp.settings.SettingsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.azhariharisalhamdi.githubuserapp.database.DatabaseContract.UserColumns.AVATAR_URL;
import static com.azhariharisalhamdi.githubuserapp.database.DatabaseContract.UserColumns.CONTENT_URI;
import static com.azhariharisalhamdi.githubuserapp.database.DatabaseContract.UserColumns.FOLLOWER;
import static com.azhariharisalhamdi.githubuserapp.database.DatabaseContract.UserColumns.FOLLOWING;
import static com.azhariharisalhamdi.githubuserapp.database.DatabaseContract.UserColumns.TABLE_NAME;
import static com.azhariharisalhamdi.githubuserapp.database.DatabaseContract.UserColumns.USERNAME;

public class DetailSelectedActivity extends AppCompatActivity {
    public static final String USER_DATA_DETAIL_SEARCH = "user_data_detail_search";
    public static final String USER_DATA_DETAIL_FAVORITE = "user_data_detail_favorite";
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
    private User userData;
    private int position;
    private boolean has_favorited = false;
    private boolean from_favorite = false;
    private Uri uriPath;

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

        //get parcel data intent
        if(getIntent().getParcelableExtra(USER_DATA_DETAIL_SEARCH) != null){
            received = getIntent().getParcelableExtra(USER_DATA_DETAIL_SEARCH);
            from_favorite = false;
        }else{
            received = getIntent().getParcelableExtra(USER_DATA_DETAIL_FAVORITE);
            from_favorite = true;
        }

        if(received != null) {
            progressBar.setVisibility(View.VISIBLE);
            favButton.setVisibility(View.INVISIBLE);
        }
        Log.d(TAG, "oncreate, Username : "+received.getUsername());
        String lastpath = ""+received.getId();
        if (CONTENT_URI != null) {
            String[] projections = {
                    DatabaseContract.UserColumns._ID,
                    DatabaseContract.UserColumns.USERNAME,
                    DatabaseContract.UserColumns.AVATAR_URL,
                    DatabaseContract.UserColumns.FOLLOWER,
                    DatabaseContract.UserColumns.FOLLOWING
            };
            String username_string = "SELECT * FROM " + TABLE_NAME + " WHERE " + USERNAME + " =?";
//            Cursor cursor = getContentResolver().query(uriPath, projections, username_string, new String[]{lastpath}, null);
            isFavorited = isUserFavorited(received.getUsername());
            if (isFavorited) {
                isFavorited = true;
                favButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.outline_favorite_24));
                Log.d(TAG, "user's favorited");
            }else{
                isFavorited = false;
                favButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.outline_favorite_border_24));
                Log.d(TAG, "user's not favorited");
            }
        }
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
                    deteleUserData(uriPath, temp_user);
                }else{
                    Log.d(TAG, "user's to be favorited");
                    favButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.outline_favorite_24));
                    isFavorited = true;
                    storeUserData(temp_user);
                }
            }
        });
    }

    public boolean isUserFavorited(String value) {
        Uri uri = Uri.parse(CONTENT_URI + "/__check_status");
        String selectString = USERNAME + " =?";
        String[] projections = {
                DatabaseContract.UserColumns._ID,
                DatabaseContract.UserColumns.USERNAME,
                DatabaseContract.UserColumns.AVATAR_URL,
                DatabaseContract.UserColumns.FOLLOWER,
                DatabaseContract.UserColumns.FOLLOWING
        };
        Cursor cursor = getContentResolver().query(uri, projections, selectString, new String[] {value}, null);
        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;
            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
        }
        cursor.close();
        return hasObject;
    }

    private void storeUserData(User githubUser){
        ContentValues values = new ContentValues();
        values.put(USERNAME, githubUser.getUsername());
        values.put(AVATAR_URL, githubUser.getAvatar());
        values.put(FOLLOWER, githubUser.getFollowers());
        values.put(FOLLOWING, githubUser.getFollowing());
        Uri result = getContentResolver().insert(DatabaseContract.UserColumns.CONTENT_URI, values);
        if (result != null) {
            showSnackbarMessage(getResources().getString(R.string.success_store_user));
        } else {
            showSnackbarMessage(getResources().getString(R.string.fail_change_db));
        }
    }

    private void deteleUserData(Uri uri, User githubUser){
        String selectString = USERNAME + " =?";
        String[] projections = {
                DatabaseContract.UserColumns._ID,
                DatabaseContract.UserColumns.USERNAME,
                DatabaseContract.UserColumns.AVATAR_URL,
                DatabaseContract.UserColumns.FOLLOWER,
                DatabaseContract.UserColumns.FOLLOWING
        };
        long result = getContentResolver().delete(DatabaseContract.UserColumns.CONTENT_URI, selectString, new String[]{githubUser.getUsername()});
//        long result = getContentResolver().delete(uri, null, null);
        if (result > 0) {
            showSnackbarMessage(getResources().getString(R.string.success_delete_user));
        } else {
            showSnackbarMessage(getResources().getString(R.string.fail_change_db));
        }
    }

    public String getUserName(){
        return received.getUsername();
    }

    public void setUI(User GithubUser) {
        Log.d(TAG, "oncreate, Username : " + GithubUser.getUsername());

        if (GithubUser.getName() != "-"){
            String detail_user_name = getResources().getString(R.string.user_name_detail, GithubUser.getUsername(), GithubUser.getName());
            nameTv.setText(detail_user_name);
        }else {
            nameTv.setText(GithubUser.getUsername());
        }

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
                    favButton.setVisibility(View.VISIBLE);
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
