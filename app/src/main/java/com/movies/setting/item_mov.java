package com.movies.setting;

public class item_mov {
    private int id;
    private String MovieUrl;
    private String MovieTrailer;
    private String MovieTitle;
    private String Image;
    private String Description;
    private String Download;
    private String Quality;
    private String Subtitle;
    private String Rating;
    private String Trailer;
    private String Catname;
    private String Catid;

    public item_mov(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieUrl() {
        return MovieUrl;
    }

    public void setMovieUrl(String movieUrl) {
        MovieUrl = movieUrl;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDownload() {
        return Download;
    }

    public void setDownload(String download) {
        Download = download;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getMovieTitle() {
        return MovieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        MovieTitle = movieTitle;
    }

    public String getMovieTrailer() {
        return MovieTrailer;
    }

    public void setMovieTrailer(String movieTrailer) {
        MovieTrailer = movieTrailer;
    }

    public String getQuality() {
        return Quality;
    }

    public void setQuality(String quality) {
        Quality = quality;
    }

    public String getSubtitle() {
        return Subtitle;
    }

    public void setSubtitle(String subtitle) {
        Subtitle = subtitle;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getTrailer() {
        return Trailer;
    }

    public void setTrailer(String trailer) {
        Trailer = trailer;
    }

    public String getCatname() {
        return Catname;
    }

    public void setCatname(String catname) {
        Catname = catname;
    }

    public String getCatid() {
        return Catid;
    }

    public void setCatid(String catid) {
        Catid = catid;
    }
}
