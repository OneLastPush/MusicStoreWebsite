package com.trouble.backing;

import com.trouble.JPAController.InvoicesJpaController;
import com.trouble.entities.Invoices;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Backing Bean for an Invoices entity.
 * 
 * @author Seaim Khan
 */
@Named("theInvoices")
@SessionScoped
public class InvoiceBackingBean implements Serializable{
 
    @Inject
    private InvoicesJpaController invoicesJpaController;

    private List<Invoices> invoices;
    private List<Invoices> filteredInvoices;
    
    /**
     * Init method that loads a list of all Invoices entities.
     */
    @PostConstruct
    public void init()
    {
        this.invoices = invoicesJpaController.findInvoicesEntities();
    }
    
    /**
     * Gets a list of invoices.
     * 
     * @return The list of invoices.
     */
    public List<Invoices> getInvoices() {
        return invoices;
    }
    
    /**
     * Sets a list of invoices.
     * 
     * @param invoices The inputted list of Invoices entities.
     */
    public void setInvoices(List<Invoices> invoices) {
        this.invoices = invoices;
    }

    /**
     * Gets a list of filtered invoices.
     * 
     * @return The list of filtered invoices.
     */
    public List<Invoices> getFilteredInvoices() {
        return filteredInvoices;
    }

    /**
     * Sets a list of filtered invoices.
     * 
     * @param filteredInvoices The inputted list of filtered Invoices entities.
     */
    public void setFilteredInvoices(List<Invoices> filteredInvoices) {
        this.filteredInvoices = filteredInvoices;
    }
}
