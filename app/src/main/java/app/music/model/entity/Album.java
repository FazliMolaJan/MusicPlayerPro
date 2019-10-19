package app.music.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Album implements Parcelable {

    private String albumName;
    private List<BaseMusik> musicList;

    public Album(String albumName, List<BaseMusik> musicList) {
        this.albumName = albumName;
        this.musicList = musicList;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public List<BaseMusik> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<BaseMusik> musicList) {
        this.musicList = musicList;
    }

    protected Album(Parcel in) {
        albumName = in.readString();
        if (in.readByte() == 0x01) {
            musicList = new ArrayList<>();
            in.readList(musicList, BaseMusik.class.getClassLoader());
        } else {
            musicList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumName);
        if (musicList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(musicList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
}
