package com.thexabyte.mylibrary;

import com.google.android.gms.wearable.DataMapItem;

/**
 * Created by Dipesh on 2/2/2016.
 */

public class SportsGame {
    public static final String SPORT_PATH = "/hockey";
    public static final String SPORT = "com.dscore.sport";

    public static final String HOME_TEAM_INITAL = "com.dscore.homeTeamInitial";
    public static final String HOME_TEAM_CITY = "com.dscore.homeTeamCity";
    public static final String HOME_TEAM_NAME = "com.dscore.homeTeamName";
    public static final String HOME_TEAM_SCORE = "com.dscore.homeTeamScore";

    public static final String AWAY_TEAM_INITAL = "com.dscore.awayTeamInitial";
    public static final String AWAY_TEAM_CITY = "com.dscore.awayTeamCity";
    public static final String AWAY_TEAM_NAME = "com.dscore.awayTeamName";
    public static final String AWAY_TEAM_SCORE = "com.dscore.awayTeamScore";

    public static final String GAME_TIME_OR_START_DATE = "com.dscore.gameTimeOrStartDate";
    public static final String GAME_STATUS_OR_START_TIME = "com.dscore.gameStatusOrStartTime";

    public String gameTimeOrStartDate;
    public String gameStatusOrStartTime;

    public SportsTeam awayTeam;
    public SportsTeam homeTeam;

    public boolean isOnWatch;
    public Sport sport;
    public TimeStatus timeStatus;
    public enum TimeStatus{
        PAST,
        LIVE,
        FUTURE,
    }

    public SportsGame(String gameTimeOrStartDate,
                      String gameStatusOrStartTime,
                      SportsTeam awayTeam,
                      SportsTeam homeTeam,
                      boolean isOnWatch,
                      Sport sport,
                      TimeStatus timeStatus) {
        this.gameTimeOrStartDate = gameTimeOrStartDate;
        this.gameStatusOrStartTime = gameStatusOrStartTime;
        this.awayTeam = awayTeam;
        this.homeTeam = homeTeam;
        this.isOnWatch = isOnWatch;
        this.sport = sport;
        this.timeStatus = timeStatus;

        //changed the value of these to show what we want in the watch face
        String[] hockeyTimeSplit = gameTimeOrStartDate.split(" ");

        if(gameTimeOrStartDate.toLowerCase().equals("final ot")){
            this.gameTimeOrStartDate = gameTimeOrStartDate;
        }
        else if (gameTimeOrStartDate.toLowerCase().contains("1st") ||
                gameTimeOrStartDate.toLowerCase().contains("2nd") ||
                gameTimeOrStartDate.toLowerCase().contains("3rd") ||
                gameTimeOrStartDate.toLowerCase().contains("4th") ||
                gameTimeOrStartDate.toLowerCase().contains("final")) {
            this.gameTimeOrStartDate = hockeyTimeSplit[0]; //top text
            if (hockeyTimeSplit.length > 1) {
                this.gameStatusOrStartTime = hockeyTimeSplit[1]; //bottom text
            } else {
                this.gameStatusOrStartTime = "";
            }
        }
    }

    //for watch faces
    public SportsGame(boolean debug) {
        this.gameTimeOrStartDate = "PRE_GAME";
        this.gameStatusOrStartTime = "LIVE";

        this.gameTimeOrStartDate = "TODAY";
        this.gameTimeOrStartDate = "SAT 3/5";
        this.gameStatusOrStartTime = "7:00 PM";

        this.gameTimeOrStartDate = "11:45";
        this.gameStatusOrStartTime = "3rd";

        this.homeTeam = new SportsTeam("MTL", "Montreal", "Canadiens", "080");
        this.awayTeam = new SportsTeam("TOR", "Toronto", "Maple Leafs", "000");

        if (!debug) {
            this.gameTimeOrStartDate = ""; //"00:00"
            this.gameStatusOrStartTime = "";

            this.homeTeam = new SportsTeam("", "", "", "");
            this.awayTeam = new SportsTeam("", "", "", "");
        }
    }

    public int changeDataCount = 0;

    public void changeData() {

        switch (changeDataCount) {
            case 0:
                this.gameTimeOrStartDate = "PRE GAME";
                this.gameStatusOrStartTime = "LIVE";
                break;
            case 1:
                this.gameTimeOrStartDate = "SAT 3/5";
                this.gameStatusOrStartTime = "7:00 PM";
                break;
            case 2:
                this.gameTimeOrStartDate = "FINAL";
                this.gameStatusOrStartTime = "OT";
                break;
            case 3:
                this.gameTimeOrStartDate = "20:00";
                this.gameStatusOrStartTime = "3rd";
                break;
            case 4:
                this.gameTimeOrStartDate = "";
                this.gameStatusOrStartTime = "No Game";
                changeDataCount = -1;
                break;
            default:
                break;
        }

        changeDataCount++;
    }

    public SportsGame(DataMapItem dataItem) {

        String awayTeamInitial = dataItem.getDataMap().getString(AWAY_TEAM_INITAL);
        String awayTeamCity = dataItem.getDataMap().getString(AWAY_TEAM_CITY);
        String awayTeamName = dataItem.getDataMap().getString(AWAY_TEAM_NAME);
        String awayTeamScore = dataItem.getDataMap().getString(AWAY_TEAM_SCORE);
        SportsTeam awayTeam = new SportsTeam(awayTeamInitial, awayTeamCity, awayTeamName, awayTeamScore);

        String homeTeamInitial = dataItem.getDataMap().getString(HOME_TEAM_INITAL);
        String homeTeamCity = dataItem.getDataMap().getString(HOME_TEAM_CITY);
        String homeTeamName = dataItem.getDataMap().getString(HOME_TEAM_NAME);
        String homeTeamScore = dataItem.getDataMap().getString(HOME_TEAM_SCORE);
        SportsTeam homeTeam = new SportsTeam(homeTeamInitial, homeTeamCity, homeTeamName, homeTeamScore);

        this.awayTeam = awayTeam;
        this.homeTeam = homeTeam;

        this.sport = Sport.fromInt(dataItem.getDataMap().getInt(SPORT));

        this.gameTimeOrStartDate = dataItem.getDataMap().getString(GAME_TIME_OR_START_DATE); //"ts":"20:00 1st, "ts":"SAT 3/5", PREGAME, TODAY
        this.gameStatusOrStartTime = dataItem.getDataMap().getString(GAME_STATUS_OR_START_TIME);  //"bs":"FINAL", "bs":"LIVE", "bs":"7:00 PM"

        //changed the value of these to show what we want in the watch face
        String[] hockeyTimeSplit = gameTimeOrStartDate.split(" ");
        if (gameTimeOrStartDate.toLowerCase().contains("1st") ||
                gameTimeOrStartDate.toLowerCase().contains("2nd") ||
                gameTimeOrStartDate.toLowerCase().contains("3rd") ||
                gameTimeOrStartDate.toLowerCase().contains("4th") ||
                gameTimeOrStartDate.toLowerCase().contains("final")) {
            gameTimeOrStartDate = hockeyTimeSplit[0]; //top text
            if (hockeyTimeSplit.length > 1) {
                gameStatusOrStartTime = hockeyTimeSplit[1]; //bottom text
            } else {
                gameStatusOrStartTime = "";
            }
        }
//        else {
//            gameTime = gameTimeOrStartDate;
//            gamePeriod = gameStatusOrStartTime;
//        }
    }

    @Override
    public boolean equals(Object obj) {
        SportsGame sg = (SportsGame) obj;
        if (sg.homeTeam.initials.equals(this.homeTeam.initials) &&
                sg.homeTeam.initials.equals(this.homeTeam.initials)) {
            return true;
        }
        return false;
    }

    public boolean gameStatusChanged(SportsGame previousGameInstance) {
        String previousGameTimeOrStartDate = previousGameInstance.gameTimeOrStartDate;

        if (previousGameTimeOrStartDate.toUpperCase().contains("TODAY") && !gameTimeOrStartDate.toUpperCase().contains("TODAY") ||
                previousGameTimeOrStartDate.toUpperCase().contains("PREGAME") && !gameTimeOrStartDate.toUpperCase().contains("PREGAME") ||
                previousGameTimeOrStartDate.toUpperCase().contains("1ST") && !gameTimeOrStartDate.toUpperCase().contains("1ST") ||
                previousGameTimeOrStartDate.toUpperCase().contains("2ND") && !gameTimeOrStartDate.toUpperCase().contains("2ND") ||
                previousGameTimeOrStartDate.toUpperCase().contains("3RD") && !gameTimeOrStartDate.toUpperCase().contains("3RD") ||
                previousGameTimeOrStartDate.toUpperCase().contains("4TH") && !gameTimeOrStartDate.toUpperCase().contains("4TH") ||
                !previousGameTimeOrStartDate.toUpperCase().contains("'") && gameTimeOrStartDate.toUpperCase().contains("'") ||
                previousGameTimeOrStartDate.toUpperCase().contains("'") && !gameTimeOrStartDate.toUpperCase().contains("'")
                ) {
            return true;
        } else {
            return false;
        }
    }

    public boolean gameScoreChanged(SportsGame previousGameInstance) {
        if(sport == Sport.NBA){ //dont vibrate for basketbasll score changes
            return  false;
        }
        if (!previousGameInstance.homeTeam.score.equals(this.homeTeam.score) ||
                !previousGameInstance.awayTeam.score.equals(this.awayTeam.score)
                ) {
            return true;
        } else {
            return false;
        }
    }

}
