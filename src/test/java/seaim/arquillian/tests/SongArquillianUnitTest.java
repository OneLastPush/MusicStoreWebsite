
package seaim.arquillian.tests;

import com.trouble.JPAController.AlbumsJpaController;
import com.trouble.JPAController.ArtistsJpaController;
import com.trouble.JPAController.GenresJpaController;
import com.trouble.JPAController.SongsJpaController;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.backing.SongBackingBean;
import com.trouble.entities.Songs;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import static org.assertj.core.api.Assertions.assertThat;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Seaim Khan
 */
@RunWith(Arquillian.class)
public class SongArquillianUnitTest {
    @Deployment
    public static WebArchive deploy() {

        // Use an alternative to the JUnit assert library called AssertJ
        // Need to reference MySQL driver as it is not part of GlassFish
        final File[] dependencies = Maven
                .resolver()
                .loadPomFromFile("pom.xml")
                .resolve(
                        "org.assertj:assertj-core").withoutTransitivity()
                .asFile();

        // For testing Arquillian prefers a resources.xml file over a
        // context.xml
        // Actual file name is resources-mysql-ds.xml in the test/resources
        // folder
        // The SQL script to create the database is also in this folder
        final WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "test.war")
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
                
                .addPackage(SongsJpaController.class.getPackage())
                .addPackage(RollbackFailureException.class.getPackage())
                .addPackage(SongBackingBean.class.getPackage())
                .addPackage(Songs.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/glassfish-resources.xml"), "glassfish-resources.xml")
                .addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml")
                .addAsResource("createTroubleDB.sql")
                .addAsLibraries(dependencies);

        return webArchive;
    }
    
    @Inject
    private SongsJpaController sjc;
    
    @Inject
    private SongBackingBean sbb;
    
    @Inject
    private AlbumsJpaController ajc;
    
    @Inject
    private ArtistsJpaController art;
    
    @Inject
    private GenresJpaController gjc;
    
    @Resource(name="java:app/TroubleMediaDataBase")
    private DataSource ds;
    
    public SongArquillianUnitTest(){}
    
    @Test
    public void TestCreateSong() throws Exception{
        Songs song = new Songs();
        song.setAlbumId(ajc.findAlbums(1));
        song.setArtistId(art.findArtists(1));
        song.setGenreId(gjc.findGenres(1));
        song.setDateAdded(new Date());
        song.setCostPrice(new BigDecimal("0.22"));
        song.setListPrice(new BigDecimal("1.99"));
        song.setRemovalStatus(false);
        song.setSingleSong(false);
        song.setSongLength(new Date());
        song.setTrackNumber(2);
        song.setTrackTitle("Test");
        sjc.create(song);
        assertThat(sjc.getSongsCount()).isEqualTo(128);
    }
    
    @Test
    public void TestEditSong() throws Exception{
        Songs oldSong = sjc.findSongs(1);
        Songs newSong = sjc.findSongs(1);
        newSong.setListPrice(new BigDecimal("0.99"));
        sjc.edit(newSong);
        assertThat(sjc.findSongs(1).getListPrice()).isNotEqualTo(oldSong.getListPrice());
    }
    
    @Test
    public void TestSongTotalSales() throws Exception{
        BigDecimal totalSales = sbb.getSongTotalSales(1);
        assertThat(totalSales).isEqualTo(new BigDecimal("0.00"));
    }
    
    /**
     * This routine is courtesy of Bartosz Majsak who also solved my Arquillian
     * remote server problem
     */
    @Before
    public void seedDatabase() {
        final String seedDataScript = loadAsString("createTroubleDB.sql");

        try (Connection connection = ds.getConnection()) {
            for (String statement : splitStatements(new StringReader(
                    seedDataScript), ";")) {
                connection.prepareStatement(statement).execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed seeding database", e);
        }
        //System.out.println("Seeding works");
    }

    /**
     * The following methods support the seedDatabse method
     */
    private String loadAsString(final String path) {
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(path)) {
            return new Scanner(inputStream).useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new RuntimeException("Unable to close input stream.", e);
        }
    }

    private List<String> splitStatements(Reader reader,
        String statementDelimiter) {
        final BufferedReader bufferedReader = new BufferedReader(reader);
        final StringBuilder sqlStatement = new StringBuilder();
        final List<String> statements = new LinkedList<>();
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || isComment(line)) {
                    continue;
                }
                sqlStatement.append(line);
                if (line.endsWith(statementDelimiter)) {
                    statements.add(sqlStatement.toString());
                    sqlStatement.setLength(0);
                }
            }
            return statements;
        } catch (IOException e) {
            throw new RuntimeException("Failed parsing sql", e);
        }
    }

    private boolean isComment(final String line) {
        return line.startsWith("--") || line.startsWith("//")
                || line.startsWith("/*");
    }
}
