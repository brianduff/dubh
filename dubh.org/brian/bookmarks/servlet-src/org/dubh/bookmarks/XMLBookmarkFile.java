package org.dubh.bookmarks;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * An implementation of BookmarkFile that is based on an XML file. You
 * cannot instantiate this class. Instead call newInstance(String) which
 * will return a BookmarkFile.
 * <P>
 * @author Brian Duff
 */
class XMLBookmarkFile implements BookmarkFile
{
   private Document m_document;
   private File m_file;

   // Make sure this contains the same characters as its equivalent in
   // bookmarks.xsl
   private static final String BAD_CHARS = " ().,-&/?:";

   private final static boolean DIAGNOSTICS = true;
   
   private XMLBookmarkFile(String file)
      throws BookmarkFileException
   {
      try
      {
         m_file = new File(file);

         InputStream is = new FileInputStream(m_file);

         DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         m_document = db.parse(new InputSource(is));

      }
      catch (SAXException saxe)
      {
         throw new BookmarkFileException(
            "Failed to parse XML from "+file+": "+saxe.getMessage(),
            saxe
         );
      }
      catch (IOException ioe)
      {
         throw new BookmarkFileException(
            "Failed to read XML from "+file+": "+ioe.getMessage(),
            ioe
         );
      }
      catch (ParserConfigurationException pce)
      {
         throw new BookmarkFileException(
            "XML Parser configuration error: "+pce.getMessage(),
            pce
         );
      }
   }

   /**
    * Construct a new instance of a bookmark file, based on the specified
    * source XML file.
    *
    * @param xmlFilename the xml file to load
    * @return a new BookmarkFile
    */
   public static BookmarkFile newInstance(String xmlFilename)
      throws BookmarkFileException
   {
      return new XMLBookmarkFile(xmlFilename);
   }

   /**
    * get the xml document
    */
   private Document getDocument()
   {
      return m_document;
   }

   private String removeChars(String baseString, String badChars)
   {
      StringBuffer sbResult = new StringBuffer(baseString.length());

      for (int i=0; i < baseString.length(); i++)
      {
         char thisChar = baseString.charAt(i);
         // If the character is not in badChars
         if (badChars.indexOf(thisChar) == -1)
         {
            // Append it to the result string.
            sbResult.append(thisChar);
         }
      }

      return sbResult.toString();
      
   }

   /**
    * Convert a title to an id by stripping out undesirable characters
    */
   private String titleToId(String title)
   {
      return removeChars(title, BAD_CHARS);
   }

   /**
    * Find the specified item.
    */
   private Element findItem(String itemId, Element startElement)
   {
      // (DOM level 2 only)
      if (startElement.hasAttribute("title"))
      {
         if (titleToId(startElement.getAttribute("title")).equals(itemId))
         {
            return startElement;
         }
      }

      NodeList nl = startElement.getElementsByTagName("*");

      for (int i=0; i < nl.getLength(); i++)
      {
         Element found = findItem(itemId, (Element)nl.item(i));
         if (found != null)
         {
            return found;
         }
      }

      return null;
   }

   /**
    * Find an item, starting at the root
    */
   private Element findItem(String itemId)
      throws BookmarkFileException
   {
      Element e = findItem(itemId, m_document.getDocumentElement());
      if (e == null)
      {
         throw new BookmarkFileException(
            "Failed to find element with id "+itemId
         );
      }
      return e;
   }

   /**
    * Write the XML file back out
    */
   private void persistXML()
      throws BookmarkFileException
   {
      try
      {
         XMLSerializer xmls = new XMLSerializer(//new OutputFormat(
         //   "XML",
         //   "UTF8",
         //   true
         //)
         );
         OutputStream os = new FileOutputStream(m_file);
         xmls.setOutputByteStream(os);
         xmls.asDOMSerializer().serialize(m_document);

         os.flush();
         os.close();
      }
      catch (IOException ioe)
      {
         throw new BookmarkFileException(
            "Failed to serialize bookmarks to "+m_file,
            ioe
         );
      }
   }

   /////////////////////////////////////////////////////////////////////////////
   // BookmarkFile interface

   /**
    * Delete an item from the bookmark file.
    */
   public void deleteItem(String itemId) throws BookmarkFileException
   {
      Element foundItem = findItem(itemId);

      try
      {
         foundItem.getParentNode().removeChild(foundItem);
      }
      catch (DOMException dome)
      {
         throw new BookmarkFileException(
            "Failed to remove "+itemId,
            dome
         );
      }

      persistXML();
   }


   /**
    * Rename an item in the bookmark file.
    */
   public void renameItem(String oldId, String newName)
      throws BookmarkFileException
   {

   }

   /**
    * Add a category to the bookmark file
    *
    * @param parentCategory the id of the parent category. If null, the
    *    new category is added to the top level
    * @param categoryName the name of the new category
    */
   public void addCategory(String parentCategory, String categoryName)
      throws BookmarkFileException
   {

   }

   /**
    * Add a link to the bookmark file
    *
    * @param parentCategory the id of the category in which to place
    *    the bookmark
    * @param linkName the name of the link
    * @param linkURL the URL of the link
    */
   public void addLink(String parentCategory, String linkName, String linkURL)
      throws BookmarkFileException
   {

   }

   private void diagnostics(String txt)
   {
      if (DIAGNOSTICS)
      {
         System.out.print("XMLBookmarkFile Diagnostics: ");
         System.out.println(txt);
      }
   }

   /**
    * Get the index of an element in its parent.
    *
    * @return the node index, or -1 if the node couldn't be found in
    *    its parent (should never happen)
    */
   private int getIndexOf(Node child)
   {
      Node parent = child.getParentNode();

      NodeList nl = parent.getChildNodes();
      int nodeIndex = -1;
      for (int i=0; i < nl.getLength(); i++)
      {
         if (nl.item(i) == child)
         {
            nodeIndex = i;
            break;
         }
      }

      return nodeIndex;
   }

   /**
    * Move a link one position earlier in its category
    *
    * @param linkId the link to move
    */
   public void moveLinkUp(String link)
      throws BookmarkFileException
   {
      Element found = findItem(link);

      int nodeIndex = getIndexOf(found);
      if (nodeIndex == -1)
      {
         throw new BookmarkFileException(
            "Could not determine index of "+link
         );
      }

      Node predecessor = null;
      // Now, find a predecessor element of the correct type.
      Node parent = found.getParentNode();
      NodeList nl = parent.getChildNodes();      
      for (int i=nodeIndex-1; i >= 0; i--)
      {
         Node candidate = nl.item(i);
         if (candidate.getNodeType() == Node.ELEMENT_NODE)
         {
            if (((Element)candidate).getTagName().equals("link"))
            {
               predecessor = candidate;
               break;
            }
         }
      }

      if (predecessor == null)
      {
         throw new BookmarkFileException(
            "Link is already at the top of its category: "+link
         );
      }

      parent.removeChild(found);
      parent.insertBefore(found, predecessor);

      persistXML();

   }

   /**
    * Move a link one position later in its category
    *
    * @param link the link to move
    */
   public void moveLinkDown(String link)
      throws BookmarkFileException
   {
      Element found = findItem(link);

      int nodeIndex = getIndexOf(found);
      if (nodeIndex == -1)
      {
         throw new BookmarkFileException(
            "Could not determine index of "+link
         );
      }

      boolean foundFirst = false;
      Node beforeMe = null;
      // We need to find a node to reinsert before. This means we have
      // to find the node two indices later than the found node.
      Node parent = found.getParentNode();
      NodeList nl = parent.getChildNodes();

      for (int i=nodeIndex+1; i < nl.getLength(); i++ )
      {
         Node candidate = nl.item(i);
         if (candidate.getNodeType() == Node.ELEMENT_NODE)
         {
            if (((Element)candidate).getTagName().equals("link"))
            {
               if (foundFirst)
               {
                  // We found the second successor.
                  // Cool, lets quit.
                  beforeMe = candidate;
                  break;
               }
               else
               {
                  // We found the first successor. Keep looking for the
                  // second successor.
                  foundFirst = true;
               }
            }
         }
      }

      if (foundFirst)
      {
         // The item is not at the bottom, but there is no item to
         // insert before. we simply remove the item and append it at the
         // end
         if (beforeMe == null)
         {
            parent.removeChild(found);
            parent.appendChild(found);
         }
         else
         {
            parent.removeChild(found);
            parent.insertBefore(found, beforeMe);
         }
      }
      else
      {
         throw new BookmarkFileException(
            "Link is already at the top of its category: "+link
         );
      }

      persistXML();
   }

   /**
    * Change the URL of a link
    *
    * @param linkId
    * @param newURL
    */
   public void changeLinkURL(String linkId, String newURL)
      throws BookmarkFileException
   {

   }

   /**
    * Is the given item a category?
    */
   public boolean isCategory(String id)
      throws BookmarkFileException
   {
      Element e = findItem(id);

      return (e.getTagName().equals("category"));
   }

   /**
    * Get the unescaped name of the item
    */
   public String getName(String id)
      throws BookmarkFileException
   {
      return (findItem(id).getAttribute("name"));
   }

   /**
    * Get the URL of a link item
    */
   public String getURL(String id)
      throws BookmarkFileException
   {
      Element e = findItem(id);

      if (!e.getTagName().equals("link"))
      {
         throw new BookmarkFileException(
            id+" is not a link"
         );
      }

      return e.getAttribute("href");
   }

}

 