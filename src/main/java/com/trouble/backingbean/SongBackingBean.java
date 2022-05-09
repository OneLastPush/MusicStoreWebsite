package com.trouble.backingbean;

import beans.action.SearchActionBean;
import com.trouble.entities.Songs;
import com.trouble.JPAController.SongsJpaController;
import com.trouble.entities.Albums;
import com.trouble.entities.Reviews;
import com.trouble.paginator.ReviewPaginator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Transient;

/**
 *
 * @author Frank
 */
@Named("songBacking")
@RequestScoped
public class SongBackingBean implements Serializable
{

    @Inject
    private SongsJpaController songsJpaController;
    @Transient
    private ReviewPaginator paginator;

    private Songs song;
    @Inject
    private SearchActionBean sab;

    public SearchActionBean getSab()
    {
        return sab;
    }

    public void setSab(SearchActionBean sab)
    {
        this.sab = sab;
    }

    /**
     * Get the entity if not instantiated, it will
     *
     * @return song -entity
     */
    public Songs getSong()
    {
        if (song == null)
        {
            song = new Songs();
        }
        return song;
    }

    /**
     * Get list of song
     *
     * @return
     */
    public List<Songs> getAllSongs()
    {
        return songsJpaController.findSongsEntities();
    }

    /**
     * Get songs list from specific album Order by the track number
     *
     * @param album
     *
     * @return
     */
    public List<Songs> getSongsFromAlbum(Albums album)
    {
        return songsJpaController.findSongsFromAlbum(album);
    }

    /**
     * Get artist Name from id
     *
     * @param artistId
     *
     * @return
     */
    /*
     * public String getArtistName(int artistId) { return
     * sab.findArtistName(artistId); }
     */
    /**
     * Find song with id
     *
     * @param id
     *
     * @return
     */
    public Songs getSong(int id)
    {
        song = songsJpaController.findSongs(id);
        return song;
    }

    public ReviewPaginator getReviewPaginator()
    {
        return paginator;
    }

    public List<Reviews> setReviewList(Songs song)
    {
        List<Reviews> list = new ArrayList();
        for(Reviews r: song.getReviewsList()){
            if(r.getApprovalStatus()){
                list.add(r);
            }
        }
        paginator = new ReviewPaginator(list);
        return paginator.getList();
    }

//    public void getToSongPage(Songs song){
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        String outcome = "./songPage.xhtml?songId=" + song.getId();
//        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, outcome);
//    }
    public String getToSongPage(Songs song)
    {

        return "./songPage.xhtml?songId=" + song.getId();

    }

}
