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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "users")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")
            , @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id")
            , @NamedQuery(name = "Users.findByTitle", query = "SELECT u FROM Users u WHERE u.title = :title")
            , @NamedQuery(name = "Users.findByPword", query = "SELECT u FROM Users u WHERE u.pword = :pword")
            , @NamedQuery(name = "Users.findByFname", query = "SELECT u FROM Users u WHERE u.fname = :fname")
            , @NamedQuery(name = "Users.findByLname", query = "SELECT u FROM Users u WHERE u.lname = :lname")
            , @NamedQuery(name = "Users.findByCompanyName", query = "SELECT u FROM Users u WHERE u.companyName = :companyName")
            , @NamedQuery(name = "Users.findByAddress1", query = "SELECT u FROM Users u WHERE u.address1 = :address1")
            , @NamedQuery(name = "Users.findByAddress2", query = "SELECT u FROM Users u WHERE u.address2 = :address2")
            , @NamedQuery(name = "Users.findByCity", query = "SELECT u FROM Users u WHERE u.city = :city")
            , @NamedQuery(name = "Users.findByProvince", query = "SELECT u FROM Users u WHERE u.province = :province")
            , @NamedQuery(name = "Users.findByCountry", query = "SELECT u FROM Users u WHERE u.country = :country")
            , @NamedQuery(name = "Users.findByPostalCode", query = "SELECT u FROM Users u WHERE u.postalCode = :postalCode")
            , @NamedQuery(name = "Users.findByHomeNumber", query = "SELECT u FROM Users u WHERE u.homeNumber = :homeNumber")
            , @NamedQuery(name = "Users.findByCellNumber", query = "SELECT u FROM Users u WHERE u.cellNumber = :cellNumber")
            , @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email")
            , @NamedQuery(name = "Users.findByAdminPrivilege", query = "SELECT u FROM Users u WHERE u.adminPrivilege = :adminPrivilege")
        })
public class Users implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 10)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pword")
    private String pword;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "fname")
    private String fname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "lname")
    private String lname;
    @Size(max = 255)
    @Column(name = "company_name")
    private String companyName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "address_1")
    private String address1;
    @Size(max = 100)
    @Column(name = "address_2")
    private String address2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "city")
    private String city;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "province")
    private String province;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "country")
    private String country;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "postal_code")
    private String postalCode;
    @Size(max = 15)
    @Column(name = "home_number")
    private String homeNumber;
    @Size(max = 15)
    @Column(name = "cell_number")
    private String cellNumber;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    @Column(name = "admin_privilege")
    private Boolean adminPrivilege;
    @JoinColumn(name = "last_genre_searched", referencedColumnName = "id")
    @ManyToOne
    private Genres lastGenreSearched;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientId")
    private List<Invoices> invoicesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clientId")
    private List<Reviews> reviewsList;

    public Users()
    {
    }

    public Users(Integer id)
    {
        this.id = id;
    }

    public Users(Integer id, String pword, String fname, String lname, String address1, String city, String province, String country, String postalCode, String email)
    {
        this.id = id;
        this.pword = pword;
        this.fname = fname;
        this.lname = lname;
        this.address1 = address1;
        this.city = city;
        this.province = province;
        this.country = country;
        this.postalCode = postalCode;
        this.email = email;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getPword()
    {
        return pword;
    }

    public void setPword(String pword)
    {
        this.pword = pword;
    }

    public String getFname()
    {
        return fname;
    }

    public void setFname(String fname)
    {
        this.fname = fname;
    }

    public String getLname()
    {
        return lname;
    }

    public void setLname(String lname)
    {
        this.lname = lname;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getAddress1()
    {
        return address1;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public String getHomeNumber()
    {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber)
    {
        this.homeNumber = homeNumber;
    }

    public String getCellNumber()
    {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber)
    {
        this.cellNumber = cellNumber;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Boolean getAdminPrivilege()
    {
        return adminPrivilege;
    }

    public void setAdminPrivilege(Boolean adminPrivilege)
    {
        this.adminPrivilege = adminPrivilege;
    }

    public Genres getLastGenreSearched()
    {
        return lastGenreSearched;
    }

    public void setLastGenreSearched(Genres lastGenreSearched)
    {
        this.lastGenreSearched = lastGenreSearched;
    }

    @XmlTransient
    public List<Invoices> getInvoicesList()
    {
        return invoicesList;
    }

    public void setInvoicesList(List<Invoices> invoicesList)
    {
        this.invoicesList = invoicesList;
    }

    @XmlTransient
    public List<Reviews> getReviewsList()
    {
        return reviewsList;
    }

    public void setReviewsList(List<Reviews> reviewsList)
    {
        this.reviewsList = reviewsList;
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
        if (!(object instanceof Users))
        {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.trouble.entities.Users[ id=" + id + " ]";
    }

}
