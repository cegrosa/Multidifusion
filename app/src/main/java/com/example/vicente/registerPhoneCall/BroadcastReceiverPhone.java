package com.example.vicente.registerPhoneCall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BroadcastReceiverPhone extends BroadcastReceiver {

    private static int lastSte = TelephonyManager.CALL_STATE_IDLE;
    private static boolean incomingCall;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            int ste = -1;

            //  El estado (IDLE, RINGING, OFFHOOK), se obtendra del 'intent'
            //   del 'onReceive' a través del paquete 'TelephonyManager'
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            //  Y lo mismo con el número de teléfono.
            String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            //  Se utiliza un 'switch' para determinar
            //   el estado en formato númerico
            switch (state) {
                case "RINGING":
                    ste = 1;
                    incomingCall = true;
                    break;
                case "OFFHOOK":
                    ste = 2;
                    if (lastSte != 1) {
                        incomingCall = false;
                    }
                    break;
                case "IDLE":
                    ste = 0;
                    break;
            }

            lastSte = ste;

            //  Se crea un 'intent', para inicializar el servicio,
            //   que mandara las peticiones HTTP al servidor
            Intent i = new Intent(context, ServiceRestClient.class);

            //  Se metera en el intent el estado (en formato de tipo 'String')
            i.putExtra("state", ste + "");

            //  Se añade el número de teléfono
            i.putExtra("numberphone", phoneNumber);

            //  Y tambien si la llamada es saliente/entrante
            i.putExtra("incoming", incomingCall + "");

            //  Se iniciara el servicio
            context.startService(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

