--
-- Test data
--

USE faqbase;

INSERT INTO users (email, password, forenames, surname, type)
VALUES ('Brian.Duff@oracle.com', 'wyzdex', 'Brian', 'Duff', 'Administrator');

INSERT INTO users (email, password, forenames, surname)
VALUES ('John.Smith@somewhere.org', 'wyzdex', 'John Albert', 'Smith');

INSERT INTO users (email, password, forenames, surname)
VALUES ('Reggie@oracle.com', 'wyzdex', 'Reginald', 'Turbermeister');

INSERT INTO categories (title, description, creator)
VALUES ('Java', 'Questions related to the java programming language', 1);

INSERT INTO categories (title, description, creator)
VALUES ('C#', 'Questions about Microsoft C#', 1);

INSERT INTO categories (title, description, creator)
VALUES ('Jean Michel Jarre', 'Questions about the French Synth-Meister', 2);

INSERT INTO categories (title, description, creator)
VALUES ('Linux', 'Questions about the Linux operating system', 3);

INSERT INTO categories (title, description, creator, parent_cat)
VALUES ('Servlets', 'Java Servlets', 1, 1);

INSERT INTO questions (category, title, description, keywords, creator, published)
VALUES (5, 'What are servlets?', 'The description', 'servlet fish', 2, "Yes");

INSERT INTO questions (category, title, description, keywords, creator, published)
VALUES (1, 'Some random question', 'The description', 'servlet fish', 3, "Yes");

INSERT INTO questions (category, title, description, keywords, creator, published)
VALUES (1, 'Unpublished Question', 'The description', 'servlet fish', 3, "No");

INSERT INTO answers (question, body, creator)
VALUES (1, "This is one answer to your question. Tra la la", 1);

INSERT INTO answers (question, body, creator)
VALUES (1, "This is another answer to the same question. By someone else", 2);

INSERT INTO answers (question, body, creator)
VALUES (2, "This is an answer to another question", 3);