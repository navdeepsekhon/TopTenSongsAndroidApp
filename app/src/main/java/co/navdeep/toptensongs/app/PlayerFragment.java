package co.navdeep.toptensongs.app;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.navdeep.toptensampler.R;
import co.navdeep.toptensongs.adapter.ListItem;
import co.navdeep.toptensongs.utils.TopTenConstants;

/**
 * Created by Navdeep Sekhon on 12/17/2015.
 * www.navdeep.co
 */
public class PlayerFragment extends DialogFragment {
    private String TAG = getClass().getSimpleName();
    private MediaPlayer mMediaPlayer;
    private ImageView mImageView;
    private TextView mArtistName;
    private TextView mAlbumName;
    private TextView mSongName;
    private ImageView mPlayButton;
    private ImageView mPauseButton;
    private SeekBar mSeekBar;
    private Handler mHandler = new Handler();

    int mCurrentTrack;
    ArrayList<ListItem> mListItems;

    public PlayerFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_player, container, false);

        extractViewRefs(rootView);
        addOnClickListenersForMediaButtons(rootView);

        mCurrentTrack = getArguments().getInt(TopTenConstants.INTENT_KEY_POSITION);
        mListItems = getArguments().getParcelableArrayList(TopTenConstants.INTENT_KEY_SONG_LIST);
        String artistName = getArguments().getString(TopTenConstants.INTENT_KEY_ARTIST_NAME);
        mArtistName.setText(artistName);

        playSong(mCurrentTrack);
        initializeSeekBar();
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mMediaPlayer.isPlaying())
            mMediaPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mMediaPlayer.isPlaying())
            mMediaPlayer.start();
    }

    public void playSong(final int position){
        try {
            Log.d(TAG, "About to play song at url:" + mListItems.get(position).getPreviewUrl());
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(mListItems.get(position).getPreviewUrl());

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mCurrentTrack = position;
                    updateUI(mListItems.get(mCurrentTrack));
                    Log.d(TAG,"Starting the playback now...");
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Log.d(TAG, "Song playback complete...moving to next song");
                            playNext();
                        }
                    });
                }
            });

            mMediaPlayer.prepareAsync();

        } catch (Exception e){

        }
    }

    public void playPauseToggle(){
        if(mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlayButton.setVisibility(View.VISIBLE);
            mPauseButton.setVisibility(View.GONE);
        }
        else {
            mMediaPlayer.start();
            mPlayButton.setVisibility(View.GONE);
            mPauseButton.setVisibility(View.VISIBLE);
        }
    }

    public void playPrevious(){
        mMediaPlayer.stop();
        mMediaPlayer.release();
        int previous = mCurrentTrack -1 < 0 ? mListItems.size() - 1 : mCurrentTrack - 1;
        playSong(previous);

    }

    public void playNext(){
        mMediaPlayer.stop();
        mMediaPlayer.release();
        int next = mCurrentTrack + 1 >=  mListItems.size() ? 0 : mCurrentTrack + 1;
        playSong(next);
    }

    private void addOnClickListenersForMediaButtons(View rootView){
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPauseToggle();
            }
        });

        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPauseToggle();
            }
        });

        ImageView previousBtn = (ImageView)rootView.findViewById(R.id.player_previous);
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrevious();
            }
        });

        ImageView nextBtn = (ImageView)rootView.findViewById(R.id.player_next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        });
    }

    private void extractViewRefs(View rootView){
        mImageView = (ImageView) rootView.findViewById(R.id.player_imageview);
        mArtistName = (TextView) rootView.findViewById(R.id.player_artist_name_textview);
        mAlbumName = (TextView) rootView.findViewById(R.id.player_album_name_textview);
        mSongName = (TextView) rootView.findViewById(R.id.player_song_name_textview);
        mPlayButton = (ImageView) rootView.findViewById(R.id.player_play);
        mPauseButton = (ImageView) rootView.findViewById(R.id.player_pause);
        mSeekBar = (SeekBar) rootView.findViewById(R.id.player_seekbar);
    }

    private void updateUI(ListItem item){
        Log.d(TAG,"Current songName:" + item.getName() + " Album:" + item.getAlbum());
        Picasso.with(getActivity()).load(item.getLargerImageUrl()).into(mImageView);
        mAlbumName.setText(item.getAlbum());
        mSongName.setText(item.getName());
        mPauseButton.setVisibility(View.VISIBLE);
        mPlayButton.setVisibility(View.GONE);
        mSeekBar.setProgress(0);
        mSeekBar.setMax(mMediaPlayer.getDuration()/1000);
    }

    private void initializeSeekBar(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
                    int progress = mMediaPlayer.getCurrentPosition()/1000;
                    mSeekBar.setProgress(progress);
                }
                mHandler.postDelayed(this, 1000);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mMediaPlayer != null && fromUser)
                    mMediaPlayer.seekTo(progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
