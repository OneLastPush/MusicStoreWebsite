package com.trouble.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Seaim Khan
 */
@Entity
@Table(name = "artists")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Artists.findAll", query = "SELECT a FROM Artists a")
            , @NamedQuery(name = "Artists.findById", query = "SELECT a FROM Artists a WHERE a.id = :id")
            , @NamedQuery(name = "Artists.findByArtist", query = "SELECT a FROM Artists a WHERE a.artist = :artist")
        })
public class Artists implements Serializable
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
    @Column(name = "artist")
    private String artist;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artistId")
    private List<Albums> albumsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artistId")
    private List<Songs> songsList;

    public Artists()
    {
    }

    public Artists(Integer id)
    {
        this.id = id;
    }

    public Artists(Integer id, String artist)
    {
        this.id = id;
        this.artist = artist;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    @XmlTransient
    public List<Albums> getAlbumsList()
    {
        return albumsList;
    }

    public void setAlbumsList(List<Albums> albumsList)
    {
        this.albumsList = albumsList;
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
        if (!(object instanceof Artists))
        {
            return false;
        }
        Artists other = (Artists) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.trouble.entities.Artists[ id=" + id + " ]";
    }

}
