package com.beermartket.alcohol.user.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.beermartket.alcohol.model.SanPham;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ThanhToanController {
	@GetMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestParam("cartItems") String cartItemsJson) {
        // Logic để xử lý yêu cầu thanh toán và giỏ hàng từ tham số URL

        // Chuyển chuỗi JSON thành đối tượng danh sách giỏ hàng (sử dụng thư viện JSON hoặc ObjectMapper)
       
        List<SanPham> cart = convertCartItemsJsonToList(cartItemsJson);

        // Thực hiện logic thanh toán và xử lý đơn hàng
        // Sau khi thanh toán thành công, trả về một thông báo hoặc mã đơn hàng
        String paymentMessage = "Thanh toán thành công!";
        return ResponseEntity.ok(paymentMessage);
    }

    // Hàm để chuyển chuỗi JSON thành danh sách giỏ hàng (ví dụ sử dụng thư viện ObjectMapper)
    private List<SanPham> convertCartItemsJsonToList(String cartItemsJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(cartItemsJson, new TypeReference<List<SanPham>>(){});
        } catch (IOException e) {
            // Xử lý lỗi nếu cần
        }
        return new ArrayList<>(); // Trả về danh sách trống nếu có lỗi
    }
}
