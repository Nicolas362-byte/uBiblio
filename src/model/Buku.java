
package model;

public class Buku {
    private String isbn;
    private String judul;
    private String penulis;
    private String genre;

    public Buku(String isbn, String judul,
                String penulis, String genre) {
        this.isbn = isbn;
        this.judul = judul;
        this.penulis = penulis;
        this.genre = genre;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getJudul() {
        return judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public String getGenre() {
        return genre;
    }
}
