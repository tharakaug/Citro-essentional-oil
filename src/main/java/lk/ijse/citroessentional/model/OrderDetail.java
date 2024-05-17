package lk.ijse.citroessentional.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetail {
    private String id;
    private String itemId;
    private int qty;
}
