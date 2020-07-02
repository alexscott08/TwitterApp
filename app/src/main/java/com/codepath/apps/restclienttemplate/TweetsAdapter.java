package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    // Pass in the context and list of tweets
    Context context;
    List<Tweet> tweets;
    private TwitterClient client;
    public static final String TAG = "TweetsAdapter";
    SwipeRefreshLayout swipeContainer;
    private TweetsAdapter adapter;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
        client = TwitterApp.getRestClient(context);
    }

    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at the position
        Tweet tweet = tweets.get(position);
        // Bind the tweet with view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImageView;
        TextView bodyTextView;
        TextView nameTextView;
        TextView screenNameTextView;
        ImageView embeddedImageView;
        TextView retweetTextView;
        TextView favTextView;
        TextView dateTextView;
        ImageButton retweetImgBtn;
        ImageButton favImgBtn;
        Button replyBtn;
        boolean retweeted = false;
        boolean favorited = false;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            bodyTextView = itemView.findViewById(R.id.bodyTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            screenNameTextView = itemView.findViewById(R.id.screenNameTextView);
            embeddedImageView = itemView.findViewById(R.id.embeddedImageView);
            retweetTextView = itemView.findViewById(R.id.retweetTextView);
            favTextView = itemView.findViewById(R.id.favTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            retweetImgBtn = itemView.findViewById(R.id.retweetImgBtn);
            favImgBtn = itemView.findViewById(R.id.favImgBtn);
            replyBtn = itemView.findViewById(R.id.replyBtn);
        }

        public void bind(final Tweet tweet) {
            bodyTextView.setText(tweet.body);
            screenNameTextView.setText("@" + tweet.user.screenName);
            nameTextView.setText(tweet.user.name);
            retweetTextView.setText(tweet.retweetCount + "");
            favTextView.setText(tweet.favoriteCount + "");
            dateTextView.setText(" • " + tweet.formattedDate);

            GlideApp.with(context).load(tweet.user.profileImageUrl).transform(new CircleCrop()).into(profileImageView);

            // Adds image to tweet view if available
            if (tweet.embeddedMediaUrl != null) {
                embeddedImageView.setVisibility(View.VISIBLE);
                GlideApp.with(context).load(tweet.embeddedMediaUrl).centerCrop()
                        .into(embeddedImageView);
            } else {
                embeddedImageView.setVisibility(View.GONE);
            }

            // Updates retweet and favorite status of tweets with onClickListener
            retweeted = tweet.userRetweeted;
            retweetImgBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   updateRetweet(tweet);
                   retweetTweet(retweeted, tweet.id);
                }
            });
            favorited = tweet.userFavorited;
            favImgBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    updateFav(tweet);
                    favoriteTweet(favorited, tweet.id);
                }
            });

            replyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ComposeActivity.class);
                    intent.putExtra("reply id", tweet.id);
                    context.startActivity(intent);
                }
            });

        }
        private void favoriteTweet(boolean favorited, long tweetId) {
            client.favoriteTweet(favorited, tweetId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess!" +json.toString());
                    JSONArray jsonArray = json.jsonArray;
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure", throwable);
                }
            });
        }

        private void retweetTweet(boolean retweeted, long tweetId) {
            client.retweetTweet(retweeted, tweetId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess!" +json.toString());
                    JSONArray jsonArray = json.jsonArray;
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure", throwable);
                }
            });
        }

        // Sets favorited status for tweets
        private void updateFav(Tweet tweet) {
            if (!favorited) {
                favImgBtn.setImageResource(R.drawable.ic_vector_heart);
                favorited = true;
            } else {
                favImgBtn.setImageResource(R.drawable.ic_vector_heart_stroke);
                favorited = false;
            }
        }

        // Sets retweeted status for tweets
        private void updateRetweet(Tweet tweet) {
            if (!retweeted) {
                retweetImgBtn.setImageResource(R.drawable.ic_vector_reply);
                retweeted = true;
            } else {
                retweetImgBtn.setImageResource(R.drawable.ic_vector_retweet_stroke);
                retweeted = false;
            }
        }
    }
}
