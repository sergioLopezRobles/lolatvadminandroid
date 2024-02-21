package com.luzatuvida.lolatvadminandroid.impresoraBluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothSocket;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.luzatuvida.lolatvadminandroid.R;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.Seguridad;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ImpresoraBluetooth {

    /*
    Identificador error del 103 al 110
     */

    Fragment fragmento;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;

    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    public static byte[] format = { 27, 33, 0 };

    Seguridad seguridad;
    Global global;

    public ImpresoraBluetooth(Fragment fragmento) {
        this.fragmento = fragmento;
        seguridad = new Seguridad(fragmento);
        global = new Global(fragmento);
    }

    public boolean findBluetoothDevice() {

        boolean respuesta = false;

        try {

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(bluetoothAdapter == null){
                //Bluetooth no funciona
                Toast.makeText(fragmento.getActivity(), "El Bluetooth del dispositivo no funciona (Contactar con el administrador)", Toast.LENGTH_LONG).show();
            }else {
                //Bluetooth funciona correctamente

                if (!bluetoothAdapter.isEnabled()) {
                    //Bluetooth desactivado
                    Toast.makeText(fragmento.getActivity(), "Favor de encender el Bluetooth", Toast.LENGTH_LONG).show();
                } else {
                    //Bluetooth activado

                    String valorImpresoraSeleccionada = seguridad.leerSharedPreferences("valorImpresoraSeleccionada");
                    if (valorImpresoraSeleccionada.length() == 0) {
                        Toast.makeText(fragmento.getActivity(), "Favor de agregar el dispositivo bluetooth (Ir a configuración)", Toast.LENGTH_LONG).show();
                    } else {

                        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

                        if(pairedDevice.size() > 0){
                            for(BluetoothDevice pairedDev : pairedDevice){
                                //Log.e("device: ", pairedDev.getName());
                                if(pairedDev.getName().equals(valorImpresoraSeleccionada)){
                                    bluetoothDevice = pairedDev;
                                    break;
                                }
                            }
                            if(bluetoothDevice == null) {
                                Toast.makeText(fragmento.getActivity(), "El bluetooth no funciona correctamente", Toast.LENGTH_LONG).show();
                            }else {
                                closedBluetoothPrinter();
                                openBluetoothPrinter();
                                respuesta = true;
                            }
                        }

                    }
                }
            }

        }catch (Exception e) {
            global.escribirError(e, 103);
            e.printStackTrace();
        }

        return respuesta;

    }

    private void openBluetoothPrinter() {

        try {

            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            beginListenData();
            Toast.makeText(fragmento.getActivity(), "Impresora conectada correctamente", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            try{
                bluetoothSocket = (BluetoothSocket) bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(bluetoothDevice,1);
                bluetoothSocket.connect();
            }catch (Exception ex){
                global.escribirError(e, 104);
                Toast.makeText(fragmento.getActivity(), "No fue posible conectar con la impresora, intente de nuevo (impresora apagada/fuera de alcance)", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void beginListenData(){
        try{

            final Handler handler =new Handler();
            final byte delimiter=10;
            stopWorker =false;
            readBufferPosition=0;
            readBuffer = new byte[1024];

            thread=new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
                            int byteAvailable = inputStream.available();
                            if(byteAvailable>0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for(int i=0; i<byteAvailable; i++){
                                    byte b = packetByte[i];
                                    if(b==delimiter){
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodedByte,0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte,"US-ASCII");
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //lblPrinterName.setText(data);
                                            }
                                        });
                                    }else{
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }
                        }catch(Exception e){
                            global.escribirError(e, 105);
                            stopWorker=true;
                        }
                    }

                }
            });

            thread.start();
        }catch (Exception e){
            global.escribirError(e, 106);
            e.printStackTrace();
        }
    }

    // Printing Text to Bluetooth Printer //
    public void printData(Object[] objetos, int opcion) {

        if(isBluetoothHeadsetConnected()) {

            try {

                String msg = "";
                printPhoto(R.drawable.logo, 270, 120); //Mandamos a imprimir el logo.
                Thread.sleep(1000); // Le damos tiempo a la impresora de imprimir el logo.
                switch (opcion) {
                    case 0:
                        String sucursal = limpiarAcentos((String) objetos[0]);
                        String nombreCliente = limpiarAcentos((String) objetos[1]);
                        String idContrato = (String) objetos[2];

                        String espacios = "        ";
                        if(idContrato.length() > 10) {
                            espacios = "      ";
                        }
                        String folio = (String) objetos[3];
                        String totalanterior = "$" + objetos[4];
                        String abono = "$" + objetos[5];
                        String total = "$" + objetos[6];

                        String fechaHora = (String) objetos[7];
                        String nombreUsuario = limpiarAcentos((String) objetos[8]);
                        String telefonoatencionclientessucursal = (String) objetos[9];
                        String estadoContrato = (String) objetos[10];
                        String whatsapp = (String) objetos[11];

                        //Validamos tamaño de nombre
                        if(nombreUsuario.length() > 10){
                            //Solo extraemos los 10 primeros caracteres
                            nombreUsuario = nombreUsuario.substring(0, 10);
                        }

                        String cadenaSaldos = obtenerCadenaSaldos(totalanterior, abono, total);
                        String cantidadConLetra = cantidadConLetra(total.replace("$", ""));

                        msg = "SUC. " + sucursal + "\n\n";
                        if(!telefonoatencionclientessucursal.equals("null") || !whatsapp.equals("null")){
                            msg += "NUMEROS DE ATENCION A CLIENTES\n";
                            if(!telefonoatencionclientessucursal.equals("null") ){
                                msg += "TELEFONO: " + telefonoatencionclientessucursal;
                                msg += "\n";
                            }
                            if(!whatsapp.equals("null")){
                                msg += "WHATSAPP: " + whatsapp;
                                msg += "\n";
                            }
                            msg += "\n";
                        }
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        outputStream.write(PrinterCommands.NORMAL);
                        outputStream.write(msg.getBytes());
                        msg = "EXIJA SU TICKET DE ABONO\n\n";
                        msg += "ABONOS SIN COMPROBANTE NO SERAN\n";
                        msg += "VALIDOS PARA ACLARACIONES\n\n";
                        msg += "TICKET VALIDO SOLO SIN\n";
                        msg += "TACHADURAS Y SIN ALTERACIONES\n\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        outputStream.write(PrinterCommands.BOLD);
                        outputStream.write(msg.getBytes());
                        msg = nombreCliente + "\n\n";
                        msg += " CONTRATO " + espacios + "FOLIO\n";
                        msg += idContrato + espacios + folio + "\n\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        outputStream.write(PrinterCommands.NORMAL);
                        outputStream.write(msg.getBytes());
                        msg = " SALDO ANT    ABONO    SALDO NVO\n";
                        msg += " " + cadenaSaldos + "\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                        outputStream.write(msg.getBytes());
                        if(objetos[12].equals("7")){
                            //Si el tipo de abono es de compra de producto
                            msg = "(DE PRODUCTO)\n";
                            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                            outputStream.write(PrinterCommands.BOLD);
                            outputStream.write(msg.getBytes());
                        }
                        msg = cantidadConLetra.toUpperCase() + "PESOS\n\n";
                        msg += fechaHora + "   " + nombreUsuario + "\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                        outputStream.write(PrinterCommands.NORMAL);
                        outputStream.write(msg.getBytes());
                        msg = "www.luzatuvida.com.mx\n";
                        msg += "\n\n\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        outputStream.write(PrinterCommands.BOLD);
                        outputStream.write(msg.getBytes());

                        if(objetos[6].equals("0") && !estadoContrato.equals("6")){
                            //Si total es igual a 0 y el estado del contrato es diferente a CANCELADO -> agregar leyenda PAGADO
                            msg = "PAGADO" + "\n\n\n";
                            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                            outputStream.write(PrinterCommands.BOLD);
                            outputStream.write(msg.getBytes());
                        }

                        if(estadoContrato.equals("6")){
                            //El contrato ha sido cancelado -> agregar leyenda CANCELADO
                            msg = "CANCELADO" + "\n\n\n";
                            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                            outputStream.write(PrinterCommands.BOLD);
                            outputStream.write(msg.getBytes());
                        }
                        break;
                    case 1:
                        String numTotalContratosSumado = (String) objetos[0];
                        String numTotalContratosQueNoSeHanSubidoAPagina = (String) objetos[1];
                        List<String> keys = (List<String>) objetos[2];
                        List<String> values = (List<String>) objetos[3];
                        String totalAbonos = (String) objetos[4];
                        int contador = 0;

                        if (keys.size() > 0) {
                            msg = "ABONOS\n";
                            for (int i = 0; i < keys.size(); i++) {
                                msg += keys.get(i) + " -> " + values.get(i) + "\n";
                                contador += Integer.parseInt(values.get(i));
                            }
                            msg += "TOTAL ABONOS: " + contador + "\n";
                            msg += "TOTAL: $" + totalAbonos + "\n\n";
                        } else {
                            msg = "SIN ABONOS\n";
                            msg += "TOTAL ABONOS: 0\n";
                            msg += "TOTAL: $0\n\n";
                        }
                        outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                        outputStream.write(PrinterCommands.NORMAL);
                        outputStream.write(msg.getBytes());
                        msg = "TOTAL CONTRATOS:       " + numTotalContratosSumado + "\n";
                        msg += "CONTRATOS PENDIENTES:  " + numTotalContratosQueNoSeHanSubidoAPagina + "\n\n\n\n\n\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        outputStream.write(PrinterCommands.NORMAL);
                        outputStream.write(msg.getBytes());
                        break;
                    case 2:
                        String fechaActualYHora = (String) objetos[0];
                        String nombreCobrador = (String) objetos[1];
                        String telefonoAtencionClientes = (String)  objetos[2];
                        String whatsappAtencionClientes = (String) objetos[3];
                        String clienteNombre = limpiarAcentos((String) objetos[4]);
                        String saldoContrato = (String) objetos[5];

                        //Validamos tamaño de nombre cobrador
                        if(nombreCobrador.length() > 10){
                            //Solo extraemos los 10 primeros caracteres
                            nombreCobrador = nombreCobrador.substring(0, 10);
                        }

                        msg = "REGISTRO DE VISITA\n\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        outputStream.write(PrinterCommands.NORMAL);
                        outputStream.write(msg.getBytes());
                        msg = "TICKET NO VALIDO COMO\n";
                        msg += "COMPROBANTE DE PAGO\n\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        outputStream.write(PrinterCommands.BOLD);
                        outputStream.write(msg.getBytes());
                        msg = "DEPARTAMENTO DE COBRANZA\n\n";
                        if(!telefonoAtencionClientes.equals("null") || !whatsappAtencionClientes.equals("null")){
                            msg += "FAVOR DE COMUNICARSE AL\n";
                            if(!telefonoAtencionClientes.equals("null")){
                                msg += "TEL: " + telefonoAtencionClientes;
                                msg += "\n";
                            }
                            if(!whatsappAtencionClientes.equals("null")){
                                msg += "WHATSAPP: " + whatsappAtencionClientes;
                                msg += "\n";
                            }
                            msg += "\n";
                        }
                        msg += clienteNombre + "\n\n";
                        msg += "SALDO CONTRATO\n";
                        msg += "$" + saldoContrato + ".0" + "\n\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        outputStream.write(PrinterCommands.NORMAL);
                        outputStream.write(msg.getBytes());
                        msg = fechaActualYHora + "   " +nombreCobrador+"\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                        outputStream.write(msg.getBytes());
                        msg = "www.luzatuvida.com.mx\n\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        outputStream.write(PrinterCommands.BOLD);
                        outputStream.write(msg.getBytes());
                        msg = "NOTA:\n";
                        msg += "\n\n\n\n\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                        outputStream.write(PrinterCommands.NORMAL);
                        outputStream.write(msg.getBytes());
                        break;
                    case 3:
                        String sucursal2 = limpiarAcentos((String) objetos[0]);
                        String nombreCliente2 = limpiarAcentos((String) objetos[1]);
                        String idContrato2 = (String) objetos[2];

                        String fechaHora2 = (String) objetos[3];
                        String nombreUsuario2 = limpiarAcentos((String) objetos[4]);
                        String telefonoatencionclientessucursal2 = (String) objetos[5];
                        String whatsapp2 = (String) objetos[6];
                        String fechaLimiteGarantia = "";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            fechaLimiteGarantia = LocalDate.now().plusDays(7).toString();
                        }
                        fechaLimiteGarantia = fechaLimiteGarantia.replaceAll("-","/");
                        //Validamos tamaño de nombre
                        if(nombreUsuario2.length() > 10){
                            //Solo extraemos los 10 primeros caracteres
                            nombreUsuario2 = nombreUsuario2.substring(0, 10);
                        }

                        msg = "SUC. " + sucursal2 + "\n\n";
                        if(!telefonoatencionclientessucursal2.equals("null") || !whatsapp2.equals("null")){
                            msg += "NUMEROS DE ATENCION A CLIENTES\n";
                            if(!telefonoatencionclientessucursal2.equals("null")){
                                msg += "TELEFONO: " + telefonoatencionclientessucursal2 + "\n";
                            }
                            if(!whatsapp2.equals("null")){
                                msg += "WHATSAPP: " + whatsapp2 + "\n";
                            }
                            msg += "\n";
                        }
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        outputStream.write(PrinterCommands.NORMAL);
                        outputStream.write(msg.getBytes());
                        msg = "TICKET VALIDO SOLO SIN\n";
                        msg += "TACHADURAS Y SIN ALTERACIONES\n\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        outputStream.write(PrinterCommands.BOLD);
                        outputStream.write(msg.getBytes());
                        msg += nombreCliente2 + "\n\n";
                        msg += " CONTRATO " + "\n";
                        msg += idContrato2 + "\n\n";
                        msg += "LENTE ENTREGADO" + "\n\n\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        outputStream.write(PrinterCommands.NORMAL);
                        outputStream.write(msg.getBytes());
                        msg = fechaHora2 + "   " + nombreUsuario2 + "\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                        outputStream.write(PrinterCommands.NORMAL);
                        outputStream.write(msg.getBytes());
                        msg = "GARANTIA VALIDA HASTA\n";
                        msg += fechaLimiteGarantia + "\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        outputStream.write(PrinterCommands.NORMAL);
                        outputStream.write(msg.getBytes());
                        msg = "www.luzatuvida.com.mx\n";
                        msg += "\n\n\n";
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        outputStream.write(PrinterCommands.BOLD);
                        outputStream.write(msg.getBytes());
                        break;
                }

            } catch (Exception e) {
                global.escribirError(e, 107);
                e.printStackTrace();
            }

        }else {
            Toast.makeText(fragmento.getActivity(), "Error al imprimir/Conexión/Sin papel", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isBluetoothHeadsetConnected() {
        boolean connected = false;

        try {
            Method m = bluetoothDevice.getClass().getMethod("isConnected", (Class[]) null);
            connected = (boolean) m.invoke(bluetoothDevice, (Object[]) null);
        } catch (Exception e) {
            global.escribirError(e, 108);
            e.printStackTrace();
        }

        return connected;
    }

    public String limpiarAcentos(String cadena) {
        String limpio = "";
        if (cadena.length() > 0) {
            String valor = cadena;
            valor = valor.toUpperCase();
            // Normalizar texto para eliminar acentos, dieresis, cedillas y tildes
            limpio = Normalizer.normalize(valor, Normalizer.Form.NFD);
            // Quitar caracteres no ASCII excepto la enie, interrogacion que abre, exclamacion que abre, grados, U con dieresis.
            limpio = limpio.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");
            // Regresar a la forma compuesta, para poder comparar la enie con la tabla de valores
            limpio = Normalizer.normalize(limpio, Normalizer.Form.NFC);
            limpio = limpio.replace("Ñ", "N");
        }
        return limpio;
    }

    private String obtenerCadenaSaldos(String totalanterior, String abono, String total) {

        String cadenaSaldos = "";

        switch (totalanterior.length()) {
            case 0:
                cadenaSaldos = totalanterior + "             " + obtenerCadenaAbonoYTotal(abono, total);
                break;
            case 1:
                cadenaSaldos = totalanterior + "            " + obtenerCadenaAbonoYTotal(abono, total);
                break;
            case 2:
                cadenaSaldos = totalanterior + "           " + obtenerCadenaAbonoYTotal(abono, total);
                break;
            case 3:
                cadenaSaldos = totalanterior + "          " + obtenerCadenaAbonoYTotal(abono, total);
                break;
            case 4:
                cadenaSaldos = totalanterior + "         " + obtenerCadenaAbonoYTotal(abono, total);
                break;
            case 5:
                cadenaSaldos = totalanterior + "        " + obtenerCadenaAbonoYTotal(abono, total);
                break;
            case 6:
                cadenaSaldos = totalanterior + "       " + obtenerCadenaAbonoYTotal(abono, total);
                break;
            case 7:
                cadenaSaldos = totalanterior + "      " + obtenerCadenaAbonoYTotal(abono, total);
                break;
            case 8:
                cadenaSaldos = totalanterior + "     " + obtenerCadenaAbonoYTotal(abono, total);
                break;
            case 9:
                cadenaSaldos = totalanterior + "    " + obtenerCadenaAbonoYTotal(abono, total);
                break;
            case 10:
                cadenaSaldos = totalanterior + "   " + obtenerCadenaAbonoYTotal(abono, total);
                break;
            case 11:
                cadenaSaldos = totalanterior + "  " + obtenerCadenaAbonoYTotal(abono, total);
                break;
            case 12:
                cadenaSaldos = totalanterior + " " + obtenerCadenaAbonoYTotal(abono, total);
                break;
        }

        return cadenaSaldos;
    }

    private String obtenerCadenaAbonoYTotal(String abono, String total) {

        String cadenaAbonoYTotal = "";

        switch (abono.length()) {
            case 0:
                cadenaAbonoYTotal = abono + "         " + total;
                break;
            case 1:
                cadenaAbonoYTotal = abono + "        " + total;
                break;
            case 2:
                cadenaAbonoYTotal = abono + "       " + total;
                break;
            case 3:
                cadenaAbonoYTotal = abono + "      " + total;
                break;
            case 4:
                cadenaAbonoYTotal = abono + "     " + total;
                break;
            case 5:
                cadenaAbonoYTotal = abono + "    " + total;
                break;
            case 6:
                cadenaAbonoYTotal = abono + "   " + total;
                break;
            case 7:
                cadenaAbonoYTotal = abono + "  " + total;
                break;
            case 8:
                cadenaAbonoYTotal = abono + " " + total;
                break;
            case 9:
                cadenaAbonoYTotal = abono + total;
                break;
        }

        return cadenaAbonoYTotal;
    }

    // Disconnect Printer //disconnectBT
    public void closedBluetoothPrinter () {

        if (outputStream != null) {

            try {
                stopWorker = true;
                outputStream.close();
                inputStream.close();
                bluetoothSocket.close();
                //lblPrinterName.setText("Printer Disconnected.");
            } catch (Exception e) {
                global.escribirError(e, 109);
                e.printStackTrace();
            }

        }

    }

    public void verificarSiBluetoothPrinterEstaCerrado() {
        if(stopWorker && !bluetoothSocket.isConnected()) {
            openBluetoothPrinter();
        }
    }

    public void printPhoto(int img, int newWidth, int newHeight) {
        try {
            Bitmap bmp = getResizedBitmap(BitmapFactory.decodeResource(fragmento.getContext().getResources(),
                    img), newWidth, newHeight);
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                outputStream.write(command);

            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            global.escribirError(e, 110);
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
            Toast.makeText(fragmento.getActivity(), "Error al imprimir/Conexión/Sin papel -> Logo", Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    /**
     * Convierte el número que recibe como argumento a su representación escrita con letra.
     *
     * @param s Una cadena de texto que contiene los dígitos de un número.
     * @return  Una cadena de texto que contiene la representación con letra de
     *          la parte entera del número que se recibió como argumento.
     */
    public String cantidadConLetra(String s) {
        StringBuilder result = new StringBuilder();
        BigDecimal totalBigDecimal = new BigDecimal(s).setScale(2, BigDecimal.ROUND_DOWN);
        long parteEntera = totalBigDecimal.toBigInteger().longValue();
        int triUnidades      = (int)((parteEntera % 1000));
        int triMiles         = (int)((parteEntera / 1000) % 1000);
        int triMillones      = (int)((parteEntera / 1000000) % 1000);
        int triMilMillones   = (int)((parteEntera / 1000000000) % 1000);

        if (parteEntera == 0) {
            result.append("Cero ");
            return result.toString();
        }

        if (triMilMillones > 0) result.append(triTexto(triMilMillones).toString() + "Mil ");
        if (triMillones > 0)    result.append(triTexto(triMillones).toString());

        if (triMilMillones == 0 && triMillones == 1) result.append("Millón ");
        else if (triMilMillones > 0 || triMillones > 0) result.append("Millones ");

        if (triMiles > 0)       result.append(triTexto(triMiles).toString() + "Mil ");
        if (triUnidades > 0)    result.append(triTexto(triUnidades).toString());

        return result.toString().toUpperCase();
    }

    /**
     * Convierte una cantidad de tres cifras a su representación escrita con letra.
     *
     * @param n La cantidad a convertir.
     * @return  Una cadena de texto que contiene la representación con letra
     *          del número que se recibió como argumento.
     */
    private StringBuilder triTexto(int n) {
        StringBuilder result = new StringBuilder();
        int centenas = n / 100;
        int decenas  = (n % 100) / 10;
        int unidades = (n % 10);

        switch (centenas) {
            case 0: break;
            case 1:
                if (decenas == 0 && unidades == 0) {
                    result.append("Cien ");
                    return result;
                }
                else result.append("Ciento ");
                break;
            case 2: result.append("Doscientos "); break;
            case 3: result.append("Trescientos "); break;
            case 4: result.append("Cuatrocientos "); break;
            case 5: result.append("Quinientos "); break;
            case 6: result.append("Seiscientos "); break;
            case 7: result.append("Setecientos "); break;
            case 8: result.append("Ochocientos "); break;
            case 9: result.append("Novecientos "); break;
        }

        switch (decenas) {
            case 0: break;
            case 1:
                if (unidades == 0) { result.append("Diez "); return result; }
                else if (unidades == 1) { result.append("Once "); return result; }
                else if (unidades == 2) { result.append("Doce "); return result; }
                else if (unidades == 3) { result.append("Trece "); return result; }
                else if (unidades == 4) { result.append("Catorce "); return result; }
                else if (unidades == 5) { result.append("Quince "); return result; }
                else result.append("Dieci");
                break;
            case 2:
                if (unidades == 0) { result.append("Veinte "); return result; }
                else result.append("Veinti");
                break;
            case 3: result.append("Treinta "); break;
            case 4: result.append("Cuarenta "); break;
            case 5: result.append("Cincuenta "); break;
            case 6: result.append("Sesenta "); break;
            case 7: result.append("Setenta "); break;
            case 8: result.append("Ochenta "); break;
            case 9: result.append("Noventa "); break;
        }

        if (decenas > 2 && unidades > 0)
            result.append("y ");

        switch (unidades) {
            case 0: break;
            case 1: result.append("Un "); break;
            case 2: result.append("Dos "); break;
            case 3: result.append("Tres "); break;
            case 4: result.append("Cuatro "); break;
            case 5: result.append("Cinco "); break;
            case 6: result.append("Seis "); break;
            case 7: result.append("Siete "); break;
            case 8: result.append("Ocho "); break;
            case 9: result.append("Nueve "); break;
        }

        return result;
    }

}
