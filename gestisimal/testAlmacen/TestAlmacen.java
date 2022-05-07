package gestisimal.testAlmacen;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
import gestisimal.articuloAlmacen.Almacen;
import gestisimal.articuloAlmacen.ArticuloEnAlmacenException;
import gestisimal.articuloAlmacen.ArticuloNoEncontradoException;
import gestisimal.articuloAlmacen.StockIllegalException;
import menu.Menu;
import util.Util;
/**
 * 
 * @author Hugo Vicente Peligro
 *
 *
 */
public class TestAlmacen {
  private static Almacen almacen = new Almacen();

  /**
   * Menu del programa
   * @param args No se utiliza
   */
  public static void main(String[] args){
    Menu menu = createMenu();
    rellenarAlmacen();

    int opt;
    do {
      opt = menu.choose();
      switch (opt) {
        case 1 -> anadirArticulo();
        case 2 -> eliminarArticulo();
        case 3 -> anadirUnidades();
        case 4 -> eliminarUnidades();
        case 5 -> modificarArticulo();
        case 6 -> guardarJson();
        case 7 -> almacenAPartirDeJson();
        case 8 -> guardarXml();
        case 9 -> almacenAPartirDeXml();
        case 10 -> mostrarAlmacen();
      } 
    } while (opt != menu.lastOption());
    System.out.println("¡Hasta la próxima!");

  }

  /**
   * Menu principal de la aplicacion
   * @return Opciones del menu de almacen
   */
  private static Menu createMenu() {
    return new Menu("Menu de opciones","Añadir artículo", "Eliminar artículo",
        "Añadir unidades de un artículo", "Eliminar unidades de un artículo",
        "Modificar artículo del almacen","Guardar almacén en JSON",
        "Crear almacén a partir de JSON","Guardar almacén en XML",
        "Crear almacén a partir de XML",
        "Mostrar almacén", "Salir del programa");
  }
  /**
   * rellena el almacen con 5 articulos
   */
  private static void rellenarAlmacen() {
    for(int i = 1; i <= 5; i++) {
      try {
        almacen.incluirArticulo("Artículo" + i,"Marca" + i,Util.randomInt(1, 100), Util.randomInt(10, 100), Util.randomInt(50, 500));
      } catch (ArticuloEnAlmacenException e) {
        e.printStackTrace();
      }
    }
  }
  /**
   * Añade un articulo a almacen solicitando sus datos por consola
   */
  private static void anadirArticulo(){
    String nombre = Util.readStr("Introduce el nombre del articulo ");
    String marca = Util.readStr("Introduce la marca del articulo ");
    double precioCompra = Util.readDouble("Introduce el precio de compra del artículo ");
    double precioVenta = Util.readDouble("Introduce el precio de venta del artículo ");
    int numUnidades = Util.readInt("Introduce el número de unidades a introducir ");
    int stockSeguridad = Util.readInt("Introduce el stock de seguridad del artículo ");
    int stockMaximo = Util.readInt("Introduce el stock de seguridad máximo ");
    try {
      almacen.incluirArticulo(nombre, marca, precioCompra, precioVenta, numUnidades, stockSeguridad, stockMaximo);
    } catch (ArticuloEnAlmacenException e) {
      System.err.println("ERROR: el artículo que has intentado añadir ya existe en el almacén.");
      e.printStackTrace();
    }
  }
  /**
   * Elimina un articulo del almacen solicitando sus datos por consola
   */
  private static void eliminarArticulo(){
    int code = Util.readInt("Introduce el código del articulo a eliminar");
    try {
      almacen.eliminarArticulo(code);
    } catch (ArticuloNoEncontradoException e) {
      printCodigoErroneo();
    }
  }
  /**
   * Muestra un mensaje de error si no encuentra un articulo dentro del almacen
   */
  private static void printCodigoErroneo() {
    System.err.println("ERROR: ese código no corresponde a ningún artículo.");

  }

  /**
   * Añade unidades a un articulo del almacen, solicitando sus datos por consola
   */
  private static void anadirUnidades(){
    int code = Util.readInt("Introduce el código del articulo a añadir unidades");
    int numUnidades = Util.readInt("Introduce el número de unidades a añadir al artículo");
    try {
      almacen.anadirUnidades(code, numUnidades);
    } catch (ArticuloNoEncontradoException e) {
      printCodigoErroneo();
    } catch (StockIllegalException e) {
      System.err.println("El número de unidades no puede ser mayor al stock máximo");
      e.printStackTrace();
    }
  }
  /**
   * Elimina unidades de un articulo del almacen, solicitando sus datos por consola
   */
  private static void eliminarUnidades(){
    int code = Util.readInt("Introduce el código del articulo a eliminar unidades");
    int numUnidades = Util.readInt("Introduce el número de unidades a eliminar al artículo");
    try {
      almacen.eliminarUnidades(code, numUnidades);
    } catch (ArticuloNoEncontradoException e) {
      printCodigoErroneo();
    } catch (StockIllegalException e) {
      System.err.println("El número de unidades no puede ser menor que 0");
      e.printStackTrace();
    }
  }
  /**
   * Modifica un articulo del almacen, solicitando sus datos por consola
   */
  private static void modificarArticulo(){
    int code = Util.readInt("Introduce el código del articulo a modificar");
    String nombre = Util.readStr("Introduce el nombre del articulo ");
    String marca = Util.readStr("Introduce la marca del articulo ");
    double precioCompra = Util.readDouble("Introduce el precio de compra del artículo ");
    double precioVenta = Util.readDouble("Introduce el precio de venta del artículo ");
    int numUnidades = Util.readInt("Introduce el número de unidades a introducir ");
    int stockSeguridad = Util.readInt("Introduce el stock de seguridad del artículo ");
    int stockMaximo = Util.readInt("Introduce el stock de seguridad máximo ");
    try {
      almacen.modificarArticulo(code, nombre, marca, precioCompra, precioVenta, numUnidades, stockSeguridad, stockMaximo);
    } catch (ArticuloNoEncontradoException e) {
      printCodigoErroneo();
    } catch (ArticuloEnAlmacenException e) {
      System.err.println("El nombre y la marca ya existen en el almacén");
    }
  }
  /**
   * Guarda un objeto almacen en un fichero con formato JSON pedido por consola
   */
  private static void guardarJson() {
    try {
      almacen.guardarJson(Util.readStr("Nombre del fichero con extensión .json"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  /**
   * Genera un objeto Almacen a partir de un fichero JSON pedido por consola
   */
  private static void almacenAPartirDeJson() {
    try {
      try {
        almacen = new Almacen(Util.readStr("Nombre del fichero con extensión .json"));
      } catch (ParserConfigurationException | SAXException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  /**
   * Genera un objeto Almacen a partir de un fichero JSON pedido por consola
   */
  private static void guardarXml() {
    try {
      almacen.guardarXml(Util.readStr("Nombre del fichero con extensión .xml"));

    } catch (TransformerException e) {

      e.printStackTrace();
    } catch (ParserConfigurationException e) {

      e.printStackTrace();
    }catch (IOException e) {

      e.printStackTrace();
    }
  }
  /**
   * Genera un objeto Almacen a partir de un fichero XML pedido por consola
   */
  private static void almacenAPartirDeXml() {
    try {
      try {
        almacen = new Almacen(Util.readStr("Nombre del fichero con extensión .xml"));
      } catch (ParserConfigurationException | SAXException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  /**
   * Muestra por pantalla el almacen
   */
  private static void mostrarAlmacen() {
    System.out.println(almacen);
  }

}
