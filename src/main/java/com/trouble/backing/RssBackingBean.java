package com.trouble.backing;

import com.trouble.JPAController.RssJpaController;
import com.trouble.entities.Rss;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Backing Bean for an Rss entity.
 * 
 * @author Seaim Khan
 */
@Named("theFeeds")
@SessionScoped
public class RssBackingBean implements Serializable{
    
    @Inject
    private RssJpaController rssJpaController;
    
    /**
     * Gets a list of all RSS feeds.
     * 
     * @return The list of all Rss entities.
     */
    public List<Rss> getFeed(){
        return rssJpaController.findRssEntities();
    }
    
    /**
     * Gets the current RSS feed link.
     * 
     * @return The RSS feed link.
     */
    public String getCurrentFeed(){
        Rss currentFeed = rssJpaController.findCurrentFeed();        
        return currentFeed.getFeed();
    }
}
