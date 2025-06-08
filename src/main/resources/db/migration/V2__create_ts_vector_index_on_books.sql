ALTER TABLE books ADD COLUMN full_text_search tsvector
GENERATED ALWAYS AS (to_tsvector('english', coalesce(title, '') || ' ' || coalesce(author, '') || ' ' || coalesce(isbn, ''))) STORED;

CREATE INDEX books_full_text_search_idx ON books USING GIN (full_text_search);
