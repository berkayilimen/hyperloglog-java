public class MergeTest {

    public static void main(String[] args) {

        HyperLogLog h1 = new HyperLogLog(10);
        HyperLogLog h2 = new HyperLogLog(10);

        for(int i=0;i<50000;i++){
            h1.add("A"+i);
        }

        for(int i=25000;i<75000;i++){
            h2.add("A"+i);
        }

        h1.merge(h2);

        System.out.println("Birleştirilmiş tahmin: " + h1.estimate());
    }
}
