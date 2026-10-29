
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {

    // Warna utama aplikasi
    private final Color NAVY = new Color(23, 54, 93);
    private final Color BLUE = new Color(39, 100, 165);
    private final Color BG = new Color(245, 248, 252);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT = new Color(45, 55, 72);

    // Penyimpanan sementara tanpa database
    private final List<Buku> daftarBuku = new ArrayList<>();

    private DefaultTableModel model;
    private JTable tabel;
    private JTextField txtCari;
    private JLabel lblTotal;
    private JLabel lblGenre;
    private JLabel lblHalaman;
    private JPanel panelKonten;

    private String halamanAktif = "Dashboard";

    // Class Buku dalam file yang sama
    static class Buku {
        String isbn;
        String judul;
        String penulis;
        String genre;

        Buku(String isbn, String judul, String penulis, String genre) {
            this.isbn = isbn;
            this.judul = judul;
            this.penulis = penulis;
            this.genre = genre;
        }
    }

    public Main() {
        setTitle("uBiblio - Personal Library Management");
        setSize(1100, 700);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout utama
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        tambahDataContoh();
        buatSidebar();
        buatAreaUtama();
        perbaruiTabel();
    }

    // Sidebar navigasi
    private void buatSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(NAVY);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(25, 15, 20, 15));

        JLabel logo = new JLabel("  uBiblio");
        logo.setForeground(WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 27));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subLogo = new JLabel("  PERSONAL LIBRARY");
        subLogo.setForeground(new Color(190, 210, 235));
        subLogo.setFont(new Font("SansSerif", Font.PLAIN, 10));
        subLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(subLogo);
        sidebar.add(Box.createVerticalStrut(45));

        sidebar.add(buatTombolMenu("  Dashboard", "Dashboard"));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(buatTombolMenu("  Koleksi Buku", "Koleksi Buku"));

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("<html><center>uBiblio Desktop<br>Java Swing Edition</center></html>");
        footer.setForeground(new Color(190, 210, 235));
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(footer);

        add(sidebar, BorderLayout.WEST);
    }

    private JButton buatTombolMenu(String teks, String halaman) {
        JButton tombol = new JButton(teks);
        tombol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        tombol.setPreferredSize(new Dimension(190, 45));
        tombol.setHorizontalAlignment(SwingConstants.LEFT);
        tombol.setFont(new Font("SansSerif", Font.BOLD, 13));
        tombol.setForeground(WHITE);
        tombol.setBackground(halaman.equals(halamanAktif)
                ? BLUE : NAVY);
        tombol.setBorder(new EmptyBorder(10, 12, 10, 8));
        tombol.setFocusPainted(false);
        tombol.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tombol.setAlignmentX(Component.LEFT_ALIGNMENT);

        tombol.addActionListener(e -> {
            halamanAktif = halaman;
            lblHalaman.setText(halaman);
            perbaruiWarnaMenu();
            tampilkanHalaman(halaman);
        });

        return tombol;
    }

    // Area utama aplikasi
    private void buatAreaUtama() {
        JPanel kanan = new JPanel(new BorderLayout());
        kanan.setBackground(BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(new EmptyBorder(20, 28, 20, 28));

        lblHalaman = new JLabel("Dashboard");
        lblHalaman.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblHalaman.setForeground(NAVY);

        JLabel profil = new JLabel("Perpustakaan Pribadi");
        profil.setFont(new Font("SansSerif", Font.PLAIN, 13));
        profil.setForeground(new Color(100, 116, 139));

        header.add(lblHalaman, BorderLayout.WEST);
        header.add(profil, BorderLayout.EAST);

        kanan.add(header, BorderLayout.NORTH);

        // Konten halaman
        panelKonten = new JPanel(new BorderLayout());
        panelKonten.setBackground(BG);
        panelKonten.setBorder(new EmptyBorder(24, 28, 24, 28));

        kanan.add(panelKonten, BorderLayout.CENTER);
        add(kanan, BorderLayout.CENTER);

        buatHalamanKoleksi();
    }

    // Halaman koleksi dan tabel
    private void buatHalamanKoleksi() {
        panelKonten.removeAll();
        panelKonten.setLayout(new BorderLayout(0, 18));

        JPanel atas = new JPanel();
        atas.setLayout(new BoxLayout(atas, BoxLayout.Y_AXIS));
        atas.setBackground(BG);

        JLabel judul = new JLabel("Kelola Koleksi Buku");
        judul.setFont(new Font("SansSerif", Font.BOLD, 19));
        judul.setForeground(TEXT);

        JLabel deskripsi = new JLabel(
                "Tambah, cari, ubah, dan hapus koleksi buku kamu.");
        deskripsi.setFont(new Font("SansSerif", Font.PLAIN, 13));
        deskripsi.setForeground(new Color(110, 125, 145));

        atas.add(judul);
        atas.add(Box.createVerticalStrut(6));
        atas.add(deskripsi);
        atas.add(Box.createVerticalStrut(18));

        // Kartu ringkasan
        JPanel kartu = new JPanel(new GridLayout(1, 2, 15, 0));
        kartu.setBackground(BG);
        kartu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JPanel kartuTotal = buatKartu("Total Koleksi", "0");
        lblTotal = (JLabel) kartuTotal.getClientProperty("nilai");
        JPanel kartuGenre = buatKartu("Jumlah Genre", "0");
        lblGenre = (JLabel) kartuGenre.getClientProperty("nilai");

        kartu.add(kartuTotal);
        kartu.add(kartuGenre);
        atas.add(kartu);

        panelKonten.add(atas, BorderLayout.NORTH);

        // Tabel dan panel pencarian
        JPanel areaTabel = new JPanel(new BorderLayout(0, 12));
        areaTabel.setBackground(WHITE);
        areaTabel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel barisCari = new JPanel(new BorderLayout(10, 0));
        barisCari.setBackground(WHITE);

        txtCari = new JTextField();
        txtCari.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtCari.setToolTipText("Cari ISBN, judul, penulis, atau genre");
        txtCari.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 228, 238)),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JButton btnTambah = buatTombol(" + Tambah Buku", BLUE);
        btnTambah.addActionListener(e -> dialogBuku(null));

        barisCari.add(txtCari, BorderLayout.CENTER);
        barisCari.add(btnTambah, BorderLayout.EAST);

        model = new DefaultTableModel(
                new Object[]{"ISBN", "Judul Buku", "Penulis", "Genre"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabel = new JTable(model);
        tabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabel.setRowHeight(35);
        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.setShowVerticalLines(false);
        tabel.setGridColor(new Color(235, 240, 246));
        tabel.setSelectionBackground(new Color(220, 235, 250));
        tabel.setSelectionForeground(NAVY);
        tabel.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12));
        tabel.getTableHeader().setBackground(new Color(235, 242, 250));
        tabel.getTableHeader().setForeground(NAVY);
        tabel.getTableHeader().setPreferredSize(new Dimension(0, 38));

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(BorderFactory.createLineBorder(
                new Color(230, 235, 242)));

        // Tombol edit dan hapus
        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bawah.setBackground(WHITE);

        JButton btnEdit = buatTombol("Edit Buku", new Color(225, 155, 35));
        JButton btnHapus = buatTombol("Hapus Buku", new Color(190, 55, 65));

        btnEdit.addActionListener(e -> editBuku());
        btnHapus.addActionListener(e -> hapusBuku());

        bawah.add(btnEdit);
        bawah.add(btnHapus);

        areaTabel.add(barisCari, BorderLayout.NORTH);
        areaTabel.add(scroll, BorderLayout.CENTER);
        areaTabel.add(bawah, BorderLayout.SOUTH);

        panelKonten.add(areaTabel, BorderLayout.CENTER);

        // Pencarian langsung saat mengetik
        txtCari.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                perbaruiTabel();
            }

            public void removeUpdate(DocumentEvent e) {
                perbaruiTabel();
            }

            public void changedUpdate(DocumentEvent e) {
                perbaruiTabel();
            }
        });

        panelKonten.revalidate();
        panelKonten.repaint();
        perbaruiTabel();
    }

    private JPanel buatKartu(String judul, String nilaiAwal) {
        JPanel kartu = new JPanel(new BorderLayout(0, 8));
        kartu.setBackground(WHITE);
        kartu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 233, 242)),
                new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel labelJudul = new JLabel(judul);
        labelJudul.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelJudul.setForeground(new Color(100, 116, 139));

        JLabel nilai = new JLabel(nilaiAwal);
        nilai.setFont(new Font("SansSerif", Font.BOLD, 24));
        nilai.setForeground(NAVY);

        kartu.add(labelJudul, BorderLayout.NORTH);
        kartu.add(nilai, BorderLayout.CENTER);
        kartu.putClientProperty("nilai", nilai);

        return kartu;
    }

    private JButton buatTombol(String teks, Color warna) {
        JButton tombol = new JButton(teks);
        tombol.setFont(new Font("SansSerif", Font.BOLD, 12));
        tombol.setForeground(WHITE);
        tombol.setBackground(warna);
        tombol.setFocusPainted(false);
        tombol.setBorder(new EmptyBorder(10, 15, 10, 15));
        tombol.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return tombol;
    }

    // Form tambah dan edit
    private void dialogBuku(Buku bukuLama) {
        boolean modeEdit = bukuLama != null;

        JTextField fieldISBN = new JTextField(
                modeEdit ? bukuLama.isbn : "");
        JTextField fieldJudul = new JTextField(
                modeEdit ? bukuLama.judul : "");
        JTextField fieldPenulis = new JTextField(
                modeEdit ? bukuLama.penulis : "");

        String[] pilihanGenre = {
            "Novel", "Pendidikan", "Teknologi",
            "Fantasi", "Sejarah", "Komik", "Lainnya"
        };

        JComboBox<String> comboGenre = new JComboBox<>(pilihanGenre);

        if (modeEdit) {
            comboGenre.setSelectedItem(bukuLama.genre);
            if (comboGenre.getSelectedItem() == null) {
                comboGenre.addItem(bukuLama.genre);
                comboGenre.setSelectedItem(bukuLama.genre);
            }
        }

        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.setPreferredSize(new Dimension(350, 270));
        form.add(new JLabel("ISBN:"));
        form.add(fieldISBN);
        form.add(new JLabel("Judul buku:"));
        form.add(fieldJudul);
        form.add(new JLabel("Penulis:"));
        form.add(fieldPenulis);
        form.add(new JLabel("Genre:"));
        form.add(comboGenre);

        int hasil = JOptionPane.showConfirmDialog(
                this,
                form,
                modeEdit ? "Edit Data Buku" : "Tambah Buku Baru",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (hasil != JOptionPane.OK_OPTION) {
            return;
        }

        String isbn = fieldISBN.getText().trim();
        String judul = fieldJudul.getText().trim();
        String penulis = fieldPenulis.getText().trim();
        String genre = (String) comboGenre.getSelectedItem();

        if (isbn.isEmpty() || judul.isEmpty() || penulis.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "ISBN, judul, dan penulis wajib diisi.",
                    "Data belum lengkap",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ISBN tidak boleh duplikat
        for (Buku b : daftarBuku) {
            if (b.isbn.equalsIgnoreCase(isbn)
                    && (bukuLama == null || b != bukuLama)) {
                JOptionPane.showMessageDialog(this,
                        "ISBN tersebut sudah digunakan.",
                        "ISBN duplikat",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        if (modeEdit) {
            bukuLama.isbn = isbn;
            bukuLama.judul = judul;
            bukuLama.penulis = penulis;
            bukuLama.genre = genre;

            JOptionPane.showMessageDialog(this,
                    "Data buku berhasil diperbarui!");
        } else {
            daftarBuku.add(new Buku(isbn, judul, penulis, genre));

            JOptionPane.showMessageDialog(this,
                    "Buku berhasil ditambahkan!");
        }

        perbaruiTabel();
    }

    private void editBuku() {
        int baris = tabel.getSelectedRow();

        if (baris == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih buku yang ingin diedit terlebih dahulu.");
            return;
        }

        String isbn = model.getValueAt(baris, 0).toString();
        Buku buku = cariISBN(isbn);

        if (buku != null) {
            dialogBuku(buku);
        }
    }

    private void hapusBuku() {
        int baris = tabel.getSelectedRow();

        if (baris == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih buku yang ingin dihapus terlebih dahulu.");
            return;
        }

        String isbn = model.getValueAt(baris, 0).toString();
        Buku buku = cariISBN(isbn);

        if (buku == null) {
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Hapus buku \"" + buku.judul + "\"?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            daftarBuku.remove(buku);
            perbaruiTabel();

            JOptionPane.showMessageDialog(this,
                    "Buku berhasil dihapus.");
        }
    }

    private Buku cariISBN(String isbn) {
        for (Buku b : daftarBuku) {
            if (b.isbn.equalsIgnoreCase(isbn)) {
                return b;
            }
        }
        return null;
    }

    // Memperbarui tabel berdasarkan kata pencarian
    private void perbaruiTabel() {
        if (model == null) {
            return;
        }

        model.setRowCount(0);

        String kata = txtCari == null
                ? "" : txtCari.getText().trim().toLowerCase();

        for (Buku b : daftarBuku) {
            boolean cocok = b.isbn.toLowerCase().contains(kata)
                    || b.judul.toLowerCase().contains(kata)
                    || b.penulis.toLowerCase().contains(kata)
                    || b.genre.toLowerCase().contains(kata);

            if (cocok) {
                model.addRow(new Object[]{
                    b.isbn, b.judul, b.penulis, b.genre
                });
            }
        }

        if (lblTotal != null) {
            lblTotal.setText(String.valueOf(daftarBuku.size()));
        }

        if (lblGenre != null) {
            long jumlahGenre = daftarBuku.stream()
                    .map(b -> b.genre.toLowerCase())
                    .distinct()
                    .count();

            lblGenre.setText(String.valueOf(jumlahGenre));
        }
    }

    // Dashboard
    private void tampilkanHalaman(String halaman) {
        if (halaman.equals("Dashboard")) {
            panelKonten.removeAll();
            panelKonten.setLayout(new BorderLayout(0, 18));

            JLabel sambutan = new JLabel(
                    "<html><h1>Selamat Datang di uBiblio!</h1>"
                    + "<p style='font-size:12px;color:#64748b'>"
                    + "Kelola dan temukan koleksi buku pribadi kamu "
                    + "dengan mudah.</p></html>"
            );
            sambutan.setBorder(new EmptyBorder(10, 0, 10, 0));

            JPanel isi = new JPanel(new GridLayout(1, 2, 15, 15));
            isi.setBackground(BG);

            JPanel kartu1 = buatKartu("Total Buku", "0");
            JPanel kartu2 = buatKartu("Total Genre", "0");

            isi.add(kartu1);
            isi.add(kartu2);

            JPanel tengah = new JPanel(new BorderLayout(0, 15));
            tengah.setBackground(BG);
            tengah.add(sambutan, BorderLayout.NORTH);
            tengah.add(isi, BorderLayout.CENTER);

            JButton bukaKoleksi = buatTombol(
                    "Buka Koleksi Buku", BLUE);

            bukaKoleksi.addActionListener(e -> {
                halamanAktif = "Koleksi Buku";
                lblHalaman.setText("Koleksi Buku");
                perbaruiWarnaMenu();
                tampilkanHalaman("Koleksi Buku");
            });

            JPanel bawah = new JPanel(new FlowLayout(FlowLayout.LEFT));
            bawah.setBackground(BG);
            bawah.add(bukaKoleksi);

            panelKonten.add(tengah, BorderLayout.NORTH);
            panelKonten.add(bawah, BorderLayout.CENTER);

            JLabel total1 = (JLabel) kartu1.getClientProperty("nilai");
            JLabel total2 = (JLabel) kartu2.getClientProperty("nilai");

            total1.setText(String.valueOf(daftarBuku.size()));

            long genre = daftarBuku.stream()
                    .map(b -> b.genre.toLowerCase())
                    .distinct()
                    .count();
            total2.setText(String.valueOf(genre));

        } else {
            buatHalamanKoleksi();
        }

        panelKonten.revalidate();
        panelKonten.repaint();
    }

    private void perbaruiWarnaMenu() {
        Component[] komponen =
                ((JPanel) getContentPane().getComponent(0)).getComponents();

        for (Component c : komponen) {
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                boolean aktif = b.getText().trim().equals(halamanAktif);
                b.setBackground(aktif ? BLUE : NAVY);
            }
        }
    }

    // Data contoh supaya tabel tidak kosong saat pertama dibuka
    private void tambahDataContoh() {
        daftarBuku.add(new Buku(
                "978001", "Laskar Pelangi",
                "Andrea Hirata", "Novel"));

        daftarBuku.add(new Buku(
                "978002", "Bumi",
                "Tere Liye", "Fantasi"));

        daftarBuku.add(new Buku(
                "978003", "Pemrograman Java",
                "Abdul Kadir", "Teknologi"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Tetap gunakan tampilan bawaan Swing
            }

            new Main().setVisible(true);
        });
    }
}

