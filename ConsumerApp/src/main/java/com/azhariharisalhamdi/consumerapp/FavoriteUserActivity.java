package com.azhariharisalhamdi.consumerapp;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azhariharisalhamdi.consumerapp.adapter.FavoriteUserAdapter;
import com.azhariharisalhamdi.consumerapp.database.DatabaseContract;
import com.azhariharisalhamdi.consumerapp.helper.MappingHelper;
import com.azhariharisalhamdi.consumerapp.models.User;
import com.azhariharisalhamdi.consumerapp.settings.SettingsActivity;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FavoriteUserActivity extends AppCompatActivity implements LoadUserCallback {

    public static final String EXTRA_USER = "extra_user";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;
    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    private ProgressBar progressBar;
    private RecyclerView rvFavoriteUsers;
    private FavoriteUserAdapter adapter;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_user);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Favorite User");

        progressBar = findViewById(R.id.progressBar);
        rvFavoriteUsers = findViewById(R.id.favoriteUser);
        rvFavoriteUsers.setLayoutManager(new LinearLayoutManager(this));
        rvFavoriteUsers.setHasFixedSize(true);

        adapter = new FavoriteUserAdapter(this);
        rvFavoriteUsers.setAdapter(adapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver dataObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(DatabaseContract.UserColumns.CONTENT_URI, true, dataObserver);

        if (savedInstanceState == null) {
            new LoadUserListAsync(this, (LoadUserCallback) this).execute();
        } else {
            ArrayList<User> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListUsers(list);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListUsers());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.other_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(FavoriteUserActivity.this, SettingsActivity.class);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<User> users) {
        progressBar.setVisibility(View.INVISIBLE);
        if (users.size() > 0) {
            adapter.setListUsers(users);
        } else {
            adapter.setListUsers(new ArrayList<User>());
            showSnackbarMessage(getResources().getString(R.string.no_data_saved));
        }
    }

    private static class LoadUserListAsync extends AsyncTask<Void, Void, ArrayList<User>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadUserCallback> weakCallback;

        private LoadUserListAsync(Context context, LoadUserCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.UserColumns.CONTENT_URI, null, null, null, null);
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);
            weakCallback.get().postExecute(users);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == DetailSelectedActivity.REQUEST_ADD) {
                if (resultCode == DetailSelectedActivity.RESULT_ADD) {
                    User user = data.getParcelableExtra(DetailSelectedActivity.EXTRA_USER);

                    adapter.addItem(user);
                    rvFavoriteUsers.smoothScrollToPosition(adapter.getItemCount() - 1);

                    showSnackbarMessage("Satu item berhasil ditambahkan");
                }
            }
            else if (requestCode == DetailSelectedActivity.REQUEST_UPDATE) {
                if (resultCode == DetailSelectedActivity.RESULT_UPDATE) {

                    User user = data.getParcelableExtra(DetailSelectedActivity.EXTRA_USER);
                    int position = data.getIntExtra(DetailSelectedActivity.EXTRA_POSITION, 0);

                    adapter.updateItem(position, user);
                    rvFavoriteUsers.smoothScrollToPosition(position);

                    showSnackbarMessage("Satu item berhasil diubah");
                }
                else if (resultCode == DetailSelectedActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(DetailSelectedActivity.EXTRA_POSITION, 0);

                    adapter.removeItem(position);

                    showSnackbarMessage("Satu item berhasil dihapus");
                }
            }
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvFavoriteUsers, message, Snackbar.LENGTH_SHORT).show();
    }

    public static class DataObserver extends ContentObserver {
        final Context context;
        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadUserListAsync(context, (LoadUserCallback) context).execute();
        }
    }
}

interface LoadUserCallback {
    void preExecute();
    void postExecute(ArrayList<User> users);
}