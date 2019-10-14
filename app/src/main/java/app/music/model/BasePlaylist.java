package app.music.model;

import java.util.Calendar;

public class BasePlaylist<MusicType extends BaseMusik> {

    private Calendar createdTime;
    private String playlistName;
    private boolean playlistType;
}
