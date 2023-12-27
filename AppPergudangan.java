package pergudangan;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;


import java.util.HashMap;
import java.util.Map;
import static javafx.application.Application.launch;

public class AppPergudangan extends Application {

    private Map<String, Gudang> gudangs = new HashMap<>();
    private ObservableList<Produk> daftarProduk = FXCollections.observableArrayList();

    private ComboBox<String> warehouseComboBox;
    private TableView<Produk> daftarProdukTable;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Aplikasi Manajemen Gudang");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        warehouseComboBox = new ComboBox<>();
        warehouseComboBox.getItems().addAll("A", "B", "C");
        warehouseComboBox.setPromptText("Pilih Gudang");

        TextField productNameInput = new TextField();
        productNameInput.setPromptText("Nama Barang");

        TextField stockInput = new TextField();
        stockInput.setPromptText("Jumlah Stok");

        Button addButton = new Button("Tambah Barang");
        addButton.setOnAction(e -> handleAddProduct(productNameInput.getText(), stockInput.getText()));

        grid.add(productNameInput, 0, 0);
        grid.add(stockInput, 0, 1);
        grid.add(warehouseComboBox, 0, 2);
        grid.add(addButton, 0, 3);

        TextField editStockInput = new TextField();
        editStockInput.setPromptText("Edit Jumlah Stok");

        Button increaseButton = new Button("Tambah Stok");
        increaseButton.setOnAction(e -> handleEditProduct(daftarProdukTable.getSelectionModel().getSelectedItem(), editStockInput.getText(), true));

        Button decreaseButton = new Button("Kurangi Stok");
        decreaseButton.setOnAction(e -> handleEditProduct(daftarProdukTable.getSelectionModel().getSelectedItem(), editStockInput.getText(), false));

        VBox editBox = new VBox(4);
        editBox.getChildren().addAll(editStockInput, increaseButton, decreaseButton);
        grid.add(editBox, 5, 5);

        daftarProdukTable = new TableView<>(daftarProduk);
        daftarProdukTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Produk, String> productNameColumn = new TableColumn<>("Nama Barang");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<Produk, Integer> stockColumn = new TableColumn<>("Jumlah Stok");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("jumlahStok"));

        TableColumn<Produk, String> warehouseColumn = new TableColumn<>("Gudang");
        warehouseColumn.setCellValueFactory(new PropertyValueFactory<>("gudang"));

        daftarProdukTable.getColumns().addAll(productNameColumn, stockColumn, warehouseColumn);
        grid.add(daftarProdukTable, 0, 5, 2, 1);

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleAddProduct(String productName, String stock) {
        try {
            int stockValue = Integer.parseInt(stock);
            Produk newProduct = new Produk(productName, stockValue);

            String gudangTerpilih = warehouseComboBox.getValue();

            if (gudangTerpilih != null) {
                newProduct.setGudang(gudangTerpilih);
                gudangs.computeIfAbsent(gudangTerpilih, key -> new Gudang()).tambahProduk(newProduct, gudangTerpilih);
                daftarProduk.add(newProduct); // Tambahkan ke daftar produk untuk ditampilkan di tabel
                showInformationAlert("Barang berhasil ditambahkan ke " + gudangTerpilih);
            } else {
                showWarningAlert("Pilih gudang terlebih dahulu.");
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Nilai stok tidak valid. Harap masukkan angka yang valid.");
        }
    }

    private void handleEditProduct(Produk selectedProduct, String stock, boolean isIncrease) {
        try {
            int stockValue = Integer.parseInt(stock);
            if (selectedProduct != null) {
                if (isIncrease) {
                    selectedProduct.tambahStok(stockValue);
                    showInformationAlert("Jumlah stok barang " + selectedProduct.getNama() + " ditambahkan.");
                } else {
                    selectedProduct.kurangiStok(stockValue);
                    showInformationAlert("Jumlah stok barang " + selectedProduct.getNama() + " dikurangi.");
                }
                daftarProdukTable.refresh(); // Memperbarui tampilan tabel
            } else {
                showWarningAlert("Pilih barang terlebih dahulu.");
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Nilai stok tidak valid. Harap masukkan angka yang valid.");
        }
    }


    private void tampilkanDetailProduk() {
        if (!daftarProduk.isEmpty()) {
            showInformationAlert("Stok di Seluruh Gudang");

            // Menampilkan stok di semua gudang atau seluruh barang yang ditambahkan
            for (Produk produk : daftarProduk) {
                String detailProduk = "Nama Barang: " + produk.getNama() + "\n"
                        + "Jumlah Stok: " + produk.getJumlahStok() + "\n"
                        + "Gudang: " + produk.getGudang();
                System.out.println(detailProduk);
            }
        } else {
            showInformationAlert("Belum ada barang ditambahkan.");
        }
    }


    private void showInformationAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarningAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Kesalahan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
