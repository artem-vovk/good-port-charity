package com.charity.charity.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class CustomInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customInfo_id", nullable = false)
    private Long id;


    private String language;

    private String contentName;

    private String translation;

    public CustomInfo() {
    }

    public CustomInfo(String contentName, String language, String translation) {
        this.contentName = contentName;
        this.language = language;
        this.translation = translation;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
