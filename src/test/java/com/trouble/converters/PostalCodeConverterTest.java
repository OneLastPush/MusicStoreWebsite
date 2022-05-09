/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trouble.converters;

import java.util.regex.Pattern;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Max
 */
public class PostalCodeConverterTest
{

    public PostalCodeConverterTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of getAsObject method, of class PostalCodeConverter.
     */
    @Test
    public void testGetAsObject()
    {
        System.out.println("getAsObject");
        FacesContext context = null;
        UIComponent component = null;
        String value = "h9s2m8";
        PostalCodeConverter instance = new PostalCodeConverter();
        Object expResult = "H9S2M8";
        Object result = instance.getAsObject(context, component, value);
        assertEquals(expResult, result);

    }

    /**
     * Test of getAsObject method, of class PostalCodeConverter.
     */
    @Test
    public void testGetAsObjectInvalidChar()
    {
        System.out.println("getAsObject");
        FacesContext context = null;
        UIComponent component = null;
        String value = "h9s2m/8";
        PostalCodeConverter instance = new PostalCodeConverter();
        Object expResult = null;
        Object result = instance.getAsObject(context, component, value);
        assertEquals(expResult, result);

    }

    /**
     * Test of getAsObject method, of class PostalCodeConverter.
     */
    @Test
    public void testGetAsObjectWSpace()
    {
        System.out.println("getAsObject");
        FacesContext context = null;
        UIComponent component = null;
        String value = "h9s 2m8";
        PostalCodeConverter instance = new PostalCodeConverter();
        Object expResult = "H9S2M8";
        Object result = instance.getAsObject(context, component, value);
        assertEquals(expResult, result);

    }

    /**
     * Test of getAsString method, of class PostalCodeConverter.
     */
    @Test
    public void testGetAsString()
    {
        System.out.println("getAsString");
        FacesContext context = null;
        UIComponent component = null;
        Object value = "H9S2M8";
        PostalCodeConverter instance = new PostalCodeConverter();
        String expResult = "H9S 2M8";
        String result = instance.getAsString(context, component, value);
        assertEquals(expResult, result);

    }

    /**
     * Test of getAsString method, of class PostalCodeConverter.
     */
    @Test
    public void testGetAsStringRegex()
    {
        System.out.println("getAsString");
        FacesContext context = null;
        UIComponent component = null;
        Object value = "h9s2m8";
        PostalCodeConverter instance = new PostalCodeConverter();
        String expResult = "H9S 2M8";
        String result = instance.getAsString(context, component, value);
        //assertEquals(expResult, result);
        Boolean tr = Pattern.matches("([ABCEGHJKLMNPRSTVXY][0-9][ABCEGHJKLMNPRSTVWXYZ] ?[0-9][ABCEGHJKLMNPRSTVWXYZ][0-9])", result);
        assertTrue(tr);
    }

}
