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

package oracle.jdevimpl.templatemaker;

import java.awt.MediaTracker;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import oracle.ide.model.DeployableTextNode;
import oracle.ide.net.URLFileSystem;

/**
 * TemplateNode is a default text node used to represent a template file.
 * We use our own type of node rather than relying on the NodeFactory so
 * that the node is not compilable etc.
 *
 * @author Brian.Duff@oracle.com
 * @version $Revision: 1.2 $
 */
public class TemplateNode extends DeployableTextNode
{

  private final String m_name;
  private final static Icon s_icon;

  static
  {
    ImageIcon icon = new ImageIcon(
      TemplateNode.class.getResource( "template.png" )
    );
    if ( icon.getImageLoadStatus() == MediaTracker.COMPLETE )
    {
      s_icon = icon;
    }
    else
    {
      s_icon = null;
    }
  }

  /**
   * Construct a template node with the specified URL
   *
   * @param u the url of the template node
   */
  public TemplateNode( URL u )
  {
    super( u );
    m_name = URLFileSystem.getName( u );
  }

// ----------------------------------------------------------------------------
// DeployableTextNode overrides
// ----------------------------------------------------------------------------

  /**
   * Override this to just return the name without extension
   */
  public String getShortLabel()
  {
    return m_name;
  }

  /**
   * Override to provide a nice new icon
   */
  public Icon getIcon()
  {
    if ( s_icon != null )
    {
      return s_icon;
    }
    return super.getIcon();
  }
}