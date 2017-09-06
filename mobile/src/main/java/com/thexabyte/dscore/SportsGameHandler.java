package com.thexabyte.dscore;

import com.thexabyte.mylibrary.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by Dipesh on 3/13/2016.
 */
public class SportsGameHandler {
    private static boolean teamsInitialized = false;
    private static ArrayList<SportsTeam> hockeyTeams = new ArrayList<SportsTeam>();
    private static ArrayList<SportsTeam> nbaTeams = new ArrayList<SportsTeam>();
    private static ArrayList<SportsTeam> mlsTeams = new ArrayList<>();
    private static ArrayList<SportsTeam> euro2016Teams = new ArrayList<>();
    private static ArrayList<SportsTeam> ncaafTeams = new ArrayList<>();
    private static ArrayList<SportsTeam> ncaabTeams = new ArrayList<>();
    private static ArrayList<SportsTeam> nflTeams = new ArrayList<>();

    private static ArrayList<SportsTeam> PremierLeagueTeams = new ArrayList<>();
    private static ArrayList<SportsTeam> Ligue1Teams = new ArrayList<>();
    private static ArrayList<SportsTeam> SerieATeams = new ArrayList<>();
    private static ArrayList<SportsTeam> FACupTeams = new ArrayList<>();
    private static ArrayList<SportsTeam> ChampionshipTeams = new ArrayList<>();
    private static ArrayList<SportsTeam> ChampionLeagueTeams = new ArrayList<>();
    private static ArrayList<SportsTeam> EuropaLeagueTeams = new ArrayList<>();
    private static ArrayList<SportsTeam> LALigaTeams = new ArrayList<>();
    private static ArrayList<SportsTeam> BundesligaTeams = new ArrayList<>();
    private static ArrayList<SportsTeam> ScottishPremiershipTeams = new ArrayList<>();

    public static ArrayList<SportsGame> getGames(Sport sport, String result) {
        ArrayList<SportsGame> sportsGames = new ArrayList<>();
        if (sport == Sport.NHL) {
            sportsGames = getNHLHockeyGames(result);
        } else if (sport == Sport.NBA) {
            sportsGames = getNBAGames(result);
        } else if (sport == Sport.MLS) {
            sportsGames = getMLSGames(result);
        } else if (sport == Sport.EURO_2016) {
            sportsGames = getEuro2016Games(result);
        } else if (sport == Sport.NCAAF) {
            sportsGames = getNCAAFGames(result);
        } else if (sport == Sport.NCAAB) {
            sportsGames = getNCAABGames(result);
        } else if (sport == Sport.PremierLeague) {
            sportsGames = getPremierLeagueGames(result);
        } else if (sport == Sport.Ligue1) {
            sportsGames = getLigue1Games(result);
        } else if (sport == Sport.SerieA) {
            sportsGames = getSerieAGames(result);
        } else if (sport == Sport.FACup) {
            sportsGames = getFACupGames(result);
        } else if (sport == Sport.Championship) {
            sportsGames = getChampionshipGames(result);
        } else if (sport == Sport.ChampionLeague) {
            sportsGames = getChampionLeagueGames(result);
        } else if (sport == Sport.EuropaLeague) {
            sportsGames = getEuropaLeagueGames(result);
        } else if (sport == Sport.LALiga) {
            sportsGames = getLALigaGames(result);
        } else if (sport == Sport.Bundesliga) {
            sportsGames = getBundesligaGames(result);
        } else if (sport == Sport.ScottishPremiership) {
            sportsGames = getScottishPremiershipGames(result);
        } else if (sport == Sport.NFL) {
            sportsGames = getNFLGames(result);
        }
        return sportsGames;
    }

    public static ArrayList<SportsTeam> getTeams(Sport sport) {
        ArrayList<SportsTeam> sportsTeams = new ArrayList<>();
        if (sport == Sport.NHL) {
            sportsTeams = getNHLTeams();
        } else if (sport == Sport.NBA) {
            sportsTeams = getNBATeams();
        } else if (sport == Sport.MLS) {
            sportsTeams = getMLSTeams();
        } else if (sport == Sport.EURO_2016) {
            sportsTeams = getEuro2016Teams();
        } else if (sport == Sport.NCAAF) {
            sportsTeams = getNCAAFTeams();
        } else if (sport == Sport.NCAAB) {
            sportsTeams = getNCAABTeams();
        } else if (sport == Sport.PremierLeague) {
            sportsTeams = getPremierLeagueTeams();
        } else if (sport == Sport.Ligue1) {
            sportsTeams = getLigue1Teams();
        } else if (sport == Sport.SerieA) {
            sportsTeams = getSerieATeams();
        } else if (sport == Sport.FACup) {
            sportsTeams = getFACupTeams();
        } else if (sport == Sport.Championship) {
            sportsTeams = getChampionshipTeams();
        } else if (sport == Sport.ChampionLeague) {
            sportsTeams = getChampionLeagueTeams();
        } else if (sport == Sport.EuropaLeague) {
            sportsTeams = getEuropaLeagueTeams();
        } else if (sport == Sport.LALiga) {
            sportsTeams = getLALigaTeams();
        } else if (sport == Sport.Bundesliga) {
            sportsTeams = getBundesligaTeams();
        } else if (sport == Sport.ScottishPremiership) {
            sportsTeams = getScottishPremiershipTeams();
        } else if (sport == Sport.NFL) {
            sportsTeams = getNFLTeams();
        }

        return sportsTeams;
    }

    // Initialize Teams
    private static void InitializeNBAList() {
        nbaTeams.add(new SportsTeam("ATL", "Atlanta", "Hawks", false));
        nbaTeams.add(new SportsTeam("BKN", "Brooklyn", "Nets", false));
        nbaTeams.add(new SportsTeam("BOS", "Boston", "Celtics", false));
        nbaTeams.add(new SportsTeam("CHA", "Charlotte", "Hornets", false));
        nbaTeams.add(new SportsTeam("CHI", "Chicago", "Bulls", false));
        nbaTeams.add(new SportsTeam("CLE", "Cleveland", "Cavaliers", false));
        nbaTeams.add(new SportsTeam("DAL", "Dallas", "Mavericks", false));
        nbaTeams.add(new SportsTeam("DEN", "Denver", "Nuggets", false));
        nbaTeams.add(new SportsTeam("DET", "Detroit", "Pistons", false));
        nbaTeams.add(new SportsTeam("GS", "Golden State", "Warriors", false));
        nbaTeams.add(new SportsTeam("HOU", "Houston", "Rockets", false));
        nbaTeams.add(new SportsTeam("IND", "Indiana", "Pacers", false));
        nbaTeams.add(new SportsTeam("LAC", "Los Angeles", "Clippers", false));
        nbaTeams.add(new SportsTeam("LAL", "Los Angeles", "Lakers", false));
        nbaTeams.add(new SportsTeam("MEM", "Memphis", "Grizzlies", false));
        nbaTeams.add(new SportsTeam("MIA", "Miami", "Heat", false));
        nbaTeams.add(new SportsTeam("MIL", "Milwaukee", "Bucks", false));
        nbaTeams.add(new SportsTeam("MIN", "Minnesota", "Timberwolves", false));
        nbaTeams.add(new SportsTeam("NOP", "New Orleans", "Pelicans", false));
        nbaTeams.add(new SportsTeam("NYK", "New York", "Knicks", false));
        nbaTeams.add(new SportsTeam("OKC", "Oklahoma City", "Thunder", false));
        nbaTeams.add(new SportsTeam("ORL", "Orlando", "Magic", false));
        nbaTeams.add(new SportsTeam("PHI", "Philadelphia", "76ers", false));
        nbaTeams.add(new SportsTeam("PHX", "Phoenix", "Suns", false));
        nbaTeams.add(new SportsTeam("POR", "Portland", "Trail Blazers", false));
        nbaTeams.add(new SportsTeam("SAC", "Sacramento", "Kings", false));
        nbaTeams.add(new SportsTeam("SA", "San Antonio", "Spurs", false));
        nbaTeams.add(new SportsTeam("TOR", "Toronto", "Raptors", false));
        nbaTeams.add(new SportsTeam("UTA", "Utah", "Jazz", false));
        nbaTeams.add(new SportsTeam("WSH", "Washington", "Wizards", false));

        HashSet<String> set = MainActivity.getFavorite(Sport.NBA.name);


        for (int i = 0; i < nbaTeams.size(); i++) {
            SportsTeam team = nbaTeams.get(i);
            if (set.contains(team.initials)) {
                team.isFavorite = true;
            }
        }
    }

    private static void InitializeNHLList() {
        hockeyTeams.add(new SportsTeam("ANA", "Anaheim", "Ducks", false));
        hockeyTeams.add(new SportsTeam("ARI", "Arizona", "Coyotes", false));
        hockeyTeams.add(new SportsTeam("BOS", "Boston", "Bruins", false));
        hockeyTeams.add(new SportsTeam("BUF", "Buffalo", "Sabres", false));
        hockeyTeams.add(new SportsTeam("CGY", "Calgary", "Flames", false));
        hockeyTeams.add(new SportsTeam("CAR", "Carolina", "Hurricanes", false));
        hockeyTeams.add(new SportsTeam("CHI", "Chicago", "Black Hawks", false));
        hockeyTeams.add(new SportsTeam("COL", "Colorado", "Avalanche", false));
        hockeyTeams.add(new SportsTeam("CBJ", "Columbus", "Blue Jackets", false));
        hockeyTeams.add(new SportsTeam("DAL", "Dallas", "Stars", false));
        hockeyTeams.add(new SportsTeam("DET", "Detroit", "Red Wings", false));
        hockeyTeams.add(new SportsTeam("EDM", "Edmonton", "Oilers", false));
        hockeyTeams.add(new SportsTeam("FLA", "Florida", "Panthers", false));
        hockeyTeams.add(new SportsTeam("LAK", "Los Angeles", "Kings", false));
        hockeyTeams.add(new SportsTeam("MIN", "Minnesota", "Wild", false));
        hockeyTeams.add(new SportsTeam("MTL", "Montreal", "Canadiens", false));
        hockeyTeams.add(new SportsTeam("NSH", "Nashville", "Predators", false));
        hockeyTeams.add(new SportsTeam("NJD", "New Jersey", "Devils", false));
        hockeyTeams.add(new SportsTeam("NYI", "New York", "Islanders", false));
        hockeyTeams.add(new SportsTeam("NYR", "New York", "Rangers", false));
        hockeyTeams.add(new SportsTeam("OTT", "Ottawa", "Senators", false));
        hockeyTeams.add(new SportsTeam("PHI", "Philadelphia", "Flyers", false));
        hockeyTeams.add(new SportsTeam("PIT", "Pittsburgh", "Penguins", false));
        hockeyTeams.add(new SportsTeam("SJS", "San Jose", "Sharks", false));
        hockeyTeams.add(new SportsTeam("STL", "St Louis", "Blues", false));
        hockeyTeams.add(new SportsTeam("TBL", "Tampa Bay", "Lightning", false));
        hockeyTeams.add(new SportsTeam("TOR", "Toronto", "Maple Leafs", false));
        hockeyTeams.add(new SportsTeam("VAN", "Vancouver", "Canucks", false));
        hockeyTeams.add(new SportsTeam("WSH", "Washington", "Capitals", false));
        hockeyTeams.add(new SportsTeam("WPG", "Winnipeg", "Jets", false));


        HashSet<String> hockeyGamesFavoritesSet = MainActivity.getFavorite(Sport.NHL.name);

        for (int i = 0; i < hockeyTeams.size(); i++) {
            SportsTeam team = hockeyTeams.get(i);
            if (hockeyGamesFavoritesSet.contains(team.initials)) {
                team.isFavorite = true;
            }
        }
    }

    private static void InitializeMLSList() {
        mlsTeams.add(new SportsTeam("CHI", "Chicago", "Fire", false));
        mlsTeams.add(new SportsTeam("COL", "Colorado", "Rapids", false));
        mlsTeams.add(new SportsTeam("CLB", "Columbus", "Crew", false));
        mlsTeams.add(new SportsTeam("DCU", "D.C.", "United", false));
        mlsTeams.add(new SportsTeam("FCD", "FC", "Dallas", false));
        mlsTeams.add(new SportsTeam("HOU", "Houston", "Dynamo", false));
        mlsTeams.add(new SportsTeam("LAG", "LA", "Galaxy", false));
        mlsTeams.add(new SportsTeam("MTL", "Montreal", "Impact", false));
        mlsTeams.add(new SportsTeam("NE", "New England", "Revolution", false));
        mlsTeams.add(new SportsTeam("NYRB", "New York", "Red Bulls", false));
        mlsTeams.add(new SportsTeam("NYC", "New York City", "FC", false));
        mlsTeams.add(new SportsTeam("ORL", "Orlando City", "SC", false));
        mlsTeams.add(new SportsTeam("PHI", "Philadelphia", "Union", false));
        mlsTeams.add(new SportsTeam("POR", "Portland", "Timbers", false));
        mlsTeams.add(new SportsTeam("RSL", "Real", "Salt Lake", false));
        mlsTeams.add(new SportsTeam("SJ", "San Jose", "Earthquakes", false));
        mlsTeams.add(new SportsTeam("SEA", "Seattle Sounders", "FC", false));
        mlsTeams.add(new SportsTeam("SKC", "Sporting", "Kansas City", false));
        mlsTeams.add(new SportsTeam("TFC", "Toronto", "FC", false));
        mlsTeams.add(new SportsTeam("VAN", "Vancouver", "Whitecaps", false));

        HashSet<String> set = MainActivity.getFavorite(Sport.MLS.name);

        for (int i = 0; i < mlsTeams.size(); i++) {
            SportsTeam team = mlsTeams.get(i);
            if (set.contains(team.initials)) {
                team.isFavorite = true;
            }
        }
    }

    private static void InitializeEuro2016List() {
        euro2016Teams.add(new SportsTeam("ALB", "", "Albania", false));
        euro2016Teams.add(new SportsTeam("AUS", "", "Austria", false));
        euro2016Teams.add(new SportsTeam("BEL", "", "Belgium", false));
        euro2016Teams.add(new SportsTeam("CRO", "", "Croatia", false));
        euro2016Teams.add(new SportsTeam("CZE", "", "Czech Republic", false));
        euro2016Teams.add(new SportsTeam("ENG", "", "England", false));
        euro2016Teams.add(new SportsTeam("FRA", "", "France", false));
        euro2016Teams.add(new SportsTeam("GER", "", "Germany", false));
        euro2016Teams.add(new SportsTeam("HUN", "", "Hungary", false));
        euro2016Teams.add(new SportsTeam("ICE", "", "Iceland", false));
        euro2016Teams.add(new SportsTeam("ITA", "", "Italy", false));
        euro2016Teams.add(new SportsTeam("NIR", "", "Northern Ireland", false));
        euro2016Teams.add(new SportsTeam("POL", "", "Poland", false));
        euro2016Teams.add(new SportsTeam("POR", "", "Portugal", false));
        euro2016Teams.add(new SportsTeam("ROI", "", "Republic of Ireland", false));
        euro2016Teams.add(new SportsTeam("ROM", "", "Romania", false));
        euro2016Teams.add(new SportsTeam("RUS", "", "Russia", false));
        euro2016Teams.add(new SportsTeam("SLO", "", "Slovakia", false));
        euro2016Teams.add(new SportsTeam("SPA", "", "Spain", false));
        euro2016Teams.add(new SportsTeam("SWE", "", "Sweden", false));
        euro2016Teams.add(new SportsTeam("SWI", "", "Switzerland", false));
        euro2016Teams.add(new SportsTeam("TUR", "", "Turkey", false));
        euro2016Teams.add(new SportsTeam("UKR", "", "Ukraine", false));
        euro2016Teams.add(new SportsTeam("WAL", "", "Wales", false));

        HashSet<String> set = MainActivity.getFavorite(Sport.EURO_2016.name);

        for (int i = 0; i < euro2016Teams.size(); i++) {
            SportsTeam team = euro2016Teams.get(i);
            if (set.contains(team.initials)) {
                team.isFavorite = true;
            }
        }
    }

    private static void InitializeNCAAFList() {
        ncaafTeams.add(new SportsTeam("AB CH", "Abilene Christian", "", false));
        ncaafTeams.add(new SportsTeam("ALDBRO", "A-broaddus", "", false));
        ncaafTeams.add(new SportsTeam("AFA", "Air Force", "", false));
        ncaafTeams.add(new SportsTeam("AKRN", "Akron", "", false));
        ncaafTeams.add(new SportsTeam("ALA", "Alabama", "", false));
        ncaafTeams.add(new SportsTeam("AL A&M", "Alabama A&M", "", false));
        ncaafTeams.add(new SportsTeam("AL ST", "Alabama St.", "", false));
        ncaafTeams.add(new SportsTeam("ALB", "Albany", "", false));
        ncaafTeams.add(new SportsTeam("ALCR", "Alcorn St.", "", false));
        ncaafTeams.add(new SportsTeam("AP ST", "Appalachian St.", "", false));
        ncaafTeams.add(new SportsTeam("UAPB", "AR Pine Bluff", "", false));
        ncaafTeams.add(new SportsTeam("ARIZ", "Arizona", "", false));
        ncaafTeams.add(new SportsTeam("AZ ST", "Arizona St.", "", false));
        ncaafTeams.add(new SportsTeam("ARK", "Arkansas", "", false));
        ncaafTeams.add(new SportsTeam("AR ST", "Arkansas St.", "", false));
        ncaafTeams.add(new SportsTeam("ARMY", "Army", "", false));
        ncaafTeams.add(new SportsTeam("AUB", "Auburn", "", false));
        ncaafTeams.add(new SportsTeam("PEAY", "Austin Peay", "", false));
        ncaafTeams.add(new SportsTeam("ATCHRI", "Atlantic Christian", "", false));
        ncaafTeams.add(new SportsTeam("BALL", "Ball St.", "", false));
        ncaafTeams.add(new SportsTeam("BAYL", "Baylor", "", false));
        ncaafTeams.add(new SportsTeam("BCC", "Bethune-Cookman", "", false));
        ncaafTeams.add(new SportsTeam("BLUEFI", "Bluefield", "", false));
        ncaafTeams.add(new SportsTeam("BOIS", "Boise St.", "", false));
        ncaafTeams.add(new SportsTeam("BC", "Boston Coll.", "", false));
        ncaafTeams.add(new SportsTeam("BGSU", "Bowling Green", "", false));
        ncaafTeams.add(new SportsTeam("BRWN", "Brown", "", false));
        ncaafTeams.add(new SportsTeam("BRYT", "Bryant", "", false));
        ncaafTeams.add(new SportsTeam("BUCK", "Bucknell", "", false));
        ncaafTeams.add(new SportsTeam("BUFF", "Buffalo", "", false));
        ncaafTeams.add(new SportsTeam("BUTL", "Butler", "", false));
        ncaafTeams.add(new SportsTeam("BYU", "BYU", "", false));
        ncaafTeams.add(new SportsTeam("CPSLO", "Cal Poly", "", false));
        ncaafTeams.add(new SportsTeam("CAL", "California", "", false));
        ncaafTeams.add(new SportsTeam("CAMP", "Campbell", "", false));
        ncaafTeams.add(new SportsTeam("CMU", "Cent. Michigan", "", false));
        ncaafTeams.add(new SportsTeam("UCA", "Central Arkansas", "", false));
        ncaafTeams.add(new SportsTeam("CCSU", "Central Conn. St.", "", false));
        ncaafTeams.add(new SportsTeam("CST", "Central St.", "", false));
        ncaafTeams.add(new SportsTeam("CWA", "Central Washington", "", false));
        ncaafTeams.add(new SportsTeam("CH SO", "Charleston Sou.", "", false));
        ncaafTeams.add(new SportsTeam("CHAR", "Charlotte", "", false));
        ncaafTeams.add(new SportsTeam("CHAT", "Chattanooga", "", false));
        ncaafTeams.add(new SportsTeam("CHWN", "Chowan", "", false));
        ncaafTeams.add(new SportsTeam("CINC", "Cincinnati", "", false));
        ncaafTeams.add(new SportsTeam("CIT", "Citadel", "", false));
        ncaafTeams.add(new SportsTeam("CLEM", "Clemson", "", false));
        ncaafTeams.add(new SportsTeam("CCU", "Coastal Car.", "", false));
        ncaafTeams.add(new SportsTeam("COLG", "Colgate", "", false));
        ncaafTeams.add(new SportsTeam("COLO", "Colorado", "", false));
        ncaafTeams.add(new SportsTeam("CO ST", "Colorado St.", "", false));
        ncaafTeams.add(new SportsTeam("COLU", "Columbia", "", false));
        ncaafTeams.add(new SportsTeam("CONN", "Connecticut", "", false));
        ncaafTeams.add(new SportsTeam("CONCMI", "Concordia College", "", false));
        ncaafTeams.add(new SportsTeam("CORN", "Cornell", "", false));
        ncaafTeams.add(new SportsTeam("DART", "Dartmouth", "", false));
        ncaafTeams.add(new SportsTeam("DAV", "Davidson", "", false));
        ncaafTeams.add(new SportsTeam("DAYT", "Dayton", "", false));
        ncaafTeams.add(new SportsTeam("DELA", "Delaware", "", false));
        ncaafTeams.add(new SportsTeam("DE ST", "Delaware St.", "", false));
        ncaafTeams.add(new SportsTeam("DRAK", "Drake", "", false));
        ncaafTeams.add(new SportsTeam("DUKE", "Duke", "", false));
        ncaafTeams.add(new SportsTeam("DUQ", "Duquesne", "", false));
        ncaafTeams.add(new SportsTeam("EKU", "E. Kentucky", "", false));
        ncaafTeams.add(new SportsTeam("ETSU", "E. Tennessee St.", "", false));
        ncaafTeams.add(new SportsTeam("ECU", "East Carolina", "", false));
        ncaafTeams.add(new SportsTeam("EMU", "East. Michigan", "", false));
        ncaafTeams.add(new SportsTeam("EIU", "Eastern Illinois", "", false));
        ncaafTeams.add(new SportsTeam("EWU", "Eastern Wash.", "", false));
        ncaafTeams.add(new SportsTeam("ECSU", "Elizabeth City St.", "", false));
        ncaafTeams.add(new SportsTeam("ELON", "Elon", "", false));
        ncaafTeams.add(new SportsTeam("FLA", "Florida", "", false));
        ncaafTeams.add(new SportsTeam("FAMU", "Florida A&M", "", false));
        ncaafTeams.add(new SportsTeam("FAU", "Florida Atlantic", "", false));
        ncaafTeams.add(new SportsTeam("FIU", "Florida Intl.", "", false));
        ncaafTeams.add(new SportsTeam("FL ST", "Florida St.", "", false));
        ncaafTeams.add(new SportsTeam("FORD", "Fordham", "", false));
        ncaafTeams.add(new SportsTeam("FRES", "Fresno St.", "", false));
        ncaafTeams.add(new SportsTeam("FURM", "Furman", "", false));
        ncaafTeams.add(new SportsTeam("FRNK", "Franklin", "", false));
        ncaafTeams.add(new SportsTeam("G WBB", "Gardner-Webb", "", false));
        ncaafTeams.add(new SportsTeam("GTWN", "Georgetown", "", false));
        ncaafTeams.add(new SportsTeam("GA", "Georgia", "", false));
        ncaafTeams.add(new SportsTeam("GA SO", "Georgia Southern", "", false));
        ncaafTeams.add(new SportsTeam("GA ST", "Georgia St.", "", false));
        ncaafTeams.add(new SportsTeam("GT", "Georgia Tech", "", false));
        ncaafTeams.add(new SportsTeam("GRAM", "Grambling St.", "", false));
        ncaafTeams.add(new SportsTeam("HAM", "Hampton", "", false));
        ncaafTeams.add(new SportsTeam("HARV", "Harvard", "", false));
        ncaafTeams.add(new SportsTeam("HAW", "Hawaii", "", false));
        ncaafTeams.add(new SportsTeam("HC", "Holy Cross", "", false));
        ncaafTeams.add(new SportsTeam("HOUS", "Houston", "", false));
        ncaafTeams.add(new SportsTeam("HBU", "Houston Baptist", "", false));
        ncaafTeams.add(new SportsTeam("HOW", "Howard", "", false));
        ncaafTeams.add(new SportsTeam("IDA", "Idaho", "", false));
        ncaafTeams.add(new SportsTeam("ID ST", "Idaho St.", "", false));
        ncaafTeams.add(new SportsTeam("ILL", "Illinois", "", false));
        ncaafTeams.add(new SportsTeam("IL ST", "Illinois St.", "", false));
        ncaafTeams.add(new SportsTeam("IW", "Incarnate Word", "", false));
        ncaafTeams.add(new SportsTeam("IND", "Indiana", "", false));
        ncaafTeams.add(new SportsTeam("IN ST", "Indiana St.", "", false));
        ncaafTeams.add(new SportsTeam("IOWA", "Iowa", "", false));
        ncaafTeams.add(new SportsTeam("IA ST", "Iowa St.", "", false));
        ncaafTeams.add(new SportsTeam("JACK", "Jackson St.", "", false));
        ncaafTeams.add(new SportsTeam("JXVL", "Jacksonville", "", false));
        ncaafTeams.add(new SportsTeam("JV ST", "Jacksonville St.", "", false));
        ncaafTeams.add(new SportsTeam("JMU", "James Madison", "", false));
        ncaafTeams.add(new SportsTeam("KAN", "Kansas", "", false));
        ncaafTeams.add(new SportsTeam("KS ST", "Kansas St.", "", false));
        ncaafTeams.add(new SportsTeam("KENN", "Kennesaw St.", "", false));
        ncaafTeams.add(new SportsTeam("KENT", "Kent St.", "", false));
        ncaafTeams.add(new SportsTeam("UK", "Kentucky", "", false));
        ncaafTeams.add(new SportsTeam("KY ST", "Kentucky St.", "", false));
        ncaafTeams.add(new SportsTeam("KYWE", "KY Wesleyan", "", false));
        ncaafTeams.add(new SportsTeam("ULL", "La Lafayet.", "", false));
        ncaafTeams.add(new SportsTeam("ULM", "LA Monroe", "", false));
        ncaafTeams.add(new SportsTeam("LAFA", "Lafayette", "", false));
        ncaafTeams.add(new SportsTeam("LAMR", "Lamar", "", false));
        ncaafTeams.add(new SportsTeam("LEH", "Lehigh", "", false));
        ncaafTeams.add(new SportsTeam("LIB", "Liberty", "", false));
        ncaafTeams.add(new SportsTeam("LT", "Louisiana Tech", "", false));
        ncaafTeams.add(new SportsTeam("LOU", "Louisville", "", false));
        ncaafTeams.add(new SportsTeam("LSU", "LSU", "", false));
        ncaafTeams.add(new SportsTeam("MAIN", "Maine", "", false));
        ncaafTeams.add(new SportsTeam("MARI", "Marist", "", false));
        ncaafTeams.add(new SportsTeam("MRSH", "Marshall", "", false));
        ncaafTeams.add(new SportsTeam("MD", "Maryland", "", false));
        ncaafTeams.add(new SportsTeam("MCN", "McNeese St.", "", false));
        ncaafTeams.add(new SportsTeam("MEMP", "Memphis", "", false));
        ncaafTeams.add(new SportsTeam("MERC", "Mercer", "", false));
        ncaafTeams.add(new SportsTeam("MERR", "Merrimack", "", false));
        ncaafTeams.add(new SportsTeam("MIA", "Miami", "", false));
        ncaafTeams.add(new SportsTeam("MIA F", "Miami (FL)", "", false));
        ncaafTeams.add(new SportsTeam("MIA O", "Miami (OH)", "", false));
        ncaafTeams.add(new SportsTeam("MICH", "Michigan", "", false));
        ncaafTeams.add(new SportsTeam("MI ST", "Michigan St.", "", false));
        ncaafTeams.add(new SportsTeam("MTSU", "Middle Tenn. St.", "", false));
        ncaafTeams.add(new SportsTeam("MINN", "Minnesota", "", false));
        ncaafTeams.add(new SportsTeam("MVSU", "Miss. Valley St.", "", false));
        ncaafTeams.add(new SportsTeam("MISS", "Mississippi", "", false));
        ncaafTeams.add(new SportsTeam("MS ST", "Mississippi St.", "", false));
        ncaafTeams.add(new SportsTeam("MIZZ", "Missouri", "", false));
        ncaafTeams.add(new SportsTeam("MO ST", "Missouri St.", "", false));
        ncaafTeams.add(new SportsTeam("MONM", "Monmouth", "", false));
        ncaafTeams.add(new SportsTeam("MONT", "Montana", "", false));
        ncaafTeams.add(new SportsTeam("MT ST", "Montana St.", "", false));
        ncaafTeams.add(new SportsTeam("MRHD", "Morehead St.", "", false));
        ncaafTeams.add(new SportsTeam("MORG", "Morgan St.", "", false));
        ncaafTeams.add(new SportsTeam("MURR", "Murray St.", "", false));
        ncaafTeams.add(new SportsTeam("NC", "N. Carolina", "", false));
        ncaafTeams.add(new SportsTeam("ND ST", "N. Dak. St.", "", false));
        ncaafTeams.add(new SportsTeam("NM ST", "N. Mex. St.", "", false));
        ncaafTeams.add(new SportsTeam("NC A&T", "N.C. A&T", "", false));
        ncaafTeams.add(new SportsTeam("NCCU", "N.C. Central", "", false));
        ncaafTeams.add(new SportsTeam("NC ST", "N.C. State", "", false));
        ncaafTeams.add(new SportsTeam("NAVY", "Navy", "", false));
        ncaafTeams.add(new SportsTeam("NEB", "Nebraska", "", false));
        ncaafTeams.add(new SportsTeam("NEV", "Nevada", "", false));
        ncaafTeams.add(new SportsTeam("NH", "New Hampshire", "", false));
        ncaafTeams.add(new SportsTeam("N MEX", "New Mexico", "", false));
        ncaafTeams.add(new SportsTeam("NICH", "Nicholls", "", false));
        ncaafTeams.add(new SportsTeam("NORF", "Norfolk St.", "", false));
        ncaafTeams.add(new SportsTeam("N DAK", "North Dakota", "", false));
        ncaafTeams.add(new SportsTeam("N TX", "North Texas", "", false));
        ncaafTeams.add(new SportsTeam("NAU", "Northern Arizona", "", false));
        ncaafTeams.add(new SportsTeam("N CO", "Northern Colorado", "", false));
        ncaafTeams.add(new SportsTeam("N ILL", "Northern Illinois", "", false));
        ncaafTeams.add(new SportsTeam("N IOWA", "Northern Iowa", "", false));
        ncaafTeams.add(new SportsTeam("N.W.", "Northwestern", "", false));
        ncaafTeams.add(new SportsTeam("ND", "Notre Dame", "", false));
        ncaafTeams.add(new SportsTeam("NW ST", "N'western St.", "", false));
        ncaafTeams.add(new SportsTeam("OHIO", "Ohio", "", false));
        ncaafTeams.add(new SportsTeam("OH ST", "Ohio St.", "", false));
        ncaafTeams.add(new SportsTeam("OKLA", "Oklahoma", "", false));
        ncaafTeams.add(new SportsTeam("OK ST", "Oklahoma St.", "", false));
        ncaafTeams.add(new SportsTeam("ODU", "Old Dominion", "", false));
        ncaafTeams.add(new SportsTeam("ORE", "Oregon", "", false));
        ncaafTeams.add(new SportsTeam("OR ST", "Oregon St.", "", false));
        ncaafTeams.add(new SportsTeam("PANHST", "Panhandle St.", "", false));
        ncaafTeams.add(new SportsTeam("PSU", "Penn St.", "", false));
        ncaafTeams.add(new SportsTeam("PENN", "Pennsylvania", "", false));
        ncaafTeams.add(new SportsTeam("PITT", "Pittsburgh", "", false));
        ncaafTeams.add(new SportsTeam("POR ST", "Portland St.", "", false));
        ncaafTeams.add(new SportsTeam("PV A&M", "Prairie View A&M", "", false));
        ncaafTeams.add(new SportsTeam("PRES", "Presbyterian", "", false));
        ncaafTeams.add(new SportsTeam("PRIN", "Princeton", "", false));
        ncaafTeams.add(new SportsTeam("PURD", "Purdue", "", false));
        ncaafTeams.add(new SportsTeam("PIKECO", "Pikeville College", "", false));
        ncaafTeams.add(new SportsTeam("QNCY", "Quincy Hawks", "", false));
        ncaafTeams.add(new SportsTeam("RI", "Rhode Island", "", false));
        ncaafTeams.add(new SportsTeam("RICE", "Rice", "", false));
        ncaafTeams.add(new SportsTeam("RICH", "Richmond", "", false));
        ncaafTeams.add(new SportsTeam("RMU", "Robert Morris", "", false));
        ncaafTeams.add(new SportsTeam("RCKMT", "Rocky Mountain", "", false));
        ncaafTeams.add(new SportsTeam("RUTG", "Rutgers", "", false));
        ncaafTeams.add(new SportsTeam("SC ST", "S. Carolina St.", "", false));
        ncaafTeams.add(new SportsTeam("SDK ST", "S. Dakota St.", "", false));
        ncaafTeams.add(new SportsTeam("SDG ST", "S. Diego St.", "", false));
        ncaafTeams.add(new SportsTeam("SFA", "S.F. Austin", "", false));
        ncaafTeams.add(new SportsTeam("CSUS", "Sacramento St.", "", false));
        ncaafTeams.add(new SportsTeam("SAC HT", "Sacred Heart", "", false));
        ncaafTeams.add(new SportsTeam("SAM H", "Sam Houston St.", "", false));
        ncaafTeams.add(new SportsTeam("", "Samford", "", false));
        ncaafTeams.add(new SportsTeam("SAN D", "San Diego", "", false));
        ncaafTeams.add(new SportsTeam("SJSU", "San Jose St.", "", false));
        ncaafTeams.add(new SportsTeam("SAV ST", "Savannah St.", "", false));
        ncaafTeams.add(new SportsTeam("SE LA", "SE Louisiana", "", false));
        ncaafTeams.add(new SportsTeam("SEMO", "SE Missouri St.", "", false));
        ncaafTeams.add(new SportsTeam("SHORTE", "Shorter", "", false));
        ncaafTeams.add(new SportsTeam("SIMF", "Simon Fraser", "", false));
        ncaafTeams.add(new SportsTeam("SMU", "SMU", "", false));
        ncaafTeams.add(new SportsTeam("S ALA", "South Alabama", "", false));
        ncaafTeams.add(new SportsTeam("S CAR", "South Carolina", "", false));
        ncaafTeams.add(new SportsTeam("S DAK", "South Dakota", "", false));
        ncaafTeams.add(new SportsTeam("S FLA", "South Florida", "", false));
        ncaafTeams.add(new SportsTeam("SOR", "Southern Oregon", "", false));
        ncaafTeams.add(new SportsTeam("SOU", "Southern", "", false));
        ncaafTeams.add(new SportsTeam("S ILL", "Southern Ill.", "", false));
        ncaafTeams.add(new SportsTeam("USM", "Southern Miss", "", false));
        ncaafTeams.add(new SportsTeam("SUU", "Southern Utah", "", false));
        ncaafTeams.add(new SportsTeam("SWESTE", "Southwestern", "", false));
        ncaafTeams.add(new SportsTeam("STANSL", "St. Anselm", "", false));
        ncaafTeams.add(new SportsTeam("STAU", "St. Augustine", "", false));
        ncaafTeams.add(new SportsTeam("STF PA", "St. Francis (PA)", "", false));
        ncaafTeams.add(new SportsTeam("STAN", "Stanford", "", false));
        ncaafTeams.add(new SportsTeam("STET", "Stetson", "", false));
        ncaafTeams.add(new SportsTeam("STONY", "Stony Brook", "", false));
        ncaafTeams.add(new SportsTeam("SYRA", "Syracuse", "", false));
        ncaafTeams.add(new SportsTeam("TARL", "Tarleton St.", "", false));
        ncaafTeams.add(new SportsTeam("TCU", "TCU", "", false));
        ncaafTeams.add(new SportsTeam("TEMP", "Temple", "", false));
        ncaafTeams.add(new SportsTeam("TENN", "Tennessee", "", false));
        ncaafTeams.add(new SportsTeam("TN ST", "Tennessee St.", "", false));
        ncaafTeams.add(new SportsTeam("TN TCH", "Tennessee Tech", "", false));
        ncaafTeams.add(new SportsTeam("UTM", "Tenn-Martin", "", false));
        ncaafTeams.add(new SportsTeam("TXA&MK", "Tex A&M Kingsville", "", false));
        ncaafTeams.add(new SportsTeam("TEX", "Texas", "", false));
        ncaafTeams.add(new SportsTeam("TAMU", "Texas A&M", "", false));
        ncaafTeams.add(new SportsTeam("TX SO", "Texas Southern", "", false));
        ncaafTeams.add(new SportsTeam("TX ST", "Texas St.", "", false));
        ncaafTeams.add(new SportsTeam("TX TCH", "Texas Tech", "", false));
        ncaafTeams.add(new SportsTeam("TOLE", "Toledo", "", false));
        ncaafTeams.add(new SportsTeam("TOWS", "Towson", "", false));
        ncaafTeams.add(new SportsTeam("TROY", "Troy", "", false));
        ncaafTeams.add(new SportsTeam("TULA", "Tulane", "", false));
        ncaafTeams.add(new SportsTeam("TULS", "Tulsa", "", false));
        ncaafTeams.add(new SportsTeam("UCD", "UC Davis", "", false));
        ncaafTeams.add(new SportsTeam("UCF", "UCF", "", false));
        ncaafTeams.add(new SportsTeam("UCLA", "UCLA", "", false));
        ncaafTeams.add(new SportsTeam("MASS", "UMass", "", false));
        ncaafTeams.add(new SportsTeam("UNLV", "UNLV", "", false));
        ncaafTeams.add(new SportsTeam("USC", "USC", "", false));
        ncaafTeams.add(new SportsTeam("UTAH", "Utah", "", false));
        ncaafTeams.add(new SportsTeam("UT ST", "Utah St.", "", false));
        ncaafTeams.add(new SportsTeam("UTEP", "UTEP", "", false));
        ncaafTeams.add(new SportsTeam("UTSA", "UTSA", "", false));
        ncaafTeams.add(new SportsTeam("VALP", "Valparaiso", "", false));
        ncaafTeams.add(new SportsTeam("VAND", "Vanderbilt", "", false));
        ncaafTeams.add(new SportsTeam("VILL", "Villanova", "", false));
        ncaafTeams.add(new SportsTeam("UVA", "Virginia", "", false));
        ncaafTeams.add(new SportsTeam("VMI", "Virginia Military", "", false));
        ncaafTeams.add(new SportsTeam("VT", "Virginia Tech", "", false));
        ncaafTeams.add(new SportsTeam("VIRG", "Virginia-Lynchburg", "", false));
        ncaafTeams.add(new SportsTeam("WKU", "W. Kentucky", "", false));
        ncaafTeams.add(new SportsTeam("WMU", "W. Michigan", "", false));
        ncaafTeams.add(new SportsTeam("WAGN", "Wagner", "", false));
        ncaafTeams.add(new SportsTeam("WAKE", "Wake Forest", "", false));
        ncaafTeams.add(new SportsTeam("WSC", "Warner Southern", "", false));
        ncaafTeams.add(new SportsTeam("WASH", "Washington", "", false));
        ncaafTeams.add(new SportsTeam("WA ST", "Washington St.", "", false));
        ncaafTeams.add(new SportsTeam("WEBR", "Weber St.", "", false));
        ncaafTeams.add(new SportsTeam("W VA", "West Virginia", "", false));
        ncaafTeams.add(new SportsTeam("WCU", "Western Carolina", "", false));
        ncaafTeams.add(new SportsTeam("WIU", "Western Ill.", "", false));
        ncaafTeams.add(new SportsTeam("WNM", "Western New Mexico", "", false));
        ncaafTeams.add(new SportsTeam("WOR", "Western Oregon", "", false));
        ncaafTeams.add(new SportsTeam("WAL", "West Alabama", "", false));
        ncaafTeams.add(new SportsTeam("W&M", "William & Mary", "", false));
        ncaafTeams.add(new SportsTeam("WIS", "Wisconsin", "", false));
        ncaafTeams.add(new SportsTeam("WOFF", "Wofford", "", false));
        ncaafTeams.add(new SportsTeam("WYO", "Wyoming", "", false));
        ncaafTeams.add(new SportsTeam("YALE", "Yale", "", false));
        ncaafTeams.add(new SportsTeam("YSU", "Youngstown St.", "", false));
        ncaafTeams.add(new SportsTeam("MCKEND", "McKendree", "", false));
        ncaafTeams.add(new SportsTeam("TRININ", "Trinity Intl", "", false));
        ncaafTeams.add(new SportsTeam("BACO", "Bacone College", "", false));
        ncaafTeams.add(new SportsTeam("BOW", "Bowie St.", "", false));
        ncaafTeams.add(new SportsTeam("JOSM", "Johnson Smith", "", false));
        ncaafTeams.add(new SportsTeam("LINCOL", "Lincoln", "", false));
        ncaafTeams.add(new SportsTeam("FAY", "Fayetteville St.", "", false));
        ncaafTeams.add(new SportsTeam("NMHI", "New Mexico Highlands", "", false));
        ncaafTeams.add(new SportsTeam("TUSK", "Tuskegee", "", false));
        ncaafTeams.add(new SportsTeam("LIV", "Livingstone", "", false));
        ncaafTeams.add(new SportsTeam("EWATER", "Edward Waters", "", false));
        ncaafTeams.add(new SportsTeam("TALR", "Taylor", "", false));
    }

    private static void InitializeNCAABList() {
        ncaabTeams.add(new SportsTeam("ABC", "Abilene", "Christian", false));
        ncaabTeams.add(new SportsTeam("AIR", "Air", "Force", false));
        ncaabTeams.add(new SportsTeam("AKR", "Akron", "", false));
        ncaabTeams.add(new SportsTeam("UAL", "Alabama", "", false));
        ncaabTeams.add(new SportsTeam("AAM", "Alabama", "A&M", false));
        ncaabTeams.add(new SportsTeam("ALS", "Alabama", "State", false));
        ncaabTeams.add(new SportsTeam("UAB", "Alabama", "Birmingham", false));
        ncaabTeams.add(new SportsTeam("ABN", "Albany", "", false));
        ncaabTeams.add(new SportsTeam("ALC", "Alcorn", "State", false));
        ncaabTeams.add(new SportsTeam("AME", "American", "", false));
        ncaabTeams.add(new SportsTeam("APP", "Appalachian", "State", false));
        ncaabTeams.add(new SportsTeam("UAZ", "Arizona", "", false));
        ncaabTeams.add(new SportsTeam("AZS", "Arizona", "State", false));
        ncaabTeams.add(new SportsTeam("UAR", "Arkansas", "", false));
        ncaabTeams.add(new SportsTeam("ARS", "Arkansas", "State", false));
        ncaabTeams.add(new SportsTeam("ALR", "Arkansas", "Little Rock", false));
        ncaabTeams.add(new SportsTeam("APB", "Arkansas", "Pine Bluff", false));
        ncaabTeams.add(new SportsTeam("ARM", "Army", "", false));
        ncaabTeams.add(new SportsTeam("AUB", "Auburn", "", false));
        ncaabTeams.add(new SportsTeam("APY", "Austin", "Peay", false));
        ncaabTeams.add(new SportsTeam("BAL", "Ball", "State", false));
        ncaabTeams.add(new SportsTeam("BAY", "Baylor", "", false));
        ncaabTeams.add(new SportsTeam("BEL", "Belmont", "", false));
        ncaabTeams.add(new SportsTeam("BCC", "Bethune", "Cookman", false));
        ncaabTeams.add(new SportsTeam("BIN", "Binghamton", "", false));
        ncaabTeams.add(new SportsTeam("BOI", "Boise", "State", false));
        ncaabTeams.add(new SportsTeam("BSC", "Boston", "College", false));
        ncaabTeams.add(new SportsTeam("BST", "Boston ", "University", false));
        ncaabTeams.add(new SportsTeam("BGS", "Bowling", "Green", false));
        ncaabTeams.add(new SportsTeam("BRA", "Bradley", "", false));
        ncaabTeams.add(new SportsTeam("BYU", "Brigham", "Young", false));
        ncaabTeams.add(new SportsTeam("BRO", "Brown", "", false));
        ncaabTeams.add(new SportsTeam("BRY", "Bryant", "", false));
        ncaabTeams.add(new SportsTeam("BUC", "Bucknell", "", false));
        ncaabTeams.add(new SportsTeam("BUF", "Buffalo", "", false));
        ncaabTeams.add(new SportsTeam("BUT", "Butler", "", false));
        ncaabTeams.add(new SportsTeam("CPS", "Cal Poly", "San Luis Obispo", false));
        ncaabTeams.add(new SportsTeam("CSB", "Cal State", "Bakersfield", false));
        ncaabTeams.add(new SportsTeam("CSF", "Cal State", "Fullerton", false));
        ncaabTeams.add(new SportsTeam("CSN", "Cal State", "Northridge", false));
        ncaabTeams.add(new SportsTeam("UCA", "California", "", false));
        ncaabTeams.add(new SportsTeam("CAD", "California", "Davis", false));
        ncaabTeams.add(new SportsTeam("CAI", "California", "Irvine", false));
        ncaabTeams.add(new SportsTeam("CLA", "California", "Los Angeles", false));
        ncaabTeams.add(new SportsTeam("CRI", "California", "Riverside", false));
        ncaabTeams.add(new SportsTeam("CAS", "California-Santa", "Barbara", false));
        ncaabTeams.add(new SportsTeam("CAM", "Campbell", "", false));
        ncaabTeams.add(new SportsTeam("CAN", "Canisius", "", false));
        ncaabTeams.add(new SportsTeam("CAR", "Central", "Arkansas", false));
        ncaabTeams.add(new SportsTeam("CCT", "Central", "Connecticut State", false));
        ncaabTeams.add(new SportsTeam("CFL", "Central", "Florida", false));
        ncaabTeams.add(new SportsTeam("CMI", "Central", "Michigan", false));
        ncaabTeams.add(new SportsTeam("CHS", "Charleson", "Southern", false));
        ncaabTeams.add(new SportsTeam("CHI", "Chicago", "State", false));
        ncaabTeams.add(new SportsTeam("CIN", "Cincinnati", "", false));
        ncaabTeams.add(new SportsTeam("CIT", "Citadel", "", false));
        ncaabTeams.add(new SportsTeam("CLE", "Clemson", "", false));
        ncaabTeams.add(new SportsTeam("CLV", "Cleveland", "State", false));
        ncaabTeams.add(new SportsTeam("COA", "Coastal", "Carolina", false));
        ncaabTeams.add(new SportsTeam("CLG", "Colgate", "", false));
        ncaabTeams.add(new SportsTeam("COC", "College of", "Charleson", false));
        ncaabTeams.add(new SportsTeam("UCO", "Colorado", "", false));
        ncaabTeams.add(new SportsTeam("COS", "Colorado State", "", false));
        ncaabTeams.add(new SportsTeam("COL", "Columbia", "", false));
        ncaabTeams.add(new SportsTeam("UCT", "Connecticut", "", false));
        ncaabTeams.add(new SportsTeam("COP", "Coppin", "State", false));
        ncaabTeams.add(new SportsTeam("COR", "Cornell", "", false));
        ncaabTeams.add(new SportsTeam("CRE", "Creighton", "", false));
        ncaabTeams.add(new SportsTeam("DAR", "Dartmouth", "", false));
        ncaabTeams.add(new SportsTeam("DAV", "Davidson", "", false));
        ncaabTeams.add(new SportsTeam("DAY", "Dayton", "", false));
        ncaabTeams.add(new SportsTeam("UDE", "Delaware", "", false));
        ncaabTeams.add(new SportsTeam("DES", "Delaware", "State", false));
        ncaabTeams.add(new SportsTeam("DEN", "Denver", "", false));
        ncaabTeams.add(new SportsTeam("DEP", "DePaul", "", false));
        ncaabTeams.add(new SportsTeam("DET", "Detroit", "", false));
        ncaabTeams.add(new SportsTeam("DRA", "Drake", "", false));
        ncaabTeams.add(new SportsTeam("DRE", "Drexel", "", false));
        ncaabTeams.add(new SportsTeam("DUK", "Duke", "", false));
        ncaabTeams.add(new SportsTeam("DUQ", "Duquesne", "", false));
        ncaabTeams.add(new SportsTeam("ECU", "East", "Carolina", false));
        ncaabTeams.add(new SportsTeam("ETN", "East Tennessee", "State", false));
        ncaabTeams.add(new SportsTeam("EIL", "Eastern", "Illinois", false));
        ncaabTeams.add(new SportsTeam("EKY", "Eastern", "Kentucky", false));
        ncaabTeams.add(new SportsTeam("EMI", "Eastern", "Michigan", false));
        ncaabTeams.add(new SportsTeam("EWA", "Eastern", "Washington", false));
        ncaabTeams.add(new SportsTeam("ELO", "Elon", "", false));
        ncaabTeams.add(new SportsTeam("EVA", "Evansville", "", false));
        ncaabTeams.add(new SportsTeam("FAI", "Fairfield", "", false));
        ncaabTeams.add(new SportsTeam("FLD", "Fairliegh", "Dickinson", false));
        ncaabTeams.add(new SportsTeam("UFL", "Florida", "", false));
        ncaabTeams.add(new SportsTeam("FAM", "Florida", "A&M", false));
        ncaabTeams.add(new SportsTeam("FAU", "Florida", "Atlantic", false));
        ncaabTeams.add(new SportsTeam("FGC", "Florida", "Gulf Coast", false));
        ncaabTeams.add(new SportsTeam("FIU", "Florida", "International", false));
        ncaabTeams.add(new SportsTeam("FLS", "Florida", "State", false));
        ncaabTeams.add(new SportsTeam("FOR", "Fordham", "", false));
        ncaabTeams.add(new SportsTeam("FRS", "Fresno", "State", false));
        ncaabTeams.add(new SportsTeam("FUR", "Furman", "", false));
        ncaabTeams.add(new SportsTeam("GWB", "Gardner", "Webb", false));
        ncaabTeams.add(new SportsTeam("GMU", "George", "Mason", false));
        ncaabTeams.add(new SportsTeam("GWU", "George", "Washington", false));
        ncaabTeams.add(new SportsTeam("GRG", "Georgetown", "", false));
        ncaabTeams.add(new SportsTeam("UGA", "Georgia", "", false));
        ncaabTeams.add(new SportsTeam("GSO", "Georgia", "Southern", false));
        ncaabTeams.add(new SportsTeam("GAS", "Georgia", "State", false));
        ncaabTeams.add(new SportsTeam("GAT", "Georgia", "Tech", false));
        ncaabTeams.add(new SportsTeam("GON", "Gonzaga", "", false));
        ncaabTeams.add(new SportsTeam("GRA", "Grambling", "State", false));
        ncaabTeams.add(new SportsTeam("GCN", "Grand", "Canyon", false));
        ncaabTeams.add(new SportsTeam("HAM", "Hampton", "", false));
        ncaabTeams.add(new SportsTeam("HRT", "Hartford", "", false));
        ncaabTeams.add(new SportsTeam("HAR", "Harvard", "", false));
        ncaabTeams.add(new SportsTeam("UHI", "Hawaii", "", false));
        ncaabTeams.add(new SportsTeam("HPU", "High", "Point", false));
        ncaabTeams.add(new SportsTeam("HOF", "Hofstra", "", false));
        ncaabTeams.add(new SportsTeam("HCR", "Holy Cross", "", false));
        ncaabTeams.add(new SportsTeam("HOU", "Houston", "", false));
        ncaabTeams.add(new SportsTeam("HBP", "Houston", "Baptist", false));
        ncaabTeams.add(new SportsTeam("HOW", "Howard", "", false));
        ncaabTeams.add(new SportsTeam("UID", "Idaho", "", false));
        ncaabTeams.add(new SportsTeam("IDS", "Idaho", "State", false));
        ncaabTeams.add(new SportsTeam("UIL", "Illinois", "", false));
        ncaabTeams.add(new SportsTeam("ILS", "Illinois", "State", false));
        ncaabTeams.add(new SportsTeam("ILC", "Illinois", "Chicago", false));
        ncaabTeams.add(new SportsTeam("ICW", "Incarnate", "Word", false));
        ncaabTeams.add(new SportsTeam("UIN", "Indiana", "", false));
        ncaabTeams.add(new SportsTeam("INS", "Indiana", "State", false));
        ncaabTeams.add(new SportsTeam("ION", "Iona", "", false));
        ncaabTeams.add(new SportsTeam("UIA", "Iowa", "", false));
        ncaabTeams.add(new SportsTeam("IAS", "Iowa", "State", false));
        ncaabTeams.add(new SportsTeam("IFW", "IPFW", "", false));
        ncaabTeams.add(new SportsTeam("IUP", "IUPUI", "", false));
        ncaabTeams.add(new SportsTeam("JCK", "Jackson", "State", false));
        ncaabTeams.add(new SportsTeam("JAX", "Jacksonville", "", false));
        ncaabTeams.add(new SportsTeam("JXS", "Jacksonville", "State", false));
        ncaabTeams.add(new SportsTeam("JMD", "James", "Madison", false));
        ncaabTeams.add(new SportsTeam("UKS", "Kansas", "", false));
        ncaabTeams.add(new SportsTeam("KSS", "Kansas", "State", false));
        ncaabTeams.add(new SportsTeam("KSW", "Kennesaw", "State", false));
        ncaabTeams.add(new SportsTeam("KNT", "Kent", "State", false));
        ncaabTeams.add(new SportsTeam("UKY", "Kentucky", "", false));
        ncaabTeams.add(new SportsTeam("LSL", "La Salle", "", false));
        ncaabTeams.add(new SportsTeam("LAF", "Lafayette", "", false));
        ncaabTeams.add(new SportsTeam("LMR", "Lamar", "", false));
        ncaabTeams.add(new SportsTeam("LEH", "Lehigh", "", false));
        ncaabTeams.add(new SportsTeam("LIB", "Liberty", "", false));
        ncaabTeams.add(new SportsTeam("LIP", "Lipscomb", "", false));
        ncaabTeams.add(new SportsTeam("LBS", "Long Beach", "State", false));
        ncaabTeams.add(new SportsTeam("LIU", "Long", "Island", false));
        ncaabTeams.add(new SportsTeam("LNG", "Longwood", "", false));
        ncaabTeams.add(new SportsTeam("LAL", "Louisiana", "Lafayette", false));
        ncaabTeams.add(new SportsTeam("LAM", "Louisiana", "Monroe", false));
        ncaabTeams.add(new SportsTeam("LAS", "Louisiana", "State", false));
        ncaabTeams.add(new SportsTeam("LAT", "Louisiana", "Tech", false));
        ncaabTeams.add(new SportsTeam("LOU", "Louisville", "", false));
        ncaabTeams.add(new SportsTeam("LLC", "Loyola", "Chicago", false));
        ncaabTeams.add(new SportsTeam("LLM", "Loyola", "Maryland", false));
        ncaabTeams.add(new SportsTeam("LMU", "Loyola", "Marymount", false));
        ncaabTeams.add(new SportsTeam("UME", "Maine", "", false));
        ncaabTeams.add(new SportsTeam("MAN", "Manhattan", "", false));
        ncaabTeams.add(new SportsTeam("MRS", "Marist", "", false));
        ncaabTeams.add(new SportsTeam("MRQ", "Marquette", "", false));
        ncaabTeams.add(new SportsTeam("MAR", "Marshall", "", false));
        ncaabTeams.add(new SportsTeam("UMD", "Maryland", "", false));
        ncaabTeams.add(new SportsTeam("MBC", "Maryland", "Baltimore County", false));
        ncaabTeams.add(new SportsTeam("MES", "Maryland", "Eastern Shore", false));
        ncaabTeams.add(new SportsTeam("UMA", "Massachusetts", "", false));
        ncaabTeams.add(new SportsTeam("MAL", "Massachusetts", "Lowell", false));
        ncaabTeams.add(new SportsTeam("MCN", "McNeese", "State", false));
        ncaabTeams.add(new SportsTeam("MEM", "Memphis", "", false));
        ncaabTeams.add(new SportsTeam("MER", "Mercer", "", false));
        ncaabTeams.add(new SportsTeam("MFL", "Miami", "Florida", false));
        ncaabTeams.add(new SportsTeam("MOH", "Miami", "OH", false));
        ncaabTeams.add(new SportsTeam("UMI", "Michigan", "", false));
        ncaabTeams.add(new SportsTeam("MIS", "Michigan", "State", false));
        ncaabTeams.add(new SportsTeam("MTN", "Middle", "Tennessee State", false));
        ncaabTeams.add(new SportsTeam("UMN", "Minnesota", "", false));
        ncaabTeams.add(new SportsTeam("UMS", "Mississippi", "", false));
        ncaabTeams.add(new SportsTeam("MSS", "Mississippi", "State", false));
        ncaabTeams.add(new SportsTeam("MSV", "Mississippi", "Valley State", false));
        ncaabTeams.add(new SportsTeam("UMO", "Missouri", "", false));
        ncaabTeams.add(new SportsTeam("MOS", "Missouri", "State", false));
        ncaabTeams.add(new SportsTeam("MKC", "Missouri", "Kansas City", false));
        ncaabTeams.add(new SportsTeam("MON", "Monmouth", "", false));
        ncaabTeams.add(new SportsTeam("UMT", "Montana", "", false));
        ncaabTeams.add(new SportsTeam("MTS", "Montana", "State", false));
        ncaabTeams.add(new SportsTeam("MHD", "Morehead", "State", false));
        ncaabTeams.add(new SportsTeam("MRG", "Morgan", "State", false));
        ncaabTeams.add(new SportsTeam("MSM", "Mount", "St. Mary's", false));
        ncaabTeams.add(new SportsTeam("MUR", "Murray", "State", false));
        ncaabTeams.add(new SportsTeam("NAV", "Navy", "", false));
        ncaabTeams.add(new SportsTeam("UNE", "Nebraska", "", false));
        ncaabTeams.add(new SportsTeam("NEO", "Nebraska", "Omaha", false));
        ncaabTeams.add(new SportsTeam("NLV", "Nevada", "Las Vegas", false));
        ncaabTeams.add(new SportsTeam("NVR", "Nevada", "Reno", false));
        ncaabTeams.add(new SportsTeam("UNH", "New", "Hampshire", false));
        ncaabTeams.add(new SportsTeam("UNM", "New", "Mexico", false));
        ncaabTeams.add(new SportsTeam("NMS", "New Mexico", "State", false));
        ncaabTeams.add(new SportsTeam("NOR", "New", "Orleans", false));
        ncaabTeams.add(new SportsTeam("NGR", "Niagara", "", false));
        ncaabTeams.add(new SportsTeam("NCH", "Nicholls", "State", false));
        ncaabTeams.add(new SportsTeam("NJT", "NJIT", "", false));
        ncaabTeams.add(new SportsTeam("NFK", "Norfolk", "State", false));
        ncaabTeams.add(new SportsTeam("UNC", "North", "Carolina", false));
        ncaabTeams.add(new SportsTeam("NAT", "North Carolina", "A&T", false));
        ncaabTeams.add(new SportsTeam("NCC", "North Carolina", "Central", false));
        ncaabTeams.add(new SportsTeam("NCS", "North Carolina", "State", false));
        ncaabTeams.add(new SportsTeam("NCA", "North Carolina", "Asheville", false));
        ncaabTeams.add(new SportsTeam("CHA", "North Carolina", "Charlotte", false));
        ncaabTeams.add(new SportsTeam("NCG", "North Carolina", "Greensboro", false));
        ncaabTeams.add(new SportsTeam("NCW", "North Carolina", "Wilmington", false));
        ncaabTeams.add(new SportsTeam("UND", "North Dakota", "", false));
        ncaabTeams.add(new SportsTeam("NDS", "North Dakota", "State", false));
        ncaabTeams.add(new SportsTeam("NFL", "North", "Florida", false));
        ncaabTeams.add(new SportsTeam("NTX", "North", "Texas", false));
        ncaabTeams.add(new SportsTeam("NEA", "Northeastern", "", false));
        ncaabTeams.add(new SportsTeam("NAZ", "Northern", "Arizona", false));
        ncaabTeams.add(new SportsTeam("NCO", "Northern", "Colorado", false));
        ncaabTeams.add(new SportsTeam("NIL", "Northern", "Illinois", false));
        ncaabTeams.add(new SportsTeam("NIA", "Northern", "Iowa", false));
        ncaabTeams.add(new SportsTeam("NKY", "Northern", "Kentucky", false));
        ncaabTeams.add(new SportsTeam("NWU", "Northwestern", "", false));
        ncaabTeams.add(new SportsTeam("NWS", "Northwestern", "State", false));
        ncaabTeams.add(new SportsTeam("NDM", "Notre", "Dame", false));
        ncaabTeams.add(new SportsTeam("OAK", "Oakland", "", false));
        ncaabTeams.add(new SportsTeam("UOH", "Ohio", "", false));
        ncaabTeams.add(new SportsTeam("OHS", "Ohio", "State", false));
        ncaabTeams.add(new SportsTeam("UOK", "Oklahoma", "", false));
        ncaabTeams.add(new SportsTeam("OKS", "Oklahoma", "State", false));
        ncaabTeams.add(new SportsTeam("OLD", "Old", "Dominion", false));
        ncaabTeams.add(new SportsTeam("ORB", "Oral Roberts", "", false));
        ncaabTeams.add(new SportsTeam("UOR", "Oregon", "", false));
        ncaabTeams.add(new SportsTeam("ORS", "Oregon", "State", false));
        ncaabTeams.add(new SportsTeam("PAC", "Pacific", "", false));
        ncaabTeams.add(new SportsTeam("PAS", "Penn State", "", false));
        ncaabTeams.add(new SportsTeam("UPA", "Pennsylvania", "", false));
        ncaabTeams.add(new SportsTeam("PEP", "Pepperdine", "", false));
        ncaabTeams.add(new SportsTeam("PIT", "Pittsburgh", "", false));
        ncaabTeams.add(new SportsTeam("POR", "Portland", "", false));
        ncaabTeams.add(new SportsTeam("PST", "Portland", "State", false));
        ncaabTeams.add(new SportsTeam("PVA", "Prairie", "View A&M", false));
        ncaabTeams.add(new SportsTeam("PRE", "Presbyterian", "", false));
        ncaabTeams.add(new SportsTeam("PRI", "Princeton", "", false));
        ncaabTeams.add(new SportsTeam("PRO", "Providence", "", false));
        ncaabTeams.add(new SportsTeam("PUR", "Purdue", "", false));
        ncaabTeams.add(new SportsTeam("QUI", "Quinnipiac", "", false));
        ncaabTeams.add(new SportsTeam("RAD", "Radford", "", false));
        ncaabTeams.add(new SportsTeam("URI", "Rhode", "Island", false));
        ncaabTeams.add(new SportsTeam("RIC", "Rice", "", false));
        ncaabTeams.add(new SportsTeam("RCH", "Richmond", "", false));
        ncaabTeams.add(new SportsTeam("RID", "Rider", "", false));
        ncaabTeams.add(new SportsTeam("RMR", "Robert", "Morris", false));
        ncaabTeams.add(new SportsTeam("RUT", "Rutgers", "", false));
        ncaabTeams.add(new SportsTeam("SAC", "Sacramento", "State", false));
        ncaabTeams.add(new SportsTeam("SHS", "Sam Houston", "State", false));
        ncaabTeams.add(new SportsTeam("SAM", "Samford", "", false));
        ncaabTeams.add(new SportsTeam("SDG", "San Diego", "", false));
        ncaabTeams.add(new SportsTeam("SND", "San Diego", "State", false));
        ncaabTeams.add(new SportsTeam("SFO", "San", "Francisco", false));
        ncaabTeams.add(new SportsTeam("SJS", "San Jose", "State", false));
        ncaabTeams.add(new SportsTeam("SCL", "Santa", "Clara", false));
        ncaabTeams.add(new SportsTeam("SVS", "Savannah", "State", false));
        ncaabTeams.add(new SportsTeam("SEA", "Seattle", "", false));
        ncaabTeams.add(new SportsTeam("SET", "Seton", "Hall", false));
        ncaabTeams.add(new SportsTeam("SNA", "Siena", "", false));
        ncaabTeams.add(new SportsTeam("SIE", "SIU", "Edwardsville", false));
        ncaabTeams.add(new SportsTeam("SAL", "South", "Alabama", false));
        ncaabTeams.add(new SportsTeam("USC", "South", "Carolina", false));
        ncaabTeams.add(new SportsTeam("SCS", "South Carolina", "State", false));
        ncaabTeams.add(new SportsTeam("SCU", "South Carolina", "Upstate", false));
        ncaabTeams.add(new SportsTeam("USD", "South Dakota", "", false));
        ncaabTeams.add(new SportsTeam("SDS", "South Dakota", "State", false));
        ncaabTeams.add(new SportsTeam("SFL", "South", "Florida", false));
        ncaabTeams.add(new SportsTeam("SMO", "Southeast", "Missouri State", false));
        ncaabTeams.add(new SportsTeam("SLA", "Southeastern", "Louisiana", false));
        ncaabTeams.add(new SportsTeam("SOU", "Southern", "", false));
        ncaabTeams.add(new SportsTeam("SCA", "Southern", "California", false));
        ncaabTeams.add(new SportsTeam("SIL", "Southern", "Illinois", false));
        ncaabTeams.add(new SportsTeam("SMU", "Southern", "Methodist", false));
        ncaabTeams.add(new SportsTeam("SMS", "Southern", "Mississippi", false));
        ncaabTeams.add(new SportsTeam("SUT", "Southern", "Utah", false));
        ncaabTeams.add(new SportsTeam("STB", "St. Bonaventure", "", false));
        ncaabTeams.add(new SportsTeam("SFN", "St. Francis (NY)", "", false));
        ncaabTeams.add(new SportsTeam("SFP", "St. Francis (PA)", "", false));
        ncaabTeams.add(new SportsTeam("SJN", "St. John's", "", false));
        ncaabTeams.add(new SportsTeam("SJO", "St. Joseph's", "", false));
        ncaabTeams.add(new SportsTeam("STL", "St. Louis", "", false));
        ncaabTeams.add(new SportsTeam("STM", "St. Mary's", "", false));
        ncaabTeams.add(new SportsTeam("STP", "St. Peter's", "", false));
        ncaabTeams.add(new SportsTeam("STA", "Stanford", "", false));
        ncaabTeams.add(new SportsTeam("SFA", "Stephen F.", "Austin", false));
        ncaabTeams.add(new SportsTeam("STE", "Stetson", "", false));
        ncaabTeams.add(new SportsTeam("STO", "Stony", "Brook", false));
        ncaabTeams.add(new SportsTeam("SYR", "Syracuse", "", false));
        ncaabTeams.add(new SportsTeam("TEM", "Temple", "", false));
        ncaabTeams.add(new SportsTeam("UTN", "Tennessee", "", false));
        ncaabTeams.add(new SportsTeam("TNS", "Tennessee", "State", false));
        ncaabTeams.add(new SportsTeam("TNT", "Tennessee", "Tech", false));
        ncaabTeams.add(new SportsTeam("TNC", "Tennessee-Chattanooga", "", false));
        ncaabTeams.add(new SportsTeam("TNM", "Tennessee-Martin", "", false));
        ncaabTeams.add(new SportsTeam("UTX", "Texas", "", false));
        ncaabTeams.add(new SportsTeam("TAM", "Texas", "A&M", false));
        ncaabTeams.add(new SportsTeam("TCC", "Texas A&M-Corpus", "Christi", false));
        ncaabTeams.add(new SportsTeam("TCU", "Texas", "Christian", false));
        ncaabTeams.add(new SportsTeam("STX", "Texas", "Southern", false));
        ncaabTeams.add(new SportsTeam("TXS", "Texas", "State", false));
        ncaabTeams.add(new SportsTeam("TXT", "Texas", "Tech", false));
        ncaabTeams.add(new SportsTeam("TXA", "Texas-Arlington", "", false));
        ncaabTeams.add(new SportsTeam("TEP", "Texas-El", "Paso", false));
        ncaabTeams.add(new SportsTeam("TPA", "Texas-Pan", "American", false));
        ncaabTeams.add(new SportsTeam("TSA", "Texas-San", "Antonio", false));
        ncaabTeams.add(new SportsTeam("TOL", "Toledo", "", false));
        ncaabTeams.add(new SportsTeam("TOW", "Towson", "", false));
        ncaabTeams.add(new SportsTeam("TRO", "Troy", "", false));
        ncaabTeams.add(new SportsTeam("TLN", "Tulane", "", false));
        ncaabTeams.add(new SportsTeam("TLS", "Tulsa", "", false));
        ncaabTeams.add(new SportsTeam("UUT", "Utah", "", false));
        ncaabTeams.add(new SportsTeam("UTS", "Utah", "State", false));
        ncaabTeams.add(new SportsTeam("UTV", "Utah ", "Valley", false));
        ncaabTeams.add(new SportsTeam("VAL", "Valparaiso", "", false));
        ncaabTeams.add(new SportsTeam("VAN", "Vanderbilt", "", false));
        ncaabTeams.add(new SportsTeam("UVT", "Vermont", "", false));
        ncaabTeams.add(new SportsTeam("VIL", "Villanova", "", false));
        ncaabTeams.add(new SportsTeam("UVA", "Virginia", "", false));
        ncaabTeams.add(new SportsTeam("VCU", "Virginia", "Commonwealth", false));
        ncaabTeams.add(new SportsTeam("VMI", "Virginia Military", "Institute", false));
        ncaabTeams.add(new SportsTeam("VAT", "Virginia", "Tech", false));
        ncaabTeams.add(new SportsTeam("WAG", "Wagner", "", false));
        ncaabTeams.add(new SportsTeam("WFU", "Wake", "Forest", false));
        ncaabTeams.add(new SportsTeam("UWA", "Washington", "", false));
        ncaabTeams.add(new SportsTeam("WAS", "Washington", "State", false));
        ncaabTeams.add(new SportsTeam("WEB", "Weber", "State", false));
        ncaabTeams.add(new SportsTeam("UWV", "West", "Virginia", false));
        ncaabTeams.add(new SportsTeam("WCA", "Western", "Carolina", false));
        ncaabTeams.add(new SportsTeam("WIL", "Western", "Illinois", false));
        ncaabTeams.add(new SportsTeam("WKY", "Western", "Kentucky", false));
        ncaabTeams.add(new SportsTeam("WMI", "Western", "Michigan", false));
        ncaabTeams.add(new SportsTeam("WIC", "Wichita", "State", false));
        ncaabTeams.add(new SportsTeam("WMR", "William & Mary", "", false));
        ncaabTeams.add(new SportsTeam("WIN", "Winthrop", "", false));
        ncaabTeams.add(new SportsTeam("UWI", "Wisconsin", "", false));
        ncaabTeams.add(new SportsTeam("WGB", "Wisconsin-Green", "Bay", false));
        ncaabTeams.add(new SportsTeam("WIM", "Wisconsin-Milwaukee", "", false));
        ncaabTeams.add(new SportsTeam("WOF", "Wofford", "", false));
        ncaabTeams.add(new SportsTeam("WRI", "Wright", "State", false));
        ncaabTeams.add(new SportsTeam("UWY", "Wyoming", "", false));
        ncaabTeams.add(new SportsTeam("XAV", "Xavier", "", false));
        ncaabTeams.add(new SportsTeam("YAL", "Yale", "", false));
        ncaabTeams.add(new SportsTeam("YNG", "Youngstown", "State", false));

    }

    private static void InitializePremierLeagueList() {
        PremierLeagueTeams.add(new SportsTeam("BOU", "Bournemouth", "", false));
        PremierLeagueTeams.add(new SportsTeam("ARS", "Arsenal", "", false));
        PremierLeagueTeams.add(new SportsTeam("BUR", "Burnley", "", false));
        PremierLeagueTeams.add(new SportsTeam("CHE", "Chelsea", "", false));
        PremierLeagueTeams.add(new SportsTeam("CRY", "Crystal", "Palace", false));
        PremierLeagueTeams.add(new SportsTeam("EVE", "Everton", "", false));
        PremierLeagueTeams.add(new SportsTeam("HUL", "Hull", "City", false));
        PremierLeagueTeams.add(new SportsTeam("LEI", "Leicester", "City", false));
        PremierLeagueTeams.add(new SportsTeam("LIV", "Liverpool", "", false));
        PremierLeagueTeams.add(new SportsTeam("MCI", "Manchester", "City", false));
        PremierLeagueTeams.add(new SportsTeam("MUN", "Manchester", "United", false));
        PremierLeagueTeams.add(new SportsTeam("MID", "Middlesbrough", "", false));
        PremierLeagueTeams.add(new SportsTeam("SOU", "Southampton", "", false));
        PremierLeagueTeams.add(new SportsTeam("STK", "Stoke", "City", false));
        PremierLeagueTeams.add(new SportsTeam("SUN", "Sunderland", "", false));
        PremierLeagueTeams.add(new SportsTeam("SWA", "Swansea", "City", false));
        PremierLeagueTeams.add(new SportsTeam("TOT", "Tottenham", "Hotspur", false));
        PremierLeagueTeams.add(new SportsTeam("WAT", "Watford", "", false));
        PremierLeagueTeams.add(new SportsTeam("WBA", "West Bromwich", "Albion", false));
        PremierLeagueTeams.add(new SportsTeam("WHU", "West Ham", "United", false));
    }

    private static void InitializeLigue1Teams() {
        Ligue1Teams.add(new SportsTeam("ANG", "Angers", "", false));
        Ligue1Teams.add(new SportsTeam("BAS", "Bastia", "", false));
        Ligue1Teams.add(new SportsTeam("BOR", "Bordeaux", "", false));
        Ligue1Teams.add(new SportsTeam("CAE", "Caen", "", false));
        Ligue1Teams.add(new SportsTeam("DIJ", "Dijon", "", false));
        Ligue1Teams.add(new SportsTeam("GUI", "Guingamp", "", false));
        Ligue1Teams.add(new SportsTeam("LIL", "Lille", "", false));
        Ligue1Teams.add(new SportsTeam("LOR", "Lorient", "", false));
        Ligue1Teams.add(new SportsTeam("OL", "Lyon", "", false));
        Ligue1Teams.add(new SportsTeam("OM", "Marseille", "", false));
        Ligue1Teams.add(new SportsTeam("MET", "Metz", "", false));
        Ligue1Teams.add(new SportsTeam("ASM", "Monaco", "", false));
        Ligue1Teams.add(new SportsTeam("MHS", "Montpellier", "", false));
        Ligue1Teams.add(new SportsTeam("NAL", "Nancy", "Tomblaine", false));
        Ligue1Teams.add(new SportsTeam("NAN", "Nantes", "", false));
        Ligue1Teams.add(new SportsTeam("NIC", "Nice", "", false));
        Ligue1Teams.add(new SportsTeam("PSG", "Paris", "Saint-Germain", false));
        Ligue1Teams.add(new SportsTeam("REN", "Rennes", "", false));
        Ligue1Teams.add(new SportsTeam("STE", "Saint-Étienne", "", false));
        Ligue1Teams.add(new SportsTeam("TFC", "Toulouse", "", false));
    }

    private static void InitializeSerieATeams() {
        SerieATeams.add(new SportsTeam("ATA", "Atalanta", "", false));
        SerieATeams.add(new SportsTeam("BOL", "Bologna", "", false));
        SerieATeams.add(new SportsTeam("CAG", "Cagliari", "", false));
        SerieATeams.add(new SportsTeam("CHV", "Chievo", "", false));
        SerieATeams.add(new SportsTeam("CRO", "Crotone", "", false));
        SerieATeams.add(new SportsTeam("EMP", "Empoli", "", false));
        SerieATeams.add(new SportsTeam("FIO", "Fiorentina", "", false));
        SerieATeams.add(new SportsTeam("GEN", "Genoa", "", false));
        SerieATeams.add(new SportsTeam("INT", "Internazionale", "", false));
        SerieATeams.add(new SportsTeam("JUV", "Juventus", "", false));
        SerieATeams.add(new SportsTeam("LAZ", "Lazio", "", false));
        SerieATeams.add(new SportsTeam("MIL", "Milan", "", false));
        SerieATeams.add(new SportsTeam("NAP", "Napoli", "", false));
        SerieATeams.add(new SportsTeam("PAL", "Palermo", "", false));
        SerieATeams.add(new SportsTeam("PES", "Pescara", "", false));
        SerieATeams.add(new SportsTeam("ROM", "Roma", "", false));
        SerieATeams.add(new SportsTeam("SAM", "Sampdoria", "", false));
        SerieATeams.add(new SportsTeam("SAS", "Sassuolo", "", false));
        SerieATeams.add(new SportsTeam("TOR", "Torino", "", false));
        SerieATeams.add(new SportsTeam("UDI", "Udinese", "", false));

    }

    private static void InitializeFACupTeams() {
        FACupTeams.add(new SportsTeam("BOU", "", "Bournemouth", false));
    }

    private static void InitializeChampionshipTeams() {
        ChampionshipTeams.add(new SportsTeam("AVFC", "Aston Villa", "", false));
        ChampionshipTeams.add(new SportsTeam("BAR", "Barnsley", "", false));
        ChampionshipTeams.add(new SportsTeam("BIR", "Birmingham", "City", false));
        ChampionshipTeams.add(new SportsTeam("BLA", "Blackburn", "Rovers", false));
        ChampionshipTeams.add(new SportsTeam("BRE", "Brentford", "", false));
        ChampionshipTeams.add(new SportsTeam("AVFC", "Brighton & Hove", "Albion", false));
        ChampionshipTeams.add(new SportsTeam("BRI", "Bristol", "City", false));
        ChampionshipTeams.add(new SportsTeam("BUR", "Burton", "Albion", false));
        ChampionshipTeams.add(new SportsTeam("CAR", "Cardiff", "City", false));
        ChampionshipTeams.add(new SportsTeam("DER", "Derby", "County", false));
        ChampionshipTeams.add(new SportsTeam("FUL", "Fulham", "", false));
        ChampionshipTeams.add(new SportsTeam("HUD", "Huddersfield", "Town", false));
        ChampionshipTeams.add(new SportsTeam("IPS", "Ipswich", "Town", false));
        ChampionshipTeams.add(new SportsTeam("LEE", "Leeds", "United", false));
        ChampionshipTeams.add(new SportsTeam("NEW", "Newcastle", "United", false));
        ChampionshipTeams.add(new SportsTeam("NOR", "Norwich", "City", false));
        ChampionshipTeams.add(new SportsTeam("NOT", "Nottingham", "Forest", false));
        ChampionshipTeams.add(new SportsTeam("PNE", "Preston", "North End", false));
        ChampionshipTeams.add(new SportsTeam("QPR", "Queens Park", "Rangers", false));
        ChampionshipTeams.add(new SportsTeam("REA", "Reading", "", false));
        ChampionshipTeams.add(new SportsTeam("ROT", "Rotherham", "United", false));
        ChampionshipTeams.add(new SportsTeam("SHE", "Sheffield", "Wednesday", false));
        ChampionshipTeams.add(new SportsTeam("WIG", "Wigan", "Athletic", false));
        ChampionshipTeams.add(new SportsTeam("WOL", "Wolverhampton", "Wanderers", false));

    }

    private static void InitializeChampionLeagueTeams() {
        ChampionLeagueTeams.add(new SportsTeam("ARS", "Arsenal", "", false));
        ChampionLeagueTeams.add(new SportsTeam("ATL", "Atlético", "Madrid", false));
        ChampionLeagueTeams.add(new SportsTeam("BAR", "Barcelona", "", false));
        ChampionLeagueTeams.add(new SportsTeam("BAS", "Basel", "", false));
        ChampionLeagueTeams.add(new SportsTeam("BL", "Bayer 04", "Leverkusen", false));
        ChampionLeagueTeams.add(new SportsTeam("BAY", "FC Bayern", "Munich", false));
        ChampionLeagueTeams.add(new SportsTeam("SLB", "Benfica", "", false));
        ChampionLeagueTeams.add(new SportsTeam("BES", "Besiktas", "", false));
        ChampionLeagueTeams.add(new SportsTeam("DOR", "Borussia", "Dortmund", false));
        ChampionLeagueTeams.add(new SportsTeam("MGL", "Borussia", "Mönchengladbach", false));
        ChampionLeagueTeams.add(new SportsTeam("CEL", "Celtic", "", false));
        ChampionLeagueTeams.add(new SportsTeam("CLU", "Club Brugge", "", false));
        ChampionLeagueTeams.add(new SportsTeam("COP", "FC København", "", false));
        ChampionLeagueTeams.add(new SportsTeam("CSM", "CSKA Moscow", "", false));
        ChampionLeagueTeams.add(new SportsTeam("DZ", "Dinamo", "Zagreb", false));
        ChampionLeagueTeams.add(new SportsTeam("DK", "Dynamo", "Kyiv", false));
        ChampionLeagueTeams.add(new SportsTeam("JUV", "Juventus", "", false));
        ChampionLeagueTeams.add(new SportsTeam("LEG", "Legia", "Warsaw", false));
        ChampionLeagueTeams.add(new SportsTeam("LEI", "Leicester", "City", false));
        ChampionLeagueTeams.add(new SportsTeam("LUD", "Ludogorets", "Razgrad", false));
        ChampionLeagueTeams.add(new SportsTeam("OL", "Lyon", "", false));
        ChampionLeagueTeams.add(new SportsTeam("MC", "Manchester", "City", false));
        ChampionLeagueTeams.add(new SportsTeam("MON", "Monaco", "", false));
        ChampionLeagueTeams.add(new SportsTeam("NAP", "Napoli", "", false));
        ChampionLeagueTeams.add(new SportsTeam("PSG", "Paris", "Saint-Germain", false));
        ChampionLeagueTeams.add(new SportsTeam("POR", "Porto", "", false));
        ChampionLeagueTeams.add(new SportsTeam("PSV", "PSV Eindhoven", "", false));
        ChampionLeagueTeams.add(new SportsTeam("RM", "Real Madrid", "", false));
        ChampionLeagueTeams.add(new SportsTeam("ROS", "Rostov", "", false));
        ChampionLeagueTeams.add(new SportsTeam("SEV", "Sevilla", "", false));
        ChampionLeagueTeams.add(new SportsTeam("SCP", "Sporting CP", "", false));
        ChampionLeagueTeams.add(new SportsTeam("TOT", "Tottenham", "Hotspur", false));


    }

    private static void InitializeEuropaLeagueTeams() {
        EuropaLeagueTeams.add(new SportsTeam("BOU", "", "Bournemouth", false));
    }

    private static void InitializeLALigaTeams() {
        LALigaTeams.add(new SportsTeam("ALV", "Alavés", "", false));
        LALigaTeams.add(new SportsTeam("ATH", "Athletic Bilbao", "", false));
        LALigaTeams.add(new SportsTeam("ATM", "Atlético Madrid", "", false));
        LALigaTeams.add(new SportsTeam("FCB", "Barcelona", "", false));
        LALigaTeams.add(new SportsTeam("CEL", "Celta Vigo", "", false));
        LALigaTeams.add(new SportsTeam("RCD", "Deportivo La Coruña", "", false));
        LALigaTeams.add(new SportsTeam("EIB", "Eibar", "", false));
        LALigaTeams.add(new SportsTeam("ESP", "Espanyol", "", false));
        LALigaTeams.add(new SportsTeam("GCF", "Granada", "", false));
        LALigaTeams.add(new SportsTeam("LPA", "Las Palmas", "", false));
        LALigaTeams.add(new SportsTeam("LEG", "Leganés", "", false));
        LALigaTeams.add(new SportsTeam("MCF", "Málaga", "", false));
        LALigaTeams.add(new SportsTeam("OSA", "Osasuna", "", false));
        LALigaTeams.add(new SportsTeam("RBB", "Real Betis", "", false));
        LALigaTeams.add(new SportsTeam("RMA", "Real Madrid", "", false));
        LALigaTeams.add(new SportsTeam("RSO", "Real Sociedad", "", false));
        LALigaTeams.add(new SportsTeam("SFC", "Sevilla", "", false));
        LALigaTeams.add(new SportsTeam("RSG", "Sporting Gijón", "", false));
        LALigaTeams.add(new SportsTeam("VCF", "Valencia", "", false));
        LALigaTeams.add(new SportsTeam("VIL", "Villarreal", "", false));
    }

    private static void InitializeBundesligaTeams() {
        BundesligaTeams.add(new SportsTeam("KOL", "1. FC Köln", "", false));
        BundesligaTeams.add(new SportsTeam("HOF", "1899 Hoffenheim", "", false));
        BundesligaTeams.add(new SportsTeam("LEV", "Bayer 04", "Leverkusen", false));
        BundesligaTeams.add(new SportsTeam("MUN", "FC Bayern", "Munich", false));
        BundesligaTeams.add(new SportsTeam("DOR", "Borussia", "Dortmund", false));
        BundesligaTeams.add(new SportsTeam("MGL", "Borussia", "Mönchengladbach", false));
        BundesligaTeams.add(new SportsTeam("DAR", "SV Darmstadt 98", "", false));
        BundesligaTeams.add(new SportsTeam("FRA", "Eintracht Frankfurt", "", false));
        BundesligaTeams.add(new SportsTeam("AUG", "FC Augsburg", "", false));
        BundesligaTeams.add(new SportsTeam("ING", "FC Ingolstadt", "", false));
        BundesligaTeams.add(new SportsTeam("HAM", "Hamburger SV", "", false));
        BundesligaTeams.add(new SportsTeam("BSC", "Hertha BSC", "", false));
        BundesligaTeams.add(new SportsTeam("MAI", "Mainz 05", "", false));
        BundesligaTeams.add(new SportsTeam("RBL", "RB Leipzig", "", false));
        BundesligaTeams.add(new SportsTeam("FRE", "SC Freiburg", "", false));
        BundesligaTeams.add(new SportsTeam("S04", "Schalke 04", "", false));
        BundesligaTeams.add(new SportsTeam("WOL", "VfL Wolfsburg", "", false));
        BundesligaTeams.add(new SportsTeam("BRE", "SV Werder", "Bremen", false));
    }

    private static void InitializeScottishPremiershipTeams() {
        ScottishPremiershipTeams.add(new SportsTeam("ABE", "", "Aberdeen", false));
        ScottishPremiershipTeams.add(new SportsTeam("CEL", "", "Celtic", false));
        ScottishPremiershipTeams.add(new SportsTeam("DND", "", "Dundee", false));
        ScottishPremiershipTeams.add(new SportsTeam("HAM", "", "Hamilton Academical", false));
        ScottishPremiershipTeams.add(new SportsTeam("HOM", "", "Heart of Midlothian", false));
        ScottishPremiershipTeams.add(new SportsTeam("INV", "", "Inverness Caledonian Thistle", false));
        ScottishPremiershipTeams.add(new SportsTeam("KIL", "", "Kilmarnock", false));
        ScottishPremiershipTeams.add(new SportsTeam("MOT", "", "Motherwell", false));
        ScottishPremiershipTeams.add(new SportsTeam("PAR", "", "Partick Thistle", false));
        ScottishPremiershipTeams.add(new SportsTeam("RAN", "", "Rangers", false));
        ScottishPremiershipTeams.add(new SportsTeam("ROS", "", "Ross County", false));
        ScottishPremiershipTeams.add(new SportsTeam("STJ", "", "St. Johnstone", false));
    }

    private static void InitializeNFLTeams() {
        nflTeams.add(new SportsTeam("ARI", "Arizona", "Cardinals", false));
        nflTeams.add(new SportsTeam("ATL", "Atlanta", "Falcons", false));
        nflTeams.add(new SportsTeam("BAL", "Baltimore", "Ravens", false));
        nflTeams.add(new SportsTeam("BUF", "Buffalo", "Bills", false));
        nflTeams.add(new SportsTeam("CAR", "Carolina", "Panthers", false));
        nflTeams.add(new SportsTeam("CHI", "Chicago", "Bears", false));
        nflTeams.add(new SportsTeam("CIN", "Cincinnati", "Bengals", false));
        nflTeams.add(new SportsTeam("CLE", "Cleveland", "Browns", false));
        nflTeams.add(new SportsTeam("DAL", "Dallas", "Cowboys", false));
        nflTeams.add(new SportsTeam("DEN", "Denver", "Broncos", false));
        nflTeams.add(new SportsTeam("DET", "Detroit", "Lions", false));
        nflTeams.add(new SportsTeam("GB", "Green Bay", "Packers", false));
        nflTeams.add(new SportsTeam("HOU", "Houston", "Texas", false));
        nflTeams.add(new SportsTeam("IND", "Indianapolis", "Colts", false));
        nflTeams.add(new SportsTeam("JAX", "Jacksonville", "Jaguars", false));
        nflTeams.add(new SportsTeam("KC", "Kansas City", "Chiefs", false));
        nflTeams.add(new SportsTeam("LA", "Los Angeles", "Rams", false));
        nflTeams.add(new SportsTeam("MIA", "Miami", "Dolphins", false));
        nflTeams.add(new SportsTeam("MIN", "Minnesota", "Vikings", false));
        nflTeams.add(new SportsTeam("NE", "New England", "Patriots", false));
        nflTeams.add(new SportsTeam("NO", "New Orleans", "Saints", false));
        nflTeams.add(new SportsTeam("NYG", "New York", "Giants", false));
        nflTeams.add(new SportsTeam("NYJ", "New York", "Jets", false));
        nflTeams.add(new SportsTeam("OAK", "Oakland", "Raiders", false));
        nflTeams.add(new SportsTeam("PHI", "Philadelphia", "Eagles", false));
        nflTeams.add(new SportsTeam("PIT", "Pittsburgh", "Steelers", false));
        nflTeams.add(new SportsTeam("SD", "San Diego", "Chargers", false));
        nflTeams.add(new SportsTeam("SF", "San Francisco", "49ers", false));
        nflTeams.add(new SportsTeam("SEA", "Seattle", "Seahawks", false));
        nflTeams.add(new SportsTeam("TB", "Tampa Bay", "Buccaneers", false));
        nflTeams.add(new SportsTeam("TEN", "Tennessee", "Titans", false));
        nflTeams.add(new SportsTeam("WAS", "Washington", "Redskins", false));


    }

    private static void InitializeAllTeams() {
        if (!teamsInitialized) {
            InitializeNBAList();
            InitializeNHLList();
            InitializeMLSList();
            InitializeEuro2016List();
            InitializeNCAAFList();
            InitializeNCAABList();
            InitializePremierLeagueList();
            InitializeLigue1Teams();
            InitializeSerieATeams();
            InitializeFACupTeams();
            InitializeChampionshipTeams();
            InitializeChampionLeagueTeams();
            InitializeEuropaLeagueTeams();
            InitializeLALigaTeams();
            InitializeBundesligaTeams();
            InitializeScottishPremiershipTeams();
            InitializeNFLTeams();
            teamsInitialized = true;
        }
    }


    // Get Teams
    public static ArrayList<SportsTeam> getNHLTeams() {
        InitializeAllTeams();
        return hockeyTeams;
    }

    public static ArrayList<SportsTeam> getNBATeams() {
        InitializeAllTeams();
        return nbaTeams;
    }

    public static ArrayList<SportsTeam> getMLSTeams() {
        InitializeAllTeams();
        return mlsTeams;
    }

    public static ArrayList<SportsTeam> getEuro2016Teams() {
        InitializeAllTeams();
        return euro2016Teams;
    }

    public static ArrayList<SportsTeam> getNCAAFTeams() {
        InitializeAllTeams();
        return ncaafTeams;
    }

    public static ArrayList<SportsTeam> getNCAABTeams() {
        InitializeAllTeams();
        return ncaabTeams;
    }

    public static ArrayList<SportsTeam> getPremierLeagueTeams() {
        InitializeAllTeams();
        return PremierLeagueTeams;
    }

    public static ArrayList<SportsTeam> getLigue1Teams() {
        InitializeAllTeams();
        return Ligue1Teams;
    }

    public static ArrayList<SportsTeam> getSerieATeams() {
        InitializeAllTeams();
        return SerieATeams;
    }

    public static ArrayList<SportsTeam> getFACupTeams() {
        InitializeAllTeams();
        return FACupTeams;
    }

    public static ArrayList<SportsTeam> getChampionshipTeams() {
        InitializeAllTeams();
        return ChampionshipTeams;
    }

    public static ArrayList<SportsTeam> getChampionLeagueTeams() {
        InitializeAllTeams();
        return ChampionLeagueTeams;
    }

    public static ArrayList<SportsTeam> getEuropaLeagueTeams() {
        InitializeAllTeams();
        return EuropaLeagueTeams;
    }

    public static ArrayList<SportsTeam> getLALigaTeams() {
        InitializeAllTeams();
        return LALigaTeams;
    }

    public static ArrayList<SportsTeam> getBundesligaTeams() {
        InitializeAllTeams();
        return BundesligaTeams;
    }

    public static ArrayList<SportsTeam> getScottishPremiershipTeams() {
        InitializeAllTeams();
        return ScottishPremiershipTeams;
    }

    public static ArrayList<SportsTeam> getNFLTeams() {
        InitializeAllTeams();
        return nflTeams;
    }


    // Get Games
    public static ArrayList<SportsGame> getNBAGames(String result) {
        getNBATeams(); //initialize the list

        ArrayList<SportsGame> games = new ArrayList<>();

        int count = 1;
        result = result.replace("%20", " ");
        String myData = "";
        String nba = "&nba";

        String left = "left" + count + "=";
        int leftIndex = result.indexOf(left);
        while (leftIndex != -1) {
            SportsGame.TimeStatus timeStatus;
            result = result.substring(leftIndex + left.length());
            String gameData = result.substring(0, result.indexOf(nba));
            String teamsAndScore = gameData.substring(0, result.indexOf("(") - 1);
            teamsAndScore = teamsAndScore.replace("^", "");
            String gameTime = gameData.substring(result.indexOf("(") + 1, result.indexOf(")"));

            String homeName = "";
            String awayName = "";
            String homeScore = "";
            String awayScore = "";
            String gameTimeOrStartDate = "";
            String gameStatusOrStartTime = "";

            if (teamsAndScore.toLowerCase().contains(" at ")) { //game hasnt started
                String[] split = teamsAndScore.split(" at ");
                awayName = split[0].trim(); //just contains the city
                homeName = split[1].trim();
                gameTimeOrStartDate = "TODAY";
                int lastSpace = gameTime.lastIndexOf(" ");
                gameStatusOrStartTime = gameTime.substring(0, lastSpace).trim();
                timeStatus = SportsGame.TimeStatus.FUTURE;
            } else {
                String[] split = teamsAndScore.split("  "); //two spaces

                String awayString = split[0].trim(); //[city] [score]
                int awayLastSpace = awayString.lastIndexOf(" ");
                awayName = awayString.substring(0, awayLastSpace).trim();
                awayScore = awayString.substring(awayLastSpace + 1, awayString.length()).trim();

                String homeString = split[1].trim();
                int homeLastSpace = homeString.lastIndexOf(" ");
                homeName = homeString.substring(0, homeLastSpace).trim();
                homeScore = homeString.substring(homeLastSpace + 1, homeString.length()).trim();

                gameTime = gameTime.replace("IN ", "");
                gameTimeOrStartDate = gameTime;
                if (gameTime.toLowerCase().contains("final")) {
                    gameStatusOrStartTime = "FINAL";
                    timeStatus = SportsGame.TimeStatus.PAST;
                } else {
                    gameStatusOrStartTime = "LIVE";
                    timeStatus = SportsGame.TimeStatus.LIVE;
                }
            }
            myData += gameTimeOrStartDate + " " + gameStatusOrStartTime + "\n";

            SportsTeam home = new SportsTeam("initial", "city", "name", "score");
            SportsTeam away = new SportsTeam("initial", "city", "name", "score");

            //special case for these two teams since Los Angeles has two teams
            if (homeName.toLowerCase().equals("LA Clippers".toLowerCase())) {
                home = new SportsTeam("LAC", "Los Angeles", "Clippers", homeScore);
            } else if (homeName.toLowerCase().equals("LA Lakers".toLowerCase())) {
                home = new SportsTeam("LAL", "Los Angeles", "Lakers", homeScore);
            }
            if (awayName.toLowerCase().equals("LA Clippers".toLowerCase())) {
                away = new SportsTeam("LAC", "Los Angeles", "Clippers", awayName);
            } else if (awayName.toLowerCase().equals("LA Lakers".toLowerCase())) {
                away = new SportsTeam("LAL", "Los Angeles", "Lakers", awayName);
            }
            for (SportsTeam st : nbaTeams) {
                if (st.city.toLowerCase().equals(homeName.toLowerCase())) {
                    home = new SportsTeam(st.initials, st.city, st.name, homeScore);
                } else if (st.city.toLowerCase().equals(awayName.toLowerCase())) {
                    away = new SportsTeam(st.initials, st.city, st.name, awayScore);
                }
            }
            count++;
            left = "left" + count + "=";
            leftIndex = result.indexOf(left);

            SportsGame sg = new SportsGame(gameTimeOrStartDate,
                    gameStatusOrStartTime,
                    away,
                    home,
                    false,
                    Sport.NBA,
                    timeStatus);

            games.add(sg);
        }

        return games;
    }

    public static ArrayList<SportsGame> getNHLHockeyGames(String result) {
        getNHLTeams();//initialize the list

        ArrayList<SportsGame> hockeyGames = new ArrayList<SportsGame>();
        ArrayList<SportsGame> hockeyGamesToAddToEnd = new ArrayList<SportsGame>();
        while (result.indexOf("\"id\"") >= 1) {
            SportsGame.TimeStatus timeStatus;
            result = result.substring(result.indexOf("\"id\"") + 4);

            String gameTimeOrStartDate = getNextValue(result, "\"ts\""); //"ts":"20:00 1st, "ts":"SATURDAY 3/5", PREGAME, TODAY, FINAL
            String gameStatusOrStartTime = getNextValue(result, "\"bs\""); //"bs":"FINAL", "bs":"LIVE", "bs":"7:00 PM"
            if (gameStatusOrStartTime.contains("FINAL")) {
                gameTimeOrStartDate = gameStatusOrStartTime;
                if (gameStatusOrStartTime.equals("FINAL")) {
                    gameTimeOrStartDate = "FINAL "; //add a space for the watch face
                }
                timeStatus = SportsGame.TimeStatus.PAST;

            } else if (gameStatusOrStartTime.equals("LIVE")) {
                timeStatus = SportsGame.TimeStatus.LIVE;
            } else {
                timeStatus = SportsGame.TimeStatus.FUTURE;
            }

            String[] namesOfDays = new String[]{
                    "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"
            };

            //localization todo test
//            if (gameStatusOrStartTime.toUpperCase().contains("AM") || gameStatusOrStartTime.toUpperCase().contains("PM")) {
//                try {
//                    Calendar c = Calendar.getInstance();
//                    int year = c.get(Calendar.YEAR);
//                    TimeZone tz = c.getTimeZone();
//
//                    String[] dateSplit1 = gameStatusOrStartTime.split(" ");
//                    String[] dateSplit2 = dateSplit1[1].split("/");
//                    int month = Integer.parseInt(dateSplit2[0]);
//                    int day = Integer.parseInt(dateSplit2[1]);
//
//                    String[] timeSplit1 = gameTimeOrStartDate.split(" ");
//                    String[] timeSplit2 = timeSplit1[0].split(":");
//                    int hour = Integer.parseInt(timeSplit2[0]);
//                    int minute = Integer.parseInt(timeSplit2[1]);
//
//                    GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
//                    calendar.set(year, month, day, hour, minute);
//                    int i = calendar.get(Calendar.HOUR);
//                    calendar.setTimeZone(tz);
//                    int i2 = calendar.get(Calendar.HOUR);
//
//                    String am_pm = "";
//                    if (calendar.get(Calendar.AM_PM) == 0)
//                        am_pm = "AM";
//                    else am_pm = "PM";
//
//                    gameTimeOrStartDate = namesOfDays[calendar.get(Calendar.DAY_OF_WEEK)] + " " + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
//                    gameStatusOrStartTime = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " " + am_pm;
//
//                } catch (Exception e) {
//
//                }
//            }

            gameTimeOrStartDate = gameTimeOrStartDate
                    .replace("SATURDAY", "SAT")
                    .replace("SUNDAY", "SUN")
                    .replace("MONDAY", "MON")
                    .replace("TUESDAY", "TUE")
                    .replace("WEDNESDAY", "WED")
                    .replace("THURSDAY", "THU")
                    .replace("FRIDAY", "FRI");

            int closingBracket = result.indexOf("}");
            int nextTeam = result.indexOf("\"atv\"");

            String awayTeamName = getNextValue(result, "\"atv\"");
            String awayTeamScore = getNextValue(result, "\"ats\"");
            SportsTeam awayTeam = findTeam(awayTeamName);

            String homeTeamName = getNextValue(result, "\"htv\"");
            String homeTeamScore = getNextValue(result, "\"hts\"");
            SportsTeam homeTeam = findTeam(homeTeamName);
//
//            if (homeTeamName.equals("canucks")){
//                int i = 0;
//                String s = "";
//            }

            if ((awayTeam != null || homeTeam != null) && nextTeam<closingBracket) {
                awayTeam.setScore(awayTeamScore);
                homeTeam.setScore(homeTeamScore);
                SportsGame hg = new SportsGame(gameTimeOrStartDate,
                        gameStatusOrStartTime,
                        awayTeam,
                        homeTeam,
                        false,
                        Sport.NHL,
                        timeStatus);
                if (gameStatusOrStartTime.toLowerCase().equals("live") || gameStatusOrStartTime.toLowerCase().contains("today")) {
                    hockeyGames.add(0, hg); //the live games will be shown first if they exist
                } else if (gameStatusOrStartTime.toLowerCase().contains("final")) {
                    hockeyGamesToAddToEnd.add(hg);
                } else {
                    hockeyGames.add(hg);
                }
            } else {
                String breakpoint = "";
            }
        }

        for (SportsGame sg : hockeyGamesToAddToEnd) {
            hockeyGames.add(sg);
        }
        return hockeyGames;
        //    {"startIndex":0,
//            "refreshInterval":20,
//            "games":
//        [{"id":2015020744,
//            "gs":3,
//            "ts":"05:17 2nd",
//            "tsc":"progress",
//            "bs":"LIVE", //or when game will start
//            "bsc":"progress",
//            "atn":"Toronto",
//            "atv":"mapleleafs",
//            "ats":"1",
//            "atc":"progress",
//            "htn":"Boston",
//            "htv":"bruins",
//            "hts":"1",
//            "htc":"progress",
//            "pl":true,
//            "rl":false,
//            "vl":false,
//            "gcl":true,
//            "gcll":true,
//            "ustv":"",
//            "catv":"TVAS"},{"
    }

    public static ArrayList<SportsGame> getMLSGames(String result) {
        InitializeMLSList();
        return getScoreFromYahoo(result, Sport.MLS, mlsTeams);
    }

    public static ArrayList<SportsGame> getEuro2016Games(String result) {
        InitializeEuro2016List();
        return getScoreFromYahoo(result, Sport.EURO_2016, euro2016Teams);
    }

    public static ArrayList<SportsGame> getNCAAFGames(String result) {
        InitializeNCAAFList();
        return getScoreFromYahoo(result, Sport.NCAAF, ncaafTeams);
    }

    public static ArrayList<SportsGame> getNCAABGames(String result) {
        InitializeAllTeams();
        return new ArrayList<SportsGame>(); //TODO
    }

    public static ArrayList<SportsGame> getPremierLeagueGames(String result) {
        InitializeAllTeams();
        return getScoreFromYahoo(result, Sport.PremierLeague, PremierLeagueTeams);
    }

    public static ArrayList<SportsGame> getLigue1Games(String result) {
        InitializeAllTeams();
        return getScoreFromYahoo(result, Sport.Ligue1, Ligue1Teams); //TODO
    }

    public static ArrayList<SportsGame> getSerieAGames(String result) {
        InitializeAllTeams();
        return getScoreFromYahoo(result, Sport.SerieA, SerieATeams);
    }

    public static ArrayList<SportsGame> getFACupGames(String result) {
        InitializeAllTeams();
        return new ArrayList<SportsGame>(); //TODO
    }

    public static ArrayList<SportsGame> getChampionshipGames(String result) {
        InitializeAllTeams();
        return getScoreFromYahoo(result, Sport.Championship, ChampionshipTeams);
    }

    public static ArrayList<SportsGame> getChampionLeagueGames(String result) {
        InitializeAllTeams();
        return getScoreFromYahoo(result, Sport.ChampionLeague, ChampionLeagueTeams);
    }

    public static ArrayList<SportsGame> getEuropaLeagueGames(String result) {
        InitializeAllTeams();
        return new ArrayList<SportsGame>(); //TODO
    }

    public static ArrayList<SportsGame> getLALigaGames(String result) {
        InitializeAllTeams();
        return getScoreFromYahoo(result, Sport.LALiga, LALigaTeams);
    }

    public static ArrayList<SportsGame> getBundesligaGames(String result) {
        InitializeAllTeams();
        return getScoreFromYahoo(result, Sport.Bundesliga, BundesligaTeams);
    }

    public static ArrayList<SportsGame> getScottishPremiershipGames(String result) {
        InitializeAllTeams();
        return getScoreFromYahoo(result, Sport.ScottishPremiership, ScottishPremiershipTeams);
    }

    public static ArrayList<SportsGame> getNFLGames(String result) {
        InitializeAllTeams();
        return getScoreFromYahoo(result, Sport.NFL, nflTeams);
    }

    //    <span title="Postponed: Celtic vs. Partick Thistle">Celtic vs. Partick Thistle</span>
//    </dt>
//    <dd class="state">
//    <span class="period">PPD.</span>
//    </dd>        <dd class="team ">
//    <span class="score">P</span> Celtic
//            </dd>
//    <dd class="team ">
//    <span class="score">P</span> Partick Thistle
//    </dd>            </dl>
//    </div>
    public static ArrayList<SportsGame> getScoreFromYahoo(String result, Sport sport, ArrayList<SportsTeam> teams) {

        ArrayList<SportsGame> games = new ArrayList<>();

        String startText = "";
        String endText = "";
        if (sport == Sport.MLS
                || sport == Sport.EURO_2016
                || sport == Sport.ScottishPremiership
                || sport == Sport.Championship
                || sport == Sport.PremierLeague
                || sport == Sport.SerieA
                || sport == Sport.Ligue1
                || sport == Sport.ChampionLeague
                || sport == Sport.LALiga
                || sport == Sport.Bundesliga) {
            startText = "<li class=\"soccer-header\">";
            endText = "<div class=\"league-header\">";
        } else if (sport == Sport.NCAAF) {
            startText = "<li class=\"ncaaf-header\">";
            endText = "<div id=\"Navigation\"";
        } else if (sport == Sport.NFL) {
            startText = "<li class=\"nfl-header\">";
            endText = "<div id=\"Navigation\"";
        }

        String gameStartTag = "data-time=\"";
        String stateTag = "<dd class=\"state\">";
        String scoreTag = "<span class=\"score\">";
        String ddTag = "</dd>";
        String spanOpenTag = "<span>";
        String spanCloseTag = "</span>";
        String emOpenTag = "<em>";
        String emCloseTag = "</em>";
        String teamNameTag = "<dd class=\"team \">";
        String elapsedTimeTag = "<span class=\"elapsed-time\">";
        String periodTimeTag = "<span class=\"period\">";

        int startIndex = result.indexOf(startText);
        int endIndex = result.indexOf(endText);

        if (startIndex != -1 && endIndex != -1) {
            result = result.substring(startIndex, endIndex);

            int index = result.indexOf(gameStartTag);

            int countBreaker = 0;
            while (index > 0) {
                countBreaker++;
                SportsGame.TimeStatus timeStatus;
                String gameTimeOrStartDate = ""; //"ts":"20:00 1st, "ts":"SAT 3/5", PREGAME, TODAY
                String gameStatusOrStartTime = "";  //"bs":"FINAL", "bs":"LIVE", "bs":"7:00 PM"
                SportsTeam home = new SportsTeam("initial", "city", "name", "score");
                SportsTeam away = new SportsTeam("initial", "city", "name", "score");

                result = result.substring(index + gameStartTag.length(), result.length());
                String tempResult = result;

                int timeStampStart = 0;
                int timeStampEnd = tempResult.indexOf("\">");
                String timeStamp = tempResult.substring(timeStampStart, timeStampEnd);

                int stateStart = tempResult.indexOf(stateTag);
                tempResult = tempResult.substring(stateStart + stateTag.length(), tempResult.length());
                int stateEnd = tempResult.indexOf(ddTag);
                String state = tempResult.substring(0, stateEnd).trim();

                String state2 = tempResult.substring(stateEnd + ddTag.length(), tempResult.indexOf("</li>"));

                String homeTeam = "";
                String awayTeam = "";
                String homeScore = "";
                String awayScore = "";

                if (state.contains("Live")) {

                    int awayScoreStart = tempResult.indexOf(scoreTag);
                    tempResult = tempResult.substring(awayScoreStart + scoreTag.length(), tempResult.length());
                    int awayScoreEnd = tempResult.indexOf(spanCloseTag);
                    String awayScoreEm = tempResult.substring(0, awayScoreEnd).trim();
                    awayScore = awayScoreEm.substring(awayScoreEm.indexOf(emOpenTag) + emOpenTag.length(), awayScoreEm.indexOf(emCloseTag));
                    int awayTeamEnd = tempResult.indexOf(ddTag);
                    awayTeam = tempResult.substring(awayScoreEnd + spanCloseTag.length(), awayTeamEnd).trim();
                    tempResult = tempResult.substring(awayTeamEnd + ddTag.length(), tempResult.length());
                    if(countBreaker == 5){
                        int x = countBreaker - 1;
                    }
                    int homeScoreStart = tempResult.indexOf(scoreTag);
                    tempResult = tempResult.substring(homeScoreStart + scoreTag.length(), tempResult.length());
                    int homeScoreEnd = tempResult.indexOf(spanCloseTag);
                    String homeScoreEm = tempResult.substring(0, homeScoreEnd).trim();
                    homeScore = homeScoreEm.substring(homeScoreEm.indexOf(emOpenTag) + emOpenTag.length(), homeScoreEm.indexOf(emCloseTag));
                    int homeTeamEnd = tempResult.indexOf(ddTag);
                    homeTeam = tempResult.substring(homeScoreEnd + spanCloseTag.length(), homeTeamEnd).trim();
                    String time = "";

                    if(state.contains("<span>Live") && !state.contains("elapsed-time")) {
                        state = "Live";
                        if(state2.contains("Halftime")) {
                            time="Halftime";
                        }else {
                            time = state2.substring(state2.indexOf(periodTimeTag) + periodTimeTag.length(), state2.indexOf(spanCloseTag));
                        }
                    }
                    else {
                        state = state.substring(state.indexOf(elapsedTimeTag) + elapsedTimeTag.length(), state.length());
                        time = state.substring(0, state.indexOf(spanCloseTag));
                    }

                    if(time.contains("Halftime")) {
                        gameTimeOrStartDate = "Half";
                    }
                    else {
                        gameTimeOrStartDate = time;
                    }
                    gameStatusOrStartTime = "Live";
                    timeStatus = SportsGame.TimeStatus.LIVE;
                } else if (state.contains("FT") || state.contains("Final")) {
                    int awayScoreStart = tempResult.indexOf(scoreTag);
                    tempResult = tempResult.substring(awayScoreStart + scoreTag.length(), tempResult.length());
                    int awayScoreEnd = tempResult.indexOf(spanCloseTag);
                    awayScore = tempResult.substring(0, awayScoreEnd).trim();
                    int awayTeamEnd = tempResult.indexOf(ddTag);
                    awayTeam = tempResult.substring(awayScoreEnd + spanCloseTag.length(), awayTeamEnd).trim();
                    tempResult = tempResult.substring(awayTeamEnd + ddTag.length(), tempResult.length());

                    int homeScoreStart = tempResult.indexOf(scoreTag);
                    tempResult = tempResult.substring(homeScoreStart + scoreTag.length(), tempResult.length());
                    int homeScoreEnd = tempResult.indexOf(spanCloseTag);
                    homeScore = tempResult.substring(0, homeScoreEnd).trim();
                    int homeTeamEnd = tempResult.indexOf(ddTag);
                    homeTeam = tempResult.substring(homeScoreEnd + spanCloseTag.length(), homeTeamEnd).trim();


                    gameStatusOrStartTime = "";
                    if (state.equals("FT")) {
                        gameTimeOrStartDate = "FT";
                    } else if (state.equals("Final")) {
                        gameTimeOrStartDate = "Final";
                    } else if (state.contains("Overtime")) {
                        gameTimeOrStartDate = "Final OT";
                    }
                    timeStatus = SportsGame.TimeStatus.PAST;
                } else {
                    String day = "";
                    if(state.indexOf(spanOpenTag) != -1) {
                        day = state.substring(state.indexOf(spanOpenTag) + spanOpenTag.length(), state.indexOf(spanCloseTag));
                    }
                    String hour = "";
                    if (state.indexOf(emOpenTag) != -1) {
                        hour = state.substring(state.indexOf(emOpenTag) + emOpenTag.length(), state.indexOf(emCloseTag));
                    }

                    state = day + " " + hour;
                    if (day.contains("PPD.")) {
                        state = "PPD.";
                    }
                    int awayTeamStart = tempResult.indexOf(teamNameTag);
                    tempResult = tempResult.substring(awayTeamStart + teamNameTag.length(), tempResult.length());
                    int awayTeamEnd = tempResult.indexOf(ddTag);
                    awayTeam = tempResult.substring(0, awayTeamEnd).trim();
                    tempResult = tempResult.substring(awayTeamEnd + ddTag.length(), tempResult.length());

                    int homeTeamStart = tempResult.indexOf(teamNameTag);
                    tempResult = tempResult.substring(homeTeamStart + teamNameTag.length(), tempResult.length());
                    int homeTeamEnd = tempResult.indexOf(ddTag);
                    homeTeam = tempResult.substring(0, homeTeamEnd).trim();

                    Timestamp stamp;
                    Date gameDate = new Date();
                    try {
                        Long timestampLong = Long.parseLong(timeStamp) * 1000;
                        stamp = new Timestamp(timestampLong);
                        gameDate = new Date(stamp.getTime());

//                            if (sport == SportsGame.Sport.EURO_2016) {
//                                //british time
//                                timestampLong = timestampLong - (5 * 60 * 60 * 1000);
//                            }
//                            stamp = new Timestamp(timestampLong);
//                            gameDate = new Date(stamp.getTime());
                    } catch (Exception e) {

                    }
                    String[] namesOfDays = new String[]{
                            "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"
                    };
                    int dayOfWeek = gameDate.getDay();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    String strDate = dateFormat.format(gameDate);

                    gameStatusOrStartTime = strDate; //"bs":"FINAL", "bs":"LIVE", "bs":"7:00 PM"
                    gameTimeOrStartDate = namesOfDays[dayOfWeek] + " " + (gameDate.getMonth() + 1) + "/" + gameDate.getDate(); //"ts":"20:00 1st, "ts":"SAT 3/5", PREGAME, TODAY
                    timeStatus = SportsGame.TimeStatus.FUTURE;
                }

                homeTeam = teamNameChange(sport, homeTeam);
                awayTeam = teamNameChange(sport, awayTeam);


                if (sport == Sport.ChampionLeague || sport == Sport.Bundesliga) {
                    for (SportsTeam st : teams) {
                        if ((st.city + " " + st.name).contains(homeTeam)) {
                            home = new SportsTeam(st.initials, st.city, st.name, homeScore);
                        } else if ((st.city + " " + st.name).contains(awayTeam)) {
                            away = new SportsTeam(st.initials, st.city, st.name, awayScore);
                        }
                    }
                } else if (sport == Sport.MLS ) {
                    for (SportsTeam st : teams) {
                        if (homeTeam.equals(st.city + " " + st.name)) {
                            home = new SportsTeam(st.initials, st.city, st.name, homeScore);
                        } else if (awayTeam.equals(st.city + " " + st.name)) {
                            away = new SportsTeam(st.initials, st.city, st.name, awayScore);
                        }
                    }
                } else if (sport == Sport.PremierLeague
                        || sport == Sport.SerieA) {

                    if(sport == Sport.PremierLeague) {
                        if (homeTeam.equals("Man Utd")) {
                            home = new SportsTeam("MUN", "Manchester", "United", homeScore);
                        } else if (homeTeam.equals("Man City")) {
                            home = new SportsTeam("MCI", "Manchester", "City", homeScore);
                        }

                        if (awayTeam.equals("Man Utd")) {
                            away = new SportsTeam("MUN", "Manchester", "United", awayScore);
                        } else if (awayTeam.equals("Man City")) {
                            away = new SportsTeam("MCI", "Manchester", "City", awayScore);
                        }
                    }

                    for (SportsTeam st : teams) {
                        if (homeTeam.equals(st.city)) {
                            home = new SportsTeam(st.initials, st.city, st.name, homeScore);
                        } else if (awayTeam.equals(st.city)) {
                            away = new SportsTeam(st.initials, st.city, st.name, awayScore);
                        }
                    }
                } else if (sport == Sport.EURO_2016
                        || sport == Sport.ScottishPremiership) {
                    for (SportsTeam st : teams) {
                        if (homeTeam.contains(st.name)) {
                            home = new SportsTeam(st.initials, st.city, st.name, homeScore);
                        } else if (awayTeam.contains(st.name)) {
                            away = new SportsTeam(st.initials, st.city, st.name, awayScore);
                        }
                    }
                } else if (sport == Sport.Championship
                        || sport == Sport.Ligue1
                        || sport == Sport.LALiga) {

                    for (SportsTeam st : teams) {
                        if (st.city.contains(homeTeam)) {
                            home = new SportsTeam(st.initials, st.city, st.name, homeScore);
                        } else if (st.city.contains(awayTeam)) {
                            away = new SportsTeam(st.initials, st.city, st.name, awayScore);
                        }
                    }
                }  else if (sport == Sport.NFL) {
                    for (SportsTeam st : teams) {
                        if (homeTeam.equals(st.initials)) {
                            home = new SportsTeam(st.initials, st.city, st.name, homeScore);
                        } else if (awayTeam.equals(st.initials)) {
                            away = new SportsTeam(st.initials, st.city, st.name, awayScore);
                        }
                    }
                } else if (sport == Sport.NCAAF) {

                    if(awayTeam.equals("POR ST") || homeTeam .equals("POR ST")){
                        String s = "tset";
                        s = s.toLowerCase();
                    }
                    for (SportsTeam st : teams) {
                        if (st.initials.equals(homeTeam)) {
                            home = new SportsTeam(st.initials, st.city, st.name, homeScore);
                        } else if (st.initials.equals(awayTeam)) {
                            away = new SportsTeam(st.initials, st.city, st.name, awayScore);
                        }

                        if (home.score != "score" && away.score != "score") {
                            break;
                        }
                    }
                }
                if (home.score != "score" || away.score != "score") {
                    games.add(new SportsGame(gameTimeOrStartDate, gameStatusOrStartTime, away, home, false, sport, timeStatus));
                }
                index = result.indexOf(gameStartTag);
            }
        }
        devCount++;
        return games;
    }
    public static int devCount = 0;

    public static String teamNameChange(Sport sport, String name) {
         if (sport == Sport.Bundesliga) {
            if (name.equals("Borussia M'gladbach")) {
                return "Borussia Mönchengladbach";
            }
        } else if (sport == Sport.ChampionLeague) {
            if (name.equals("Borussia M'gladbach")) {
                return "Borussia Mönchengladbach";
            } else if (name.equals("Man City")) {
                return "Manchester City";
            } else if (name.equals("Paris SG")) {
                return "Paris Saint-Germain";
            } else if (name.equals("Spurs")) {
                return "Tottenham Hotspur";
            }
        } else if (sport == Sport.Ligue1) {
            if (name.equals("Paris SG")) {
                return "Paris";
            } else if (name.equals("St Etienne")) {
                return "Saint-Étienne";
            }
        } else if (sport == Sport.PremierLeague) {
            if (name.equals("West Brom")) {
                return "West Bromwich";
            } else if (name.equals("Crystal Palace")) {
                return "Crystal";
            } else if (name.equals("Spurs")) {
                return "Tottenham";
            }
        } else if (sport == Sport.Championship) {
            if (name.equals("QPR")) {
                return "Queens Park";
            } else if (name.equals("Wolves")) {
                return "Wolverhampton";
            } else if (name.equals("Nott'm Forest")) {
                return "Nottingham";
            } else if (name.equals("Sheffield Wed")) {
                return "Sheffield";
            } else if (name.equals("Bristol C")) {
                return "Bristol";
            }
        } else if(sport == Sport.NCAAF){
            String spanEndTag = "</span>";
            if(name.contains(spanEndTag)){
                name = name.substring(name.indexOf(spanEndTag) + spanEndTag.length() + 1);
                return name;
            }
        }
        return name;
    }

    public static SportsTeam findTeam(String compareName) {
        for (SportsTeam ht : hockeyTeams) {
            if (ht.compareName.equals(compareName)) {
                return new SportsTeam(ht.initials, ht.city, ht.name, false);
            }
        }
        return null;
    }

    static int count=0;
    public static String getNextValue(String result, String keyword) {
        count++;
        try {
            int index1 = result.indexOf(keyword);
            result = result.substring(index1);
            int index2 = result.indexOf(",");
            String retStr = result.substring(keyword.length() + 2, index2 - 1);
            return retStr;
        }
        catch (Exception e) {
            String ss = e.toString();
            return  "";
        }

    }

//    public static String[] getTeamData(String myTeam, String result) {
//        String textToWrite = "";
//
//        String timeText = "";
//        String awayTeam = "";
//        String awayScore = "";
//        String homeTeam = "";
//        String homeScore = "";
//
//        int teamIndex = result.indexOf("<span>" + myTeam + "</span>");
//        if (teamIndex != -1) {
//            String s1 = result.substring(0, teamIndex);
//            String s2 = result.substring(teamIndex);
//
//            int startIndex = s1.lastIndexOf("gmInfo");
//            int endIndex = s2.indexOf("gmInfo");
//            if (endIndex == -1) {
//                //this means that its the last game in the list, so use this instead
//                endIndex = s2.indexOf("<script type=\"text/javascript\">");
//            }
//            String myTeamResult = result.substring(startIndex, teamIndex + endIndex + 8);
//
//            String finalSearch = "<span class=\"final\">";
//            String progressSearch = "<span class=\"progress\">";
//            String spanStartSearch = "<span>";
//            String spanEndSearch = "</span>";
//            String awayLineSearch = "<div class=\"awayLine\">";
//            String scoreSearch = "<div class=\"score \">";
//            String scoreWinnerSearch = "<div class=\"score winner\">";
//            String divEndSearch = "</div>";
//
//            int finalIndex = myTeamResult.indexOf(finalSearch);
//            int progressIndex = myTeamResult.indexOf(progressSearch);
//
//            if (finalIndex != -1) {
//                myTeamResult = myTeamResult.substring(finalIndex);
//
//                int progressEndIndex = myTeamResult.indexOf(spanEndSearch);
//                timeText = myTeamResult.substring(finalSearch.length(), progressEndIndex);
//
//                int awayLineIndex = myTeamResult.indexOf(awayLineSearch);
//                myTeamResult = myTeamResult.substring(awayLineIndex);
//
//                int awayTeamIndex = myTeamResult.indexOf(spanStartSearch) + spanStartSearch.length();
//                int awayTeamEndIndex = myTeamResult.indexOf(spanEndSearch);
//                awayTeam = myTeamResult.substring(awayTeamIndex, awayTeamEndIndex);
//                myTeamResult = myTeamResult.substring(awayTeamEndIndex + spanEndSearch.length());
//
//                int awayTeamScoreIndex = myTeamResult.indexOf(scoreSearch);
//                int awayTeamScoreIndex2 = myTeamResult.indexOf(scoreWinnerSearch);
//
//                awayScore = "";
//                if (awayTeamScoreIndex2 < awayTeamScoreIndex) {
//                    myTeamResult = myTeamResult.substring(awayTeamScoreIndex2);
//                    int awayTeamScoreEndIndex = myTeamResult.indexOf(divEndSearch);
//                    awayScore = myTeamResult.substring(scoreWinnerSearch.length(), awayTeamScoreEndIndex);
//                } else {
//                    myTeamResult = myTeamResult.substring(awayTeamScoreIndex);
//                    int awayTeamScoreEndIndex = myTeamResult.indexOf(divEndSearch);
//                    awayScore = myTeamResult.substring(scoreSearch.length(), awayTeamScoreEndIndex);
//                }
//                int homeTeamIndex = myTeamResult.indexOf(spanStartSearch) + spanStartSearch.length();
//                int homeTeamEndIndex = myTeamResult.indexOf(spanEndSearch);
//                homeTeam = myTeamResult.substring(homeTeamIndex, homeTeamEndIndex);
//
//                myTeamResult = myTeamResult.substring(homeTeamEndIndex);
//
//                int homeTeamScoreIndex = myTeamResult.indexOf(scoreSearch);
//                int homeTeamScoreIndex2 = myTeamResult.indexOf(scoreWinnerSearch);
//
//                if (homeTeamScoreIndex == -1) {
//                    myTeamResult = myTeamResult.substring(homeTeamScoreIndex2);
//                    int awayTeamScoreEndIndex = myTeamResult.indexOf(divEndSearch);
//                    homeScore = myTeamResult.substring(scoreWinnerSearch.length(), awayTeamScoreEndIndex);
//                } else {
//                    myTeamResult = myTeamResult.substring(homeTeamScoreIndex);
//                    int homeTeamScoreEndIndex = myTeamResult.indexOf(divEndSearch);
//                    homeScore = myTeamResult.substring(scoreSearch.length(), homeTeamScoreEndIndex);
//                }
//                textToWrite += "Time: " + timeText + "\n";
//                textToWrite += "Away Team: " + awayTeam + "\n";
//                textToWrite += "Away Score: " + awayScore + "\n";
//                textToWrite += "Home Team: " + homeTeam + "\n";
//                textToWrite += "Home Score: " + homeScore + "\n";
//                //textToWrite += myTeamResult + "\n";
//            } else if (progressIndex != -1) {
//                myTeamResult = myTeamResult.substring(progressIndex);
//
//                int progressEndIndex = myTeamResult.indexOf(spanEndSearch);
//                timeText = myTeamResult.substring(progressSearch.length(), progressEndIndex);
//
//                int awayLineIndex = myTeamResult.indexOf(awayLineSearch);
//                myTeamResult = myTeamResult.substring(awayLineIndex);
//
//                int awayTeamIndex = myTeamResult.indexOf(spanStartSearch) + spanStartSearch.length();
//                int awayTeamEndIndex = myTeamResult.indexOf(spanEndSearch);
//                awayTeam = myTeamResult.substring(awayTeamIndex, awayTeamEndIndex);
//                myTeamResult = myTeamResult.substring(awayTeamEndIndex + spanEndSearch.length());
//
//                int awayTeamScoreIndex = myTeamResult.indexOf(scoreSearch);
//                myTeamResult = myTeamResult.substring(awayTeamScoreIndex);
//                int awayTeamScoreEndIndex = myTeamResult.indexOf(divEndSearch);
//                awayScore = myTeamResult.substring(scoreSearch.length(), awayTeamScoreEndIndex);
//
//                int homeTeamIndex = myTeamResult.indexOf(spanStartSearch) + spanStartSearch.length();
//                int homeTeamEndIndex = myTeamResult.indexOf(spanEndSearch);
//                homeTeam = myTeamResult.substring(homeTeamIndex, homeTeamEndIndex);
//
//                myTeamResult = myTeamResult.substring(homeTeamEndIndex);
//
//                int homeTeamScoreIndex = myTeamResult.indexOf(scoreSearch);
//                myTeamResult = myTeamResult.substring(homeTeamScoreIndex);
//                int homeTeamScoreEndIndex = myTeamResult.indexOf(divEndSearch);
//                homeScore = myTeamResult.substring(scoreSearch.length(), homeTeamScoreEndIndex);
//
//                textToWrite += "Time: " + timeText + "\n";
//                textToWrite += "Away Team: " + awayTeam + "\n";
//                textToWrite += "Away Score: " + awayScore + "\n";
//                textToWrite += "Home Team: " + homeTeam + "\n";
//                textToWrite += "Home Score: " + homeScore + "\n";
//                //textToWrite += myTeamResult + "\n";
//
//            }
//            for (SportsTeam team : SportsGame.hockeyTeams) {
//                if (team.initials.equals(awayTeam)) {
//                    awayTeam = team.toString();
//                } else if (team.initials.equals(homeTeam)) {
//                    homeTeam = team.toString();
//                }
//            }
//            return new String[]{timeText, awayTeam, awayScore, homeTeam, homeScore};
//
//        } else {
//            textToWrite = "Team information unvailable";
//            return new String[]{"Team information unvailable"};
//        }
//    }
}
