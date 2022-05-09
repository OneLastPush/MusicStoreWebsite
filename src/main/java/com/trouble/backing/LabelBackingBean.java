package com.trouble.backing;

import com.trouble.JPAController.LabelsJpaController;
import com.trouble.entities.Labels;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Backing Bean for a Labels entity.
 * 
 * @author Seaim Khan
 */
@Named("theLabels")
@SessionScoped
public class LabelBackingBean implements Serializable
{

    @Inject
    private LabelsJpaController labelsJpaController;
    
    /**
     * Gets a list of all labels.
     * 
     * @return The list of all Labels entities.
     */
    public List<Labels> getLabels(){
        return labelsJpaController.findLabelsEntities();
    }
    
    /**
     * A complete method for PrimeFaces autocomplete that finds recording labels
     * that start with a particular input.
     * 
     * @param query The inputted query.
     * @return The list of suggested Labels entities based on matching input.
     */
    public List<Labels> completeLabel(String query){
        List<Labels> labels = getLabels();
        List<Labels> suggested = new ArrayList<>();
        for (Labels l : labels)
        {
            if (l.getLabelName().startsWith(query))
            {
                suggested.add(l);
            }
        }
        return suggested;
    }
}
