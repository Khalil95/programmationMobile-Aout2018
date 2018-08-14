package com.henallux.primodeal.Adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.henallux.primodeal.Model.Publication;
import com.henallux.primodeal.R;

import java.util.List;

/**
 * Created by bil on 26-02-18.
 */

public class PublicationAdapter extends ArrayAdapter<Publication> {

    public PublicationAdapter(Context context, List<Publication> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_publication,parent, false);
        }

        TweetViewHolder viewHolder = (TweetViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TweetViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);

                viewHolder.fromNameShop = (TextView) convertView.findViewById(R.id.fromNameShop);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Publication tweet = getItem(position);
        viewHolder.title.setText(tweet.getTitle());
        viewHolder.description.setText(tweet.getDescription());
        if(tweet.getApplicationUser() != null)
            viewHolder.description.setText(tweet.getApplicationUser().getNameShop());

       // viewHolder.avatar.setImageDrawable(new ColorDrawable(tweet.getColor()));

        return convertView;
    }


    private class TweetViewHolder{
        public TextView title;
        public TextView description;
        public TextView fromNameShop;
    }
}
