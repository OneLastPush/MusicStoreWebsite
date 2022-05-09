package beans.action;

import com.trouble.JPAController.AlbumsJpaController;
import com.trouble.JPAController.SongsJpaController;
import com.trouble.JPAController.SongwriterSongsJpaController;
import com.trouble.entities.Albums;
import com.trouble.entities.Artists;
import com.trouble.entities.Genres;
import com.trouble.entities.Labels;
import com.trouble.entities.Songs;
import com.trouble.entities.SongwriterSongs;
import com.trouble.entities.Songwriters;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

/**
 * Action Bean that works for both Album and Tracks.
 *
 * @author Max, Seaim and Frank
 */
@SessionScoped
@Named("album")
public class AlbumActionBean implements Serializable
{

    @Inject
    private AlbumsJpaController albumsController;

    @Inject
    private SongsJpaController songsController;

    @Inject
    private SongwriterSongsJpaController songwriterSongsController;

    @Inject
    private Songs newSong;

    @Inject
    private Albums newAlbum;

    @Inject
    private SongwriterSongs songwriterOfSong;

    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;

    private Integer id;
    private String albumTitle;
    private String trackTitle;
    private Albums album;
    private Boolean singleSong;
    private Integer trackNumber;
    private Date songLength;
    private Genres genre;
    private String albumCover;
    private Date releaseDate;
    private Integer trackCount;
    private BigDecimal costPrice;
    private BigDecimal listPrice;
    private BigDecimal salePrice;
    private Boolean removalStatus;
    private Date removalDate;
    private Artists artist;
    private Labels recordingLabels;
    private Songwriters songwriter;
    private List<Songs> songsList;
    private final Date currentDate = new Date();

    private String name;
    private Songs currentSong;
    private Albums currentAlbum;
    private Boolean disabled = true;
    private Boolean disabledSong = true;
    private Integer maxTrackCount;
    
    /**
     * No-parameter default constructor
     */
    public AlbumActionBean() {}
    
    /**
     * Init method that loads default values for several parameters.
     */
    @PostConstruct
    public void init(){
        unsetParams();
        removalStatus = false;
        singleSong = false;   
    }

    /**
     * Gets the ID of an entity.
     * 
     * @return The entity's ID 
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * Sets the ID of an entity.
     * 
     * @param id The inputted entity ID.
     */
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * Gets a cost price.
     * 
     * @return The cost price.
     */
    public BigDecimal getCostPrice() {
        return costPrice;
    }

    /**
     * Sets a cost price.
     * 
     * @param costPrice The inputted cost price.
     */
    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    /**
     * Gets a list price.
     * 
     * @return The list price.
     */
    public BigDecimal getListPrice() {
        return listPrice;
    }

    /**
     * Sets a list price.
     * 
     * @param listPrice The inputted list price.
     */
    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    /**
     * Gets an Artists entity.
     * 
     * @return The Artists entity.
     */
    public Artists getArtist() {
        return artist;
    }

    /**
     * Sets an Artists entity.
     * 
     * @param artist The inputted Artists entity.
     */
    public void setArtist(Artists artist) {
        this.artist = artist;
    }

    /**
     * Gets a Recording Labels entity.
     * 
     * @return The Recording Labels entity. 
     */
    public Labels getRecordingLabels() {
        return recordingLabels;
    }

    /**
     * Sets a Recording Labels entity
     * 
     * @param recordingLabels The inputted Recording Labels entity. 
     */
    public void setRecordingLabels(Labels recordingLabels) {
        this.recordingLabels = recordingLabels;
    }

    /**
     * Gets a list of Songs entities.
     * 
     * @return The list of Songs entities. 
     */
    public List<Songs> getSongsList() {
        return songsList;
    }

    /**
     * Sets a list of Songs entities.
     * 
     * @param songsList The inputted Songs entities. 
     */
    public void setSongsList(List<Songs> songsList) {
        this.songsList = songsList;
    }
    
    /**
     * Formats the time from an inputted string.
     * 
     * @param time An inputted string.
     * @return A time-formatted string.
     */
    public String formatTime(String time){
        String[] array = time.split(" ")[3].split(":");
        return array[0] + ":" + array[1];
    }
    
    /**
     * Formats the date from an inputted string.
     * 
     * @param date An inputted string.
     * @return A date-formatted string.
     */
    public String formatDate(String date){
        String[] array = date.split(" ");
        return array[1] + " " + array[2] + " " + array[5];
    }
    
    /**
     * Gets the current price of an album.
     * 
     * @param album An inputted Albums entity.
     * @return A price string of either list or sale.
     */
    public String getCurrentPrice(String salePrice, String listPrice)
    {
        String currentPrice;
        if (salePrice.equals("0.00"))
        {
            currentPrice = listPrice;
        }
        else
        {
            currentPrice = salePrice;
        }
        return currentPrice;
    }
    
    /**
     * Gets a name.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets a name.
     * 
     * @param name The inputted name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the current album.
     * 
     * @return An album entity representing the current album.
     */
    public Albums getCurrentAlbum() {
        return currentAlbum;
    }

    /**
     * Sets the current album.
     * 
     * @param currentAlbum An inputted album entity. 
     */
    public void setCurrentAlbum(Albums currentAlbum) {
        this.currentAlbum = currentAlbum;
    }

    /**
     * Gets the current song.
     * 
     * @return An song entity representing the current song.
     */
    public Songs getCurrentSong() {
        return currentSong;
    }

    /**
     * Sets the current song.
     * 
     * @param currentSong An inputted song entity. 
     */
    public void setCurrentSong(Songs currentSong) {
        this.currentSong = currentSong;
    }
    
    /**
     * Gets an album title.
     * 
     * @return The album title.
     */
    public String getAlbumTitle() {
        return albumTitle;
    }

    /**
     * Sets an album title. 
     * 
     * @param albumTitle The inputted album title.
     */
    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    /**
     * Gets a file name of an album cover.
     * 
     * @return The album cover's file name.
     */
    public String getAlbumCover() {
        return albumCover;
    }

    /**
     * Sets a file name for an album cover.
     * 
     * @param albumCover The inputted file name.
     */
    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }
    
    /**
     * Gets a release date.
     * 
     * @return The release date. 
     */
    public Date getReleaseDate() {
        return releaseDate;
    }
    
    /**
     * Sets a release date.
     * 
     * @param releaseDate The inputted release date.
     */
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    /**
     * Gets a track count.
     * 
     * @return The track count.
     */
    public Integer getTrackCount() {
        return trackCount;
    }

    /**
     * Sets a track count.
     * 
     * @param trackCount The inputted track count.
     */
    public void setTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
    }
    
    /**
     * Gets a sale price.
     * 
     * @return The sale price.
     */
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    /**
     * Sets a sale price.
     * 
     * @param salePrice The inputted sale price.
     */
    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    /**
     * Gets a removal status.
     * 
     * @return The removal status.
     */
    public Boolean getRemovalStatus() {
        return removalStatus;
    }

    /**
     * Sets a removal status.
     * 
     * @param removalStatus The inputted removal status.
     */
    public void setRemovalStatus(Boolean removalStatus) {
        this.removalStatus = removalStatus;
    }
    
    /**
     * Gets a removal date.
     * 
     * @return The removal date.
     */
    public Date getRemovalDate() {
        return removalDate;
    }

    /**
     * Sets a removal date.
     * 
     * @param removalDate The inputted removal date.
     */
    public void setRemovalDate(Date removalDate) {
        this.removalDate = removalDate;
    }

    /**
     * Gets a track title.
     * 
     * @return The track title. 
     */
    public String getTrackTitle() {
        return trackTitle;
    }

    /**
     * Sets a track title.
     * 
     * @param trackTitle The inputted track title.
     */
    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    /**
     * Gets a boolean representing a single or album track.
     * 
     * @return False if album track, true if single.
     */
    public Boolean getSingleSong() {
        return singleSong;
    }
    
    /**
     * Sets a boolean representing a single or album track.
     * 
     * @param singleSong The inputted boolean value.
     */
    public void setSingleSong(Boolean singleSong) {
        this.singleSong = singleSong;
    }
    
    /**
     * Gets a track number.
     * 
     * @return The track number. 
     */
    public Integer getTrackNumber() {
        return trackNumber;
    }
    
    /**
     * Sets a track number.
     * 
     * @param trackNumber The inputted track number.
     */
    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    /**
     * Gets a song length.
     * 
     * @return The song length.
     */
    public Date getSongLength() {
        return songLength;
    }

    /**
     * Sets a song length.
     * 
     * @param songLength The inputted song length.
     */
    public void setSongLength(Date songLength) {
        this.songLength = songLength;
    }
    
    /**
     * Gets a Genres entity.
     * 
     * @return The Genres entity.
     */
    public Genres getGenre() {
        return genre;
    }

    /**
     * Sets a Genres entity.
     * 
     * @param genre The inputted Genres entity.
     */
    public void setGenre(Genres genre) {
        this.genre = genre;
    }
    
    /**
     * Gets a Songwriters entity.
     * 
     * @return The Songwriters entity.
     */
    public Songwriters getSongwriter() {
        return songwriter;
    }
    
    /**
     * Sets a Songwriters entity.
     * 
     * @param songwriter The inputted Songwriters entity.
     */
    public void setSongwriter(Songwriters songwriter) {
        this.songwriter = songwriter;
    }
    
    /**
     * Gets an Albums entity.
     * 
     * @return The Albums entity.
     */
    public Albums getAlbum() {
        return album;
    }

    /**
     * Sets an Albums entity.
     * 
     * @param album The inputted Albums entity.
     */
    public void setAlbum(Albums album) {
        this.album = album;
    }
    
    /**
     * Gets a boolean representing a disabled value.
     * 
     * @return True if disabled, false if not.
     */
    public Boolean getDisabled() {
        return disabled;
    }

    /**
     * Sets a disabled value.
     * 
     * @param disabled The inputted boolean value.
     */
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    
    /**
     * Gets a boolean representing a disabled value for a song.
     * 
     * @return True if disabled, false if not.
     */
    public Boolean getDisabledSong() {
        return disabledSong;
    }

    /**
     * Sets a disabled value for a song.
     * 
     * @param disabledSong The inputted boolean value.
     */
    public void setDisabledSong(Boolean disabledSong) {
        this.disabledSong = disabledSong;
    }
    
    /**
     * Gets the max track count of an album.
     * 
     * @return The max track count.
     */
    public Integer getMaxTrackCount() {
        return maxTrackCount;
    }
    
    /**
     * Sets the max track count of an album.
     * 
     * @param maxTrackCount The inputted max track count.
     */
    public void setMaxTrackCount(Integer maxTrackCount) {
        this.maxTrackCount = maxTrackCount;
    }
    
    /**
     * PrimeFaces event handler for a row select on a datatable.
     * 
     * @param event A SelectEvent object.
     */
    public void onRowSelect(SelectEvent event){
        disabled = false;
    }
    
    /**
     * PrimeFaces event handler for a row select on a song datatable.
     * 
     * @param event A SelectEvent object.
     */
    public void onSongRowSelect(SelectEvent event){
        disabledSong = false;
    }
    
    /**
     * Sets the album parameters for creation or editing of an Albums entity.
     * 
     * @param album An Albums entity.
     */
    private void setAlbumParams(Albums album){
        album.setAlbumTitle(albumTitle);
        album.setReleaseDate(releaseDate);
        album.setArtistId(artist);
        album.setRecordingLabel(recordingLabels);
        if (getAlbumCover() != null && !getAlbumCover().isEmpty())
        {
            album.setAlbumCover(getAlbumCover());
        }
        else
            album.setAlbumCover("resources/covers/no_cover.jpg");
        
        album.setTrackCount(trackCount);
        album.setCostPrice(costPrice);
        album.setListPrice(listPrice);
        album.setDateAdded(currentDate);

        if (salePrice != null)
        {
            album.setSalePrice(salePrice);
        }
        else
        {
            album.setSalePrice(new BigDecimal("0.00"));
        }

        if (removalStatus != null && removalStatus == true)
        {
            album.setRemovalStatus(removalStatus);
            album.setRemovalDate(removalDate);
        }
        else
        {
            album.setRemovalStatus(false);
        }
    }
    
    /**
     * Set default values for editing an album.
     */
    public void setEditAlbum(){
        this.setAlbumTitle(currentAlbum.getAlbumTitle());
        this.setReleaseDate(currentAlbum.getReleaseDate());
        this.setArtist(currentAlbum.getArtistId());
        this.setRecordingLabels(currentAlbum.getRecordingLabel());
        this.setAlbumCover(currentAlbum.getAlbumCover());
        this.setTrackCount(currentAlbum.getTrackCount());
        this.setCostPrice(currentAlbum.getCostPrice());
        this.setListPrice(currentAlbum.getListPrice());
        this.setSalePrice(currentAlbum.getSalePrice());
        this.setRemovalStatus(currentAlbum.getRemovalStatus());
        if (currentAlbum.getRemovalDate() != null)
        {
            this.setRemovalDate(currentAlbum.getRemovalDate());
        }
    }
    
    /**
     * Unset parameters after completion of create/edit.
     */
    private void unsetParams(){
        this.albumTitle = null;
        this.albumCover = null;
        this.artist = null;
        this.releaseDate = null;
        this.recordingLabels = null;
        this.trackCount = null;
        this.costPrice = null;
        this.listPrice = null;
        this.salePrice = null;
        this.removalDate = null;
        this.removalStatus = false;
        this.trackNumber = null;
        this.trackTitle = null;
        this.genre = null;
        this.album = null;
        this.singleSong = false;
        this.songLength = null;
        this.songwriter = null;
        this.maxTrackCount = null;
        this.newSong = null;
        this.newAlbum = null;
        this.currentSong = new Songs();
        this.currentAlbum = new Albums();
    }
    
    /**
     * Sets the song parameters for creation or editing of an Songs entity.
     * 
     * @param song An Songs entity.
     */
    private void setSongParams(Songs song){
        song.setTrackTitle(trackTitle);
        song.setSongLength(songLength);
        song.setGenreId(genre);
        song.setCostPrice(costPrice);
        song.setListPrice(listPrice);
        song.setDateAdded(currentDate);
        song.setSingleSong(singleSong);

        if (salePrice != null)
        {
            song.setSalePrice(salePrice);
        }
        else
        {
            song.setSalePrice(new BigDecimal("0.00"));
        }

        if (removalStatus != null && removalStatus == true)
        {
            song.setRemovalStatus(removalStatus);
            song.setRemovalDate(removalDate);
        }
        else
        {
            song.setRemovalStatus(false);
        }

        if (singleSong)
        {
            song.setArtistId(artist);
            song.setTrackNumber(1);
            setAlbumTitle(trackTitle);
            setTrackCount(1);
            setAlbumParams(newAlbum);
        }
        else
        {
            song.setAlbumId(album);
            song.setTrackNumber(trackNumber);
            song.setArtistId(album.getArtistId());
        }
    }
    
    /**
     * Set default values for editing a song.
     */
    public void setEditTrack(){
        this.setTrackTitle(currentSong.getTrackTitle());
        this.setSongLength(currentSong.getSongLength());
        this.setGenre(currentSong.getGenreId());
        this.setCostPrice(currentSong.getCostPrice());
        this.setListPrice(currentSong.getListPrice());
        this.setSalePrice(currentSong.getSalePrice());
        this.setSingleSong(currentSong.getSingleSong());
        this.setRemovalStatus(currentSong.getRemovalStatus());
        if (currentSong.getRemovalDate() != null)
        {
            this.setRemovalDate(currentSong.getRemovalDate());
        }
        this.setTrackCount(currentSong.getTrackNumber());
        if (getSingleSong())
        {
            currentAlbum = albumsController.findAlbumByTitle(trackTitle);
            setEditAlbum();
        }
        else
        {
            this.setAlbum(currentSong.getAlbumId());
            this.setTrackNumber(currentSong.getTrackNumber());
            this.setMaxTrackCount(currentSong.getAlbumId().getTrackCount());
        }
    }
    
    /**
     * PrimeFaces handler for uploading a file to the server.
     * 
     * @param event A FileUploadEvent object
     * @throws IOException if an error happens during file upload process.
     */
    public void handleFileUpload(FileUploadEvent event) throws IOException {
        String path = FacesContext.getCurrentInstance().getExternalContext()
            .getRealPath("/");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        String name = fmt.format(new Date())
                + "-" + event.getFile().getFileName();
        String fullPath = "resources/covers/" + name;
        validateLength(fullPath);
        File folder = new File(path + "resources/covers/");
        if(!folder.exists())
            folder.mkdirs();
        
        File file = new File(path + fullPath);

        InputStream is = event.getFile().getInputstream();
        OutputStream out = new FileOutputStream(file);
        byte buf[] = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0)
            out.write(buf, 0, len);
        is.close();
        out.close();
        
        FacesMessage message = new FacesMessage("Successful", name + " is uploaded.");
        setAlbumCover(fullPath);
        FacesContext.getCurrentInstance().addMessage(null, message);
        
    }
    
    private void validateLength(String fullPath){
        if(fullPath.length() > 255)
            throw new ValidatorException(new FacesMessage("The filepath is too long!"));
    }
    
    /**
     * Gets the current date.
     * 
     * @return The current date.
     */
    public Date getCurrentDate() {
        return currentDate;
    }
    
    /**
     * An action method that creates a new Albums entity after a 
     * successful button click.
     * 
     * @throws IOException if there is an error redirecting/dispatching to a 
     *                     page.
     */
    public void createAlbum() throws IOException{
        try{
            newAlbum = new Albums();
            setAlbumParams(newAlbum);
            albumsController.create(newAlbum);
            unsetParams();
        }
        catch (Exception e)
        {
            Logger.getLogger(AlbumActionBean.class.getName()).log(Level.SEVERE, null, e);
            return;
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("manager.xhtml");
    }
    
    /**
     * An action method that edits a Songs entity after a 
     * successful button click.
     * 
     * @throws IOException if there is an error redirecting/dispatching to a 
     *                     page.
     */
    public void editAlbum() throws IOException{
        try{
            setAlbumParams(currentAlbum);
            albumsController.edit(currentAlbum);
            unsetParams();
            disabled = true;
        }
        catch (Exception e)
        {
            Logger.getLogger(AlbumActionBean.class.getName()).log(Level.SEVERE, null, e);
            return;
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("manager.xhtml");
    }
    
    /**
     * An action method that creates a new Songs entity after a 
     * successful button click.
     * 
     * @throws IOException if there is an error redirecting/dispatching to a 
     *                     page.
     */
    public void createTrack() throws IOException{
        try{
            newSong = new Songs();
            newAlbum = new Albums();
            setSongParams(newSong);
            if (singleSong)
            {
                albumsController.create(newAlbum);
                newSong.setAlbumId(newAlbum);
            }
            songsController.create(newSong);
            if (songwriter != null)
            {
                songwriterOfSong.setSongwriterId(songwriter);
                songwriterOfSong.setSongId(newSong);
                songwriterSongsController.create(songwriterOfSong);
            }
            unsetParams();
        }
        catch (Exception e)
        {
            Logger.getLogger(AlbumActionBean.class.getName()).log(Level.SEVERE, null, e);
            return;
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("manager.xhtml");
    }
    
    /**
     * An action method that edits a Songs entity after a 
     * successful button click.
     * 
     * @throws IOException if there is an error redirecting/dispatching to a 
     *                     page.
     */
    public void editTrack() throws IOException{
        try{
            newAlbum = new Albums();
            setSongParams(currentSong);
            if (singleSong)
            {
                albumsController.edit(currentAlbum);
                currentSong.setAlbumId(currentAlbum);
            }
            songsController.edit(currentSong);
            unsetParams();
            disabledSong = true;
        }
        catch (Exception e)
        {
            Logger.getLogger(AlbumActionBean.class.getName()).log(Level.SEVERE, null, e);
            return;
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("manager.xhtml");
    }
}
