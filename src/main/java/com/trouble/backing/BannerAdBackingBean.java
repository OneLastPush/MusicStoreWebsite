package com.trouble.backing;

import com.trouble.JPAController.BannerAdJpaController;
import com.trouble.entities.BannerAd;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Backing Bean for a BannerAd entity.
 * 
 * @author Seaim Khan
 */
@Named("theAds")
@SessionScoped
public class BannerAdBackingBean implements Serializable{
    
    @Inject
    private BannerAdJpaController bannerAdJpaController;
    
    private BannerAd currentAd;
    
    /**
     * Gets a list of all banner ads.
     * 
     * @return The list of all BannerAd entities.
     */
    public List<BannerAd> getAds(){
        return bannerAdJpaController.findBannerAdEntities();
    }
    
    public BannerAd getCurrentAd(){
        return currentAd = bannerAdJpaController.findCurrentAd();
    }
}
