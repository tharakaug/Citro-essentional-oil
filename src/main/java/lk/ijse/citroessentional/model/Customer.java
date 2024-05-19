package lk.ijse.citroessentional.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Customer { // this model class represent real world customer entity
    private String id;
    private String name;
    private String tel;
    private String address;

}
