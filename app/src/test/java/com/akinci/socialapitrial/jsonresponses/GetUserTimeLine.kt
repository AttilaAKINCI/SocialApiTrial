package com.akinci.socialapitrial.jsonresponses

class GetUserTimeLine {
    companion object {
        val userTimeLineJsonResponse = """
            [
               {
                  "id":1336170512814457473,
                  "text":"#SiddeteHayir O kadar çok var ki ne ceza gelirse https://t.co/5pfkcEARwk",
                  "entities":{
                     "hashtags":[
                        {
                           "text":"SiddeteHayir"
                        }
                     ]
                  },
                  "user":{
                     "id":1336170512814457473,
                     "name":"Vildan",
                     "screen_name":"Vildan",
                     "location":"",
                     "description":"Ankara, Beşiktaş",
                     "followers_count":0,
                     "friends_count":74,
                     "verified":false,
                     "profile_image_url_https":"https://pbs.twimg.com/profile_images/1358570750769324033/2Ml28l.jpg"
                  },
                  "created_at":"Sat Mar 06 22:45:57 +0000 2021"
               }
            ]
        """
    }
}