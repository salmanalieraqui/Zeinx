package com.example.salman.zeinx;

/**
 * Created by Salman on 30-08-2017.
 */

public class SongInfo {
    public String songName,artistName,songUrl;

    public SongInfo() {
    }

    public SongInfo(String songName, String artistName, String songUrl) {
        this.songName = songName;
        this.artistName = artistName;
        this.songUrl = songUrl;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getSongUrl() {
        return songUrl;
    }
}
