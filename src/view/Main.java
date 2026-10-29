
package view;

import controller.BukuController;
import model.Buku;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class Main extends JFrame {

    private final Color NAVY = new Color(23, 54, 93);
    private final Color BLUE = new Color(39, 100, 165);
    private final Color BG = new Color(245, 248, 252);
    private final Color WHITE = Color.WHITE;

    private final BukuController controller =
            new BukuController();

    private DefaultTableModel model;
    private JTable tabel;
    private JTextField txtCari;
    private JLabel lblHalaman;
    private JLabel lblTotal;
    private JLabel lblGenre;
    private JPanel konten;
    private JButton btnDashboard, btnKoleksi;

    public Main() {
        setTitle("uBiblio - Personal Library Management");
        setSize(1100, 700);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        buatSidebar();
        buatAreaUtama();
        tampilkanDashboard();
    }

    private void buatSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(NAVY);
        sidebar.setLayout(new BoxLayout(sidebar,
                BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(25, 15, 20, 15));

        JLabel logo = new JLabel("  uBiblio");
        logo.setForeground(WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 27));

        JLabel sub = new JLabel("  PERSONAL LIBRARY");
        sub.setForeground(new Color(190, 210, 235));

        btnDashboard = menu("Dashboard");
        btnKoleksi = menu("Koleksi Buku");

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(sub);
        sidebar.add(Box.createVerticalStrut(45));
        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnKoleksi);
        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel(
                "<html><center>uBiblio Desktop<br>"
                + "Java Swing Edition</center></html>");
        footer.setForeground(new Color(190, 210, 235));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(footer);

        add(sidebar, BorderLayout.WEST);
    }

    private JButton menu(String nama) {
        JButton b = new JButton("  " + nama);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        b.setPreferredSize(new Dimension(190, 45));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setForeground(WHITE);
        b.setBackground(NAVY);
        b.setBorder(new EmptyBorder(10, 12, 10, 8));
        b.setFocusPainted(false);

        b.addActionListener(e -> {
            warnaMenu(nama);
            lblHalaman.setText(nama);

            if (nama.equals("Dashboard")) {
                tampilkanDashboard();
            } else {
                tampilkanKoleksi();
            }
        });

        return b;
    }

    private void warnaMenu(String nama) {
        btnDashboard.setBackground(
                nama.equals("Dashboard") ? BLUE : NAVY);
        btnKoleksi.setBackground(
                nama.equals("Koleksi Buku") ? BLUE : NAVY);
    }

    private void buatAreaUtama() {
        JPanel kanan = new JPanel(new BorderLayout());
        kanan.setBackground(BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(new EmptyBorder(20, 28, 20, 28));

        lblHalaman = new JLabel("Dashboard");
        lblHalaman.setFont(
                new Font("SansSerif", Font.BOLD, 24));
        lblHalaman.setForeground(NAVY);

        JLabel profil = new JLabel("Perpustakaan Pribadi");
        header.add(lblHalaman, BorderLayout.WEST);
        header.add(profil, BorderLayout.EAST);

        konten = new JPanel(new BorderLayout());
        konten.setBackground(BG);
        konten.setBorder(new EmptyBorder(24, 28, 24, 28));

        kanan.add(header, BorderLayout.NORTH);
        kanan.add(konten, BorderLayout.CENTER);
        add(kanan, BorderLayout.CENTER);
    }

    private JPanel kartu(String judul, JLabel nilai) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225, 233, 242)),
                new EmptyBorder(15, 16, 15, 16)));

        JLabel label = new JLabel(judul);
        label.setForeground(new Color(100, 116, 139));

        nilai.setFont(new Font("SansSerif", Font.BOLD, 24));
        nilai.setForeground(NAVY);

        p.add(label, BorderLayout.NORTH);
        p.add(nilai, BorderLayout.CENTER);
        return p;
    }

    private JButton tombol(String teks, Color warna) {
        JButton b = new JButton(teks);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setForeground(WHITE);
        b.setBackground(warna);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 15, 10, 15));
        return b;
    }

    private void tampilkanDashboard() {
        konten.removeAll();

        JLabel sambutan = new JLabel(
                "<html><h1>Selamat Datang di uBiblio!</h1>"
                + "<p style='font-size:12px;color:#64748b'>"
                + "Kelola koleksi buku pribadi dengan mudah."
                + "</p></html>");

        lblTotal = new JLabel("0");
        lblGenre = new JLabel("0");

        JPanel statistik = new JPanel(
                new GridLayout(1, 2, 15, 15));
        statistik.setBackground(BG);
        statistik.add(kartu("Total Buku", lblTotal));
        statistik.add(kartu("Total Genre", lblGenre));

        JButton buka = tombol("Buka Koleksi Buku", BLUE);
        buka.addActionListener(e -> {
            warnaMenu("Koleksi Buku");
            lblHalaman.setText("Koleksi Buku");
            tampilkanKoleksi();
        });

        JPanel isi = new JPanel(new BorderLayout(0, 20));
        isi.setBackground(BG);
        isi.add(sambutan, BorderLayout.NORTH);
        isi.add(statistik, BorderLayout.CENTER);

        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bawah.setBackground(BG);
        bawah.add(buka);

        konten.add(isi, BorderLayout.NORTH);
        konten.add(bawah, BorderLayout.CENTER);

        perbaruiStatistik();
        konten.revalidate();
        konten.repaint();
    }

    private void tampilkanKoleksi() {
        konten.removeAll();
        konten.setLayout(new BorderLayout(0, 15));

        JPanel atas = new JPanel(new BorderLayout());
        atas.setBackground(BG);

        JLabel judul = new JLabel("Kelola Koleksi Buku");
        judul.setFont(new Font("SansSerif", Font.BOLD, 19));

        JLabel deskripsi = new JLabel(
                "Tambah, cari, ubah, dan hapus koleksi buku.");
        deskripsi.setForeground(new Color(110, 125, 145));

        JPanel teks = new JPanel();
        teks.setBackground(BG);
        teks.setLayout(new BoxLayout(teks, BoxLayout.Y_AXIS));
        teks.add(judul);
        teks.add(Box.createVerticalStrut(5));
        teks.add(deskripsi);

        atas.add(teks, BorderLayout.NORTH);

        JPanel ringkasan = new JPanel(
                new GridLayout(1, 2, 15, 0));
        ringkasan.setBackground(BG);

        lblTotal = new JLabel("0");
        lblGenre = new JLabel("0");
        ringkasan.add(kartu("Total Koleksi", lblTotal));
        ringkasan.add(kartu("Jumlah Genre", lblGenre));

        JPanel isiAtas = new JPanel(new BorderLayout(0, 15));
        isiAtas.setBackground(BG);
        isiAtas.add(atas, BorderLayout.NORTH);
        isiAtas.add(ringkasan, BorderLayout.CENTER);

        JPanel area = new JPanel(new BorderLayout(0, 12));
        area.setBackground(WHITE);
        area.setBorder(new EmptyBorder(15, 15, 15, 15));

        txtCari = new JTextField();
        txtCari.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtCari.setToolTipText("Cari ISBN, judul, penulis, atau genre");

        JButton tambah = tombol("+ Tambah Buku", BLUE);
        JPanel pencarian = new JPanel(new BorderLayout(10, 0));
        pencarian.setBackground(WHITE);
        pencarian.add(txtCari, BorderLayout.CENTER);
        pencarian.add(tambah, BorderLayout.EAST);

        model = new DefaultTableModel(
                new String[]{"ISBN", "Judul Buku",
                    "Penulis", "Genre"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabel = new JTable(model);
        tabel.setRowHeight(35);
        tabel.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        tabel.setShowVerticalLines(false);
        tabel.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12));
        tabel.getTableHeader().setBackground(
                new Color(235, 242, 250));
        tabel.getTableHeader().setForeground(NAVY);

        JScrollPane scroll = new JScrollPane(tabel);

        JButton edit = tombol("Edit Buku",
                new Color(225, 155, 35));
        JButton hapus = tombol("Hapus Buku",
                new Color(190, 55, 65));

        JPanel bawah = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bawah.setBackground(WHITE);
        bawah.add(edit);
        bawah.add(hapus);

        area.add(pencarian, BorderLayout.NORTH);
        area.add(scroll, BorderLayout.CENTER);
        area.add(bawah, BorderLayout.SOUTH);

        konten.add(isiAtas, BorderLayout.NORTH);
        konten.add(area, BorderLayout.CENTER);

        tambah.addActionListener(e -> dialogBuku(null));
        edit.addActionListener(e -> editBuku());
        hapus.addActionListener(e -> hapusBuku());

        txtCari.getDocument().addDocumentListener(
                new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                muatTabel();
            }

            public void removeUpdate(DocumentEvent e) {
                muatTabel();
            }

            public void changedUpdate(DocumentEvent e) {
                muatTabel();
            }
        });

        muatTabel();
        konten.revalidate();
        konten.repaint();
    }

    private void muatTabel() {
        if (model == null || txtCari == null) {
            return;
        }

        model.setRowCount(0);

        try {
            List<Buku> daftar =
                    controller.cari(txtCari.getText().trim());

            for (Buku b : daftar) {
                model.addRow(new Object[]{
                    b.getIsbn(), b.getJudul(),
                    b.getPenulis(), b.getGenre()
                });
            }

            perbaruiStatistik();

        } catch (SQLException e) {
            tampilkanError(e);
        }
    }

    private void perbaruiStatistik() {
        try {
            List<Buku> daftar = controller.cari("");
            long genre = daftar.stream()
                    .map(Buku::getGenre)
                    .map(String::toLowerCase)
                    .distinct().count();

            if (lblTotal != null) {
                lblTotal.setText(
                        String.valueOf(daftar.size()));
            }
            if (lblGenre != null) {
                lblGenre.setText(String.valueOf(genre));
            }

        } catch (SQLException e) {
            tampilkanError(e);
        }
    }

    private void dialogBuku(String isbnLama) {
        boolean edit = isbnLama != null;
        Buku lama = null;

        if (edit) {
            try {
                for (Buku b : controller.cari("")) {
                    if (b.getIsbn().equals(isbnLama)) {
                        lama = b;
                        break;
                    }
                }
            } catch (SQLException e) {
                tampilkanError(e);
                return;
            }
        }

        JTextField isbn = new JTextField(
                lama == null ? "" : lama.getIsbn());
        JTextField judul = new JTextField(
                lama == null ? "" : lama.getJudul());
        JTextField penulis = new JTextField(
                lama == null ? "" : lama.getPenulis());

        JComboBox<String> genre = new JComboBox<>(
                new String[]{"Novel", "Pendidikan",
                    "Teknologi", "Fantasi", "Sejarah",
                    "Komik", "Lainnya"});

        if (lama != null) {
            genre.setSelectedItem(lama.getGenre());
            if (genre.getSelectedItem() == null) {
                genre.addItem(lama.getGenre());
                genre.setSelectedItem(lama.getGenre());
            }
        }

        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.setPreferredSize(new Dimension(350, 260));
        form.add(new JLabel("ISBN:"));
        form.add(isbn);
        form.add(new JLabel("Judul buku:"));
        form.add(judul);
        form.add(new JLabel("Penulis:"));
        form.add(penulis);
        form.add(new JLabel("Genre:"));
        form.add(genre);

        int hasil = JOptionPane.showConfirmDialog(
                this, form,
                edit ? "Edit Data Buku" : "Tambah Buku Baru",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (hasil != JOptionPane.OK_OPTION) {
            return;
        }

        String id = isbn.getText().trim();
        String nama = judul.getText().trim();
        String pengarang = penulis.getText().trim();
        String kategori = (String) genre.getSelectedItem();

        if (id.isEmpty() || nama.isEmpty()
                || pengarang.isEmpty() || kategori == null) {
            JOptionPane.showMessageDialog(this,
                    "Semua data wajib diisi.");
            return;
        }

        Buku buku = new Buku(id, nama, pengarang, kategori);

        try {
            boolean sukses = edit
                    ? controller.edit(isbnLama, buku)
                    : controller.tambah(buku);

            if (sukses) {
                JOptionPane.showMessageDialog(this,
                        edit ? "Data buku diperbarui!"
                             : "Buku berhasil ditambahkan!");
                muatTabel();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Data tidak ditemukan atau gagal diubah.");
            }

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                JOptionPane.showMessageDialog(this,
                        "ISBN sudah digunakan.",
                        "ISBN duplikat",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                tampilkanError(e);
            }
        }
    }

    private void editBuku() {
        int baris = tabel.getSelectedRow();

        if (baris == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih buku yang ingin diedit.");
            return;
        }

        String isbn = model.getValueAt(baris, 0).toString();
        dialogBuku(isbn);
    }

    private void hapusBuku() {
        int baris = tabel.getSelectedRow();

        if (baris == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih buku yang ingin dihapus.");
            return;
        }

        String isbn = model.getValueAt(baris, 0).toString();
        String judul = model.getValueAt(baris, 1).toString();

        int konfirmasi = JOptionPane.showConfirmDialog(
                this, "Hapus buku \"" + judul + "\"?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (controller.hapus(isbn)) {
                JOptionPane.showMessageDialog(this,
                        "Buku berhasil dihapus.");
                muatTabel();
            }
        } catch (SQLException e) {
            tampilkanError(e);
        }
    }

    private void tampilkanError(SQLException e) {
        JOptionPane.showMessageDialog(this,
                "Terjadi kesalahan database:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            new Main().setVisible(true);
        });
    }
}

