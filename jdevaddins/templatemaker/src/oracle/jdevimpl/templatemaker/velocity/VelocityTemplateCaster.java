/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is TemplateMaker addin for Oracle9i JDeveloper
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package oracle.jdevimpl.templatemaker.velocity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.ParseErrorException;

import oracle.ide.addin.Context;
import oracle.ide.net.URLFileSystem;
import oracle.ide.util.Assert;

import oracle.jdevimpl.templatemaker.TemplateCaster;
import oracle.jdevimpl.templatemaker.TemplateCastFailedException;

/**
 * Template caster that uses the Jakarta Velocity template engine to 
 * cast the template.
 *
 * The following context references are set:
  <p>
  <b><tt>oracleide.destination.url</tt></b>:
      The URL of the destination file <br>
      
  <b><tt>oracleide.destination.path</tt></b>:
      The path of the destination file including its filename <br>
      
  <b><tt>oracleide.destination.file</tt></b>:
      The filename of the destination file <br>
      
  <b><tt>oracleide.destination.nameWithoutExtension</tt></b>:
      The filename of the destination file with its extension removed <br>
      
  <b><tt>oracleide.template.url</tt></b>:
      The URL of the template file <br>
      
  <b><tt>oracleide.template.path</tt></b>:
      The path of the template file including its filename <br>
      
  <b><tt>oracleide.template.file</tt></b>:
      The filename of the template file <br>
      
  <b><tt>oracleide.template.nameWithoutExtension</tt></b>:
      The filename of the template file with its extension removed <br>  
      
 *
 * @author Brian.Duff@oracle.com
 * @version $Revision: 1.2 $
 */
public class VelocityTemplateCaster implements TemplateCaster
{
  private final VelocityEngine m_velocityEngine;

  private static final String IDE_KEY="oracleide";

  public VelocityTemplateCaster()
  {
    m_velocityEngine = new VelocityEngine();

    m_velocityEngine.setProperty(
      "resource.loader", "file,url"
    );
    m_velocityEngine.setProperty(
      "url.resource.loader.class", 
      URLResourceLoader.class.getName()
    );
    m_velocityEngine.setProperty(
      "url.resource.loader.description", 
      "Oracle IDE URL Filesystem Resource Loader"
    );
    m_velocityEngine.setProperty(
      "runtime.log.logsystem.class",
      IDELogSystem.class.getName()
    );
    
    try
    {
      m_velocityEngine.init(); 
    }
    catch (Exception e)
    {
      Assert.fail( e );
    }
  }

  /**
   * Construct a velocity context from an IDE context
   *
   * @param ideContext the IDE context
   * @param templateURL the URL of the template
   * @param destinationURL the URL of the destination
   */
  private org.apache.velocity.context.Context getVelocityContext(
    final Context ideContext, final URL templateURL, final URL destinationURL )
  {
    org.apache.velocity.context.Context vContext = new VelocityContext();

    vContext.put( IDE_KEY, 
      new VelocityContextObject( ideContext, templateURL, destinationURL )
    );
    
    return vContext;
  }

// ----------------------------------------------------------------------------
// TemplateCaster implementation
// ----------------------------------------------------------------------------

  public void castTemplate( final Context context, final URL templateURL, 
      final URL destinationURL )
    throws TemplateCastFailedException
  {
    if ( !"file".equals(templateURL.getProtocol()) )
    {
      throw new TemplateCastFailedException(
        "Only file: URLs are supported for velocity templates"
      );
    }

    try
    {
      Template template = m_velocityEngine.getTemplate(
        templateURL.toString()
      );
      org.apache.velocity.context.Context velContext = 
        getVelocityContext( context, templateURL, destinationURL );

      OutputStream os = URLFileSystem.openOutputStream( destinationURL );
      OutputStreamWriter osWriter = new OutputStreamWriter( os );
      template.merge( velContext, osWriter );
      osWriter.flush();
      osWriter.close();
    }
    catch ( ResourceNotFoundException rnfe )
    {
      throw new TemplateCastFailedException(
        "The template could not be found", rnfe
      );
    }
    catch ( ParseErrorException pee )
    {
      throw new TemplateCastFailedException(
        "The template could not be parsed by the velocity template engine",
        pee
      );
    }
    catch ( IOException ioe )
    {
      throw new TemplateCastFailedException(
        "An I/O Error occurred writing the template",
        ioe
      );
    }
    catch ( Exception ex )
    {
      throw new TemplateCastFailedException(
        "An error occurred initializing the template", ex
      );
    }
  }
}