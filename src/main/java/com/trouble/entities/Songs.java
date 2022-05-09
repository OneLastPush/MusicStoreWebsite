package com.trouble.entities;

import com.trouble.paginator.ReviewPaginator;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Seaim Khan, Frank Birikundavyi
 */
@Entity
@Table(name = "songs")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Songs.findAll", query = "SELECT s FROM Songs s")
            , @NamedQuery(name = "Songs.findById", query = "SELECT s FROM Songs s WHERE s.id = :id")
            , @NamedQuery(name = "Songs.findByTrackTitle", query = "SELECT s FROM Songs s WHERE s.trackTitle = :trackTitle")
            , @NamedQuery(name = "Songs.findBySongLength", query = "SELECT s FROM Songs s WHERE s.songLength = :songLength")
            , @NamedQuery(name = "Songs.findByTrackNumber", query = "SELECT s FROM Songs s WHERE s.trackNumber = :trackNumber")
            , @NamedQuery(name = "Songs.findByCostPrice", query = "SELECT s FROM Songs s WHERE s.costPrice = :costPrice")
            , @NamedQuery(name = "Songs.findByListPrice", query = "SELECT s FROM Songs s WHERE s.listPrice = :listPrice")
            , @NamedQuery(name = "Songs.findBySalePrice", query = "SELECT s FROM Songs s WHERE s.salePrice = :salePrice")
            , @NamedQuery(name = "Songs.findByDateAdded", query = "SELECT s FROM Songs s WHERE s.dateAdded = :dateAdded")
            , @NamedQuery(name = "Songs.findBySingleSong", query = "SELECT s FROM Songs s WHERE s.singleSong = :singleSong")
            , @NamedQuery(name = "Songs.findByRemovalStatus", query = "SELECT s FROM Songs s WHERE s.removalStatus = :removalStatus")
            , @NamedQuery(name = "Songs.findByRemovalDate", query = "SELECT s FROM Songs s WHERE s.removalDate = :removalDate")
        })
public class Songs implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "track_title")
    private String trackTitle;
    @Column(name = "song_length")
    @Temporal(TemporalType.TIME)
    private Date songLength;
    @Basic(optional = false)
    @NotNull
    @Column(name = "track_number")
    private int trackNumber;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cost_price")
    private BigDecimal costPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "list_price")
    private BigDecimal listPrice;
    @Column(name = "sale_price")
    private BigDecimal salePrice;
    @Column(name = "date_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdded;
    @Column(name = "single_song")
    private Boolean singleSong;
    @Column(name = "removal_status")
    private Boolean removalStatus;
    @Column(name = "removal_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date removalDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventoryId")
    private List<InvoiceDetailsSongs> invoiceDetailsSongsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventoryId")
    private List<Reviews> reviewsList;
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Albums albumId;
    @JoinColumn(name = "genre_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Genres genreId;
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Artists artistId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "songId")
    private List<SongwriterSongs> songwriterSongsList;

    public Songs()
    {

    }

    public Songs(Integer id)
    {
        this.id = id;
    }

    public Songs(Integer id, String trackTitle, int trackNumber, BigDecimal costPrice, BigDecimal listPrice)
    {
        this.id = id;
        this.trackTitle = trackTitle;
        this.trackNumber = trackNumber;
        this.costPrice = costPrice;
        this.listPrice = listPrice;

    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getTrackTitle()
    {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle)
    {
        this.trackTitle = trackTitle;
    }

    public Date getSongLength()
    {
        return songLength;
    }

    public void setSongLength(Date songLength)
    {
        this.songLength = songLength;
    }

    public int getTrackNumber()
    {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber)
    {
        this.trackNumber = trackNumber;
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

    public Date getDateAdded()
    {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded)
    {
        this.dateAdded = dateAdded;
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

    @XmlTransient
    public List<InvoiceDetailsSongs> getInvoiceDetailsSongsList()
    {
        return invoiceDetailsSongsList;
    }

    public void setInvoiceDetailsSongsList(List<InvoiceDetailsSongs> invoiceDetailsSongsList)
    {
        this.invoiceDetailsSongsList = invoiceDetailsSongsList;
    }

    @XmlTransient
    public List<Reviews> getReviewsList()
    {
        return reviewsList;
    }

    public void setReviewsList(List<Reviews> reviewsList)
    {
        this.reviewsList = reviewsList;
    }

    public Albums getAlbumId()
    {
        return albumId;
    }

    public void setAlbumId(Albums albumId)
    {
        this.albumId = albumId;
    }

    public Genres getGenreId()
    {
        return genreId;
    }

    public void setGenreId(Genres genreId)
    {
        this.genreId = genreId;
    }

    public Artists getArtistId()
    {
        return artistId;
    }

    public void setArtistId(Artists artistId)
    {
        this.artistId = artistId;
    }

    @XmlTransient
    public List<SongwriterSongs> getSongwriterSongsList()
    {
        return songwriterSongsList;
    }

    public void setSongwriterSongsList(List<SongwriterSongs> songwriterSongsList)
    {
        this.songwriterSongsList = songwriterSongsList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Songs))
        {
            return false;
        }
        Songs other = (Songs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Songs:" + "Title: " + trackTitle + ", Price: $" + listPrice;
    }

    
    
}
