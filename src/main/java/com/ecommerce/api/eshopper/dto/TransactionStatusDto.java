package com.ecommerce.api.eshopper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionStatusDto implements Serializable {

    private String status;
    private String message;
    private String data;

}
