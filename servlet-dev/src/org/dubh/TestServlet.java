package org.dubh;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * @author Ian Davies
 */
public class TestServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException
    {

        HttpSession userSession = request.getSession(true);

        userSession.setAttribute("testAttrib", "Simple Attribute");

                // now we need to transfer the control to home.jsp

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/test.xml");

        if (rd != null)
        {
         rd.forward(request, response);
        }
    }
}