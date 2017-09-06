package com.thexabyte.mylibrary;

import java.util.ArrayList;

/**
 * Created by Dipesh on 6/24/2016.
 * <p/>
 * How to add a sport:
 * 1. Add new static sport variable
 * 2. Add the sport to Sport.allSports
 * 3. Modify SportsGameHandler.getGames
 * 4. Modify SportsGameHandler.getTeams
 * 5. Add sport to the LeaguesFragment
 */
public class Sport {

    public static Sport NHL = new Sport(0, "NHL", "http://live.nhle.com/GameData/RegularSeasonScoreboardv3.jsonp");
    public static Sport NBA = new Sport(1, "NBA", "http://espn.go.com/nba/bottomline/scores");
    public static Sport NCAAF = new Sport(4, "NCAAF", "https://ca.sports.yahoo.com/college-football/teams/");

    public static Sport NCAAB = new Sport(5, "NCAAB", "");

    public static Sport MLS = new Sport(2, "MLS", "https://ca.sports.yahoo.com/soccer/mls/teams/");
    public static Sport EURO_2016 = new Sport(3, "EURO 2016", "https://uk.sports.yahoo.com/football/euro-2016/teams/");

    public static Sport PremierLeague = new Sport(6, "Premier", "https://uk.sports.yahoo.com/football/premier-league/teams/");
    public static Sport Ligue1 = new Sport(7, "Ligue 1", "https://uk.sports.yahoo.com/football/ligue-1/teams/");
    public static Sport SerieA = new Sport(8, "Serie A", "https://ca.sports.yahoo.com/soccer/serie-a/teams/");
    public static Sport FACup = new Sport(9, "FA Cup", ""); //cancelled 2016-201
    public static Sport Championship = new Sport(10, "Championship", "https://uk.sports.yahoo.com/football/championship/teams/");
    public static Sport ChampionLeague = new Sport(11, "Champion", "https://uk.sports.yahoo.com/football/champions-league/teams/");
    public static Sport EuropaLeague = new Sport(12, "Europa", "");
    public static Sport LALiga = new Sport(13, "LA Liga", "https://uk.sports.yahoo.com/football/la-liga/teams/");
    public static Sport Bundesliga = new Sport(14, "Bundesliga", "https://ca.sports.yahoo.com/soccer/bundesliga/");
    public static Sport ScottishPremiership = new Sport(15, "Scottish", "https://uk.sports.yahoo.com/football/scottish-premiership/teams/");

    public static Sport NFL = new Sport(16, "NFL", "https://ca.sports.yahoo.com/nfl/teams/");

    public static Sport UNKNOWN = new Sport(-1, "", "");

    public static Sport[] allSports = {NHL, NBA, MLS, EURO_2016, NCAAF, NCAAB, NFL,
            Championship, ScottishPremiership, PremierLeague, Ligue1, LALiga, SerieA, ChampionLeague,Bundesliga};

    public int id;
    public String url;
    public String name;

    public Sport(int id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public static Sport fromInt(int sport) {
        switch (sport) {
            case 0:
                return NHL;
            case 1:
                return NBA;
            case 2:
                return MLS;
            case 3:
                return EURO_2016;
            case 4:
                return NCAAF;
            case 5:
                return NCAAB;
            case 6:
                return PremierLeague;
            case 7:
                return SerieA;
            case 8:
                return FACup;
            case 9:
                return Ligue1;
            case 10:
                return Championship;
            case 11:
                return ChampionLeague;
            case 12:
                return EuropaLeague;
            case 13:
                return LALiga;
            case 14:
                return Bundesliga;
            case 15:
                return ScottishPremiership;
            case 16:
                return NFL;
            default:
                return UNKNOWN;
        }
    }
}