package app.music.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnlineMusik extends BaseMusik implements Parcelable {

    @SerializedName("musicId")
    @Expose
    private String musicId;

    @SerializedName("coverArt")
    @Expose
    private String coverArt;

    @SerializedName("mvLink")
    @Expose
    private String mvLink;

    public OnlineMusik() {
    }

    public OnlineMusik(String title, String artist, String year, String album, String genre,
                       String location, Integer duration, String type, String lyrics,
                       String dateModified, String musicId, String coverArt, String mvLink) {
        super(title, artist, year, album, genre, location, duration, type, lyrics, dateModified);
        this.musicId = musicId;
        this.coverArt = coverArt;
        this.mvLink = mvLink;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getCoverArt() {
        return coverArt;
    }

    public void setCoverArt(String coverArt) {
        this.coverArt = coverArt;
    }

    public String getMvLink() {
        return mvLink;
    }

    public void setMvLink(String mvLink) {
        this.mvLink = mvLink;
    }

    protected OnlineMusik(Parcel in) {
        super(in);
        musicId = in.readString();
        coverArt = in.readString();
        mvLink = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(musicId);
        dest.writeString(coverArt);
        dest.writeString(mvLink);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OnlineMusik> CREATOR = new Parcelable.Creator<OnlineMusik>() {
        @Override
        public OnlineMusik createFromParcel(Parcel in) {
            return new OnlineMusik(in);
        }

        @Override
        public OnlineMusik[] newArray(int size) {
            return new OnlineMusik[size];
        }
    };
}
