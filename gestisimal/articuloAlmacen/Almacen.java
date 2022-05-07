package gestisimal.articuloAlmacen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * 
 * @author Hugo Vicente Peligro
 *
 *
 */

public class Almacen {

  private List<Articulo> almacen =  new ArrayList<>();
  /**
   * Crea un nuevo Almacen
   */
  public Almacen() {

  }
  /**
   * Crea un nuveo Almacen con los parametros indicados
   * @param fichero fichero donde se almacena un objeto almacen en formato XML o JSON
   * @throws IOException 
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public Almacen(String fichero) throws IOException, ParserConfigurationException, SAXException {
    if (fichero.substring(fichero.lastIndexOf(".")).equals(".json")){

      String json = Files.readString(Paths.get(fichero));

      Gson gson = new Gson();
      Type articuloListType = new TypeToken<ArrayList<Articulo>>(){}.getType();
      almacen = gson.fromJson(json, articuloListType);
      System.out.println(almacen);


    }

    if(fichero.substring(fichero.lastIndexOf(".")).equals(".xml")) {


      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(new File(fichero));

      document.getDocumentElement() // Accede al nodo raíz del documento
      .normalize();         // Elimina nodos vacíos y combina adyacentes en caso de que los hubiera

      // Accedemos a la lista de nodos Artículo
      NodeList nodes = document.getElementsByTagName("articulo");


      for (int i = 0; i < nodes.getLength(); i++) {
        Node node = nodes.item(i);
        Element articulo = (Element) node;

        // Campos del artículo (teniendo en cuenta que conocemos la estructura y etiquetas utilizadas)

        int codigo = Integer.parseInt(articulo.getAttribute("codigo"));
        String nombre = articulo.getElementsByTagName("nombre").item(0).getTextContent();
        String marca = articulo.getElementsByTagName("marca").item(0).getTextContent();
        int numUnidades = Integer.parseInt(articulo.getElementsByTagName("NumeroUnidades").item(0).getTextContent());
        double precioCompra = Double.parseDouble(articulo.getElementsByTagName("PrecioCompra").item(0).getTextContent());
        double precioVenta = Double.parseDouble(articulo.getElementsByTagName("PrecioVenta").item(0).getTextContent());
        int stockSeguridad = Integer.parseInt(articulo.getElementsByTagName("StockSeguridad").item(0).getTextContent());
        int stockMaximo = Integer.parseInt(articulo.getElementsByTagName("StockMaximo").item(0).getTextContent());

        almacen.add(new Articulo(codigo, nombre, marca, precioCompra, precioVenta, numUnidades, stockSeguridad,stockMaximo));
      }
      System.out.println(almacen);
    }



  }
  /**
   * Se encarga de incluir un Articulo en el almacen
   * @param nombre nombre del articulo a incluir en el almacen
   * @param marca marca del articulo a incluir en el almacen
   * @param precioCompra precioCompra del articulo a incluir en el almacen
   * @param precioVenta precioVenta del articulo a incluir en el almacen
   * @param numUnidades numUnidades del articulo a incluir en el almacen
   * @param stockSeguridad stockSeguridad del articulo a incluir en el almacen
   * @param stockMaximo stockMaximo del articulo a incluir en el almacen
   * @throws ArticuloEnAlmacenException cuando se intenta añadir un articulo que ya se encuentra en el almacen
   */
  public void incluirArticulo(String nombre, String marca, double precioCompra, 
      double precioVenta, int numUnidades, int stockSeguridad, int stockMaximo) throws ArticuloEnAlmacenException {
    if(!exists(nombre,marca)) {
      almacen.add(new Articulo(nombre, marca, precioCompra, precioVenta, numUnidades, stockSeguridad,stockMaximo));
    }
    else {
      throw new ArticuloEnAlmacenException("El elemento ya se encuentra en el almacén");
    }

  }
  /**
   * Se encarga de incluir un Articulo en el almacen
   * @param nombre nombre del articulo a incluir en el almacen
   * @param marca marca del articulo a incluir en el almacen
   * @param precioCompra precioCompra del articulo a incluir en el almacen
   * @param precioVenta precioVenta del articulo a incluir en el almacen
   * @param numUnidades numUnidades del articulo a incluir en el almacen
   * @param stockSeguridad stockSeguridad del articulo a incluir en el almacen
   * @throws ArticuloEnAlmacenException cuando se intenta añadir un articulo que ya se encuentra en el almacen
   */
  public void incluirArticulo(String nombre, String marca, double precioCompra,
      double precioVenta, int numUnidades, int stockSeguridad) throws ArticuloEnAlmacenException{

    incluirArticulo(nombre, marca, precioCompra, precioVenta, numUnidades, stockSeguridad, 0);
  }
  /**
   * Se encarga de incluir un Articulo en el almacen
   * @param nombre nombre del articulo a incluir en el almacen
   * @param marca marca del articulo a incluir en el almacen
   * @param precioCompra precioCompra del articulo a incluir en el almacen
   * @param precioVenta precioVenta del articulo a incluir en el almacen
   * @param numUnidades numUnidades del articulo a incluir en el almacen
   * @throws ArticuloEnAlmacenException cuando se intenta añadir un articulo que ya se encuentra en el almacen
   */
  public void incluirArticulo(String nombre, String marca, double precioCompra,
      double precioVenta, int numUnidades) throws ArticuloEnAlmacenException {
    incluirArticulo(nombre, marca, precioCompra, precioVenta, numUnidades, 0, 0);
  }

  /**
   * Se encarga de eliminar un Articulo en el almacen
   * @param code code del articulo a eliminar en el almacen
   * @throws ArticuloNoEncontradoException cuando no encuentra el articulo a eliminar en el almacen
   */
  public void eliminarArticulo(int code) throws ArticuloNoEncontradoException {
    if(!almacen.remove(new Articulo(code))) {
      throw new ArticuloNoEncontradoException("El articulo que se desea borrar no se encuentra en el almacen");
    }
  }

  /**
   * Se encarga de eliminar un Articulo en el almacen
   * @param nombre nombre del articulo a eliminar en el almacen
   * @param marca marca del articulo a eliminar en el almacen
   * @throws ArticuloNoEncontradoException cuando no encuentra el articulo a eliminar en el almacen
   */
  public void eliminarArticulo(String nombre, String marca) throws ArticuloNoEncontradoException {
    if(!exists(nombre, marca)) {
      throw new ArticuloNoEncontradoException("El articulo que se desea borrar no se encuentra en el almacen");
    }
    almacen.removeIf(n -> n.getMarca().equals(nombre) && n.getNombre().equals(nombre));
  }
  /**
   * Se encarga de buscar en el almacen un articulo para saber si lo contiene dicho almacen
   * @param name name del articulo a buscar en el almacen
   * @param marca marca del articiculo a buscar en el almacen 
   * @return true si el articulo se encuentra en el almacen. False si no se encuentra el articulo en 
   *          el almacen
   */
  public boolean exists(String name, String marca) {
    for(Articulo art : almacen) {
      if(art.getNombre().equals(name) && art.getMarca().equals(marca)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Añade unidades a un articulo determinado del almacen.
   * @param code code del articulo a añadir unidades del almacen
   * @param numUnidades numUnidades del articulo a añadir unidades del almacen
   * @throws ArticuloNoEncontradoException cuando el articulo a añadir unidades no se encuentra en el almacen
   * @throws StockIllegalException cuando el articulo a añadir unidades supera el stockMaximo del articulo
   */
  public void anadirUnidades(int code, int numUnidades) throws ArticuloNoEncontradoException, StockIllegalException {
    if(almacen.contains(new Articulo(code))){
      Articulo articulo = almacen.get(almacen.indexOf(new Articulo(code)));
      articulo.anadirUnidades(numUnidades);
      return;
    }
    throw new ArticuloNoEncontradoException("No se ha encontrado el artículo");

  }

  /**
   * Elimina unidades de un articulo determinado del almacen
   * @param code code del articulo a eliminar unidades del almacen
   * @param numUnidades numUnidades del articulo a eliminar unidades del almacen
   * @throws ArticuloNoEncontradoException cuando el articulo a eliminar unidades no se encuentra en el almacen
   * @throws StockIllegalException cuando el articulo a eliminar unidades supera el stockMaximo del articulo
   */
  public void eliminarUnidades(int code, int numUnidades) throws ArticuloNoEncontradoException, StockIllegalException{
    if(almacen.contains(new Articulo(code))){
      Articulo articulo = almacen.get(almacen.indexOf(new Articulo(code)));
      articulo.eliminarUnidades(numUnidades);
      return;
    }
    throw new ArticuloNoEncontradoException("No se ha encontrado el artículo");
  }

  /**
   * Modifica un articulo determinado del almacen
   * @param code code del articulo a modificar del almacen
   * @param nombre nombre del articulo a modificar del almacen
   * @param marca marca del articulo a modificar del almacen
   * @param precioCompra precioCompra del articulo a modificar del almacen
   * @param precioVenta precioVenta del articulo a modificar del almacen
   * @param numUnidades numUnidades del articulo a modificar del almacen
   * @param stockSeguridad stockSeguridad del articulo a modificar del almacen
   * @param stockMaximo stockMaximo del articulo a modificar del almacen
   * @throws ArticuloNoEncontradoException cuando el articulo a modificar del almacen no se encuentra
   * @throws ArticuloEnAlmacenException cuando cambias el articulo y ya se encuentra otro en el almacen con otro nombre
   */
  public void modificarArticulo(int code, String nombre, String marca,double precioCompra,
      double precioVenta, int numUnidades, int stockSeguridad, int stockMaximo) 
          throws ArticuloNoEncontradoException, ArticuloEnAlmacenException {
    if (!almacen.contains(new Articulo(code))){
      throw new ArticuloNoEncontradoException("No se ha encontrado el artículo");
    }
    Articulo articulo = almacen.get(almacen.indexOf(new Articulo(code)));
    if((!articulo.getNombre().equals(nombre) || !articulo.getMarca().equals(marca)) && exists(nombre, marca)) {
      throw new ArticuloEnAlmacenException("Ya existe un artículo con el mismo nombre y marca en el almacen");
    }
    articulo.set(nombre, marca, precioCompra, precioVenta, numUnidades, stockSeguridad, stockMaximo);
  }
  /**
   * Guarda el objeto Almacen en un fichero de formato JSON
   * @param fichero fichero JSON donde se quiera guardar el objeto almacen
   * @throws IOException
   */
  public void guardarJson(String fichero) throws IOException {

    String json = new Gson().toJson(almacen);

    //Guardamos Json en un archivo.
    BufferedWriter file = new BufferedWriter(new FileWriter(fichero));
    file.write(json);
    file.close();

    System.out.println("Creado almacen.json");
  }
  /**
   * Guarda el objeto almacen en un fichero de formato XML
   * @param fichero fichero XML donde se quiere guardar el objeto almacen
   * @throws IOException
   * @throws TransformerException
   * @throws ParserConfigurationException
   */
  public void guardarXml(String fichero) throws IOException, TransformerException, ParserConfigurationException {



    // Creación del documento XML 
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.newDocument(); // este objeto hará todo el trabajo

    Element root = document.createElement("almacen");
    document.appendChild(root);

    //Recorremos lista de artículos
    for(Articulo articulo : almacen) {

      // Nodo Artículo (contiene los elementos)
      Element elementArt = document.createElement("articulo");
      root.appendChild(elementArt);

      //El código es un atributo del nodo articulo
      Attr attrCodigo = document.createAttribute("codigo");
      attrCodigo.setValue(Integer.toString(articulo.getArticuloCodigo()));
      elementArt.setAttributeNode(attrCodigo);


      //Creación del elemento nombre
      Element elementNombre = document.createElement("nombre");
      elementNombre.appendChild(document.createTextNode(articulo.getNombre()));
      elementArt.appendChild(elementNombre);

      //Creacion del elemento marca
      Element elementMarca = document.createElement("marca");
      elementMarca.appendChild(document.createTextNode(articulo.getMarca()));
      elementArt.appendChild(elementMarca);

      //Creación del elemento precioCompra
      Element elementPrecCompra = document.createElement("PrecioCompra");
      elementPrecCompra.appendChild(document.createTextNode(Double.toString(articulo.getPrecioCompra())));
      elementArt.appendChild(elementPrecCompra);

      // Creación del elemento precioVenta    
      Element elementPrecVenta = document.createElement("PrecioVenta");
      elementPrecVenta.appendChild(document.createTextNode(Double.toString(articulo.getPrecioVenta())));
      elementArt.appendChild(elementPrecVenta);

      // Creación del elemento numUnidades
      Element elementNumUnidades = document.createElement("NumeroUnidades");
      elementNumUnidades.appendChild(document.createTextNode(Integer.toString(articulo.getNumUnidades())));
      elementArt.appendChild(elementNumUnidades);


      //Creación del elemento stockDeSeguridad
      Element elementStockSeguridad = document.createElement("StockSeguridad");
      elementStockSeguridad.appendChild(document.createTextNode(Integer.toString(articulo.getStockSeguridad())));
      elementArt.appendChild(elementStockSeguridad);

      //Creacion del elemeto stockMaximo
      Element elementStockMaximo = document.createElement("StockMaximo");
      elementStockMaximo.appendChild(document.createTextNode(Integer.toString(articulo.getStockMaximo())));
      elementArt.appendChild(elementStockMaximo);  

    }

    // Guardar XML en fichero
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(document);
    StreamResult result;
    result = new StreamResult(new FileWriter(fichero));
    transformer.transform(source, result);

    System.out.println("Creado "+ fichero);
  }

  /**
   * Devuelve una representación del almacen en forma de cadena. 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "almacen [almacen=" + almacen + "]";
  }



}
