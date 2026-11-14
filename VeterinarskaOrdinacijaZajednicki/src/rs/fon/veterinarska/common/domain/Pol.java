package rs.fon.veterinarska.common.domain;

/**
 * Vrednosti u bazi odgovaraju cirilicnim nazivima iz seminarskog rada.
 *
 * @author nenad
 */
public enum Pol {
    MUZJAK("мужјак"),
    ZENKA("женка"),
    NEPOZNAT("непознат");

    private final String vrednostUBazi;

    Pol(String vrednostUBazi) {
        this.vrednostUBazi = vrednostUBazi;
    }

    public String getVrednostUBazi() {
        return vrednostUBazi;
    }

    public static Pol izVrednostiUBazi(String vrednost) {
        for (Pol pol : values()) {
            if (pol.vrednostUBazi.equals(vrednost)) {
                return pol;
            }
        }
        throw new IllegalArgumentException("Nepoznata vrednost pola: " + vrednost);
    }

    @Override
    public String toString() {
        return vrednostUBazi;
    }
}
