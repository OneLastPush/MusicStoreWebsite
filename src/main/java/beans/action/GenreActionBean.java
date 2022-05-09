package beans.action;

import com.trouble.JPAController.AlbumsJpaController;
import com.trouble.entities.Albums;
import com.trouble.entities.Genres_;
import com.trouble.entities.Songs;
import com.trouble.entities.Songs_;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

/**
 *
 * @author Frank Birikundavyi
 */
@RequestScoped
@Named("genreAction")
public class GenreActionBean implements Serializable
{
    @Inject
    private AlbumsJpaController albumsJpaController;
    
    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;
    
    private List<Albums> result;

    public List<Albums> getResult()
    {
        return result;
    }
//    public int getSize(){
//        int size = 0;
//        if(result != null){
//            size = result.size();
//        }
//        return size;
//    }

    public void setResult(List<Albums> result)
    {
        this.result = result;
    }
    
    public String getUrl(){
        return "./genrePage.xhtml";
    }
    public void findAllAlbumsByGenre(String genreName){
        System.out.println("This genre name: "+ genreName);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Songs> song = cq.from(Songs.class);
        Join genre = song.join(Songs_.genreId);
        cq.where(cb.equal(genre.get(Genres_.genreName), genreName));
        TypedQuery<Songs> query = em.createQuery(cq);
        List<Songs> list = query.getResultList();
        System.out.println("Number entry in list : " + list.size());
        getAllAblumId(list);
        
        }

    private void getAllAblumId(List<Songs> slist)
    {
        List<Albums> alist = new ArrayList<>();
        for(Songs s: slist){
            Albums temp = s.getAlbumId();
            if(!isDuplicate(alist, temp)){
                alist.add(temp);
            }
        }
        result = alist;
    }

    private boolean isDuplicate(List<Albums> alist, Albums temp)
    {
        for(Albums a: alist){
            if(a.getId() == temp.getId()){
                return true;
            }    
        }
        return false;
    }
     /**
     * Get list of albums
     *
     * @return
     */
    public void findAllAlbums()
    {
        result = albumsJpaController.findAlbumsEntities();
    }

}
