package dubh.mail.nntp;

import javax.mail.MessagingException;

/**
 * Thrown whenever an error occurs in the NNTP store.
 */
public class NNTPException extends MessagingException
{
   protected int m_nCode;
   
   public NNTPException(String description, int responseCode)
   {
      super(description);
      m_nCode = responseCode;
   }   
   
   public int getResponseCode()
   {
      return m_nCode;
   }
}