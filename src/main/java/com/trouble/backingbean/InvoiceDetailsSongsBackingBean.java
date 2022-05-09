package com.trouble.backingbean;

import com.trouble.JPAController.InvoiceDetailsSongsJpaController;
import com.trouble.entities.InvoiceDetailsSongs;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Frank
 */
@Named("invoiceDetailsSongBacking")
@RequestScoped
public class InvoiceDetailsSongsBackingBean implements Serializable
{

    @Inject
    private InvoiceDetailsSongsJpaController invoiceDetailsSongsJpaController;

    private InvoiceDetailsSongs invoiceDetailsSong;

    /**
     * Get the entity if not instantiated, it will be instantiated
     *
     * @return invoiceDetailsSongs -entity
     */
    public InvoiceDetailsSongs getInvoiceDetailsSongs()
    {
        if (invoiceDetailsSong == null)
        {
            invoiceDetailsSong = new InvoiceDetailsSongs();
        }
        return invoiceDetailsSong;
    }

    /**
     * Get list of invoiceDetailsSongs
     *
     * @return
     */
    public List<InvoiceDetailsSongs> getAllInvoiceDetailsSongs()
    {

        return invoiceDetailsSongsJpaController.findInvoiceDetailsSongsEntities();
    }
}
