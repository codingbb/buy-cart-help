package com.example.buyer.order;

import com.example.buyer.product.Product;
import com.example.buyer.product.ProductResponse;
import com.example.buyer.user.User;
import com.example.buyer.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class OrderController {
    private final OrderService orderService;
    private final HttpSession session;

    //주문 취소 로직
    @PostMapping("/order-cancel")
    public String orderCancel(OrderRequest.CancelDTO requestDTO) {
//        System.out.println("주문 취소 DTO : " + requestDTO);
        orderService.orderCancel(requestDTO);
        return "redirect:/buy-list";
    }

    //내가 주문한 상품 확인 폼 //주문한 내역이 나와야함
    @GetMapping("/my-buy-form")
    public String myBuyForm(HttpServletRequest request, @RequestParam Integer orderId) {
        OrderResponse.BuyFormDTO findBuyForm = orderService.findBuyForm(orderId);
        System.out.println("바이폼!! : " + findBuyForm);
        request.setAttribute("buyForm", findBuyForm);
        return "/order/my-buy-form";
    }


    //내 구매목록
    @GetMapping("/buy-list")
    public String buyList(HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        List<OrderResponse.ListDTO> orderList = orderService.findListAll(sessionUser.getId());
//        System.out.println("오더 리스트 여기! : " + orderList);
        request.setAttribute("orderList", orderList);

        return "/order/buy-list";
    }

    // 주문하기 = 구매하기
    @PostMapping("/order")
    public String order(OrderRequest.DTO requestDTO) {
//        System.out.println(requestDTO);
        orderService.saveOrder(requestDTO);

        return "redirect:/buy-list";

    }

    @GetMapping("/order-form")
    public String orderForm(@RequestParam("productId") Integer productId, @RequestParam("buyQty") Integer buyQty, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        //dto 사용해서 한 번에 다 담기
        OrderRequest.ViewDTO dto = orderService.viewForm(sessionUser.getId(), productId, buyQty);
        System.out.println("주문폼 dto 값 확인 : " + dto);
        request.setAttribute("order", dto);

        return "/order/order-form";
    }

    // 주문폼 //Get 요청이겠지?? POST? GET? POST? GET?
    @PostMapping("/order-form")
    public String orderFormPost() {

        return "/order/order-form";
    }


}
