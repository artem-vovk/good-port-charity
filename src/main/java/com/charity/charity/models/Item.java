package com.charity.charity.models;

import com.charity.charity.controllers.ItemController;
import com.charity.charity.services.ReversoContext;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class Item {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    //Original language
    private String originLanguage;

//    @NotEmpty(message = "{form_errors.all_fields.not_empty}")
//    @Size(min = 1, message = "{form_errors.all_fields.not_empty}")
    @Min(value = 0, message = "{form_errors.item_price.min}")
    @Max(value = 100000000,message = "{form_errors.item_price.max}")
    private int price;

    @Column(columnDefinition="TEXT", length = 2000)
    private String fileName;

    //date in mSec
    private long date;

    //United field
    @JoinColumn(name = "author")
    @OneToOne(fetch = FetchType.EAGER)
    private User user;


    @Size(min = 40, max = 245, message = "{form_errors.item_title.size}")
    private String title; //default language - Ukrainian

    @Size(min = 40, max = 245, message = "{form_errors.item_title.size}")
    private String titleEn;

    @Size(min = 40, max = 245, message = "{form_errors.item_title.size}")
    private String titleDe;

    @Size(min = 40, max = 245, message = "{form_errors.item_title.size}")
    private String titlePl;

    @Size(min = 40, max = 245, message = "{form_errors.item_title.size}")
    private String titleRu;


    @Size(max = 7000, message = "Максимальний размір тексту 4000 символів")
    @Column(columnDefinition="LONGTEXT", length=20000)
    private String info; //default language - Ukrainian

    @Size(max = 7000, message = "Maximum text size 4000 characters")
    @Column(columnDefinition="LONGTEXT", length=20000)
    private String infoEn;

    @Size(max = 7000, message = "Maximale Textgröße 4000 Zeichen")
    @Column(columnDefinition="LONGTEXT", length=20000)
    private String infoDe;

    @Size(max = 7000, message = "Maksymalny rozmiar tekstu 4000 znaków")
    @Column(columnDefinition="LONGTEXT", length=20000)
    private String infoPl;

    @Size(max = 7000, message = "Максимальный размер текста 4000 символов")
    @Column(columnDefinition="LONGTEXT", length=20000)
    private String infoRu;


    @Size(min = 1, message = "Выберите страну")
    private String country;

    @Size(min = 1, message = "Выберите тип")
    private String type;

    @Size(min = 1, message = "Выберите категорию")
    private String category;



    public Item() {
    }

    //work
    public Item(int price, User user, long date) {
        this.price = price;
        this.user = user;
        this.date = date;
    }

    public Item(String title, int price, String info) {
        this.price = price;
        this.title = title;
        this.info = ReversoContext.reversoContext(info);
    }

    //конструктор для полей - для фильтрации
    public Item(String title, int price, String info, String country, String type, String category) {
        this.price = price;
        this.title = title;
        this.info = ReversoContext.reversoContext(info);
        this.country = country;
        this.type = type;
        this.category = category;


    }



    public long getId() {
        return id;
    }

    public String getTitle() {
        return ReversoContext.backReversoContext(title);
    }

    public void setTitle(String title) {
        this.title = ReversoContext.reversoContext(title);
    }

    public String getInfo() {
        return ReversoContext.backReversoContext(info);

    }

    public void setInfo(String info) {
        this.info = ReversoContext.reversoContext(info);

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }


    public String getFirstImageName(){

        ItemController itemController = new ItemController();
        String[] firstImageName = itemController.getArrayImageName(this.fileName);

        return firstImageName[0];

    }

    public String getOriginLanguage() {
        return originLanguage;
    }

    public void setOriginLanguage(String originLanguage) {
        this.originLanguage = originLanguage;
    }

    public String getTitleEn() {
        return ReversoContext.backReversoContext(titleEn);
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = ReversoContext.reversoContext(titleEn);
    }

    public String getTitleDe() {
        return ReversoContext.backReversoContext(titleDe);
    }

    public void setTitleDe(String titleDe) {
        this.titleDe = ReversoContext.reversoContext(titleDe);
    }

    public String getTitlePl() {
        return ReversoContext.backReversoContext(titlePl);
    }

    public void setTitlePl(String titlePl) {
        this.titlePl = ReversoContext.reversoContext(titlePl);
    }

    public String getTitleRu() {
        return ReversoContext.backReversoContext(titleRu);
    }

    public void setTitleRu(String titleRu) {
        this.titleRu = ReversoContext.reversoContext(titleRu);
    }

    public String getInfoEn() {
        return ReversoContext.backReversoContext(infoEn);
    }

    public void setInfoEn(String infoEn) {
        this.infoEn = ReversoContext.reversoContext(infoEn);
    }

    public String getInfoDe() {
        return ReversoContext.backReversoContext(infoDe);
    }

    public void setInfoDe(String infoDe) {
        this.infoDe = ReversoContext.reversoContext(infoDe);
    }

    public String getInfoPl() {
        return ReversoContext.backReversoContext(infoPl);
    }

    public void setInfoPl(String infoPl) {
        this.infoPl = ReversoContext.reversoContext(infoPl);
    }

    public String getInfoRu() {
        return ReversoContext.backReversoContext(infoRu);
    }

    public void setInfoRu(String infoRu) {
        this.infoRu = ReversoContext.reversoContext(infoRu);
    }

    public void setId(long id) {
        this.id = id;
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

    //    public String reversoContext(String str){
//        String revStr = str.replace("<", "&lt;")
//                .replace(">", "&gt;")
//                .replace("'", "&#39;")
//                .replace("\"", "&Prime;")
//                .replace("script", "")
//                .replace("style", "")
//                .replace("button", "");
//        return revStr;
//    }
//
//
//    public String backReversoContext(String str) {
//        String revStr = str.replace("&lt;", "<")
//                .replace("&gt;", ">")
//                .replace("&#39;", "'")
//                .replace("&Prime;", "\"")
//                .replace("&quot;", "\"");
//        return revStr;
//
//    }

}


