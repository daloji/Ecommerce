package com.ecommerce.cozashop.controller;

import static java.util.Objects.nonNull;
import com.ecommerce.cozashop.model.CartItem;
import com.ecommerce.cozashop.model.ProductItem;
import com.ecommerce.cozashop.model.User;
import com.ecommerce.cozashop.service.CartItemService;
import com.ecommerce.cozashop.service.ProductItemService;
import com.ecommerce.cozashop.service.ProductService;
import com.ecommerce.cozashop.service.StripService;
import com.ecommerce.cozashop.service.UserService;
import com.stripe.model.checkout.Session;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductItemService productItemService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private HttpSession session;

    @Autowired
    private UserService userService;
    
    @Autowired
    private StripService stripService;

    @GetMapping("/shopping-cart")
    public ModelAndView  show(Model model) {

        User user = (User) session.getAttribute("user");
        
        if(nonNull(user)) {
        	user.setId(userService.getIdUserByEmail(user.getEmail()));

            List<CartItem> cartList = cartItemService.getAllProductCartWithUser(user.getId());
            double total = cartItemService.getTotal(cartList);

            model.addAttribute("cart_item", cartList);
            model.addAttribute("total", total);
            model.addAttribute("countCart", cartList.size());	
            if (cartList.isEmpty()) {
                return new ModelAndView("shopping-cart-empty");
            }
        }else {
        	  return new ModelAndView("shopping-cart-not-connected");
        }
   
        return new ModelAndView("shopping-cart"); 
    }

    @GetMapping("/add-to-cart/{id}/{qty}")
    @ResponseBody
    public Integer addToCart(@PathVariable(name = "id") Long id,
                            @PathVariable(name = "qty") Integer qty) {

        User user = (User) session.getAttribute("user");

        user.setId(userService.getIdUserByEmail(user.getEmail()));

        List<CartItem> list = cartItemService.getAllProductCartWithUser(user.getId());

        if (cartItemService.checkProductAlreadyExists(id)) {
            CartItem item = cartItemService.getOneCartByProduct(id);

            item.setQty(item.getQty() + qty);
            cartItemService.addToCart(item);

            return list.size();
        }
        ProductItem product = new ProductItem();


        CartItem cartItem = new CartItem();

        product.setId(id);
        cartItem.setQty(qty);
        cartItem.setItem(product);
        cartItem.setUser(user);

        cartItemService.addToCart(cartItem);
        list = cartItemService.getAllProductCartWithUser(user.getId());

        session.removeAttribute("totalCart");
        session.setAttribute("totalCart", list.size());


        return list.size();
    }

    @RequestMapping( value = "/remove-cart-item/{cid}", produces = "application/json")
    @ResponseBody
    public String removeCartItem(@PathVariable(name = "cid") Integer id) {

        CartItem item = cartItemService.getOneCartByIdCart(id);
        cartItemService.removeCartItem(item);

        User user = (User) session.getAttribute("user");

        user.setId(userService.getIdUserByEmail(user.getEmail()));

        List<CartItem> list = cartItemService.getAllProductCartWithUser(user.getId());
        double total = cartItemService.getTotal(list);
        JSONObject data = new JSONObject();


        session.removeAttribute("totalCart");
        session.setAttribute("totalCart", list.size());

        data.put("size", list.size());
        data.put("total", total);

        System.out.println("Remove successfully");

        return data.toString();
    }


    @RequestMapping(value = "/update-qty/{cid}/{qty}", produces = "application/json")
    @ResponseBody
    public String changeQty(@PathVariable(name = "cid") Integer id,
                            @PathVariable(name = "qty") Integer qty) {
        CartItem cartItem = cartItemService.getOneCartByIdCart(id);

        cartItem.setQty(qty);
        cartItemService.addToCart(cartItem);

        User user = (User) session.getAttribute("user");

        user.setId(userService.getIdUserByEmail(user.getEmail()));

        List<CartItem> list = cartItemService.getAllProductCartWithUser(user.getId());
        double subTotal = cartItemService.getTotal(list);

        JSONObject info = new JSONObject();
        info.put("subTotal", subTotal);
        info.put("sumItem", qty * cartItem.getItem().getPrice());

        return info.toString();
    }

}
