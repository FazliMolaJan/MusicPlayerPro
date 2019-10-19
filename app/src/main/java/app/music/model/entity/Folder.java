package app.music.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Folder implements Parcelable {

    private String folderName;
    private List<BaseMusik> musicList;

    public Folder(String folderName, List<BaseMusik> musicList) {
        this.folderName = folderName;
        this.musicList = musicList;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public List<BaseMusik> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<BaseMusik> musicList) {
        this.musicList = musicList;
    }

    protected Folder(Parcel in) {
        folderName = in.readString();
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
        dest.writeString(folderName);
        if (musicList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(musicList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Folder> CREATOR = new Parcelable.Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel in) {
            return new Folder(in);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };
}
