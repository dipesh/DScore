package com.thexabyte.dscore;

/**
 * Created by Dipesh on 2/14/2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thexabyte.mylibrary.SportsGame;

import java.util.HashMap;
import java.util.List;

public class AllGamesExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<SportsGame>> _listDataChild;

    public AllGamesExpandableListAdapter(Context context, List<String> listDataHeader,
                                         HashMap<String, List<SportsGame>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final SportsGame hockeyGame = (SportsGame) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_all_games, null);
        }

        TextView homeTeamTextView = (TextView) convertView.findViewById(R.id.homeTeamTextView);
        TextView awayTeamTextView = (TextView) convertView.findViewById(R.id.awayTeamTextView);
        TextView homeTeamScore = (TextView) convertView.findViewById(R.id.homeTeamScore);
        TextView awayTeamScore = (TextView) convertView.findViewById(R.id.awayTeamScore);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
        final ImageView watchImageView = (ImageView) convertView.findViewById(R.id.watchImageView);

        if (hockeyGame.isOnWatch) {
            watchImageView.setImageResource(R.drawable.ic_watch_red_48dp);
        } else {
            watchImageView.setImageResource(R.drawable.ic_watch_black_48dp);
        }


        homeTeamTextView.setText(hockeyGame.homeTeam.city + " " + hockeyGame.homeTeam.name);
        awayTeamTextView.setText(hockeyGame.awayTeam.city + " " + hockeyGame.awayTeam.name);
        homeTeamScore.setText(hockeyGame.homeTeam.score);
        awayTeamScore.setText(hockeyGame.awayTeam.score);

        if (hockeyGame.gameTimeOrStartDate.toLowerCase().contains("1st") ||
                hockeyGame.gameTimeOrStartDate.toLowerCase().contains("2nd") ||
                hockeyGame.gameTimeOrStartDate.toLowerCase().contains("3rd") ||
                hockeyGame.gameTimeOrStartDate.toLowerCase().contains("4th") ||
                hockeyGame.gameTimeOrStartDate.toLowerCase().contains("final")) {
            timeTextView.setText(hockeyGame.gameTimeOrStartDate);
        } else {
            timeTextView.setText(hockeyGame.gameTimeOrStartDate + " " + hockeyGame.gameStatusOrStartTime);
        }


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }
        catch(Exception e ) {
            return  0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);

        lblListHeader.setText(headerTitle);

        //ExpandableListView mExpandableListView = (ExpandableListView) parent;
        //mExpandableListView.expandGroup(groupPosition);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
