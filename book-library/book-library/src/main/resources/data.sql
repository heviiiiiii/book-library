-- Seed data loaded after Hibernate generates the schema.
-- Five books spanning different genres so the demo screen looks alive.

INSERT INTO book (title, author, genre, publication_year, available) VALUES
  ('Clean Code', 'Robert C. Martin', 'Software Engineering', 2008, TRUE),
  ('Effective Java', 'Joshua Bloch', 'Programming', 2018, TRUE),
  ('The Pragmatic Programmer', 'Andrew Hunt', 'Software Engineering', 1999, FALSE),
  ('Full Stack Vue.js', 'Hassan Djirdeh', 'Web Development', 2018, TRUE),
  ('Spring in Action', 'Craig Walls', 'Frameworks', 2022, TRUE);
