package org.dubh.bookmarks;

/**
 * This interface represents the bookmarks file to the servlet. The servlet
 * always accesses the file through this interface, meaning the servlet
 * is truly decoupled from any notion of how the bookmark file is stored
 * and implemented.
 * <P>
 * @author Brian Duff
 */
interface BookmarkFile
{
   /**
    * Delete an item from the bookmark file.
    */
   public void deleteItem(String itemId) throws BookmarkFileException;

   /**
    * Rename an item in the bookmark file.
    */
   public void renameItem(String oldId, String newName)
      throws BookmarkFileException;

   /**
    * Add a category to the bookmark file
    *
    * @param parentCategory the id of the parent category. If null, the
    *    new category is added to the top level
    * @param categoryName the name of the new category
    */
   public void addCategory(String parentCategory, String categoryName)
       throws BookmarkFileException;

   /**
    * Add a link to the bookmark file
    *
    * @param parentCategory the id of the category in which to place
    *    the bookmark
    * @param linkName the name of the link
    * @param linkURL the URL of the link
    */
   public void addLink(String parentCategory, String linkName, String linkURL)
       throws BookmarkFileException;

   /**
    * Move a link one position earlier in its category
    *
    * @param linkId the link to move
    */
   public void moveLinkUp(String link)
      throws BookmarkFileException;

   /**
    * Move a link one position later in its category
    *
    * @param link the link to move
    */
   public void moveLinkDown(String link)
      throws BookmarkFileException;

   /**
    * Change the URL of a link
    *
    * @param linkId
    * @param newURL
    */
   public void changeLinkURL(String linkId, String newURL)
       throws BookmarkFileException;

   /**
    * Is the given item a category?
    */
   public boolean isCategory(String id)
      throws BookmarkFileException;

   /**
    * Get the unescaped name of the item
    */
   public String getName(String id)
      throws BookmarkFileException;

   /**
    * Get the URL of a link item
    */
   public String getURL(String id)
      throws BookmarkFileException;
}

 