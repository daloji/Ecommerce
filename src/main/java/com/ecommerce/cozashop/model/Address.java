package com.ecommerce.cozashop.model;

public class Address {
    private String address;
    private String district;
    private String city;
    private String country;

    public Address(String address, String district, String city, String country) {
        this.address = address;
        this.district = district;
        this.city = city;
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return address + ", " + district + " District, " + city + " City, " + country;
    }
}
