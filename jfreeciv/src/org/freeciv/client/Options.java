package org.freeciv.client;

public class Options implements Constants
{
   public static final int NUM_MW = 3;
   public static final int MW_OUTPUT = 1;
   public static final int MW_MESSAGES = 2;  /* add to the messages window */
   public static final int MW_POPUP =4;  /* popup an individual window */

   private int[] messages_where;

   public void initMessagesWhere()
   {
      messages_where = new int[E_LAST];
      int out_only[] = {E_IMP_BUY, E_IMP_SOLD, E_UNIT_BUY, E_MY_DIPLOMAT,
		   E_UNIT_LOST_ATT, E_UNIT_WIN_ATT};
      int i;

      for(i=0; i<E_LAST; i++) {
         messages_where[i] = MW_OUTPUT | MW_MESSAGES;
      }
      for(i=0; i<out_only.length; i++) {
         messages_where[out_only[i]] = MW_OUTPUT;
      }
   }

   public int getMessageWhere(int i)
   {
      return messages_where[i];
   }
}