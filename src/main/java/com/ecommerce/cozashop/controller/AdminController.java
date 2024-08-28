package com.ecommerce.cozashop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ecommerce.cozashop.model.Product;
import com.ecommerce.cozashop.model.ProductCategory;
import com.ecommerce.cozashop.model.ProductForm;
import com.ecommerce.cozashop.model.ProductItem;
import com.ecommerce.cozashop.service.ProductCategoryService;
import com.ecommerce.cozashop.service.ProductItemService;

import static java.util.Objects.nonNull;

import java.util.List;

@Controller
public class AdminController {

	@Autowired
	private ProductCategoryService productCatecoryService;
	
	@Autowired
	ProductItemService productItemService;

	@GetMapping("/admin")
	public String showAdmin() {
		return "admin/dashboard";
	}

	@GetMapping("/admin/sales-report")
	public String showSalesReport(Model model) {
		return "admin/sales-report";
	}

	@GetMapping("/admin/category")
	public ModelAndView showCategory(Model model) {
		List<ProductCategory>  listCategory = productCatecoryService.findAllProductCategory();
		model.addAttribute("list_category", listCategory);
		return new ModelAndView("admin/category");
	}

	@GetMapping("/admin/add-category")
	public String showAddCategory(Model model) {
		ProductCategory productCategory = new ProductCategory();
		model.addAttribute("category", productCategory);
		return "admin/addCategory";
	}

	@GetMapping("/admin/product")
	public String showProduct(Model model) {
	
		List<ProductItem> listProductItems = productItemService.getProductItems();
		if(nonNull(listProductItems)) {
			model.addAttribute("listProductItems", listProductItems);	
		}
		return "admin/product";
	}

	@GetMapping("/admin/add-product")
	public String showAddProduct(Model model) {

		List<ProductCategory>  listCategory = productCatecoryService.findAllProductCategory();
		if(nonNull(listCategory)) {
			model.addAttribute("listCategory", listCategory);
		}
		ProductForm productform = new ProductForm();
		model.addAttribute("productform", productform);
		return "admin/addProduct";
	}


	@PostMapping("/admin/create-category")
	public String showAdmin(@ModelAttribute ProductCategory category,
			Model model) {
		if(nonNull(category)) {
			productCatecoryService.addProductCategory(category);	
		}
		return "admin/dashboard";
	}

	
	@GetMapping("/admin/edit-category/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id,Model model) {
		ProductCategory productCategory = productCatecoryService.findCategoryById(id);
		if(nonNull(productCategory)) {
			model.addAttribute("category", productCategory);
		}
		return "admin/addCategory";
	}
	
	
	@GetMapping("/admin/delete-category/{id}")
	public ModelAndView deleteCategory(@PathVariable(name = "id") Integer id,Model model) {
		productCatecoryService.deleteCategory(id);
		List<ProductCategory>  listCategory = productCatecoryService.findAllProductCategory();
		model.addAttribute("list_category", listCategory);
		return new ModelAndView("admin/category");
	}

	@PostMapping("/admin/create-product")
	public String createProduct(@ModelAttribute ProductForm productform ,
			Model model) {
		
		String idCat = productform.getCategory();
		ProductItem productItem = productform.getProductItem();
		if(nonNull(idCat)) {
			int category = Integer.valueOf(idCat);
			ProductCategory prodCategory = productCatecoryService.findCategoryById(category);
			Product product = productItem.getProduct_id();
			product.setProductCategory(prodCategory);
			productItemService.save(productItem);
		}		
		

		return "admin/dashboard";
	}

}
