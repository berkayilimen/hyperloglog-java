import java.nio.charset.StandardCharsets;

public class HyperLogLog {

    private int p;                // bucket bit sayısı
    private int m;                // bucket sayısı = 2^p
    private byte[] registers;     // register array

    public HyperLogLog(int p) {
        this.p = p;
        this.m = 1 << p;
        this.registers = new byte[m];
    }

    // Veri ekleme
    public void add(String value) {

        long hash = murmurHash64(value);

        int bucket = (int) (hash >>> (64 - p)); // ilk p bit -> bucket

        long remaining = (hash << p) | (1L << (p - 1));

        int leadingZeros = Long.numberOfLeadingZeros(remaining) + 1;

        registers[bucket] = (byte) Math.max(registers[bucket], leadingZeros);
    }

    // Cardinality tahmini
    public double estimate() {

        double alpha;

        if (m == 16) alpha = 0.673;
        else if (m == 32) alpha = 0.697;
        else if (m == 64) alpha = 0.709;
        else alpha = 0.7213 / (1 + 1.079 / m);

        double sum = 0.0;

        for (int i = 0; i < m; i++) {
            sum += Math.pow(2.0, -registers[i]);
        }

        double estimate = alpha * m * m / sum;

        // Small range correction
        int zeros = 0;
        for (int i = 0; i < m; i++) {
            if (registers[i] == 0) zeros++;
        }

        if (estimate <= (5.0 / 2.0) * m && zeros != 0) {
            estimate = m * Math.log((double) m / zeros);
        }

        // Large range correction
        double pow32 = Math.pow(2, 32);
        if (estimate > (1.0 / 30.0) * pow32) {
            estimate = -pow32 * Math.log(1 - (estimate / pow32));
        }

        return estimate;
    }

    // Merge işlemi
    public void merge(HyperLogLog other) {

        if (this.m != other.m) {
            throw new IllegalArgumentException("Bucket sayıları aynı olmalı!");
        }

        for (int i = 0; i < m; i++) {
            this.registers[i] = (byte) Math.max(this.registers[i], other.registers[i]);
        }
    }

    // MurmurHash3 (basit 64 bit versiyon)
    private long murmurHash64(String value) {

        byte[] data = value.getBytes(StandardCharsets.UTF_8);

        long h = 1125899906842597L;

        for (byte b : data) {
            h = 31 * h + b;
        }

        return h;
    }
}
