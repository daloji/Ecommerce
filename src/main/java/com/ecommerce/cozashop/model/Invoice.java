package com.ecommerce.cozashop.model;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name = "invoice")
@Data
@NoArgsConstructor
public class Invoice {

	@Id
	@GeneratedValue
	@NonNull
	private Integer id;

	@NonNull
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	@EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
	@ToString.Exclude
	private User user;

	@NonNull
	private String invoiceId;

	@NonNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate order_date;

	@NonNull
	private String filename;

	@NonNull
	private String path;
}
