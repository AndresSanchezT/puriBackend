package com.AndresSanchezDev.SISTEMASPURI.entity.DTO;

import java.util.List;

public class PedidoValidacionDTO {
    private List<ItemPedidoDTO> items;

    public PedidoValidacionDTO(List<ItemPedidoDTO> items) {
        this.items = items;
    }

    public List<ItemPedidoDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemPedidoDTO> items) {
        this.items = items;
    }
}