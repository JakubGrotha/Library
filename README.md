# Library

## How to use

You can start the application using an IDE (such as Intellij IDEA). Alternatively, you can use this command:

```bash
./gradlew bootRun
```

or if you're using Windows:
```bash
./gradlew.bat bootRun
```

### Authentication

Before you can test any of the endpoints, you first need to register. You can use any credentials, for example:

```json
{
  "username": "username",
  "password": "password"
}
```

Send the credentials as request body to `/api/users/register` (POST method) endpoint,
and you will receive the token (JWT), you need to pass with
every request in the Authorization header (with the "Bearer " prefix).

You can also log in using `/api/users/login` endpoint (POST method). 
You need to use valid credentials.

### Book management

#### Adding book

If you want to add a new book, send a request to `/api/books` endpoint (POST method). 
Here is an example of a valid request body:

```json
{
  "title": "Pride and Prejudice",
  "author": "Jane Austen",
  "isbn": "9780141439518",
  "publishedDate": "2022-12-31"
}
```

#### Accessing all books

If you want to view the data about all books, send a GET request to `/api/books`.

#### Accessing a book by id

If you want to get the data about a single book, send a GET request to `/api/books/{id}`,
where {id} is the id of the book you want to access.

#### Editing a book

You can edit a book by sending a PUT request to `/api/books/{id}`,
where {id} is the id of the book you want to access. You need to send the updated data in the request body, for example:

```json
{
  "title": "Pride and Prejudice",
  "author": "Jane Austen",
  "isbn": "9780141439518",
  "publishedDate": "2022-12-31"
}
```

#### Deleting a book

You can delete a book by sending a DELETE request to `/api/books/{id}`,
where {id} is the id of the book you want to delete.

#### Filtering books using a query

You can get data about books that contain a specific query by sending a GET request with a query parameter at `/api/books/search`,
where {query} is the query that will be used to filter the books. 

An example of a valid url: `/api/books/search?query=pride` containing the required parameter

All books that contain the query in their title, author or isbn will be returned.
