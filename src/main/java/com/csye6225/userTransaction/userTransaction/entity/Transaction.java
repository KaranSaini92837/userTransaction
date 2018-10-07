package com.csye6225.userTransaction.userTransaction.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Transaction {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	
	@NotNull
	private String description;
	@NotNull
	private String merchant;
	@NotNull
	private String amount;
	@NotNull
	private String date;
	@NotNull
	private String type;

	public Transaction() {
		super();
	}

	public Transaction(String description, String merchant, String amount, String date, String type) {
		super();
		this.description = description;
		this.merchant = merchant;
		this.amount = amount;
		this.date = date;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", description=" + description + ", merchant=" + merchant + ", amount="
				+ amount + ", date=" + date + ", type=" + type + "]";
	}

}
