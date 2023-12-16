package View.Dashboard.Features;

import Controller.SceneController;
import Model.LoginModel;
import Model.PantauPemasukanPengeluaran;
import Model.TampilkanSemuaTarget;
import Model.TanamUangModel;
import Utils.AlertHelper;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javafx.stage.Stage;
import javafx.util.Duration;

// class DashboardPage digunakan untuk menampilkan halaman dashboard
public class TanamUangPage {

    private Stage stage; // Property stage sebaiknya dideklarasikan sebagai final
    private SceneController sceneController; // Tambahkan property SceneController\
    private double saldo;
    private int userId;
    private Tooltip tooltip = new Tooltip();
    private static PantauPemasukanPengeluaran model = new PantauPemasukanPengeluaran();
    private static List<String> keteranganBarangList = model.getKeteranganBarangList();
    private static List<Double> nominalBarangList = model.getNominalBarangList();
    private static List<String> tipeBarangList = model.getTipeBarangList();
    private static List<String> tanggalBarangList = model.getTanggalBarangList();

    public DecimalFormat formatRupiah;

    private ComboBox<String> combobox = new ComboBox<String>();
    private Text title = new Text("Pengeluaran");
    private String tipeTanamUang = "pengeluaran";
    private TextField fieldJumlah = new TextField();
    private TextField fieldKeterangan = new TextField();
    private RadioButton radioButtonCash = new RadioButton("Cash");
    private RadioButton radioButtonTransfer = new RadioButton("Transfer");
    private DatePicker datePickerTanggal = new DatePicker();

    public TanamUangPage(Stage stage) {
        this.stage = stage;
        // this.username = getUsername();
        this.sceneController = new SceneController(stage); // Inisialisasi SceneController
        this.saldo = getSaldo();
        LoginModel loginModel = new LoginModel();
        this.userId = loginModel.getUserId();
    }

    // Menampilkan halaman dashboard
    public void start(String[] listKategoriPemasukan, String[] listKategoriPengeluaran) {
        // Membuat side bar
        VBox sideBar = ImageLinkPaneTanam.createImageLinkVBox(this.stage, sceneController);
        // sideBar.setTranslateX(-stage.getWidth() / 2 + 40);
        sideBar.setAlignment(Pos.CENTER_RIGHT);
        VBox.setVgrow(sideBar, Priority.ALWAYS);

        // Membuat teks welcome
        Text welcome = createText("Catat Keuanganmu\n", "-fx-font: 35 'Poppins Regular'; -fx-fill: #FFFFFF;", 0, 0);
        Text name = createText("Dengan Lengkap!", "-fx-font: 35 'Poppins SemiBold'; -fx-fill: #FFFFFF;", 0, 10);

        // StackPane untuk menampung teks
        StackPane textPane = new StackPane(welcome, name);
        textPane.setAlignment(Pos.CENTER_LEFT);
        textPane.setPadding(new Insets(0, 0, 20, 10));

        // Menambahkan gambar
        ImageView contentImageView = new ImageView(new Image("file:src/Assets/View/Dashboard/LogoTanamUang.png"));
        contentImageView.setFitWidth(400);
        contentImageView.setFitHeight(300);

        // StackPane untuk menampung gambar
        StackPane contentImagePane = new StackPane(contentImageView);
        contentImagePane.setAlignment(Pos.CENTER_RIGHT);
        contentImagePane.setPadding(new Insets(0, 0, 0, 0));

        // Konten bagian atas untuk main pane
        HBox kontenAtasPane = new HBox(textPane, contentImagePane);
        kontenAtasPane.setSpacing(100);
        // HBox.setHgrow(textPane, Priority.ALWAYS);
        textPane.setTranslateX(20);

        // ------------------------------------------------------------------------------------------------------------//

        // Membuat vbox atas untuk konten tengah
        // VBox kontenTengahAtas = new VBox(Fitur);
        // kontenTengahAtas.setAlignment(Pos.CENTER_LEFT);
        // kontenTengahAtas.setPadding(new Insets(0, 0, 20, 0));

        // Konten kiri
        ImageView Gambarduit = new ImageView(new Image("file:src/Assets/View/Dashboard/Gambarduit.png"));
        Gambarduit.setFitWidth(220);
        Gambarduit.setFitHeight(43);
        Gambarduit.setPreserveRatio(true);

        VBox vboxKiriAtas = new VBox(Gambarduit);
        vboxKiriAtas.setAlignment(Pos.TOP_LEFT);
        vboxKiriAtas.setPadding(new Insets(0, 0, 0, 20));

        // Membulatkan tampilan uang agar tidak muncul E
        long roundedValue = Math.round(this.saldo);

        Text Saldoawal = createText("Saldo Awal", "-fx-font: 15 'Poppins-Regular'; -fx-fill: #064D00;", 0, 0);
        Text Saldoawal2 = createText(formatDuit(roundedValue), "-fx-font: 30 'Poppins SemiBold'; -fx-fill: #ffffff;", 0,
                -6);
        Saldoawal2.setWrappingWidth(200); // Wrap the Saldoawal2 to a certain width
        Saldoawal2.setVisible(true); // Hide the Saldoawal2 initially

        // Create a timeline for the rolling animation
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10),
                new KeyValue(Saldoawal2.layoutYProperty(), -Saldoawal2.getLayoutBounds().getHeight())));
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Trigger the animation when the Saldoawal2 is hovered
        Saldoawal2.setOnMouseEntered(e -> {
            Saldoawal2.setVisible(true);
            timeline.play();
        });

        // Stop the animation and hide the Saldoawal2 when the mouse is not hovering
        Saldoawal2.setOnMouseExited(e -> {
            timeline.stop();
            Saldoawal2.setVisible(false);
        });
        VBox vboxKiriTengah = new VBox(Saldoawal, Saldoawal2);
        vboxKiriTengah.setAlignment(Pos.CENTER_LEFT);
        vboxKiriTengah.setPadding(new Insets(0, 0, 0, 20));

        ImageView UbahSaldo = new ImageView(new Image("file:src/Assets/View/Dashboard/UbahSaldo.png"));
        UbahSaldo.setFitWidth(220);
        UbahSaldo.setFitHeight(35);
        UbahSaldo.setPreserveRatio(true);

        Hyperlink UbahSaldoHyperlink = new Hyperlink();
        UbahSaldoHyperlink.setGraphic(UbahSaldo);
        UbahSaldoHyperlink.setTranslateY(10);
        UbahSaldoHyperlink.setOnMouseClicked(e -> sceneController.switchToTanamUang());

        VBox vboxKiriBawah = new VBox(UbahSaldoHyperlink);
        vboxKiriBawah.setAlignment(Pos.BOTTOM_CENTER);

        VBox penyatuanKonten = new VBox(vboxKiriAtas, vboxKiriTengah, vboxKiriBawah);
        penyatuanKonten.setPadding(new Insets(20, 0, 0, 0));
        penyatuanKonten.setSpacing(4);
        // Saldoawal2.wrappingWidthProperty().bind(penyatuanKonten.widthProperty());

        StackPane Kontenkiri = new StackPane(penyatuanKonten);
        Kontenkiri.setStyle("-fx-background-color: #39B200; -fx-background-radius: 20px");
        Kontenkiri.setAlignment(Pos.CENTER);

        // Konten Tengah

        Button buttonPemasukan = new Button("Pemasukan");
        Button buttonPengeluaran = new Button("Pengeluaran");
        Rectangle buttonBG = new Rectangle();
        Rectangle buttonActive = new Rectangle();

        buttonPemasukan.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-font: 22 'Poppins SemiBold';" +
            "-fx-text-fill: #FFFFFF"
        );
        buttonPengeluaran.setStyle(
            "-fx-background-color: transparent;" + 
            "-fx-font: 22 'Poppins SemiBold';" + 
            "-fx-text-fill: #FFFFFF"
        );
        this.title = new Text("Pengeluaran");
        this.title.setStyle("-fx-font: 24 Poppins;");
        this.title.setFill(Color.valueOf("#FFFFFF"));

        Label labelTanggal = new Label("Tanggal");
        labelTanggal.setStyle("-fx-font: 20 'Poppins Medium'");
        labelTanggal.setTextFill(Color.valueOf("#FFFFFF"));
        this.datePickerTanggal.getStylesheets().add(getClass().getResource("/Utils/DatePicker.css").toExternalForm());
        this.datePickerTanggal.setPrefWidth(180);

        Label labelKategori = new Label("Kategori");
        labelKategori.setStyle("-fx-font: 20 'Poppins Medium'");
        labelKategori.setTextFill(Color.valueOf("#FFFFFF"));
        this.combobox = new ComboBox(FXCollections.observableArrayList(listKategoriPengeluaran));
        this.combobox.getStylesheets().add(getClass().getResource("/Utils/ComboBoxTanamUang.css").toExternalForm());

        Timeline rectKeKiri = 
            new Timeline(
                new KeyFrame(Duration.millis(600),
                    new KeyValue(buttonActive.translateXProperty(), buttonActive.getTranslateX(), Interpolator.EASE_BOTH)));  

        Timeline rectKeKanan = 
            new Timeline(
                new KeyFrame(Duration.millis(600),
                    new KeyValue(buttonActive.translateXProperty(), 190, Interpolator.EASE_BOTH)));  

        buttonPemasukan.setOnMouseClicked(e -> {
            this.tipeTanamUang = "pemasukan";
            buttonActive.setFill(Color.valueOf("#477619"));
            // StackPane.setAlignment(buttonActive, javafx.geometry.Pos.CENTER_RIGHT);
            rectKeKanan.play();
            this.combobox.setItems(FXCollections.observableArrayList(listKategoriPemasukan));
            TanamUangModel.getKategoriTanamUang(this.tipeTanamUang);
        });

        buttonPengeluaran.setOnMouseClicked(e -> {
            this.tipeTanamUang = "pengeluaran";
            buttonActive.setFill(Color.valueOf("#761E19"));
            // StackPane.setAlignment(buttonActive, javafx.geometry.Pos.CENTER_LEFT);
            rectKeKiri.play();
            this.combobox.setItems(FXCollections.observableArrayList(listKategoriPengeluaran));
            TanamUangModel.getKategoriTanamUang(this.tipeTanamUang);
        });

        StackPane mainPane = new StackPane();

        Hyperlink hyperlinkEdit = new Hyperlink();
        hyperlinkEdit.setGraphic(new ImageView(new Image("file:src/Assets/View/Dashboard/EditTanamUang.png")));

        hyperlinkEdit.setOnMouseClicked(e -> {
            double paneWidth = this.stage.getWidth() - 350;
            double paneHeight = this.stage.getHeight() - 200;

            StackPane backgroundMainPane = new StackPane();
            backgroundMainPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-background-radius: 20");

            StackPane editMainPane = new StackPane();
            editMainPane.setMaxSize(paneWidth - 400, paneHeight - 200);
            editMainPane.setStyle("-fx-background-color: #141F23; -fx-background-radius: 20");

            VBox contentPane = new VBox();
            contentPane.setStyle("-fx-background-radius: 20");
            contentPane.setMaxSize(editMainPane.getMaxWidth() - 80, editMainPane.getMaxHeight() - 20);
            contentPane.setSpacing(20);
            
            HBox topBar = new HBox();
            topBar.setSpacing(10);
            topBar.setAlignment(Pos.CENTER_LEFT);

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setMaxSize(contentPane.getMaxWidth(), contentPane.getMaxHeight() - 100);

            VBox scrollPaneContent = createScrollPaneContent(scrollPane, editMainPane);

            String firstCapitalLetter = this.tipeTanamUang.substring(0, 1).toUpperCase() + this.tipeTanamUang.substring(1);
            Text titleKategoriPengeluaran = createText("Kategori " + firstCapitalLetter, "-fx-font: 20 'Poppins Bold';", "#FFFFFF");
            Hyperlink backHyperlink = new Hyperlink();
            backHyperlink.setGraphic(new ImageView(new Image("file:src/Assets/View/Dashboard/Back.png")));

            backHyperlink.setOnMouseClicked(f -> {
                this.combobox.setItems(FXCollections.observableArrayList(TanamUangModel.getKategoriTanamUang(this.tipeTanamUang)));
                mainPane.getChildren().remove(editMainPane);
                mainPane.getChildren().remove(backgroundMainPane);
            });

            Hyperlink tambahHyperlink = new Hyperlink();
            tambahHyperlink.setGraphic(new ImageView(new Image("file:src/Assets/View/Dashboard/Tambah.png")));
            
            tambahHyperlink.setOnMouseClicked(f -> {
                StackPane tambahPane = new StackPane();
                StackPane backgroundTambahPane = new StackPane();
                tambahPane.setMaxSize(editMainPane.getMaxWidth() - 200, editMainPane.getMaxHeight() - 200);
                tambahPane.setStyle("-fx-background-color: #141F23; -fx-background-radius: 20;");

                backgroundTambahPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");

                Text titleTambah = createText("Tambah Kategori", "-fx-font: 20 'Poppins Bold'", "#FFFFFF");
                Hyperlink tambahBackHyperlink = new Hyperlink();
                tambahBackHyperlink.setGraphic(new ImageView(new Image("file:src/Assets/View/Dashboard/Back.png")));

                tambahBackHyperlink.setOnMouseClicked(g -> {
                    refreshView(scrollPane, scrollPaneContent, mainPane);
                    editMainPane.getChildren().remove(tambahPane);
                    editMainPane.getChildren().remove(backgroundTambahPane);
                });

                HBox hboxTitleTambah = new HBox(tambahBackHyperlink, titleTambah);
                hboxTitleTambah.setSpacing(20);
                
                Label labelTambah = new Label("Nama:");
                labelTambah.setStyle("-fx-font: 14 'Poppins Regular'; -fx-text-fill: #FFFFFF");
                labelTambah.setTextFill(Color.valueOf("#000000"));
                TextField fieldTambah = new TextField();
                Button buttonTambah = new Button("Tambah");
                Button buttonBatal = new Button("Batal");

                buttonBatal.setOnMouseClicked(g -> {
                    editMainPane.getChildren().remove(tambahPane);
                    editMainPane.getChildren().remove(backgroundTambahPane);
                });

                buttonTambah.setOnMouseClicked(g -> {
                    if (TanamUangModel.tambahKategori(fieldTambah.getText(), tipeTanamUang)) {
                        refreshView(scrollPane, scrollPaneContent, editMainPane);

                        editMainPane.getChildren().remove(tambahPane);
                        editMainPane.getChildren().remove(backgroundTambahPane);

                        AlertHelper.info("Kategori " + fieldTambah.getText() + " berhasil ditambahkan!");
                    } else {
                        AlertHelper.alert("Kategori gagal ditambahkan!");
                    }
                });

                HBox hboxFormTambah = new HBox(labelTambah, fieldTambah);
                hboxFormTambah.setSpacing(20);
                hboxFormTambah.setAlignment(Pos.CENTER);
                HBox hboxButtonTambah = new HBox(buttonBatal, buttonTambah);
                hboxButtonTambah.setSpacing(20);
                hboxButtonTambah.setAlignment(Pos.CENTER);

                VBox vboxTambah = new VBox(hboxTitleTambah, hboxFormTambah, hboxButtonTambah);
                vboxTambah.setMaxSize(tambahPane.getMaxWidth() - 50, tambahPane.getMaxHeight() - 80);
                vboxTambah.setSpacing(20);
                
                tambahPane.getChildren().add(vboxTambah);

                editMainPane.getChildren().addAll(backgroundTambahPane, tambahPane);
            });

            scrollPane.setContent(scrollPaneContent);
            String scrollbarStyle = "-fx-background-color: #141F23;";
            scrollbarStyle += "-fx-background-color: #0B1214;";
            scrollbarStyle += "-fx-background-insets: 0;";
            scrollbarStyle += "-fx-padding: 0;";
            scrollbarStyle += "-fx-background-radius: 20px;";

            scrollPane.setStyle(scrollbarStyle);
            scrollPane.getStylesheets().add(getClass().getResource("/Utils/ScrollBar.css").toExternalForm());

            topBar.getChildren().addAll(backHyperlink, titleKategoriPengeluaran, tambahHyperlink);
            contentPane.getChildren().addAll(topBar, scrollPane);
            editMainPane.getChildren().add(contentPane);
            mainPane.getChildren().addAll(backgroundMainPane, editMainPane);
        });

        Label labelJumlah = new Label("Jumlah");
        labelJumlah.setStyle("-fx-font: 20 'Poppins Medium'");
        labelJumlah.setTextFill(Color.valueOf("#FFFFFF"));
        // TextField fieldJumlah = new TextField();
        this.fieldJumlah.setPrefWidth(180);
        this.fieldJumlah.getStylesheets().add(getClass().getResource("/Utils/TextField.css").toExternalForm());

        Label labelTipeTransaksi = new Label("Tipe Transaksi");
        labelTipeTransaksi.setStyle("-fx-font: 20 'Poppins Medium'");
        labelTipeTransaksi.setTextFill(Color.valueOf("#FFFFFF"));
        this.radioButtonCash = new RadioButton("Cash");
        this.radioButtonCash.getStylesheets().add(getClass().getResource("/Utils/RadioButton.css").toExternalForm());
        this.radioButtonTransfer = new RadioButton("Transfer");
        this.radioButtonTransfer.getStylesheets().add(getClass().getResource("/Utils/RadioButton.css").toExternalForm());

        ToggleGroup toggleGroup = new ToggleGroup();
        this.radioButtonCash.setToggleGroup(toggleGroup);
        this.radioButtonTransfer.setToggleGroup(toggleGroup);

        Label labelKeterangan = new Label("Keterangan");
        labelKeterangan.setStyle("-fx-font: 20 'Poppins Medium'");
        labelKeterangan.setTextFill(Color.valueOf("#FFFFFF"));
        // TextField fieldKeterangan = new TextField();
        this.fieldKeterangan.setMaxWidth(200);
        this.fieldKeterangan.getStylesheets().add(getClass().getResource("/Utils/TextField.css").toExternalForm());

        Button buttonSimpan = new Button("Simpan");
        buttonSimpan.setOnMouseClicked(e -> {
            if (isFormFilled()) {
                LocalDate selectedTanggal = this.datePickerTanggal.getValue();
                DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern("YYYY-MM-d");
                String tanggal = selectedTanggal.format(formatTanggal).toString();

                String selectedKategori = this.combobox.getValue().toString();

                int kategoriId = 0;
                
                double jumlah = Double.parseDouble(fieldJumlah.getText().replace(",", ""));
                String keterangan = fieldKeterangan.getText();
                String tipePembayaran = "";

                if (radioButtonCash.isSelected()) {
                    tipePembayaran = radioButtonCash.getText();
                } else if (radioButtonTransfer.isSelected()) {
                    tipePembayaran = radioButtonTransfer.getText();
                }

                boolean isDefault = TanamUangModel.getIsKategoriDefault(selectedKategori);

                if (isDefault) {
                    kategoriId = TanamUangModel.getIdKategoriDefault(selectedKategori);
                } else {
                    kategoriId = TanamUangModel.getIdKategoriUser(selectedKategori);
                }

                System.out.println("Kategori Id: " + kategoriId);
                System.out.println("Is Default: " + isDefault);

                if (TanamUangModel.simpanTanamUang(tanggal, selectedKategori, kategoriId, jumlah, getSaldo(), tipePembayaran, keterangan, this.tipeTanamUang, isDefault)) {
                    if (this.tipeTanamUang.toLowerCase().equals("pengeluaran")) {
                        clearSelectionTanamUang("Pengeluaran telah tercatat");
                    } else {
                        clearSelectionTanamUang("Pemasukan telah tercatat");
                    }
                } else {
                    AlertHelper.alert("Mohon isi data Anda.");
                }
            } else {
                AlertHelper.alert("Mohon isi data Anda");
            }
        });

        Text historiKeuanganmu = createText("Histori Keuanganmu",
                    "-fx-font: 30 'Poppins Regular'; -fx-fill: #FFFFFF;",
                    0, 0);

        // kontenHistorikeuangan
        VBox kontenHistoriKeuangan = new VBox();
        kontenHistoriKeuangan.setSpacing(10);

        HBox isiKontenTulisan = new HBox(historiKeuanganmu);
            isiKontenTulisan.setSpacing(15);
            isiKontenTulisan.setPadding(new Insets(5, 0, 0, 25));
            isiKontenTulisan.setAlignment(Pos.CENTER_LEFT);

            VBox kontenTulisan = new VBox(isiKontenTulisan);

            // Membuat konten histori keuangan
            // Yang perlu diambil di database adalah keterangan, nominal, tipe menggunakan
            // gambar, dan tanggal

            for (int i = 0; i < getTotalBarangyangDIbeli(); i++) {
                String keterangan = keteranganBarangList.get(i);
                double nominal = nominalBarangList.get(i);
                String tipe = tipeBarangList.get(i);
                String tanggal = tanggalBarangList.get(i);

                Text keteranganText = createText(keterangan, "-fx-font: 15 'Poppins Bold'; -fx-fill: #FFFFFF;",
                        0, 0);

                String tampilanKeterangan = keteranganText.getText().length() > 10
                        ? keteranganText.getText().substring(0, 10) + "..."
                        : keteranganText.getText();

                Label labelKeteranganHistori = new Label(tampilanKeterangan);
                labelKeteranganHistori.setStyle("-fx-font: 15 'Poppins SemiBold'; -fx-text-fill: #ffffff;"); // Set the style

                // Tambahkan tooltip yang menampilkan saldo lengkap
                Tooltip tooltipKeterangan = new Tooltip(keteranganText.getText());
                Tooltip.install(labelKeteranganHistori, tooltipKeterangan);

                VBox keteranganStackPane = new VBox(labelKeteranganHistori);
                keteranganStackPane.setAlignment(Pos.CENTER_LEFT);

                Long roundedValueNominal = Math.round(nominal);
                Text nominalText = createText(formatDuit(roundedValueNominal),
                        "-fx-font: 15 'Poppins Bold'; -fx-fill: #798F97;", 0, 0);

                ImageView kondisi = new ImageView();
                if (tipe.equals("pemasukan")) {
                    kondisi = new ImageView("file:src/Assets/View/Dashboard/PemasukanKondisi.png");
                    kondisi.setFitHeight(35);
                    kondisi.setFitWidth(100);
                    kondisi.setPreserveRatio(true);
                } else {
                    kondisi = new ImageView("file:src/Assets/View/Dashboard/PengeluaranKondisi.png");
                    kondisi.setFitHeight(35);
                    kondisi.setFitWidth(100);
                    kondisi.setPreserveRatio(true);
                }

                HBox gambarKondisidanNominal = new HBox(nominalText, kondisi);
                gambarKondisidanNominal.setSpacing(10);
                VBox nominalStackPane = new VBox(gambarKondisidanNominal);
                nominalStackPane.setAlignment(Pos.CENTER);

                Text tanggalText = createText(formatTanggal(tanggal), "-fx-font: 15 'Poppins Bold'; -fx-fill: #798F97;",
                        0, 0);

                VBox tanggalTextPane = new VBox(tanggalText);
                tanggalTextPane.setAlignment(Pos.CENTER_RIGHT);

                HBox kontenHistoriKeuanganBarang = new HBox(keteranganStackPane, nominalStackPane,
                        gambarKondisidanNominal,
                        tanggalTextPane);
                kontenHistoriKeuanganBarang.setSpacing(80);
                kontenHistoriKeuanganBarang.setPadding(new Insets(10, 0, 10, 25));
                kontenHistoriKeuanganBarang.setAlignment(Pos.CENTER);
                kontenHistoriKeuanganBarang.setStyle("-fx-background-color: #213339; -fx-background-radius: 30px");

                kontenHistoriKeuangan.getChildren().add(kontenHistoriKeuanganBarang);
                keteranganStackPane.setMaxWidth(200);
                nominalStackPane.setMaxWidth(200);
                tanggalTextPane.setMaxWidth(200);
            }

        VBox kontenBawahPane = new VBox(kontenTulisan, kontenHistoriKeuangan);
        kontenBawahPane.setPadding(new Insets(0, 0, 0, 10));
        kontenBawahPane.setSpacing(10);
        
        // HBOX
        StackPane stackPaneButton = new StackPane(buttonBG, buttonActive, buttonPengeluaran, buttonPemasukan);
        HBox hboxButtonTU = new HBox(stackPaneButton);

        StackPane.setAlignment(buttonPengeluaran, javafx.geometry.Pos.CENTER_LEFT);
        StackPane.setAlignment(buttonPemasukan, javafx.geometry.Pos.CENTER_RIGHT);

        stackPaneButton.setMinSize(hboxButtonTU.getMaxWidth() + 50, hboxButtonTU.getMaxHeight() + 50);
        buttonBG.setFill(Color.valueOf("#0D1416"));
        buttonBG.setWidth(360);
        buttonBG.setHeight(60);
        buttonBG.setArcWidth(50);
        buttonBG.setArcHeight(50);

        buttonActive.setFill(Color.valueOf("#761E19"));
        buttonActive.setWidth(170);
        buttonActive.setHeight(60);
        buttonActive.setArcWidth(50);
        buttonActive.setArcHeight(50);
        StackPane.setAlignment(buttonActive, javafx.geometry.Pos.CENTER_LEFT);

        HBox formTanggal = new HBox(labelTanggal, datePickerTanggal);
        formTanggal.setSpacing(20);

        HBox formKategori = new HBox(labelKategori, this.combobox, hyperlinkEdit);
        formKategori.setSpacing(20);

        HBox formNominal = new HBox(labelJumlah, fieldJumlah);
        formNominal.setSpacing(20);

        HBox formTipeTransaksi = new HBox(labelTipeTransaksi, radioButtonCash, radioButtonTransfer);
        formTipeTransaksi.setSpacing(20);
        formTipeTransaksi.setAlignment(Pos.CENTER_LEFT);

        HBox formKeterangan = new HBox(labelKeterangan, fieldKeterangan);
        formKeterangan.setSpacing(20);
        // formKeterangan.setAlignment(Pos.CENTER);

        // HBox hboxButtonSimpan = new HBox(buttonSimpan);
        // Menambahkan gambar
        ImageView imageSimpan = new ImageView(new Image("file:src/Assets/View/Dashboard/SimpanTanamUang.png"));
        imageSimpan.setFitWidth(200);
        imageSimpan.setFitHeight(50);
        imageSimpan.setPreserveRatio(true);
        Hyperlink hyperlinkSimpan = new Hyperlink();
        hyperlinkSimpan.setGraphic(imageSimpan);
        hyperlinkSimpan.setOnMouseClicked(e -> {
            if (isFormFilled()) {
                LocalDate selectedTanggal = this.datePickerTanggal.getValue();
                DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern("YYYY-MM-d");
                String tanggal = selectedTanggal.format(formatTanggal).toString();

                String selectedKategori = this.combobox.getValue().toString();

                int kategoriId = 0;
                
                double jumlah = Double.parseDouble(fieldJumlah.getText().replace(",", ""));
                String keterangan = fieldKeterangan.getText();
                String tipePembayaran = "";

                if (radioButtonCash.isSelected()) {
                    tipePembayaran = radioButtonCash.getText();
                } else if (radioButtonTransfer.isSelected()) {
                    tipePembayaran = radioButtonTransfer.getText();
                }

                boolean isDefault = TanamUangModel.getIsKategoriDefault(selectedKategori);

                if (isDefault) {
                    kategoriId = TanamUangModel.getIdKategoriDefault(selectedKategori);
                } else {
                    kategoriId = TanamUangModel.getIdKategoriUser(selectedKategori);
                }

                System.out.println("Kategori Id: " + kategoriId);
                System.out.println("Is Default: " + isDefault);

                if (TanamUangModel.simpanTanamUang(tanggal, selectedKategori, kategoriId, jumlah, getSaldo(), tipePembayaran, keterangan, this.tipeTanamUang, isDefault)) {
                    if (this.tipeTanamUang.toLowerCase().equals("pengeluaran")) {
                        clearSelectionTanamUang("Pengeluaran telah tercatat");
                    } else {
                        clearSelectionTanamUang("Pemasukan telah tercatat");
                    }
                } else {
                    AlertHelper.alert("Mohon isi data Anda.");
                }
            } else {
                AlertHelper.alert("Mohon isi data Anda");
            }
        });

        VBox vboxKontenKiriKiri = new VBox(labelTanggal, labelKategori, labelJumlah);
        vboxKontenKiriKiri.setSpacing(18);
        vboxKontenKiriKiri.setAlignment(Pos.CENTER_LEFT);
        VBox vboxKontenKiriKanan = new VBox(this.datePickerTanggal, this.combobox, this.fieldJumlah);
        vboxKontenKiriKanan.setSpacing(10);
        vboxKontenKiriKanan.setAlignment(Pos.CENTER_LEFT);

        HBox hboxGabunganKontenKiri = new HBox(vboxKontenKiriKiri, vboxKontenKiriKanan);
        hboxGabunganKontenKiri.setSpacing(20);
        hboxGabunganKontenKiri.setAlignment(Pos.CENTER_LEFT);

        HBox hboxRadioButton = new HBox(this.radioButtonCash, this.radioButtonTransfer);
        hboxRadioButton.setSpacing(20);
        hboxRadioButton.setAlignment(Pos.CENTER);

        VBox vboxKontenKananKiri = new VBox(labelTipeTransaksi, labelKeterangan);
        vboxKontenKananKiri.setSpacing(18);
        VBox vboxKontenKananKanan = new VBox(hboxRadioButton, fieldKeterangan, hyperlinkSimpan);
        vboxKontenKananKanan.setSpacing(10);

        HBox hboxGabunganKontenKanan = new HBox(vboxKontenKananKiri, vboxKontenKananKanan);
        hboxGabunganKontenKanan.setSpacing(20);
        hboxGabunganKontenKanan.setAlignment(Pos.CENTER_LEFT);

        HBox hboxKontenGabungan = new HBox(hboxGabunganKontenKiri, hboxGabunganKontenKanan);
        hboxKontenGabungan.setSpacing(60);
        // hboxKontenGabungan.setStyle("-fx-background-color: #ffff00"); // yellow
        
        VBox kontenTengah = new VBox(hboxButtonTU, hboxKontenGabungan);
        kontenTengah.setSpacing(20);
        
        // Membuat konten bagian tengah untuk main pane
        VBox kontenTengahPane = new VBox(kontenTengah);
        kontenTengahPane.setPadding(new Insets(0, 0, 0, 30));
        kontenTengahPane.setSpacing(10);

        fieldJumlah.textProperty().addListener(new ChangeListener<String>() {
            private String codeFormat = ",##0";
            private int codeLen = 4;

            @Override
            // ObservableValue = StringProperty (Represent an object that has changed its
            // state)
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Hapus listener sementara

                fieldJumlah.textProperty().removeListener(this);
                if (newValue.replace(",", "").matches("\\d*")) { // Check inputan angka atau tidak
                    if (newValue.length() > oldValue.length()) {
                        if (newValue.length() > 13) {
                            fieldJumlah.setText(oldValue);
                        } else {
                            updateCodeFormat(newValue);
                            formatAndSet(newValue, this.codeFormat);
                        }
                    }
                } else {
                    fieldJumlah.setText(oldValue);
                }

                // Tambahkan kembali listener setelah pembaruan teks
                fieldJumlah.textProperty().addListener(this);
            }

            private void formatAndSet(String text, String format) {
                DecimalFormat decimalFormat = new DecimalFormat(format);
                double value = Double.parseDouble(text.replace(",", ""));
                System.out.println(value);
                fieldJumlah.setText("");
                fieldJumlah.setText(decimalFormat.format(value));
            }

            private void updateCodeFormat(String text) {
                int newLen = text.replace(",", "").length();
                if (newLen == this.codeLen + 3) {
                    this.codeFormat = "#," + this.codeFormat;
                    codeLen += 3;
                } else if (newLen >= 4) {
                    this.codeFormat = "#" + this.codeFormat;
                }
            }
        });
        // VBox untuk menampung konten atas, tengah, dan bawah
        // VBox kontenVBox = new VBox(kontenAtasPane, kontenTengahPane, kontenBawahPane);
        VBox kontenVBox = new VBox();
        kontenVBox.getChildren().addAll(kontenAtasPane, kontenTengahPane, kontenBawahPane);
        VBox.setMargin(kontenBawahPane, new Insets(5, 0, 0, 0));
        kontenVBox.setSpacing(2);
        kontenVBox.setPadding(new Insets(0, 10, 0, 0));
        kontenVBox.setStyle("-fx-background-color: #141F23;");
        kontenVBox.setMaxHeight(this.stage.getHeight());

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(kontenVBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setMaxHeight(this.stage.getHeight() - 100);
        scrollPane.setMaxWidth(this.stage.getWidth());
        scrollPane.getStylesheets().add(getClass().getResource("/Utils/ScrollBar.css").toExternalForm());

        String scrollbarStyle = "-fx-background-color: #141F23;";
        scrollbarStyle += "-fx-background-color: #0B1214;";
        scrollbarStyle += "-fx-background-insets: 0;";
        scrollbarStyle += "-fx-padding: 0;";
        scrollbarStyle += "-fx-background-radius: 20px;";

        scrollPane.setId("custom-scrollbar");
        // scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle(scrollbarStyle);

        mainPane.getChildren().add(scrollPane);
        mainPane.setStyle("-fx-background-color: #141F23;-fx-background-radius: 30px;");
        mainPane.setMaxHeight(this.stage.getHeight() - 20);
        // mainPane.setMaxWidth(this.stage.getWidth());
        mainPane.setPadding(new Insets(0, 10, 0, 0));
        mainPane.setAlignment(Pos.CENTER);

        HBox rightBar = new HBox();

        // Mengatur binding fitToHeight dan fitToWidth
        scrollPane.setFitToWidth(true);

        HBox penggabunganMainPanedenganSideBar = new HBox(sideBar, mainPane);
        penggabunganMainPanedenganSideBar.setStyle("-fx-background-color: #0B1214;");
        penggabunganMainPanedenganSideBar.setPadding(new Insets(10, 0, 0, 0));
        HBox fullPane = new HBox(penggabunganMainPanedenganSideBar, rightBar);
        // Set horizontal grow priority for mainPane
        // HBox.setHgrow(mainPaneHBox, Priority.ALWAYS);
        fullPane.setStyle("-fx-background-color: #0B1214");

        Scene scene = new Scene(fullPane, this.stage.getWidth(), this.stage.getHeight());

        this.stage.setScene(scene);
        this.stage.setMaximized(true);
        this.stage.show(); // Tetapkan setelah styling selesai
    }

    private void refreshView(ScrollPane scrollPane, VBox scrollContentBox, StackPane mainPane) {
        scrollContentBox.getChildren().clear();
        
        VBox contentPane = new VBox();
        contentPane.setStyle("-fx-background-radius: 20");
        contentPane.setMaxSize(mainPane.getMaxWidth(), mainPane.getMaxHeight() - 20);
        contentPane.setSpacing(20);

        VBox scrollPaneContent = createScrollPaneContent(scrollPane, mainPane);

        // scrollPaneContent.setStyle("-fx-background-color: #213339;");
        // scrollPane.setContent(scrollPaneContent);
        // scrollPane.setStyle("-fx-background-color: #213339;");

        scrollPaneContent.setSpacing(15);
        scrollPaneContent.setAlignment(Pos.CENTER);
        scrollPaneContent.setStyle("-fx-background-color: #141F23");

        scrollPane.setContent(scrollPaneContent);
    }

    private static String formatTanggal(String tanggal) {
        // Parse string tanggal dari database ke objek LocalDate
        LocalDate tanggalLocalDate = LocalDate.parse(tanggal);

        // Format ulang LocalDate ke dalam string dengan format yang diinginkan
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMMM-yyyy");
        String tanggalDiformat = tanggalLocalDate.format(formatter);

        // Gunakan hasil tanggal yang sudah diformat
        return tanggalDiformat;
    }

    private VBox createScrollPaneContent(ScrollPane scrollPane, StackPane mainPane) {
        VBox scrollPaneContent = new VBox();
        String[] listKategori = TanamUangModel.getKategoriTanamUang(this.tipeTanamUang);

        for (int i = 0; i < listKategori.length; i++) {
            Text namaKategori = createText(listKategori[i], "-fx-font: 16 Poppins;", "#FFFFFF");
            Hyperlink editHyperlink = new Hyperlink();
            editHyperlink.setGraphic(new ImageView(new Image("file:src/Assets/View/Dashboard/Edit.png")));
            Hyperlink deleteHyperlink = new Hyperlink();
            deleteHyperlink.setGraphic(new ImageView(new Image("file:src/Assets/View/Dashboard/Delete.png")));

            HBox hboxNamaKategori = new HBox(namaKategori);
            HBox hboxEditHyperlink = new HBox(editHyperlink);
            HBox hboxDeleteHyperlink = new HBox(deleteHyperlink);
            
            HBox.setHgrow(hboxNamaKategori, Priority.ALWAYS);
            hboxNamaKategori.setAlignment(Pos.CENTER_LEFT);
            hboxEditHyperlink.setAlignment(Pos.CENTER_LEFT);
            hboxDeleteHyperlink.setAlignment(Pos.CENTER_LEFT);

            hboxNamaKategori.setPadding(new Insets(0, 0, 0, 20));

            HBox itemContent = new HBox(hboxNamaKategori, hboxEditHyperlink, hboxDeleteHyperlink);

            int index = i;
            boolean isKategoriDefault = TanamUangModel.getIsKategoriDefault(namaKategori.getText());

            deleteHyperlink.setOnMouseClicked(e -> {
                StackPane paneHapus = new StackPane();
                StackPane backgroundPaneHapus = new StackPane();

                // paneHapus.setMaxSize(mainPane.getWidth() - 400, mainPane.getHeight() - 400);
                paneHapus.setMaxSize(400, 200);
                paneHapus.setStyle("-fx-background-radius: 20; -fx-background-color: #141F23");
                backgroundPaneHapus.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
                
                Text titleHapus = createText("Hapus Kategori", "-fx-font: 20 'Poppins Bold';", "#FFFFFF");
                Hyperlink deleteBackHyperlink = new Hyperlink();
                deleteBackHyperlink.setGraphic(new ImageView(new Image("file:src/Assets/View/Dashboard/Back.png")));

                deleteBackHyperlink.setOnMouseClicked(f -> {
                    refreshView(scrollPane, scrollPaneContent, mainPane);
                    mainPane.getChildren().remove(paneHapus);
                    mainPane.getChildren().remove(backgroundPaneHapus);
                });

                Text titleYakinHapus = createText("Apakah Anda yakin ingin menghapus kategori ", "-fx-font: 18 'Poppins Medium';", "#FFFFFF");
                Text titleNamaKategoriHapus = createText(listKategori[index], "-fx-font: bold 18 Poppins;", "#ff424c");
                Text titleTandaTanya = createText("?", "-fx-font: 18 'Poppins Medium';", "#FFFFFF");

                HBox hboxTitleHapus = new HBox(deleteBackHyperlink, titleHapus);
                hboxTitleHapus.setSpacing(20);

                HBox hboxSubTitleHapus = new HBox(titleYakinHapus, titleNamaKategoriHapus, titleTandaTanya);

                Button buttonHapus = new Button("Hapus");
                Button buttonHapusBatal = new Button("Batal");

                buttonHapusBatal.setOnAction(f -> {
                    if (mainPane.getParent() != null) {
                        mainPane.getChildren().remove(paneHapus);
                        mainPane.getChildren().remove(backgroundPaneHapus);
                    }
                });

                buttonHapus.setOnAction(f -> {
                    if (isKategoriDefault) {
                        if (TanamUangModel.hapusKategori(listKategori[index], isKategoriDefault)) {
                            refreshView(scrollPane, scrollPaneContent, mainPane);
    
                            if (mainPane.getParent() != null) {
                                    mainPane.getChildren().remove(paneHapus);
                                    mainPane.getChildren().remove(backgroundPaneHapus);
                            }
                            AlertHelper.info("Kategori " + listKategori[index] + " berhasil dihapus!");
                        } else {
                            AlertHelper.alert("Kategori gagal dihapus!");
                        }
                    } else {
                        if (TanamUangModel.hapusKategori(listKategori[index], isKategoriDefault)) {
                            refreshView(scrollPane, scrollPaneContent, mainPane);
    
                            if (mainPane.getParent() != null) {
                                    mainPane.getChildren().remove(paneHapus);
                                    mainPane.getChildren().remove(backgroundPaneHapus);
                                }
                            AlertHelper.info("Kategori " + listKategori[index] + " berhasil dihapus!");
                        } else {
                            AlertHelper.alert("Kategori gagal dihapus!");
                        }
                    }
                });

                HBox hboxButtonHapus = new HBox(buttonHapusBatal, buttonHapus);
                hboxButtonHapus.setSpacing(20);
                hboxButtonHapus.setAlignment(Pos.CENTER);

                VBox itemContentHapus = new VBox(hboxTitleHapus, hboxSubTitleHapus, hboxButtonHapus);
                itemContentHapus.setSpacing(20);
                itemContentHapus.setMaxSize(paneHapus.getMaxWidth() - 100, paneHapus.getMaxHeight() - 30);
                itemContentHapus.setPadding(new Insets(10, 0, 0, 10));

                paneHapus.getChildren().add(itemContentHapus);

                if (!mainPane.getChildren().contains(paneHapus)) {
                    mainPane.getChildren().addAll(backgroundPaneHapus, paneHapus);
                }

            });

            editHyperlink.setOnMouseClicked(e -> {
                StackPane paneEdit = new StackPane();
                StackPane backgroundPaneEdit = new StackPane();
                paneEdit.setMaxSize(400, 190);
                paneEdit.setStyle("-fx-background-radius: 20; -fx-background-color: #141F23");

                backgroundPaneEdit.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
                Hyperlink editBackHyperlink = new Hyperlink();
                editBackHyperlink.setGraphic(new ImageView(new Image("file:src/Assets/View/Dashboard/Back.png")));

                editBackHyperlink.setOnMouseClicked(f -> {
                    refreshView(scrollPane, scrollPaneContent, mainPane);
                    mainPane.getChildren().remove(paneEdit);
                    mainPane.getChildren().remove(backgroundPaneEdit);
                });

                Text titleEdit = createText("Ubah Kategori", "-fx-font: 20 'Poppins Bold';", "#FFFFFF");
                Label labelEditNamaKategori = new Label("Nama:");
                labelEditNamaKategori.setStyle("-fx-font: 16 'Poppins Medium'");
                labelEditNamaKategori.setTextFill(Color.valueOf("#FFFFFF"));
                TextField fieldEditNamaKategori = new TextField(listKategori[index]);
                fieldEditNamaKategori.setStyle(
                    "-fx-background-radius: 20px; -fx-background-color: #0D1416;" +
                    "-fx-border-color: #213339;" +
                    "-fx-border-radius: 20px; " +
                    "-fx-text-fill: #FFFFFF"
                );
                Button editButtonSimpanKategori = new Button("Simpan");
                Button editButtonBatalKategori = new Button("Batal");

                editButtonBatalKategori.setOnMouseClicked(g -> {
                    if (mainPane.getParent() != null) {
                        mainPane.getChildren().remove(paneEdit);
                        mainPane.getChildren().remove(backgroundPaneEdit);
                    }
                });

                String nama_kategori = namaKategori.getText();

                editButtonSimpanKategori.setOnMouseClicked(h -> {
                    String valueFieldEditNamaKategori = fieldEditNamaKategori.getText();

                    if (isKategoriDefault) {
                        int idKategoriDefault = TanamUangModel.getIdKategoriDefault(nama_kategori);
                        boolean statusSimpan = TanamUangModel.simpanNamaKategori(valueFieldEditNamaKategori,
                                namaKategori.getText(),
                                isKategoriDefault, this.tipeTanamUang, idKategoriDefault);

                        if (statusSimpan) {
                            refreshView(scrollPane, scrollPaneContent, mainPane);

                            if (mainPane.getParent() != null) {
                                mainPane.getChildren().remove(paneEdit);
                                mainPane.getChildren().remove(backgroundPaneEdit);
                            }

                            AlertHelper.info("Berhasil mengubah nama kategori!");
                        } else {
                            AlertHelper.alert("Gagal mengubah nama kategori!");
                        }
                    } else {
                        boolean statusSimpan = TanamUangModel.simpanNamaKategori(valueFieldEditNamaKategori,
                                namaKategori.getText(),
                                isKategoriDefault, this.tipeTanamUang, 0);
                        if (statusSimpan) {
                            refreshView(scrollPane, scrollPaneContent, mainPane);

                            if (mainPane.getParent() != null) {
                                mainPane.getChildren().remove(paneEdit);
                                mainPane.getChildren().remove(backgroundPaneEdit);
                            }

                            AlertHelper.info("Berhasil mengubah nama kategori!");
                        } else {
                            AlertHelper.alert("Gagal mengubah nama kategori!");
                        }
                    }
                });

                
                HBox editTitleHBox = new HBox(editBackHyperlink, titleEdit);
                editTitleHBox.setSpacing(20);
                
                HBox editHBox = new HBox(labelEditNamaKategori, fieldEditNamaKategori);
                editHBox.setSpacing(20);
                editHBox.setAlignment(Pos.CENTER);

                HBox editButtonHBox = new HBox(editButtonBatalKategori, editButtonSimpanKategori);
                editButtonHBox.setSpacing(20);
                editButtonHBox.setAlignment(Pos.CENTER);

                VBox editVBox = new VBox(editTitleHBox, editHBox, editButtonHBox);
                editVBox.setMaxSize(paneEdit.getMaxWidth() - 50, paneEdit.getMaxHeight() - 50);
                editVBox.setSpacing(20);
                editVBox.setAlignment(Pos.CENTER);

                paneEdit.getChildren().add(editVBox);

                if (!mainPane.getChildren().contains(paneEdit)) {
                    mainPane.getChildren().add(backgroundPaneEdit);
                    mainPane.getChildren().add(paneEdit);
                }
            });

            VBox itemBox = new VBox();
            itemBox.setMaxSize(scrollPane.getMaxWidth() - 20, 400);
            itemBox.getChildren().add(itemContent);
            itemBox.setAlignment(Pos.CENTER);
            itemBox.setStyle("-fx-background-color: #213339; -fx-background-radius: 10px;");

            scrollPaneContent.getChildren().add(itemBox);
        }

        scrollPaneContent.setSpacing(15);
        // scrollPaneContent.setMaxSize(mainPane.getWidth(), mainPane.getHeight() - 20);
        scrollPaneContent.setAlignment(Pos.CENTER);
        scrollPaneContent.setStyle("-fx-background-color: #141F23");
        // scrollPaneContent.setStyle("-fx-background-color: #C21292");
        return scrollPaneContent;
    }

    // fungsi untuk membuat text dengan return Text
    private Text createText(String text, String style, String color) {
        Text newText = new Text(text);
        newText.setStyle(style); // menetapkan style text
        newText.setFill(Color.valueOf(color)); // menetapkan warna text
        return newText;
    }

    private void clearSelectionTanamUang(String message) {
        this.datePickerTanggal.setValue(null);
        this.combobox.getSelectionModel().clearSelection();
        this.fieldJumlah.setText("");
        this.fieldKeterangan.setText("");
        this.radioButtonCash.setSelected(false);
        this.radioButtonTransfer.setSelected(false);

        AlertHelper.info(message);
    }

    private boolean isFormFilled() {
        System.out.println("DatePicker: " + (this.datePickerTanggal.getValue() != null));
        System.out.println("ComboBox: " + (this.combobox.getValue() != null));
        System.out.println("Field Jumlah: " + (!this.fieldJumlah.getText().isEmpty()));
        System.out.println("field Keterangan: " + (!this.fieldKeterangan.getText().isEmpty()));
        System.out.println("Radio button: " + (this.radioButtonCash.isSelected() || this.radioButtonTransfer.isSelected()));

        return this.datePickerTanggal.getValue() != null &&
                this.combobox.getValue() != null &&
                !this.fieldJumlah.getText().isEmpty() &&
                !this.fieldKeterangan.getText().isEmpty() &&
                (this.radioButtonCash.isSelected() || this.radioButtonTransfer.isSelected());
    }


    private void updateTooltipPosition(double x, double y) {
        tooltip.setX(x + 10); // Sesuaikan posisi X agar tidak menutupi cursor
        tooltip.setY(y - 20); // Sesuaikan posisi Y agar tidak menutupi cursor
    }

    private Text createText(String text, String style, double translateX, double translateY) {
        Text welcome = new Text(text);
        welcome.setStyle(style);
        welcome.setTranslateX(translateX);
        welcome.setTranslateY(translateY);
        welcome.toFront();
        return welcome;
    }

    // Fungsi untuk mendapatkan username
    private String getUsername() {
        LoginModel loginModel = new LoginModel();
        return loginModel.getLastActiveUsers();
    }

    private double getSaldo() {
        LoginModel loginModel = new LoginModel();
        return loginModel.getUserSaldo();
    }

    private double getFirstTargetHarga() {
        TampilkanSemuaTarget tampilkanSemuaTarget = new TampilkanSemuaTarget();
        return tampilkanSemuaTarget.ambilHargaDataPertama(this.userId);

    }

    private int getTotalBarangyangDIbeli() {
        return model.banyakDatadiTransac();
    }

    // Mouse COORDINATES TRACKER: fungsi untuk mencetak koordinat x dan y dari
    // sebuah mouse yang diklik
    private void setOnMouseClicked(StackPane root, Node item) {
        root.setOnMouseClicked(new javafx.event.EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent me) {
                double x = me.getSceneX();
                double y = me.getSceneY();

                // mengkalkulasi posisi translasi x dan y untuk mendapatkan posisi yang ideal
                double itemX = x - (root.getWidth() / 2); // untuk menetapkan pada tengah node root
                double itemY = y - (root.getHeight() / 2); // untuk menetapkan pada tengah node root

                // menetapkan posisi baru untuk item
                item.setTranslateX(itemX); // posisi baru untuk koordinat x
                item.setTranslateY(itemY); // posisi baru untuk kooordinat y

                System.out.println("Item placed at X -> " + itemX);
                System.out.println("Item placed at Y -> " + itemY);
            }
        });
    }

    // Fungsi untuk mengambil data yang paling pertama di database target
    private String getTarget() {
        TampilkanSemuaTarget target1 = new TampilkanSemuaTarget();
        return target1.ambilDataNamaTargetPertama(this.userId);
    }

    private long mendapatkanSaldoUntukMembeliBarang() {
        TampilkanSemuaTarget tampilkanSemuaTarget = new TampilkanSemuaTarget();
        return tampilkanSemuaTarget.ambilSaldodanBatasKritis();

    }

    private static String formatDuit(double nilai) {
        // Membuat instance NumberFormat untuk mata uang Indonesia (IDR)
        Locale locale = Locale.forLanguageTag("id-ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        // Mengatur nilai
        String formattedValue = numberFormat.format(nilai);

        return formattedValue;
    }
}

class ImageLinkPaneTanam {
    public static VBox createImageLinkVBox(Stage stage, SceneController sceneController) {
        // Gunakan ImageView untuk semua pilihan di Sidebar
        ImageView logoImageView = new ImageView(new Image("file:src/Assets/View/Dashboard/Logo.png"));
        logoImageView.setFitWidth(200);
        logoImageView.setFitHeight(50);
        // logoImageView.setTranslateY(-stage.getHeight() / 2 + 270);
        // logoImageView.setTranslateX(-stage.getWidth() / 2 + 485);

        ImageView homePageImageView = new ImageView(new Image("file:src/Assets/View/Dashboard/HomePage.png"));
        ImageView tanamUangImageView = new ImageView(new Image("file:src/Assets/View/Dashboard/Tanam Uang.png"));
        ImageView pantauUangImageView = new ImageView(new Image("file:src/Assets/View/Dashboard/Pantau Uang.png"));
        ImageView panenUangImageView = new ImageView(new Image("file:src/Assets/View/Dashboard/Panen Uang.png"));
        ImageView modeUser = new ImageView("file:src/Assets/View/Dashboard/Mode User.png");
        ImageView logOut = new ImageView("file:src/Assets/View/Dashboard/Log Out.png");

        // Menyesuaikan ukuran ImageView
        homePageImageView.setFitWidth(190);
        homePageImageView.setFitHeight(35);
        homePageImageView.setPreserveRatio(true);
        homePageImageView.setTranslateX(5);
        tanamUangImageView.setFitWidth(190);
        tanamUangImageView.setFitHeight(35);
        tanamUangImageView.setPreserveRatio(true);
        pantauUangImageView.setFitWidth(190);
        pantauUangImageView.setFitHeight(35);
        pantauUangImageView.setPreserveRatio(true);
        panenUangImageView.setFitWidth(190);
        panenUangImageView.setFitHeight(35);
        panenUangImageView.setPreserveRatio(true);
        modeUser.setFitWidth(190);
        modeUser.setFitHeight(35);
        modeUser.setPreserveRatio(true);
        logOut.setFitWidth(200);
        logOut.setFitHeight(60);
        logOut.setPreserveRatio(true);

        // Membuat Hyperlink dengan menggunakan ImageView
        Hyperlink homeHyperlink = createHyperlinkWithImageView(homePageImageView);
        Hyperlink tanamUangHyperlink = createHyperlinkWithImageView(tanamUangImageView);
        Hyperlink pantauUangHyperlink = createHyperlinkWithImageView(pantauUangImageView);
        Hyperlink panenUangHyperlink = createHyperlinkWithImageView(panenUangImageView);
        Hyperlink modeUserHyperlink = createHyperlinkWithImageView(modeUser);
        Hyperlink logOutHyperlink = createHyperlinkWithImageView(logOut);

        // Menambahkan fungsi ketika hyperlink diklik
        homeHyperlink.setOnMouseClicked(e -> sceneController.switchToDashboard());
        tanamUangHyperlink.setOnMouseClicked(e -> sceneController.switchToTanamUang());
        pantauUangHyperlink.setOnMouseClicked(e -> sceneController.switchToPantauUang());
        panenUangHyperlink.setOnMouseClicked(e -> sceneController.switchToPanenUang());
        // modeUserHyperlink.setOnMouseClicked(e -> sceneController.switchToModeUser());
        logOutHyperlink.setOnMouseClicked(e -> sceneController.switchToLogin());

        Rectangle region = new Rectangle(220, 85);
        region.setStyle("-fx-background-radius: 30 0 0 30;");
        region.setTranslateY(-20);
        region.setFill(Color.valueOf("#141F23"));
        homeHyperlink.setTranslateX(10);
        Group aktifGroup = new Group(region, tanamUangHyperlink);
        aktifGroup.setTranslateX(10);
        // Membuat masing - masing Vbox dan menambahkan Hyperlink ke dalamnya
        VBox homeVBox = new VBox(homeHyperlink);
        homeVBox.setAlignment(Pos.CENTER);
        VBox tanamUangVBox = new VBox(aktifGroup);
        tanamUangVBox.setAlignment(Pos.CENTER);
        VBox pantauUangVBox = new VBox(pantauUangHyperlink);
        pantauUangVBox.setAlignment(Pos.CENTER);
        VBox panenUangVBox = new VBox(panenUangHyperlink);
        panenUangVBox.setAlignment(Pos.CENTER);
        VBox modeUserVBox = new VBox(modeUserHyperlink);
        modeUserVBox.setAlignment(Pos.CENTER);
        VBox logOutVBox = new VBox(logOutHyperlink);
        logOutVBox.setAlignment(Pos.CENTER);
        // Membuat VBox dan menambahkan Hyperlink ke dalamnya
        VBox kontenSide = new VBox(logoImageView, homeVBox, tanamUangVBox, pantauUangVBox, panenUangVBox,
                modeUserVBox,
                logOutVBox);
        kontenSide.setSpacing(50);
        kontenSide.setAlignment(Pos.CENTER_RIGHT);
        kontenSide.setPadding(new Insets(40, 0, 0, 60));

        return kontenSide;

    }

    private static Hyperlink createHyperlinkWithImageView(ImageView imageView) {
        Hyperlink hyperlink = new Hyperlink();
        hyperlink.setGraphic(imageView);
        return hyperlink;
    }

}

class rightBar {
    public static HBox createRightBar(Stage stage, SceneController sceneController) {
        return null;
    }
}