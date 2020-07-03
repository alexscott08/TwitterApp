package com.codepath.apps.restclienttemplate.models;


import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public String replyTo;
    public String dateCreated;
    public String formattedDate;
    public boolean userFavorited;
    public boolean userRetweeted;


    // Empty constructor for Parcel lib
    public Tweet() { }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.replyTo = jsonObject.getString("in_reply_to_screen_name");
        if (!tweet.replyTo.equals("null")) {
            tweet.body = "Replying to @" + tweet.replyTo + "\n" + jsonObject.getString("text");
        } else {
            tweet.body = jsonObject.getString("text");
        }
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.retweetCount = jsonObject.getInt("retweet_count");
        tweet.favoriteCount = jsonObject.getInt("favorite_count");
        tweet.id = jsonObject.getLong("id");
        tweet.dateCreated = jsonObject.getString("created_at");
        tweet.formattedDate = getRelativeTimeAgo(tweet.dateCreated);
        tweet.userFavorited = jsonObject.getBoolean("favorited");
        tweet.userRetweeted = jsonObject.getBoolean("retweeted");


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

    // Converts tweets from JSONArray format to a list of type Tweet
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }


    // Corrects time formatting parsed from Twitter API
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Log.i("Date", relativeDate);
        return relativeDate;
    }

}
