package com.trouble.backingbean;

import com.trouble.JPAController.InvoiceDetailsAlbumJpaController;
import com.trouble.entities.InvoiceDetailsAlbum;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Frank
 */
@Named("invoiceDetailsAlbumBacking")
@RequestScoped
public class InvoiceDetailsAlbumBackingBean implements Serializable
{

    @Inject
    private InvoiceDetailsAlbumJpaController invoiceDetailsAlbumJpaController;

    private InvoiceDetailsAlbum invoiceDetailsAlbum;

    /**
     * Get the entity if not instantiated, it will be instantiated
     *
     * @return invoiceDetailsAlbum -entity
     */
    public InvoiceDetailsAlbum getInvoiceDetailsAlbum()
    {
        if (invoiceDetailsAlbum == null)
        {
            invoiceDetailsAlbum = new InvoiceDetailsAlbum();
        }
        return invoiceDetailsAlbum;
    }

    /**
     * Get list of invoiceDetailsAlbum
     *
     * @return
     */
    public List<InvoiceDetailsAlbum> getAllInvoiceDetailsAlbum()
    {

        return invoiceDetailsAlbumJpaController.findInvoiceDetailsAlbumEntities();
    }
}
