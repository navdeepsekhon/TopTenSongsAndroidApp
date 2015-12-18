package co.navdeep.toptensongs.apiCalls;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.navdeep.toptensampler.R;
import co.navdeep.toptensongs.adapter.ListItem;
import co.navdeep.toptensongs.adapter.ListViewAdapter;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Navdeep on 6/28/2015.
 * http://www.navdeep.co
 */
public class SpotifyApiCalls {

    public static void getAutoCompleteRequest(String searchString, Activity caller){
        Log.d("getAutoCompleteRequest", "Request:" +searchString);
        if("".equals(searchString.replaceAll("\\*", "")) || searchString.replaceAll("\\*","").length() < 3) {
            updateAdapter(new ArrayList<ListItem>(), caller, false);
            return;
        }
        final Activity activity = caller;
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        HashMap<String, Object> params = new HashMap<>();
        params.put("limit", "10");
        spotify.searchArtists(searchString, params, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                ArrayList<ListItem> list = new ArrayList<ListItem>();
                for(Artist a : artistsPager.artists.items){
                    list.add(new ListItem(a.name, (a.images.size() > 0)? a.images.get(0).url : "", a.id));
                }

                updateAdapter(list, activity, false);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("AutoComplete", "Got error " + error.toString());
            }
        });
    }

    public static void getTopSongs(String artistId, Activity caller){
        Log.d("getTopSongs()", "Called for artistId:" + artistId);
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        final Activity activity = caller;
        HashMap<String, Object> params = new HashMap<>();
        params.put("country", "US");

        spotify.getArtistTopTrack(artistId, params, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {
                ArrayList<ListItem> list = new ArrayList<ListItem>();
                for(Track t : tracks.tracks){
                    String imageUrl = "";
                    String largerImageUrl = "";
                    int totalImages = t.album.images.size();
                    if(totalImages > 0){
                        imageUrl = t.album.images.get(totalImages - 1).url;
                        largerImageUrl = t.album.images.get(0).url;
                    }
                    list.add(new ListItem(t.name, imageUrl, largerImageUrl, t.id, t.album.name, t.preview_url));
                }

                updateAdapter(list, activity, true);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("getTopSongs", "Got error " + error.toString());
            }
        });

    }
    private static void updateAdapter(List<ListItem> list, Activity activity, boolean isSongView){
        ListView listView = null;
        if(!isSongView)
            listView = (ListView)activity.findViewById(R.id.listview_artists);
        else
            listView = (ListView)activity.findViewById(R.id.fragment_top_songs).findViewById(R.id.listview_artists);
        ListViewAdapter adapter = ((ListViewAdapter)listView.getAdapter());
        adapter.clear();
        adapter.setIsSongView(isSongView);
        adapter.addAll(list);
    }
}
