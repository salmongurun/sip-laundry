package siplaundry.util;

public class MessageUtil {
    public static String verifyMessGen(String code) {
        StringBuilder builder = new StringBuilder();

        builder.append("Halo, Berikut adalah kode verifikasi SIP Laundry: \n");
        builder.append(code);
        builder.append("\nJANGAN BERIKAN KODE INI KEPADA SIAPAPUN");

        return builder.toString();
    }

    public static String retardMessGen(int day, String reason) {
        StringBuilder builder = new StringBuilder("Mohon maaf atas ketidaknyamanan anda\n");

        builder.append("Memberitahukan bahwa pesanan laundry anda tidak dapat kami selesaikan tepat waktu dikarenakan ");

        if(reason.isBlank()) {
            builder.append("**terdapat kendala**\n");
        } else {
            builder.append(reason + "\n");
        }

        builder.append("Pesanan tersebut akan terlambat " + day + " hari, terima kasih");

        return builder.toString();
    }
}
