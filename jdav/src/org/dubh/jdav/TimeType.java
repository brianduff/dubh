// $Id: TimeType.java,v 1.1.1.1 2000-09-17 17:38:13 briand Exp $
// Copyright (c) 2000 Brian Duff 
package org.dubh.jdav;

/**
 * Represents a time type as specified in section 9.8 of RFC2518
 * <P>
 * @author Brian Duff (<a href="mailto:brian@dubh.org">brian@dubh.org<a>)
 */
public class TimeType
{

   private static TimeType s_infiniteInstance;

   private TimeTypeType m_type;

   private int m_seconds;
   private String m_extendValue;

   public static class TimeTypeType {
      private TimeTypeType() {}
   }

   /**
    * Indicates that the time type is second. getSeconds() returns the number
    * of seconds this time type represents. getExtendValue() is undefined
    */
   public static TimeTypeType SECONDS = new TimeTypeType();

   /**
    * Indicates that the time type is infinite. getSeconds() and
    * getExtendValue() are undefined
    */
   public static TimeTypeType INFINITE = new TimeTypeType();

   /**
    * Indicates that the time type is extend. getExtendValue() returns the
    * value of the extension time type. getSeconds() is undefined
    */
   public static TimeTypeType EXTEND = new TimeTypeType();

   /**
    * Construct a time type using the given specifier string. The specifier
    * string is of the form: <pre>
    *   TimeType = ("Second-" DAVTimeOutVal | "Infinite" | Other)
    *   DAVTimeOutVal = 1*digit
    *   Other = "Extend" field-value [see section 4.2 of RFC2068]
    * </pre>
    */
   private TimeType(String specifier)
      throws DAVException
   {
      specifier = specifier.trim();
      if (specifier.startsWith("Second-"))
      {
         String numericBit = specifier.substring(7);
         try
         {
            m_seconds = Integer.parseInt(numericBit);
            m_type = SECONDS;
         }
         catch (NumberFormatException nfe)
         {
            throw new DAVException(
               "Invalid value for TimeType Seconds: "+numericBit
            );
         }
      }
      else if (specifier.startsWith("Extend"))
      {
         m_extendValue = specifier.substring(6);   // should this be 7?
         m_type = EXTEND;
      }
      else
      {
         throw new DAVException(
            "Unrecognized time type specifier: "+specifier
         );
      }
   }

   /**
    * Private constructor; used by the get...Instance() methods
    */
   private TimeType(TimeTypeType type)
   {
      m_type = type;
   }

   /**
    * Get an instance of TimeType based on a specifier string of the form:
    * <pre>
    *   TimeType = ("Second-" DAVTimeOutVal | "Infinite" | Other)
    *   DAVTimeOutVal = 1*digit
    *   Other = "Extend" field-value [see section 4.2 of RFC2068]
    * </pre>
    * e.g. "Second-1234", "Infinite", "Extend whatever you like"
    */
   public static TimeType getInstance(String specifier)
      throws DAVException
   {
      if (specifier.startsWith("Infinite"))
      {
         return getInfiniteInstance();
      }
      else
      {
         return new TimeType(specifier);
      }
   }

   /**
    * Get an instance of TimeType that represents an infinite amount of time
    */
   public static TimeType getInfiniteInstance()
   {
      return new TimeType(INFINITE);
   }

   /**
    * Get an instance of TimeType that represents a particular number of
    * seconds.
    * @param s the number of seconds the instance should represent
    */
   public static TimeType getSecondInstance(int s)
   {
      TimeType t = new TimeType(SECONDS);
      t.m_seconds = s;
      return t;
   }

   /**
    * Get an instance of TimeType that represents an extended time time
    * @param s the extended string
    */
   public static TimeType getExtendedInstance(String s)
   {
      TimeType t = new TimeType(EXTEND);
      t.m_extendValue = s;
      return t;
   }

   /**
    * Get the number of seconds this time type represents. This is only
    * defined if getType() returns SECONDS
    */
   public int getSeconds()
   {
      return m_seconds;
   }

   /**
    * Get the extended value of this time type. This is only defined if
    * getType() returns EXTEND
    */
   public String getExtendedValue()
   {
      return m_extendValue;
   }

   /**
    * Get the type of this time type instance. This is one of SECONDS, INFINITE
    * or EXTEND.
    */
   public TimeTypeType getType()
   {
      return m_type;
   }
}

 