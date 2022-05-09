package com.trouble.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author Seaim Khan
 */
@Entity
@Table(name = "albums")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Albums.findAll", query = "SELECT a FROM Albums a")
            , @NamedQuery(name = "Albums.findById", query = "SELECT a FROM Albums a WHERE a.id = :id")
            , @NamedQuery(name = "Albums.findByAlbumTitle", query = "SELECT a FROM Albums a WHERE a.albumTitle = :albumTitle")
            , @NamedQuery(name = "Albums.findByReleaseDate", query = "SELECT a FROM Albums a WHERE a.releaseDate = :releaseDate")
            , @NamedQuery(name = "Albums.findByAlbumCover", query = "SELECT a FROM Albums a WHERE a.albumCover = :albumCover")
            , @NamedQuery(name = "Albums.findByTrackCount", query = "SELECT a FROM Albums a WHERE a.trackCount = :trackCount")
            , @NamedQuery(name = "Albums.findByDateAdded", query = "SELECT a FROM Albums a WHERE a.dateAdded = :dateAdded")
            , @NamedQuery(name = "Albums.findByCostPrice", query = "SELECT a FROM Albums a WHERE a.costPrice = :costPrice")
            , @NamedQuery(name = "Albums.findByListPrice", query = "SELECT a FROM Albums a WHERE a.listPrice = :listPrice")
            , @NamedQuery(name = "Albums.findBySalePrice", query = "SELECT a FROM Albums a WHERE a.salePrice = :salePrice")
            , @NamedQuery(name = "Albums.findByRemovalStatus", query = "SELECT a FROM Albums a WHERE a.removalStatus = :removalStatus")
            , @NamedQuery(name = "Albums.findByRemovalDate", query = "SELECT a FROM Albums a WHERE a.removalDate = :removalDate")
        })
public class Albums implements Serializable
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
    @Column(name = "album_title")
    private String albumTitle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "release_date")
    @Temporal(TemporalType.DATE)
    private Date releaseDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "album_cover")
    private String albumCover;
    @Basic(optional = false)
    @NotNull
    @Column(name = "track_count")
    private int trackCount;
    @Column(name = "date_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdded;
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
    @Column(name = "removal_status")
    private Boolean removalStatus;
    @Column(name = "removal_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date removalDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventoryId")
    private List<InvoiceDetailsAlbum> invoiceDetailsAlbumList;
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Artists artistId;
    @JoinColumn(name = "recording_label", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Labels recordingLabel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "albumId")
    private List<Songs> songsList;

    public Albums()
    {
    }

    public Albums(Integer id)
    {
        this.id = id;
    }

    public Albums(Integer id, String albumTitle, Date releaseDate, String albumCover, int trackCount, BigDecimal costPrice, BigDecimal listPrice)
    {
        this.id = id;
        this.albumTitle = albumTitle;
        this.releaseDate = releaseDate;
        this.albumCover = albumCover;
        this.trackCount = trackCount;
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

    public String getAlbumTitle()
    {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle)
    {
        this.albumTitle = albumTitle;
    }

    public Date getReleaseDate()
    {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public String getAlbumCover()
    {
        return albumCover;
    }

    public void setAlbumCover(String albumCover)
    {
        this.albumCover = albumCover;
    }

    public int getTrackCount()
    {
        return trackCount;
    }

    public void setTrackCount(int trackCount)
    {
        this.trackCount = trackCount;
    }

    public Date getDateAdded()
    {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded)
    {
        this.dateAdded = dateAdded;
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
    public List<InvoiceDetailsAlbum> getInvoiceDetailsAlbumList()
    {
        return invoiceDetailsAlbumList;
    }

    public void setInvoiceDetailsAlbumList(List<InvoiceDetailsAlbum> invoiceDetailsAlbumList)
    {
        this.invoiceDetailsAlbumList = invoiceDetailsAlbumList;
    }

    public Artists getArtistId()
    {
        return artistId;
    }

    public void setArtistId(Artists artistId)
    {
        this.artistId = artistId;
    }

    public Labels getRecordingLabel()
    {
        return recordingLabel;
    }

    public void setRecordingLabel(Labels recordingLabel)
    {
        this.recordingLabel = recordingLabel;
    }

    @XmlTransient
    public List<Songs> getSongsList()
    {
        return songsList;
    }

    public void setSongsList(List<Songs> songsList)
    {
        this.songsList = songsList;
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
        if (!(object instanceof Albums))
        {
            return false;
        }
        Albums other = (Albums) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.trouble.entities.Albums[ id=" + id + " ]";
    }

}
