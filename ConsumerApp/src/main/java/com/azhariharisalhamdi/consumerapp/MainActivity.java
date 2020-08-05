package com.azhariharisalhamdi.consumerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.azhariharisalhamdi.consumerapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    //create array of github user's list
    ArrayList<HashMap<String, String>> githubUserList;

    ListAdapter adapter;

    //create method to get json data
    public String getUserData(String FileName){
        String dataUser = null;
        try{
            InputStream inputStream = getAssets().open(FileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            dataUser = new String(buffer, "UTF-8");

        }catch (IOException e){
            e.printStackTrace();
        }
        return dataUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set activity title
        setTitle("Github User's");

        //use activity main
        setContentView(R.layout.activity_main);

        //create listview instance
        ListView listView = findViewById(R.id.lv_list);

        //create githubUserList object data
        githubUserList = new ArrayList<>();

        //get json data from file name
        String jsonStr = getUserData("githubuser.json");

        //check if json data null
        if (jsonStr != null)
        {
            try {
                //create userJson object instace and store it in Json array from "users" object
                JSONObject userJson = new JSONObject(jsonStr);
                JSONArray userArray = userJson.getJSONArray("users");

                //iterate array length to store element
                for (int i = 0; i < userArray.length(); i++)
                {
                    //parse Json object by element in user object
                    JSONObject detail = userArray.getJSONObject(i);
                    String username = detail.getString("username");
                    String name = detail.getString("name");

                    String avatar = detail.getString("avatar");
                    int avatarId = getResources().getIdentifier(avatar, null, getPackageName());

                    String company = detail.getString("company");
                    String location = detail.getString("location");
                    int repository = detail.getInt("repository");

                    int follower = detail.getInt("follower");
                    int following = detail.getInt("following");
                    String followerStr = "Follower : " + Integer.toString(follower);
                    String followingStr = "Following : " + Integer.toString(following);

                    //create hash map to store element from json data
                    HashMap<String, String> userData = new HashMap<>();

                    //store parsed element in hashmap
                    userData.put("username", username);
                    userData.put("name", name);
                    userData.put("avatar", avatar);
                    userData.put("avatarId", Integer.toString(avatarId));
                    userData.put("company", company);
                    userData.put("location", location);
                    userData.put("repository", Integer.toString(repository));
                    userData.put("follower", followerStr);
                    userData.put("following", followingStr);

                    //push hashmap per index element in githubUserList
                    githubUserList.add(userData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //set data to view in github user's list
        String[] dataToView = new String[]{"avatarId", "name", "follower", "following"};
        int[] elementId = new int[]{R.id.avatar, R.id.name, R.id.follower, R.id.following};

        adapter = new SimpleAdapter(
                MainActivity.this, githubUserList,
                                    R.layout.item_list,
                                    dataToView,
                                    elementId);
        listView.setAdapter(adapter);

        //get click action position from list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User dataUserSend = new User();
                dataUserSend = getUser(githubUserList, position);
                Intent moveWithObjectIntent = new Intent(MainActivity.this, detail_activity.class);
                moveWithObjectIntent.putExtra(detail_activity.USER_DATA_DETAIL, dataUserSend);
                startActivity(moveWithObjectIntent);
            }
        });
    }

    //parsing method from arraylist hashmap
    public User getUser(ArrayList<HashMap<String, String>> list, int pos)
    {
        User temp = new User();
        HashMap<String, String> tmpData = list.get(pos);
        Set<String> key = tmpData.keySet();
        Iterator it = key.iterator();
        while (it.hasNext()) {
            String keyStr = (String) it.next();
            String dataParse = (String) tmpData.get(keyStr);
            if(keyStr == "username") {
                temp.setUsername(dataParse);
            }
            else if (keyStr == "name") {
                temp.setName(dataParse);
            }
            else if (keyStr == "avatar") {
                temp.setAvatar(dataParse);
            }
            else if (keyStr == "company") {
                temp.setCompany(dataParse);
            }
            else if (keyStr == "location") {
                temp.setLocation(dataParse);
            }
            else if (keyStr == "repository") {
                temp.setPublic_repo(Integer.getInteger(dataParse));
            }
            else if (keyStr == "follower") {
                temp.setFollowers(Integer.getInteger(dataParse));
            }
            else if (keyStr == "following") {
                temp.setFollowing(Integer.getInteger(dataParse));
            }
        }
        return temp;
    }
}
