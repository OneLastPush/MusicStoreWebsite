package com.trouble.validators;

import com.trouble.JPAController.SurveyQuestionsJpaController;
import com.trouble.entities.SurveyQuestions;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * A Validator class that checks if a survey is unique.
 * 
 * @author Seaim Khan
 */
@Named
public class SurveyQuestionValidator implements Validator
{

    @Inject
    private SurveyQuestionsJpaController sqc;

    /**
     * Validates uniqueness of question.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @throws ValidatorException if the survey already exists.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        String question = (String) value;
        List<SurveyQuestions> invalid = sqc.findByQuestion(question);
        if (!invalid.isEmpty())
        {
            System.out.print("NOTUNIQUE: Question already exists!");
            throw new ValidatorException(new FacesMessage("This question already exists!"));
        }
    }

}
