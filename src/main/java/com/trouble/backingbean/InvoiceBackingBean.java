package com.trouble.backingbean;

import com.trouble.JPAController.InvoicesJpaController;
import com.trouble.entities.Invoices;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Frank
 */
@Named("invoiceBacking")
@RequestScoped
public class InvoiceBackingBean implements Serializable
{

    @Inject
    private InvoicesJpaController invoicesJpaController;

    private Invoices invoice;

    /**
     * Get the entity if not instantiated, it will be instantiated
     *
     * @return invoice -entity
     */
    public Invoices getInvoice()
    {
        if (invoice == null)
        {
            invoice = new Invoices();
        }
        return invoice;
    }

    /**
     * Get list of invoices
     *
     * @return
     */
    public List<Invoices> getAllInvoices()
    {

        return invoicesJpaController.findInvoicesEntities();
    }
}
