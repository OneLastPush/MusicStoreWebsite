package com.trouble.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Seaim Khan
 */
@Entity
@Table(name = "survey_choices")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "SurveyChoices.findAll", query = "SELECT s FROM SurveyChoices s")
            , @NamedQuery(name = "SurveyChoices.findById", query = "SELECT s FROM SurveyChoices s WHERE s.id = :id")
            , @NamedQuery(name = "SurveyChoices.findByChoice", query = "SELECT s FROM SurveyChoices s WHERE s.choice = :choice")
            , @NamedQuery(name = "SurveyChoices.findByVote", query = "SELECT s FROM SurveyChoices s WHERE s.vote = :vote")
        })
public class SurveyChoices implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "choice")
    private String choice;
    @Column(name = "vote")
    private Integer vote;
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SurveyQuestions questionId;

    public SurveyChoices()
    {
    }

    public SurveyChoices(Integer id)
    {
        this.id = id;
    }

    public SurveyChoices(Integer id, String choice)
    {
        this.id = id;
        this.choice = choice;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getChoice()
    {
        return choice;
    }

    public void setChoice(String choice)
    {
        this.choice = choice;
    }

    public Integer getVote()
    {
        return vote;
    }

    public void setVote(Integer vote)
    {
        this.vote = vote;
    }

    public SurveyQuestions getQuestionId()
    {
        return questionId;
    }

    public void setQuestionId(SurveyQuestions questionId)
    {
        this.questionId = questionId;
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
        if (!(object instanceof SurveyChoices))
        {
            return false;
        }
        SurveyChoices other = (SurveyChoices) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.trouble.entities.SurveyChoices[ id=" + id + " ]";
    }

}
