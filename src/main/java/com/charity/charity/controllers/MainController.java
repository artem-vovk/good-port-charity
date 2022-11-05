package com.charity.charity.controllers;


import com.charity.charity.models.Item;
import com.charity.charity.repositirys.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.IntStream;

@Controller //указал, что это у нас класс контроллер
public class MainController {


    @Autowired
    private ItemRepository itemRepository;


    @GetMapping("/")
    public String showIndexWithPagination(Locale locale,
                //HttpServletRequest request, // удалить, нужно біло для вітягивания куков при загрузке
                Model model,
                @RequestParam(value = "page", required = false, defaultValue = "0") Integer page
            ) throws IOException, GeneralSecurityException {


        //get actual local/language
        String localeStr = locale.getLanguage();
        model.addAttribute("actualLocal", localeStr);

        //pagination
        Page<Item> pageItems = itemRepository.findAll(PageRequest.of(page, 9, Sort.Direction.DESC, "date"));
        model.addAttribute("pageItems", pageItems);
        //
        //how many pages
        model.addAttribute("numbers", IntStream.range(0, pageItems.getTotalPages()).toArray());

        return "index-page";
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
