package co.navdeep.toptensongs.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import co.navdeep.toptensampler.R;
import co.navdeep.toptensongs.utils.TopTenConstants;

/**
 * Created by Navdeep Sekhon on 12/17/2015.
 * www.navdeep.co
 */
public class PlayerActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        if (savedInstanceState == null) {
            PlayerFragment pf = new PlayerFragment();
            if(getIntent() != null) {
                Bundle args = new Bundle();
                args.putInt(TopTenConstants.INTENT_KEY_POSITION, getIntent().getIntExtra(TopTenConstants.INTENT_KEY_POSITION, 0));
                args.putString(TopTenConstants.INTENT_KEY_ARTIST_NAME, getIntent().getStringExtra(TopTenConstants.INTENT_KEY_ARTIST_NAME));
                args.putParcelableArrayList(TopTenConstants.INTENT_KEY_SONG_LIST, getIntent().getParcelableArrayListExtra(TopTenConstants.INTENT_KEY_SONG_LIST));
                pf.setArguments(args);
            }
            Log.d(TAG, "playerActivity created...creating fragment");
            getSupportFragmentManager().beginTransaction().add(R.id.player_container, pf).commit();
        }
    }
}
