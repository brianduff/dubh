package org.dubh.bookmarks;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.util.Date;

/**
 * This servlet processes operations on the bookmarks XML file.
 *
 * @author brian@dubh.org
 */
public class EditBookmarksServlet extends HttpServlet
{
   private static final boolean DEBUG_MODE = false;

   private String m_xmlFile;


   public void init(ServletConfig config)
      throws ServletException
   {
      super.init(config);

      m_xmlFile = config.getInitParameter("xmlfile");
   }

   private BookmarkFile getBookmarkFile()
   {
      return XMLBookmarkFile.newInstance(m_xmlFile);
   }

   /**
    * If an error occurs, this method will print an appropriate message
    * to the web page.
    *
    * @param request the servlet request
    * @param response the servlet response
    * @param message the message to display
    * @param exception the exception which occurred (if any)
    */
   private void doError(HttpServletRequest request,
      HttpServletResponse response, String message, Throwable exception,
      String link, String linkTitle)
      throws IOException
   {
      response.setContentType("text/html");
      PrintWriter out = new PrintWriter (response.getOutputStream());

      out.println("<html>");
      out.println("<head><title>Unable to Edit Bookmarks</title></head>");
      out.println("<body>");
      out.println("<h1>Unable to Edit Bookmarks</h1>");
      out.println("<p>An error occurred while editing bookmarks:</p>");
      out.println("<p><b>" + message + "</b></p>");
      if (exception != null && !(exception instanceof EditBookmarksException))
      {
         out.println("<p><pre>");
         exception.printStackTrace(out);
         out.println("</pre></p>");

         if (exception instanceof BookmarkFileException)
         {
            BookmarkFileException bfe = (BookmarkFileException)exception;

            if (bfe.getCause() != null)
            {
               out.println("<p><i>Caused by exception:</i><p><pre>");
               bfe.getCause().printStackTrace(out);
               out.println("</pre></p>");
            }
         }

      }
      if (link != null)
      {
         out.println("<p><a href=\""+link+"\">" + linkTitle + "</a></p>");
      }
      out.println("</body></html>");      
      out.close();
   }

   private void debug(HttpServletResponse response, String action, String item,
      String source)
   throws ServletException, IOException
   {
      response.setContentType("text/html");
      PrintWriter out = new PrintWriter (response.getOutputStream());

      out.println("<html>");
      out.println("<head><title>Edit Bookmarks DEBUG MODE</title></head>");
      out.println("<body>");
      out.println("<h1>Edit Bookmarks DEBUG MODE</h1>");

      out.println("<p><b>Action: </b>"+action+"<br />");
      out.println("<b>Item: </b>"+item+"<br />");
      out.println("<b>Source: </b>"+source+"<br />");
      out.println("<b>XML File: </b>"+m_xmlFile+"<br />");
      out.println("Expires:"+new Date().toString());

      out.println("</body></html>");

      out.close();
   }

   /**
   * Process the HTTP Get request
   */
   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
   {
      response.setContentType("text/html");

      String strAction = (String) request.getParameter("action");
      String strItem   = (String) request.getParameter("item");
      String strSource = (String) request.getParameter("srcurl");

      try
      {
         if (DEBUG_MODE)
         {
            debug(response, strAction, strItem, strSource);
         }
         else
         {
            processAction(strAction, strItem);
            if (strSource != null)
            {
               // We must prevent the client from caching the
               // source document. This is hacky, but using the
               // Cache-Control HTTP header doesn't seem to work (IE ignores
               // it??)
               Date d = new Date();
               int ccidx = strSource.indexOf("&cachecontrol");
               if (ccidx > 0)
               {
                  strSource = strSource.substring(0, ccidx);
               }
               String dstr = ""+d.getYear()+""+d.getMonth()+""+d.getDate()+""+
                  d.getHours()+""+d.getMinutes()+""+d.getSeconds();
               response.sendRedirect(strSource+"&cachecontrol="+dstr);
            }
         }
      }
      catch (Throwable t)
      {
         if (t instanceof ThreadDeath) throw (ThreadDeath)t;

         doError(request, response, t.getMessage(), t, strSource,
            "Back to Bookmarks");
      }
   }

   /**
   * Process the HTTP Post request
   */
   public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
   {
      doGet(request, response);
   }

   /**
   * Get Servlet information
   * @return java.lang.String
   */
   public String getServletInfo()
   {
      return "org.dubh.bookmarks.EditBookmarksServlet Information";
   }

   /**
    * Process the action
    */
   private void processAction(String action, String item)
      throws Exception
   {
      if (action == null || action.trim().length() == 0)
      {
         throw new EditBookmarksException("No action was specified");
      }

      if (item == null || item.trim().length() == 0)
      {
         throw new EditBookmarksException("No item was specified");
      }

      if (action.equalsIgnoreCase("add"))
      {
         addCommand(item);
      }
      else if (action.equalsIgnoreCase("delete"))
      {
         deleteCommand(item);
      }
      else if (action.equalsIgnoreCase("moveup"))
      {
         moveUpCommand(item);
      }
      else if (action.equalsIgnoreCase("movedown"))
      {
         moveDownCommand(item);
      }
      else if (action.equalsIgnoreCase("configure"))
      {
         configureCommand(item);
      }
      else
      {
         throw new EditBookmarksException(
            "Unrecognized action - "+action
         );
      }
   }

   private void addCommand(String item)
      throws Exception
   {

   }

   private void deleteCommand(String item)
      throws Exception
   {
      getBookmarkFile().deleteItem(item);
   }

   private void moveUpCommand(String item)
      throws Exception
   {
      getBookmarkFile().moveLinkUp(item);
   }

   private void moveDownCommand(String item)
      throws Exception
   {
      getBookmarkFile().moveLinkDown(item);
   }

   private void configureCommand(String item)
      throws Exception
   {

   }

   private void doAddBookmarkForm(HttpServletRequest req,
      HttpServletResponse res, String item, String source)
   {

   }

   /**
    * We use our own exception class. When we report errors on these
    * exceptions, we don't display a stack trace.
    */
   class EditBookmarksException extends Exception
   {
      public EditBookmarksException(String message)
      {
         super(message);
      }
   }
}

 