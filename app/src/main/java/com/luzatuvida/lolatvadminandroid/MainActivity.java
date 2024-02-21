package com.luzatuvida.lolatvadminandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.luzatuvida.lolatvadminandroid.global.Camara;
import com.luzatuvida.lolatvadminandroid.global.Global;
import com.luzatuvida.lolatvadminandroid.global.Internet;
import com.luzatuvida.lolatvadminandroid.global.Llaves;
import com.luzatuvida.lolatvadminandroid.global.ObtenerRol;
import com.luzatuvida.lolatvadminandroid.global.Seguridad;
import com.luzatuvida.lolatvadminandroid.global.Sincronizacion;
import com.luzatuvida.lolatvadminandroid.global.accionesDrawer;
import com.luzatuvida.lolatvadminandroid.global.baseDeDatos;
import com.luzatuvida.lolatvadminandroid.vista.buzon;
import com.luzatuvida.lolatvadminandroid.vista.configuracion;
import com.luzatuvida.lolatvadminandroid.vista.inicioSesion;
import com.luzatuvida.lolatvadminandroid.vista.notascobranza;
import com.luzatuvida.lolatvadminandroid.vista.principal;
import com.google.android.material.navigation.NavigationView;
import com.luzatuvida.lolatvadminandroid.vista.supervisionVehicular;

import java.util.List;

public class MainActivity extends AppCompatActivity implements accionesDrawer {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    LinearLayout llCerrarSesion,llContratos,llSincronizar,llConfiguracion,llReporte, llBuzon, llSupervision, llAlmacenamientoImagenesContrato, llNotasCobranza;

    ActionBarDrawerToggle toggle;

    baseDeDatos bd;
    Sincronizacion sincronizacion;
    ObtenerRol obtenerRol;
    Internet internet;
    Runnable runnable;
    Llaves llaves;
    public Menu menuHamburguesa;
    Seguridad seguridad;

    TextView tvEliminarReporte;
    Global global;
    LayoutInflater inflater = null;
    View searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bloquear capturas de pantallas en la aplicacion
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        //No permitir a la pantalla que se suspenda con el tiempo de espera
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Inicialización de Clases
        llaves = new Llaves();
        bd = new baseDeDatos(this, "basedatos", null, llaves.versiondb);
        internet = new Internet(this);

        //UI
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        llContratos = findViewById(R.id.llContratos);
        llSincronizar = findViewById(R.id.llSincronizacion);
        llConfiguracion = findViewById(R.id.llConfiguracion);
        llSupervision = findViewById(R.id.llSupervision);
        llReporte = findViewById(R.id.llReporte);
        tvEliminarReporte = findViewById(R.id.tvEliminarReporte);
        llBuzon = findViewById(R.id.llBuzon);
        llCerrarSesion = findViewById(R.id.llCerrarSesion);
        llAlmacenamientoImagenesContrato = findViewById(R.id.llAlmacenamientoImagenesContrato);
        llNotasCobranza = findViewById(R.id.llNotasCobranza);

        //Setup toolbar
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().add(R.id.content, new inicioSesion()).commit();
        setTitle("");

        toggle = setUpDrawerToogle();
        drawerLayout.addDrawerListener(toggle);

        llContratos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global = new Global(obtenerFragment());
                String descargaCompleta = global.obtenerAtributoTabla("GLOBAL", "DESCARGACOMPLETA", "ID", "1");
                if(descargaCompleta.contains("1")) {
                    //Descarga de datos fue insertada correctamente
                    Fragment verificadorFragment = new principal(false);
                    FragmentTransaction transaction = ((FragmentActivity) obtenerFragment().getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, verificadorFragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    drawerLayout.closeDrawers();
                }else if(descargaCompleta.contains("4")){
                    //EN ESPERA
                    Toast.makeText(MainActivity.this, "Sincronizando, espera un momento porfavor...", Toast.LENGTH_LONG).show();
                }else {
                    //Descarga de datos incompleta o no alcanzo a responder el webservice
                    Toast.makeText(MainActivity.this, "Para continuar, es necesario sincronizar...", Toast.LENGTH_LONG).show();
                }
            }
        });

        llSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (internet.verificarConexionInternet()) {
                    //Tiene internet
                    global = new Global(obtenerFragment());
                    if(!global.obtenerAtributoTabla("GLOBAL", "DESCARGACOMPLETA", "ID", "1").contains("4")) {
                        //Diferente a estado ESPERA
                        obtenerRol = new ObtenerRol(obtenerFragment());
                        if (obtenerRol.obtenerAtributoUsuarioLogeado("ROL").equals("4")) {
                            //Rol cobranza
                            if (obtenerFragment() instanceof principal) {
                                //Estamos en la vista principal (Lista contratos)
                                mandarAllamarSincronizar();
                            } else {
                                //Estamos en una vista diferente a la principal (Lista contratos)
                                Toast.makeText(MainActivity.this, "Para sincronizar por favor ir a la lista de contratos", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            //Asistente/Optometrista
                            mandarAllamarSincronizar();
                            drawerLayout.closeDrawers();
                        }
                    }else {
                        //EN ESPERA
                        Toast.makeText(MainActivity.this, "Sincronizando, espera un momento porfavor...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //No se cuenta con internet
                    Toast.makeText(MainActivity.this, "Sin conexión a internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        llConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global = new Global(obtenerFragment());
                String descargaCompleta = global.obtenerAtributoTabla("GLOBAL", "DESCARGACOMPLETA", "ID", "1");
                if(descargaCompleta.contains("1")) {
                    //Descarga de datos fue insertada correctamente
                    obtenerRol = new ObtenerRol(obtenerFragment());
                    if (obtenerRol.obtenerAtributoUsuarioLogeado("ROL").equals("4")) {
                        //Rol cobranza
                        Fragment verificadorFragment = new configuracion();
                        FragmentTransaction transaction = ((FragmentActivity) obtenerFragment().getContext()).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content, verificadorFragment);
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        drawerLayout.closeDrawers();
                    } else {
                        //Asistente/Optometrista
                        Toast.makeText(MainActivity.this, "Por el momento no tienes acceso a esta vista", Toast.LENGTH_LONG).show();
                    }
                }else if(descargaCompleta.contains("4")){
                    //EN ESPERA
                    Toast.makeText(MainActivity.this, "Sincronizando, espera un momento porfavor...", Toast.LENGTH_LONG).show();
                }else {
                    //Descarga de datos incompleta o no alcanzo a responder el webservice
                    Toast.makeText(MainActivity.this, "Para continuar, es necesario sincronizar...", Toast.LENGTH_LONG).show();
                }
            }
        });

        llSupervision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (internet.verificarConexionInternet()) {
                    //Tiene internet
                    obtenerRol = new ObtenerRol(obtenerFragment());
                    if (obtenerRol.obtenerAtributoUsuarioLogeado("ROL").equals("4")) {
                        //Rol cobranza
                        Fragment verificadorFragment = new supervisionVehicular();
                        FragmentTransaction transaction = ((FragmentActivity) obtenerFragment().getContext()).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content, verificadorFragment);
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        drawerLayout.closeDrawers();
                    } else {
                        //Asistente/Optometrista
                        Toast.makeText(MainActivity.this, "Por el momento no tienes acceso a esta vista", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //No se cuenta con internet
                    Toast.makeText(MainActivity.this, "Sin conexión a internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        llReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global = new Global(obtenerFragment());
                if(!global.obtenerAtributoTabla("GLOBAL", "DESCARGACOMPLETA", "ID", "1").contains("4")) {
                    //Diferente a estado ESPERA
                    global.compartirArchivoErrores();
                    drawerLayout.closeDrawers();
                }else {
                    Toast.makeText(MainActivity.this, "Sincronizando, espera un momento porfavor...", Toast.LENGTH_LONG).show();
                }
            }
        });
        llAlmacenamientoImagenesContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global = new Global(obtenerFragment());
                if(!global.obtenerAtributoTabla("GLOBAL", "DESCARGACOMPLETA", "ID", "1").contains("4")) {
                    //Diferente a estado ESPERA

                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(obtenerFragment().getActivity());

                    //Inicializar dialog de adaptacion para zoom a imagen
                    inflater = (LayoutInflater) obtenerFragment().getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    searchView = inflater.inflate(R.layout.dialog_fotos_contrato, null);
                    //Crear e inicializar PthotoView
                    PhotoView adaptadorFotosContrato = (PhotoView) searchView.findViewById(R.id.adaptadorFotosContrato);
                    //Agregar a adaptador el mapa de bits de imagen descargada
                    adaptadorFotosContrato.setImageResource(R.drawable.carpetaoculta);

                    alerta.setView(searchView).setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Debe ir vacio por que se esta obteniendo abajo
                        }
                    });

                    final android.app.AlertDialog dialog = alerta.create();
                    dialog.show();

                    dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }

                    });

                } else {
                    Toast.makeText(MainActivity.this, "Sincronizando, espera un momento porfavor...", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvEliminarReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                global = new Global(obtenerFragment());
                String descargaCompleta = global.obtenerAtributoTabla("GLOBAL", "DESCARGACOMPLETA", "ID", "1");
                if(descargaCompleta.contains("1")) {
                    //Descarga de datos fue insertada correctamente

                    boolean mostrarAlerta = false;
                    Camara camara = new Camara(obtenerFragment());

                    if (!camara.mayRequestStoragePermission()) {
                        Toast.makeText(MainActivity.this, "Necesitas aceptar los permisos", Toast.LENGTH_LONG).show();
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        if (!Environment.isExternalStorageManager()) {
                            Toast.makeText(MainActivity.this, "Necesitas aceptar los permisos adicionales", Toast.LENGTH_LONG).show();
                            camara.mandarAPantallaConfiguracionTelefono();
                        } else {
                            camara.crearDirectorios();
                            camara.crearDirectorios();
                            mostrarAlerta = true;
                        }
                    } else {
                        camara.crearDirectorios();
                        camara.crearDirectorios();
                        mostrarAlerta = true;
                    }

                    if (mostrarAlerta) {
                        //mostrarAlerta = true

                        AlertDialog.Builder alerta = new AlertDialog.Builder(obtenerFragment().getContext());
                        alerta.setTitle("Confirmación").setMessage("¿Estas seguro de eliminar el reporte?").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                global.eliminarArchivo(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/luzatuvida/.nomedia/reporte.txt");
                                Toast.makeText(MainActivity.this, "Se elimino correctamente el reporte", Toast.LENGTH_LONG).show();
                                drawerLayout.closeDrawers();
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                    }

                }else if(descargaCompleta.contains("4")){
                    //EN ESPERA
                    Toast.makeText(MainActivity.this, "Sincronizando, espera un momento porfavor...", Toast.LENGTH_LONG).show();
                }else {
                    //Descarga de datos incompleta o no alcanzo a responder el webservice
                    Toast.makeText(MainActivity.this, "Para continuar, es necesario sincronizar...", Toast.LENGTH_LONG).show();
                }

            }
        });

        llBuzon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global = new Global(obtenerFragment());
                String descargaCompleta = global.obtenerAtributoTabla("GLOBAL", "DESCARGACOMPLETA", "ID", "1");
                if(descargaCompleta.contains("1")) {
                    //Descarga de datos fue insertada correctamente
                    Fragment verificadorFragment = new buzon();
                    FragmentTransaction transaction = ((FragmentActivity) obtenerFragment().getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, verificadorFragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    drawerLayout.closeDrawers();
                }else if(descargaCompleta.contains("4")){
                    //EN ESPERA
                    Toast.makeText(MainActivity.this, "Sincronizando, espera un momento porfavor...", Toast.LENGTH_LONG).show();
                }else {
                    //Descarga de datos incompleta o no alcanzo a responder el webservice
                    Toast.makeText(MainActivity.this, "Para continuar, es necesario sincronizar...", Toast.LENGTH_LONG).show();
                }
            }
        });

        llNotasCobranza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global = new Global(obtenerFragment());
                String descargaCompleta = global.obtenerAtributoTabla("GLOBAL", "DESCARGACOMPLETA", "ID", "1");
                if(descargaCompleta.contains("1")) {
                    //Descarga de datos fue insertada correctamente
                    Fragment verificadorFragment = new notascobranza();
                    FragmentTransaction transaction = ((FragmentActivity) obtenerFragment().getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, verificadorFragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    drawerLayout.closeDrawers();
                }else if(descargaCompleta.contains("4")){
                    //EN ESPERA
                    Toast.makeText(MainActivity.this, "Sincronizando, espera un momento porfavor...", Toast.LENGTH_LONG).show();
                }else {
                    //Descarga de datos incompleta o no alcanzo a responder el webservice
                    Toast.makeText(MainActivity.this, "Para continuar, es necesario sincronizar...", Toast.LENGTH_LONG).show();
                }
            }
        });
        llCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (internet.verificarConexionInternet()) {
                    global = new Global(obtenerFragment());
                    if(!global.obtenerAtributoTabla("GLOBAL", "DESCARGACOMPLETA", "ID", "1").contains("4")) {
                        //Diferente a estado ESPERA
                        sincronizacion = new Sincronizacion(obtenerFragment(), true);
                        obtenerRol = new ObtenerRol(obtenerFragment());
                        Object[] objetosWebService = {obtenerRol.obtenerAtributoUsuarioLogeado("TOKEN"), "", ""};
                        sincronizacion.sincronizarMetodo(0, objetosWebService, 1);
                    }else {
                        Toast.makeText(MainActivity.this, "Sincronizando, espera un momento porfavor...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Sin conexión a internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        Handler handler = new Handler();
        runnable = new Runnable() {
            public void run() {

                if (internet.verificarConexionInternet()) {
                    toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
                } else {
                    toolbar.setTitleTextColor(Color.parseColor("#30ffffff"));
                }
                handler.postDelayed(this, 3000);
            }
        };
        runnable.run();

        //Eventos para saber cuando se abre, cierra, etc el menu principal
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                global = new Global(obtenerFragment());
                String tamanoArchivo = global.obtenerTamanoArchivo(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/luzatuvida/.nomedia/reporte.txt");
                if(tamanoArchivo.length() > 0) {
                    //Existe archivo
                    llReporte.setVisibility(View.VISIBLE);
                    tvEliminarReporte.setVisibility(View.VISIBLE);
                    tvEliminarReporte.setText("Eliminar reporte (" + tamanoArchivo + ")");
                }else {
                    //No existe archivo
                    llReporte.setVisibility(View.GONE);
                    tvEliminarReporte.setVisibility(View.GONE);
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    private ActionBarDrawerToggle setUpDrawerToogle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
        lockDrawer();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    private void mandarAllamarSincronizar() {
        sincronizacion = new Sincronizacion(obtenerFragment(), true);
        sincronizacion.actualizarBanderaDescargaCompleta("0");
        obtenerRol = new ObtenerRol(obtenerFragment());
        Object[] objetosWebService = {obtenerRol.obtenerAtributoUsuarioLogeado("TOKEN"), "", ""};
        sincronizacion.sincronizarMetodo(2, objetosWebService, 0);
        Fragment verificadorFragment = new principal(false);
        FragmentTransaction transaction = ((FragmentActivity) obtenerFragment().getContext()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, verificadorFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
        lockDrawer();
        hideMenuHamburguesa();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Handle item selection
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return guardarYCheckearItem(item);
        //return super.onOptionsItemSelected(item);

    }

    private boolean guardarYCheckearItem(MenuItem item) {

        try {
            //Guardar el valor del check seleccionado

            if (item.getItemId() == R.id.item_todos) {
                item.setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaTodos", "1");
                menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
            } else {
                seguridad.guardarSharedPreferences("valorHamburguesaTodos", "0");
                menuHamburguesa.findItem(R.id.item_todos).setChecked(false);
            }

            if (item.getItemId() == R.id.item_generalruta) {
                item.setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaRuta", "0");
                menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                menuHamburguesa.findItem(R.id.item_lunesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercolesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_juevesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabadoruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingoruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_todos).setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaTodos", "1");
            } else {
                menuHamburguesa.findItem(R.id.item_generalruta).setChecked(false);
            }

            if (item.getItemId() == R.id.item_lunesruta) {
                item.setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaRuta", "1");
                menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                menuHamburguesa.findItem(R.id.item_generalruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercolesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_juevesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabadoruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingoruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_todos).setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaTodos", "1");
            } else {
                menuHamburguesa.findItem(R.id.item_lunesruta).setChecked(false);
            }

            if (item.getItemId() == R.id.item_martesruta) {
                item.setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaRuta", "2");
                menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                menuHamburguesa.findItem(R.id.item_generalruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_lunesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercolesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_juevesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabadoruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingoruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_todos).setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaTodos", "1");
            } else {
                menuHamburguesa.findItem(R.id.item_martesruta).setChecked(false);
            }

            if (item.getItemId() == R.id.item_miercolesruta) {
                item.setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaRuta", "3");
                menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                menuHamburguesa.findItem(R.id.item_generalruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_lunesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_juevesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabadoruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingoruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_todos).setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaTodos", "1");
            } else {
                menuHamburguesa.findItem(R.id.item_miercolesruta).setChecked(false);
            }

            if (item.getItemId() == R.id.item_juevesruta) {
                item.setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaRuta", "4");
                menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                menuHamburguesa.findItem(R.id.item_generalruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_lunesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercolesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabadoruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingoruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_todos).setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaTodos", "1");
            } else {
                menuHamburguesa.findItem(R.id.item_juevesruta).setChecked(false);
            }

            if (item.getItemId() == R.id.item_viernesruta) {
                item.setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaRuta", "5");
                menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                menuHamburguesa.findItem(R.id.item_generalruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_lunesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercolesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_juevesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabadoruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingoruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_todos).setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaTodos", "1");
            } else {
                menuHamburguesa.findItem(R.id.item_viernesruta).setChecked(false);
            }

            if (item.getItemId() == R.id.item_sabadoruta) {
                item.setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaRuta", "6");
                menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                menuHamburguesa.findItem(R.id.item_generalruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_lunesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercolesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_juevesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingoruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_todos).setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaTodos", "1");
            } else {
                menuHamburguesa.findItem(R.id.item_sabadoruta).setChecked(false);
            }

            if (item.getItemId() == R.id.item_domingoruta) {
                item.setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaRuta", "7");
                menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                menuHamburguesa.findItem(R.id.item_generalruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_lunesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_martesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_miercolesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_juevesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_viernesruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_sabadoruta).setChecked(false);
                menuHamburguesa.findItem(R.id.item_todos).setChecked(true);
                seguridad.guardarSharedPreferences("valorHamburguesaTodos", "1");
            } else {
                menuHamburguesa.findItem(R.id.item_domingoruta).setChecked(false);
            }

            if (item.getItemId() == R.id.item_lunes) {
                if(menuHamburguesa.findItem(R.id.item_lunes).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaLunes", "0");
                    menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                }else {
                    item.setChecked(true);
                    seguridad.guardarSharedPreferences("valorHamburguesaLunes", "1");
                    menuHamburguesa.findItem(R.id.item_todos).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                    seguridad.guardarSharedPreferences("valorHamburguesaRuta", "8");
                }
            } else {
                if(!menuHamburguesa.findItem(R.id.item_lunes).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaLunes", "0");
                    menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                }
            }

            if (item.getItemId() == R.id.item_martes) {
                if(menuHamburguesa.findItem(R.id.item_martes).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaMartes", "0");
                    menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                }else {
                    item.setChecked(true);
                    seguridad.guardarSharedPreferences("valorHamburguesaMartes", "1");
                    menuHamburguesa.findItem(R.id.item_todos).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                    seguridad.guardarSharedPreferences("valorHamburguesaRuta", "8");
                }
            } else {
                if(!menuHamburguesa.findItem(R.id.item_martes).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaMartes", "0");
                    menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                }
            }

            if (item.getItemId() == R.id.item_miercoles) {
                if(menuHamburguesa.findItem(R.id.item_miercoles).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaMiercoles", "0");
                    menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                }else {
                    item.setChecked(true);
                    seguridad.guardarSharedPreferences("valorHamburguesaMiercoles", "1");
                    menuHamburguesa.findItem(R.id.item_todos).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                    seguridad.guardarSharedPreferences("valorHamburguesaRuta", "8");
                }
            } else {
                if(!menuHamburguesa.findItem(R.id.item_miercoles).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaMiercoles", "0");
                    menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                }
            }

            if (item.getItemId() == R.id.item_jueves) {
                if(menuHamburguesa.findItem(R.id.item_jueves).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaJueves", "0");
                    menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                }else {
                    item.setChecked(true);
                    seguridad.guardarSharedPreferences("valorHamburguesaJueves", "1");
                    menuHamburguesa.findItem(R.id.item_todos).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                    seguridad.guardarSharedPreferences("valorHamburguesaRuta", "8");
                }
            } else {
                if(!menuHamburguesa.findItem(R.id.item_jueves).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaJueves", "0");
                    menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                }
            }

            if (item.getItemId() == R.id.item_viernes) {
                if(menuHamburguesa.findItem(R.id.item_viernes).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaViernes", "0");
                    menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                }else {
                    item.setChecked(true);
                    seguridad.guardarSharedPreferences("valorHamburguesaViernes", "1");
                    menuHamburguesa.findItem(R.id.item_todos).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                    seguridad.guardarSharedPreferences("valorHamburguesaRuta", "8");
                }
            } else {
                if(!menuHamburguesa.findItem(R.id.item_viernes).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaViernes", "0");
                    menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                }
            }

            if (item.getItemId() == R.id.item_sabado) {
                if(menuHamburguesa.findItem(R.id.item_sabado).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaSabado", "0");
                    menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                }else {
                    item.setChecked(true);
                    seguridad.guardarSharedPreferences("valorHamburguesaSabado", "1");
                    menuHamburguesa.findItem(R.id.item_todos).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                    seguridad.guardarSharedPreferences("valorHamburguesaRuta", "8");
                }
            } else {
                if(!menuHamburguesa.findItem(R.id.item_sabado).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaSabado", "0");
                    menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                }
            }

            if (item.getItemId() == R.id.item_domingo) {
                if(menuHamburguesa.findItem(R.id.item_domingo).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaDomingo", "0");
                    menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                }else {
                    item.setChecked(true);
                    seguridad.guardarSharedPreferences("valorHamburguesaDomingo", "1");
                    menuHamburguesa.findItem(R.id.item_todos).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                    seguridad.guardarSharedPreferences("valorHamburguesaRuta", "8");
                }
            } else {
                if(!menuHamburguesa.findItem(R.id.item_domingo).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaDomingo", "0");
                    menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                }
            }

            if (item.getItemId() == R.id.item_contado) {
                if(menuHamburguesa.findItem(R.id.item_contado).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaContado", "0");
                    menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                }else {
                    item.setChecked(true);
                    seguridad.guardarSharedPreferences("valorHamburguesaContado", "1");
                    menuHamburguesa.findItem(R.id.item_todos).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                    seguridad.guardarSharedPreferences("valorHamburguesaRuta", "8");
                }
            } else {
                if(!menuHamburguesa.findItem(R.id.item_contado).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaContado", "0");
                    menuHamburguesa.findItem(R.id.item_contado).setChecked(false);
                }
            }

            if (item.getItemId() == R.id.item_semanal) {
                if(menuHamburguesa.findItem(R.id.item_semanal).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaSemanal", "0");
                    menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                }else {
                    item.setChecked(true);
                    seguridad.guardarSharedPreferences("valorHamburguesaSemanal", "1");
                    menuHamburguesa.findItem(R.id.item_todos).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                    seguridad.guardarSharedPreferences("valorHamburguesaRuta", "8");
                }
            } else {
                if(!menuHamburguesa.findItem(R.id.item_semanal).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaSemanal", "0");
                    menuHamburguesa.findItem(R.id.item_semanal).setChecked(false);
                }
            }

            if (item.getItemId() == R.id.item_quincenal) {
                if(menuHamburguesa.findItem(R.id.item_quincenal).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaQuincenal", "0");
                    menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                }else {
                    item.setChecked(true);
                    seguridad.guardarSharedPreferences("valorHamburguesaQuincenal", "1");
                    menuHamburguesa.findItem(R.id.item_todos).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                    seguridad.guardarSharedPreferences("valorHamburguesaRuta", "8");
                }
            } else {
                if(!menuHamburguesa.findItem(R.id.item_quincenal).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaQuincenal", "0");
                    menuHamburguesa.findItem(R.id.item_quincenal).setChecked(false);
                }
            }

            if (item.getItemId() == R.id.item_mensual) {
                if(menuHamburguesa.findItem(R.id.item_mensual).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaMensual", "0");
                    menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                }else {
                    item.setChecked(true);
                    seguridad.guardarSharedPreferences("valorHamburguesaMensual", "1");
                    menuHamburguesa.findItem(R.id.item_todos).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_lunes).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_martes).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_miercoles).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_jueves).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_viernes).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_sabado).setChecked(false);
                    menuHamburguesa.findItem(R.id.item_domingo).setChecked(false);
                    seguridad.guardarSharedPreferences("valorHamburguesaRuta", "8");
                }
            } else {
                if(!menuHamburguesa.findItem(R.id.item_mensual).isChecked()) {
                    seguridad.guardarSharedPreferences("valorHamburguesaMensual", "0");
                    menuHamburguesa.findItem(R.id.item_mensual).setChecked(false);
                }
            }

            if(!menuHamburguesa.findItem(R.id.item_lunes).isChecked() && !menuHamburguesa.findItem(R.id.item_martes).isChecked()
                && !menuHamburguesa.findItem(R.id.item_miercoles).isChecked() && !menuHamburguesa.findItem(R.id.item_jueves).isChecked()
                && !menuHamburguesa.findItem(R.id.item_viernes).isChecked() && !menuHamburguesa.findItem(R.id.item_sabado).isChecked()
                && !menuHamburguesa.findItem(R.id.item_domingo).isChecked() && !menuHamburguesa.findItem(R.id.item_contado).isChecked()
                && !menuHamburguesa.findItem(R.id.item_semanal).isChecked() && !menuHamburguesa.findItem(R.id.item_quincenal).isChecked()
                && !menuHamburguesa.findItem(R.id.item_mensual).isChecked() && !menuHamburguesa.findItem(R.id.item_lunesruta).isChecked()
                && !menuHamburguesa.findItem(R.id.item_martesruta).isChecked() && !menuHamburguesa.findItem(R.id.item_miercolesruta).isChecked()
                && !menuHamburguesa.findItem(R.id.item_juevesruta).isChecked() && !menuHamburguesa.findItem(R.id.item_viernesruta).isChecked()
                && !menuHamburguesa.findItem(R.id.item_sabadoruta).isChecked() && !menuHamburguesa.findItem(R.id.item_domingoruta).isChecked()) {
                seguridad.guardarSharedPreferences("valorHamburguesaTodos", "1");
                seguridad.guardarSharedPreferences("valorHamburguesaRuta", "0");
            }


            //Mandar a llamar pantalla principal
            Fragment verificadorFragment = new principal(false);
            FragmentTransaction transaction = ((FragmentActivity) obtenerFragment().getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, verificadorFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.addToBackStack(null);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void lockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toolbar.setNavigationIcon(null);
    }

    @Override
    public void unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private Fragment obtenerFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    return fragment;
                }
            }
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Creacion del menuHamburguesa
        menuHamburguesa = menu;
        getMenuInflater().inflate(R.menu.drawer_menu_hamburguesa, menuHamburguesa);
        MenuCompat.setGroupDividerEnabled(menuHamburguesa, true);
        hideMenuHamburguesa();
        return true;
    }

    @Override
    public void showMenuHamburguesa() {
        //Mostrar menuHamburguesa
        menuHamburguesa.setGroupVisible(R.id.groupPrimero, true);
        menuHamburguesa.setGroupVisible(R.id.groupSegundo, true);
        menuHamburguesa.setGroupVisible(R.id.groupTercero, true);
        menuHamburguesa.setGroupVisible(R.id.groupCuarto, true);
    }

    @Override
    public void hideMenuHamburguesa() {
        //Ocultar menuHamburguesa
        menuHamburguesa.setGroupVisible(R.id.groupPrimero, false);
        menuHamburguesa.setGroupVisible(R.id.groupSegundo, false);
        menuHamburguesa.setGroupVisible(R.id.groupTercero, false);
        menuHamburguesa.setGroupVisible(R.id.groupCuarto, false);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //Se ejecuta cada vez que se habre la aplicacion

        seguridad = new Seguridad(obtenerFragment());
        try {

            //Obtener valoresHamburguesa
            boolean valorHamburguesaTodos = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaTodos"));
            boolean valorHamburguesaLunes = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaLunes"));
            boolean valorHamburguesaMartes = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaMartes"));
            boolean valorHamburguesaMiercoles = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaMiercoles"));
            boolean valorHamburguesaJueves = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaJueves"));
            boolean valorHamburguesaViernes = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaViernes"));
            boolean valorHamburguesaSabado = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaSabado"));
            boolean valorHamburguesaDomingo = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaDomingo"));
            boolean valorHamburguesaContado = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaContado"));
            boolean valorHamburguesaSemanal = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaSemanal"));
            boolean valorHamburguesaQuincenal = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaQuincenal"));
            boolean valorHamburguesaMensual = "1".equals(seguridad.leerSharedPreferences("valorHamburguesaMensual"));
            String valorHamburguesaRuta = seguridad.leerSharedPreferences("valorHamburguesaRuta");

            if(!valorHamburguesaLunes && !valorHamburguesaMartes && !valorHamburguesaMiercoles && !valorHamburguesaJueves
                    && !valorHamburguesaViernes && !valorHamburguesaSabado && !valorHamburguesaDomingo
                    && !valorHamburguesaContado && !valorHamburguesaSemanal && !valorHamburguesaQuincenal
                    && !valorHamburguesaMensual && !valorHamburguesaRuta.equals("0")){
                valorHamburguesaTodos = true;
                seguridad.guardarSharedPreferences("valorHamburguesaTodos", "1");
                seguridad.guardarSharedPreferences("valorHamburguesaRuta", valorHamburguesaRuta);
            }

            //Verificar el valorHamburguesa donde se habia quedado guardado y checkear ese RadioButton
            if(valorHamburguesaTodos) {
                menu.findItem(R.id.item_todos).setChecked(true); //Se activa el todos
                menu.findItem(R.id.item_martes).setChecked(false);
                menu.findItem(R.id.item_miercoles).setChecked(false);
                menu.findItem(R.id.item_jueves).setChecked(false);
                menu.findItem(R.id.item_viernes).setChecked(false);
                menu.findItem(R.id.item_sabado).setChecked(false);
                menu.findItem(R.id.item_domingo).setChecked(false);
                menu.findItem(R.id.item_contado).setChecked(false);
                menu.findItem(R.id.item_semanal).setChecked(false);
                menu.findItem(R.id.item_quincenal).setChecked(false);
                menu.findItem(R.id.item_mensual).setChecked(false);
                menu.findItem(R.id.item_lunes).setChecked(false);
                menu.findItem(R.id.item_generalruta).setChecked(true); //Se activa el ruta general
                menu.findItem(R.id.item_lunesruta).setChecked(false);
                menu.findItem(R.id.item_martesruta).setChecked(false);
                menu.findItem(R.id.item_miercolesruta).setChecked(false);
                menu.findItem(R.id.item_juevesruta).setChecked(false);
                menu.findItem(R.id.item_viernesruta).setChecked(false);
                menu.findItem(R.id.item_sabadoruta).setChecked(false);
                menu.findItem(R.id.item_domingoruta).setChecked(false);
            }else {
                menu.findItem(R.id.item_todos).setChecked(false);
            }

            switch (valorHamburguesaRuta) {
                case "1": //Lunes
                    menu.findItem(R.id.item_todos).setChecked(true); //Se activa el todos
                    menu.findItem(R.id.item_martes).setChecked(false);
                    menu.findItem(R.id.item_miercoles).setChecked(false);
                    menu.findItem(R.id.item_jueves).setChecked(false);
                    menu.findItem(R.id.item_viernes).setChecked(false);
                    menu.findItem(R.id.item_sabado).setChecked(false);
                    menu.findItem(R.id.item_domingo).setChecked(false);
                    menu.findItem(R.id.item_contado).setChecked(false);
                    menu.findItem(R.id.item_semanal).setChecked(false);
                    menu.findItem(R.id.item_quincenal).setChecked(false);
                    menu.findItem(R.id.item_mensual).setChecked(false);
                    menu.findItem(R.id.item_lunes).setChecked(false);
                    menu.findItem(R.id.item_generalruta).setChecked(false);
                    menu.findItem(R.id.item_lunesruta).setChecked(true); //Se activa el ruta lunes
                    menu.findItem(R.id.item_martesruta).setChecked(false);
                    menu.findItem(R.id.item_miercolesruta).setChecked(false);
                    menu.findItem(R.id.item_juevesruta).setChecked(false);
                    menu.findItem(R.id.item_viernesruta).setChecked(false);
                    menu.findItem(R.id.item_sabadoruta).setChecked(false);
                    menu.findItem(R.id.item_domingoruta).setChecked(false);
                    break;
                case "2": //Martes
                    menu.findItem(R.id.item_todos).setChecked(true); //Se activa el todos
                    menu.findItem(R.id.item_martes).setChecked(false);
                    menu.findItem(R.id.item_miercoles).setChecked(false);
                    menu.findItem(R.id.item_jueves).setChecked(false);
                    menu.findItem(R.id.item_viernes).setChecked(false);
                    menu.findItem(R.id.item_sabado).setChecked(false);
                    menu.findItem(R.id.item_domingo).setChecked(false);
                    menu.findItem(R.id.item_contado).setChecked(false);
                    menu.findItem(R.id.item_semanal).setChecked(false);
                    menu.findItem(R.id.item_quincenal).setChecked(false);
                    menu.findItem(R.id.item_mensual).setChecked(false);
                    menu.findItem(R.id.item_lunes).setChecked(false);
                    menu.findItem(R.id.item_generalruta).setChecked(false);
                    menu.findItem(R.id.item_lunesruta).setChecked(false);
                    menu.findItem(R.id.item_martesruta).setChecked(true); //Se activa el ruta martes
                    menu.findItem(R.id.item_miercolesruta).setChecked(false);
                    menu.findItem(R.id.item_juevesruta).setChecked(false);
                    menu.findItem(R.id.item_viernesruta).setChecked(false);
                    menu.findItem(R.id.item_sabadoruta).setChecked(false);
                    menu.findItem(R.id.item_domingoruta).setChecked(false);
                    break;
                case "3": //Miercoles
                    menu.findItem(R.id.item_todos).setChecked(true); //Se activa el todos
                    menu.findItem(R.id.item_martes).setChecked(false);
                    menu.findItem(R.id.item_miercoles).setChecked(false);
                    menu.findItem(R.id.item_jueves).setChecked(false);
                    menu.findItem(R.id.item_viernes).setChecked(false);
                    menu.findItem(R.id.item_sabado).setChecked(false);
                    menu.findItem(R.id.item_domingo).setChecked(false);
                    menu.findItem(R.id.item_contado).setChecked(false);
                    menu.findItem(R.id.item_semanal).setChecked(false);
                    menu.findItem(R.id.item_quincenal).setChecked(false);
                    menu.findItem(R.id.item_mensual).setChecked(false);
                    menu.findItem(R.id.item_lunes).setChecked(false);
                    menu.findItem(R.id.item_generalruta).setChecked(false);
                    menu.findItem(R.id.item_lunesruta).setChecked(false);
                    menu.findItem(R.id.item_martesruta).setChecked(false);
                    menu.findItem(R.id.item_miercolesruta).setChecked(true); //Se activa el ruta miercoles
                    menu.findItem(R.id.item_juevesruta).setChecked(false);
                    menu.findItem(R.id.item_viernesruta).setChecked(false);
                    menu.findItem(R.id.item_sabadoruta).setChecked(false);
                    menu.findItem(R.id.item_domingoruta).setChecked(false);
                    break;
                case "4": //Jueves
                    menu.findItem(R.id.item_todos).setChecked(true); //Se activa el todos
                    menu.findItem(R.id.item_martes).setChecked(false);
                    menu.findItem(R.id.item_miercoles).setChecked(false);
                    menu.findItem(R.id.item_jueves).setChecked(false);
                    menu.findItem(R.id.item_viernes).setChecked(false);
                    menu.findItem(R.id.item_sabado).setChecked(false);
                    menu.findItem(R.id.item_domingo).setChecked(false);
                    menu.findItem(R.id.item_contado).setChecked(false);
                    menu.findItem(R.id.item_semanal).setChecked(false);
                    menu.findItem(R.id.item_quincenal).setChecked(false);
                    menu.findItem(R.id.item_mensual).setChecked(false);
                    menu.findItem(R.id.item_lunes).setChecked(false);
                    menu.findItem(R.id.item_generalruta).setChecked(false);
                    menu.findItem(R.id.item_lunesruta).setChecked(false);
                    menu.findItem(R.id.item_martesruta).setChecked(false);
                    menu.findItem(R.id.item_miercolesruta).setChecked(false);
                    menu.findItem(R.id.item_juevesruta).setChecked(true); //Se activa el ruta jueves
                    menu.findItem(R.id.item_viernesruta).setChecked(false);
                    menu.findItem(R.id.item_sabadoruta).setChecked(false);
                    menu.findItem(R.id.item_domingoruta).setChecked(false);
                    break;
                case "5": //Viernes
                    menu.findItem(R.id.item_todos).setChecked(true); //Se activa el todos
                    menu.findItem(R.id.item_martes).setChecked(false);
                    menu.findItem(R.id.item_miercoles).setChecked(false);
                    menu.findItem(R.id.item_jueves).setChecked(false);
                    menu.findItem(R.id.item_viernes).setChecked(false);
                    menu.findItem(R.id.item_sabado).setChecked(false);
                    menu.findItem(R.id.item_domingo).setChecked(false);
                    menu.findItem(R.id.item_contado).setChecked(false);
                    menu.findItem(R.id.item_semanal).setChecked(false);
                    menu.findItem(R.id.item_quincenal).setChecked(false);
                    menu.findItem(R.id.item_mensual).setChecked(false);
                    menu.findItem(R.id.item_lunes).setChecked(false);
                    menu.findItem(R.id.item_generalruta).setChecked(false);
                    menu.findItem(R.id.item_lunesruta).setChecked(false);
                    menu.findItem(R.id.item_martesruta).setChecked(false);
                    menu.findItem(R.id.item_miercolesruta).setChecked(false);
                    menu.findItem(R.id.item_juevesruta).setChecked(false);
                    menu.findItem(R.id.item_viernesruta).setChecked(true); //Se activa el ruta viernes
                    menu.findItem(R.id.item_sabadoruta).setChecked(false);
                    menu.findItem(R.id.item_domingoruta).setChecked(false);
                    break;
                case "6": //Sabado
                    menu.findItem(R.id.item_todos).setChecked(true); //Se activa el todos
                    menu.findItem(R.id.item_martes).setChecked(false);
                    menu.findItem(R.id.item_miercoles).setChecked(false);
                    menu.findItem(R.id.item_jueves).setChecked(false);
                    menu.findItem(R.id.item_viernes).setChecked(false);
                    menu.findItem(R.id.item_sabado).setChecked(false);
                    menu.findItem(R.id.item_domingo).setChecked(false);
                    menu.findItem(R.id.item_contado).setChecked(false);
                    menu.findItem(R.id.item_semanal).setChecked(false);
                    menu.findItem(R.id.item_quincenal).setChecked(false);
                    menu.findItem(R.id.item_mensual).setChecked(false);
                    menu.findItem(R.id.item_lunes).setChecked(false);
                    menu.findItem(R.id.item_generalruta).setChecked(false);
                    menu.findItem(R.id.item_lunesruta).setChecked(false);
                    menu.findItem(R.id.item_martesruta).setChecked(false);
                    menu.findItem(R.id.item_miercolesruta).setChecked(false);
                    menu.findItem(R.id.item_juevesruta).setChecked(false);
                    menu.findItem(R.id.item_viernesruta).setChecked(false);
                    menu.findItem(R.id.item_sabadoruta).setChecked(true); //Se activa el ruta sabado
                    menu.findItem(R.id.item_domingoruta).setChecked(false);
                    break;
                case "7": //Domingo
                    menu.findItem(R.id.item_todos).setChecked(true); //Se activa el todos
                    menu.findItem(R.id.item_martes).setChecked(false);
                    menu.findItem(R.id.item_miercoles).setChecked(false);
                    menu.findItem(R.id.item_jueves).setChecked(false);
                    menu.findItem(R.id.item_viernes).setChecked(false);
                    menu.findItem(R.id.item_sabado).setChecked(false);
                    menu.findItem(R.id.item_domingo).setChecked(false);
                    menu.findItem(R.id.item_contado).setChecked(false);
                    menu.findItem(R.id.item_semanal).setChecked(false);
                    menu.findItem(R.id.item_quincenal).setChecked(false);
                    menu.findItem(R.id.item_mensual).setChecked(false);
                    menu.findItem(R.id.item_lunes).setChecked(false);
                    menu.findItem(R.id.item_generalruta).setChecked(false);
                    menu.findItem(R.id.item_lunesruta).setChecked(false);
                    menu.findItem(R.id.item_martesruta).setChecked(false);
                    menu.findItem(R.id.item_miercolesruta).setChecked(false);
                    menu.findItem(R.id.item_juevesruta).setChecked(false);
                    menu.findItem(R.id.item_viernesruta).setChecked(false);
                    menu.findItem(R.id.item_sabadoruta).setChecked(false);
                    menu.findItem(R.id.item_domingoruta).setChecked(true); //Se activa el ruta domingo
                    break;
                case "8": //Descheckear todos los checkbox de ruta
                    menu.findItem(R.id.item_generalruta).setChecked(false);
                    menu.findItem(R.id.item_lunesruta).setChecked(false);
                    menu.findItem(R.id.item_martesruta).setChecked(false);
                    menu.findItem(R.id.item_miercolesruta).setChecked(false);
                    menu.findItem(R.id.item_juevesruta).setChecked(false);
                    menu.findItem(R.id.item_viernesruta).setChecked(false);
                    menu.findItem(R.id.item_sabadoruta).setChecked(false);
                    menu.findItem(R.id.item_domingoruta).setChecked(false);
                    break;
                default: //Valor vacio o igual a 0 (General)
                    menu.findItem(R.id.item_todos).setChecked(true); //Se activa el todos
                    menu.findItem(R.id.item_martes).setChecked(false);
                    menu.findItem(R.id.item_miercoles).setChecked(false);
                    menu.findItem(R.id.item_jueves).setChecked(false);
                    menu.findItem(R.id.item_viernes).setChecked(false);
                    menu.findItem(R.id.item_sabado).setChecked(false);
                    menu.findItem(R.id.item_domingo).setChecked(false);
                    menu.findItem(R.id.item_contado).setChecked(false);
                    menu.findItem(R.id.item_semanal).setChecked(false);
                    menu.findItem(R.id.item_quincenal).setChecked(false);
                    menu.findItem(R.id.item_mensual).setChecked(false);
                    menu.findItem(R.id.item_lunes).setChecked(false);
                    menu.findItem(R.id.item_generalruta).setChecked(true); //Se activa el ruta general
                    menu.findItem(R.id.item_lunesruta).setChecked(false);
                    menu.findItem(R.id.item_martesruta).setChecked(false);
                    menu.findItem(R.id.item_miercolesruta).setChecked(false);
                    menu.findItem(R.id.item_juevesruta).setChecked(false);
                    menu.findItem(R.id.item_viernesruta).setChecked(false);
                    menu.findItem(R.id.item_sabadoruta).setChecked(false);
                    menu.findItem(R.id.item_domingoruta).setChecked(false);
                    break;
            }

            if(valorHamburguesaLunes) {
                menu.findItem(R.id.item_lunes).setChecked(true);
                menu.findItem(R.id.item_contado).setChecked(false);
                menu.findItem(R.id.item_semanal).setChecked(false);
                menu.findItem(R.id.item_quincenal).setChecked(false);
                menu.findItem(R.id.item_mensual).setChecked(false);
            }

            if(valorHamburguesaMartes) {
                menu.findItem(R.id.item_martes).setChecked(true);
                menu.findItem(R.id.item_contado).setChecked(false);
                menu.findItem(R.id.item_semanal).setChecked(false);
                menu.findItem(R.id.item_quincenal).setChecked(false);
                menu.findItem(R.id.item_mensual).setChecked(false);
            }

            if(valorHamburguesaMiercoles) {
                menu.findItem(R.id.item_miercoles).setChecked(true);
                menu.findItem(R.id.item_contado).setChecked(false);
                menu.findItem(R.id.item_semanal).setChecked(false);
                menu.findItem(R.id.item_quincenal).setChecked(false);
                menu.findItem(R.id.item_mensual).setChecked(false);
            }

            if(valorHamburguesaJueves) {
                menu.findItem(R.id.item_jueves).setChecked(true);
                menu.findItem(R.id.item_contado).setChecked(false);
                menu.findItem(R.id.item_semanal).setChecked(false);
                menu.findItem(R.id.item_quincenal).setChecked(false);
                menu.findItem(R.id.item_mensual).setChecked(false);
            }

            if(valorHamburguesaViernes) {
                menu.findItem(R.id.item_viernes).setChecked(true);
                menu.findItem(R.id.item_contado).setChecked(false);
                menu.findItem(R.id.item_semanal).setChecked(false);
                menu.findItem(R.id.item_quincenal).setChecked(false);
                menu.findItem(R.id.item_mensual).setChecked(false);
            }

            if(valorHamburguesaSabado) {
                menu.findItem(R.id.item_sabado).setChecked(true);
                menu.findItem(R.id.item_contado).setChecked(false);
                menu.findItem(R.id.item_semanal).setChecked(false);
                menu.findItem(R.id.item_quincenal).setChecked(false);
                menu.findItem(R.id.item_mensual).setChecked(false);
            }

            if(valorHamburguesaDomingo) {
                menu.findItem(R.id.item_domingo).setChecked(true);
                menu.findItem(R.id.item_contado).setChecked(false);
                menu.findItem(R.id.item_semanal).setChecked(false);
                menu.findItem(R.id.item_quincenal).setChecked(false);
                menu.findItem(R.id.item_mensual).setChecked(false);
            }

            if(valorHamburguesaContado) {
                menu.findItem(R.id.item_contado).setChecked(true);
                menu.findItem(R.id.item_lunes).setChecked(false);
                menu.findItem(R.id.item_martes).setChecked(false);
                menu.findItem(R.id.item_miercoles).setChecked(false);
                menu.findItem(R.id.item_jueves).setChecked(false);
                menu.findItem(R.id.item_viernes).setChecked(false);
                menu.findItem(R.id.item_sabado).setChecked(false);
                menu.findItem(R.id.item_domingo).setChecked(false);
            }

            if(valorHamburguesaSemanal) {
                menu.findItem(R.id.item_semanal).setChecked(true);
                menu.findItem(R.id.item_lunes).setChecked(false);
                menu.findItem(R.id.item_martes).setChecked(false);
                menu.findItem(R.id.item_miercoles).setChecked(false);
                menu.findItem(R.id.item_jueves).setChecked(false);
                menu.findItem(R.id.item_viernes).setChecked(false);
                menu.findItem(R.id.item_sabado).setChecked(false);
                menu.findItem(R.id.item_domingo).setChecked(false);
            }

            if(valorHamburguesaQuincenal) {
                menu.findItem(R.id.item_quincenal).setChecked(true);
                menu.findItem(R.id.item_lunes).setChecked(false);
                menu.findItem(R.id.item_martes).setChecked(false);
                menu.findItem(R.id.item_miercoles).setChecked(false);
                menu.findItem(R.id.item_jueves).setChecked(false);
                menu.findItem(R.id.item_viernes).setChecked(false);
                menu.findItem(R.id.item_sabado).setChecked(false);
                menu.findItem(R.id.item_domingo).setChecked(false);
            }

            if(valorHamburguesaMensual) {
                menu.findItem(R.id.item_mensual).setChecked(true);
                menu.findItem(R.id.item_lunes).setChecked(false);
                menu.findItem(R.id.item_martes).setChecked(false);
                menu.findItem(R.id.item_miercoles).setChecked(false);
                menu.findItem(R.id.item_jueves).setChecked(false);
                menu.findItem(R.id.item_viernes).setChecked(false);
                menu.findItem(R.id.item_sabado).setChecked(false);
                menu.findItem(R.id.item_domingo).setChecked(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}