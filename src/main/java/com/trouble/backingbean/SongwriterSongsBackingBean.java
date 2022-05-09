package com.trouble.backingbean;

import com.trouble.entities.SongwriterSongs;
import com.trouble.JPAController.SongwriterSongsJpaController;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Frank
 */
@Named("songwriterSongsBacking")
@RequestScoped
public class SongwriterSongsBackingBean implements Serializable
{

    @Inject
    private SongwriterSongsJpaController songwriterSongsJpaController;

    private SongwriterSongs songwriterSong;

    /**
     * Get the entity if not instantiated, it will
     *
     * @return songwriterSong -entity
     */
    public SongwriterSongs getSongwriterSong()
    {
        if (songwriterSong == null)
        {
            songwriterSong = new SongwriterSongs();
        }
        return songwriterSong;
    }

    /**
     * Get list of songwriterSong
     *
     * @return
     */
    public List<SongwriterSongs> getAllSongwriterSongs()
    {
        return songwriterSongsJpaController.findSongwriterSongsEntities();
    }
}
