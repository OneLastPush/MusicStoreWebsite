package com.trouble.backingbean;

import com.trouble.entities.Users;
import com.trouble.JPAController.UsersJpaController;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Frank
 */
@Named("userBacking")
@RequestScoped
public class userBackingBean implements Serializable
{

    @Inject
    private UsersJpaController usersJpaController;

    private Users user;

    /**
     * Get the entity if not instantiated, it will
     *
     * @return user -entity
     */
    public Users getUser()
    {
        if (user == null)
        {
            user = new Users();
        }
        return user;
    }

    /**
     * Get list of user
     *
     * @return
     */
    public List<Users> getAllUsers()
    {

        return usersJpaController.findUsersEntities();
    }
}
