
// Copyright (c) 2000 
package org.dubh.apps.ewatch.type.file;

import org.dubh.apps.ewatch.type.*;
import org.dubh.apps.ewatch.*;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * A base class for File*EventType.
 *
 * @author Brian Duff
 */
public abstract class AbstractFileEventType extends AbstractEventType
{

   public AbstractFileEventType(String uniqueID)
   {
      super(uniqueID);
   }
   
   /**
    * Get an object that can be used when the user responds to an event. This
    * method may return null if there is no responder.
    */
   public EventResponder getEventResponder()
   {
      // This is / will be the only method that FET extends from the base class;
      // the responder for file events will always display the contents of the
      // file if it exists.
      return null; //TODO
   }
}

