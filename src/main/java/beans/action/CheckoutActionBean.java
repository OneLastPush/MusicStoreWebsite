package beans.action;

import com.mysql.jdbc.Constants;
import com.trouble.JPAController.SongsJpaController;
import com.trouble.JPAController.UsersJpaController;
import com.trouble.entities.Albums;
import com.trouble.entities.Songs;
import com.trouble.entities.Users;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Cookie;

/**
 *
 * @author Max
 */
@Named(value = "checkout")
@SessionScoped
public class CheckoutActionBean implements Serializable {

    @Inject
    private UsersJpaController userController;
    @Inject
    private SongsJpaController songController;

    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;

    private Double subTotal;
    private Double taxes;
    private Double total;

    private DecimalFormat df = new DecimalFormat("0.00");

    private ArrayList<Songs> cart = new ArrayList<>();

    public CheckoutActionBean() {

    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getTaxes() {
        return taxes;
    }

    public void setTaxes(Double taxes) {
        this.taxes = taxes;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * adding item to the cart list
     *
     * @param song
     */
    public void addItem(Songs song) {
        System.out.println("Add this song to cart : " + song.getTrackTitle());
        cart.add(song);

    }

    public void growlMsg() {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage("Successful", "Add to cart"));

    }

    /**
     * removing item from the list
     *
     * @param s
     * @return
     */
    public String removeItem(Songs s) {

        System.out.println(s);

        boolean remove = cart.remove(s);
        System.out.println(remove);

        return null;

    }

    /**
     * get the whole cart
     *
     * @return
     */
    public ArrayList<Songs> getCart() {
        return cart;
    }

    /**
     * Method to add the first five songs as a test now because the bean does
     * not work across session scoped we need that button to show the rest of
     * the functionality of the checkout
     *
     * @return
     */
    public String addCheckoutTest() {
        cart.addAll(songController.findSongsEntities(5, 1));
        return "./checkout.xhtml";
    }

    /**
     * Get the subtotal
     *
     * @return return the subtotal
     */
    public String totalPrice() {
        Double price = 0.0;
        for (Songs s : cart) {
            price += s.getListPrice().doubleValue();
        }
        subTotal = price;
        return df.format(subTotal);
    }

    public void addAlbum(Albums album) {
        ArrayList<Songs> songs = (ArrayList<Songs>) album.getSongsList();
        cart.addAll(songs);

    }

    /**
     * Get the user email from the cookie
     * @return
     */
    private String readCookie() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> cookieMap = context.getExternalContext().getRequestCookieMap();
        String email = ((Cookie) cookieMap.get("email")).getValue();
        return email;
    }
    /**
     * Get the taxes
     * @return just the taxes
     */
    public String getTotalTax() {
        Double tax = 0.0;
        String email = readCookie();

        System.out.println(email);
        if (email == null) {
            return df.format("0.00");
        }
        Users user = userController.findByEmail(email);
        if (null != user.getProvince()) {
            switch (user.getProvince()) {
                case "Alberta":
                    tax = 0.05;
                    break;
                case "British Columbia":
                    tax = 0.12;
                    break;
                case "Manitoba":
                    tax = 0.13;
                    break;
                case "New Brunswick":
                    tax = 0.15;
                    break;
                case "Newfoundland and Labrador":
                    tax = 0.15;
                    break;
                case "Northwest Territories":
                    tax = 0.05;
                    break;
                case "Nova Scotia":
                    tax = 0.15;
                    break;
                case "Nunavut":
                    tax = 0.05;
                    break;
                case "Ontario":
                    tax = 0.13;
                    break;
                case "Prince Edward Island":
                    tax = 0.15;
                    break;
                case "Quebec":
                    tax = 0.14975;
                    break;
                case "Saskatchewan":
                    tax = 0.10;
                    break;
                case "Yukon":
                    tax = 0.05;
                    break;
                default:
                    break;
            }
        }
        if (totalPrice() != null) {

            taxes = Double.parseDouble(totalPrice()) * tax;
            return df.format(taxes);
        } else {
            return "0.00";
        }

    }

    /**
     * Get the total
     * @return subtotal plus taxes
     */
    public String totalPlusTax() {
        if (totalPrice() != null || getTotalTax() != null) {
            total = Double.parseDouble(totalPrice()) + Double.parseDouble(getTotalTax());
            return df.format(total);
        } else {
            return df.format("0.00");
        }

    }

}
