package com.solaris.androidstone.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.solaris.androidstone.R;

public class NewsContentFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        view = inflater.inflate(R.layout.news_content_frag, container, false);
        return view;
    }

   public void refresh(String newsTitle,String newsContent){
        View visibilityLayout = view.findViewById(R.id.visivility_layout);
       visibilityLayout.setVisibility(View.VISIBLE);

       TextView newsTitleText = (TextView) view.findViewById(R.id.news_title);
       TextView newsContentText = (TextView) view.findViewById(R.id.news_content);

       newsTitleText.setText(newsTitle);
       newsContentText.setText(newsContent);
    }
}
