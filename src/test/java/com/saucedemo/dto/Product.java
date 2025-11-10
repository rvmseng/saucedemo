package com.saucedemo.dto;

public class Product {

	private String name;
	private String description;
	private String imageLink;
	private String price;

	public Product() {

	}

	public Product(String name, String desc, String imageLink, String price) {
		this.name = name;
		this.description = desc;
		this.imageLink = imageLink;
		this.price = price;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
}
