/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1;

/**
 *
 * @author FOR ORACLE
 */
public class PropertyValue {

    private final String propertyName;
    
    private final String propertyValue;
    private final String Description;

    public PropertyValue(String propertyName, String propertyValue, String Description) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
        this.Description = Description;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public String getDescription() {
        return Description;
    }
    
    
}
