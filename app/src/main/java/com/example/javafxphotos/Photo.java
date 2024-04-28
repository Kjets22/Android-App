package com.example.javafxphotos;

import android.net.Uri;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable{
    String imagePath;
    private List<Tag> tags = new ArrayList<>();
    private static final long serialVersionUID = 1L;
    public Photo(String imagePath){
        this.imagePath = imagePath;
    }

    public String getPath(){
        return imagePath;
    }
    public void setTags(List<Tag> newTags){
        tags = newTags;
    }
    public List<Tag> getTags(){
        return tags;
    }

    public void add_tag(Tag e){
        getTags().add(e);
    }

    public void remove_tag(Tag e){
        tags.removeIf(tag -> tag.value.equalsIgnoreCase(e.value) &&
                tag.name.equalsIgnoreCase(e.name));
    }

    public void delete_tag(String value, String name){
        for (Tag tag : tags){
            if(value == tag.getName() && name == tag.getValue()){
                tags.remove(tag);
            }
        }
    }

}

