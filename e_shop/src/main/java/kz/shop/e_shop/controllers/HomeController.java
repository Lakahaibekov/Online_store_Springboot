package kz.shop.e_shop.controllers;

import kz.shop.e_shop.entities.*;
import kz.shop.e_shop.services.ItemService;
import kz.shop.e_shop.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.picture.viewPath}")
    private String viewPathPicture;

    @Value("${file.picture.uploadPath}")
    private String uploadPathPicture;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;

    @GetMapping(value = "/")
    public  String index(Model model){

        model.addAttribute("currentUser", getUserData());

        List<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        return "index";
    }

    @GetMapping(value = "/admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public  String admin(Model model){

        model.addAttribute("currentUser", getUserData());

        List<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        return "admin";
    }

    @GetMapping(value = "/brandsAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public  String brandsAdmin(Model model) {

        model.addAttribute("currentUser", getUserData());

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        return "brandsAdmin";
    }

    @GetMapping(value = "/countriesAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public  String countriesAdmin(Model model) {

        model.addAttribute("currentUser", getUserData());

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        return "countriesAdmin";
    }

    @GetMapping(value = "/categoriesAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public  String categoriesAdmin(Model model) {

        model.addAttribute("currentUser", getUserData());

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);

        return "categoriesAdmin";
    }

    @GetMapping(value = "/rolesAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public  String rolesAdmin(Model model) {

        model.addAttribute("currentUser", getUserData());

        List<Roles> roles = userService.getAllRoles();
        model.addAttribute("roles", roles);

        return "rolesAdmin";
    }

    @GetMapping(value = "/usersAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public  String usersAdmin(Model model) {

        model.addAttribute("currentUser", getUserData());

        List<Users> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "usersAdmin";
    }

    @GetMapping(value = "/detailsUserAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String detailsUserAdmin(Model model, @RequestParam(name = "id") Long id){

        model.addAttribute("currentUser", getUserData());

        Users user = userService.getUser(id);
        model.addAttribute("user", user);

        List<Roles> roles = userService.getAllRoles();
        model.addAttribute("roles", roles);

        return "userDetailsAdmin";
    }

    @GetMapping(value = "/registration")
    public  String registration(Model model) {

        model.addAttribute("currentUser", getUserData());

        List<Roles> roles = userService.getAllRoles();
        model.addAttribute("roles", roles);

        model.addAttribute("error", "");

        return "registration";
    }


    @PostMapping(value = "/additem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addItem(@RequestParam(name = "brand_id", defaultValue = "0") Long id,
                          @RequestParam(name = "name", defaultValue = "No Task") String name,
                          @RequestParam(name = "description", defaultValue = "No Desc  ") String description,
                          @RequestParam(name = "price") double price,
                          @RequestParam(name = "stars") int stars,
                          @RequestParam(name = "smallPicURL") String smallPicURL,
                          @RequestParam(name = "largePicURL") String largePicURL,
                          @RequestParam(name = "addedDate") Date addedDate,
                          @RequestParam(name = "inTopPage") boolean inTopPage){
        Brands brand = itemService.getBrand(id);
        if(brand!=null) {

            Items item = new Items();

            item.setName(name);
            item.setDescription(description);
            item.setPrice(price);
            item.setStars(stars);
            item.setSmallPicURL(smallPicURL);
            item.setLargePicURL(largePicURL);
            item.setAddedDate(addedDate);
            item.setInTopPage(inTopPage);
            item.setBrand(brand);

            itemService.addItem(item);
        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/addbrand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addBrand(@RequestParam(name = "country_id", defaultValue = "0") Long id,
                          @RequestParam(name = "name", defaultValue = "No Task") String name){
        Countries country = itemService.getCountry(id);
        if(country!=null) {

            Brands brand = new Brands();

            brand.setName(name);
            brand.setCountry(country);

            itemService.addBrand(brand);
        }
        return "redirect:/brandsAdmin";
    }

    @PostMapping(value = "/addcountry")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCountry(@RequestParam(name = "name", defaultValue = "No Task") String name,
                           @RequestParam(name = "code", defaultValue = "No Task") String code){


        Countries country = new Countries();

        country.setName(name);
        country.setCode(code);

        itemService.addCountry(country);

        return "redirect:/countriesAdmin";
    }

    @PostMapping(value = "/addcategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addcategory(@RequestParam(name = "name", defaultValue = "No Task") String name,
                             @RequestParam(name = "logoURL", defaultValue = "No Task") String logoURL){


        Categories category = new Categories();

        category.setName(name);
        category.setLogoURL(logoURL);

        itemService.addCategory(category);

        return "redirect:/categoriesAdmin";
    }

    @PostMapping(value = "/addrole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addrole(@RequestParam(name = "role", defaultValue = "No Task") String name,
                              @RequestParam(name = "description", defaultValue = "No Task") String description){


        Roles role = new Roles();

        role.setRole(name);
        role.setDescription(description);

        userService.addRole(role);

        return "redirect:/rolesAdmin";
    }

    @PostMapping(value = "/adduser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adduser(@RequestParam(name = "email", defaultValue = "No Task") String email,
                          @RequestParam(name = "full_name", defaultValue = "No Task") String full_name,
                          @RequestParam(name = "password", defaultValue = "No Task") String password){


        Users user = new Users();

        user.setEmail(email);
        user.setFull_name(full_name);
        user.setPassword(passwordEncoder.encode(password));

        userService.addUser(user);

        return "redirect:/usersAdmin";
    }



    @GetMapping(value = "/details/{idshka}")
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public String details(Model model, @PathVariable(name = "idshka") Long id){

        model.addAttribute("currentUser", getUserData());

        Items item = itemService.getItem(id);
        model.addAttribute("item", item);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Pictures> pictures = itemService.findByItem(id);
        model.addAttribute("pictures",pictures);

        List<Comments> comments = itemService.findCommentsByItem(id);
        model.addAttribute("comments", comments);

        return "details";
    }

    @GetMapping(value = "/detailsitem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String detailsitem(Model model, @RequestParam(name = "id") Long id){

        model.addAttribute("currentUser", getUserData());

        Items item = itemService.getItem(id);
        model.addAttribute("item", item);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);

        List<Pictures> pictures = itemService.getAllPictures();
        model.addAttribute("pictures", pictures);

        return "itemDetailsAdmin";
    }

    @GetMapping(value = "/search")
    public String searchByName(Model model, @RequestParam(name = "name", defaultValue = "No Item") String name){

        model.addAttribute("currentUser", getUserData());

        List<Items> items = itemService.getAllItemsByNameAsc(name);
        model.addAttribute("items", items);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        return "search";
    }

    @GetMapping(value = "/searchDetails")
    public String searchDetails(Model model, @RequestParam(name = "name", defaultValue = "No Item") String name,
                                @RequestParam(name = "brand_name", defaultValue = "No Item") String brand_name,
                                @RequestParam(name = "item_pricefrom", defaultValue = "0") double item_pricefrom,
                                @RequestParam(name = "item_priceto", defaultValue = "0") double item_priceto,
                                @RequestParam(name = "options", defaultValue = "ascending") String options){

        model.addAttribute("currentUser", getUserData());

        List<Items> items = itemService.getAllItemsByNameAsc(name);
        if(name.equals("No Item") && !brand_name.equals("---")){
            items = itemService.getAllItemsByBrandName(brand_name);
        }
        if(!name.equals("No Item") && !brand_name.equals("---")){
            items = itemService.getAllItemsByNameAndBrandNameAsc(name, brand_name);
        }
        if(!name.equals("No Item") && options.equals("descending") && brand_name.equals("---")){
            items = itemService.getAllItemsByNameDesc(name);
        }
        if(!name.equals("No Item") && item_pricefrom != 0 && item_priceto != 0 && options.equals("ascending")  && brand_name.equals("---")){
            items = itemService.getAllItemsByNameAndPriceBetweenAsc(name, item_pricefrom, item_priceto);
        }
        if(!name.equals("No Item") && item_pricefrom != 0 && item_priceto != 0 && options.equals("descending")  && brand_name.equals("---")){
            items = itemService.getAllItemsByNameAndPriceBetweenDesc(name, item_pricefrom, item_priceto);
        }
        model.addAttribute("items", items);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        return "search";
    }

    @GetMapping(value = "/searchbybrand")
    public String searchbybrand(Model model, @RequestParam(name = "name", defaultValue = "No name") String name){

        model.addAttribute("currentUser", getUserData());

        List<Items> items = itemService.getAllItemsByBrandName(name);

        model.addAttribute("items", items);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        return "search";
    }

    @PostMapping(value = "/saveitem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String saveitem(@RequestParam(name = "item_id") Long id,
                           @RequestParam(name = "brand_id") Long brand_id,
                           @RequestParam(name = "name", defaultValue = "No Task") String name,
                           @RequestParam(name = "description", defaultValue = "No Desc  ") String description,
                           @RequestParam(name = "price") double price,
                           @RequestParam(name = "stars") int stars,
                           @RequestParam(name = "smallPicURL") String smallPicURL,
                           @RequestParam(name = "largePicURL") String largePicURL,
                           @RequestParam(name = "addedDate") Date addedDate,
                           @RequestParam(name = "inTopPage") boolean inTopPage){

        Items item = itemService.getItem(id);
        if(item!=null){
            Brands brand = itemService.getBrand(brand_id);
            if(brand!=null) {
                item.setName(name);
                item.setDescription(description);
                item.setPrice(price);
                item.setStars(stars);
                item.setSmallPicURL(smallPicURL);
                item.setLargePicURL(largePicURL);
                item.setAddedDate(addedDate);
                item.setInTopPage(inTopPage);
                item.setBrand(brand);

                itemService.saveItem(item);
            }
        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/deleteitem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String deleteitem(@RequestParam(name = "item_id") Long id){
        Items item = itemService.getItem(id);
        if(item!=null){
            itemService.deleteItem(item);
        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/savecountry")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String savecountry(@RequestParam(name = "id") Long id,
                           @RequestParam(name = "name", defaultValue = "No Task") String name,
                           @RequestParam(name = "code", defaultValue = "No Desc  ") String code){

        Countries country = itemService.getCountry(id);
        if(country!=null){
            country.setName(name);
            country.setCode(code);

            itemService.saveCountry(country);
        }
        return "redirect:/countriesAdmin";
    }

    @PostMapping(value = "/deletecountry")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deletecountry(@RequestParam(name = "id") Long id){
        Countries country = itemService.getCountry(id);
        if(country!=null){
            itemService.deleteCountry(country);
        }
        return "redirect:/countriesAdmin";
    }


    @PostMapping(value = "/savebrand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String savebrand(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "country_id", defaultValue = "No Task") Long country_id,
                              @RequestParam(name = "name", defaultValue = "No Task") String name){

        Brands brand = itemService.getBrand(id);
        if(brand!=null) {
            Countries country = itemService.getCountry(country_id);
            if (country != null) {
                brand.setName(name);
                brand.setCountry(country);

                itemService.saveBrand(brand);
            }
        }
        return "redirect:/brandsAdmin";
    }

    @PostMapping(value = "/deletebrand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deletebrand(@RequestParam(name = "id") Long id){
        Brands brand = itemService.getBrand(id);
        if(brand!=null){
            itemService.deleteBrand(brand);
        }
        return "redirect:/brandsAdmin";
    }

    @PostMapping(value = "/savecategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String savecategory(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "name", defaultValue = "No Task") String name,
                              @RequestParam(name = "logoURL", defaultValue = "No Desc  ") String logoURL){

        Categories category = itemService.getCategory(id);
        if(category!=null){
            category.setName(name);
            category.setLogoURL(logoURL);

            itemService.saveCategory(category);
        }
        return "redirect:/categoriesAdmin";
    }

    @PostMapping(value = "/deletecategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deletecategory(@RequestParam(name = "id") Long id){
        Categories category = itemService.getCategory(id);
        if(category!=null){
            itemService.deleteCategory(category);
        }
        return "redirect:/categoriesAdmin";
    }


    @PostMapping(value = "/saverole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saverole(@RequestParam(name = "id") Long id,
                               @RequestParam(name = "role", defaultValue = "No Task") String name,
                               @RequestParam(name = "description", defaultValue = "No Desc  ") String description){

        Roles role = userService.getRole(id);
        if(role!=null){
            role.setRole(name);
            role.setDescription(description);

            userService.saveRole(role);
        }
        return "redirect:/rolesAdmin";
    }

    @PostMapping(value = "/deleterole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleterole(@RequestParam(name = "id") Long id){
        Roles role = userService.getRole(id);
        if(role!=null){
            userService.deleteRole(role);
        }
        return "redirect:/rolesAdmin";
    }

    @PostMapping(value = "/saveuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saveuser(@RequestParam(name = "user_id") Long id,
                           @RequestParam(name = "email", defaultValue = "No Task") String email,
                           @RequestParam(name = "full_name", defaultValue = "No Desc  ") String full_name,
                           @RequestParam(name = "password", defaultValue = "No Desc  ") String password){

        Users user = userService.getUser(id);
        if(user!=null){
            user.setEmail(email);
            user.setFull_name(full_name);
            user.setPassword(passwordEncoder.encode(password));

            userService.saveUser(user);
        }
        return "redirect:/usersAdmin";
    }

    @PostMapping(value = "/deleteuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteuser(@RequestParam(name = "user_id") Long id){
        Users user = userService.getUser(id);
        if(user!=null){
            userService.deleteUser(user);
        }
        return "redirect:/usersAdmin";
    }


    @PostMapping(value = "/assignrole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String assignrole(@RequestParam(name = "user_id") Long user_id,
                                 @RequestParam(name = "role_id") Long role_id){
        Roles role = userService.getRole(role_id);
        if(role!=null){
            Users user = userService.getUser(user_id);
            if(user!=null){
                List<Roles> roles = user.getRoles();
                if(roles==null){
                    roles = new ArrayList<>();
                }
                roles.add(role);

                userService.saveUser(user);

                return "redirect:/detailsUserAdmin?id=" + user_id;
            }
        }
        return "redirect:/";
    }


    @PostMapping(value = "/assigncategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String assigncategory(@RequestParam(name = "item_id") Long item_id,
                                @RequestParam(name = "category_id") Long category_id){
        Categories cat = itemService.getCategory(category_id);
        if(cat!=null){
            Items item = itemService.getItem(item_id);
            if(item!=null){
                List<Categories> categories = item.getCategories();
                if(categories==null){
                    categories = new ArrayList<>();
                }
                categories.add(cat);

                itemService.saveItem(item);

                return "redirect:/detailsitem?id=" + item_id;
            }
        }
        return "redirect:/";
    }


    @PostMapping(value = "/reassignrole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String reassignrole(@RequestParam(name = "user_id") Long user_id,
                                   @RequestParam(name = "role_id") Long role_id){
        Roles role = userService.getRole(role_id);
        if(role!=null){
            Users user = userService.getUser(user_id);
            if(user!=null){
                List<Roles> roles = user.getRoles();
                if(roles==null){
                    roles = new ArrayList<>();
                }
                roles.remove(role);

                userService.saveUser(user);

                return "redirect:/detailsUserAdmin?id=" + user_id;
            }
        }
        return "redirect:/";
    }

    @PostMapping(value = "/reassigncategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String reassigncategory(@RequestParam(name = "item_id") Long item_id,
                                 @RequestParam(name = "category_id") Long category_id){
        Categories cat = itemService.getCategory(category_id);
        if(cat!=null){
            Items item = itemService.getItem(item_id);
            if(item!=null){
                List<Categories> categories = item.getCategories();
                if(categories==null){
                    categories = new ArrayList<>();
                }
                categories.remove(cat);

                itemService.saveItem(item);

                return "redirect:/detailsitem?id=" + item_id;
            }
        }
        return "redirect:/";
    }

    @GetMapping(value = "/403")
    public String accessDenied(Model model){
        model.addAttribute("currentUser", getUserData());
        return "403";
    }

    @GetMapping(value = "/login")
    public String login(Model model){
        model.addAttribute("currentUser", getUserData());
        return "login";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){
        model.addAttribute("currentUser", getUserData());

        model.addAttribute("error", "");
        return "profile";
    }

    @PostMapping(value = "/updateprofile")
    @PreAuthorize("isAuthenticated()")
    public String updateprofile(Model model,@RequestParam(name = "user_id") Long id,
                           @RequestParam(name = "email", defaultValue = "No") String email,
                           @RequestParam(name = "full_name", defaultValue = "No") String full_name){

        Users user = userService.getUser(id);
        if(user!=null){
            user.setEmail(email);
            user.setFull_name(full_name);

            model.addAttribute("error", "Successfully changed profile");

            userService.saveUser(user);
        }else
            model.addAttribute("error", "Incorrect");

        model.addAttribute("currentUser", getUserData());
        return "profile";
    }

    @PostMapping(value = "/updatepassword")
    @PreAuthorize("isAuthenticated()")
    public String updatepassword(Model model, @RequestParam(name = "user_id") Long id,
                           @RequestParam(name = "old_password", defaultValue = "No") String old_password,
                           @RequestParam(name = "new_password", defaultValue = "No") String new_password,
                           @RequestParam(name = "re_new_password", defaultValue = "No") String re_new_password){

        Users user = userService.getUser(id);

        boolean result = passwordEncoder.matches(old_password,user.getPassword());
        if (user != null) {
            if (result) {
                if (new_password.equals(re_new_password)) {
                    model.addAttribute("error", "Successfully changed password");
                    user.setPassword(passwordEncoder.encode(new_password));
                    userService.saveUser(user);
                }
                else {
                    model.addAttribute("error", "Incorrect re-password");
                }
            }else {
                model.addAttribute("error", "Incorrect old password");
            }
        }
        model.addAttribute("currentUser", getUserData());
        return "profile";
    }

    @PostMapping(value = "/registerUser")
    public String registerUser(Model model, @RequestParam(name = "role_id") Long role_id,
                                 @RequestParam(name = "email", defaultValue = "No") String email,
                                 @RequestParam(name = "password", defaultValue = "No") String password,
                                 @RequestParam(name = "re_password", defaultValue = "No") String re_password,
                                 @RequestParam(name = "full_name", defaultValue = "No") String full_name){

        Roles role = userService.getRole(role_id);

        if (role != null) {
            Users user = new Users();
            if (password.equals(re_password)) {
                user.setId(null);
                user.setEmail(email);
                user.setFull_name(full_name);
                user.setPassword(passwordEncoder.encode(password));
                List<Roles> roles = user.getRoles();
                if(roles==null){
                    roles = new ArrayList<>();
                }
                roles.add(role);
                user.setRoles(roles);
                userService.saveUser(user);
            }
            else {
                model.addAttribute("error", "Incorrect re-password");
                List<Roles> roles = userService.getAllRoles();
                model.addAttribute("roles", roles);
                return "registration";
            }
        }
        model.addAttribute("currentUser", getUserData());
        return "redirect:/login";
    }


    @PostMapping(value = "/uploadavatar")
    @PreAuthorize("isAuthenticated()")
    public String uploadavatar(@RequestParam(name = "user_ava") MultipartFile file){

        if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/jpg") || file.getContentType().equals("image/png")) {

            try {

                Users currentUser = getUserData();

                String picName = DigestUtils.sha1Hex("avatar_" + currentUser.getId() + "_!Picture");

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName+".jpg");
                Files.write(path, bytes);


                currentUser.setPictureURL(picName);
                userService.saveUser(currentUser);

                return "redirect:/profile";

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/";
    }

    @GetMapping(value = "/viewphoto/{url}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody byte []viewProfilePhoto(@PathVariable(name = "url") String url) throws IOException {

        String pictureURL = viewPath+defaultPicture;

        if (url!=null&&!url.equals("null")){
            pictureURL = viewPath+url+".jpg";
        }

        InputStream in;
        try {

            ClassPathResource resourse = new ClassPathResource(pictureURL);
            in = resourse.getInputStream();

        }catch (Exception e){

            ClassPathResource resourse = new ClassPathResource(defaultPicture);
            in = resourse.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }



    @PostMapping(value = "/uploadpicture")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String uploadpicture(@RequestParam(name = "item_picture") MultipartFile file,
                                @RequestParam(name = "item_id") Long item_id){

        if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/jpg") || file.getContentType().equals("image/png")) {

            try {

                String picName = DigestUtils.sha1Hex("picture_" + System.currentTimeMillis() + "_!Picture");

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPathPicture +picName+".jpg");
                Files.write(path, bytes);


                Pictures picture = new Pictures();
                Items item = itemService.getItem(item_id);

                Date date = new Date(System.currentTimeMillis());
                picture.setUrl(picName);
                picture.setAddedDate(date);
                picture.setItem(item);
                itemService.addPicture(picture);

                return "redirect:/detailsitem?id="+item_id;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/";
    }

    @GetMapping(value = "/viewPicture", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public @ResponseBody byte [] viewItemPicture(@RequestParam(name = "url") String url) throws IOException {

        String pictureURL = viewPath+defaultPicture;

        if (url!=null&&!url.equals("null")){
            pictureURL = viewPathPicture+url+".jpg";
        }

        InputStream in;
        try {

            ClassPathResource resourse = new ClassPathResource(pictureURL);
            in = resourse.getInputStream();

        }catch (Exception e){

            ClassPathResource resourse = new ClassPathResource(viewPath+defaultPicture);
            in = resourse.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }

    @PostMapping(value = "/deletePicture")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String deletePicture(@RequestParam(name = "pic_id",defaultValue = "0")Long pic_id){
        Pictures picture = itemService.getPicture(pic_id);
        Long item_id = picture.getItem().getId();
        if(picture!=null){
            itemService.deletePicture(picture);
            return "redirect:/detailsitem?id="+item_id;
        }
        return "redirect:/";
    }



    @GetMapping(value = "/busket")
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public String busket(Model model,HttpSession session){

        model.addAttribute("currentUser", getUserData());

        List<Items> items = itemService.getAllItems();
        model.addAttribute("items",items);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands",brands);

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries",countries);


        return "busket";
    }


    @GetMapping(value = "/addToBasket")
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public String addToBasket(HttpSession session, Model model, @RequestParam(name = "item_id")Long id){
        if(session.getAttribute("busket")==null){
            List<Purchases> busket = new ArrayList<>();
            Items item = itemService.getItem(id);
            Purchases p = new Purchases();
            p.setPrice((int) item.getPrice());
            p.setItem(item);
            p.setAmount(1);
            busket.add(p);
            session.setAttribute("busket",busket);
        }else {
            Items item = itemService.getItem(id);
            List<Purchases> busket = (List<Purchases>) session.getAttribute("busket");
            Purchases purchase = null;
            for (Purchases p : busket) {
                if (p.getItem().getId() == id) {
                    purchase = p;
                    break;
                }
            }
            if(purchase==null){
                purchase = new Purchases();
                purchase.setItem(item);
                purchase.setPrice((int) item.getPrice());
                purchase.setAmount(1);
                busket.add(purchase);
            }
            else{
                purchase.setAmount(purchase.getAmount()+1);
                purchase.setPrice((int) (purchase.getPrice()+item.getPrice()));
            }
            session.setAttribute("busket",busket);
        }


        return "redirect:/busket";
    }


    @GetMapping(value = "/deleteFromBasket")
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public String deleteFromBasket(HttpSession session,Model model,@RequestParam(name = "item_id")Long id){

        Items item = itemService.getItem(id);
        List<Purchases> busket = (List<Purchases>) session.getAttribute("busket");
        Purchases purchase = null;
        for (Purchases p : busket) {
            if (p.getItem().getId() == id) {
                purchase = p;
                break;
            }
        }
        if(purchase.getAmount()==1){
            busket.remove(purchase);
        }
        else{
            purchase.setAmount(purchase.getAmount()-1);
            purchase.setPrice((int) (purchase.getPrice()-item.getPrice()));
        }
        session.setAttribute("busket",busket);

        return "redirect:/busket";
    }


    @GetMapping(value = "/clearBasket")
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public String clearBasket(Model model,HttpSession session){
        session.removeAttribute("busket");
        return "redirect:/busket";
    }



    @PostMapping(value = "/checkIn")
    @PreAuthorize("isAnonymous() || isAuthenticated()")
    public String checkIn(Model model,HttpSession session,@RequestParam(name = "fullname")String fullname){
        List<Purchases> busket = (List<Purchases>) session.getAttribute("busket");
        if(busket!=null){
            for (Purchases purchases : busket) {
                purchases.setBuyer(fullname);
                purchases.setDate(new Date(System.currentTimeMillis()));
                itemService.addPurchase(purchases);
            }
            session.removeAttribute("busket");
            model.addAttribute("success","You have successfully purchased a product");
        }
        else{
            model.addAttribute("error","You didn't select any products");
        }

        model.addAttribute("currentUser", getUserData());

        List<Items> items = itemService.getAllItems();
        model.addAttribute("items",items);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands",brands);

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries",countries);


        return "busket";
    }



    @GetMapping(value = "/soldItemsAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String soldItemsAdmin(Model model){

        model.addAttribute("currentUser", getUserData());

        List<Items> items = itemService.getAllItems();
        model.addAttribute("items",items);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands",brands);

        List<Purchases> purchases = itemService.getAllPurchases();
        model.addAttribute("purchases",purchases);


        return "soldItemsAdmin";
    }


    @PostMapping(value = "/addComment")
    @PreAuthorize("isAuthenticated()")
    public String addComment(@RequestParam(name = "item_id") Long item_id,
                             @RequestParam(name = "user_id") Long user_id,
                             @RequestParam(name = "comment") String text){

        Items item = itemService.getItem(item_id);
        Users user = userService.getUser(user_id);
        Comments comment = new Comments();
        comment.setComment(text);
        comment.setAddedDate(new Date(System.currentTimeMillis()));
        comment.setItem(item);
        comment.setAuthor(user);

        itemService.addComment(comment);

        return "redirect:/details/"+item_id;
    }

    @GetMapping(value = "/deleteComment")
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(@RequestParam(name = "id") Long id){

        Comments comment = itemService.getComment(id);
        Items item = itemService.getItem(comment.getItem().getId());

        itemService.deleteComment(comment);

        return "redirect:/details/"+item.getId();
    }

    @PostMapping(value = "/editComment")
    @PreAuthorize("isAuthenticated()")
    public String editComment(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "comment") String text){

        Comments comment = itemService.getComment(id);
        Items item = itemService.getItem(comment.getItem().getId());
        if(comment!=null){
            comment.setComment(text);
            comment.setAddedDate(new Date(System.currentTimeMillis()));

            itemService.saveComment(comment);
        }
        return "redirect:/details/"+item.getId();
    }


    private Users getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }
}
