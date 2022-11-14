package com.charity.charity.controllers;


import com.charity.charity.models.Item;
import com.charity.charity.repositirys.ItemRepository;
import com.charity.charity.services.ArrayWithFiltersOptions;
import org.apache.tomcat.util.digester.DocumentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.IntStream;

@Controller //указал, что это у нас класс контроллер
public class MainController {


    @Autowired
    private ItemRepository itemRepository;


    @GetMapping("/")
    public String showIndexWithPagination(Locale locale,
                Model model,
                @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                @RequestParam (value = "category", required = false, defaultValue = "empty") String category,
                @RequestParam (value = "type", required = false, defaultValue = "empty") String type,
                @RequestParam (value = "country", required = false,  defaultValue = "empty") String country
            ) throws IOException, GeneralSecurityException {


        model.addAttribute("filtercategory", category);
        model.addAttribute("filtertype", type);
        model.addAttribute("filtercountry", country);

        List<String> optionsCountry = ArrayWithFiltersOptions.optionsCountry();
        model.addAttribute("optionsCountry", optionsCountry);



        if (category.equals("empty"))
            category = null;

        if (type.equals("empty"))
            type = null;

        if (country.equals("empty"))
            country = null;



        //get actual local/language
        String localeStr = locale.getLanguage();
        model.addAttribute("actualLocal", localeStr);

        //pagination
        Page<Item> pageItems = itemRepository.findAll(PageRequest.of(page, 9, Sort.Direction.DESC, "date"), category, type, country );
        model.addAttribute("pageItems", pageItems);
        //
        //how many pages
        model.addAttribute("numbers", IntStream.range(0, pageItems.getTotalPages()).toArray());

        return "index-page";
    }




    @PostMapping("/filter")
    public String filter(@RequestParam (name = "category", required = false) String category,
                         @RequestParam (name = "type", required = false) String type,
                         @RequestParam (name = "country", required = false) String country
                         ) throws NoSuchFieldException, IllegalAccessException {



        String link = "/?category=";
        if (!category.equals("empty")) {
            link += category;
        }
        if (!type.equals("empty")) {
            link += "&type=" + type;
        }
        if (!country.equals("empty")) {
            link += "&country=" + country;
        }

        link += "#carts";

//        System.out.println("Метод пост" + country + type +  category + "ЛИНК" + link);

//        	System.setProperty("file.encoding","UTF-8");
//		Field charset = DocumentProperties.Charset.class.getDeclaredField("defaultCharset");
//		charset.setAccessible(true);
//		charset.set(null,null);

        return "redirect:" + link;

    }



    @GetMapping("/about")
    public String about(@RequestParam (name = "userName", required = false, defaultValue = "Значение по умолчанию") String userName,
                        @RequestParam (name = "id", required = false, defaultValue = "Значение по умолчанию") String id,
                        Model model,
                        Locale locale) {

        model.addAttribute("name", userName);
        model.addAttribute("id", id);

        //get actual local/language
        String localeStr = locale.getLanguage();
        model.addAttribute("actualLocal", localeStr);

        return "about";
    }



}
