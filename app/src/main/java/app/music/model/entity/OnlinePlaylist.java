package app.music.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OnlinePlaylist implements Parcelable {

    private Calendar mCreatedTime;
    private String playlistName;
    private List<OnlineMusik> musicList;
    private String accentColorMode;
    private OnlineMusik thumbnailSong;
    private OnlineMusik newestSong;
    private String accentColor;
    private boolean isOnlinePlaylist;

    public OnlinePlaylist(Calendar mCreatedTime, String playlistName, List<OnlineMusik> musicList,
                          String accentColorMode, OnlineMusik thumbnailSong, OnlineMusik newestSong,
                          String accentColor, boolean isOnlinePlaylist) {
        this.mCreatedTime = mCreatedTime;
        this.playlistName = playlistName;
        this.musicList = musicList;
        this.accentColorMode = accentColorMode;
        this.thumbnailSong = thumbnailSong;
        this.newestSong = newestSong;
        this.accentColor = accentColor;
        this.isOnlinePlaylist = isOnlinePlaylist;
    }

    public Calendar getCreatedTime() {
        return mCreatedTime;
    }

    public void setCreatedTime(Calendar mCreatedTime) {
        this.mCreatedTime = mCreatedTime;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public List<OnlineMusik> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<OnlineMusik> musicList) {
        this.musicList = musicList;
    }

    public String getAccentColorMode() {
        return accentColorMode;
    }

    public void setAccentColorMode(String accentColorMode) {
        this.accentColorMode = accentColorMode;
    }

    public OnlineMusik getThumbnailSong() {
        return thumbnailSong;
    }

    public void setThumbnailSong(OnlineMusik thumbnailSong) {
        this.thumbnailSong = thumbnailSong;
    }

    public OnlineMusik getNewestSong() {
        return newestSong;
    }

    public void setNewestSong(OnlineMusik newestSong) {
        this.newestSong = newestSong;
    }

    public String getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(String accentColor) {
        this.accentColor = accentColor;
    }

    public boolean isOnlinePlaylist() {
        return isOnlinePlaylist;
    }

    public void setOnlinePlaylist(boolean onlinePlaylist) {
        isOnlinePlaylist = onlinePlaylist;
    }

    protected OnlinePlaylist(Parcel in) {
        mCreatedTime = (Calendar) in.readValue(Calendar.class.getClassLoader());
        playlistName = in.readString();
        if (in.readByte() == 0x01) {
            musicList = new ArrayList<OnlineMusik>();
            in.readList(musicList, OnlineMusik.class.getClassLoader());
        } else {
            musicList = null;
        }
        accentColorMode = in.readString();
        thumbnailSong = (OnlineMusik) in.readValue(OnlineMusik.class.getClassLoader());
        newestSong = (OnlineMusik) in.readValue(OnlineMusik.class.getClassLoader());
        accentColor = in.readString();
        isOnlinePlaylist = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mCreatedTime);
        dest.writeString(playlistName);
        if (musicList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(musicList);
        }
        dest.writeString(accentColorMode);
        dest.writeValue(thumbnailSong);
        dest.writeValue(newestSong);
        dest.writeString(accentColor);
        dest.writeByte((byte) (isOnlinePlaylist ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Creator<OnlinePlaylist> CREATOR = new Creator<OnlinePlaylist>() {
        @Override
        public OnlinePlaylist createFromParcel(Parcel in) {
            return new OnlinePlaylist(in);
        }

        @Override
        public OnlinePlaylist[] newArray(int size) {
            return new OnlinePlaylist[size];
        }
    };
}
