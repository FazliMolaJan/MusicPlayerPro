package app.music.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Genre implements Parcelable {

    private String genre;
    private List<BaseMusik> musicList;

    public Genre(String genre, List<BaseMusik> musicList) {
        this.genre = genre;
        this.musicList = musicList;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<BaseMusik> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<BaseMusik> musicList) {
        this.musicList = musicList;
    }

    public static Creator<Genre> getCREATOR() {
        return CREATOR;
    }

    protected Genre(Parcel in) {
        genre = in.readString();
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
        dest.writeString(genre);
        if (musicList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(musicList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Genre> CREATOR = new Parcelable.Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };
}
