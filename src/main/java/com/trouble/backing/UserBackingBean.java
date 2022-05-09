package com.trouble.backing;

import com.trouble.JPAController.UsersJpaController;
import com.trouble.entities.Users;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Backing Bean for a Songs entity.
 * 
 * @author Seaim Khan
 */
@Named("theUsers")
@SessionScoped
public class UserBackingBean implements Serializable{
   
    @Inject
    private UsersJpaController usersJpaController;

    private List<Users> users;
    private List<Users> filteredUsers;
    
    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;
    
    /**
     * Init method that loads a list of all Users entities.
     */
    @PostConstruct
    public void init()
    {
        this.users = usersJpaController.findUsersEntities();
    }

    /**
     * Gets a list of all users.
     * 
     * @return The list of Users entities.
     */
    public List<Users> getUsers() {
        return users;
    }

    /**
     * Sets a list of all users.
     * 
     * @param users The inputted list of Users entities.
     */
    public void setUsers(List<Users> users) {
        this.users = users;
    }

    /**
     * Gets a list of filtered users.
     * 
     * @return The list of filtered Users entities.
     */
    public List<Users> getFilteredUsers() {
        return filteredUsers;
    }
    
    /**
     * Sets a list of filtered users.
     * 
     * @param filteredUsers The list of filtered Users entities.
     */
    public void setFilteredUsers(List<Users> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }
    
    /**
     * A complete method for PrimeFaces autocomplete that finds users that start
     * with a particular input.
     * 
     * @param query The inputted query.
     * @return The list of suggested Users entities based on matching 
     *         input.
     */
    public List<Users> completeUser(String query){
        List<Users> suggested = new ArrayList<>();
        for(Users u : users){
            if(u.getEmail().startsWith(query))
                suggested.add(u);
        }
        return suggested;
    }
    
    /**
     * Gets a user's total purchase value.
     * 
     * @param id The user's id.
     * @return The total purchase value.
     */
    public BigDecimal getTotalPurchases(Integer id){
        Query query = em.createNativeQuery(
            "SELECT SUM(net_value) AS total_purchases FROM invoices " + 
            "WHERE invoices.client_id = ? ")
                .setParameter(1, id);
        if(query.getSingleResult() == null)
            return new BigDecimal("0.00");
        else
            return (BigDecimal)query.getSingleResult();
    }
}
