package com.example.javafxphotos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Album implements Serializable {
    private List<Photo> photos = new ArrayList<Photo>();
    String name;

    public Album(String name){
        this.name=name;
    }


    /**
     * @return String
     */
    public String getName(){
        return name;
    }

    public List<Photo> getPhotos(){
        return photos;
    }

    public void set_name(String title){
        name = title;
    }

    public void add_photo(String location, List<Tag> tags){
        photos.add(new Photo(location));
    }
    public void add_photo(Photo photo){
        photos.add(photo);
    }

    public void remove_photo(String location){
        findphoto_action(location,photo->photos.remove(photo));
    }

    public void findphoto_action(String location, Consumer<Photo> action){
        for (Photo photo : photos){
            if(location.equals(photo.getLocation())){
                action.accept(photo);
            }
        }
    }

}

