# Online bookstore-application

A sample bookstore application demonstrating the add/delete/find/search use cases for books.

## Pre-requisites
1) This application is written using Java 11.
2) Maven version (3.9.x) should be installed.
3) This application uses spring boot version 2.7.1.

## Build and launch.
2) Open the command prompt/terminal and navigate to the project folder.
3) Execute the below command to build the project.

   `mvn clean install`

4) After the project is successfully build, the target folder is created inside the project folder. Navigate to the target folder.
5) Execute the below command to launch the project

   `java -jar bookstore-0.0.1-SNAPSHOT.jar`

   The above command will launch the application in the port 8080. Once the server is up, the REST API's could be fired using curl, POSTMAN etc

## REST APIs
The below REST APIs are provided to simulate the operations for books. 

### Add Book API (POST api/v1/books)
This API allows user to add a book to the bookstore. Although the book is uniquely identified by its ISBN but as the ISB
is out of scope of this exercise, book's id is used instead to uniquely identify. Further, there is a constraint that does not allow user to add a book with same name and author more than once.

### Get Book by Id API (GET api/v1/books/{id})
This API allows user to get the book by providing specific id.

### Delete Book By Id API (DELETE api/v1/books/{id})
This API allows user to delete a book by providing specific id.

### Get Books API (GET api/v1/books)
This API allows user to get the list of books. It supports query parameters like category and author as well. 
As this API returns multiple books, hence pagination is also supported and can be achieved by providing additional query parameters.

### Search book API (GET api/v1/books/search)
This API allows user to search for book by specifying a search query provided as a query parameter.
Currently only EQUALITY operation is supported as of now. The design of the API is extensible and in future the support for
more operations like LESS THAN, GREATER THAN, LIKE%, %LIKE etc can be added easily.
As this API returns multiple books, hence pagination is also supported and can be achieved by providing additional query parameters.

Example - if a user wants to search for books with name='Learn Algorithms'. The search query to be provided will be 
query=name:Learn Algorithms, where ":" resembles the EQUAL TO operator.

#### NOTE:
Currently only pagination(page based) is supported for API's that return multiple books. But the design is extensible enough to add support for sorting. 

For additional information on API's, please refer the swagger file.

## Limitations and Improvements
1) The application starts on the port 8080, therefore any other application must not be running on this port. This can be changed by providing a port in the application.properties file.

2) No authentication/authorization is added.

3) In memory db is used for this exercise. Each time a server restarts the changes are lost.

