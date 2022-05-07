package gestisimal.articuloAlmacen;
/**
 * Lanzada para indicar que no se ha encontrado el articulo en el almacen
 * @author Hugo Vicente Peligro
 *
 *
 */

public class ArticuloNoEncontradoException extends Exception {
  /**
   * Construye una excepción ArticuloNoEncontradoException con el mensaje especificado
   * @param message mensaje detallado
   */
  public ArticuloNoEncontradoException(String message) {
    super(message);
  }

}
