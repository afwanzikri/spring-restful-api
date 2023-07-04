// .md file extension (also written as .markdown) stands for “Markdown documentation”
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
// expiredAt in milliseconds
```json
{
  "data" : {
    "token" : "TOKEN",
    "expiredAt" : 23434565435345 
  }
}
```
Response Body (Failed, 401) :
```json
{
  "errors"  : "Username or Password is wrong"
}
```

## Get User
Endpoint : GET /api/users/current

Request Header :
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data" : {
    "username" : "afwan",
    "name"     : "Afwan Zikri"
  }
}
```
Response Body (Failed, 401) :
```json
{
  "errors"  : "Unauthorized"
}
```

## Update User
Endpoint : PATCH /api/users/current

Request Header :
- X-API-TOKEN : Token (Mandatory)

Request Body :
/*
    put if only want to update name
    put if only want to update password
*/
```json
{
  "name" : "Afwan Zikri ZR", 
  "password" : "new pswd" 
}
```

Response Body (Success) :
```json
{
  "data" : {
    "username" : "afwan",
    "name"     : "Afwan Zikri"
  }
}
```
Response Body (Failed, 401) :
```json
{
  "errors"  : "Unauthorized"
}
```

## Logout User
Endpoint : DELETE /api/auth/logout

Request Header :
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data" : "OK"
}
```