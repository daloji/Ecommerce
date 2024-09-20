package com.ecommerce.cozashop.controller;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
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

import com.ecommerce.cozashop.model.Address;
import com.ecommerce.cozashop.model.Banner;
import com.ecommerce.cozashop.model.BannerForm;
import com.ecommerce.cozashop.model.Block;
import com.ecommerce.cozashop.model.BlockForm;
import com.ecommerce.cozashop.model.DeliveryStatus;
import com.ecommerce.cozashop.model.ImageProduct;
import com.ecommerce.cozashop.model.Logo;
import com.ecommerce.cozashop.model.LogoForm;
import com.ecommerce.cozashop.model.OrderLine;
import com.ecommerce.cozashop.model.PaymentStatus;
import com.ecommerce.cozashop.model.Product;
import com.ecommerce.cozashop.model.ProductCategory;
import com.ecommerce.cozashop.model.ProductForm;
import com.ecommerce.cozashop.model.ProductItem;
import com.ecommerce.cozashop.model.Role;
import com.ecommerce.cozashop.model.ShopOrder;
import com.ecommerce.cozashop.model.Slider;
import com.ecommerce.cozashop.model.SliderForm;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.service.BannerService;
import com.ecommerce.cozashop.service.BlockService;
import com.ecommerce.cozashop.service.FileStorageService;
import com.ecommerce.cozashop.service.LogoService;
import com.ecommerce.cozashop.service.OrderLineService;
import com.ecommerce.cozashop.service.ProductCategoryService;
import com.ecommerce.cozashop.service.ProductItemService;
import com.ecommerce.cozashop.service.RoleService;
import com.ecommerce.cozashop.service.ShopOrderService;
import com.ecommerce.cozashop.service.SliderService;
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
	
	@Autowired
	LogoService logoService;
	
	@Autowired
	SliderService sliderService;
	
	@Autowired
	BlockService blockService;
	

	@GetMapping("/admin")
	public String showAdmin(Model model) {
		return showDashboard(model);
	}

	@GetMapping("/admin/sales-report")
	public String showSalesReport(Model model) {
		List<ShopOrder> listShoper = shopOrderService.findAllShopOrder();	
		model.addAttribute("listShoper", listShoper);
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

	
	
	@GetMapping("/admin/add-slider")
	public String showAddSlider(Model model) {
		SliderForm sliderForm = new SliderForm();
		model.addAttribute("sliderForm", sliderForm);
		return "admin/addSlider";
	}

	@GetMapping("/admin/product")
	public String showProduct(Model model) {

		List<ProductItem> listProductItems = productItemService.getProductItems();
		if(nonNull(listProductItems)) {
			model.addAttribute("listProductItems", listProductItems);	
		}
		return "admin/product";
	}
	
	
	@GetMapping("/admin/logo")
	public String showLogo(Model model) {
		Logo logo = logoService.getLogo();
		LogoForm logoForm = new LogoForm();
		logoForm.setLogo(logo);
		model.addAttribute("logo", logoForm);	
		return "admin/logo";
	}


	@GetMapping("/admin/add-logo")
	public String showAddLogo(Model model) {
		Logo logo = new Logo();
		LogoForm logoForm = new LogoForm();
		logoForm.setLogo(logo);
		model.addAttribute("logo", logoForm);	
		return "admin/addLogo";
	}

	@GetMapping("/admin/users")
	public String showUsers(Model model) {
		List<User> listUser = userService.findAllUser();
		if(nonNull(listUser)) {
			model.addAttribute("listUser", listUser);
		}

		return "admin/users";
	}

	
	
	@GetMapping("/admin/slider")
	public String showSlider(Model model) {
		Slider slider = sliderService.findAllSlider();
		if(nonNull(slider)) {
			model.addAttribute("slider", slider);
		}

		return "admin/slider";
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
			LocalDate nowdate = LocalDate.now(); 
			for(ShopOrder shopOrder:listShopOrder) {
				if(shopOrder.getStatus() == PaymentStatus.ACCEPT) {
					totalOrderMonthly = totalOrderMonthly + shopOrder.getOrder_total();
				}
				if(nowdate.isEqual(shopOrder.getOrder_date()) && shopOrder.getStatus() == PaymentStatus.ACCEPT){
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
	
	
	@GetMapping("/admin/delete-logo/{id}")
	public String deleteLogo(@PathVariable(name = "id") int id,Model model) {
		logoService.delete(id);
		return "admin/dashboard";
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

	@PostMapping("/admin/create-slider")
	public String showCreateSlider(@ModelAttribute SliderForm sliderForm,
			Model model) {
		if(nonNull(sliderForm.getBannerFile())) {
			Slider slider = sliderForm.getSlider();
			MultipartFile bannerFile = sliderForm.getBannerFile();
			if(nonNull(bannerFile)) {
				String file = fileStorage.saveFile(bannerFile);
				slider.setImageBanner(file);
			}
			
			sliderService.save(slider);
		}
		return "admin/dashboard";
	}
	
	@PostMapping("/admin/create-logo")
	public String createAdmin(@ModelAttribute LogoForm logoform,
			Model model) {
		if(nonNull(logoform.getLogo())) {
			Logo logo = logoform.getLogo();
			MultipartFile bannerFile = logoform.getBannerFile();
			if(nonNull(bannerFile)) {
				String file = fileStorage.saveFile(bannerFile);
				logo.setImageLogo(file);
			}
			
			logoService.addLogo(logo);
		}
		return "admin/dashboard";
	}

	@GetMapping("/admin/edit-user/{id}")
	public String editUser(@PathVariable("id") Integer id,Model model) {

		User user = userService.findById(id);
		if(nonNull(user)) {
			model.addAttribute("user", user);	
		}

		List<Role> listRole = roleService.getRoles();
		model.addAttribute("listRole", listRole);
		return "admin/editUser";
	}
	
	@GetMapping("/admin/edit-logo/{id}")
	public String editLogo(@PathVariable("id") Integer id,Model model) {
	   Logo logo = logoService.getLogo();
	   LogoForm logoform =new LogoForm();
	   logoform.setLogo(logo);
	   model.addAttribute("logo", logoform);
		return "admin/addLogo";
	}

	@PostMapping("/admin/edit-user")
	public String updateUser(@ModelAttribute User user) {
		Long id = user.getId();
		User olduser = userService.findById(id.intValue());
		if(nonNull(user.getEmail())) {
			olduser.setEmail(user.getEmail());
		}
		if(nonNull(user.getFirst_name())) {
			olduser.setFirst_name(user.getFirst_name());
		}
		if(nonNull(user.getLast_name())) {
			olduser.setLast_name(user.getFirst_name());
		}
		if(nonNull(user.getPhone())) {
			olduser.setPhone(user.getPhone());
		}
		if(nonNull(user.getRole())) {
			olduser.setRole(user.getRole());
		}
		if(nonNull(user.getAddress())) {
			Address oldAdresse = olduser.getAddress();
			Address adresse = user.getAddress();
			if(nonNull(adresse.getCity())) {
				oldAdresse.setCity(adresse.getCity());
			}
			if(nonNull(adresse.getComplement())) {
				oldAdresse.setComplement(adresse.getComplement());
			}
			if(nonNull(adresse.getCountry())) {
				oldAdresse.setCountry(adresse.getCountry());
			}
			if(nonNull(adresse.getDistrict())) {
				oldAdresse.setDistrict(adresse.getDistrict());
			}

			if(nonNull(adresse.getRoad())) {
				oldAdresse.setRoad(adresse.getRoad());
			}
			olduser.setAddress(oldAdresse);
		}

		userService.updateAccount(olduser);
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

	@GetMapping("/admin/delete-user/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id) {
		User user = userService.getUserById((long)id);
		userService.deleteUser(user);
		return "redirect:/admin/users";
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
	

	@GetMapping("/admin/edit-slider/{id}")
	public String showEditSlader(@PathVariable(name = "id") Integer id,Model model) {
		
		Slider slider = sliderService.findSliderById(id);
		SliderForm sliderForm = new SliderForm();
		sliderForm.setSlider(slider);
		model.addAttribute("sliderForm", sliderForm);
		return "admin/addSlider";
	}
	

	@GetMapping("/admin/delete-slider/{id}")
	public String deleteSlider(@PathVariable(name = "id") Integer id) {
		 sliderService.delete(id);
		return "redirect:/admin/slider";
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

	@GetMapping("/admin/product-inventory")
	public String productInventory(Model model) {

		return "admin/product-inventory";
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
	
	
	

	@GetMapping("/admin/block")
	public String showBlock(Model model) {
		List<Block> listBock = blockService.findAllBlock();
		model.addAttribute("listBock", listBock); 
		return "admin/block";
	}
	
	@GetMapping("/admin/add-block")
	public String showAddBlock(Model model) {
		Block block = new Block();
		BlockForm blockForm = new BlockForm();
		blockForm.setBlock(block);
		model.addAttribute("block", blockForm); 
		return "admin/addBlock";
	}
	
	@PostMapping("/admin/create-block")
	public String createBlock(BlockForm blocForm ,Model model) {
		Block block = blocForm.getBlock();
		MultipartFile bannerFile = blocForm.getBlockFile();
		if(nonNull(bannerFile)) {
			String file = fileStorage.saveFile(bannerFile);
			block.setImageBanner(file);
		}
		blockService.addBlock(block);
		return "admin/dashboard";
	}
	
	@GetMapping("/admin/edit-block/{id}")
	public String editBlock(@PathVariable(name = "id") Integer id,Model model) {
		
		Block block = blockService.findBlockById(id);
		BlockForm blockForm = new BlockForm();
		blockForm.setBlock(block);
		model.addAttribute("block", blockForm);
		
		return "admin/addBlock";
	}
	
}
