package com.davince.rest; 
//import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
//import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString), Root.class); */

import lombok.Data;

@Data
public class Root{
    public String shortName;
    public String name;
    public double priceChangePercentage;
    public Price price;
    public SellPrice sellPrice;
}
