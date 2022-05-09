package beans.action;

import java.io.InputStream;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * An action bean meant to handle the download of the MP3 file.
 * 
 * @author Dimitri Spyropoulos
 */
@Named("downloads")
@SessionScoped
public class DownloadsActionBean implements Serializable
{

    private StreamedContent downloadFile;

    public DownloadsActionBean()
    {
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/mp3/music.mp3");
        downloadFile = new DefaultStreamedContent(stream, "audio/mpeg", "music.mp3");
    }

    public StreamedContent getDownloadFile()
    {
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/mp3/music.mp3");
        downloadFile = new DefaultStreamedContent(stream, "audio/mpeg", "music.mp3");
        return downloadFile;
    }
}
