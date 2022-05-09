
package seaim.arquillian.tests;

import com.trouble.JPAController.SurveyChoicesJpaController;
import com.trouble.JPAController.SurveyQuestionsJpaController;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.backing.SurveyBackingBean;
import com.trouble.entities.SurveyChoices;
import com.trouble.entities.SurveyQuestions;
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
public class SurveyArquillianUnitTest {
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
                
                .addPackage(SurveyQuestionsJpaController.class.getPackage())
                .addPackage(SurveyBackingBean.class.getPackage())
                .addPackage(SurveyChoicesJpaController.class.getPackage())
                .addPackage(RollbackFailureException.class.getPackage())
                .addPackage(SurveyQuestions.class.getPackage())
                .addPackage(SurveyChoices.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/glassfish-resources.xml"), "glassfish-resources.xml")
                .addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml")
                .addAsResource("createTroubleDB.sql")
                .addAsLibraries(dependencies);

        return webArchive;
    }
    
    @Inject
    private SurveyQuestionsJpaController sqjc;
    
    @Inject
    private SurveyChoicesJpaController scjc;
    
    @Inject
    private SurveyBackingBean sbb;
    
    @Resource(name="java:app/TroubleMediaDataBase")
    private DataSource ds;
    
    public SurveyArquillianUnitTest(){}
    
    @Test
    public void TestCreateSurvey() throws Exception{
        SurveyQuestions survey = new SurveyQuestions();
        survey.setQuestion("How's this test?");
        survey.setCurrentQuestion(false);
        sqjc.create(survey);
        assertThat(sqjc.getSurveyQuestionsCount()).isEqualTo(2);
    }
    
    @Test
    public void TestEditSurvey() throws Exception{
        SurveyQuestions oldSurvey = sqjc.findSurveyQuestions(1);
        SurveyQuestions newSurvey = sqjc.findSurveyQuestions(1);
        newSurvey.setCurrentQuestion(false);
        sqjc.edit(newSurvey);
        assertThat(sqjc.findSurveyQuestions(1).getCurrentQuestion()).isNotEqualTo(oldSurvey.getCurrentQuestion());
    }
    
    @Test
    public void TestCurrentSurvey() throws Exception{
        SurveyQuestions survey = sbb.getCurrentSurveyQuestion();
        assertThat(survey.getCurrentQuestion()).isEqualTo(true);
    }
    
    @Test
    public void TestCreateChoice() throws Exception{
        SurveyChoices choice = new SurveyChoices();
        choice.setChoice("Test");
        choice.setQuestionId(sqjc.findSurveyQuestions(1));
        scjc.create(choice);
        assertThat(scjc.getSurveyChoicesCount()).isEqualTo(4);
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
