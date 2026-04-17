package org.matbylin.core.models.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ProductModel {
    private String name;
    private String description;
    private String price;
}
