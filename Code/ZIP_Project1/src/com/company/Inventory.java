package com.company;

public class Inventory {
    private String partName;
    private int partNumber;
    private int partQuantity;

    public Inventory() {
        this.partName = "";
        this.partNumber = 0;
        this.partQuantity = 0;
    }

    public Inventory (String partName, int partNumber, int partQuantity) {
        this.partName = partName;
        this.partNumber = partNumber;
        this.partQuantity = partQuantity;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    public int getPartQuantity() {
        return partQuantity;
    }

    public void setPartQuantity(int partQuantity) {
        this.partQuantity = partQuantity;
    }
}
