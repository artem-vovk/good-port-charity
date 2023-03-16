package com.charity.charity.models;

import com.charity.charity.services.MakeArrayImagesName;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id", nullable = false)
    private Long id;

    private String originLanguage;

    @Min(value = 0, message = "{form_errors.item_price.min}")
    @Max(value = 100000000,message = "{form_errors.item_price.max}")
    private int price;

    @Column(columnDefinition="TEXT", length = 2000)
    private String fileName;

    private long date;

    @Size(min = 40, max = 245, message = "{form_errors.item_title.size}")
    private String originTitle;

    @Size(max = 20000, message = "{page-update.form.label_text}")
    @Column(columnDefinition="LONGTEXT", length=20000)
    private String originInfo;

    @Size(min = 1, message = "{filer_param.error_mess.select_country}")
    private String country;

    @Size(min = 1, message = "{filer_param.error_mess.select_type}")
    private String type;

    @Size(min = 1, message = "{filer_param.error_mess.select_category}")
    private String category;

    @JoinColumn(name = "author")
    @OneToOne(fetch = FetchType.EAGER)
    private User user;


    @JoinColumn(name = "item_id_join")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) //orphanRemoval = true - is very important for remove children
    private List<CustomInfo> customInfos = new CopyOnWriteArrayList<CustomInfo>();


    public Item(){
    }

    public Item(int price, String originTitle, String originInfo) {
        this.price = price;
        this.originTitle = originTitle;
        this.originInfo = originInfo;
    }

    public Item(int price, String originTitle, String originInfo, String country, String type, String category, Locale locale) {
        this.price = price;
        this.country = country;
        this.type = type;
        this.category = category;
        String actualLanguage = locale.getLanguage();
        addTranslationToCustomInfos("Title", actualLanguage, originTitle);
        addTranslationToCustomInfos("Info", actualLanguage, originInfo);
    }

    public List<CustomInfo> getCustomInfos() {
        return customInfos;
    }

    public void setCustomInfos(List<CustomInfo> customInfos) {
        this.customInfos = customInfos;
    }

    public void addCustomInfo(CustomInfo customInfo) {
        this.customInfos.add(customInfo);
    }

    public void deleteCustomInfo(CustomInfo customInfo){
        this.customInfos.remove(customInfo);
    }

    public void addTranslationToCustomInfos(String contentName, String language, String translation){
        List<CustomInfo> customInfos = new ArrayList<>();
        CustomInfo customInfo1 = new CustomInfo(contentName, language, translation);
        customInfos.add(customInfo1);
        this.setCustomInfos(customInfos);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getOriginLanguage() {
        return originLanguage;
    }

    public void setOriginLanguage(String originLanguage) {
        this.originLanguage = originLanguage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOriginTitle() {
        return originTitle;
    }

    public void setOriginTitle(String originTitle) {
        this.originTitle = originTitle;
    }

    public String getOriginInfo() {
        return originInfo;
    }

    public void setOriginInfo(String originInfo) {
        this.originInfo = originInfo;
    }



    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getFirstImageName(){

        String[] firstImageName = getImageNamesAsArray();

        return firstImageName[0];
    }

    public String[] getImageNamesAsArray() {
        if (this.fileName != null) {
            return this.fileName.split("\\,");
        } else {
            return new String[]{};
        }
    }

}
