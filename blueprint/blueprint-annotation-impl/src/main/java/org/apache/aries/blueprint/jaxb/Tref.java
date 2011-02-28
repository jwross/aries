//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.04.23 at 12:57:08 PM EDT 
//


package org.apache.aries.blueprint.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *                 
 *                 Tref is the type used for <ref> elements.  This specifies a required
 *                 component id for the reference component.
 *                 
 * 			
 * 
 * <p>Java class for Tref complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tref">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="component-id" use="required" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tidref" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tref", namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0")
@XmlRootElement(name = "ref")
public class Tref {

    @XmlAttribute(name = "component-id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String componentId;

    /**
     * Gets the value of the componentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * Sets the value of the componentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComponentId(String value) {
        this.componentId = value;
    }

}