package beans.action;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * An action bean meant to handle operations within the reports page.
 *
 * @author Dimitri Spyropoulos
 */
@ManagedBean(name = "report")
@SessionScoped
public class ReportBean implements Serializable
{

    private Date calendar1 = new Date();
    //Sets the second date as one day after the current date.
    private Date calendar2 = new Date(calendar1.getTime() + (1000 * 60 * 60 * 24));
    private int id = 1;

    public ReportBean()
    {
    }

    public Date getCalendar1()
    {
        return calendar1;
    }

    public void setCalendar1(Date calendar1)
    {
        this.calendar1 = calendar1;
    }

    public Date getCalendar2()
    {
        return calendar2;
    }

    public void setCalendar2(Date calendar2)
    {
        this.calendar2 = calendar2;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

}
