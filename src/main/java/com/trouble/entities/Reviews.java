package com.trouble.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "reviews")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Reviews.findAll", query = "SELECT r FROM Reviews r")
            , @NamedQuery(name = "Reviews.findById", query = "SELECT r FROM Reviews r WHERE r.id = :id")
            , @NamedQuery(name = "Reviews.findByRating", query = "SELECT r FROM Reviews r WHERE r.rating = :rating")
            , @NamedQuery(name = "Reviews.findByReviewText", query = "SELECT r FROM Reviews r WHERE r.reviewText = :reviewText")
            , @NamedQuery(name = "Reviews.findByApprovalStatus", query = "SELECT r FROM Reviews r WHERE r.approvalStatus = :approvalStatus")
        })
public class Reviews implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rating")
    private int rating;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "review_text")
    private String reviewText;
    @Column(name = "approval_status")
    private Boolean approvalStatus;
    @JoinColumn(name = "inventory_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Songs inventoryId;
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users clientId;

    public Reviews()
    {
    }

    public Reviews(Integer id)
    {
        this.id = id;
    }

    public Reviews(Integer id, int rating, String reviewText)
    {
        this.id = id;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public int getRating()
    {
        return rating;
    }

    public void setRating(int rating)
    {
        this.rating = rating;
    }

    public String getReviewText()
    {
        return reviewText;
    }

    public void setReviewText(String reviewText)
    {
        this.reviewText = reviewText;
    }

    public Boolean getApprovalStatus()
    {
        return approvalStatus;
    }

    public void setApprovalStatus(Boolean approvalStatus)
    {
        this.approvalStatus = approvalStatus;
    }

    public Songs getInventoryId()
    {
        return inventoryId;
    }

    public void setInventoryId(Songs inventoryId)
    {
        this.inventoryId = inventoryId;
    }

    public Users getClientId()
    {
        return clientId;
    }

    public void setClientId(Users clientId)
    {
        this.clientId = clientId;
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
        if (!(object instanceof Reviews))
        {
            return false;
        }
        Reviews other = (Reviews) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.trouble.entities.Reviews[ id=" + id + " ]";
    }

}
