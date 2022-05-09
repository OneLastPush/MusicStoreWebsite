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
@Table(name = "labels")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Labels.findAll", query = "SELECT l FROM Labels l")
            , @NamedQuery(name = "Labels.findById", query = "SELECT l FROM Labels l WHERE l.id = :id")
            , @NamedQuery(name = "Labels.findByLabelName", query = "SELECT l FROM Labels l WHERE l.labelName = :labelName")
        })
public class Labels implements Serializable
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
    @Column(name = "label_name")
    private String labelName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recordingLabel")
    private List<Albums> albumsList;

    public Labels()
    {
    }

    public Labels(Integer id)
    {
        this.id = id;
    }

    public Labels(Integer id, String labelName)
    {
        this.id = id;
        this.labelName = labelName;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getLabelName()
    {
        return labelName;
    }

    public void setLabelName(String labelName)
    {
        this.labelName = labelName;
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
        if (!(object instanceof Labels))
        {
            return false;
        }
        Labels other = (Labels) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.trouble.entities.Labels[ id=" + id + " ]";
    }

}
