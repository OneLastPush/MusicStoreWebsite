package beans.action;

import com.trouble.JPAController.SurveyChoicesJpaController;
import com.trouble.JPAController.SurveyQuestionsJpaController;
import com.trouble.backing.SurveyBackingBean;
import com.trouble.entities.SurveyChoices;
import com.trouble.entities.SurveyQuestions;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

/**
 * An action bean meant to work for handling Survey operations.
 * 
 * @author Max Page-Slowik and Seaim Khan
 */
@Named("surveyAction")
@SessionScoped
public class SurveyActionBean implements Serializable
{
    @Inject
    private SurveyChoicesJpaController surveyChoiceController;
    @Inject
    private SurveyQuestionsJpaController surveyQuestionController;
    @Inject
    private SurveyBackingBean surveyBack;
    
    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;

    private Integer id;
    private String question;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private boolean skip;
    private Boolean disabled = true;
    private Boolean rendered = false;
    private Boolean renderedSurvey = false;
    private SurveyQuestions selected;
    private SurveyChoices selectedChoice;
    private SurveyQuestions newSurveyQuestion;
    private HorizontalBarChartModel horizontalBarModel;
    
    /**
     * No-parameter default constructor
     */
    public SurveyActionBean() {}

    /**
     * Init function that creates an initial horizontal bar graph for survey
     * results.
     */
    @PostConstruct
    public void init() {
        rendered = false;
        renderedSurvey = true;
        createHorizontalBarModel();
        newSurveyQuestion = new SurveyQuestions();
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
     * Gets a question.
     * 
     * @return The question.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets a question.
     * 
     * @param question The inputted question.
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Gets a first choice.
     * 
     * @return The first choice.
     */
    public String getChoice1() {
        return choice1;
    }

    /**
     * Sets a first choice.
     * 
     * @param choice1 The inputted first choice.
     */
    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    /**
     * Gets a second choice.
     * 
     * @return The second choice.
     */
    public String getChoice2() {
        return choice2;
    }

    /**
     * Sets a second choice.
     * 
     * @param choice2 The inputted second choice.
     */
    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    /**
     * Gets a third choice.
     * 
     * @return The third choice.
     */
    public String getChoice3() {
        return choice3;
    }

    /**
     * Sets a third choice.
     * 
     * @param choice3 The inputted third choice.
     */
    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }

    /**
     * Gets a fourth choice.
     * 
     * @return The fourth choice.
     */
    public String getChoice4() {
        return choice4;
    }

    /**
     * Sets a fourth choice.
     * 
     * @param choice4 The inputted fourth choice.
     */
    public void setChoice4(String choice4) {
        this.choice4 = choice4;
    }

    /**
     * Checks if a page is skipped on survey creation module.
     * 
     * @return True if skipped, false if not.
     */
    public boolean isSkip() {
        return skip;
    }

    /**
     * Sets a Boolean value representing a skip.
     * 
     * @param skip The inputted Boolean value.
     */
    public void setSkip(boolean skip) {
        this.skip = skip;
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
     * Gets a boolean representing a rendered value.
     * 
     * @return True if rendered, false if not.
     */
    public Boolean getRendered() {
        return rendered;
    }
    
    
    /**
     * Sets a rendered value.
     * 
     * @param rendered The inputted boolean value.
     */
    public void setRendered(Boolean rendered) {
        this.rendered = rendered;
    }
    
    /**
     * Gets a boolean representing a rendered value.
     * 
     * @return True if rendered, false if not.
     */
    public Boolean getRenderedSurvey() {
        return renderedSurvey;
    }
    
    /**
     * Sets a rendered value.
     * 
     * @param renderedSurvey The inputted boolean value.
     */
    public void setRenderedSurvey(Boolean renderedSurvey) {
        this.renderedSurvey = renderedSurvey;
    }
    
    
    /**
     * Gets a selected survey.
     * 
     * @return The selected survey.
     */
    public SurveyQuestions getSelected() {
        return selected;
    }

    /**
     * Sets a selected survey.
     * 
     * @param selected The inputted SurveyQuestions entity.
     */
    public void setSelected(SurveyQuestions selected) {
        this.selected = selected;
    }
    
    /**
     * Gets a selected survey choice.
     * 
     * @return The selected survey choice.
     */
    public SurveyChoices getSelectedChoice() {
        return selectedChoice;
    }

    /**
     * Sets a selected survey choice.
     * 
     * @param selectedChoice The inputted SurveyChoices entity.
     */
    public void setSelectedChoice(SurveyChoices selectedChoice) {
        this.selectedChoice = selectedChoice;
    }

    /**
     * Gets a horizontal bar chart model.
     * 
     * @return The horizontal bar chart model.
     */
    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    /**
     * Sets a horizontal bar chart model.
     * 
     * @param horizontalBarModel The inputted horizontal bar chart model.
     */
    public void setHorizontalBarModel(HorizontalBarChartModel horizontalBarModel) {
        this.horizontalBarModel = horizontalBarModel;
    }
    
    /**
     * Creates a horizontal bar chart model.
     */
    public void createHorizontalBarModel(){
        horizontalBarModel = new HorizontalBarChartModel();
        horizontalBarModel.setTitle(surveyBack.getCurrentSurveyQuestion().getQuestion());
        
        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Votes");
        
        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Choices");
    }
    
    /**
     * PrimeFaces event handler for a wizard module.
     * 
     * @param event A FlowEvent object.
     * @return The next step of the module.
     */
    public String onFlowProcess(FlowEvent event) {
        if(skip) {
            skip = false;   //Reset in case user goes back
            return "confirm";
        }
        else
        {
            return event.getNewStep();
        }
    }

    public SurveyQuestions getCurrentSurveyQuestion()
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<SurveyQuestions> rt = cq.from(SurveyQuestions.class);
        cq.where(cb.equal(rt.get(""), true));
        cq.select(rt);
        TypedQuery<SurveyQuestions> query = em.createQuery(cq);

        return query.getSingleResult();
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
     * Set the survey choices for a survey question.
     * 
     * @return The list of SurveyChoices entities.
     */
    private List<SurveyChoices> setSurveyChoices(){
        List<SurveyChoices> tempList = new ArrayList<>();
        List<String> choiceStrings = new ArrayList<>();
        SurveyChoices tempChoice;
        choiceStrings.add(choice1);
        choiceStrings.add(choice2);

        if (choice3 != null && !choice3.isEmpty())
        {
            choiceStrings.add(choice3);
        }

        if (choice4 != null && !choice4.isEmpty())
        {
            choiceStrings.add(choice4);
        }

        for (String s : choiceStrings)
        {
            tempChoice = new SurveyChoices();
            tempChoice.setChoice(s);
            tempList.add(tempChoice);
            tempChoice = null;
        }

        return tempList;
    }
    
    /**
     * Unset parameters after completion of create.
     */
    private void unsetParams(){
        this.question = "";
        this.choice1 = "";
        this.choice2 = "";
        this.choice3 = "";
        this.choice4 = "";
        newSurveyQuestion.setSurveyChoicesList(null);
        newSurveyQuestion = new SurveyQuestions();
    }
    
    /**
     * An action method that creates a new SurveyQuestions entity along with
     * new SurveyChoices entities after a successful button click.
     * 
     * @throws IOException if there is an error redirecting/dispatching to a 
     *                     page.
     */
    public void save() throws IOException{
        try{
            newSurveyQuestion.setQuestion(question);
            newSurveyQuestion.setCurrentQuestion(false);
            surveyQuestionController.create(newSurveyQuestion);
            List<SurveyChoices> tempList = setSurveyChoices();
            for (SurveyChoices sc : tempList)
            {
                sc.setQuestionId(newSurveyQuestion);
                sc.setVote(0);
                surveyChoiceController.create(sc);
            }
            newSurveyQuestion.setSurveyChoicesList(tempList);
            tempList = null;
            unsetParams();
        }
        catch (Exception e)
        {
            Logger.getLogger(SurveyActionBean.class.getName()).log(Level.SEVERE, null, e);
            return;
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("manager.xhtml");
    }
    
    /**
     * Action method that sets a survey to be displayed on button click.
     */
    public void setCurrentSurvey(){ 
        try {
            List<SurveyQuestions> surveys = surveyQuestionController.findSurveyQuestionsEntities();
            for (SurveyQuestions s : surveys)
            {
                if (!s.equals(selected))
                {
                    if (s.getCurrentQuestion() == true)
                    {
                        s.setCurrentQuestion(false);
                        surveyQuestionController.edit(s);
                    }
                }
            }
            selected.setCurrentQuestion(true);
            surveyQuestionController.edit(selected);
            selected = null;
            disabled = true;
        } catch (Exception e) {
            Logger.getLogger(SurveyActionBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void increment(){
        try{
            selectedChoice.setVote(selectedChoice.getVote() + 1);
            surveyChoiceController.edit(selectedChoice);
            selectedChoice = new SurveyChoices();
            ChartSeries choicesBar = new ChartSeries();
            for(SurveyChoices sc : surveyBack.getCurrentSurveyQuestion().getSurveyChoicesList())
                choicesBar.set(sc.getChoice(), sc.getVote());
            horizontalBarModel.addSeries(choicesBar);
            rendered = true;
            renderedSurvey = false;
        }
        catch (Exception e) {
            Logger.getLogger(SurveyActionBean.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
