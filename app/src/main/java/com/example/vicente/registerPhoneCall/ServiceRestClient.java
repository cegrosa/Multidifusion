package com.example.vicente.registerPhoneCall;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceRestClient extends Service {

    //  En esta 'ArrayList', se almacenaran las llamadas
    //   del 'getPhoneCalls'
    ArrayList<PhoneCall> phoneCalls = new ArrayList<>();

    public ServiceRestClient() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags,
                              int startId) {

        //  Se recogen los datos que han sido pasados
        //   por el intent
        //  Al estado se convierte en un 'Integer'
        //   y el 'incomingCall' se convierte en 'Boolean'
        int state = Integer.parseInt(intent.getStringExtra("state"));
        String phone = intent.getStringExtra("numberphone");
        boolean incomingCall = Boolean.parseBoolean(intent.getStringExtra("incoming"));

        //  Se crea e inicializa el objeto de 'Retrofit',
        //   pansandole la URL del servidor y
        //   el convertidor de objetos que utilizara (en este caso JSON),
        //   y finalmente se construira
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //  Se creara un objeto de la interfaz 'RestClient',
        //   el cual se inicializara con el objeto 'Retrofit' ya creado
        //   con la interfaz ya dicha
        RestClient client = retrofit.create(RestClient.class);

        //  Se formara un 'String' con la fecha y hora actual
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

        //  Se creara un objeto de la clase 'PhoneCall'
        PhoneCall ll = new PhoneCall(state, phone, date);

        //  Si la llamada es de tipo 'OFFHOOK',
        //   se le aplicara los nanosegundos actuales
        if (state != 0) {
            double duration = System.nanoTime();
            ll.setDuration(duration);
        }

        //  Se obtiene la lista de llamadas a partir del método 'getPhoneCalls',
        //   que se almacenaran en la ArrayList 'phoneCalls'
        getPhoneCalls(client, incomingCall);

        //  Se obtiene el tamaño de la array
        int size = phoneCalls.size();

        //  Y si no es igual a 0,
        //   y si la duracion de la última llamada
        //   no es '-1' y el estado de la llamada actual es 'IDLE',
        //   sobrescribira la duración de la última llamada,
        //   por la duración real de la llamada
        if (size != 0) {
            if (phoneCalls.get(size - 1).getDuration() > -1 &&
                    state == 0) {
                PhoneCall llPut = phoneCalls.get(size - 1);
                double dur = ((System.nanoTime() - llPut.getDuration()) / 1000000000.0);
                llPut.setDuration(dur);

                putPhoneCall(size, llPut, client, incomingCall);
            }
        }

        //  Por último, se mandara la llamada
        //   via JSON al servidor
        postPhoneCall(ll, client, incomingCall);

        return START_REDELIVER_INTENT;
    }

    //  Este método sobrescribira la ArrayList 'phoneCalls'. (GET)
    //  Se debe de pasar como parametro un objeto de la interfaz 'RestClient'
    public void getPhoneCalls(RestClient cliente, boolean incoming) {
        Call<ArrayList<PhoneCall>> call;

        if (incoming) {
            call = cliente.getIncomingPhoneCalls();
        } else {
            call = cliente.getOutcomingPhoneCalls();
        }

        call.enqueue(new Callback<ArrayList<PhoneCall>>() {
            @Override
            public void onResponse(Call<ArrayList<PhoneCall>> call, Response<ArrayList<PhoneCall>> response) {
                phoneCalls = response.body();
                Log.v("XYZYX", "Phonecalls: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<ArrayList<PhoneCall>> call, Throwable t) {
                Log.v("XYZYX", t.getLocalizedMessage());
            }
        });
    }

    //  Este método subira la llamada al servidor. (POST)
    //  Se debe de pasar como parametros, la llamada a subir y el objeto de la interfaz 'RestClient'
    public void postPhoneCall(PhoneCall ll, RestClient cliente, boolean incoming) {
        Call<PhoneCall> call;
        if (incoming) {
            call = cliente.postIncomingPhoneCall(ll);
        } else {
            call = cliente.postOutcomingPhoneCall(ll);
        }
        call.enqueue(new Callback<PhoneCall>() {
            @Override
            public void onResponse(Call<PhoneCall> call,
                                   Response<PhoneCall> response) {
                PhoneCall c = response.body();
                Log.v("XYZYX", "PhoneCall: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<PhoneCall> call, Throwable t) {
                Log.v("XYZYX", t.getLocalizedMessage());
            }
        });
    }

    //  Este método permitira actualizar la llamada. (PUT)
    //  Se debe de pasar como parametros, el id de la llamada a actualizar,
    //   la llamada a modificar y el objeto de la interfaz 'RestClient'
    public void putPhoneCall(long id, PhoneCall llamada, RestClient cliente, boolean incoming) {
        Call<PhoneCall> call;

        if (incoming) {
            call = cliente.updateIncomingPhoneCall(id,
                    llamada.getDuration(),
                    llamada.getNumberPhone(),
                    llamada.getState(),
                    llamada.getTime());
        } else {
            call = cliente.updateOutcomingPhoneCall(id,
                    llamada.getDuration(),
                    llamada.getNumberPhone(),
                    llamada.getState(),
                    llamada.getTime());
        }

        call.enqueue(new Callback<PhoneCall>() {
            @Override
            public void onResponse(Call<PhoneCall> call,
                                   Response<PhoneCall> response) {
                PhoneCall c = response.body();
                Log.v("XYZYX", "LlamadaPut: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<PhoneCall> call, Throwable t) {
                Log.v("XYZYX", t.getLocalizedMessage());
            }
        });
    }
}
