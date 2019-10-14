package app.music.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Artist implements Parcelable {

    private String artistName;
    private List<String> albumNameList;
    private List<BaseMusik> musicList;

    public Artist(String artistName, List<String> albumNameList, List<BaseMusik> musicList) {
        this.artistName = artistName;
        this.albumNameList = albumNameList;
        this.musicList = musicList;
    }

    public String getArtistName() {
        return artistName;
    }

    public List<String> getAlbumNameList() {
        return albumNameList;
    }

    public List<BaseMusik> getMusicList() {
        return musicList;
    }

    protected Artist(Parcel in) {
        artistName = in.readString();
        if (in.readByte() == 0x01) {
            albumNameList = new ArrayList<>();
            in.readList(albumNameList, String.class.getClassLoader());
        } else {
            albumNameList = null;
        }
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
        dest.writeString(artistName);
        if (albumNameList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(albumNameList);
        }
        if (musicList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(musicList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
