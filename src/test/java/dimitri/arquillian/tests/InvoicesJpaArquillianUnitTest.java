package dimitri.arquillian.tests;

import com.trouble.JPAController.InvoicesJpaController;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.Invoices;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
 * @author Dimitri Spyropoulos
 */
@RunWith(Arquillian.class)
public class InvoicesJpaArquillianUnitTest {  
    
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
                
                .addPackage(InvoicesJpaController.class.getPackage())
                .addPackage(RollbackFailureException.class.getPackage())
                .addPackage(Invoices.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/glassfish-resources.xml"), "glassfish-resources.xml")
                .addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml")
                .addAsResource("createTroubleDB.sql")
                .addAsLibraries(dependencies);

        return webArchive;
    }
    
    @Inject
    private InvoicesJpaController invoicesController;
    
    @Resource(name= "java:app/TroubleMediaDataBase")
    private DataSource ds;    
    
    public InvoicesJpaArquillianUnitTest(){}
    
    @Test
    public void testGetSalesByArtist() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01");
        Date date2 = new Date();
        int artistId = 1;
        
        String artistSales = invoicesController.getSalesByArtist(artistId, date1, date2);
        assertThat(artistSales).isEqualTo("7.00");
    }
    
    @Test
    public void testGetSales() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01");
        Date date2 = new Date();
        
        double artistSales = invoicesController.getSales(date1, date2);
        assertThat(artistSales).isEqualTo(48.0);
    }
    
    @Test
    public void testGetProfit() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01");;
        Date date2 = new Date();
        
        String artistSales = invoicesController.getProfit(date1, date2);
        assertThat(artistSales).isEqualTo("34.09");
    }
    
    @Test
    public void testGetTotalCost() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01");
        Date date2 = new Date();
        
        double artistSales = invoicesController.getTotalCost(date1, date2);
        assertThat(artistSales).isEqualTo(13.91);
    }
    
    @Test
    public void testGetSalesByClient() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01");
        Date date2 = new Date();
        int clientId = 1;
        
        String artistSales = invoicesController.getSalesByClient(clientId, date1, date2);
        assertThat(artistSales).isEqualTo("0.00");
    }
    
    @Test
    public void testGetTopSellingTracks() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01");
        Date date2 = new Date();
        
        List<Object[]> topTracks = invoicesController.getTopSellingTracks(date1, date2);
        assertThat(topTracks.size()).isEqualTo(8);
    }
    
    @Test
    public void testGetTracksWithoutSales() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01");
        Date date2 = new Date();
   
        List<Object[]> tracks = invoicesController.getTracksWithoutSales(date1, date2);
        assertThat(tracks.size()).isEqualTo(119);
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
            throw new RuntimeException("Failed seeding database", e);
        }
    }

    /**
     * The following methods support the seedDatabase method
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
