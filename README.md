# Diff API

### Introduction
Use this api to compare two base64 encode binary data.

This API provides 2 http endpoints that accepts JSON base64 encoded binary data.

- <host>/v1/diff/<ID>/left and;
- <host>/v1/diff/<ID>/right

The result of the comparison is available on a third end point:

- <host>/v1/diff/<ID>

This api implements the [Needleman-Wunsch algorithm](https://en.wikipedia.org/wiki/Needleman%E2%80%93Wunsch_algorithm) for comparing the strings.


### How to run and test this project
How to deploy the application:
`mvn spring-boot:run`

How to run unit and integration tests:
`mvn clean verify`

### Calling the API
Please refer to [Diff API Documentation](https://documenter.getpostman.com/view/7131745/S1EH42Xq) to get detailed information about calling the API.


### Examples of left and right diff

- Example 1:

id:     1

left:   ABCDE

right:  ABCDE

diff:	`{"id": 1, "equal": true}`


- Example 2:

id:     2

left:   ABCDE

right:  ABCDEF

diff:	`{"id": 2, "leftDataSize": 5, "rightDataSize": 6}`


- Example 3:

id: 3

left:   BCDYY

right:  WBCDW

diff:
```
{
    "id": 3,
    "matches": [
        {
            "leftOffset": 0,
            "rightOffset": 1,
            "length": 3
        }
    ],
    "leftMismatches": [
        {
            "charOffset": 3,
            "length": 2
        }
    ],
    "rightMismatches": [
        {
            "charOffset": 0,
            "length": 1
        },
        {
            "charOffset": 4,
            "length": 1
        }
    ]
}
```


### Error Codes
- 400 Bad Request - Returned when the user tries to save left or right content null.
- 404 Not Found - Returned when the user tries to get diff but left or right data is missing.
