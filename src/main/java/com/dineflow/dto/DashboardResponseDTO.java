package com.dineflow.dto;

import lombok.Data;

@Data
public class DashboardResponseDTO {

    private Long totalOrders;

    private Long pendingOrders;

    private Long acceptedOrders;

    private Long deliveredOrders;

    private Double totalRevenue;
}