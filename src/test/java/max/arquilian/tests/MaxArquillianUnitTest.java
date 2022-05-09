
package max.arquilian.tests;

import beans.action.RegisterActionBean;
import com.trouble.JPAController.UsersJpaController;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.Users;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.sql.DataSource;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import javax.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author Max
 */

@RunWith(Arquillian.class)
public class MaxArquillianUnitTest {
    
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
                
                .addPackage(UsersJpaController.class.getPackage())
                .addPackage(RollbackFailureException.class.getPackage())
                .addPackage(Users.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/glassfish-resources.xml"), "glassfish-resources.xml")
                .addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml")
                .addAsResource("createTroubleDB.sql")
                .addAsLibraries(dependencies);

        return webArchive;
    }
    
   @Inject
   private UsersJpaController userController;
    
    
    
    @Resource(name= "java:app/TroubleMediaDataBase")
    private DataSource ds;
    
    
    public MaxArquillianUnitTest() {
    }
    /**
     * Test a basic insert
     * @throws Exception 
     */
    @Test
    public void testReqisterUserNoAdmin() throws Exception {
        Users user = new Users();
        user.setAddress1("321 John ave");
       
        user.setCellNumber("(514)321-3211");
        user.setCity("Montreal");
        user.setProvince("Quebec");
        user.setTitle("Mr");
        user.setCountry("Canada");
        user.setPostalCode("H9S 1A9");
        user.setEmail("john@john.com");
        user.setFname("john");
        user.setHomeNumber("(514)321-3211");
        user.setLname("john");
        user.setAdminPrivilege(false);
        
      
             user.setPword("john");
        
        userController.create(user);
        assertThat(userController.getUsersCount()).isEqualTo(21);
        
    }
    @Test
    public void testReqisterUserAdmin() throws Exception {
        Users user = new Users();
        user.setAddress1("321 John ave");
       
        user.setCellNumber("(514)321-3211");
        user.setCity("Montreal");
        user.setProvince("Quebec");
        user.setTitle("Mr");
        user.setCountry("Canada");
        user.setPostalCode("H9S 1A9");
        user.setEmail("john@john.com");
        user.setFname("john");
        user.setHomeNumber("(514)321-3211");
        user.setLname("john");
        user.setAdminPrivilege(true);
        
        
             user.setPword("john");
        
        userController.create(user);
        assertThat(userController.findByEmail("john@john.com").getAdminPrivilege()).isEqualTo(true);
        
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
