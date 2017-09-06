package com.thexabyte.dscore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.thexabyte.mylibrary.Sport;
import com.thexabyte.mylibrary.SportsTeam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    FavoritesExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<SportsTeam>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        StaticMethods.indicatorOnRight(expListView, this);
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        // Adding child data
//        listDataHeader.add("NHL");
//        listDataHeader.add("NBA");
//        listDataHeader.add("MLS");
//        listDataHeader.add("Euro 2016");
//
//        ArrayList<SportsTeam> hockey = SportsGameHandler.getNHLTeams();
//        ArrayList<SportsTeam> basketball = SportsGameHandler.getNBATeams();
//        ArrayList<SportsTeam> soccer = SportsGameHandler.getMLSTeams();
//        ArrayList<SportsTeam> euro2016 = SportsGameHandler.getEuro2016Teams();
//
//        listDataChild.put(listDataHeader.get(0), hockey); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), basketball);
//        listDataChild.put(listDataHeader.get(2), soccer);
//        listDataChild.put(listDataHeader.get(3), euro2016);
        for (int i = 0; i<Sport.allSports.length; i++){
            Sport sport = Sport.allSports[i];
            listDataHeader.add(sport.name);
            ArrayList<SportsTeam> teams = SportsGameHandler.getTeams(sport);
            listDataChild.put(listDataHeader.get(i), teams); // Header, Child data
        }
        listAdapter = new FavoritesExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

    private void saveFavorites() {

        ArrayList<HashSet<String>> favoritesList = new ArrayList<>();
        for (int i = 0; i<Sport.allSports.length; i++){
            favoritesList.add(new HashSet<String>());
            for (SportsTeam s : SportsGameHandler.getTeams(Sport.allSports[i])) {
                if (s.isFavorite) {
                    favoritesList.get(i).add(s.initials);
                }
            }
        }
        MainActivity.saveFavorites(favoritesList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //saveFavorites();
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveFavorites();
        super.onBackPressed();
    }
}
