package co.navdeep.toptensongs.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import co.navdeep.toptensampler.R;
import co.navdeep.toptensongs.adapter.ListItem;
import co.navdeep.toptensongs.adapter.ListViewAdapter;


/**
 * @author Navdeep Sekhon
 * www.navdeep.co
 */
public class ListFragment extends Fragment {

    public ListFragment() {
    }

    private ListFragmentCallback mCallback;
    private ListViewAdapter mListAdapter;

    public interface ListFragmentCallback {
        public void onItemSelected(int position, ListViewAdapter listViewAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (ListFragmentCallback) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement ListFragmentCallback for ArtistFragmentt");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        mListAdapter = new ListViewAdapter(getActivity(), R.layout.list_item_artist, new ArrayList<ListItem>());

        ListView list = (ListView)rootView.findViewById(R.id.listview_artists);
        list.setAdapter(mListAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String artistId = (String) view.getTag(R.string.tag_item_id);
                mCallback.onItemSelected(position, mListAdapter);
            }
        });
        return rootView;
    }

}
