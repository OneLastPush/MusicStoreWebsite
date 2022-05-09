
package beans.action;

import com.trouble.JPAController.UsersJpaController;
import com.trouble.entities.Genres;
import com.trouble.entities.Users;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.event.SelectEvent;

/**
 * A separate action bean meant for editing a user.
 * 
 * @author Seaim Khan
 */
@SessionScoped
@Named("editUser")
public class EditUserActionBean implements Serializable{
    @Inject
    private UsersJpaController userController;
    
    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;
    
    private Users current; 
    private Boolean disabled = true;
    
    private String title;
    private String fname;
    private String lname;
    private String email;
    private String postalCode;
    private String city;
    private String province;
    private String country;
    private String address1;
    private String address2;
    private String cell;
    private String home;
    private Genres lastGenreSearched;
    private String companyName;
    private Boolean adminPrivilege;
    
    /**
     * No-parameter default constructor
     */
    public EditUserActionBean(){}
    
    /**
     * Gets a title.
     * 
     * @return The title.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets a title.
     * 
     * @param title The inputted title.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Gets a first name.
     * 
     * @return The first name.
     */
    public String getFname() {
        return fname;
    }
    
    /**
     * Sets a first name.
     * 
     * @param fname The inputted first name. 
     */
    public void setFname(String fname) {
        if(fname == null){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing firstname","Missing firstname"));
        }
        else
            this.fname = fname;         
    }
    
    /**
     * Gets a last name.
     * 
     * @return The last name. 
     */
    public String getLname() {
        return lname;
    }
    
    /**
     * Sets a last name.
     * 
     * @param lname The inputted last name. 
     */
    public void setLname(String lname) {
        if(lname == null){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing lastname","Missing lastname"));
        }
        else
            this.lname = lname;
    }

    /**
     * Gets an email.
     * 
     * @return The email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets an email.
     * 
     * @param email The inputted email. 
     */
    public void setEmail(String email) {
       if(email == null){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing email","Missing email"));
        }
        else
            this.email = email;
    }
    
    /**
     * Gets a postal code.
     * 
     * @return The postal code. 
     */
    public String getPostalCode() {
        return postalCode;
    }
    
    /**
     * Sets a postal code.
     * 
     * @param postalCode The inputted postal code. 
     */
    public void setPostalCode(String postalCode) {
        if(postalCode == null){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing postalCode","Missing postalCode"));
        }
        else
            this.postalCode = postalCode;
    }

    /**
     * Gets a city's name.
     * 
     * @return The city name. 
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets a city's name.
     * 
     * @param city The inputted city name.
     */
    public void setCity(String city) {
        if(city == null){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing city","Missing city"));
        }
        else
            this.city = city;
    }

    /**
     * Gets a province's name.
     * 
     * @return The province name. 
     */
    public String getProvince() {
        return province;
    }

    /**
     * Sets a province's name.
     * 
     * @param province The inputted province name. 
     */
    public void setProvince(String province) {
        this.province = province;
    }
    
    /**
     * Gets a country's name.
     * 
     * @return The country name.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets a country's name.
     * 
     * @param country The inputted country name.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets an address line 1.
     * 
     * @return The address line 1. 
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * Sets an address line 1.
     * 
     * @param address1 The inputted address line 1.
     */
    public void setAddress1(String address1) {
         if(address1 == null){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing address1","Missing address1"));
        }
        else
            this.address1 = address1;
    }

    /**
     * Gets an address line 2.
     * 
     * @return The address line 2. 
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * Sets an address line 2.
     * 
     * @param address2 The inputted address line 2.
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    
    /**
     * Gets a cell number.
     * 
     * @return The cell number.
     */
    public String getCell() {
        return cell;
    }
    
    /**
     * Sets a cell number.
     * 
     * @param cell The inputted cell number.
     */
    public void setCell(String cell) {
        this.cell = cell;
    }

    /**
     * Gets a home number.
     * 
     * @return The home number. 
     */
    public String getHome() {
        return home;
    }

    /**
     * Sets a home number.
     * 
     * @param home The inputted home number.
     */
    public void setHome(String home) {
        this.home = home;
    }

    /**
     * Gets a company's name.
     * 
     * @return The company name.
     */
    public String getCompanyName() {
        return companyName;
    }
    
    /**
     * Sets a company's name.
     * 
     * @param companyName The inputted company name.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    /**
     * Gets the last genre searched for a user.
     * 
     * @return The Genres entity representing the last genre searched.
     */
    public Genres getLastGenreSearched() {
        return lastGenreSearched;
    }
    
    /**
     * Sets the last genre searched for a user.
     * 
     * @param lastGenreSearched The inputted Genres entity.
     */
    public void setLastGenreSearched(Genres lastGenreSearched) {
        this.lastGenreSearched = lastGenreSearched;
    }
    
    /**
     * Gets the current user.
     * 
     * @return The Users entity representing the current user.
     */
    public Users getCurrent() {
        return current;
    }
    
    /**
     * Sets the current user.
     * 
     * @param current The inputted Users entity.
     */
    public void setCurrent(Users current) {
        this.current = current;
    }

    /**
     * Gets a boolean representing a disabled value.
     * 
     * @return True if disabled, false if not.
     */
    public Boolean getDisabled() {
        return disabled;
    }

    /**
     * Sets a disabled value.
     * 
     * @param disabled The inputted boolean value.
     */
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Gets a boolean value representing administrator privilege.
     * 
     * @return True if administrator, false if regular user.
     */
    public Boolean getAdminPrivilege() {
        return adminPrivilege;
    }

    /**
     * Sets a boolean value representing administrator privilege.
     * 
     * @param adminPrivilege The inputted boolean value.
     */
    public void setAdminPrivilege(Boolean adminPrivilege) {
        this.adminPrivilege = adminPrivilege;
    }
    
    /**
     * PrimeFaces event handler for a row select on a datatable.
     * 
     * @param event A SelectEvent object.
     */
    public void onRowSelect(SelectEvent event){
        disabled = false;
    }
    
    /**
     * Set default values for editing a user.
     */
    public void setEditUser(){
        this.setEmail(current.getEmail());
        this.setTitle(current.getTitle());
        this.setFname(current.getFname());
        this.setLname(current.getLname());
        this.setAddress1(current.getAddress1());
        if(current.getAddress2() != null && !current.getAddress2().isEmpty())
            this.setAddress2(current.getAddress2());
        this.setCity(current.getCity());
        this.setProvince(current.getProvince());
        this.setCountry(current.getCountry());
        this.setPostalCode(current.getPostalCode());
        if(current.getCompanyName() != null && !current.getCompanyName().isEmpty())
            this.setCompanyName(current.getCompanyName());
        if(current.getHomeNumber() != null && !current.getHomeNumber().isEmpty())
            this.setHome(current.getHomeNumber());
        if(current.getCellNumber() != null && !current.getCellNumber().isEmpty())
            this.setCell(current.getCellNumber());
        if(current.getLastGenreSearched() != null)
            this.setLastGenreSearched(current.getLastGenreSearched());
    }
    
    /**
     * Sets the user parameters for editing of a Users entity.
     */
    private void setEditedParams(){
        current.setEmail(email);
        current.setTitle(title);
        current.setFname(fname);
        current.setLname(lname);
        current.setAddress1(address1);
        if(address2 != null && !address2.isEmpty())
            current.setAddress2(address2);
        current.setCity(city);
        current.setProvince(province);
        current.setCountry(country);
        current.setPostalCode(postalCode);
        if(companyName != null && !companyName.isEmpty())
            current.setCompanyName(companyName);
        current.setHomeNumber(home);
        current.setCellNumber(cell);
        if(lastGenreSearched != null)
            current.setLastGenreSearched(lastGenreSearched);
        current.setAdminPrivilege(adminPrivilege);
    }
    
    /**
     * Unset parameters after completion of edit.
     */
    private void unsetParams(){
        this.email = null;
        this.title = null;
        this.fname = null;
        this.lname = null;
        this.address1 = null;
        this.address2 = null;
        this.city = null;
        this.province = null;
        this.country = null;
        this.postalCode = null;
        this.companyName = null;
        this.home = null;
        this.cell = null;
        this.lastGenreSearched = null;
        this.adminPrivilege = null;
        this.current = new Users();
    }
    
    /**
     * An action method that edit a Users entity after a successful button 
     * click.
     * 
     * @throws IOException if there is an error redirecting/dispatching to a 
     *                     page.
     */
    public void editUser() throws IOException{
        try{
            setEditedParams();
            userController.edit(current);
            unsetParams();
            disabled = true;
        }
        catch(Exception e){
            Logger.getLogger(EditUserActionBean.class.getName()).log(Level.SEVERE, null, e);
            return;
        }
        
        FacesContext.getCurrentInstance().getExternalContext().redirect("manager.xhtml");
    }
}
