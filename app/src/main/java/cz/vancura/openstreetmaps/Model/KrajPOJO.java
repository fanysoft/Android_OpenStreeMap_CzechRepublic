package cz.vancura.openstreetmaps.Model;

public class KrajPOJO {

    String krajFileName;
    String krajNazev;
    int krajColor;

    public KrajPOJO(String krajFileName, String krajNazev, int krajColor) {
        this.krajFileName = krajFileName;
        this.krajNazev = krajNazev;
        this.krajColor = krajColor;
    }

    public String getKrajNazev() {
        return krajNazev;
    }

    public void setKrajNazev(String krajNazev) {
        this.krajNazev = krajNazev;
    }

    public String getKrajFileName() {
        return krajFileName;
    }

    public void setKrajFileName(String krajFileName) {
        this.krajFileName = krajFileName;
    }

    public int getKrajColor() {
        return krajColor;
    }

    public void setKrajColor(int krajColor) {
        this.krajColor = krajColor;
    }
}
