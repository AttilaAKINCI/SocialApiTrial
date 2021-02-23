package com.akinci.socialapitrial.common.network

class RestConfig {
    companion object {
        const val API_BASE_URL = "https://api.twitter.com/"
        const val REQUEST_TOKEN_URL = "oauth/request_token"
        const val AUTHORIZE_URL = "oauth/authorize"
        const val REQUEST_ACCESS_TOKEN = "oauth/access_token"

        const val REQUEST_SIGN_OUT = "1.1/oauth/invalidate_token"
        const val REQUEST_FETCH_FOLLOWERS = "1.1/followers/list.json"
        const val REQUEST_FETCH_FOLLOWINGS = "1.1/friends/list.json"
        const val REQUEST_GET_USER_INFO = "1.1/users/show.json"

        const val REQUEST_GET_TIME_LINE = " 1.1/statuses/user_timeline.json"
    }
}