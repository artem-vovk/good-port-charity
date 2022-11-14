package com.charity.charity.controllers;

import com.charity.charity.models.Item;
import com.charity.charity.models.User;
import com.charity.charity.repositirys.ItemRepository;
import com.charity.charity.repositirys.UserRepository;
import com.charity.charity.services.AWSS3Service;
import com.charity.charity.services.ArrayWithFiltersOptions;
import com.charity.charity.services.TranslateCustomTextService;
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

import javax.validation.*;
import java.security.GeneralSecurityException;
import java.util.*;

import java.io.IOException;


@Controller
public class ItemController implements WebMvcConfigurer {


    //index for flipping through the photo
    int index; //value will put in getMappig-method

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AWSS3Service awsS3Service;

    @Value("${google-translator-key}")
    String GOOGLE_TRANSLATOR_KEY;

    @Value("${aws_s3.bucket_name}")
    String BUCKET_NAME;


    @GetMapping("/item/add-item")
    public String addItem(Model model, Locale locale) throws GeneralSecurityException, IOException {

        //get actual local/language
        String localeStr = locale.getLanguage();
        model.addAttribute("actualLocal", localeStr);

        List<String> optionsCountry = ArrayWithFiltersOptions.optionsCountry();
        model.addAttribute("optionsCountry", optionsCountry);

        Item item = new Item("", 0, "");
        model.addAttribute("item", item);

        return "add-item";
    }


    @PostMapping("/item/add-item")
    public Object formForMakingNewItem(
            @AuthenticationPrincipal User user,
            @ModelAttribute("item") @Valid Item item,
            BindingResult bindingResult,
            Locale locale
                                    ) throws IOException, GeneralSecurityException {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("add-item");
            modelAndView.addObject("item", item);
            String localeStr = locale.getLanguage();
            modelAndView.addObject("actualLocal", locale.getLanguage());
            List<String> optionsCountry = ArrayWithFiltersOptions.optionsCountry();
            modelAndView.addObject("optionsCountry", optionsCountry);

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

            translateAndSetToDB(item, item.getTitle(), item.getInfo());

            itemRepository.save(item);

            long id = item.getId();

            return "redirect:/page-details/" + id + "/update";
        }
    }




    @GetMapping("/page-details/{id}")
    public String showPageDetails(@PathVariable(value = "id") long id, Model model, Locale locale) {

        //put value for index for flipping photo (will use index for flipping photo in another method)
        index = 0;

        //get actual local/language
        String localeStr = locale.getLanguage();
        model.addAttribute("actualLocal", localeStr);

        Item item = itemRepository.findById(id).orElse(new Item());
        model.addAttribute("item", item);

        long dateSec = item.getDate();
        Date date = new Date(dateSec);
        model.addAttribute("date", date);

        //show img-nav if app has more the 1 uploaded photo
        if (!item.getFileName().equals("")) {
            String[] allFilesName = getArrayImageName(item.getFileName());
            if (allFilesName.length > 1) {
                model.addAttribute("showNavImg", "showNavImg");
            }
        }

        return "page-details";

    }


    @GetMapping("/page-details/{id}/update")
    public String showPageDetailsUpdate(@RequestParam(name = "error", defaultValue = "", required = false) String error,
                                        @PathVariable(value = "id") long id,
                                        Model model,
                                        Locale locale) {
        if (error.equals("errorformatfile")) {
            model.addAttribute("errorformatfile", "errorformatfile");
        } else if (error.equals("limitfile")) {
            model.addAttribute("limitfile", "limitfile");
        }

        //get actual local/language
        String localeStr = locale.getLanguage();
        model.addAttribute("actualLocal", localeStr);

        Item item = itemRepository.findById(id).orElse(new Item());
        model.addAttribute("item", item);

        List<String> optionsCountry = ArrayWithFiltersOptions.optionsCountry();
        model.addAttribute("optionsCountry", optionsCountry);

        String allFileNames = item.getFileName();
        String[] arrayImageNames = getArrayImageName(allFileNames);
        model.addAttribute("arrayImageNames", arrayImageNames);

        return "page-update";
    }



    @PostMapping("/page-details/{id}/update")
    public Object formForUpdateItem(
                                    @ModelAttribute("item") @Valid Item item,
                                    BindingResult bindingResult,
                                    @PathVariable(value = "id") long id,
                                    Locale locale
                            ) throws GeneralSecurityException, IOException {

        String loc = locale.getLanguage();

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("page-update");

            modelAndView.addObject("actualLocal", loc);

            //put the same text into all language fields (this is temp-item, use for validation,
            // before translation and putting into DB)
            putTextIntoAllFields(item, loc);

            //put some fields to new item from old item for show modelAndView with errors
            Item oldItem = itemRepository.findById(id).orElse(new Item());
            item.setId(oldItem.getId());
            item.setFileName(oldItem.getFileName());
            item.setUser(oldItem.getUser());

            modelAndView.addObject("item", item);
            modelAndView.addObject("arrayImageNames", getArrayImageName(item.getFileName()));

            List<String> optionsCountry = ArrayWithFiltersOptions.optionsCountry();
            modelAndView.addObject("optionsCountry", optionsCountry);

            return modelAndView;
        } else {

            Item oldItem = itemRepository.findById(id).orElse(new Item());
            oldItem.setPrice(item.getPrice());
            oldItem.setCountry(item.getCountry());
            oldItem.setType(item.getType());
            oldItem.setCategory(item.getCategory());
            if (loc.equals("uk"))
                translateAndSetToDB(oldItem, item.getTitle(), item.getInfo());
            if (loc.equals("en"))
                translateAndSetToDB(oldItem, item.getTitleEn(), item.getInfoEn());
            if (loc.equals("de"))
                translateAndSetToDB(oldItem, item.getTitleDe(), item.getInfoDe());
            if (loc.equals("pl"))
                translateAndSetToDB(oldItem, item.getTitlePl(), item.getInfoPl());
            if (loc.equals("ru"))
                translateAndSetToDB(oldItem, item.getTitleRu(), item.getInfoRu());



            itemRepository.save(oldItem);

            return "redirect:/page-details/" + id + "/update";
        }

    }




    @PostMapping("/page-update/{id}/delete")
    public String deletePage(
            @PathVariable(value = "id") long id,
            @RequestParam String fileNames
            ) {

        Item item = itemRepository.findById(id).orElse(new Item());

        //delete files from aws s3
        if (!item.getFileName().equals("")) {
            String[] arrayFileNames = getArrayImageName(fileNames);
            for (String el : arrayFileNames) {
                awsS3Service.deleteFile(el);
            }
        }

        itemRepository.delete(item);
        return "redirect:/";
    }


    @PostMapping("/page-details/{id}/{image}/{imageNames}/update-delete-photo")
    public String deletePhotoByFilename(@PathVariable(value = "image") String imageName,
                                        @PathVariable(value = "id") long id,
                                        @PathVariable(value = "imageNames") String imageNames
                                        ) {

        String[] arrayImageName = getArrayImageName(imageNames);
        ArrayList<String> arrayListImageNames = new ArrayList<>(Arrays.asList(arrayImageName));

        Item item = itemRepository.findById(id).orElse(new Item());

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



    @PostMapping("/upload/{id}")
    public String formForUploadFile(
            @PathVariable(value = "id") long id,
            //@RequestParam String title
            @RequestParam("file")MultipartFile file
    ) throws IOException {

        if (file.getSize() > 10485760) {
            return "redirect:/page-details/" + id + "/update?error=bigfile#uploadfile";
        } else if (Objects.equals(StringUtils.getFilenameExtension(file.getOriginalFilename()), "jpg")
                    | Objects.equals(StringUtils.getFilenameExtension(file.getOriginalFilename()), "jpeg")
                    | Objects.equals(StringUtils.getFilenameExtension(file.getOriginalFilename()), "png")
                 ) {

            Item item = itemRepository.findById(id).orElse(new Item());

            if (item.getFileName().length() > 500) {
                return "redirect:/page-details/" + id + "/update?error=limitfile#uploadfile";
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

                //for feature
                oldFileName = null;
                newFileName = null;

                return "redirect:/page-details/" + id + "/update#uploadfile";
            }

        } else {
            return "redirect:/page-details/" + id + "/update?error=errorformatfile#uploadfile";
        }
    }


    //service method - swiping photo
    @GetMapping("/page-details/next-img/{image-names}")
    @ResponseBody
    public String nameOfNextImage(@PathVariable(value = "image-names") String imageNames) {
        String[] arrayNameOfImages = getArrayImageName(imageNames);

        if (index < (arrayNameOfImages.length - 1)) {
            index += 1;
        } else {
            index = 0;
        }
        return "<img alt=\"" + arrayNameOfImages[index] + "\" src=\"https://" + BUCKET_NAME + ".s3.eu-central-1.amazonaws.com/" + arrayNameOfImages[index] + "\"/>";
    }


    //service method - swiping photo
    @GetMapping("/page-details/prev-img/{image-names}")
    @ResponseBody
    public String nameOfPrevImage(@PathVariable(value = "image-names") String imageNames) {
        String[] arrayNameOfImages = getArrayImageName(imageNames);

        if (index == 0 ) {
            index = (arrayNameOfImages.length - 1);
        } else {
            index -= 1;
        }
        return "<img alt=\"" + arrayNameOfImages[index] + "\" src=\"https://" + BUCKET_NAME + ".s3.eu-central-1.amazonaws.com/" + arrayNameOfImages[index] + "\"/>";
    }




    //service method
    public String[] getArrayImageName(String allFileNames) {
        if (allFileNames != null) {
            return allFileNames.split("\\,");
        } else {
            return new String[]{};
        }
    }



    //service method - translate text and set to DB
    private void translateAndSetToDB(Item item, String title, String info) throws GeneralSecurityException, IOException {

        String[] languagesArray = new String[] {"uk", "en", "pl", "de", "ru"}; //count languages for translate

        for (String leng : languagesArray) {
            if (leng.equals("uk")) {
                String titleUk = TranslateCustomTextService.translateText(title, leng, GOOGLE_TRANSLATOR_KEY);
                String infoUk = TranslateCustomTextService.translateText(info, leng, GOOGLE_TRANSLATOR_KEY);
                item.setTitle(titleUk); //put translation into DB
                item.setInfo(infoUk); //put translation into DB
            }
            if (leng.equals("en")) {
                String titleEn = TranslateCustomTextService.translateText(title, leng, GOOGLE_TRANSLATOR_KEY);
                String infoEn = TranslateCustomTextService.translateText(info, leng, GOOGLE_TRANSLATOR_KEY);
                item.setTitleEn(titleEn); //put translation into DB
                item.setInfoEn(infoEn);
            }
            if (leng.equals("pl")) {
                String titlePl = TranslateCustomTextService.translateText(title, leng, GOOGLE_TRANSLATOR_KEY);
                String infoPl = TranslateCustomTextService.translateText(info, leng, GOOGLE_TRANSLATOR_KEY);
                item.setTitlePl(titlePl); //put translation into DB
                item.setInfoPl(infoPl);
            }
            if (leng.equals("de")) {
                String titleDe = TranslateCustomTextService.translateText(title, leng, GOOGLE_TRANSLATOR_KEY);
                String infoDe = TranslateCustomTextService.translateText(info, leng, GOOGLE_TRANSLATOR_KEY);
                item.setTitleDe(titleDe); //put translation into DB
                item.setInfoDe(infoDe);
            }
            if (leng.equals("ru")) {
                String titleRu = TranslateCustomTextService.translateText(title, leng, GOOGLE_TRANSLATOR_KEY);
                String infoRu = TranslateCustomTextService.translateText(info, leng, GOOGLE_TRANSLATOR_KEY);
                item.setTitleRu(titleRu); //put translation into DB
                item.setInfoRu(infoRu);
            }
        }


    }


    //service method (without translate and saving into DB)
    //it needs for modelAndView (errors/form validation)
    private void putTextIntoAllFields(Item item, String loc) {

        if (loc.equals("uk")){

            item.setTitleEn(item.getTitle());
            item.setTitleDe(item.getTitle());
            item.setTitlePl(item.getTitle());
            item.setTitleRu(item.getTitle());

            item.setInfoEn(item.getInfo());
            item.setInfoDe(item.getInfo());
            item.setInfoPl(item.getInfo());
            item.setInfoRu(item.getInfo());
        }
        if (loc.equals("en")) {

            item.setTitle(item.getTitleEn());
            item.setTitleDe(item.getTitleEn());
            item.setTitlePl(item.getTitleEn());
            item.setTitleRu(item.getTitleEn());

            item.setInfo(item.getInfoEn());
            item.setInfoDe(item.getInfoEn());
            item.setInfoPl(item.getInfoEn());
            item.setInfoRu(item.getInfoEn());
        }
        if (loc.equals("de")) {

            item.setTitleEn(item.getTitleDe());
            item.setTitle(item.getTitleDe());
            item.setTitlePl(item.getTitleDe());
            item.setTitleRu(item.getTitleDe());

            item.setInfoEn(item.getInfoDe());
            item.setInfo(item.getInfoDe());
            item.setInfoPl(item.getInfoDe());
            item.setInfoRu(item.getInfoDe());
        }
        if (loc.equals("pl")) {

            item.setTitleEn(item.getTitlePl());
            item.setTitleDe(item.getTitlePl());
            item.setTitle(item.getTitlePl());
            item.setTitleRu(item.getTitlePl());

            item.setInfoEn(item.getInfoPl());
            item.setInfoDe(item.getInfoPl());
            item.setInfo(item.getInfoPl());
            item.setInfoRu(item.getInfoPl());
        }
        if (loc.equals("ru")) {

            item.setTitleEn(item.getTitleRu());
            item.setTitleDe(item.getTitleRu());
            item.setTitlePl(item.getTitleRu());
            item.setTitle(item.getTitleRu());

            item.setInfoEn(item.getInfoRu());
            item.setInfoDe(item.getInfoRu());
            item.setInfoPl(item.getInfoRu());
            item.setInfo(item.getInfoRu());
        }

    }

}

