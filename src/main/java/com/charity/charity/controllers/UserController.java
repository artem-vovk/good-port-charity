package com.charity.charity.controllers;

import com.charity.charity.models.Role;
import com.charity.charity.models.User;
import com.charity.charity.repositirys.ItemRepository;
import com.charity.charity.repositirys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Locale;

@Controller
public class UserController {


    @Value("${ownercode.for-admin-reg}")
    String OWNER_CODE;

    @Autowired
    private ItemRepository itemRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String loginView() {
        return "login";
    }


    @GetMapping("/reg")
    public String regView(Model model, Locale locale) {

        User user = new User();
        model.addAttribute("user", user);

        //get actual local/language
        String localeStr = locale.getLanguage();
        model.addAttribute("actualLocal", localeStr);

        return "reg";
    }


    @PostMapping("/reg")
    public Object addUser(@RequestParam String passwordcopy,
                          Locale locale,
                          @ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult
                          ) {
        String loc = locale.getLanguage();

        if (bindingResult.hasErrors() || !user.getPassword().equals(passwordcopy)
                    || userRepository.findByUsername(user.getUsername()) != null) {
            ModelAndView modelAndView = new ModelAndView("reg");
            modelAndView.addObject("user", user);
            modelAndView.addObject("actualLocal", loc);

            if (!user.getPassword().equals(passwordcopy)) {
                modelAndView.addObject("passworderror", "passworderror");
            }
            if (userRepository.findByUsername(user.getUsername()) != null) {
                modelAndView.addObject("usernameerror", "usernameerror");
            }

            return modelAndView;

        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Collections.singleton(Role.BENEFICIARY));
            user.setEnabled(true);
            userRepository.save(user);
            return "redirect:/login";
        }
    }



    @GetMapping("/reg-admin")
    public String regAdminView(Model model, Locale locale) {

        User user = new User();
        model.addAttribute("user", user);

        //get actual local/language
        String localeStr = locale.getLanguage();
        model.addAttribute("actualLocal", localeStr);

        return "reg-admin";
    }


    @PostMapping("/reg-admin")
    public Object addAdmin(@RequestParam String passwordcopy,
                          @RequestParam String ownercode,
                          Locale locale,
                          @ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult
    ) {

        String loc = locale.getLanguage();

        if (bindingResult.hasErrors() || !user.getPassword().equals(passwordcopy)
                        || userRepository.findByUsername(user.getUsername()) != null
                        || !ownercode.equals(OWNER_CODE)) {

            ModelAndView modelAndView = new ModelAndView("reg-admin");
            modelAndView.addObject("user", user);
            modelAndView.addObject("actualLocal", loc);

            if (!user.getPassword().equals(passwordcopy)) {
                modelAndView.addObject("passworderror", "passworderror");
            }
            if (userRepository.findByUsername(user.getUsername()) != null) {
                modelAndView.addObject("usernameerror", "usernameerror");
            }
            if (!ownercode.equals(OWNER_CODE)) {
                modelAndView.addObject("errorownercode", "errorownercode");
            }

            return modelAndView;

        } else{
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setRole(Collections.singleton(Role.ADMIN));
                user.setEnabled(true);
                userRepository.save(user);
                return "redirect:/login";
            }
        }



//    уже есть новый метод
//    @GetMapping("/user-page")
//    public String userPageView(HttpServletRequest request, Model model, Locale locale){
//        //get authoris user
//        Principal principal = request.getUserPrincipal();
//        String authorisUser = principal.getName();
//
//        List<Item> item = itemRepository.SQLfindByAuthor(authorisUser);
//        model.addAttribute("item", item);
//
//        //get actual local/language
//        String localeStr = locale.getLanguage();
//        model.addAttribute("actualLocal", localeStr);
//
//        User u = userRepository.findByUsername(authorisUser);
//        String authorName = u.getFirstname() + " " + u.getSurname();
//        model.addAttribute("authorName", authorName);
//
//
//        return "user-page";
//    }


}
