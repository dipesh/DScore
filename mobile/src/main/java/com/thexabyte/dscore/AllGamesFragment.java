package com.thexabyte.dscore;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import com.thexabyte.mylibrary.Sport;
import com.thexabyte.mylibrary.SportsGame;

public class AllGamesFragment extends Fragment {
    public final static String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    AllGamesExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<SportsGame>> listDataChild;
    SwipeRefreshLayout swipeLayout;

    public boolean isFavoriteFragment;
    public static ArrayList<ArrayList<SportsGame>> AllGames;

    public static SportsGame gameOnWatch;

    public static AllGamesFragment newInstance(boolean isFavoriteFragment) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_PAGE, isFavoriteFragment);
        AllGamesFragment fragment = new AllGamesFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFavoriteFragment = getArguments().getBoolean(ARG_PAGE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_games, container, false);

        if (AllGames == null) {
            AllGames = new ArrayList<>();
            for (int i = 0; i < Sport.allSports.length; i++) {
                AllGames.add(new ArrayList<SportsGame>());
            }
        }

        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        StaticMethods.indicatorOnRight(expListView,getActivity());

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        expListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (expListView == null || expListView.getChildCount() == 0) ?
                                0 : expListView.getChildAt(0).getTop();
                swipeLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ArrayList<SportsGame> list = (ArrayList) listDataChild.get(listDataHeader.get(groupPosition));

                SportsGame sg = list.get(childPosition);
                if (sg.isOnWatch) {
                    //disable the service
                    sg.isOnWatch = false;
                    MainActivity.stopService();
                    gameOnWatch = null;
                } else {
                    for (ArrayList<SportsGame> gameList : AllGamesFragment.AllGames) {
                        for (SportsGame s : gameList) {
                            s.isOnWatch = false;
                        }
                    }
                    gameOnWatch = sg;
                    sg.isOnWatch = true;
                    MyService.MY_GAME = sg;
                    MainActivity.restartService();
                }
                listAdapter.notifyDataSetChanged();
                return true;
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(true);
                            getData();
                    }
                }, 00);
            }
        });
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright        );

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        for (int i = 0; i < Sport.allSports.length; i++) {
            listDataHeader.add(Sport.allSports[i].name);
        }
        //listDataHeader.add("NCAAF");
        //listDataHeader.add("NCAAB");

        listAdapter = new AllGamesExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        if (isFavoriteFragment) {
        } else {
            swipeLayoutCall();
        }

        return view;
    }
    public void swipeLayoutCall(){
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
                getData();
            }
        });
    }
    public void getData() {
        HockeyGamesAsyncTask asyncTask = new HockeyGamesAsyncTask();
        asyncTask.execute();
    }

    public class HockeyGamesAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

            ArrayList<String> httpData = new ArrayList<>();
            if (StaticMethods.isNetworkAvailable(getActivity())) {
                for (int i = 0; i< Sport.allSports.length; i++) {
                    httpData.add(InternetTools.httpGet(Sport.allSports[i].url));
                }
            } else {
                httpData.add(getResources().getString(R.string.network_connection_unavailable));
            }

            return httpData;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            loadList(result);
        }
    }

    public void loadList(ArrayList<String> result) {
        if (result.get(0).equals(getResources().getString(R.string.network_connection_unavailable))) {
            Toast.makeText(getActivity(), getResources().getString(R.string.network_connection_unavailable), Toast.LENGTH_LONG).show();
        } else {

            for (int i = 0; i< Sport.allSports.length; i++) {
                AllGames.set(i, SportsGameHandler.getGames(Sport.allSports[i], result.get(i)));
            }

            if (gameOnWatch != null) {
                for (ArrayList<SportsGame> gameList : AllGamesFragment.AllGames) {
                    for (SportsGame s : gameList) {
                        if (s.equals(gameOnWatch)) {
                            s.isOnWatch = true;
                            break;
                        }
                    }
                }
            }

            ArrayList<ArrayList<SportsGame>> allNewGames = new ArrayList<>();

            if (isFavoriteFragment) {
                loadFavoritesList();
            } else {
                for (int i = 0; i < AllGames.size(); i++) {
                    allNewGames.add(AllGames.get(i));
                }
            }
            for (int i = 0; i < allNewGames.size(); i++) {
                listDataChild.put(listDataHeader.get(i), allNewGames.get(i)); // Header, Child data
            }
            listAdapter.notifyDataSetChanged();

            for (int position = 1; position <= listDataHeader.size(); position++)
                expListView.expandGroup(position - 1);

            swipeLayout.setRefreshing(false);
        }
    }

    public void loadFavoritesList() {
        listDataHeader.clear();
        ArrayList<HashSet<String>> allFavorites = MainActivity.getAllFavorites();
        for (int i = 0; i < AllGames.size(); i++) {
            ArrayList<SportsGame> games = AllGames.get(i);
            if (games == null) {
                games = new ArrayList<>();
            }

            ArrayList<SportsGame> favoritesList = new ArrayList<>();
            HashSet<String> favoritesSet = allFavorites.get(i);
            for (SportsGame game : games) {
                String homeInitial = game.homeTeam.initials;
                String awayInitial = game.awayTeam.initials;

                if (favoritesSet.contains(homeInitial) || favoritesSet.contains(awayInitial)) {
                    favoritesList.add(game);
                }
            }

            if(favoritesList.size() != 0) {
                listDataHeader.add(Sport.allSports[i].name);
                listDataChild.put(listDataHeader.get(listDataHeader.size()-1), favoritesList);
            }
        }
        listAdapter.notifyDataSetChanged();

        for (int position = 1; position <= listDataHeader.size(); position++)
            expListView.expandGroup(position - 1);
    }

    public void updateList() {
        if (listAdapter != null) {
            if (isFavoriteFragment) {
                swipeLayoutCall();
                loadFavoritesList();
            } else {
                listAdapter.notifyDataSetChanged();
            }
        }
    }


}

