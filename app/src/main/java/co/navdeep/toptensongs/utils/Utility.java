package co.navdeep.toptensongs.utils;

import java.util.ArrayList;

import co.navdeep.toptensongs.adapter.ListItem;
import co.navdeep.toptensongs.adapter.ListViewAdapter;


/**
 * Created by Navdeep Sekhon on 12/17/2015.
 * www.navdeep.co
 */
public class Utility {

    public static ArrayList<ListItem> convertListViewAdapterToArrayList(ListViewAdapter listViewAdapter){
        ArrayList<ListItem> list = new ArrayList<>();
        for(int i = 0; i < listViewAdapter.getCount(); i++){
            list.add(listViewAdapter.getItem(i));
        }
        return  list;
    }
}
