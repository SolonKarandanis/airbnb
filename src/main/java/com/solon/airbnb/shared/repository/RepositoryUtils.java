/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solon.airbnb.shared.repository;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author kostas
 */
public class RepositoryUtils {

    public static final String ORDER_PARAM = "order";
    public static final String PARAMETER_ALIAS = "filter";
    public static final String IGNORE_FILTER = "ignore_filter";

    public static enum SORTING_DIRECTIONS {
        ASC, DESC
    }

    private static String FormatDouble(double input, String format) {
        NumberFormat formatter = new DecimalFormat(format);
        return formatter.format(input);
    }

    public static String getMAX(EntityManager em, String table, String column, int length) throws Exception {
        String format = "";
        for (int i = 0; i < length; i++) {
            format += "0";
        }
        Query sql_max = em.createQuery("SELECT MAX(o." + column + ") FROM " + table + " o");
        Object oResult = sql_max.getSingleResult();
        if (oResult == null) {
            oResult = format;
        }
        double dmax = Double.parseDouble(oResult.toString());
        dmax++;
        return FormatDouble(dmax, format);
    }

    public static String getMAX(EntityManager em, String table, String column, int length, String where) throws Exception {
        String format = "";
        for (int i = 0; i < length; i++) {
            format += "0";
        }
        Query sql_max = em.createQuery("SELECT MAX(o." + column + ") FROM " + table + " o WHERE " + where);
        Object oResult = sql_max.getSingleResult();
        if (oResult == null) {
            oResult = format;
        }
        double dmax = Double.parseDouble(oResult.toString());
        dmax++;
        return FormatDouble(dmax, format);
    }

    /**
     * Returns and ArrayList with StatementParameter elements.
     *
     * @param ejb The EJB instance.
     * @throws java.lang.Exception Route exception all the way up.
     * @return the ArrayList StatementParameter elements
     */
    public static ArrayList<StatementParameter> getDirty(Object ejb) throws Exception {
        ArrayList<StatementParameter> retValue = new ArrayList<StatementParameter>();
        PropertyDescriptor[] pds = getProperties(ejb);
        StatementParameter parameter = null;
        for (int i = 0; i < pds.length; i++) {
            // Get property name
            String propName = pds[i].getName();
            //Introspector returns and the name of the class
            //and we do not want that.
            //if((!propName.equalsIgnoreCase("class")) && (propName.indexOf("_") == -1)){
            if ((!propName.equalsIgnoreCase("class")) && (!propName.endsWith("Transient"))) {
                //if a property ends with PK, it means
                //that the property is a Primary Key class object
                //and we need to inspect that object too.
                if (propName.endsWith("PK")) {
                    Object pk = PropertyUtils.getProperty(ejb, pds[i].getName());//getPropertyValue(ejb, pds[i]);
                    PropertyDescriptor[] pk_pds = getProperties(pk);
                    for (int k = 0; k < pk_pds.length; k++) {
                        String pk_propName = pk_pds[k].getName();
                        if (!pk_propName.equalsIgnoreCase("class")) {
                            StatementParameter pk_parameter = generateParameter(pk, pk_pds[k]);
                            if (pk_parameter != null) {
                                pk_parameter.setName(pk_parameter.getName());
                                pk_parameter.setColumn(propName + "." + pk_parameter.getName());
                                retValue.add(pk_parameter);
                            }
                        }
                    }
                } else {
                    parameter = generateParameter(ejb, pds[i]);
                }
                if (parameter != null) {
                    retValue.add(parameter);
                }
            }
        }
        return retValue;
    }

    private static StatementParameter generateParameter(Object ejb, PropertyDescriptor property) throws Exception {
        StatementParameter retValue = null;
        String propName = property.getName();
        Object propValue = PropertyUtils.getProperty(ejb, propName); //getPropertyValue(ejb, property);
        if (propValue != null) {
            if (propValue instanceof java.lang.String) {
                if (propValue.toString().trim().length() > 0) {
                    retValue = new StatementParameter(propName, propValue, "LIKE", propName, property.getPropertyType());
                }
            } else {
                retValue = new StatementParameter(propName, propValue, "=", propName, property.getPropertyType());
            }
        }
        return retValue;
    }

    public static PropertyDescriptor[] getProperties(Object ejb) throws Exception {
        BeanInfo bi = Introspector.getBeanInfo(ejb.getClass());
        return bi.getPropertyDescriptors();
    }

    /*public static Object getPropertyValue(Object ejb, PropertyDescriptor property) throws Exception {
     //Construct getter
     String getterName = property.getReadMethod().getName();
     Expression expr = new Expression(ejb, getterName, new Object[0]);
     //call getter
     expr.execute();
     //read result value
     return expr.getValue();
     }*/
    public static String serializeObject(Object obj) throws UnsupportedEncodingException, Exception {
        String retValue = null;
        if (obj == null) {
            throw new Exception("Object is null!");
        }
        // Create output stream.
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            // Create XML encoder.
            XMLEncoder xenc = new XMLEncoder(bos);
            // Write object.
            xenc.writeObject(obj);
            xenc.flush();
            retValue = bos.toString("UTF-8");

        } catch (UnsupportedEncodingException ex) {
            //Logger.getLogger(ejbUtils.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnsupportedEncodingException(ex.getMessage());
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                //Logger.getLogger(ejbUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return retValue;
    }

    public static Object deserializeObject(String xml) throws UnsupportedEncodingException, Exception {
        Object retValue = null;
        ByteArrayInputStream bis = null;
        if (xml == null) {
            throw new Exception("XML string is null!!");
        }
        if (xml.trim().length() == 0) {
            throw new Exception("XML string is empty!!");
        }
        try {
            bis = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            // Create XML encoder.
            XMLDecoder xdec = new XMLDecoder(bis);
            // Write object.
            retValue = xdec.readObject();
        } catch (UnsupportedEncodingException ex) {
        } catch (Exception ex) {
            //Logger.getLogger(ejbUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                //Logger.getLogger(ejbUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return retValue;
    }

    public static Object clone(Object obj) throws UnsupportedEncodingException, Exception {
        Object retValue = null;
        ByteArrayInputStream bis = null;
        if (obj == null) {
            throw new Exception("Object is null!");
        }
        // Create output stream.
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            // Create XML encoder.
            XMLEncoder xenc = new XMLEncoder(bos);
            // Write object.
            xenc.writeObject(obj);
            xenc.flush();
            retValue = bos.toString("UTF-8");
            bis = new ByteArrayInputStream(retValue.toString().getBytes("UTF-8"));
            // Create XML encoder.
            XMLDecoder xdec = new XMLDecoder(bis);
            // Write object.
            retValue = xdec.readObject();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RepositoryUtils.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnsupportedEncodingException(ex.getMessage());
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                Logger.getLogger(RepositoryUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return retValue;

    }
}
