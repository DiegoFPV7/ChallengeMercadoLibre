package com.pino.challengemercadolibre.dto;

import java.util.List;

public class ProductoDTO {
    private String title;
    private String price;
    private List<PitureDTO> pictures;
    private String warranty;
    private String permalink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<PitureDTO> getPictures() {
        return pictures;
    }

    public void setPictures(List<PitureDTO> pictures) {
        this.pictures = pictures;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }
}
