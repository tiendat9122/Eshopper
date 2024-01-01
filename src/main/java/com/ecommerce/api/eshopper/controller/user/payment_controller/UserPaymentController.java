package com.ecommerce.api.eshopper.controller.user.payment_controller;

import com.ecommerce.api.eshopper.config.VnPayConfig;
import com.ecommerce.api.eshopper.dto.OrdersDto;
import com.ecommerce.api.eshopper.dto.PaymentDto;
import com.ecommerce.api.eshopper.dto.TransactionStatusDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class UserPaymentController {

//    @PostMapping("/create_payment")
    public ResponseEntity<?> createPayment(@RequestBody OrdersDto ordersDto) throws UnsupportedEncodingException {

        long amount = (long) (ordersDto.getTotalBill())*100;
        String orderType = "170000";
        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_IpAddr", "0:0:0:0:0:0:0:1");
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setStatus("Ok");
        paymentDto.setMessage("Sucessfully");
        paymentDto.setURL(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(paymentDto);

    }

    // Gan vao nut hoac cai gi do de tra ve cho nguoi dung xem
//    @GetMapping("/payment_info")
    public ResponseEntity<?> transaction(@RequestParam(value = "vnp_Amount") String amount,
                                         @RequestParam(value = "vnp_BackCode") String bankCode,
                                         @RequestParam(value = "vnp_OrderInfo") String orderInfo,
                                         @RequestParam(value = "vnp_ResponseCode") String responseCode) {
        TransactionStatusDto transactionStatusDto = new TransactionStatusDto();
        if (responseCode.equals("00")) {
            transactionStatusDto.setStatus("Ok");
            transactionStatusDto.setMessage("Successfully");
            transactionStatusDto.setData("");
        } else {
            transactionStatusDto.setStatus("No");
            transactionStatusDto.setMessage("Failed");
            transactionStatusDto.setData("");
        }
        return ResponseEntity.status(HttpStatus.OK).body(transactionStatusDto);
    }


}
