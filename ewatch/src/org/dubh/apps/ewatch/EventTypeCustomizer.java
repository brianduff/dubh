
// Copyright (c) 2000 
package org.dubh.apps.ewatch;

import javax.swing.JComponent;

/**
 * An object implementing the EventTypeCustomizer can be used to display a UI
 * that customizes an instance of an event type.
 * <P>
 * @author Brian Duff
 */
public interface EventTypeCustomizer
{
   /**
    * You must provide a lightweight swing component that can be used to
    * customize the event type.
    */
   public JComponent getComponent();

   /**
    * You should initialize your UI component based on the specified event
    * type.
    *
    * @throws java.lang.Exception if the contents of the event type are in
    * some way invalid and the event type could not be reverted.
    */
   public void revert(EventType etype) throws Exception;

   /**
    * You should modify the event type based on the contents of the UI.
    *
    * @param etype The event type that should be modified based on the
    * current contents of the UI.
    * @throws java.lang.Exception if the UI failed to validate. The event
    *   type should be unchanged in this instance. The message of the
    *   exception will be displayed in a dialog to the user and they will
    *   have the opportunity to correct any problems and resave.
    */
   public void save(EventType etype) throws Exception;
}

 