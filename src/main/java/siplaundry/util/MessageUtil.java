package siplaundry.util;

public class MessageUtil {
    public static String verifyMessGen(String code) {
        StringBuilder builder = new StringBuilder();

        builder.append("Halo, Berikut adalah kode verifikasi SIP Laundry: \n");
        builder.append(code);
        builder.append("\nJANGAN BERIKAN KODE INI KEPADA SIAPAPUN");

        return builder.toString();
    }
}
