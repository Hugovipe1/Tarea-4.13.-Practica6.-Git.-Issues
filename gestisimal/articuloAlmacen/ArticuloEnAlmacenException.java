package gestisimal.articuloAlmacen;
/**
 * Lanzada para indicar que ya se encuentra el articulo en el almacen
 * @author Hugo Vicente Peligro
 *
 *
 */
public class ArticuloEnAlmacenException extends Exception {

  /**
   * Construye una excepción ArticuloEnAlmacenException con el mensaje especificado
   * @param message mensaje detallado
   */
  public ArticuloEnAlmacenException(String message) {
    super(message);

  }

}
