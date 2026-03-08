package sistema;

public class DatosPartido {
    private String ronda;
    private Ciudad ciudad;
    private String estadio;
    private int goles1;
    private int goles2;

    // Constructor completo
    public DatosPartido(String ronda, Ciudad ciudad, String estadio, int goles1, int goles2) {
        this.ronda = ronda.trim().toUpperCase();
        this.ciudad = ciudad;
        this.estadio = estadio.trim().toUpperCase();
        this.goles1 = goles1;
        this.goles2 = goles2;
    }

    public String getRonda() {
        return ronda;
    }

    public void setRonda(String ronda) {
        this.ronda = ronda.trim().toUpperCase();
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstadio() {
        return estadio;
    }

    public void setEstadio(String estadio) {
        this.estadio = estadio.trim().toUpperCase();
    }

    public int getGoles1() {
        return goles1;
    }

    public void setGoles1(int goles1) {
        this.goles1 = goles1;
    }

    public int getGoles2() {
        return goles2;
    }

    public void setGoles2(int goles2) {
        this.goles2 = goles2;
    }
}
