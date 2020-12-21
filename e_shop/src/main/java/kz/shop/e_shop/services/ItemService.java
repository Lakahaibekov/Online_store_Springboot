package kz.shop.e_shop.services;

import kz.shop.e_shop.entities.*;

import java.util.List;

public interface ItemService {

    Items addItem(Items item);
    List<Items> getAllItems();
    List<Items> getAllItemsByNameAsc(String name);
    List<Items> getAllItemsByNameDesc(String name);
    List<Items> getAllItemsByNameAndPriceBetweenAsc(String name, double price1, double price2);
    List<Items> getAllItemsByNameAndPriceBetweenDesc(String name, double price1, double price2);

    List<Items> getAllItemsByBrandName(String brand_name);
    List<Items> getAllItemsByNameAndBrandNameAsc(String name, String brand_name);

    Items getItem(Long id);
    void deleteItem(Items item);
    Items saveItem(Items item);

    Countries addCountry(Countries country);
    List<Countries> getAllCountries();
    Countries getCountry(Long id);
    void deleteCountry(Countries country);
    Countries saveCountry(Countries country);

    Brands addBrand(Brands brand);
    List<Brands> getAllBrands();
    Brands getBrand(Long id);
    void deleteBrand(Brands brand);
    Brands saveBrand(Brands brand);

    List<Categories> getAllCategories();
    Categories getCategory(Long id);
    void deleteCategory(Categories category);
    Categories saveCategory(Categories category);
    Categories addCategory(Categories category);

    Pictures addPicture(Pictures picture);
    List<Pictures> getAllPictures();
    Pictures getPicture(Long id);
    void deletePicture(Pictures picture);
    Pictures savePicture(Pictures picture);
    List<Pictures> findByItem(Long Item_id);

    List<Purchases> getAllPurchases();
    Purchases getPurchase(Long id);
    Purchases addPurchase(Purchases purchase);
    Purchases savePurchase(Purchases purchase);
    void deletePurchases(Purchases purchase);

    List<Comments> getAllComments();
    Comments getComment(Long id);
    Comments addComment(Comments comment);
    Comments saveComment(Comments comment);
    void deleteComment(Comments comment);
    List<Comments> findCommentsByItem(Long item_id);
}
