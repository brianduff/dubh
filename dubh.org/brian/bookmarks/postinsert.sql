
-- Hacky bit of SQL to reparent categories after re-insertion from XML. This is
-- totally non-dynamic and generally evil, but I couldn't figure out how
-- to do this without object views which mysql doesn't support.

use bookmarks;

UPDATE categories SET parent_cat=9 WHERE title="HTML" or title="Java" or title="SCM" or title="Scripting" or title="WebDAV" or title="XML" or title="User Interfaces" or title="Open Source";
UPDATE categories SET parent_cat=18 WHERE title="Admin" or title="Bug Database" or title="Internal Discussion Groups" or title="Oracle Technology Network" or title="Repository Wizards" or title="Usability";

