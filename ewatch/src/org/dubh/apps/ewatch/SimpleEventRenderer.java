package org.dubh.apps.ewatch;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * The SimpleEventRenderer is an implementation of the EventRenderer interface
 * that displays the event as a simple label. The label's font and color can
 * be configured using event attributes. 
 * <P>
 * @author Brian Duff
 */
public class SimpleEventRenderer implements EventRenderer
{
   /**
    * If this attribute is defined in your event, the value must be a
    * Font object, which is used to set the font for the displayed event.
    * If your event doesn't have this attribute, the default font
    * (dialog 12 plain) is used.
    */
   public static final SERAttribute FONT_ATTRIBUTE = new SERAttribute();

   /**
    * If this attribute is defined in your event, the value must be a Color
    * object, which is used to set the foreground color of the displayed event.
    * If your event doesn't have this attribute, the default color (Color.black)
    * is used.
    */
   public static final SERAttribute COLOR_ATTRIBUTE = new SERAttribute();

   /**
    * If this attribute is defined in your event, the value must be an Icon
    * object, which is used to set the icon displayed to the left of the
    * event. If your event doesn't have this attribute, no icon is displayed.
    */
   public static final SERAttribute ICON_ATTRIBUTE = new SERAttribute();

   /**
    * Get a component used to display the event.
    */
   public Component getEventRendererComponent(Event e)
   {
      // Could store this and reset its properties (?)
      JLabel theLabel = new JLabel();

      String label = null;

      label = e.getName();
      if (label == null || label.trim().length() == 0)
      {
         // Try getting type name
         String typeName = e.getEventType().getName();
         if (typeName == null || typeName.trim().length() == 0)
         {
            typeName = e.getEventType().getClass().getName();
         }
         label = "Untitled Event (Type: "+typeName+")";

      }
      theLabel.setText(e.getName());

      // Try to get the font, color and icon.
      Font font = null;
      try
      {
         font = (Font)e.getAttribute(FONT_ATTRIBUTE);
      }
      catch (ClassCastException cce) {}

      if (font == null)
      {
         font = new Font("dialog", Font.PLAIN, 12);
      }

      Color color = null;
      try
      {
         color = (Color)e.getAttribute(COLOR_ATTRIBUTE);
      }
      catch (ClassCastException cce) {}

      if (color == null)
      {
         color = Color.black;
      }

      Icon icon = null;
      try
      {
         icon = (Icon)e.getAttribute(ICON_ATTRIBUTE);
      }
      catch (ClassCastException cce) {}

      theLabel.setFont(font);
      theLabel.setForeground(color);
      if (icon != null)
      {
         theLabel.setIcon(icon);
      }

      return theLabel;

   }

   /**
    * Typed enumerator for attributes used by the SER
    */
   static final class SERAttribute {}
}

 