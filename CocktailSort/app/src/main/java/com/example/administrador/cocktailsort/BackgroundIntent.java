package com.example.administrador.cocktailsort;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class BackgroundIntent extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "BackgroundIntent";

    public BackgroundIntent() {
        super(BackgroundIntent.class.getName());
    }

    public static int[] cocktailSort(int[] numbers)
    {
        boolean swapped = true;
        int i = 0;
        int j = numbers.length - 1;
        while(i < j && swapped)
        {
            swapped = false;
            for(int k = i; k < j; k++)
            {
                if(numbers[k] > numbers[k + 1])
                {
                    int temp = numbers[k];
                    numbers[k] = numbers[k + 1];
                    numbers[k + 1] = temp;
                    swapped = true;
                }
            }
            j--;
            if(swapped)
            {
                swapped = false;
                for(int k = j; k > i; k--)
                {
                    if(numbers[k] < numbers[k - 1])
                    {
                        int temp = numbers[k];
                        numbers[k] = numbers[k - 1];
                        numbers[k - 1] = temp;
                        swapped = true;
                    }
                }
            }
            i++;
        }
        return numbers;
    }
        private void Sort(){
            int n = 150;
            int arreglo[] = new int[n];

            Random random = new Random();
            for (int i = 0; i < arreglo.length; i++){
                arreglo[i] = random.nextInt(MAX_VALUE)  ;
            }
            cocktailSort(arreglo);

        }

        static final int MAX_VALUE = 100000;
        int data[] = new int[MAX_VALUE];


    public void generarNuevoArreglo() {
        Random random = new Random();

        for (int i = 0; i < data.length; i++){
            data[i] = random.nextInt(MAX_VALUE);
        }
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        Bundle bundle = new Bundle();

 /* Service Started */
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);
        try {

            long start = System.nanoTime();
            generarNuevoArreglo();
            Sort();
            long end = System.nanoTime();

            long elapsedTime = end - start;
            String res = "trascurrieron: " + elapsedTime + "nano seconds\n" +
                    "lo cual es " + TimeUnit.NANOSECONDS.toSeconds(elapsedTime) + " segundos";

            bundle.putString("exectime",res);
        }
        catch (Error err)
        {
            bundle.putString(Intent.EXTRA_TEXT, err.getMessage());
            receiver.send(STATUS_ERROR, bundle);
        }

        /* Status Finished */
        receiver.send(STATUS_FINISHED, bundle);
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }
}