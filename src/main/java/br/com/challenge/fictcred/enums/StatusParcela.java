package br.com.challenge.fictcred.enums;

public enum StatusParcela {
    APROVADA(1),
    REPROVADA(2);

    private int valorInteiro;

    StatusParcela(int valorInteiro) {
        this.valorInteiro = valorInteiro;
    }

    public int getValorInteiro() {
        return valorInteiro;
    }

    public void setValorInteiro(int valorInteiro) {
        this.valorInteiro = valorInteiro;
    }

    public static StatusParcela getStatusByInt(int i) {
        for (StatusParcela e : values()) {
            if (e.getValorInteiro() == i) {
                return e;
            }
        }
        return null;
    }
}
