package co.navdeep.toptensongs.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import co.navdeep.toptensampler.R;
import co.navdeep.toptensongs.adapter.ListViewAdapter;
import co.navdeep.toptensongs.apiCalls.SpotifyApiCalls;
import co.navdeep.toptensongs.utils.TopTenConstants;
import co.navdeep.toptensongs.utils.Utility;


public class MainActivity extends AppCompatActivity implements ListFragment.ListFragmentCallback {
    private String TAG = getClass().getSimpleName();

    private boolean mTablet = false;
    private String TOP_SONGS_FRAGMENT_TAG = "topsongs";
    private String mCurrentArtistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_top_songs) != null) {
            mTablet = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_top_songs, new ListFragment(), TOP_SONGS_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTablet = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public void onItemSelected(int position, ListViewAdapter listViewAdapter) {
        if(listViewAdapter.isSongView()){
            startPlayerInDialog(position, listViewAdapter);
        } else {
            if(!mTablet) {
                Log.d(TAG, "Artist selected non tablet mode");
                String artistId = listViewAdapter.getItem(position).getId();
                String artistName = listViewAdapter.getItem(position).getName();
                Intent topSongs = new Intent(this, TopSongsActivity.class);
                topSongs.putExtra(TopTenConstants.INTENT_KEY_ARTIST_ID, artistId);
                topSongs.putExtra(TopTenConstants.INTENT_KEY_ARTIST_NAME, artistName);
                Log.d(TAG, "starting topSongs activity for artist:" + artistName);
                startActivity(topSongs);
            } else{
                String artistId = listViewAdapter.getItem(position).getId();
                mCurrentArtistName = listViewAdapter.getItem(position).getName();
                SpotifyApiCalls.getTopSongs(artistId, this);
            }
        }
    }

    private void startPlayerInDialog(int position, ListViewAdapter listViewAdapter){
        Log.d(TAG, "startPlayerInDialog position:" + position);

        PlayerFragment pf = new PlayerFragment();
        Bundle args = new Bundle();

        args.putInt(TopTenConstants.INTENT_KEY_POSITION, position);
        args.putString(TopTenConstants.INTENT_KEY_ARTIST_NAME, mCurrentArtistName);
        args.putParcelableArrayList(TopTenConstants.INTENT_KEY_SONG_LIST, Utility.convertListViewAdapterToArrayList(listViewAdapter));
        pf.setArguments(args);
        pf.show(getSupportFragmentManager(), "Player");
    }
}
