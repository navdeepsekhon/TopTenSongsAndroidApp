package co.navdeep.toptensongs.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;

import co.navdeep.toptensampler.R;
import co.navdeep.toptensongs.adapter.ListItem;
import co.navdeep.toptensongs.adapter.ListViewAdapter;
import co.navdeep.toptensongs.apiCalls.SpotifyApiCalls;
import co.navdeep.toptensongs.utils.TopTenConstants;
import co.navdeep.toptensongs.utils.Utility;


public class TopSongsActivity extends AppCompatActivity implements ListFragment.ListFragmentCallback{
    private String TAG = getClass().getSimpleName();
    private String mCurrentArtistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_songs);

        if(savedInstanceState == null){
            Intent intent = getIntent();
            if(intent != null && intent.hasExtra(TopTenConstants.INTENT_KEY_ARTIST_NAME) && intent.hasExtra(TopTenConstants.INTENT_KEY_ARTIST_ID)) {
                String id = intent.getStringExtra(TopTenConstants.INTENT_KEY_ARTIST_ID);
                mCurrentArtistName = intent.getStringExtra(TopTenConstants.INTENT_KEY_ARTIST_NAME);
                if(getSupportActionBar() != null)
                    getSupportActionBar().setSubtitle(mCurrentArtistName);
                Log.d(TAG, "topSongsActivity created...calling spotify to fetch songs");
                SpotifyApiCalls.getTopSongs(id, this);
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_songs, menu);
        return true;
    }


    @Override
    public void onItemSelected(int position, ListViewAdapter listViewAdapter) {
        Log.d(TAG, "Song Selected. Position:"+position);
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(TopTenConstants.INTENT_KEY_POSITION, position);
        intent.putExtra(TopTenConstants.INTENT_KEY_ARTIST_NAME, mCurrentArtistName);
        ArrayList<String> previewUrls = new ArrayList<>();
        ArrayList<String> imageUrls = new ArrayList<>();
        for(int i = 0; i < listViewAdapter.getCount(); i++){
            ListItem item = listViewAdapter.getItem(i);
            previewUrls.add(item.getPreviewUrl());
            imageUrls.add(item.getLargerImageUrl());
        }
        intent.putParcelableArrayListExtra(TopTenConstants.INTENT_KEY_SONG_LIST, Utility.convertListViewAdapterToArrayList(listViewAdapter));
        Log.d(TAG, "Starting the playerActivity");
        startActivity(intent);
    }
}
