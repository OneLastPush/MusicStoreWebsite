package beans.action;

import com.trouble.entities.Albums;
import com.trouble.entities.Albums_;
import com.trouble.entities.Artists_;
import com.trouble.entities.Songs;
import com.trouble.entities.Songs_;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

/**
 *
 * @author Frank Birikundavyi
 */
@RequestScoped
@Named("searchView")
public class SearchActionBean implements Serializable
{

    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;

    private String searchInput;
    private List<Albums> albumResult;
    private List<Songs> songsResult;
    private String by;
    private String resultTypeOption;
    private Date dateFrom;
    private Date dateTo;

    public SearchActionBean()
    {
        by = "title";
        resultTypeOption = "song";
    }

    public String getBy()
    {
        return by;
    }

    public void setBy(String by)
    {
        this.by = by;
    }

    public Date getDateFrom()
    {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom)
    {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo()
    {
        return dateTo;
    }

    public void setDateTo(Date dateTo)
    {
        this.dateTo = dateTo;
    }

    public String getResultTypeOption()
    {
        return resultTypeOption;
    }

    public void setResultTypeOption(String resultTypeOption)
    {
        this.resultTypeOption = resultTypeOption;
    }

    public List<Albums> getAlbumResult()
    {
        return albumResult;
    }

    public void setAlbumResult(List<Albums> albumResult)
    {
        this.albumResult = albumResult;
    }

    public List<Songs> getSongsResult()
    {
        return songsResult;
    }

    public void setSongsResult(List<Songs> songsResult)
    {
        this.songsResult = songsResult;
    }

    public String getSearchInput()
    {
        return searchInput;
    }

    public void setSearchInput(String searchInput)
    {
        this.searchInput = searchInput;
    }

    /**
     * returns the searchPage
     *
     * @return search page
     */
    public String getSearchPage()
    {
        return "./searchPage.xhtml";
    }
    /**
     * Get the size of the populated arraylist
     * Everytime a search is perform the previous result List is no longer refer
     * so anytime, there is only one list populated
     * @return size of album list or songs list
     */
    public int getSize()
    {
        int size = -1;
        if (songsResult != null)
            if (!songsResult.isEmpty())
                size = songsResult.size();
        
        if (albumResult != null)
            if (!albumResult.isEmpty())
                size = albumResult.size();
        
        if (size == -1)
            size = 0;
        
        return size;
    }
    /**
     * This method redirect to a specific song page
     */
    private void redirectToSongPage()
    {
        try
        {
            FacesContext.getCurrentInstance().getExternalContext().redirect("songPage.xhtml?songId=" + songsResult.get(0).getId());
        }
        catch (IOException ex)
        {
            Logger.getLogger(SearchActionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * This method redirect to a specific album page
     */
    private void redirectToAlbumPage()
    {
        try
        {
            FacesContext.getCurrentInstance().getExternalContext().redirect("albumPage.xhtml?albumId=" + albumResult.get(0).getId());
        }
        catch (IOException ex)
        {
            Logger.getLogger(SearchActionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method retrieve all songs which contains searchInput within
     *
     * @param searchTitle
     *
     * @return List of Songs
     */
    public void findAllSongsByTrackTitle(String searchTitle)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Songs> song = cq.from(Songs.class);
        cq.where(cb.like(song.get(Songs_.trackTitle), "%" + searchTitle + "%"));
        TypedQuery<Songs> query = em.createQuery(cq);
        songsResult = query.getResultList();
        albumResult = new ArrayList<>();
        if (songsResult.size() == 1)
        {
            redirectToSongPage();
        }
    }
    /**
     * This method retrieve all songs by the artist's name
     * @param searchArtist 
     */
    public void findAllSongsByArtist(String searchArtist)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Songs> song = cq.from(Songs.class);
        Join artist = song.join(Songs_.artistId);
        cq.where(cb.like(artist.get(Artists_.artist), "%" + searchArtist + "%"));
        TypedQuery<Songs> query = em.createQuery(cq);
        songsResult = query.getResultList();
        albumResult = new ArrayList<>();
        if (songsResult.size() == 1)
        {
            redirectToSongPage();
        }
    }

    /**
     * This method returns all the songs added between two different days
     *
     * if one song found it will redirect to its page
     * @param from
     * @param to
     */
    public void findAllSongsByDate(Date from, Date to)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Songs> song = cq.from(Songs.class);
        cb.between(song.get(Songs_.dateAdded), from, to);
        TypedQuery<Songs> query = em.createQuery(cq);
        songsResult = query.getResultList();
        albumResult = new ArrayList<>();
        if (songsResult.size() == 1)
        {
            redirectToSongPage();
        }
    }
    /**
     * This method returns a list of album  by the date it was entered in DB
     * 
     * if single entry found, it will be redirected to the album page
     * @param from
     * @param to 
     */
    public void findAllAlbumsByDate(Date from, Date to)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Albums> album = cq.from(Albums.class);
        cb.between(album.get(Albums_.dateAdded), from, to);
        TypedQuery<Albums> query = em.createQuery(cq);
        songsResult = new ArrayList<>();
        albumResult = query.getResultList();
        if (albumResult.size() == 1)
        {
            redirectToAlbumPage();
        }
    }
    /**
     * This method retrieve a list of albums by the artist name
     * 
     * if single entry found, it will be redirected to the album page 
     * @param searchArtist 
     */
    public void findAllAlbumsByArtist(String searchArtist)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Albums> album = cq.from(Albums.class);
        Join artist = album.join(Albums_.artistId);
        cq.where(cb.like(artist.get(Artists_.artist), "%" + searchArtist + "%"));
        TypedQuery<Albums> query = em.createQuery(cq);
        songsResult = new ArrayList<>();
        albumResult = query.getResultList();
        if (albumResult.size() == 1)
        {
            redirectToAlbumPage();
        }
    }
    /**
    * Find all the album by title
    * @param searchTitle 
    */
    public void findAllAlbumsByTitle(String searchTitle)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Albums> song = cq.from(Albums.class);
        cq.where(cb.like(song.get(Albums_.albumTitle), "%" + searchTitle + "%"));
        TypedQuery<Albums> query = em.createQuery(cq);
        songsResult = new ArrayList<>();
        albumResult = query.getResultList();
        if (albumResult.size() == 1)
        {
            redirectToAlbumPage();
        }
    }
    /**
     * This method select the current search method & apply it
     */
    public void search()
    {
        if ("song".equals(resultTypeOption))
        {
            switch (by)
            {
                case "title":
                    findAllSongsByTrackTitle(searchInput);
                    break;
                case "artist":
                    findAllSongsByArtist(searchInput);
                    break;
                case "date":
                    findAllSongsByDate(dateFrom, dateTo);
                    break;
            }
        }
        else
        {
            switch (by)
            {
                case "title":
                    findAllAlbumsByTitle(searchInput);
                    break;
                case "artist":
                    findAllAlbumsByArtist(searchInput);
                    break;
                case "date":
                    findAllAlbumsByDate(dateFrom, dateTo);
                    break;
            }
        }
    }

}
