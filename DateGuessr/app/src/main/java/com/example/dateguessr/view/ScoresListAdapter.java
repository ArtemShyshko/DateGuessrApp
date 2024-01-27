package com.example.dateguessr.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dateguessr.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoresListAdapter extends BaseAdapter {

    private List<String> keys;
    private Map<String, Integer> data;
    private Context context;

    public ScoresListAdapter(Context context, Map<String, Integer> data) {
        this.context = context;
        this.data = data;
        this.keys = new ArrayList<>(data.keySet());
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(keys.get(i));
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        }

        TextView keyTextView = view.findViewById(R.id.pageTitle);
        TextView valueTextView = view.findViewById(R.id.singleScore);

        String pageTitle = keys.get(i);
        int score = data.get(pageTitle);

        keyTextView.setText(pageTitle);
        valueTextView.setText(String.valueOf(score));

        return view;
    }
}
