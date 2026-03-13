import java.util.HashSet;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        HyperLogLog hll = new HyperLogLog(10); // 2^10 = 1024 bucket

        HashSet<Integer> realSet = new HashSet<>();

        Random random = new Random();

        for (int i = 0; i < 100000; i++) {

            int value = random.nextInt(1000000);

            realSet.add(value);

            hll.add(String.valueOf(value));
        }

        double estimate = hll.estimate();

        System.out.println("Gerçek Cardinality : " + realSet.size());
        System.out.println("HLL Tahmini        : " + (long) estimate);
    }
}
