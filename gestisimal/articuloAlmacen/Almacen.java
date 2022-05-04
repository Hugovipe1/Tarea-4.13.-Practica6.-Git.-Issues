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

  public Almacen() {

  }

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

  public void incluirArticulo(String nombre, String marca, double precioCompra, 
      double precioVenta, int numUnidades, int stockSeguridad, int stockMaximo) throws ArticuloEnAlmacenException {
    if(!exists(nombre,marca)) {
      almacen.add(new Articulo(nombre, marca, precioCompra, precioVenta, numUnidades, stockSeguridad,stockMaximo));
    }
    else {
      throw new ArticuloEnAlmacenException("El elemento ya se encuentra en el almacén");
    }

  }

  public void incluirArticulo(String nombre, String marca, double precioCompra,
      double precioVenta, int numUnidades, int stockSeguridad) throws ArticuloEnAlmacenException{

    incluirArticulo(nombre, marca, precioCompra, precioVenta, numUnidades, stockSeguridad, 0);
  }

  public void incluirArticulo(String nombre, String marca, double precioCompra,
      double precioVenta, int numUnidades) throws ArticuloEnAlmacenException {
    incluirArticulo(nombre, marca, precioCompra, precioVenta, numUnidades, 0, 0);
  }


  public void eliminarArticulo(int code) throws ArticuloNoEncontradoException {
    if(!almacen.remove(new Articulo(code))) {
      throw new ArticuloNoEncontradoException("El articulo que se desea borrar no se encuentra en el almacen");
    }
  }


  public void eliminarArticulo(String nombre, String marca) throws ArticuloNoEncontradoException {
    if(!exists(nombre, marca)) {
      throw new ArticuloNoEncontradoException("El articulo que se desea borrar no se encuentra en el almacen");
    }
    almacen.removeIf(n -> n.getMarca().equals(nombre) && n.getNombre().equals(nombre));
  }

  public boolean exists(String name, String marca) {
    for(Articulo art : almacen) {
      if(art.getNombre().equals(name) && art.getMarca().equals(marca)) {
        return true;
      }
    }
    return false;
  }


  public void anadirUnidades(int code, int numUnidades) throws ArticuloNoEncontradoException, StockIllegalException {
    if(almacen.contains(new Articulo(code))){
      Articulo articulo = almacen.get(almacen.indexOf(new Articulo(code)));
      articulo.anadirUnidades(numUnidades);
      return;
    }
    throw new ArticuloNoEncontradoException("No se ha encontrado el artículo");

  }


  public void eliminarUnidades(int code, int numUnidades) throws ArticuloNoEncontradoException, StockIllegalException{
    if(almacen.contains(new Articulo(code))){
      Articulo articulo = almacen.get(almacen.indexOf(new Articulo(code)));
      articulo.eliminarUnidades(numUnidades);
      return;
    }
    throw new ArticuloNoEncontradoException("No se ha encontrado el artículo");
  }

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

  public void guardarJson(String fichero) throws IOException {

    String json = new Gson().toJson(almacen);

    //Guardamos Json en un archivo.
    BufferedWriter file = new BufferedWriter(new FileWriter(fichero));
    file.write(json);
    file.close();

    System.out.println("Creado almacen.json");
  }

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


  @Override
  public String toString() {
    return "almacen [almacen=" + almacen + "]";
  }



}
