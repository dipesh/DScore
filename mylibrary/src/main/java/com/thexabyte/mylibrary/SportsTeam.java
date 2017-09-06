package com.thexabyte.mylibrary;


/**
 * Created by Dipesh on 2/2/2016.
 */
public class SportsTeam {
    public String initials;
    public String city;
    public String name;
    public String compareName; //name used when comparing in the service
    public String score;
    public boolean isFavorite;

    public void setScore(String score){
        this.score = score;
    }
    public SportsTeam(String initials, String city, String name, boolean isFavorite) {
        this.initials = initials;
        this.city= city;
        this.name = name;
        this.compareName = name.toLowerCase().replace(" ", "");
        this.isFavorite = isFavorite;

//        if(city.equals("New York")){
//            this.compareName = "NY " + this.name;
//        }
//        else {
//            this.compareName = city;
//        }
    }

    //for watch faces
    public SportsTeam(String initials, String city, String name, String score) {
        this.initials = initials;
        this.city= city;
        this.name = name;
        this.compareName = name.toLowerCase().replace(" ", "");
        this.score = score;
    }
    @Override
    public String toString(){
        return  initials +":"+city+":"+name;
    }
}