package app.music.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseMusik implements Parcelable {

    @SerializedName("title")
    @Expose
    protected String title;

    @SerializedName("artist")
    @Expose
    protected String artist;

    @SerializedName("year")
    @Expose
    protected String year;

    @SerializedName("album")
    @Expose
    protected String album;

    @SerializedName("genre")
    @Expose
    protected String genre;

    @SerializedName("location")
    @Expose
    protected String location;

    @SerializedName("duration")
    @Expose
    private Integer duration;

    @SerializedName("type")
    @Expose
    protected String type;

    @SerializedName("lyrics")
    @Expose
    private String lyrics;

    @SerializedName("dateModified")
    @Expose
    private String dateModified;

    public BaseMusik() {
    }

    public BaseMusik(String title, String artist, String year, String album, String genre,
                     String location, Integer duration, String type, String lyrics,
                     String dateModified) {
        this.title = title;
        this.artist = artist;
        this.year = year;
        this.album = album;
        this.genre = genre;
        this.location = location;
        this.duration = duration;
        this.type = type;
        this.lyrics = lyrics;
        this.dateModified = dateModified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    protected BaseMusik(Parcel in) {
        title = in.readString();
        artist = in.readString();
        year = in.readString();
        album = in.readString();
        genre = in.readString();
        location = in.readString();
        duration = in.readByte() == 0x00 ? null : in.readInt();
        type = in.readString();
        lyrics = in.readString();
        dateModified = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(year);
        dest.writeString(album);
        dest.writeString(genre);
        dest.writeString(location);
        if (duration == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(duration);
        }
        dest.writeString(type);
        dest.writeString(lyrics);
        dest.writeString(dateModified);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BaseMusik> CREATOR = new Parcelable.Creator<BaseMusik>() {
        @Override
        public BaseMusik createFromParcel(Parcel in) {
            return new BaseMusik(in);
        }

        @Override
        public BaseMusik[] newArray(int size) {
            return new BaseMusik[size];
        }
    };
}
