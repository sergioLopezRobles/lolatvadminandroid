package com.luzatuvida.lolatvadminandroid.global;

public class Llaves {

    public static String dispositivo = "62FE47F0-BE69-11EE-A750-190DD39EA4E3";
    public static String url_ruta_principal = "http://192.168.1.93:3000"; //Sin diagonal al final https://adminlabo.luzatuvida.com.mx
    public static String url_inicio_sesion = url_ruta_principal + "/api/servicio/iniciarsesion";
    //public static String url_sincronizar_1 = url_ruta_principal + "/api/servicio/sincronizaruno";
    public static String url_sincronizar_0 = url_ruta_principal + "/api/servicio/sincronizarcero";
    public static String url_sincronizar_2 = url_ruta_principal + "/api/servicio/sincronizardos";
    public static String url_cerrarsesion = url_ruta_principal + "/api/servicio/cerrarsesion";
    public static String url_supervision_entrar = url_ruta_principal + "/api/servicio/supervision";
    public static String url_supervision_fotos = url_ruta_principal + "/api/servicio/verificarfotossupervision";
    public static String url_supervision_registrar = url_ruta_principal + "/api/servicio/registrar";
    public static String url_historialmovimientos_contrato = url_ruta_principal + "/api/servicio/historialmovimientos/contrato";

    public static String carpeta_principal_ftp = "contratospruebas"; //contratos o contratospruebas
    public static String carpeta_vehiculos_ftp = "contratospruebas/vehiculos"; //vehiculos o contratospruebas/vehiculos

    public static int versiondb = 37; //Version de base de datos (ultima version: 36)
    public static boolean scolllento = false; //Debe de estar en true para que a las asis/opto se les haga lento el scroll al crear contrato
    public static boolean impresora_termica = false; //Debe de estar en true en caso de que se valla a utilizar la impresora termica (Cobranza)

    public static String ruta_ftp = ""; //ruta para subir imagenes a ftp
    public static String usuario_ftp = ""; //usuario para subir imagenes a ftp
    public static String contrasena_ftp = ""; //contrasena para subir imagenes a ftp
    public static String preciodolar = ""; //precio dolar

    //Clave publicable prueba: pk_test_51JLATqGxL0OdC7rPaMMC8K8t5Kv1m0bLN6qNyp6bfjkjBRa3maF1La64mRQOQpf5ttY23fKSPhTbaOACmexcZC0v00SX5eZ4gQ
    //Clave publicable live: pk_live_51JLATqGxL0OdC7rP4T1YHLSp2eeF8TyEsQ8ZkyBgoRNv8fkvSbeqSbsFL5ljaKnwEUT9eIjjPthFNNdxbzptGLJy00mdhfd3s8
    //Clave secreta prueba: sk_test_51JLATqGxL0OdC7rPfHqgBBRoOYbuEz7RaBiJCuoHDJVSGF1nXMuDyY8qzNs90f9jyGt6SPavq0rz8KcxtyDTh2Uh00RYpyoYV6
    //Clave secreta live: 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


    //Ultimo identificador error 339 (Se utiliza para los catch)
    //Ultimo identificador Throwable error 8 (Se utiliza en los catch Thread.setDefaultUncaughtExceptionHandler de cada clase principal)

}
