package beans.action;

import com.trouble.JPAController.BannerAdJpaController;
import com.trouble.entities.BannerAd;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

/**
 * An action bean meant to work for handling Banner Ad operations.
 * 
 * @author Seaim Khan
 */
@Named("banner")
@SessionScoped
public class BannerAdActionBean implements Serializable
{

    @Inject
    private BannerAdJpaController bannerAdController;

    @Inject
    private BannerAd newAd;

    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;

    private Integer id;
    private String company;
    private String bannerPic;
    private Boolean display;
    private BannerAd selectedAd;
    private Boolean disabled = true;
    
    /**
     * No-parameter default constructor
     */
    public BannerAdActionBean(){}
       
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
     * Gets a company name.
     * 
     * @return The company name.
     */
    public String getCompany() {
        return company;
    }

    /**
     * Sets a company name.
     * 
     * @param company The inputted company name. 
     */
    public void setCompany(String company) {
        this.company = company;
    }
    
    /**
     * Gets a banner picture file name.
     * 
     * @return The file name.
     */
    public String getBannerPic() {
        return bannerPic;
    }
    
    /**
     * Sets a banner picture file name.
     * 
     * @param bannerPic The inputted file name. 
     */
    public void setBannerPic(String bannerPic) {
        this.bannerPic = bannerPic;
    }

    /**
     * Get a boolean value representing display on home page.
     * 
     * @return True if displayed on page, false if not.
     */
    public Boolean getDisplay() {
        return display;
    }
    
    /**
     * Get a boolean value representing display on home page.
     * 
     * @param display The inputted boolean value.
     */
    public void setDisplay(Boolean display) {
        this.display = display;
    }
    
    /**
     * Gets a selected ad.
     * 
     * @return The selected BannerAd entity.
     */
    public BannerAd getSelectedAd() {
        return selectedAd;
    }
    
    /**
     * Sets a selected ad.
     * 
     * @param selectedAd The inputted ad.
     */
    public void setSelectedAd(BannerAd selectedAd) {
        this.selectedAd = selectedAd;
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
     * PrimeFaces event handler for a row select on a datatable.
     * 
     * @param event A SelectEvent object.
     */
    public void onRowSelect(SelectEvent event){
        disabled = false;     
    }
    
    /**
     * PrimeFaces handler for uploading a file to the server.
     * 
     * @param event A FileUploadEvent object
     * @throws IOException if an error happens during file upload process.
     */
    public void handleFileUpload(FileUploadEvent event) throws IOException {
        String path = FacesContext.getCurrentInstance().getExternalContext()
            .getRealPath("/");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        String name = fmt.format(new Date())
                + "-" + event.getFile().getFileName();
        String fullPath = "resources/banners/" + name;
        validateLength(fullPath);
        File folder = new File(path + "resources/banners/");
        if(!folder.exists())
            folder.mkdirs();
        
        File file = new File(path + fullPath);

        InputStream is = event.getFile().getInputstream();
        OutputStream out = new FileOutputStream(file);
        byte buf[] = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0)
            out.write(buf, 0, len);
        is.close();
        out.close();
        
        FacesMessage message = new FacesMessage("Successful", name + " is uploaded.");
        setBannerPic(fullPath);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    private void validateLength(String fullPath){
        if(fullPath.length() > 255){
            throw new ValidatorException(new FacesMessage("The filepath is too long!"));
        }
    }
    
    /**
     * Unset parameters after completion of a create.
     */
    private void unsetParams(){
        this.company = null;
        this.bannerPic = null;
        this.display = null;
    }
    
    /**
     * An action method that creates a new BannerAd entity after a 
     * successful button click.
     * 
     * @throws IOException if there is an error redirecting/dispatching to a 
     *                     page.
     */
    public void createAd() throws IOException{
        try{
            newAd.setCompany(company);
            if (getBannerPic() != null)
            {
                newAd.setBannerPic(getBannerPic());
            }
            else
                newAd.setBannerPic("resources/banners/no_banner.jpg");
            newAd.setDisplay(false);
            bannerAdController.create(newAd);
            unsetParams();
        }
        catch (Exception e)
        {
            Logger.getLogger(BannerAdActionBean.class.getName()).log(Level.SEVERE, null, e);
            return;
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("manager.xhtml");
    }
    
    /**
     * An action method that sets the ads to be displayed on the home page.
     */
    public void setAd(){
        try{
            List<BannerAd> bannerAds = bannerAdController.findBannerAdEntities();
            for(BannerAd b : bannerAds){
                if(!b.equals(selectedAd)){
                    if(b.getDisplay() == true){
                        b.setDisplay(false);
                        bannerAdController.edit(b);
                    }
                }
            }
            selectedAd.setDisplay(true);
            bannerAdController.edit(selectedAd);
            selectedAd = new BannerAd();
            disabled = true;
        } 
        catch (Exception ex) {
            Logger.getLogger(BannerAdActionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
