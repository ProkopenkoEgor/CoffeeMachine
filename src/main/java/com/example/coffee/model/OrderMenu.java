package com.example.coffee.model;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "ordermenu")
@Data
@NoArgsConstructor
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 4, max = 25, message = "Должно быть от 4 до 25 символов")
    @Pattern(regexp = "[a-zA-Zа-яА-я\\s]+", message = "Неверный ввод ожидаются только буквы")
    @Column(name = "type_of_order")
    private String typeOfOrder;

    @Column(name = "quantity_of_coffee")
    private Integer quantityOfCoffee;

    @Column(name = "quantity_of_water")
    private Integer quantityOfWater;

    @Column(name = "quantity_of_milk")
    private Integer quantityOfMilk;

    @Column(name = "quantity_of_cream")
    private Integer quantityOfCream;

    @OneToMany(mappedBy = "orderMenu",fetch = FetchType.LAZY)
    private List<Orders> ordersList;
}
