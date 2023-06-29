# Address API Specs

## Create Address
Endpoint : POST /api/contacts/{idContacts}/addresses

Request Header :
- X-API-TOKEN : TOKEN (Mandatory)

Request Body :
```json
{
  "street" : "Jalan ...",
  "city" : "Kota",
  "province" : "provinsi",
  "country" : "Negara",
  "postalCode" : "17121"
}
```

Response Body (Success) :
```json
{
  "data" : {
    "id" : "randomString",
    "street" : "Jalan ...",
    "city" : "Kota",
    "province" : "provinsi",
    "country" : "Negara",
    "postalCode" : "17121"
  }
}
```

Response Body (Failed) :
```json
{
  "errors" : "contact is not found"
}
```

## Update Address
Endpoint : PUT /api/contacts/{idContacts}/addresses/{idAddress}

Request Header :
- X-API-TOKEN : TOKEN (Mandatory)

Request Body :
```json
{
  "street" : "Jalan ...",
  "city" : "Kota",
  "province" : "provinsi",
  "country" : "Negara",
  "postalCode" : "17121"
}
```

Response Body (Success) :
```json
{
  "data" : {
    "id" : "randomString",
    "street" : "Jalan ...",
    "city" : "Kota",
    "province" : "provinsi",
    "country" : "Negara",
    "postalCode" : "17121"
  }
}
```

Response Body (Failed) :
```json
{
  "errors" : "Address is not found"
}
```

## Get Addres
Endpoint : GET /api/contacts/{idContact}/addresses/{idAddress}

Request Header :
- X-API-TOKEN : TOKEN (Mandatory)

Response Body (Success) :
```json
{
  "data" : {
    "id" : "randomString",
    "street" : "Jalan ...",
    "city" : "Kota",
    "province" : "provinsi",
    "country" : "Negara",
    "postalCode" : "17121"
  }
}
```

Response Body (Failed) :
```json
{
  "errors" : "Address is not found"
}
```

## Remove Address
Endpoint : DELETE /api/contacts/{idContact}/addresses/{idAddress}

Request Header :
- X-API-TOKEN : TOKEN (Mandatory)

Response Body (Success) :
```json
{
  "data" : "OK"
}
```

Response Body (Failed) :
```json
{
  "errors" : "Address is not found"
}
```

## List Address
Endpoint : GET /api/contacts/{idContact}/addresses

Request Header :
- X-API-TOKEN : TOKEN (Mandatory)

Response Body (Success) :
```json
{
  "data" : [
    {
      "id" : "randomString",
      "street" : "Jalan ...",
      "city" : "Kota",
      "province" : "provinsi",
      "country" : "Negara",
      "postalCode" : "17121"
    }
  ]
}
```

Response Body (Failed) :
```json
{
  "errors" : "Contact is not found"
}
```