package com.charity.charity.models;

import com.charity.charity.services.MakeArrayImagesName;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    @Size(max = 20000, message = "Максимальний размір тексту 7000 символів")
    @Column(columnDefinition="LONGTEXT", length=20000)
    private String originInfo;

    @Size(min = 1, message = "Выберите страну")
    private String country;

    @Size(min = 1, message = "Выберите тип")
    private String type;

    @Size(min = 1, message = "Выберите категорию")
    private String category;

    @JoinColumn(name = "author")
    @OneToOne(fetch = FetchType.EAGER)
    private User user;


    @JoinColumn(name = "item_id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //сделать стратегию связки, чтобы автоматом удалять связанные записи в дочерней таблице
    private List<CustomInfo> customInfos = new ArrayList<>();


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

    public void setCustomInfos(List<CustomInfo> customInfos) {
        this.customInfos = customInfos;
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

        String[] firstImageName = MakeArrayImagesName.makeArrayImageName(this.fileName);

        return firstImageName[0];
    }


}
