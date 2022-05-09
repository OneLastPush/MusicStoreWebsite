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
@Table(name = "genres")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Genres.findAll", query = "SELECT g FROM Genres g")
            , @NamedQuery(name = "Genres.findById", query = "SELECT g FROM Genres g WHERE g.id = :id")
            , @NamedQuery(name = "Genres.findByGenreName", query = "SELECT g FROM Genres g WHERE g.genreName = :genreName")
        })
public class Genres implements Serializable
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
    @Column(name = "genre_name")
    private String genreName;
    @OneToMany(mappedBy = "lastGenreSearched")
    private List<Users> usersList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genreId")
    private List<Songs> songsList;

    public Genres()
    {
    }

    public Genres(Integer id)
    {
        this.id = id;
    }

    public Genres(Integer id, String genreName)
    {
        this.id = id;
        this.genreName = genreName;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getGenreName()
    {
        return genreName;
    }

    public void setGenreName(String genreName)
    {
        this.genreName = genreName;
    }

    @XmlTransient
    public List<Users> getUsersList()
    {
        return usersList;
    }

    public void setUsersList(List<Users> usersList)
    {
        this.usersList = usersList;
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
        if (!(object instanceof Genres))
        {
            return false;
        }
        Genres other = (Genres) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.trouble.entities.Genres[ id=" + id + " ]";
    }

}
