package com.pino.challengemercadolibre.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pino.challengemercadolibre.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Camaleon {
    public static final int FORMATO_HORIZONTAL =0;
    public static final int FORMATO_VERTICAL =1;
    public static final int FORMATO_CUADRADO =2;
    public static void enviarTextoAWhatsapp(Activity activity, String codigoPais, String telefono, String texto){
        StringBuilder uri =  new StringBuilder();
        uri.append("whatsapp://send?phone="+ codigoPais+telefono);
        uri.append("&text=");
        uri.append(texto);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri.toString()));
        activity.startActivity(intent);
        activity.finish();
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return (Resources.getSystem().getDisplayMetrics().heightPixels) - 100;
    }

    public static boolean isOnlineNet() {
        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hayInternet(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static void mostrarTOAS(Activity activity, String mensaje) {
        Toast.makeText(activity, mensaje, Toast.LENGTH_SHORT).show();
    }

    public static boolean validarTelefono(String telefono) {

        if(telefono.length()==7){
            telefono = "032"+telefono;
        }
        return telefono.length() >= 10;
    }

    public static boolean validarCorreo(String correo) {
        return Pattern.matches("^[^@]+@[^@]+\\.[a-zA-Z]{2,}$", correo);
    }

    public static boolean validarTextoVacio(String text){
        return (text!= null && !text.equals("") && !text.equals("null"));
    }

    public static Dialog mostrarDialogoInfo(Context context, String titulo, String mensaje, MyCallBack<String> respuesta) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_dialogo_info);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tvTitulo = dialog.findViewById(R.id.dialogoInfoTitulo);
        tvTitulo.setText(titulo);
        TextView tvInformacion = dialog.findViewById(R.id.dialogoInfoMensaje);
        tvInformacion.setText(mensaje);
        Button btnAceptar = dialog.findViewById(R.id.dialogoInfoBoton);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                respuesta.onResponse(null);
                dialog.hide();
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();

        return dialog;
    }

    /**
     * Este metodo se usa para consumir servicios get*/
    public static void consumirRestGet(String endpoint, MyCallBack<String> jsonResponse){
        new Thread(new Runnable() {
            @Override
            public void run() {
                pintarLog("ConsumirRestGet Endpoint: "+endpoint);
                StringBuilder response = new StringBuilder();
                try {
                    URL url = new URL(endpoint);
                    HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                    conexion.setDoOutput(true);
                    conexion = (HttpURLConnection) url.openConnection();
                    conexion.setConnectTimeout(60000);
                    conexion.setReadTimeout(60000);
                    conexion.setRequestMethod("GET");
                    conexion.setRequestProperty("Content-Type", "text/plain");
                    conexion.setRequestProperty("Content-Type", "application/json");
                    conexion.setRequestProperty("Connection", "keep-alive");
                    //conexion.setRequestProperty("X-Parse-REST-API-Key", restApiKey);
                    //conexion.setRequestProperty("Authorization","Bearer "+token);
                    conexion.connect();

                    pintarLog("Codigo de respuesta: "+conexion.getResponseCode());
                    if (conexion.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : "
                                + conexion.getResponseCode());
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conexion.getInputStream())));

                    String output;
                    while ((output = br.readLine()) != null) {
                        response.append(output);
                    }

                    conexion.disconnect();
                    br.close();
                    pintarLog("Response: "+response.toString());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                jsonResponse.onResponse(response.toString());
            }
        }).start();

    }

    public static String codificarImagen(Uri imageUri, Activity activity) {
        String codificada = "";
        try {
            final InputStream imageStream = activity.getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            codificada = bitmapAB64(selectedImage);
        } catch (Exception e) {
        }
        return codificada;
    }

    public static String bitmapAB64(Bitmap bm) {
        String imgDecodableString = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //CALIDAD Imagen 1
        byte[] b = baos.toByteArray();
        imgDecodableString = Base64.encodeToString(b, Base64.DEFAULT);

        return imgDecodableString;
    }

    public static Bitmap ajustarImagen(Bitmap imagen, int width, int height) {
        return imagen.createBitmap(imagen);
        //return imagen.createScaledBitmap(imagen, width, height, false);
    }

    public static Bitmap decodificarB64(String input) {
        byte[] decodedBytes = Base64.decode(input.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static void compartirLinkEnWhatsapp(Activity activity, String mensajeLink){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");
        intent.putExtra(Intent.EXTRA_TEXT, mensajeLink);

        try {
            activity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
            mostrarTOAS(activity, "El dispositivo no tiene instalado WhatsApp");
            /*Snackbar.make(view, "El dispositivo no tiene instalado WhatsApp", Snackbar.LENGTH_LONG)
                    .show();*/
        }

    }

    public static void abrirAppEnPlayStore(Activity activity, String linkApp){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(linkApp));
        intent.setPackage("com.android.vending");
        activity.startActivity(intent);
    }




    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        if (uri.getHost().contains("com.android.providers.media")) {
            // Image pick from recent
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        } else {
            // image pick from gallery
            return  getRealPathFromDocumentUri(context,uri);
        }

    }

    public static String getRealPathFromDocumentUri(Context context, Uri uri){
        String filePath = "";

        Pattern p = Pattern.compile("(\\d+)$");
        Matcher m = p.matcher(uri.toString());
        if (!m.find()) {
            Log.e("Camaleon", "ID for requested image not found: " + uri.toString());
            return filePath;
        }
        String imgId = m.group();

        String[] column = { MediaStore.Images.Media.DATA };
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ imgId }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return filePath;
    }

    //_______________________________________________________

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param activity The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(Activity activity, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(activity, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(activity, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(activity, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(activity, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    //XXXXXXXXXXXXXXXXXXXXXXXXXX COMPRIMIR IMAGENES XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    public static Uri compressImage(Uri imageUri, int formato, Activity activity) {

        //String filePath = getRealPathFromURI(imageUri, activity);
        String filePath = getPath(activity, imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = 800;
        int actualWidth = 1024;
        if (formato == FORMATO_VERTICAL) {
            actualHeight = 1024;
            actualWidth = 800;
        }else if(formato == FORMATO_CUADRADO){
            actualHeight = 500;
            actualWidth = 500;
        }


//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow Android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Uri uri = Uri.fromFile(new File(filename));
        return uri;

    }

    private static String getFilename() {
        //Este path funcionó para un a30s
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //Environment.getExternalStorageDirectory().getPath()
        File file = new File(path, "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");


        return uriSting;

    }

    private static String getRealPathFromURI(String contentURI, Activity activity) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = activity.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static void pintarLog(String mensaje) {
        Log.i("CAMALEON", "<" + darFechaActual("dd/MM/yyyy HH:MM:SS") + ">" +mensaje);
    }

    /**public static void cambiarEstadoCheck(CheckedTextView check) {
        if (check.isChecked()) {
            check.setChecked(false);
            check.setCheckMarkDrawable(R.drawable.checkoff1);
        } else {
            check.setChecked(true);
            check.setCheckMarkDrawable(R.drawable.checkon1);
        }
    }*/

    public static String darFechaActual(String format) {
        SimpleDateFormat curFormater = new SimpleDateFormat(format);
        Date dateObj = new Date();
        return curFormater.format(dateObj);
    }

    public static void abrirLink(Activity activity,String link) {
        Uri uriLink = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uriLink);
        activity.startActivity(intent);
    }
}
