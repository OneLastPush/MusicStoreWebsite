
package beans.action;

import com.trouble.JPAController.ReviewsJpaController;
import com.trouble.entities.Reviews;
import com.trouble.entities.Songs;
import com.trouble.entities.Users;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.event.SelectEvent;

/**
 * An action bean meant to work for handling Review operations.
 * 
 * @author Seaim Khan
 */
@Named("review")
@SessionScoped
public class ReviewAdminActionBean implements Serializable {
    @Inject
    private ReviewsJpaController reviewController;
    
    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;
    
    private Integer id;
    private Songs inventoryId;
    private Users clientId;
    private Integer rating = 0;
    private String reviewText;
    private Boolean approvalStatus;
    
    private Reviews selected;
    private Boolean disabled = true;
    
    /**
     * No-parameter default constructor
     */
    public ReviewAdminActionBean(){}
    
    /**
     * PrimeFaces event handler for a row select on a datatable.
     * 
     * @param event A SelectEvent object.
     */
    public void onRowSelect(SelectEvent event){
        disabled = false;
    }
    
    public void setReview(Reviews review){
        selected = review;
    }
    public Reviews getReview(){
        if(selected == null){
        selected = new Reviews();
        }
        return selected;
    }

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
     * Gets a reviewed song.
     * 
     * @return The Songs entity representing the reviewed song.
     */
    public Songs getInventoryId() {
        return inventoryId;
    }
    
    /**
     * Sets a reviewed song.
     * 
     * @param inventoryId The inputted Songs entity.
     */
    public void setInventoryId(Songs inventoryId) {
        this.inventoryId = inventoryId;
    }

    /**
     * Gets a client.
     * 
     * @return The Users entity representing a client.
     */
    public Users getClientId() {
        return clientId;
    }

    /**
     * Sets a client.
     * 
     * @param clientId The inputted Users entity.
     */
    public void setClientId(Users clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets a rating.
     * 
     * @return The rating. 
     */
    public int getRating() {
        
        
        return rating;
    }

    /**
     * Sets a rating.
     * 
     * @param rating The inputted rating.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Gets a review's text.
     * 
     * @return The review text.
     */
    public String getReviewText() {
        return reviewText;
    }

    /**
     * Sets a review's text.
     * 
     * @param reviewText The inputted review text.
     */
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    /**
     * Gets a boolean value representing approval status.
     * 
     * @return True if approved, false if disapproved.
     */
    public Boolean getApprovalStatus() {
        return approvalStatus;
    }

    /**
     * Sets a boolean value representing approval status.
     * 
     * @param approvalStatus The inputted boolean value.
     */
    public void setApprovalStatus(Boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    /**
     * Gets a selected review.
     * 
     * @return The selected review.
     */
    public Reviews getSelected() {
        return selected;
    }

    /**
     * Sets a selected review.
     * 
     * @param selected The inputted Reviews entity.
     */
    public void setSelected(Reviews selected) {
        this.selected = selected;
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
     * Action method that sets a review's status to approve/disapprove on
     * button click.
     */
    public void setReviewStatus(){
        try{
            if(selected.getApprovalStatus())
                this.setApprovalStatus(false);
            else
                this.setApprovalStatus(true);
            
            selected.setApprovalStatus(approvalStatus);
            reviewController.edit(selected);
            selected = null;
            disabled = true;
        }
        catch(Exception e){
            Logger.getLogger(SurveyActionBean.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
    
}