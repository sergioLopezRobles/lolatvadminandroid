package com.luzatuvida.lolatvadminandroid.global;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import android.view.View;
import android.widget.ImageView;

import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


import com.luzatuvida.lolatvadminandroid.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class Camara {

    /*
    Identificador error del 48 al 50
     */

    Fragment fragmento;

    private final int PHOTO_CODE = 200;
    private final int MY_PERMISSIONS = 100;
    ImageView imageView;
    String file_path, nombreImagen;
    File dir, direccionNoMedia, direccionNoMediaTemporal;
    File direccionTemporal;
    Internet conexionInternet;
    Global global;
    ObtenerRol obtenerRol;
    int opcionFoto = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 200;
    public static boolean banderaFotoIneFrente = false;
    public static boolean banderaFotoIneAtras = false;
    public static boolean banderaFotoCasa = false;
    public static boolean banderaFotoComprobanteDomicilio = false;
    public static boolean banderaFotoPagare = false;
    public static boolean banderaFotoOtros = false;

    public Camara(Fragment fragmento) {
        global = new Global(fragmento);
        obtenerRol = new ObtenerRol(fragmento);
        this.fragmento = fragmento;
        file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        conexionInternet = new Internet(fragmento.getActivity());
        crearDirectorios();
    }

    /*Metodo/Funcion: crearDirectorios
      Descripcion: Crea los directorios necesarios para guardar las imagenes
    */
    public void crearDirectorios() {
        String mensaje = "";

        dir = new File(file_path + "/luzatuvida");
        mensaje = dir.mkdirs() ? "Creado correctamente" : "No creado";
        Log.i("MENSAJE", "Carpeta LuzATuVida:  " + mensaje);

        direccionNoMedia = new File(file_path + "/luzatuvida/.nomedia");
        mensaje = direccionNoMedia.mkdirs() ? "Creado correctamente" : "No creado";
        Log.i("MENSAJE", "Carpeta LuzATuVida .nomedia:  " + mensaje);

        direccionNoMediaTemporal = new File(file_path + "/luzatuvida/.nomedia/temp");
        mensaje = direccionNoMediaTemporal.mkdirs() ? "Creado correctamente" : "No creado";
        Log.i("MENSAJE", "Carpeta LuzATuVida temporal:  " + mensaje);

    }

    /*Metodo/Funcion: openCamera
      Descripcion: Despliega la funcion de la camara para que pueda tomar la foto
    */
    public void openCamera(ImageView imageView2, int opcion) {

        //opcion
        //0-> ivFotoINEFrente
        //1-> ivFotoINEAtras
        //2-> ivFotoCasa
        //3-> ivFotoComprobanteDomicilio
        //4-> ivFotoPagare
        //5-> ivFotoOtros
        //6-> ivFotoTarjetaPensionFrente
        //7-> ivFotoTarjetaPensionAtras
        //8-> Sin imagen de ejemplo
        opcionFoto = opcion;

        ImageView image = new ImageView(fragmento.getActivity());

        switch (opcion) {
            case 0:
            case 6:
                image.setImageResource(R.drawable.inefrenteejemplo);
                break;
            case 1:
            case 7:
                image.setImageResource(R.drawable.ineatrasejemplo);
                break;
            case 2:
            case 5:
                image.setImageResource(R.drawable.casaejemplo);
                break;
            case 3:
                image.setImageResource(R.drawable.comprobanteejemplo);
                break;
            case 4:
                image.setImageResource(R.drawable.pagereejemplo);
                break;
            case 8:
                imageView = imageView2;
                nombreImagen = System.currentTimeMillis()+".jpg";
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                direccionTemporal = new File(direccionNoMediaTemporal +"/"+nombreImagen);
                Uri photoURI = FileProvider.getUriForFile(fragmento.getContext(), fragmento.getContext().getApplicationContext().getPackageName() + ".provider", direccionTemporal);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);

                if (takePictureIntent.resolveActivity(fragmento.getActivity().getPackageManager()) != null) {
                    //Se manda la imagen al onActivityResult para agregarla en el ImageView de la vista
                    fragmento.startActivityForResult(takePictureIntent, PHOTO_CODE);
                }
                break;
        }

        if(opcion != 8){
            AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getActivity());
            alerta.setTitle("Ejemplo").setView(image)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Debe ir vacio por que se esta obteniendo abajo
                        }
                    });

            final AlertDialog dialog = alerta.create();
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageView = imageView2;
                    nombreImagen = System.currentTimeMillis()+".jpg";
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    direccionTemporal = new File(direccionNoMediaTemporal +"/"+nombreImagen);
                    Uri photoURI = FileProvider.getUriForFile(fragmento.getContext(), fragmento.getContext().getApplicationContext().getPackageName() + ".provider", direccionTemporal);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);

                    if (takePictureIntent.resolveActivity(fragmento.getActivity().getPackageManager()) != null) {
                        //Se manda la imagen al onActivityResult para agregarla en el ImageView de la vista
                        fragmento.startActivityForResult(takePictureIntent, PHOTO_CODE);
                    }
                    dialog.dismiss();
                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageView = imageView2;
                    nombreImagen = System.currentTimeMillis()+".jpg";
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    direccionTemporal = new File(direccionNoMediaTemporal +"/"+nombreImagen);
                    Uri photoURI = FileProvider.getUriForFile(fragmento.getContext(), fragmento.getContext().getApplicationContext().getPackageName() + ".provider", direccionTemporal);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);

                    if (takePictureIntent.resolveActivity(fragmento.getActivity().getPackageManager()) != null) {
                        //Se manda la imagen al onActivityResult para agregarla en el ImageView de la vista
                        fragmento.startActivityForResult(takePictureIntent, PHOTO_CODE);
                    }
                    dialog.dismiss();
                }
            });
        }

    }

    /*Metodo/Funcion: onActivityResult
      Descripcion: Obtiene la imagen mandada en el startActivityForResult agregandola en el imageView correspondiente en la vista
    */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            //Para ver de donde viene la respuesta de la peticion
            //Calidad de imagen por default - Ventas
            int reqWidth = 1125;
            int reqHeight = 1500;

            switch (requestCode) {
                case PHOTO_CODE:
                    if(obtenerRol.obtenerAtributoUsuarioLogeado("ROL").equals("4")){
                        //Calidad de imagen para rol de cobranza
                        reqWidth = 600;
                        reqHeight = 800;
                    }
                    try {
                        File direccionAntesDeCargar = new File(direccionNoMediaTemporal + "/" + nombreImagen);
                        imageView.setBackground(null);
                        imageView.setImageBitmap(validacionParaRotarImagen(direccionAntesDeCargar.getAbsolutePath(),
                                decodeSampledBitmapFromFile(direccionAntesDeCargar.getAbsolutePath(), reqWidth, reqHeight)));
                        direccionAntesDeCargar.delete();
                    }catch(Exception e){
                        global.escribirError(e, 48);
                        Log.i("MENSAJE","ERROR:"+e.toString());
                    }
                    break;
            }
        }

        //Validar si se tomo foto o solo se abrio la camara
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Se ha tomado una foto
            switch (opcionFoto){
                case 0:
                case 6:
                    banderaFotoIneFrente = true;
                    break;
                case 1:
                case 7:
                    banderaFotoIneAtras = true;
                    break;
                case 2:
                    banderaFotoCasa = true;
                    break;
                case 5:
                    banderaFotoOtros = true;
                    break;
                case 3:
                    banderaFotoComprobanteDomicilio = true;
                    break;
                case 4:
                    banderaFotoPagare = true;
                    break;
            }
        }
    }

    private Bitmap validacionParaRotarImagen(String photoPath, Bitmap bitmap) {

        Bitmap respuestaBitmap = null;

        try {

            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    respuestaBitmap = rotarImagen(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    respuestaBitmap = rotarImagen(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    respuestaBitmap = rotarImagen(bitmap, 270);
                    break;
                default:
                    respuestaBitmap = bitmap;
                    break;
            }

        } catch (IOException e) {
            global.escribirError(e, 49);
            Log.i("MENSAJE","ERROR:"+e.toString());
        }

        return respuestaBitmap;
    }

    public static Bitmap rotarImagen(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    /*Metodo/Funcion: decodeSampledBitmapFromFile
      Descripcion: Decodificar mapa de bits muestreado desde archivo
    */
    public static Bitmap decodeSampledBitmapFromFile(String path,int reqWidth, int reqHeight) {


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    /*Metodo/Funcion: mayRequestStoragePermission
      Descripcion: Pide al usuario los permisos necesarios para utilizar la camara y asi tambien guardar las imagenes en el directorio
    */
    public boolean mayRequestStoragePermission() {

        //Verificar si la version del sistema operativo es menor a la 6, si es asi no ocupamos de pedir permisos
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        //Si los permisos estan aceptados
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //Version de android es 13 o superior

            if ((fragmento.getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    && (fragmento.getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                    (fragmento.getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                return true;
            }

            //Pedir los permisos al usuario mediante un mensaje extra
            if ((fragmento.getActivity().shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                    || (fragmento.getActivity().shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                    || (fragmento.getActivity().shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION))) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getContext());
                alerta.setTitle("Permisos denegados").setMessage("Los permisos son necesarios para poder usar la aplicación").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragmento.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS);
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            } else {
                fragmento.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS);
            }

        }else {
            //Version de android es 12 o anterior

            if ((fragmento.getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    && (fragmento.getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    && (fragmento.getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                    (fragmento.getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                return true;
            }

            //Pedir los permisos al usuario mediante un mensaje extra
            if ((fragmento.getActivity().shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    || (fragmento.getActivity().shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                    || (fragmento.getActivity().shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                    || (fragmento.getActivity().shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION))) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getContext());
                alerta.setTitle("Permisos denegados").setMessage("Los permisos son necesarios para poder usar la aplicación").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragmento.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS);
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            } else {
                fragmento.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS);
            }
        }

        return false;
    }

    /*Metodo/Funcion: onRequestPermissionsResult
      Descripcion: Es mandado a llamar cuando los permisos fueron o no aceptados
    */
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                //Version de android es 13 o superior
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //Permisos aceptados
                    Toast.makeText(fragmento.getContext(), "Permisos aceptados", Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    //Permisos no aceptados
                    showExplanation();
                    return false;
                }
            }else {
                //Version de android es 12 o anterior
                if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    //Permisos aceptados
                    Toast.makeText(fragmento.getContext(), "Permisos aceptados", Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    //Permisos no aceptados
                    showExplanation();
                    return false;
                }
            }
        }
        return false;

        /*if (requestCode == PERMISO_ALMACENAMIENTO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                camara.iniciarDescargaImagenURL();
            }else {
                Toast.makeText(getContext(), "Permisos denegados", Toast.LENGTH_LONG).show();
            }
        }*/
    }

    /*Metodo/Funcion: showExplanation
      Descripcion: Es mandado a llamar cuando los permisos no fueron aceptados
    */
    private void showExplanation() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(fragmento.getContext());
        alerta.setTitle("Permisos denegados")
                .setMessage("Para usar las funciones de la aplicación correctamente necesitas aceptar los permisos")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mandarAPantallaConfiguracionTelefono();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    public void mandarAPantallaConfiguracionTelefono() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS); //Va a abrir la aplicacion de configuraciones junto con el detalle de la aplicacion para poder aceptar o habilitar los permisos
        Uri uri = Uri.fromParts("package", fragmento.getActivity().getPackageName(), null);
        intent.setData(uri);
        fragmento.startActivity(intent);
    }

    /*Metodo/Funcion: guardarImagenGaleria
      Descripcion: Guardar en directorio correspondiente las imagenes tomadas
    */
    public String guardarImagenGaleria(ImageView imageView, String nombre) {

        //Nombre de la imagen
        String fileName = String.format("%s.jpg", nombre);

        try {

            //Tomar la imagen del ImageView
            BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = draw.getBitmap();

            int alto = bitmap.getHeight();
            int ancho = bitmap.getWidth();
            Bitmap bitmapSaliente;

            //Agregar un alto de 1500px y un ancho de 1125px
            if (alto > ancho) {
                bitmapSaliente = Bitmap.createScaledBitmap(bitmap, 1125, 1500, true);
            } else {
                bitmapSaliente = Bitmap.createScaledBitmap(bitmap, 1500, 1125, true);
            }

            File outFile = new File(direccionNoMedia, fileName);

            FileOutputStream outStream = new FileOutputStream(outFile);

            bitmapSaliente.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

            return fileName;

        } catch (IOException e) {
            global.escribirError(e, 50);
            Log.i("ERROR", "IOException:  " + e);
        }

        return fileName;

    }

}
