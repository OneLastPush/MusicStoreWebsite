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
@Table(name = "songwriters")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Songwriters.findAll", query = "SELECT s FROM Songwriters s")
            , @NamedQuery(name = "Songwriters.findById", query = "SELECT s FROM Songwriters s WHERE s.id = :id")
            , @NamedQuery(name = "Songwriters.findBySongwriter", query = "SELECT s FROM Songwriters s WHERE s.songwriter = :songwriter")
        })
public class Songwriters implements Serializable
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
    @Column(name = "songwriter")
    private String songwriter;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "songwriterId")
    private List<SongwriterSongs> songwriterSongsList;

    public Songwriters()
    {
    }

    public Songwriters(Integer id)
    {
        this.id = id;
    }

    public Songwriters(Integer id, String songwriter)
    {
        this.id = id;
        this.songwriter = songwriter;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getSongwriter()
    {
        return songwriter;
    }

    public void setSongwriter(String songwriter)
    {
        this.songwriter = songwriter;
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
        if (!(object instanceof Songwriters))
        {
            return false;
        }
        Songwriters other = (Songwriters) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.trouble.entities.Songwriters[ id=" + id + " ]";
    }

}
