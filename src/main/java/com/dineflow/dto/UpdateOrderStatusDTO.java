package com.dineflow.dto;

import com.dineflow.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusDTO {

    private OrderStatus status;
}
