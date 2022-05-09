package com.trouble.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Seaim Khan
 */
@Entity
@Table(name = "invoices")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Invoices.findAll", query = "SELECT i FROM Invoices i")
            , @NamedQuery(name = "Invoices.findById", query = "SELECT i FROM Invoices i WHERE i.id = :id")
            , @NamedQuery(name = "Invoices.findBySaleDate", query = "SELECT i FROM Invoices i WHERE i.saleDate = :saleDate")
            , @NamedQuery(name = "Invoices.findByNetValue", query = "SELECT i FROM Invoices i WHERE i.netValue = :netValue")
            , @NamedQuery(name = "Invoices.findByPst", query = "SELECT i FROM Invoices i WHERE i.pst = :pst")
            , @NamedQuery(name = "Invoices.findByGst", query = "SELECT i FROM Invoices i WHERE i.gst = :gst")
            , @NamedQuery(name = "Invoices.findByHst", query = "SELECT i FROM Invoices i WHERE i.hst = :hst")
            , @NamedQuery(name = "Invoices.findByGrossValue", query = "SELECT i FROM Invoices i WHERE i.grossValue = :grossValue")
            , @NamedQuery(name = "Invoices.findByRemoved", query = "SELECT i FROM Invoices i WHERE i.removed = :removed")
        })
public class Invoices implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "sale_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "net_value")
    private BigDecimal netValue;
    @Column(name = "pst")
    private BigDecimal pst;
    @Column(name = "gst")
    private BigDecimal gst;
    @Column(name = "hst")
    private BigDecimal hst;
    @Basic(optional = false)
    @NotNull
    @Column(name = "gross_value")
    private BigDecimal grossValue;
    @Column(name = "removed")
    private Boolean removed;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "saleId")
    private List<InvoiceDetailsAlbum> invoiceDetailsAlbumList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "saleId")
    private List<InvoiceDetailsSongs> invoiceDetailsSongsList;
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users clientId;

    public Invoices()
    {
    }

    public Invoices(Integer id)
    {
        this.id = id;
    }

    public Invoices(Integer id, BigDecimal netValue, BigDecimal grossValue)
    {
        this.id = id;
        this.netValue = netValue;
        this.grossValue = grossValue;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Date getSaleDate()
    {
        return saleDate;
    }

    public void setSaleDate(Date saleDate)
    {
        this.saleDate = saleDate;
    }

    public BigDecimal getNetValue()
    {
        return netValue;
    }

    public void setNetValue(BigDecimal netValue)
    {
        this.netValue = netValue;
    }

    public BigDecimal getPst()
    {
        return pst;
    }

    public void setPst(BigDecimal pst)
    {
        this.pst = pst;
    }

    public BigDecimal getGst()
    {
        return gst;
    }

    public void setGst(BigDecimal gst)
    {
        this.gst = gst;
    }

    public BigDecimal getHst()
    {
        return hst;
    }

    public void setHst(BigDecimal hst)
    {
        this.hst = hst;
    }

    public BigDecimal getGrossValue()
    {
        return grossValue;
    }

    public void setGrossValue(BigDecimal grossValue)
    {
        this.grossValue = grossValue;
    }

    public Boolean getRemoved()
    {
        return removed;
    }

    public void setRemoved(Boolean removed)
    {
        this.removed = removed;
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

    @XmlTransient
    public List<InvoiceDetailsSongs> getInvoiceDetailsSongsList()
    {
        return invoiceDetailsSongsList;
    }

    public void setInvoiceDetailsSongsList(List<InvoiceDetailsSongs> invoiceDetailsSongsList)
    {
        this.invoiceDetailsSongsList = invoiceDetailsSongsList;
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
        if (!(object instanceof Invoices))
        {
            return false;
        }
        Invoices other = (Invoices) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.trouble.entities.Invoices[ id=" + id + " ]";
    }

}
