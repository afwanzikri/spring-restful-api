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
```json
{
  "data" : {
    "token" : "TOKEN",
    "expiredAt" : 23434565435345 // milliseconds
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