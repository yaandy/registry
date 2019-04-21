package com.lv.reg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Deprecated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    private String countryCode;
    private String countryName;
}