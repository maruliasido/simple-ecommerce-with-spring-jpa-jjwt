package com.finalbackend.tokoshop.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "t_product")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    
    @Column(name = "name",nullable = false)
    private String name;
    
    @Column(name = "price",nullable = false)
    private Integer price;
    
    @Column(name = "stock",nullable = false)
    private Integer stock;
    
    @Column(name = "description",nullable = false)
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    
    

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    

}