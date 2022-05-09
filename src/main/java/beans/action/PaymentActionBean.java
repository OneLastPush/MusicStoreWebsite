
package beans.action;

import com.kenfogel.luhncheck.LuhnCheck;
import com.trouble.JPAController.InvoiceDetailsSongsJpaController;
import com.trouble.JPAController.InvoicesJpaController;
import com.trouble.JPAController.UsersJpaController;
import com.trouble.entities.InvoiceDetailsSongs;
import com.trouble.entities.Invoices;
import com.trouble.entities.Invoices_;
import com.trouble.entities.Songs;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.Cookie;
import javax.validation.constraints.Future;
import javax.validation.constraints.Size;

/**
 *
 * @author Max
 */
@Named("payment")
public class PaymentActionBean {

    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;

    @Inject
    private CheckoutActionBean checkoutActionBean;

    @Inject
    private LoginActionBean login;
    
    @Inject
    private UsersJpaController userController;

    @Inject
    private InvoicesJpaController invoiceController;

    @Inject
    private InvoiceDetailsSongsJpaController detailController;

    @Size(min = 16)
    @LuhnCheck
    private String card = "";
    @Future
    private Date exprDate = new Date();

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public Date getExprDate() {
        return exprDate;
    }

    public void setExprDate(Date exprDate) {
        this.exprDate = exprDate;
    }

    /**
     * Get the last added invoice to add the details
     * invoice for that invoice
     * @param inv
     * @return The invoice
     */
    public Invoices getLatestAddtion(Invoices inv) {
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Invoices> cq = cb.createQuery(Invoices.class);
        Root<Invoices> invoiceRoot = cq.from(Invoices.class);
        cq.where(cb.equal(invoiceRoot.get(Invoices_.clientId), userController.findByEmail(readCookie())));
        cq.select(invoiceRoot);
        TypedQuery<Invoices> query = em.createQuery(cq);
        return query.setMaxResults(1).getResultList().get(0);
    }
    private String readCookie() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> cookieMap = context.getExternalContext().getRequestCookieMap();
        String email = ((Cookie) cookieMap.get("email")).getValue();
        return email;
    }
    
    /**
     * Set all of the invoice information up,
     * and all of the invoice details for those songs
     * @return the page to redirect to
     */
    public String pay() {
        Invoices invoice = new Invoices();
        invoice.setSaleDate(Date.from(Instant.now()));
        invoice.setClientId(userController.findByEmail(readCookie()));
        invoice.setNetValue(BigDecimal.valueOf(checkoutActionBean.getSubTotal()));
        invoice.setGrossValue(BigDecimal.valueOf(checkoutActionBean.getTotal()));
        invoice.setHst(BigDecimal.valueOf(checkoutActionBean.getTaxes()));
        invoice.setRemoved(false);
        invoice.setGst(BigDecimal.valueOf(0));
        invoice.setPst(BigDecimal.valueOf(0));
        try {
            invoiceController.create(invoice);
        } catch (Exception ex) {
            Logger.getLogger(PaymentActionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        Invoices inv = getLatestAddtion(invoice);
        System.out.println("INVOICE: " +invoiceController.findInvoices(inv.getId()));

        InvoiceDetailsSongs songDet;
        for (Songs s : checkoutActionBean.getCart()) {
            System.out.println("SONG_ID: "+s.getId());
            songDet = new InvoiceDetailsSongs();
            songDet.setInventoryId(s);
            songDet.setSaleId(inv);
            songDet.setSoldPrice(s.getListPrice());
            

            try {
                detailController.create(songDet);
            } catch (Exception ex) {
                Logger.getLogger(PaymentActionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        SendEmailReceipt sendEmail = new SendEmailReceipt();
               

        sendEmail.sendMail(readCookie(),checkoutActionBean.getCart(),checkoutActionBean.getTotal());

        return "/index.xhtml";
       
    }

}
