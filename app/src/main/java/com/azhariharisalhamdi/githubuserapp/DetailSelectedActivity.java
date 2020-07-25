package com.azhariharisalhamdi.githubuserapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.azhariharisalhamdi.githubuserapp.adapter.SectionsPagerAdapter;
import com.azhariharisalhamdi.githubuserapp.models.DetailUsers;
import com.azhariharisalhamdi.githubuserapp.rest.BaseApiClient;
import com.azhariharisalhamdi.githubuserapp.rest.DetailUserApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        //get parcel data intent
        received = getIntent().getParcelableExtra(USER_DATA_DETAIL);

        if(received != null) progressBar.setVisibility(View.VISIBLE);

        assert received != null;
        Log.d(TAG, "oncreate, Username : "+received.getUsername());
        getDetailUser_Async(received.getUsername());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        getSupportActionBar().setElevation(0);
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


}
