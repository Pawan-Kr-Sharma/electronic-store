package com.lcwd.electronic.services.impl;

import com.lcwd.electronic.dtos.AddItemToCartRequest;
import com.lcwd.electronic.dtos.CartDto;
import com.lcwd.electronic.entities.Cart;
import com.lcwd.electronic.entities.CartItem;
import com.lcwd.electronic.entities.Product;
import com.lcwd.electronic.entities.User;
import com.lcwd.electronic.exceptions.BadApiRequestException;
import com.lcwd.electronic.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.repositories.CartItemRepository;
import com.lcwd.electronic.repositories.CartRepository;
import com.lcwd.electronic.repositories.ProductRepository;
import com.lcwd.electronic.repositories.UserRepository;
import com.lcwd.electronic.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if (quantity <= 0) {
            throw new BadApiRequestException("requested quantity is not valid !!");
        }

        //fetch the product from db
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found in database!!"));
        //fetch the user from db
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));

        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();    //to check user have a cart ,if cart is present so then save in cart variable

        } catch (NoSuchElementException ex) {
            cart = new Cart();                         //if cart is not present so then create new cart and store in cart variable

            cart.setCartItemId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());         //jaise hi new cart create turant cart  create date aa jayegi
        }
        //perform cart operations

        //if cart items already present ;then update
        AtomicReference<Boolean> updated = new AtomicReference<>(false);

        List<CartItem> items = cart.getItems();             //agar nya cart hai to item 0 milega aur purana cart hai to item ki list mil jayega

        items = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {

                //item already present in cart then update
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());

                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        //cart.setItems(updatedItem);

        //create items                             (ya ham apne items set krenge is cartItems  mai )
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();

            cart.getItems().add(cartItem); //upar na krke ek hi line mai cart.getItem()
        }
        cart.setUser(user);

        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        //condition

        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("cartItem not found!!"));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart of given user not found"));
        //clear items
        cart.getItems().clear();

        Cart blankCart = cartRepository.save(cart);



//after clear the  items now remove cart
//        System.out.println("BlankCart "+blankCart.getItems());
//        String cartItemId = blankCart.getCartItemId();
//        Cart cart1 = cartRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with given id"));
//          System.out.println("2... cart1"+cart1.getCartItemId());
//           cartRepository.delete(cart1);


//        System.out.println("blank cart :"+ blankCart.getItems());
//        System.out.println("Cart cartItemId"+blankCart.getCartItemId());
//        if (blankCart == null){
//
//            System.out.println("If blank cart :"+ blankCart.getItems());
//            //String cartItemId = cart.getCartItemId();
//            //System.out.println("1..... cartItemId :"+ cartItemId);
//            Cart cart1 = cartRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with given id"));
//            System.out.println("2... cart1"+cart1);
//            cartRepository.delete(cart1);
//        }
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart of given user not found"));
        return mapper.map(cart, CartDto.class);
    }
}
