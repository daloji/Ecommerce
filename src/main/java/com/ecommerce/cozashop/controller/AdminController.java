package com.ecommerce.cozashop.controller;

import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ecommerce.cozashop.model.Banner;
import com.ecommerce.cozashop.model.BannerForm;
import com.ecommerce.cozashop.model.ImageProduct;
import com.ecommerce.cozashop.model.Product;
import com.ecommerce.cozashop.model.ProductCategory;
import com.ecommerce.cozashop.model.ProductForm;
import com.ecommerce.cozashop.model.ProductItem;
import com.ecommerce.cozashop.model.Role;
import com.ecommerce.cozashop.service.BannerService;
import com.ecommerce.cozashop.service.FileStorageService;
import com.ecommerce.cozashop.service.ProductCategoryService;
import com.ecommerce.cozashop.service.ProductItemService;
import com.ecommerce.cozashop.service.RoleService;

@Controller
public class AdminController {

	@Autowired
	private ProductCategoryService productCatecoryService;
	
	@Autowired
	ProductItemService productItemService;
	
	@Autowired
	FileStorageService fileStorage;
	
	@Autowired
	BannerService bannerService;
	
	@Autowired
	RoleService roleService;

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
		String imagePrincipal = null;
		String idCat = productform.getCategory();
		ProductItem productItem = productform.getProductItem();
		MultipartFile multipart = productform.getFilePrincipal();
		if(nonNull(multipart)) {
			imagePrincipal = fileStorage.saveFile(multipart);
			ImageProduct imageProduct = new ImageProduct();
			imageProduct.setImg_url(imagePrincipal);
			productItem.setProduct_img(imagePrincipal);
		}
		
		List<MultipartFile> listMultipart = productform.getFile();
		if(nonNull(listMultipart)) {
			List<String> listFile = fileStorage.saveFiles(listMultipart);
			List<ImageProduct> listImagprod = new ArrayList<ImageProduct>();
			for(String file:listFile) {
				ImageProduct imageprod = new ImageProduct();
				imageprod.setProductItem(productItem);
				imageprod.setImg_url(file);
				listImagprod.add(imageprod);
			}
			productItem.setImageProducts(listImagprod);
		}
		
		if(nonNull(idCat)) {
			int category = Integer.valueOf(idCat);
			ProductCategory prodCategory = productCatecoryService.findCategoryById(category);
			Product product = productItem.getProduct_id();
			product.setProductCategory(prodCategory);
			product.setProduct_img(imagePrincipal);
			productItemService.save(productItem);
		}		
		

		return "admin/dashboard";
	}
	
	
	@GetMapping("/admin/banner")
	public String showBanner(Model model) {
		List<Banner> listBanner = bannerService.findAllBanner();
		model.addAttribute("listBanner", listBanner);
		return "admin/banner";
	}

	
	@GetMapping("/admin/add-banner")
	public String addBanner(Model model) {
		BannerForm banner = new BannerForm();
		model.addAttribute("banner", banner);
		return "admin/addBanner";
	}
	
	
	@GetMapping("/admin/roles")
	public String showRoles(Model model) {
		List<Role> listRole = roleService.getRoles();
		model.addAttribute("listRole", listRole);
		return "admin/roles";
	}

	@GetMapping("/admin/add-roles")
	public String addRole(Model model) {
		Role role = new Role();
		model.addAttribute("role", role);
		return "admin/addRole";
	}
	
	@PostMapping("/admin/create-role")
	public String createRole(Role role ,Model model) {
		if(isNull(role.getId())) {
			roleService.addRole(role);
		}		
		return "admin/dashboard";
	}
	
	
	
	
	
	@GetMapping("/admin/delete-banner/{id}")
	public String deleteBanner(@PathVariable(name = "id") Integer id,Model model) {
		bannerService.delete(id);
		return "admin/dashboard";
	}
	
	@PostMapping("/admin/create-banner")
	public String createBanner(BannerForm bannerForm ,Model model) {
		Banner banner = bannerForm.getBanner();
		MultipartFile bannerFile = bannerForm.getBannerFile();
		if(nonNull(bannerFile)) {
			String file = fileStorage.saveFile(bannerFile);
			banner.setImageBanner(file);
		}
		bannerService.save(banner);
		
		return "admin/dashboard";
	}
}
