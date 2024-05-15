package lk.ijse.citroessentional.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Item {

    private String id;
    private String name;
    private String price;
    private String qty;

}
