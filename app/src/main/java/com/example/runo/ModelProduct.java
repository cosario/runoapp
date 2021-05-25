package com.example.runo;

import java.lang.reflect.Constructor;

public class ModelProduct {
    private  String productId,productTitle,productDescription,productCategory,productQuantity,originalPrice,
            profileIcon,discontedPrice,discountedNote,
            discountAvialable,timestamp,uid;

    public ModelProduct(){

    }

    public ModelProduct(String productId, String productTitle, String productDescription, String productCategory,
                        String productQuantity, String originalPrice, String profileIcon,
                        String discontedPrice, String discountedNote, String discountAvialable, String timestamp, String uid) {
        this.productId = productId;
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.productQuantity = productQuantity;
        this.originalPrice = originalPrice;
        this.profileIcon = profileIcon;
        this.discontedPrice = discontedPrice;
        this.discountedNote = discountedNote;
        this.discountAvialable = discountAvialable;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getProfileIcon() {
        return profileIcon;
    }

    public void setProfileIcon(String profileIcon) {
        this.profileIcon = profileIcon;
    }

    public String getDiscontedPrice() {
        return discontedPrice;
    }

    public void setDiscontedPrice(String discontedPrice) {
        this.discontedPrice = discontedPrice;
    }

    public String getDiscountedNote() {
        return discountedNote;
    }

    public void setDiscountedNote(String discountedNote) {
        this.discountedNote = discountedNote;
    }

    public String getDiscountAvialable() {
        return discountAvialable;
    }

    public void setDiscountAvialable(String discountAvialable) {
        this.discountAvialable = discountAvialable;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
