package com.trouble.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Seaim Khan
 */
@Entity
@Table(name = "invoice_details_songs")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "InvoiceDetailsSongs.findAll", query = "SELECT i FROM InvoiceDetailsSongs i")
            , @NamedQuery(name = "InvoiceDetailsSongs.findById", query = "SELECT i FROM InvoiceDetailsSongs i WHERE i.id = :id")
            , @NamedQuery(name = "InvoiceDetailsSongs.findBySoldPrice", query = "SELECT i FROM InvoiceDetailsSongs i WHERE i.soldPrice = :soldPrice")
        })
public class InvoiceDetailsSongs implements Serializable
{

    @Column(name = "removed")
    private Boolean removed;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "sold_price")
    private BigDecimal soldPrice;
    @JoinColumn(name = "sale_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Invoices saleId;
    @JoinColumn(name = "inventory_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Songs inventoryId;

    public InvoiceDetailsSongs()
    {
    }

    public InvoiceDetailsSongs(Integer id)
    {
        this.id = id;
    }

    public InvoiceDetailsSongs(Integer id, BigDecimal soldPrice)
    {
        this.id = id;
        this.soldPrice = soldPrice;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public BigDecimal getSoldPrice()
    {
        return soldPrice;
    }

    public void setSoldPrice(BigDecimal soldPrice)
    {
        this.soldPrice = soldPrice;
    }

    public Invoices getSaleId()
    {
        return saleId;
    }

    public void setSaleId(Invoices saleId)
    {
        this.saleId = saleId;
    }

    public Songs getInventoryId()
    {
        return inventoryId;
    }

    public void setInventoryId(Songs inventoryId)
    {
        this.inventoryId = inventoryId;
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
        if (!(object instanceof InvoiceDetailsSongs))
        {
            return false;
        }
        InvoiceDetailsSongs other = (InvoiceDetailsSongs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.trouble.entities.InvoiceDetailsSongs[ id=" + id + " ]";
    }

    public Boolean getRemoved()
    {
        return removed;
    }

    public void setRemoved(Boolean removed)
    {
        this.removed = removed;
    }

   

    
    
}
