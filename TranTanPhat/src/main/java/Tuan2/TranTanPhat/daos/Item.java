package Tuan2.TranTanPhat.daos;

import lombok.*;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Item {
    private Long bookId;
    private String bookName;
    private Double price;
    private int quantity;
}
