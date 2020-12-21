package kz.shop.e_shop.services.impl;

import kz.shop.e_shop.entities.*;
import kz.shop.e_shop.repositories.*;
import kz.shop.e_shop.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PictureReposotory pictureReposotory;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private CommentRepository commentRepository;




    @Override
    public Items addItem(Items item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Items> getAllItems() {
        return itemRepository.findAllByIdIsNotNullOrderByInTopPageDesc();
    }

    @Override
    public List<Items> getAllItemsByNameAsc(String name) {
        return itemRepository.findAllByNameIsStartingWithOrderByPriceAsc(name);
    }

    @Override
    public List<Items> getAllItemsByNameDesc(String name) {
        return itemRepository.findAllByNameIsStartingWithOrderByPriceDesc(name);
    }

    @Override
    public List<Items> getAllItemsByNameAndPriceBetweenAsc(String name, double price1, double price2) {
        return itemRepository.findAllByNameIsStartingWithAndPriceBetweenOrderByPriceAsc(name, price1, price2);
    }

    @Override
    public List<Items> getAllItemsByNameAndPriceBetweenDesc(String name, double price1, double price2) {
        return itemRepository.findAllByNameIsStartingWithAndPriceBetweenOrderByPriceDesc(name, price1, price2);
    }

    @Override
    public List<Items> getAllItemsByBrandName(String brand_name){
        return itemRepository.findAllByBrandNameEquals(brand_name);
    }

    @Override
    public List<Items> getAllItemsByNameAndBrandNameAsc(String name, String brand_name){
        return itemRepository.findAllByNameIsStartingWithAndBrandNameOrderByPriceAsc(name, brand_name);
    }

    @Override
    public Items getItem(Long id) {
        return itemRepository.getOne(id);
    }

    @Override
    public void deleteItem(Items item) {
        itemRepository.delete(item);
    }

    @Override
    public Items saveItem(Items item) {
        return itemRepository.save(item);
    }




    @Override
    public Countries addCountry(Countries country) {
        return countryRepository.save(country);
    }

    @Override
    public List<Countries> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Countries getCountry(Long id) {
        return countryRepository.getOne(id);
    }

    @Override
    public void deleteCountry(Countries country) {
        countryRepository.delete(country);
    }

    @Override
    public Countries saveCountry(Countries country) {
        return countryRepository.save(country);
    }





    @Override
    public Brands addBrand(Brands brand) {
        return brandRepository.save(brand);
    }

    @Override
    public List<Brands> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brands getBrand(Long id) {
        return brandRepository.getOne(id);
    }

    @Override
    public void deleteBrand(Brands brand) {
        brandRepository.delete(brand);
    }

    @Override
    public Brands saveBrand(Brands brand) {
        return brandRepository.save(brand);
    }



    @Override
    public List<Categories> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Categories getCategory(Long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    public void deleteCategory(Categories category) {
        categoryRepository.delete(category);
    }

    @Override
    public Categories saveCategory(Categories category) {
        return categoryRepository.save(category);
    }

    @Override
    public Categories addCategory(Categories category) {
        return categoryRepository.save(category);
    }


    @Override
    public Pictures addPicture(Pictures picture) {
        return pictureReposotory.save(picture);
    }

    @Override
    public List<Pictures> getAllPictures() {
        return pictureReposotory.findAll();
    }

    @Override
    public Pictures getPicture(Long id) {
        return pictureReposotory.getOne(id);
    }

    @Override
    public void deletePicture(Pictures picture) {
        pictureReposotory.delete(picture);
    }

    @Override
    public Pictures savePicture(Pictures picture) {
        return pictureReposotory.save(picture);
    }

    @Override
    public List<Pictures> findByItem(Long Item_id) {
        return pictureReposotory.findAllByItemId(Item_id);
    }




    @Override
    public List<Purchases> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    @Override
    public Purchases getPurchase(Long id) {
        return purchaseRepository.getOne(id);
    }

    @Override
    public Purchases addPurchase(Purchases purchase) {
        return purchaseRepository.save(purchase);
    }

    @Override
    public Purchases savePurchase(Purchases purchase) {
        return purchaseRepository.save(purchase);
    }

    @Override
    public void deletePurchases(Purchases purchase) {
        purchaseRepository.delete(purchase);
    }




    @Override
    public List<Comments> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Comments getComment(Long id) {
        return commentRepository.getOne(id);
    }

    @Override
    public Comments addComment(Comments comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comments saveComment(Comments comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Comments comment) { commentRepository.delete(comment);}

    @Override
    public List<Comments> findCommentsByItem(Long item_id) {
        return commentRepository.findAllByItemId(item_id);
    }
}
