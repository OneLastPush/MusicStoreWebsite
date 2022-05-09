package com.trouble.backingbean;

import com.trouble.entities.Labels;
import com.trouble.JPAController.LabelsJpaController;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Frank
 */
@Named("labelBacking")
@RequestScoped
public class LabelBackingBean implements Serializable
{

    @Inject
    private LabelsJpaController labelsJpaController;

    private Labels label;

    /**
     * Get the entity if not instantiated, it will
     *
     * @return label -entity
     */
    public Labels getLabel()
    {
        if (label == null)
        {
            label = new Labels();
        }
        return label;
    }

    /**
     * Get list of label
     *
     * @return
     */
    public List<Labels> getAllLabels()
    {
        return labelsJpaController.findLabelsEntities();
    }

    /**
     * Get label name from an id
     *
     * @param labelId
     *
     * @return
     */
    public String getLabelName(int labelId)
    {
        return labelsJpaController.findLabels(labelId).getLabelName();
    }
}
