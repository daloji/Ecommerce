package com.ecommerce.cozashop.controller;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ecommerce.cozashop.model.Banner;
import com.ecommerce.cozashop.model.BannerForm;
import com.ecommerce.cozashop.model.DeliveryStatus;
import com.ecommerce.cozashop.model.ImageProduct;
import com.ecommerce.cozashop.model.OrderLine;
import com.ecommerce.cozashop.model.PaymentStatus;
import com.ecommerce.cozashop.model.Product;
import com.ecommerce.cozashop.model.ProductCategory;
import com.ecommerce.cozashop.model.ProductForm;
import com.ecommerce.cozashop.model.ProductItem;
import com.ecommerce.cozashop.model.Role;
import com.ecommerce.cozashop.model.ShopOrder;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.service.BannerService;
import com.ecommerce.cozashop.service.FileStorageService;
import com.ecommerce.cozashop.service.OrderLineService;
import com.ecommerce.cozashop.service.ProductCategoryService;
import com.ecommerce.cozashop.service.ProductItemService;
import com.ecommerce.cozashop.service.RoleService;
import com.ecommerce.cozashop.service.ShopOrderService;
import com.ecommerce.cozashop.service.UserService;

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

	@Autowired
	ShopOrderService shopOrderService;

	@Autowired
	UserService userService;

	@Autowired
	OrderLineService orderLineService;


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


	@GetMapping("/admin/users")
	public String showUsers(Model model) {
		List<User> listUser = userService.findAllUser();
		if(nonNull(listUser)) {
			model.addAttribute("listUser", listUser);
		}

		return "admin/users";
	}


	@GetMapping("/admin/dashboard")
	public String showDashboard(Model model) {	
		LocalDate now = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDayOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());
		List<ShopOrder> listShopOrder = shopOrderService.findAllShopOrderBetweenDates(now, lastDayOfMonth);
		if(nonNull(listShopOrder)) {
			double totalOrderMonthly = 0;
			int totalDeliver = 0;
			int todayOrder = 0;
			for(ShopOrder shopOrder:listShopOrder) {
				if(shopOrder.getStatus() == PaymentStatus.ACCEPT) {
					totalOrderMonthly = totalOrderMonthly + shopOrder.getOrder_total();
				}
				if(now.isEqual(shopOrder.getOrder_date()) && shopOrder.getStatus() == PaymentStatus.ACCEPT){
					todayOrder ++;
				}
				List<OrderLine> listShopDelivery = shopOrder.getOrderLines();
				for(OrderLine orderLine:listShopDelivery) {
					if(orderLine.getStatus() == DeliveryStatus.DELIVERED ) {
						totalDeliver = totalDeliver + 1;	
					}
				}
	
			}
			int[] order = shopOrderService.getDistributionOrder();
			String[] stringArray = new String[order.length]; 
			for (int i = 0; i < order.length; i++) { 
			    stringArray[i] = Integer.toString(order[i]); 
			} 
			model.addAttribute("totalOrderMonthly", totalOrderMonthly + " €");
			model.addAttribute("nbOrderMonthly", listShopOrder.size());
			model.addAttribute("nbDeliverMonthly", totalDeliver);
			model.addAttribute("nbOrderDaily", todayOrder);
			model.addAttribute("order", stringArray);
		}

		return "admin/dashboard";
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


	@GetMapping("/admin/delete-product/{id}")
	public String deleteProduct(@PathVariable(name = "id") Long id,Model model) {

		productItemService.deleteItemProduct(id);
		List<ProductItem> listProductItems = productItemService.getProductItems();
		if(nonNull(listProductItems)) {
			model.addAttribute("listProductItems", listProductItems);	
		}
		return "admin/product";
	}

	@GetMapping("/admin/edit-product/{id}")
	public String editProduct(@PathVariable(name = "id") Long id,Model model) {
		ProductItem productItem = productItemService.findProductItemById(id);
		if(nonNull(productItem)) {
			ProductForm productform = new ProductForm();
			productform.setProductItem(productItem);
			Product product = productItem.getProduct_id();
			ProductCategory productCategory = product.getProductCategory();
			if(nonNull(productCategory)) {
				List<ProductCategory> listCategory = new ArrayList<ProductCategory>();
				listCategory.add(productCategory);
				model.addAttribute("listCategory", listCategory);
			}
			model.addAttribute("productform", productform);
		}
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



	@GetMapping("/admin/order")
	public String getOrder(Model model) {
		List<ShopOrder>	 listShopOrder = shopOrderService.findAllShopOrder();
		if(nonNull(listShopOrder)) {
			model.addAttribute("listShopOrder", listShopOrder); 
		}
		return "admin/order";
	}

	@GetMapping("/admin/order-line/{id}")
	public String showOrderLine(@PathVariable(name = "id") Long id,Model model) {
		List<OrderLine> listOrderLine = orderLineService.findAllOrderLineByShopId(id);
		if(nonNull(listOrderLine)) {
			model.addAttribute("listOrderLine", listOrderLine); 
		}
		return "admin/order-line";
	}
	
	
	
	@GetMapping("/admin/order-to-deliver")
	public String showOrderTodeliver(Model model) {
		Set<ShopOrder> listshop = new HashSet<ShopOrder>();
		List<ShopOrder> listShopOrder = shopOrderService.findAllShopOrder();
		if(nonNull(listShopOrder)) {
			for(ShopOrder shopOrder:listShopOrder) {
				List<OrderLine> listOrdeline = shopOrder.getOrderLines();
				if(nonNull(listOrdeline)) {
					for(OrderLine orderline:listOrdeline) {
						if(orderline.getStatus() != DeliveryStatus.DELIVERED ) {
							listshop.add(shopOrder);
						}
					}
				}
			}
			List<ShopOrder> listshopConfig = new ArrayList<>(listshop);
			model.addAttribute("listShopOrder",new ArrayList<>(listshopConfig)); 
		}
		return "admin/order-to-deliver";
	}
	
	


	@GetMapping("/admin/order/action-update")
	public String updateOrderLine(@RequestParam("action") String action,@RequestParam("orderId") Long id,Model model) {
		OrderLine orderLine = orderLineService.findOrdeLineBydId(id);
		if(nonNull(orderLine)) {
			orderLine.setStatus(DeliveryStatus.valueOf(action));
			orderLineService.save(orderLine);
			List<OrderLine> listOrderLine = orderLineService.findAllOrderLineByShopId(id);
			if(nonNull(listOrderLine)) {
				model.addAttribute("listOrderLine", listOrderLine); 
			}
		}
		return "admin/order-line";
	}


}
