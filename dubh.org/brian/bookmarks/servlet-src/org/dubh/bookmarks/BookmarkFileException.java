package org.dubh.bookmarks;

/**
 * Exception class for errors while modifying the bookmarks file
 * <P>
 * @author Brian Duff
 */
class BookmarkFileException extends Exception
{
   private Throwable m_cause;

   /**
    * Constructor
    */
   public BookmarkFileException(String message)
   {
      super(message);
   }

   /**
    * Constructor
    */
   public BookmarkFileException(String message, Throwable cause)
   {
      super(message);
      m_cause = cause;
   }

   public Throwable getCause()
   {
      return m_cause;
   }
}

 