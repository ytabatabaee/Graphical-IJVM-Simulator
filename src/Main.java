public class Main {
    public static void main(String[] args) {
        String a = "mama";
        String b = "baba";
        String c = b;
        b = "qw";
        Decoder decoder = new Decoder();
        boolean[] ii = decoder.decode("011");
        System.out.println(c);
        System.out.println(ii[3]);
    }
}
