package beans.action;

import com.trouble.JPAController.SongsJpaController;
import com.trouble.entities.Albums;
import com.trouble.entities.Artists;
import com.trouble.entities.Genres;
import com.trouble.entities.Labels;
import com.trouble.entities.Songs;
import com.trouble.entities.Songwriters;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Frank Birikundavyi
 */
@SessionScoped
@Named("track")
public class SongActionBean implements Serializable
{

    @Inject
    private SongsJpaController songsController;

    private Songs current;

    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;

    private Integer id;
    private Albums album;
    private String trackTitle;
    private Artists artist;
    private Date songLength;
    private Integer trackNumber;
    private Genres genre;
    private BigDecimal costPrice;
    private BigDecimal listPrice;
    private BigDecimal salePrice;
    private Boolean singleSong;
    private Boolean removalStatus;
    private Date removalDate;
    private Songwriters songwriter;
    private final Date currentDate = new Date();

    //Fields in case track is a single (have to copy data over to album)
    private Labels recordingLabel;
    private String albumCover;
    private String releaseDate;
    //Track count = 1, Album title = track title, Prices, removal, and artist
    //stuff are same across
    
    public SongActionBean()
    {
    }

    @PostConstruct
    public void init()
    {
        removalStatus = false;
        singleSong = false;
    }

    public void onload()
    {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        id = (int) ec.getRequest();
        this.current = songsController.findSongs(id);
    }

    public Songs getCurrent()
    {
        return current;
    }

    public void setCurrent(Songs current)
    {
        this.current = current;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Albums getAlbum()
    {
        return album;
    }

    public void setAlbum(Albums album)
    {
        this.album = album;
    }

    public String getTrackTitle()
    {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle)
    {
        this.trackTitle = trackTitle;
    }

    public Artists getArtist()
    {
        return artist;
    }

    public void setArtist(Artists artist)
    {
        this.artist = artist;
    }

    public Date getSongLength()
    {
        return songLength;
    }

    public void setSongLength(Date songLength)
    {
        this.songLength = songLength;
    }

    public Integer getTrackNumber()
    {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber)
    {
        this.trackNumber = trackNumber;
    }

    public Genres getGenre()
    {
        return genre;
    }

    public void setGenre(Genres genre)
    {
        this.genre = genre;
    }

    public BigDecimal getCostPrice()
    {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice)
    {
        this.costPrice = costPrice;
    }

    public BigDecimal getListPrice()
    {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice)
    {
        this.listPrice = listPrice;
    }

    public BigDecimal getSalePrice()
    {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice)
    {
        this.salePrice = salePrice;
    }

    public Boolean getSingleSong()
    {
        return singleSong;
    }

    public void setSingleSong(Boolean singleSong)
    {
        this.singleSong = singleSong;
    }

    public Boolean getRemovalStatus()
    {
        return removalStatus;
    }

    public void setRemovalStatus(Boolean removalStatus)
    {
        this.removalStatus = removalStatus;
    }

    public Date getRemovalDate()
    {
        return removalDate;
    }

    public void setRemovalDate(Date removalDate)
    {
        this.removalDate = removalDate;
    }

    public Date getCurrentDate()
    {
        return currentDate;
    }

    public Songwriters getSongwriter()
    {
        return songwriter;
    }

    public void setSongwriter(Songwriters songwriter)
    {
        this.songwriter = songwriter;
    }

    public Labels getRecordingLabel()
    {
        return recordingLabel;
    }

    public void setRecordingLabel(Labels recordingLabel)
    {
        this.recordingLabel = recordingLabel;
    }

    public String getAlbumCover()
    {
        return albumCover;
    }

    public void setAlbumCover(String albumCover)
    {
        this.albumCover = albumCover;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    /**
     * This method gets the current price of a selected track If there is no
     * sale price, it return the original price
     *
     * @param Song entity object
     *
     * @return current price - String format 0.00
     */
    public String getCurrentPrice(Songs s)
    {
        String currentPrice;
        if (s.getSalePrice().equals(0))
        {
            currentPrice = s.getListPrice().toPlainString();
        }
        else
        {
            currentPrice = s.getSalePrice().toPlainString();
        }
        return currentPrice;
    }

    public String redirectBackToAlbum(int songId){
        Songs s = songsController.findSongs(songId);
        return "albumPage.xhtml?albumId="+s.getAlbumId().getId();
    }
}
