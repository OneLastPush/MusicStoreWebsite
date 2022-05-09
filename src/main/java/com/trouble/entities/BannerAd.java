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
@Table(name = "banner_ad")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "BannerAd.findAll", query = "SELECT b FROM BannerAd b")
            , @NamedQuery(name = "BannerAd.findById", query = "SELECT b FROM BannerAd b WHERE b.id = :id")
            , @NamedQuery(name = "BannerAd.findByCompany", query = "SELECT b FROM BannerAd b WHERE b.company = :company")
            , @NamedQuery(name = "BannerAd.findByBannerPic", query = "SELECT b FROM BannerAd b WHERE b.bannerPic = :bannerPic")
            , @NamedQuery(name = "BannerAd.findByDisplay", query = "SELECT b FROM BannerAd b WHERE b.display = :display")
        })
public class BannerAd implements Serializable
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
    @Column(name = "company")
    private String company;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "banner_pic")
    private String bannerPic;
    @Column(name = "display")
    private Boolean display;

    public BannerAd()
    {
    }

    public BannerAd(Integer id)
    {
        this.id = id;
    }

    public BannerAd(Integer id, String company, String bannerPic)
    {
        this.id = id;
        this.company = company;
        this.bannerPic = bannerPic;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public String getBannerPic()
    {
        return bannerPic;
    }

    public void setBannerPic(String bannerPic)
    {
        this.bannerPic = bannerPic;
    }

    public Boolean getDisplay()
    {
        return display;
    }

    public void setDisplay(Boolean display)
    {
        this.display = display;
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
        if (!(object instanceof BannerAd))
        {
            return false;
        }
        BannerAd other = (BannerAd) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.trouble.entities.BannerAd[ id=" + id + " ]";
    }

}
