
package beans.action;

import com.trouble.JPAController.InvoiceDetailsSongsJpaController;
import com.trouble.JPAController.InvoicesJpaController;
import com.trouble.entities.InvoiceDetailsSongs;
import com.trouble.entities.Invoices;
import com.trouble.entities.Users;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;

/**
 * An action bean meant to work for handling Invoice operations.
 * 
 * @author Seaim Khan
 */
@SessionScoped
@Named("invoice")
public class InvoiceActionBean implements Serializable{
    @Inject
    private InvoicesJpaController invoiceController;
    
    @Inject
    private InvoiceDetailsSongsJpaController invoiceSongController;
    
    private Invoices currentInvoice;
    private Boolean disabled = true;
    
    private Integer id;
    private Date saleDate;
    private BigDecimal netValue;
    private BigDecimal pst;
    private BigDecimal gst;
    private BigDecimal hst;
    private BigDecimal grossValue;
    private Boolean removed;
    private List<InvoiceDetailsSongs> invoiceDetailsSongsList;
    private List<InvoiceDetailsSongs> removalList;
    private Users clientId;
    private final Date currentDate = new Date();
    private DualListModel<InvoiceDetailsSongs> songs;

    /**
     * No-parameter default constructor
     */
    public InvoiceActionBean(){}

    /**
     * Gets the current invoice.
     * 
     * @return The Invoices entity representation of the current invoice.
     */
    public Invoices getCurrentInvoice() {
        return currentInvoice;
    }

    /**
     * Sets the current invoice.
     * 
     * @param currentInvoice An inputted Invoices entity.
     */
    public void setCurrentInvoice(Invoices currentInvoice) {
        this.currentInvoice = currentInvoice;
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
     * Gets a sale date.
     * 
     * @return The sale date.
     */
    public Date getSaleDate() {
        return saleDate;
    }
    
    /**
     * Sets a sale date.
     * 
     * @param saleDate The inputted sale date.
     */
    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }
    
    /**
     * Gets a net value.
     * 
     * @return The net value.
     */
    public BigDecimal getNetValue() {
        return netValue;
    }
    
    /**
     * Sets a net value.
     * 
     * @param netValue The inputted net value.
     */
    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue;
    }
    
    /**
     * Gets a PST tax amount.
     * 
     * @return The PST tax amount.
     */
    public BigDecimal getPst() {
        return pst;
    }
    
    /**
     * Sets a PST tax amount.
     * 
     * @param pst The inputted PST tax amount.
     */
    public void setPst(BigDecimal pst) {
        this.pst = pst;
    }
    
    /**
     * Gets a GST tax amount.
     * 
     * @return The GST tax amount.
     */
    public BigDecimal getGst() {
        return gst;
    }
    
    /**
     * Sets a GST tax amount.
     * 
     * @param gst The inputted GST tax amount.
     */
    public void setGst(BigDecimal gst) {
        this.gst = gst;
    }
    
    /**
     * Gets an HST tax amount.
     * 
     * @return The HST tax amount.
     */
    public BigDecimal getHst() {
        return hst;
    }
    
    /**
     * Sets an HST tax amount.
     * 
     * @param hst The inputted HST tax amount.
     */
    public void setHst(BigDecimal hst) {
        this.hst = hst;
    }
    
    /**
     * Gets a gross value.
     * 
     * @return The gross value.
     */
    public BigDecimal getGrossValue() {
        return grossValue;
    }
    
    /**
     * Sets a gross value.
     * 
     * @param grossValue The inputted gross value. 
     */
    public void setGrossValue(BigDecimal grossValue) {
        this.grossValue = grossValue;
    }
    
    /**
     * Gets a boolean value representing a removed invoice.
     * 
     * @return True if removed, false if not.
     */
    public Boolean getRemoved() {
        return removed;
    }
    
    /**
     * Sets a boolean value representing a removed invoice.
     * 
     * @param removed The inputted boolean value.
     */
    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }
    
    /**
     * Gets a list of InvoiceDetailsSongs entities.
     * 
     * @return The list of InvoiceDetailsSongs entities.
     */
    public List<InvoiceDetailsSongs> getInvoiceDetailsSongsList() {
        return invoiceDetailsSongsList;
    }
    
    /**
     * Sets a list of InvoiceDetailsSongs entities.
     * 
     * @param invoiceDetailsSongsList The inputted list of InvoiceDetailsSongs
     *                                entities.
     */
    public void setInvoiceDetailsSongsList(List<InvoiceDetailsSongs> invoiceDetailsSongsList) {
        this.invoiceDetailsSongsList = invoiceDetailsSongsList;
    }
    
    /**
     * Gets a list of InvoiceDetailsSongs entities to remove.
     * 
     * @return The list of InvoiceDetailsSongs entities to remove.
     */
    public List<InvoiceDetailsSongs> getRemovalList() {
        return removalList;
    }
    
    /**
     * Sets a list of InvoiceDetailsSongs entities to remove.
     * 
     * @param removalList The inputted list of InvoiceDetailsSongs entities to 
     *                    remove.
     */
    public void setRemovalList(List<InvoiceDetailsSongs> removalList) {
        this.removalList = removalList;
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
     * Gets the current date.
     * 
     * @return The current date.
     */
    public Date getCurrentDate() {
        return currentDate;
    }

    /**
     * Gets a dual list of InvoiceDetailsSongs entities.
     * 
     * @return The dual list of InvoiceDetailsSongs entities.
     */
    public DualListModel<InvoiceDetailsSongs> getSongs() {
        return songs;
    }

    /**
     * Sets a dual list of InvoiceDetailsSongs entities.
     * 
     * @param songs The inputted dual list of InvoiceDetailsSongs entities.
     */
    public void setSongs(DualListModel<InvoiceDetailsSongs> songs) {
        this.songs = songs;
    }
    
    /**
     * Set default values for editing an invoice.
     */
    public void setEditInvoice(){
        this.setClientId(currentInvoice.getClientId());
        this.setSaleDate(currentInvoice.getSaleDate());
        this.setGrossValue(currentInvoice.getGrossValue());
        this.setPst(currentInvoice.getPst());
        this.setGst(currentInvoice.getGst());
        this.setHst(currentInvoice.getHst());
        this.setNetValue(currentInvoice.getNetValue());
        this.setRemoved(currentInvoice.getRemoved());
        this.invoiceDetailsSongsList = invoiceSongController.getSongsForInvoice(currentInvoice);
        this.removalList = invoiceSongController.getRemovedSongsForInvoice(currentInvoice);
        this.songs = new DualListModel<>(invoiceDetailsSongsList, removalList);
    }
    
    /**
     * Unset parameters after completion of edit.
     */
    private void unsetParams(){
        this.clientId = null;
        this.saleDate = null;
        this.grossValue = null;
        this.pst = null;
        this.gst = null;
        this.hst = null;
        this.netValue = null;
        this.removed = false;
        this.removalList = new ArrayList<>();
        this.invoiceDetailsSongsList = new ArrayList<>();
        this.currentInvoice = new Invoices();
        this.songs = new DualListModel<>();
    }
    
    /**
     * An action method that edits an Invoices entity after a 
     * successful button click.
     * 
     * @throws IOException if there is an error redirecting/dispatching to a 
     *                     page.
     */
    public void editInvoice() throws IOException{
        try{
            currentInvoice.setClientId(clientId);
            currentInvoice.setSaleDate(saleDate);
            currentInvoice.setGrossValue(grossValue);
            currentInvoice.setNetValue(netValue);
            currentInvoice.setRemoved(removed);
            removalList = songs.getTarget();
            if(removalList.size() > 0){
                for(InvoiceDetailsSongs ids : removalList){
                    if(!ids.getRemoved()){
                        ids.setRemoved(true);
                        invoiceSongController.edit(ids);
                    }
                }
            }
            invoiceDetailsSongsList = songs.getSource();
            if(invoiceDetailsSongsList.size() > 0){
                for(InvoiceDetailsSongs ids : invoiceDetailsSongsList){
                    if(ids.getRemoved()){
                        ids.setRemoved(false);
                        invoiceSongController.edit(ids);
                    }
                }
            }
            
            invoiceController.edit(currentInvoice);
            unsetParams();
            disabled = true;
        }
        catch(Exception e){
            Logger.getLogger(InvoiceActionBean.class.getName()).log(Level.SEVERE, null, e);
            return;
        }
        
        FacesContext.getCurrentInstance().getExternalContext().redirect("manager.xhtml");
    }
    
}
