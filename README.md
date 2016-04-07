# UhO-Backend
all of the backend

## creating user
Create user when you first get the facebook token. You will receive a response with a user id in the result field as shown below. You can query full user record with that at any time. Store the user id on the app\client.
url: http://52.91.235.124:8090/user
verb: POST
payload json:

```json
{
      "fbToken": "EAACEdEose0cBAK14ZCKEZCoUGaRHrOrdusTXKHVsi5uA9ZCY2Nbb647p38UFRECPXIp70aHs6CusOyEIFZBqPShWentXFi5x4FM82HglGhzT0reZBCk9dPDIoKKdkKVEWHJ2C7S8kvDo8pBny2pvUufViLdmSGLbuXGTZAHtsDZBwZDZD",
      "twitterToken": "asdfadsf",
      "pushToken": "adfasfasdf"
    }
response json:
{
  "code": 200,
  "reason": "success",
  "result": "5705c8783d1d4020d0a5e1ae"
}
```

## Get user details:
query user details on a REST GET call as shown below.

url: http://52.91.235.124:8090/user?id=570568d5df5f08269161018d
verb: GET
response json: 
```json
{
  "code": 200,
  "reason": "success",
  "result": {
    "user": {
      "fbToken": "EAACEdEose0cBAK14ZCKEZCoUGaRHrOrdusTXKHVsi5uA9ZCY2Nbb647p38UFRECPXIp70aHs6CusOyEIFZBqPShWentXFi5x4FM82HglGhzT0reZBCk9dPDIoKKdkKVEWHJ2C7S8kvDo8pBny2pvUufViLdmSGLbuXGTZAHtsDZBwZDZD",
      "twitterToken": "asdfadsf",
      "lastChecked": 0,
      "pushToken": null,
      "settings": {
        "anger": 0,
        "foul language": 0,
        "political correctness": 0,
        "partying": 0
      },
      "uoid": "570568d5df5f08269161018d"
    },
    "analysis": {
      "userId": "570568d5df5f08269161018d",
      "totalPosts": 16,
      "posSplit": 14,
      "negSplit": 2,
      "negCommentsSplit": 0,
      "negPhotosSplit": 2,
      "negVideoSplit": 0,
      "analysis": {
        "10153176378196628_10153858295591628": {
          "postId": "10153176378196628_10153858295591628",
          "picURL": "https://scontent.xx.fbcdn.net/hvthumb-xfp1/v/t15.0-10/s720x720/10678970_10153858295491628_1712510438_n.jpg?oh=b361edb45e20aba9024e44432f92ea3f&oe=578D949D",
          "picComment": "Found some hints in this pic that may be bad for your reputation! However I could be wrong, please make sure this image looks ok",
          "negCmnts": [],
          "postMsg": "",
          "postMsgComment": "",
          "thisIsOk": false
        }
      }
    }
  }
}
```


## update user
use this API if you ever need to refresh any user details, such as, fb token or push token or settings etc. The REST call is the same for both create and update. First do a get user call. Get the "user" json sub-structure from it and modify values inside that and pass the entire user structure back to server on the create user call. 
* remember that the structure is replaced as-is on the server side. if information is missed then it will go missing on the server side also.
 
Example:
url: http://52.91.235.124:8090/user
verb: POST
payload json:

```json
{
      "fbToken": "EAACEdEose0cBAK14ZCKEZCoUGaRHrOrdusTXKHVsi5uA9ZCY2Nbb647p38UFRECPXIp70aHs6CusOyEIFZBqPShWentXFi5x4FM82HglGhzT0reZBCk9dPDIoKKdkKVEWHJ2C7S8kvDo8pBny2pvUufViLdmSGLbuXGTZAHtsDZBwZDZD",
      "twitterToken": "",
      "lastChecked": 0,
      "pushToken": null,
      "settings": {
        "anger": 0,
        "foul language": 0,
        "political correctness": 0,
        "partying": 0
      },
      "uoid": "570568d5df5f08269161018d"
}
```
