package beans.action;

import com.trouble.entities.Users;
import com.trouble.entities.Users_;
import java.io.Serializable;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Max Page-Slowik
 */
@Named(value = "login")
@SessionScoped
public class LoginActionBean implements Serializable
{

    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;

    private Users currUser;
    private String email;
    private String password;
    private Boolean loggedIn;

    public LoginActionBean() {

    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn)
    {
        this.loggedIn = loggedIn;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Users getCurrUser()
    {
        return currUser;
    }

    public void setCurrUser(Users currUser)
    {
        this.currUser = currUser;
    }

    private Map<String, Object> readCookie() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> cookieMap = context.getExternalContext().getRequestCookieMap();
        return cookieMap;
    }

    /**
     * Logout button that changes logs out the currUser by removing the cookie
     * and changing the boolean loggedIn to false
     *
     * @return the index page
     */
    public String logout()
    {
        loggedIn = false;
        Map<String, Object> cookieMap = readCookie();
        Cookie loginVal = ((Cookie) cookieMap.get("isLoggedIn"));
        Cookie emailVal = ((Cookie) cookieMap.get("email"));
        loginVal.setMaxAge(0);
        emailVal.setMaxAge(0);
        HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        resp.addCookie(loginVal);
        resp.addCookie(emailVal);

        FacesContext.getCurrentInstance()
                .getExternalContext().invalidateSession();
        return "/index?faces-redirect=true";
    }

    /**
     * Logout handler for Manager-side. Logs out the admin and redirects to the
     * index page.
     * 
     * @return 
     
    public String adminLogout(){
        logout();
        return "/index?faces-redirect=true";
    }*/
    
    private void writeCookie()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().addResponseCookie("email", currUser.getEmail(), null);
        context.getExternalContext().addResponseCookie("isLoggedIn", loggedIn.toString(), null);
    }

    /**
     * If a valid login will store the currUser email in a cookie as well as
     * return the currUser to the index, if it is unsuccessful it will return to
     * the login page with an error
     *
     * @return URL path login page if invalid, index if valid
     */
    public String login()
    {
        //loggedIn = false;
        String value = validateLogin();

        //return sucess if valid with db
        //return admin if succeful and valid
        //return failure if failed
        if (value.equals("success"))
        {
            loggedIn = true;
            System.out.println("Succesful login");
            writeCookie();
            return "index?faces-redirect=true";
        }
        else if (value.equals("fail"))
        {
            String message = "Fail to login, Try again";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));

            return "";
        } else if (value.equals("admin")) {
            loggedIn = true;
            System.out.println("admin login");
            writeCookie();
            return "secured/manager?faces-redirect=true";
        }
        return "";

    }

    /**
     * Get the result if there is a currUser in the database containing the
     * credentials email and hashed password, checking using BCrypts check for
     * check on hash and salt.
     *
     * @return fail if there is no currUser or if the password entered does not
     *         match, success if the password matches and if the currUser
     *         exists, admin if the currUser has admin priveleges;
     */
    private String validateLogin()
    {
        // Object oriented criteria builder
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> user = cq.from(Users.class);
        cq.where(cb.equal(user.get(Users_.email), email));
        cq.select(user);
        TypedQuery<Users> query = em.createQuery(cq);

        Users validUser = null;
        // Execute the query
        try
        {
            validUser = query.getResultList().get(0);
            System.out.println(validUser);
           // validUser = query.getSingleResult();

            if (validUser == null)
            {
                return "fail";
            } else if (!BCrypt.checkpw(password, validUser.getPword())) {
                return "fail";

            } else if (validUser.getAdminPrivilege()) {
                currUser = validUser;
                return "admin";
            } else {
                currUser = validUser;
                return "success";
            }
        }
        catch (NoResultException nre)
        {
            return "fail";
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            return "fail";
        }

    }

    @Override
    public String toString()
    {
        return "LoginActionBean{" + "email=" + email + ", password=" + password + ", loggedIn=" + loggedIn + '}';
    }
    public void isLogIn(){
        if(!loggedIn){
            FacesMessage message = new FacesMessage("Only registered user can review! Please log in!");
            FacesContext.getCurrentInstance().addMessage("reviewForm:submitReviewBtn", message);
        }
    }
}
