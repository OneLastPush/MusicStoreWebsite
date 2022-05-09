package com.trouble.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Seaim Khan
 */
@Entity
@Table(name = "rss")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Rss.findAll", query = "SELECT r FROM Rss r")
            , @NamedQuery(name = "Rss.findById", query = "SELECT r FROM Rss r WHERE r.id = :id")
            , @NamedQuery(name = "Rss.findByFeed", query = "SELECT r FROM Rss r WHERE r.feed = :feed")
            , @NamedQuery(name = "Rss.findByCurrentRss", query = "SELECT r FROM Rss r WHERE r.currentRss = :currentRss")
        })
public class Rss implements Serializable
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
    @Column(name = "feed")
    private String feed;
    @Column(name = "current_rss")
    private Boolean currentRss;

    public Rss()
    {
    }

    public Rss(Integer id)
    {
        this.id = id;
    }

    public Rss(Integer id, String feed)
    {
        this.id = id;
        this.feed = feed;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getFeed()
    {
        return feed;
    }

    public void setFeed(String feed)
    {
        this.feed = feed;
    }

    public Boolean getCurrentRss()
    {
        return currentRss;
    }

    public void setCurrentRss(Boolean currentRss)
    {
        this.currentRss = currentRss;
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
        if (!(object instanceof Rss))
        {
            return false;
        }
        Rss other = (Rss) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.trouble.entities.Rss[ id=" + id + " ]";
    }

}
