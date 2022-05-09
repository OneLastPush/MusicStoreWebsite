package beans.action;

import com.trouble.JPAController.AlbumsJpaController;
import com.trouble.JPAController.UsersJpaController;
import com.trouble.entities.Albums;
import com.trouble.entities.Genres_;
import com.trouble.entities.Songs;
import com.trouble.entities.Songs_;
import com.trouble.entities.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.servlet.http.Cookie;

/**
 *
 * @author Max Page-Slowik, Frank
 */
@Named("mainPage")
@RequestScoped
public class MainPageActionBean implements Serializable {

    @Inject
    private UsersJpaController userController;

    @Inject
    private AlbumsJpaController albumController;

    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;
    private List<Albums> lastGenreAlbums;

    public MainPageActionBean() {
    }

    public List<Albums> getAlbums() {
        List<Albums> albums = albumController.findAlbumsEntities(3, 1);
        System.out.println(albums.get(1).toString());
        System.out.println(albums.size());
        return albums;
    }

    public boolean isLogIn() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> cookieMap = context.getExternalContext().getRequestCookieMap();
        String isLogged = ((Cookie) cookieMap.get("isLoggedIn")).getValue();
        return Boolean.parseBoolean(isLogged);
    }

    private String readCookie() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        Map<String, Object> cookieMap = context.getExternalContext().getRequestCookieMap();
         String email;
        if(!cookieMap.isEmpty()){
            try{
            email = ((Cookie) cookieMap.get("email")).getValue();
            }
            catch(Exception e){
                email = null;
            }
        return email;
        }
        else{
            return null;
        }
        
    }

    public List<Albums> findLastGenreAlbums() {
        
        String email = readCookie();
        Users user = new Users();
        if(email !=null){
            user = userController.findByEmail(email);
        }
        List<Albums> list = new ArrayList();
        if (user == null) {
            lastGenreAlbums = albumController.findAlbumsEntities(3, 1);
        } else {
            if (user.getLastGenreSearched() != null) {
                //GenreActionBean gab = new GenreActionBean();
                System.out.println("This is the genre id  :  " + user.getLastGenreSearched().getId());
                list =  findAllAlbumsByGenre(user.getLastGenreSearched().getGenreName());
                for (int index = 0; index < list.size(); index++) {
                    System.out.println("This is the album id : " + list.get(index).getArtistId());
                    lastGenreAlbums.add(list.get(index));
                    if (index == 2) {
                        break;
                    }
                }
            } else {
                lastGenreAlbums = albumController.findAlbumsEntities(3, 1);
            }
        }
        return lastGenreAlbums;
    }

    public List<Albums> findAllAlbumsByGenre(String genreName) {
        System.out.println("This genre name: " + genreName);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Songs> song = cq.from(Songs.class);
        Join genre = song.join(Songs_.genreId);
        cq.where(cb.equal(genre.get(Genres_.genreName), genreName));
        TypedQuery<Songs> query = em.createQuery(cq);
        List<Songs> list = query.getResultList();
        System.out.println("Number entry in list : " + list.size());
        return getAllAblumId(list);

    }

    private List<Albums> getAllAblumId(List<Songs> slist) {
        List<Albums> alist = new ArrayList<>();
        for (Songs s : slist) {
            Albums temp = s.getAlbumId();
            if (!isDuplicate(alist, temp)) {
                alist.add(temp);
            }
        }
        return alist;
    }

    private boolean isDuplicate(List<Albums> alist, Albums temp) {
        for (Albums a : alist) {
            if (a.getId() == temp.getId()) {
                return true;
            }
        }
        return false;
    }

    public List<Albums> getLastGenreAlbums() {
        return lastGenreAlbums;
    }

    public void setLastGenreAlbums(List<Albums> lastGenreAlbums) {
        this.lastGenreAlbums = lastGenreAlbums;
    }
}
