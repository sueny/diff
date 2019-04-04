# Diff API

### Introduction
Use this api to compare two base64 encode binary data.

This API provides 2 http endpoints that accepts JSON base64 encoded binary data.

- <host>/v1/diff/<ID>/left and;
- <host>/v1/diff/<ID>/right

The result of the comparison is available on a third end point:

- <host>/v1/diff/<ID>

This api implements the algorithm Needleman-Wunsch for comparing the strings.


### Running the project
How to deploy the application:
`mvn spring-boot:run`

How to run unit and integration tests:
`mvn clean verify`

### Calling the API
Please refer to [Diff API Documentation](https://documenter.getpostman.com/view/7131745/S1EH42Xq) to get detailed information about calling the API.

### Examples

- Example 1:

id:     1
left:   ABCDE
right:  ABCDE
diff:	{equal: true} 


- Example 2:

id:     1
left:   ABCDE
right:  ABCDEF
diff:	{leftDataSize: 5, rightDataSize: 6} 


-Example 3:
left:   BCDYY
right:  WWBCD
diff:   {matches: [leftOffset: 0, rightOffset: 2, length: 3]}


### Error Codes
- 400 Bad Request - Returned when the user tries to save left or right content null.
- 404 Not Found - Returned when the user tries to get diff but left or right data is missing.
