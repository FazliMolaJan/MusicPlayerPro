package app.music.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Music extends BaseMusik implements Parcelable {

    private String fileName;
    private String trackNumber;

    public Music() {

    }

    public Music(String title, String artist, String year, String album, String genre,
                 String location, Integer duration, String type, String lyrics, String dateModified,
                 String fileName, String trackNumber) {
        super(title, artist, year, album, genre, location, duration, type, lyrics, dateModified);
        this.fileName = fileName;
        this.trackNumber = trackNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    protected Music(Parcel in) {
        super(in);
        fileName = in.readString();
        trackNumber = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(fileName);
        dest.writeString(trackNumber);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Music> CREATOR = new Parcelable.Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };
}
