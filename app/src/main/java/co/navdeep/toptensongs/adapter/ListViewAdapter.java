package co.navdeep.toptensongs.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.navdeep.toptensampler.R;


/**
 * Created by Navdeep on 6/28/2015.
 * http://www.navdeep.co
 */
public class ListViewAdapter extends ArrayAdapter<ListItem>{
    Context context;
    private boolean isSongView = false;

    public ListViewAdapter(Context context, int resource, List<ListItem> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public class ViewHolder{
        ImageView image;
        LinearLayout textLayout;
        boolean isSong;

        public ImageView getImage() {
            return image;
        }

        public LinearLayout getTextLayout() {
            return textLayout;
        }

        public boolean isSong() {
            return isSong;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ListItem  item = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_artist, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.list_item_artist_image);
            holder.textLayout = (LinearLayout)convertView.findViewById(R.id.list_item_text_layout);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        TextView name = (TextView)holder.textLayout.findViewById(R.id.list_item_artist_name);
        name.setText(item.getName());

        if(isSongView) {
            TextView album = (TextView) holder.textLayout.findViewById(R.id.list_item_album_name);
            album.setText(item.getAlbum());
            album.setVisibility(View.VISIBLE);
            convertView.setTag(R.string.tag_preview_url, item.getPreviewUrl());
        }

        convertView.setTag(R.string.tag_item_id, item.getId());
        convertView.setTag(R.string.tag_larger_image_url, item.getLargerImageUrl());

        if(item.getImageUrl() != "")
            Picasso.with(context).load(item.getImageUrl()).into(holder.image);
        else
            holder.image.setImageResource(R.drawable.ic_action_music_1);

        holder.isSong = isSongView;

        return convertView;
    }

    public void setIsSongView(boolean isSongView) {
        this.isSongView = isSongView;
    }

    public boolean isSongView() {
        return isSongView;
    }
}
