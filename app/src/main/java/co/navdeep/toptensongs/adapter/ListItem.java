package co.navdeep.toptensongs.adapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Navdeep on 6/28/2015.
 * http://www.navdeep.co
 */
public class ListItem implements Parcelable{

    private String name;
    private String imageUrl;
    private String largerImageUrl;
    private String id;
    private String album;
    private String previewUrl;

    public ListItem(String name, String imageUrl, String id){
        this.name = name;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public ListItem(String name, String imageUrl, String largerImageUrl, String id, String album, String preview_url) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.largerImageUrl = largerImageUrl;
        this.id = id;
        this.album = album;
        this.previewUrl = preview_url;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {

        return id;
    }

    public String getLargerImageUrl() {
        return largerImageUrl;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public boolean isSong(){
        return (!(album == null || album.isEmpty()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ListItem(Parcel parcel){
        name = parcel.readString();
        imageUrl = parcel.readString();
        largerImageUrl = parcel.readString();
        id = parcel.readString();
        album = parcel.readString();
        previewUrl = parcel.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(largerImageUrl);
        dest.writeString(id);
        dest.writeString(album);
        dest.writeString(previewUrl);
    }

    public static Creator<ListItem> CREATOR = new Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel source) {
            return new ListItem(source);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };
}
