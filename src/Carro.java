/**
 * Modelo que representa um Carro.
 */
public class Carro {

    private String placa;
    private String cor;
    private String modelo;
    private int    ano;
    private String cpfPessoa;   // chave estrangeira

    public Carro() {}

    public Carro(String placa, String cor, String modelo, int ano, String cpfPessoa) {
        this.placa     = placa;
        this.cor       = cor;
        this.modelo    = modelo;
        this.ano       = ano;
        this.cpfPessoa = cpfPessoa;
    }

    public String getPlaca()     { return placa; }
    public String getCor()       { return cor; }
    public String getModelo()    { return modelo; }
    public int    getAno()       { return ano; }
    public String getCpfPessoa() { return cpfPessoa; }

    public void setPlaca(String placa)         { this.placa     = placa; }
    public void setCor(String cor)             { this.cor       = cor; }
    public void setModelo(String modelo)       { this.modelo    = modelo; }
    public void setAno(int ano)                { this.ano       = ano; }
    public void setCpfPessoa(String cpfPessoa) { this.cpfPessoa = cpfPessoa; }

    @Override
    public String toString() {
        return "Carro{placa='" + placa + "', modelo='" + modelo + "', ano=" + ano + "}";
    }
}
