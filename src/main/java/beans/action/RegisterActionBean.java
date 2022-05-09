package beans.action;

import com.trouble.JPAController.UsersJpaController;
import com.trouble.entities.Genres;
import com.trouble.entities.Users;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.mindrot.jbcrypt.BCrypt;
import org.primefaces.event.SelectEvent;
 
/**
 *  An action bean meant to work for handling Registration/Create User 
 *  operations.
 * 
 * @author Max Page-Slowik and Seaim Khan
 */
@RequestScoped
@Named("register")
public class RegisterActionBean implements Serializable
{

    @Inject
    private UsersJpaController userController;
    
    
    private Users user = new Users();
    
    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;

    private Users current;
    private Boolean disabled = true;

    private String title;
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String postalCode;
    private String city;
    private String province;
    private String country;
    private String address1;
    private String address2;
    private String cell;
    private String home;
    private String companyName;
    private Genres lastGenreSearched;

    /**
     * No-parameter default constructor
     */
    public RegisterActionBean() {
        
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    
    
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
       
            this.email = email;
        
    }

    /**
     * Gets a password.
     * 
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets a password.
     * 
     * @param password The inputted password.
     */
    public void setPassword(String password) {
       
            this.password = password;
        
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

    public Genres getLastGenreSearched()
    {
        return lastGenreSearched;
    }

    public void setLastGenreSearched(Genres lastGenreSearched)
    {
        this.lastGenreSearched = lastGenreSearched;
    }

    public Users getCurrent()
    {
        return current;
    }

    public void setCurrent(Users current)
    {
        this.current = current;
    }

    public Boolean getDisabled()
    {
        return disabled;
    }

    public void setDisabled(Boolean disabled)
    {
        this.disabled = disabled;
    }

    public void onRowSelect(SelectEvent event)
    {
        disabled = false;
    }

    /**
     * Setting the required user parameters up from the registration page
     */
    public void setUserParams(){
        user.setAddress1(address1);
        if (address2 != null) {
            user.setAddress2(address2);
        }
        user.setCellNumber(cell);
        user.setCity(city);
        user.setProvince(province);
        user.setTitle(title);
        user.setCountry(country);
        user.setPostalCode(postalCode);
        user.setEmail(email);
        user.setFname(fname);
        user.setHomeNumber(home);
        user.setLname(lname);
        user.setAdminPrivilege(false);
    }

    /**
     * Hash the password using the BCrypt has and salt
     * Creates a user in the database after setting the parameters required
     * in order to properly create a user, this create is using the jpa controller
     * injection and the user injected into the class;
     * @return the index page if successful, Registration page again if unsuccessful
     * with a message
     * 
     * 
     * 
     */
    public String registerUser(){
   
         try {
             setUserParams();
             password = BCrypt.hashpw(password, BCrypt.gensalt());
             user.setPword(password);
             System.out.print("hello: " +user.toString());
             userController.create(user);

         } catch (Exception ex) {
             ex.printStackTrace();
             System.out.print(ex);
             Logger.getLogger(RegisterActionBean.class.getName()).log(Level.SEVERE, null, ex);
            return "registerPage";
         }
         
        
       return"index";
    }

    /**
     * Very similar to registerUser method, except for creating a user for the
     * manager.
     *
     * @throws java.io.IOException if forward or redirect fail.
     */
    public void createUser() throws IOException{
        try {
             setUserParams();
             password = BCrypt.hashpw(password, BCrypt.gensalt());
             user.setPword(password);
             userController.create(user);

         } catch (Exception ex) {
             Logger.getLogger(RegisterActionBean.class.getName()).log(Level.SEVERE, null, ex);
             return;
         }

        FacesContext.getCurrentInstance().getExternalContext().redirect("manager.xhtml");
    }
}
