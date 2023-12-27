package pergudangan;

import java.util.HashMap;
import java.util.Map;

public class Gudang {
    private Map<String, Map<String, Produk>> productsInWarehouses;

    public Gudang() {
        this.productsInWarehouses = new HashMap<>();
    }

    public void tambahProduk(Produk produk, String warehouse) {
        // Check if the warehouse exists in the map, if not, create a new map for it
        System.out.println("Adding product to warehouse: " + warehouse);
        productsInWarehouses.computeIfAbsent(warehouse, key -> new HashMap<>()).put(produk.getNama(), produk);
    }


    public Map<String, Produk> getProdukInWarehouse(String warehouse) {
        // Return the map of products associated with the specified warehouse
        System.out.println("Getting products from warehouse: " + warehouse);
        return productsInWarehouses.get(warehouse);
    }

}