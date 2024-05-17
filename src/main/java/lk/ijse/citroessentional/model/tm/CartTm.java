package lk.ijse.citroessentional.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartTm {
    private String id;
    private String date;
    private String itemID;
    private double unitPrice;
    private int qty;
    private double   total;
    private String cusID;
    private String cusName;

}
