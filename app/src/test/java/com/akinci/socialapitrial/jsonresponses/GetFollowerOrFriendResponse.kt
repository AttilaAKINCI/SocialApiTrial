package com.akinci.socialapitrial.jsonresponses

class GetFollowerOrFriendResponse {
    companion object {
        val followerOrFriendResponse = """
            {
               "next_cursor":1336170512814457473,
               "next_cursor_str":"1336170512814457473",
               "previous_cursor":0,
               "previous_cursor_str":"0",
               "users":[
                  {
                     "id":1336170512814457473,
                     "name":"Vildan",
                     "screen_name":"Vildan",
                     "location":"",
                     "description":"Ankara, Beşiktaş",
                     "followers_count":0,
                     "friends_count":74,
                     "verified":false,
                     "profile_image_url_https":"https://pbs.twimg.com/profile_images/1358570750769324033/2Mlormal.jpg"
                  },
                  {
                     "id":1256609,
                     "name":"Eddie",
                     "screen_name":"mr_eddieee",
                     "location":"Wien, Österreich",
                     "description":"Father, Husband, Software Engineer, Polyglot and Basketballer.",
                     "followers_count":149,
                     "friends_count":403,
                     "verified":false,
                     "profile_background_image_url_https":"https://abs.twimg.com/images/themes/theme4.gif",
                     "profile_image_url_https":"https://pbs.twimg.com/profile_images/883598776631297/p_zzl1rmal.jpg",
                     "profile_banner_url":"https://pbs.twimg.com/profile_banners/12563609/141309"
                  }
               ]
            }
        """
    }
}