package puppy.code;

public enum Hidratacion {
    NORMAL,
    FATIGADA {
        @Override float factor(float segundos) {
            return 1 - Math.min(0.6f, (segundos - 4) * 0.15f);
        }
    };

    float factor(float segundos) { return 1; }
}
