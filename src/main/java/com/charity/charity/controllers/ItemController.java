package com.charity.charity.controllers;

import com.charity.charity.models.CustomInfo;
import com.charity.charity.models.Item;
import com.charity.charity.models.User;
import com.charity.charity.repositirys.CustominfoRepository;
import com.charity.charity.repositirys.ItemRepository;
import com.charity.charity.repositirys.UserRepository;
import com.charity.charity.services.AWSS3Service;
import com.charity.charity.services.MakeArrayImagesName;
import com.charity.charity.services.ArrayWithFiltersOptions;
import com.charity.charity.services.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.*;

@Controller
public class ItemController implements WebMvcConfigurer {


    //index for flipping through the photo
    int index; //value will put in getMappig-method

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustominfoRepository custominfoRepository;

    @Autowired
    private AWSS3Service awsS3Service;

    @Value("${google-translator-key}")
    String GOOGLE_TRANSLATOR_KEY;

    @Value("${aws_s3.bucket_name}")
    String BUCKET_NAME;


    @GetMapping("/item/add-item")
    public String addItem(Model model, Locale locale) throws GeneralSecurityException, IOException {

        //get actual local/language
        String actualLanguage = locale.getLanguage(); // віше добавил глобальную переменную
        model.addAttribute("actualLocal", actualLanguage);

        List<String> optionsCountry = ArrayWithFiltersOptions.optionsCountry();
        model.addAttribute("optionsCountry", optionsCountry);

        List<String> optionsCategory = ArrayWithFiltersOptions.optionsCategory();
        model.addAttribute("optionsCategory", optionsCategory);

        Item item = new Item(0, "", "");
        model.addAttribute("item", item);

        return "add-item";
    }


    @PostMapping("/item/add-item")
    public Object addItemPost(
            @AuthenticationPrincipal User user,
            @ModelAttribute("item") @Valid Item item,
            BindingResult bindingResult,
            Locale locale
    ) throws IOException, GeneralSecurityException {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("add-item");
            modelAndView.addObject("item", item);
//            String localeStr = locale.getLanguage();
            modelAndView.addObject("actualLocal", locale.getLanguage());
            List<String> optionsCountry = ArrayWithFiltersOptions.optionsCountry();
            modelAndView.addObject("optionsCountry", optionsCountry);
            List<String> optionsCategory = ArrayWithFiltersOptions.optionsCategory();
            modelAndView.addObject("optionsCategory", optionsCategory);

            return modelAndView;
        } else {

            item.setUser(user);

            //get actual local/language
            String language = locale.getLanguage();
            //save users language - (interface language) in DB
            item.setOriginLanguage(language);
            item.setFileName("");

            Date dateNow = new Date();
            //time in m.sec
            long date = dateNow.getTime();
            item.setDate(date);

            List<CustomInfo> CustomInfos = new ArrayList<>();

            //translate to all languages and add all to CustomInfos
            TranslateService.translatCustomInfoToAllLanguages(
                    item.getOriginLanguage(),
                    item.getOriginTitle(),
                    "Title",
                    CustomInfos,
                    GOOGLE_TRANSLATOR_KEY);
            TranslateService.translatCustomInfoToAllLanguages(
                    item.getOriginLanguage(),
                    item.getOriginInfo(),
                    "Info",
                    CustomInfos,
                    GOOGLE_TRANSLATOR_KEY);

            item.setCustomInfos(CustomInfos);

            itemRepository.save(item);

            long id = item.getId();

            return "redirect:/page-details/" + id + "/update#uploadfile";
        }
    }


    @GetMapping("/page-details/{id}/update")
    public String updateItemGet(@RequestParam(name = "error", defaultValue = "", required = false) String error,
                                @RequestParam(name = "statusupdate", defaultValue = "", required = false) String statusupdate,
                                @PathVariable(value = "id") long id,
                                Model model,
                                Locale locale) {

        String[] errorsName = new String[]{"errorformatfile", "size_of_upload_file"};
        List<String> actualErrorsArray = new ArrayList<>();
        for (String el : errorsName) {
            if (error.equals(el)) {
                actualErrorsArray.add(el);
            }
        }
        model.addAttribute("actualErrorsArray", actualErrorsArray); //send error name to view


        if (statusupdate.equals("true")) {
            model.addAttribute("statusupdate", "statusupdate");
        }

        //get actual local/language
        String localeStr = locale.getLanguage();
        model.addAttribute("actualLocal", localeStr);

        Item item = itemRepository.findById(id).orElse(new Item());
        model.addAttribute("item", item);

        List<String> optionsCountry = ArrayWithFiltersOptions.optionsCountry();
        model.addAttribute("optionsCountry", optionsCountry);

        List<String> optionsCategory = ArrayWithFiltersOptions.optionsCategory();
        model.addAttribute("optionsCategory", optionsCategory);

        String[] arrayImageNames = item.getImageNamesAsArray();
        model.addAttribute("arrayImageNames", arrayImageNames);

        return "page-update";
    }


    @PostMapping("/page-details/{id}/update")
    public Object UpdateItemPost(
            @ModelAttribute("item") @Valid Item item,
            BindingResult bindingResult,
            @PathVariable(value = "id") long id,
            Locale locale
    ) throws GeneralSecurityException, IOException {

        String loc = locale.getLanguage();

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("page-update");

            modelAndView.addObject("actualLocal", loc);


            //put some fields to new item from old item for show modelAndView with errors
            Item oldItem = itemRepository.findById(id).orElse(new Item());
            item.setId(oldItem.getId());
            item.setFileName(oldItem.getFileName());
            item.setUser(oldItem.getUser());

            modelAndView.addObject("item", item);
            modelAndView.addObject("arrayImageNames", item.getImageNamesAsArray());

            List<String> optionsCountry = ArrayWithFiltersOptions.optionsCountry();
            modelAndView.addObject("optionsCountry", optionsCountry);

            List<String> optionsCategory = ArrayWithFiltersOptions.optionsCategory();
            modelAndView.addObject("optionsCategory", optionsCategory);

            return modelAndView;
        } else {

            Item oldItem = itemRepository.findById(id).orElse(new Item());
            oldItem.setPrice(item.getPrice());
            oldItem.setCountry(item.getCountry());
            oldItem.setType(item.getType());
            oldItem.setCategory(item.getCategory());
            oldItem.setOriginTitle(item.getOriginTitle());
            oldItem.setOriginInfo(item.getOriginInfo());

            oldItem.getCustomInfos().clear();

            List<CustomInfo> customInfosNew = new ArrayList<>();

            //translate to all languages and add all to customInfosNew
            TranslateService.translatCustomInfoToAllLanguages(
                    oldItem.getOriginLanguage(),
                    oldItem.getOriginTitle(),
                    "Title",
                    customInfosNew,
                    GOOGLE_TRANSLATOR_KEY);
            TranslateService.translatCustomInfoToAllLanguages(
                    oldItem.getOriginLanguage(),
                    oldItem.getOriginInfo(),
                    "Info",
                    customInfosNew,
                    GOOGLE_TRANSLATOR_KEY);

            //set customInfosNew into DB
//            oldItem.setCustomInfos(customInfosNew); //don`t work
            for(CustomInfo i : customInfosNew) {
                oldItem.addCustomInfo(i);
            }

            itemRepository.save(oldItem);
            return "redirect:/page-details/" + id + "/update?statusupdate=true#";
        }
    }


    @PostMapping("/page-update/{id}/delete")
    public String deletePage(
            @PathVariable(value = "id") long id
//            @RequestParam String fileNames
    ) {

        Item item = itemRepository.findById(id).orElse(new Item());

        //delete files from aws s3
        if (!item.getFileName().equals("")) {
//            String[] arrayFileNames = MakeArrayImagesName.makeArrayImageName(fileNames);
            String[] arrayFileNames = item.getImageNamesAsArray();
            for (String el : arrayFileNames) {
                awsS3Service.deleteFile(el);
            }
        }
        itemRepository.delete(item);
        return "redirect:/";
    }


    @GetMapping("/user-page")
    public String userPageView(HttpServletRequest request, Model model, Locale locale) {
        //get authoris user
        Principal principal = request.getUserPrincipal();
        String authorisUser = principal.getName();

        List<Item> items = itemRepository.SQLfindByAuthor(authorisUser);
        model.addAttribute("items", items);

        //get actual local/language
        String localeStr = locale.getLanguage();
        model.addAttribute("actualLocal", localeStr);

        User u = userRepository.findByUsername(authorisUser);
        String authorName = u.getFirstname() + " " + u.getSurname();
        model.addAttribute("authorName", authorName);

        return "user-page";
    }


    @GetMapping("/page-details/{id}")
    public String showPageDetails(@PathVariable(value = "id") long id, Model model, Locale locale) {

        //put value for index for flipping photo (will use index for flipping photo in another method)
        index = 0;

        //get actual local/language
        String actualLocal = locale.getLanguage();
        model.addAttribute("actualLocal", actualLocal);

        Item item = itemRepository.findById(id).orElse(new Item());

        model.addAttribute("item", item);

        long dateSec = item.getDate();
        Date date = new Date(dateSec);
        model.addAttribute("date", date);

        //show img-nav if app has more the 1 uploaded photo
        if (!item.getFileName().equals("")) {
            String[] allFilesName = item.getImageNamesAsArray();
            if (allFilesName.length > 1) {
                model.addAttribute("showNavImg", "showNavImg");
            }
        }

        return "page-details";

    }


    @PostMapping("/upload/{id}")
    public String formForUploadFile(
            @PathVariable(value = "id") long id,
            //@RequestParam String title
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        if (file.getSize() > 10485760) {
            return "redirect:/page-details/" + id + "/update?error=size_of_upload_file#uploadfile";
        } else if (Objects.equals(StringUtils.getFilenameExtension(file.getOriginalFilename()), "jpg")
                | Objects.equals(StringUtils.getFilenameExtension(file.getOriginalFilename()), "jpeg")
                | Objects.equals(StringUtils.getFilenameExtension(file.getOriginalFilename()), "png")
        ) {

            Item item = itemRepository.findById(id).orElse(new Item());

            if (item.getFileName().length() > 500) {
                return "redirect:/page-details/" + id + "/update?error=size_of_upload_file#uploadfile";
            } else {

                //upload file in AWS s3, return fileName
                String uploadFileName = awsS3Service.uploadFile(file);

                //take old fileName and add + newFileName + "," between
                String oldFileName = item.getFileName();
                String newFileName;
                if (oldFileName != null) {
                    newFileName = oldFileName + uploadFileName + ",";
                } else {
                    newFileName = uploadFileName + ",";
                }

                item.setFileName(newFileName);
                itemRepository.save(item);

                return "redirect:/page-details/" + id + "/update#uploadfile";
            }

        } else {
            return "redirect:/page-details/" + id + "/update?error=errorformatfile#uploadfile";
        }
    }



    @PostMapping("/page-details/{id}/{image}/{imageNames}/update-delete-photo")
    public String deletePhotoByFilename(@PathVariable(value = "image") String imageName,
                                        @PathVariable(value = "id") long id,
                                        @PathVariable(value = "imageNames") String imageNames
    ) {

        Item item = itemRepository.findById(id).orElse(new Item());
//        String[] arrayImageName = MakeArrayImagesName.makeArrayImageName(imageNames);
        ArrayList<String> arrayListImageNames = new ArrayList<>(Arrays.asList(item.getImageNamesAsArray()));

        if(awsS3Service.deleteFile(imageName).equals("successful delete")){

            String newFileNames = "";
            arrayListImageNames.remove(imageName);
            for (String fileName : arrayListImageNames){
                newFileNames += fileName + ",";
            }

            item.setFileName(newFileNames.toString());
            itemRepository.save(item);
        }

        return "redirect:/page-details/" + id + "/update#uploadfile";
    }



    //service method - swiping photo
    @GetMapping("/page-details/next-img/{id}")
    @ResponseBody
    public String nameOfNextImage(@PathVariable(value = "id") Long id) {

        Item item = itemRepository.findById(id).orElse(new Item());
        String[] arrayNameOfImages = item.getImageNamesAsArray();

        if (index < (arrayNameOfImages.length - 1)) {
            index += 1;
        } else {
            index = 0;
        }
        return "<img alt=\"" + arrayNameOfImages[index] + "\" src=\"https://" + BUCKET_NAME + ".s3.eu-central-1.amazonaws.com/" + arrayNameOfImages[index] + "\"/>";
    }


    //service method - swiping photo
    @GetMapping("/page-details/prev-img/{id}")
    @ResponseBody
    public String nameOfPrevImage(@PathVariable(value = "id") Long id) {

        Item item = itemRepository.findById(id).orElse(new Item());
        String[] arrayNameOfImages = item.getImageNamesAsArray();

        if (index == 0 ) {
            index = (arrayNameOfImages.length - 1);
        } else {
            index -= 1;
        }
        return "<img alt=\"" + arrayNameOfImages[index] + "\" src=\"https://" + BUCKET_NAME + ".s3.eu-central-1.amazonaws.com/" + arrayNameOfImages[index] + "\"/>";
    }

}
