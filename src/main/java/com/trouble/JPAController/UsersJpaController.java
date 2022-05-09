package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.Users;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Dimitri Spyropoulos
 */
@Named
@SessionScoped
public class UsersJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    /**
     * Default constructor
     */
    public UsersJpaController()
    {
    }

    /**
     * Take a new or detached entity and add it as a new record in the table
     *
     * @param user
     *
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void create(Users users) throws RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            em.persist(users);
            utx.commit();
        }
        catch (Exception ex)
        {
            try
            {

                utx.rollback();
            }
            catch (Exception re)
            {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    /**
     * Take a detached entity and update the matching record in the table
     *
     * @param user
     *
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void edit(Users users) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            users = em.merge(users);
            utx.commit();
        }
        catch (Exception ex)
        {
            try
            {
                utx.rollback();
            }
            catch (Exception re)
            {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                Integer id = users.getId();
                if (findUsers(id) == null)
                {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    /**
     * Delete the record that matched the primary key. Verify that the record
     * exists before deleting it.
     *
     * @param id
     *
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            Users users;
            try
            {
                users = em.getReference(Users.class, id);
                users.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            em.remove(users);
            utx.commit();
        }
        catch (Exception ex)
        {
            try
            {
                utx.rollback();
            }
            catch (Exception re)
            {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    /**
     * Return all the records in the table
     *
     * @return
     */
    public List<Users> findUsersEntities()
    {
        return findUsersEntities(true, -1, -1);
    }

    /**
     * Return some of the records from the table. Useful for paginating.
     *
     * @param maxResults
     * @param firstResult
     *
     * @return
     */
    public List<Users> findUsersEntities(int maxResults, int firstResult)
    {
        return findUsersEntities(false, maxResults, firstResult);
    }

    /**
     * Either find all or find a group of users
     *
     * @param all         True means find all, false means find subset
     * @param maxResults  Number of records to find
     * @param firstResult Record number to start returning records
     *
     * @return
     */
    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult)
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Users.class));
        Query q = em.createQuery(cq);
        if (!all)
        {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    /**
     * Find a record by primary key
     *
     * @param id
     *
     * @return
     */
    public Users findUsers(Integer id)
    {
        return em.find(Users.class, id);
    }

    /**
     * Return the number of records in the table
     *
     * @return
     */
    public int getUsersCount()
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Users> rt = cq.from(Users.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Returns clients in order of total purchases within the defined date range.
     * 
     * @param startDate
     * @param endDate
     * @return List of client names and their purchase total.
     */
    public List<Object[]> getTopClients(Date startDate, Date endDate)
    {
        Query query = em.createNativeQuery(
                "SELECT users.id, CONCAT(fname,' ', lname), SUM(net_value) AS total_purchases FROM users "
                + "INNER JOIN invoices ON invoices.client_id = users.id WHERE invoices.sale_date "
                + "BETWEEN ? AND ? GROUP BY users.id ORDER BY total_purchases DESC")
                .setParameter(1, startDate)
                .setParameter(2, endDate);
        return query.getResultList();
    }

    /**
     * Returns clients that have not made a purchase within the defined date range.
     * 
     * @param startDate
     * @param endDate
     * @return List of client names and their id.
     */
    public List<Object[]> getClientsWithoutPurchase(Date startDate, Date endDate)
    {
        Query query = em.createNativeQuery(
                "SELECT users.id, CONCAT(fname,' ', lname) FROM users LEFT OUTER JOIN invoices "
                + "ON users.id = invoices.client_id WHERE invoices.sale_date "
                + "NOT BETWEEN ? AND ? OR invoices.sale_date IS NULL ORDER BY users.id")
                .setParameter(1, startDate)
                .setParameter(2, endDate);
        return query.getResultList();
    }

    public Users findByEmail(String email) throws NoResultException
    {
        TypedQuery<Users> query
                = em.createNamedQuery("Users.findByEmail", Users.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }
}
