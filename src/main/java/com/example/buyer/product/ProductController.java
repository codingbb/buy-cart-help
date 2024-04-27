package com.example.buyer.product;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ProductController {
    private final ProductService productService;

    //TODO: main에 수량은 필요 없는데 자꾸 값이 나온다. dto에서 제외하자


    //장바구니 GET 요청용
    @GetMapping("/cart-form")
    public String cartForm() {

        return "/order/cart-form";
    }


    //장바구니
    @PostMapping("/cart-form")
    public String cartAdd() {

        return "/order/cart-form";
    }


    //상품목록보기
    @GetMapping("/product-list")
    public String productList(HttpServletRequest request) {
        List<ProductResponse.ListDTO> productList = productService.findAllList();
        request.setAttribute("productList", productList);
        System.out.println(productList);
        return "/product/product-list";
    }


    //상품 상세보기
    @GetMapping("/product/{id}")
    public String detail(@PathVariable Integer id, HttpServletRequest request) {
        ProductResponse.DetailDTO product = productService.findByIdDetail(id);
        request.setAttribute("product", product);
        return "/product/detail";
    }


    //메인 페이지
    @GetMapping("/")
    public String main(HttpServletRequest request) {
        List<ProductResponse.MainDTO> productList = productService.findAllMain();
        request.setAttribute("productList", productList);
//        System.out.println(productList);
        return "/index";
    }

    //상품명 실시간 중복체크
//    @GetMapping("/product/name-check")
//    public @ResponseBody String nameSameCheck(String name) {
//        Product product = productService.findByName(name);
//        if (product == null) {
//            return "true"; //상품 등록 가능
//        } else {
//            return "false"; //상품 등록 불가
//        }
//    }

    //상품명 실시간 중복체크 (업데이트용)
//    @GetMapping("/product/name-check/update")
//    public @ResponseBody String nameSameCheckUpdate(String name, Integer id) {
//        Product product = productService.findByNameUpdate(name, id);
//        if (product == null) {
//            return "true"; //상품 등록 가능
//        } else {
//            return "false"; //상품 등록 불가
//        }
//    }

}
