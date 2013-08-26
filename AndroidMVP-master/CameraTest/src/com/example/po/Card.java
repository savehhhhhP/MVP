package com.example.po;

public class Card {
    public String id;
    public String type;
    public String name;
    public String image;
    public String audio;
    public int position;
    public boolean Used;
    public String image_filename;
    public String audio_filename;

    public Card() {
        this.id = "";
        this.type = "";
        this.name = "";
        this.image = "";
        this.audio = "";
        this.position = 0;
        Used = false;
        this.image_filename = "";
        this.audio_filename = "";
    }

    public Card(String id, String type, String name, String image, String audio, int position, boolean used, String image_filename, String audio_filename) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.image = image;
        this.audio = audio;
        this.position = position;
        Used = used;
        this.image_filename = image_filename;
        this.audio_filename = audio_filename;
    }

    public void setUsed(boolean is){
        Used = is;
    }
    public boolean isUsed(){
        return Used;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getImage_filename() {
        return image_filename;
    }

    public void setImage_filename(String image_filename) {
        this.image_filename = image_filename;
    }

    public String getAudio_filename() {
        return audio_filename;
    }

    public void setAudio_filename(String audio_filename) {
        this.audio_filename = audio_filename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
