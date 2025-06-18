/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ebookwebsite.model;

import java.time.LocalDate;

/**
 *
 * @author ADMIN
 */
public class UserInfor {
    private int id;
    private String phone;
    private LocalDate birthDay;
    private String gender;
    private String address;
    private String introduction;

    public UserInfor() {
    }

    public UserInfor(int id, String phone, LocalDate birthDay, String gender, String address, String introduction) {
        this.id = id;
        this.phone = phone;
        this.birthDay = birthDay;
        this.gender = gender;
        this.address = address;
        this.introduction = introduction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return "UserInfor{" + "id=" + id + ", phone=" + phone + ", birthDay=" + birthDay + ", gender=" + gender + ", address=" + address + ", introduction=" + introduction + '}';
    }
}
