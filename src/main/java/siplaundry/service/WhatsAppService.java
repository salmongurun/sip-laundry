package siplaundry.service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import siplaundry.entity.WhatsappMessage;
import siplaundry.util.ConfigUtil;

public class WhatsAppService {
    private final String BASE_URL = ConfigUtil.get("gateway.url");

    private VerificationEndpoints verifEndpoint;

    public WhatsAppService() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(this.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        this.verifEndpoint = retrofit.create(VerificationEndpoints.class);
    }

    public void sendVerification(WhatsappMessage message) {
        Call<Void> call = verifEndpoint.storeMessage(message);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("Pesan berhasil dikirimkan ke " + message.getTarget());
            };

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Gagal mengirimkan pesan, error: " + t.getMessage());
            }
        });
    }
}

interface VerificationEndpoints {
    @POST("messages")
    Call<Void> storeMessage(@Body WhatsappMessage message);
}
