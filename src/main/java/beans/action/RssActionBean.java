package beans.action;

import com.trouble.JPAController.RssJpaController;
import com.trouble.entities.Rss;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.event.SelectEvent;

/**
 * An action bean meant to work for handling RSS feed operations.
 * 
 * @author Seaim Khan
 */
@Named("rss")
@SessionScoped
public class RssActionBean implements Serializable
{

    @Inject
    private RssJpaController rssController;

    @Inject
    private Rss newFeed;

    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;

    private Integer id;
    private String feed;
    private Boolean currentRss;
    private Boolean disabled = true;
    private Rss selected;
    
    /**
     * No-parameter default constructor
     */
    public RssActionBean(){}

    /**
     * Gets the ID of an entity.
     * 
     * @return The entity's ID 
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of an entity.
     * 
     * @param id The inputted entity ID.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets an RSS feed link.
     * 
     * @return The RSS feed link.
     */
    public String getFeed() {
        return feed;
    }

    /**
     * Sets an RSS feed link.
     * 
     * @param feed The inputted RSS feed link.
     */
    public void setFeed(String feed) {
        this.feed = feed;
    }

    /**
     * Gets a Boolean value representing the current RSS feed in use.
     * 
     * @return True if current feed, false if not.
     */
    public Boolean getCurrentRss() {
        return currentRss;
    }

    /**
     * Sets a Boolean value representing the current RSS feed in use.
     * 
     * @param currentRss The inputted Boolean value.
     */
    public void setCurrentRss(Boolean currentRss) {
        this.currentRss = currentRss;
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
     * Gets a selected feed.
     * 
     * @return The selected feed.
     */
    public Rss getSelected() {
        return selected;
    }

    /**
     * Sets a selected feed.
     * 
     * @param selected The inputted Rss entity.
     */
    public void setSelected(Rss selected) {
        this.selected = selected;
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
     * An action method that create a new Rss entity after a 
     * successful button click.
     * 
     * @throws IOException if there is an error redirecting/dispatching to a 
     *                     page.
     */
    public void createFeed() throws IOException{
        try{
            newFeed.setFeed(feed);
            newFeed.setCurrentRss(false);
            rssController.create(newFeed);
            this.currentRss = null;
            this.feed = null;
        }
        catch (Exception e)
        {
            Logger.getLogger(RssActionBean.class.getName()).log(Level.SEVERE, null, e);
            FacesContext.getCurrentInstance().getExternalContext().dispatch("addFeedPage.xhtml");
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("manager.xhtml");
    }
    
    /**
     * Action method that sets a feed to be displayed on button click.
     */
    public void setCurrentFeed(){
        try{
            List<Rss> feeds = rssController.findRssEntities();
            for (Rss r : feeds)
            {
                if (!r.equals(selected))
                {
                    if (r.getCurrentRss() == true)
                    {
                        r.setCurrentRss(false);
                        rssController.edit(r);
                    }
                }
            }
            selected.setCurrentRss(true);
            rssController.edit(selected);
            selected = null;
            disabled = true;
        }
        catch (Exception e)
        {
            Logger.getLogger(RssActionBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
