
package beans.action;

import com.trouble.entities.Users;
import java.io.Serializable;
import javax.inject.Named;

/**
 *
 * @author Max Page-Slowik
 */
@Named("user")
public class UserActionBean implements Serializable
{

    private Users user;

}
