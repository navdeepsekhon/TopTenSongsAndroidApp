package co.navdeep.toptensongs.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import co.navdeep.toptensampler.R;
import co.navdeep.toptensongs.apiCalls.SpotifyApiCalls;

/**
 * Created by Navdeep on 7/4/2015.
 * http://www.navdeep.co
 */
public class SearchFieldFragment extends Fragment {


    public SearchFieldFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_search_field, container, false);
        EditText editText = (EditText)rootView.findViewById(R.id.search_box);
        editText.addTextChangedListener(getTextWatcherForSearchBox(getActivity()));
        return rootView;
    }

    private TextWatcher getTextWatcherForSearchBox(final Activity activity){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SpotifyApiCalls.getAutoCompleteRequest(s.toString() + "*", activity);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

}
