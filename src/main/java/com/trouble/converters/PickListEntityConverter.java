
package com.trouble.converters;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.WeakHashMap;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Generic weak hashmap-based entity converter (Widely used for PickList).
 * 
 * @author Seaim Khan
 */
@FacesConverter("pickListEntityConverter")
public class PickListEntityConverter implements Converter {

    private static Map<Object, String> entities = new WeakHashMap<Object, String>();
    
    /**
     * Overridden getAsObject method for an entity meant for input.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @return The entity object.
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (Entry<Object, String> entry : entities.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    /**
     * Overridden getAsString method for an entity meant for output.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param entity The entity as an Object.
     * @return The entity as a String
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object entity) {
        synchronized (entities) {
            if (!entities.containsKey(entity)) {
                String value = UUID.randomUUID().toString();
                entities.put(entity, value);
                return value;
            } else {
                return entities.get(entity);
            }
        }
    }
}