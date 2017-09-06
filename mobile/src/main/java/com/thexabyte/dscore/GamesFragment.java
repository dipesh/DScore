package com.thexabyte.dscore;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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

import com.thexabyte.mylibrary.Sport;
import com.thexabyte.mylibrary.SportsGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GamesFragment extends Fragment {

    public Sport currentSport;
    AllGamesExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<SportsGame>> listDataChild;
    SwipeRefreshLayout swipeLayout;

    List<SportsGame> games = new ArrayList<>();
    List<SportsGame> live = new ArrayList<>();
    List<SportsGame> future = new ArrayList<>();
    List<SportsGame> past = new ArrayList<>();

    public GamesFragment() {
        // Required empty public constructor
    }

    public static GamesFragment newInstance() {
        GamesFragment fragment = new GamesFragment();
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
        View view = inflater.inflate(R.layout.fragment_all_games, container, false);
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        StaticMethods.indicatorOnRight(expListView, getActivity());
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
                    AllGamesFragment.gameOnWatch = null;
                } else {

                    for (SportsGame s : games) {
                        s.isOnWatch = false;
                    }

                    AllGamesFragment.gameOnWatch = sg;
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
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright);

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Live");
        listDataHeader.add("Future");
        listDataHeader.add("Past");

        listAdapter = new AllGamesExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        return view;
    }

    public void setCurrentSport(Sport sport) {
        currentSport = sport;
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
                getData();
            }
        });
    }

    public class HockeyGamesAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            if (StaticMethods.isNetworkAvailable(getActivity())) {
                return InternetTools.httpGet(currentSport.url);
            } else {
                return getResources().getString(R.string.network_connection_unavailable);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            loadList(result);
        }
    }

    public void getData() {
        if (currentSport == null) {
            Toast.makeText(this.getActivity(), "Please select a sport", Toast.LENGTH_SHORT).show();
            swipeLayout.setRefreshing(false);
        } else {
            HockeyGamesAsyncTask asyncTask = new HockeyGamesAsyncTask();
            asyncTask.execute();
        }
    }

    public void loadList(String result) {
        if (result.equals(getResources().getString(R.string.network_connection_unavailable))) {
            Toast.makeText(getActivity(), getResources().getString(R.string.network_connection_unavailable), Toast.LENGTH_LONG).show();
        } else {
            //listDataHeader.set(0,currentSport.name);
            games = SportsGameHandler.getGames(currentSport, result);
            live = new ArrayList<>();
            future = new ArrayList<>();
            past = new ArrayList<>();

            for (SportsGame sg : games) {
                if (sg.timeStatus == SportsGame.TimeStatus.LIVE) {
                    live.add(sg);
                } else if (sg.timeStatus == SportsGame.TimeStatus.FUTURE) {
                    future.add(sg);
                } else {
                    past.add(sg);
                }
            }


            listDataChild.put(listDataHeader.get(0), live);
            listDataChild.put(listDataHeader.get(1), future);
            listDataChild.put(listDataHeader.get(2), past);

            listAdapter.notifyDataSetChanged();

            for (int position = 1; position <= listDataHeader.size(); position++)
                expListView.expandGroup(position - 1);
            swipeLayout.setRefreshing(false);
        }
    }
}
