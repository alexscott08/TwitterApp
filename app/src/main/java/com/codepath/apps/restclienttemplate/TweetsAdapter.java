package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    // Pass in the context and list of tweets
    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            bodyTextView = itemView.findViewById(R.id.bodyTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            screenNameTextView = itemView.findViewById(R.id.screenNameTextView);
            embeddedImageView = itemView.findViewById(R.id.embeddedImageView);
            retweetTextView = itemView.findViewById(R.id.retweetTextView);
            favTextView = itemView.findViewById(R.id.favTextView);
        }

        public void bind(Tweet tweet) {
            bodyTextView.setText(tweet.body);
            screenNameTextView.setText("@" + tweet.user.screenName);
            nameTextView.setText(tweet.user.name);
            retweetTextView.setText(tweet.retweetCount + "");
            favTextView.setText(tweet.favoriteCount + "");
            GlideApp.with(context).load(tweet.user.profileImageUrl).transform(new CircleCrop()).into(profileImageView);

            if (tweet.embeddedMediaUrl != null) {
                embeddedImageView.setVisibility(View.VISIBLE);
                GlideApp.with(context).load(tweet.embeddedMediaUrl).centerCrop()
                        .into(embeddedImageView);
            } else {
                embeddedImageView.setVisibility(View.GONE);
            }

        }
    }
}
