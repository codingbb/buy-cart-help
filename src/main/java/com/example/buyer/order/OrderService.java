package com.example.buyer.order;

import com.example.buyer.cart.CartRepository;
import com.example.buyer.orderItem.OrderItemRepository;
import com.example.buyer.product.Product;
import com.example.buyer.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final CartRepository cartRepo;

    //구매하기 로직
    @Transactional
    public void saveOrder(OrderRequest.SaveDTO requestDTO) {
        System.out.println("으악" + requestDTO);
        //order 저장
        Integer orderId = orderRepo.save(requestDTO);

        //sum 계산
        Integer sum;
        List<Integer> price = requestDTO.getPrice();
        List<Integer> buyQty = requestDTO.getBuyQty();

        List<Integer> sums = new ArrayList<>();

        for (int i = 0; i < requestDTO.getProductId().size(); i++) {
            sum = price.get(i) * buyQty.get(i);
            sums.add(sum);
        }

        requestDTO.setSum(sums);

        //orderItem 저장
        orderItemRepo.save(requestDTO, orderId);

        //수량 반영
        orderRepo.updateQty(requestDTO);

        cartRepo.deleteBySelectId(requestDTO.getCartId());


    }

    //주문 취소하기!!
    @Transactional
    public void orderCancel(List<OrderRequest.CancelDTO> requestDTO) {
        orderRepo.findByIdAndUpdateStatus(requestDTO);
    }


    //내 주문 내역 폼 order-detail-form
    public OrderResponse.DetailDTO orderDetail(Integer orderId) {
        OrderResponse.DetailDTO orderDetail = orderRepo.findUserProductByOrderId(orderId);

//        System.out.println("dto 값 확인용!! " + orderDetail);

        return orderDetail;
    }

    public User findUser(Integer sessionUserId) {
        User user = orderRepo.findByUserId(sessionUserId);
        return user;
    }

    //장바구니에 있는거 주문하는 폼
    public List<OrderResponse.SaveFormDTO> orderCartList(Integer sessionUserId) {
        List<OrderResponse.SaveFormDTO> orderList = orderRepo.findStatusAndUserId(sessionUserId);

        Integer sum;    //물건 각각 합계
//        Integer totalSum = 0;   //토탈 총액
        for (OrderResponse.SaveFormDTO order : orderList) {
            sum = order.getPrice() * order.getBuyQty();
            order.setSum(sum);
        }

        return orderList;

    }


    //내 구매목록 로직 ssar 유저가 구매한 내역만 나와야함
    public List<OrderResponse.ListDTO> orderList(Integer sessionUserId) {

        List<OrderResponse.ListDTO> orderList = orderRepo.findAllOrder(sessionUserId);

        Map<Integer, OrderResponse.ListDTO> orderDistinct =
                orderList.stream().collect(Collectors.toMap(
                        list -> list.getOrderId(),  //orderId가 키값
                        list -> list,           // 값
                        (first, second) -> first    //같은 키를 가진 요소가 있으면 첫번째 값 사용
                ));

        // Map의 values 컬렉션을 List로 변환하여 반환
        List<OrderResponse.ListDTO> distinctOrderList = new ArrayList<>(orderDistinct.values());

        // 화면의 No용
        Integer indexNum = orderDistinct.size();
        for (OrderResponse.ListDTO listNum : orderList) {
            listNum.setIndexNum(indexNum--);
        }

        return distinctOrderList;

    }


//    //주문 취소 로직
//    public List<OrderResponse.ListDTO> orderCancelList(Integer sessionUserId) {
//        List<OrderResponse.ListDTO> orderList = orderRepo.findAllCancelOrder();
//
//        //ssar 유저가 구매한 내역만 나와야함
//        //필터를 써보고싶었어요
//        List<OrderResponse.ListDTO> findUserOrderList = orderList.stream()
//                .filter(list ->
//                        sessionUserId != null && sessionUserId.equals(list.getUserId()))
////                listDTO
//                .map(item -> {
//                    Integer sum = item.getPrice() * item.getBuyQty();
//                    item.setSum(sum);
//                    return item;
//                })
//
//                .map(item -> {
//                    if (item.getStatus().equals(false)) {
//                        item.setNowStatus("취소완료");
//                    }
//                    return item;
//                })
//                .collect(Collectors.toList());
//
////        Integer sum = orderList.stream().mapToInt(value -> value.getPrice() * value.getBuyQty()).sum();
//
//        // 화면의 No용
//        Integer indexNum = findUserOrderList.size();
//        for (OrderResponse.ListDTO listNum : findUserOrderList) {
//            listNum.setIndexNum(indexNum--);
//        }
//
//        return findUserOrderList;
//
//    }

}
