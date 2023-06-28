// md stand for mark down
# User API Spec

## Register User
Endpoint : POST /api/users

Request Body :
```json
{
  "username" : "afwan",
  "password" : "rahasia",
  "name"     : "Afwan Zikri"
}
```

Response Body (Success) :
```json
{
  "data" : "OK"
}
```
Response Body (Failed) :
```json
{
  "data"    : "error",
  "errors"  : "Username cannot be blank !!!"
}
```

## Login User
Endpoint : POST /api/auth/login

Request Body :
```json
{
  "username" : "afwan",
  "password" : "rahasia"
}
```

Response Body (Success) :
```json
{
  "data" : {
    "token" : "TOKEN",
    "expiredAt" : 23434565435345 // milliseconds
  }
}
```
Response Body (Failed) :
```json
{
  "errors"  : "Username or Password is wrong"
}
```

## Get User
Endpoint : GET /api/users/current

Request Header

Response Body (Success) :
```json
{
  "data" : {
    "username" : "afwan",
    "name"     : "Afwan Zikri"
  }
}
```
Response Body (Failed) :
```json
{
  "errors"  : "Unauthorized"
}
```

## Update User
Endpoint : PATCH /api/users/current

Request Body :
```json
{
  "name" : "Afwan Zikri ZR", // put if only want to update name
  "password" : "new pswd" // put if only want to update password
}
```

Response Body (Success) :
```json
{
  "data" : {
    "token" : "TOKEN",
    "expiredAt" : 23434565435345 // milliseconds
  }
}
```
Response Body (Failed) :
```json
{
  "errors"  : "Username or Password is wrong"
}
```


## Logout User