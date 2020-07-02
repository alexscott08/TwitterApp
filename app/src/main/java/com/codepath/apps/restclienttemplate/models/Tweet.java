package com.codepath.apps.restclienttemplate.models;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.*;

@Parcel
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public String embeddedMediaUrl;
    public int retweetCount;
    public int favoriteCount;
    public long id;

    // Empty constructor for Parcel lib
    public Tweet() { }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.retweetCount = jsonObject.getInt("retweet_count");
        tweet.favoriteCount = jsonObject.getInt("favorite_count");
        tweet.id = jsonObject.getLong("id");
//        tweet.id = jsonObject.getInt("id");

        // Pulls embedded media, if it exists
        JSONObject entitiesObject = jsonObject.getJSONObject("entities");
        if (entitiesObject.has("media")) {
            JSONArray mediaArray = entitiesObject.getJSONArray("media");
            tweet.embeddedMediaUrl = mediaArray.getJSONObject(0).getString("media_url_https");
            Log.i("Tweet", "Media: " + tweet.embeddedMediaUrl);
        } else {
            Log.i("Tweet", "no media");
        }
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}
