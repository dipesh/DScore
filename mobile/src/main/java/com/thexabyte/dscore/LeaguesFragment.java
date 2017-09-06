package com.thexabyte.dscore;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.thexabyte.mylibrary.Sport;
import com.thexabyte.mylibrary.SportsGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LeaguesFragment extends Fragment {
    LeaguesExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<Sport>> listDataChild;

    public LeaguesFragment() {
        // Required empty public constructor
    }


    public static LeaguesFragment newInstance() {
        LeaguesFragment fragment = new LeaguesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leagues, container, false);


        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                MainActivity activity = (MainActivity) getActivity();
                ArrayList<Sport> list = (ArrayList) listDataChild.get(listDataHeader.get(groupPosition));
                Sport sport = list.get(childPosition);
                activity.setSport(sport);
                return true;
            }
        });

        StaticMethods.indicatorOnRight(expListView, getActivity());
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Soccer");
        List<Sport> soccerChildList = new ArrayList<>();
        soccerChildList.add(Sport.MLS);
        soccerChildList.add(Sport.EURO_2016);
        soccerChildList.add(Sport.PremierLeague);
        soccerChildList.add(Sport.Ligue1);
        soccerChildList.add(Sport.SerieA);
        //soccerChildList.add(Sport.FACup);
        soccerChildList.add(Sport.Championship);
        soccerChildList.add(Sport.ChampionLeague);
        //soccerChildList.add(Sport.EuropaLeague);
        soccerChildList.add(Sport.LALiga);
        soccerChildList.add(Sport.Bundesliga);
        soccerChildList.add(Sport.ScottishPremiership);
        listDataChild.put(listDataHeader.get(0), soccerChildList);

        listDataHeader.add("Football");
        List<Sport> footballChildList = new ArrayList<>();
        footballChildList.add(Sport.NFL);
        footballChildList.add(Sport.NCAAF);
        listDataChild.put(listDataHeader.get(1), footballChildList);

        listDataHeader.add("Basketball");
        List<Sport> basketBallChildList = new ArrayList<>();
        basketBallChildList.add(Sport.NBA);
        basketBallChildList.add(Sport.NCAAB);
        listDataChild.put(listDataHeader.get(2), basketBallChildList);

        listDataHeader.add("Hockey");
        List<Sport> hockeyChildList = new ArrayList<>();
        hockeyChildList.add(Sport.NHL);
        listDataChild.put(listDataHeader.get(3), hockeyChildList);

        listAdapter = new LeaguesExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        for (int position = 1; position <= listDataHeader.size(); position++)
            expListView.expandGroup(position - 1);

        return view;
    }

}
