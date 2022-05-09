package com.trouble.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Seaim Khan
 */
@Entity
@Table(name = "survey_questions")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "SurveyQuestions.findAll", query = "SELECT s FROM SurveyQuestions s")
            , @NamedQuery(name = "SurveyQuestions.findById", query = "SELECT s FROM SurveyQuestions s WHERE s.id = :id")
            , @NamedQuery(name = "SurveyQuestions.findByQuestion", query = "SELECT s FROM SurveyQuestions s WHERE s.question = :question")
            , @NamedQuery(name = "SurveyQuestions.findByCurrentQuestion", query = "SELECT s FROM SurveyQuestions s WHERE s.currentQuestion = :currentQuestion")
        })
public class SurveyQuestions implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "question")
    private String question;
    @Column(name = "current_question")
    private Boolean currentQuestion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionId")
    private List<SurveyChoices> surveyChoicesList;

    public SurveyQuestions()
    {
    }

    public SurveyQuestions(Integer id)
    {
        this.id = id;
    }

    public SurveyQuestions(Integer id, String question)
    {
        this.id = id;
        this.question = question;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public Boolean getCurrentQuestion()
    {
        return currentQuestion;
    }

    public void setCurrentQuestion(Boolean currentQuestion)
    {
        this.currentQuestion = currentQuestion;
    }

    @XmlTransient
    public List<SurveyChoices> getSurveyChoicesList()
    {
        return surveyChoicesList;
    }

    public void setSurveyChoicesList(List<SurveyChoices> surveyChoicesList)
    {
        this.surveyChoicesList = surveyChoicesList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SurveyQuestions))
        {
            return false;
        }
        SurveyQuestions other = (SurveyQuestions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.trouble.entities.SurveyQuestions[ id=" + id + " ]";
    }

}
