package gestisimal.articuloAlmacen;

import java.util.Objects;

/**
 * 
 * @author Hugo Vicente Peligro
 *
 *
 */

class Articulo implements Comparable<Articulo>{
  static int codigoGeneral = 0;
  private int articuloCodigo;
  private String nombre;
  private String marca;
  private double precioCompra;
  private double precioVenta;
  private int stockSeguridad;
  private int stockMaximo;
  private int numUnidades;
  /**
   * Crea un nuevo articulo con los parámetros indicados
   * @param nombre nombre del nuevo Articulo
   * @param marca marca del nuevo Articulo
   * @param precioCompra precioCompra del nuevo Articulo
   * @param precioVenta precioVenta del nuevo Articulo
   * @param numUnidades numUnidades del nuevo Articulo
   * @param stockSeguridad stockSeguridad del nuevo Articulo
   * @param stockMaximo stockMaximo del nuevo Articulo
   * @throws AtributoIlegalException Cuando uno de los parametros no es correcto.
   */
  Articulo(String nombre, String marca, double precioCompra, double precioVenta,
      int numUnidades, int stockSeguridad, int stockMaximo) throws AtributoIlegalException{
    set(nombre, marca, precioCompra, precioVenta, numUnidades, stockSeguridad, stockMaximo);
    this.articuloCodigo = ++codigoGeneral;
  }
  /**
   * Crea un nuevo articulo con los parámetros indicados
   * @param codigo codigo del nuevo Articulo
   * @param nombre nombre del nuevo Articulo
   * @param marca marca del nuevo Articulo
   * @param precioCompra precio del nuevo Articulo
   * @param precioVenta precioVenta del nuevo Articulo
   * @param numUnidades numUnidades del nuevo Articulo
   * @param stockSeguridad stockSeguridad del nuevo Articulo
   * @param stockMaximo stockMaximo del nuevo Articulo
   * @throws AtributoIlegalException Cuando uno de los parametros no es correcto.
   */
  Articulo(int codigo, String nombre, String marca, double precioCompra, double precioVenta,
      int numUnidades, int stockSeguridad, int stockMaximo) throws AtributoIlegalException{
    if(codigo < 0) {
      throw new AtributoIlegalException("No puede haber un código menor que 0");
    }
    this.articuloCodigo = codigo;
    set(nombre, marca, precioCompra, precioVenta, numUnidades, stockSeguridad, stockMaximo);
  }
  /**
   * Crea un nuevo articulo con los parámetros indicados
   * @param nombre nombre del nuevo Articulo
   * @param marca marca del nuevo Articulo
   * @param precioCompra precioCompra del nuevo Articulo
   * @param precioVenta precioVenta del nuevo Articulo
   * @param numUnidades numUnidades del nuevo Articulo
   * @param stockSeguridad stockSeguridad del nuevo Articulo
   * @throws AtributoIlegalException Cuando uno de los parametros no es correcto.
   */
  Articulo(String nombre, String marca, double precioCompra, double precioVenta,
      int numUnidades, int stockSeguridad) throws AtributoIlegalException{
    this(nombre, marca, precioCompra, precioVenta, numUnidades, stockSeguridad, 0);
  }
  /**
   * Crea un nuevo articulo con los parámetros indicados
   * @param nombre nombre del nuevo Articulo
   * @param marca marcadel nuevo Articulo
   * @param precioCompra precioCompra del nuevo Articulo
   * @param precioVenta precioVenta
   * @param numUnidades del nuevo Articulo
   * @throws AtributoIlegalException Cuando uno de los parametros no es correcto.
   */
  Articulo(String nombre, String marca, double precioCompra, double precioVenta,
      int numUnidades) throws AtributoIlegalException{
    this(nombre, marca, precioCompra, precioVenta, numUnidades, 0, 0);
  }
  /**
   * Crea un nuevo articulo con los parámetros indicados
   * @param codigo codigo del nuevo Articulo
   */
  Articulo(int codigo){ 
    if (codigo < 0) {
      throw new AtributoIlegalException("No puede haber un código negativo");
    }
    this.articuloCodigo = codigo;
  }
  /**
   * Se encarga de actualizar los valores de los atributos
   * @param nombre nombre a asignar
   * @param marca marca a asignar
   * @param precioCompra precioCompra a asignar
   * @param precioVenta precioVenta a asignar
   * @param numUnidades numUnidades a asignar
   * @param stockSeguridad stockSeguridad a asignar
   * @param stockMaximo stockMaximo a asignar
   */
  void set(String nombre, String marca, double precioCompra, double precioVenta, int numUnidades,
      int stockSeguridad, int stockMaximo) {
    setNombre(nombre);
    setMarca(marca);
    setPrecioVenta(precioVenta);
    setPrecioCompra(precioCompra);
    setNumUnidades(numUnidades);
    if(stockSeguridad <= this.stockMaximo) {
      setStockSeguridad(stockSeguridad);
      setStockMaximo(stockMaximo);
    }else {
      setStockMaximo(stockMaximo);
      setStockSeguridad(stockSeguridad);
    }


  }
  /**
   * Se encarga de añadir unidades de un articulo
   * @param numUnidades numUnidades a añadir del Articulo
   * @throws StockIllegalException Cuando el numero de unidades es mayor que el stock máximo.
   */
  void anadirUnidades(int numUnidades) throws StockIllegalException {
    if(numUnidades < 0) {
      throw new UnidadesNegativasException("No se pueden añadir unidades negativas");
    }
    if((numUnidades + this.numUnidades) > stockMaximo && stockMaximo != 0) {
      throw new StockIllegalException("El número de unidades no puede ser mayor al stock máximo");
    }
    this.numUnidades += numUnidades;
  }
  /**
   * Se encarga de eliminar unidades
   * @param numUnidades numUnidades a eliminar del Articulo
   * @throws StockIllegalException Cuando el numero de unidades es menor que 0.
   */
  void eliminarUnidades(int numUnidades) throws StockIllegalException {
    if (numUnidades < 0) {
      throw new UnidadesNegativasException("No se puede eliminar un numero de unidades negativo");
    }
    if ((this.numUnidades - numUnidades) < 0) {
      throw new StockIllegalException("El número de unidades no puede ser menor que 0");
    }
    this.numUnidades -= numUnidades;
  }


  /**
   * Obtiene el stock de seguridad
   * @return stockSeguridad del coche
   */
  public int getStockSeguridad() {
    return stockSeguridad;
  }
  /**
   * Cambia el valor del stock de seguridad
   * @param stockSeguridad stockSeguridad a asignar
   */
  void setStockSeguridad(int stockSeguridad){
    if(stockSeguridad < 0) {
      throw new AtributoIlegalException("El stock de seguridad no puede ser menor que 0");
    }

    if((stockSeguridad > stockMaximo) && stockMaximo != 0) {
      throw new AtributoIlegalException("No puede haber un stock de seguridad mayor que el stock máximo");
    }
    this.stockSeguridad = stockSeguridad;
  }
  /**
   * Obtiene el stock maximo
   * @return stockMaximo del articulo
   */
  public int getStockMaximo() {
    return stockMaximo;
  }
  /**
   * Asigna el valor del stock máximo
   * @param stockMaximo stockMaximo a asignar
   */
  void setStockMaximo(int stockMaximo){
    if(stockMaximo < 0) {
      throw new AtributoIlegalException("El stock máximo no puede ser menor que 0");
    }

    if((stockSeguridad > stockMaximo)) {
      throw new AtributoIlegalException("No puede haber un stock de seguridad mayor que el stock máximo");
    }

    this.stockMaximo = stockMaximo;
  }
  /**
   * Obtiene el nombre
   * @return nombre del articulo
   */
  public String getNombre() {
    return nombre;
  }
  /**
   * asigna el valor del nombre
   * @param nombre nombre a asignar
   */
  void setNombre(String nombre){
    if(nombre.isBlank()) {
      throw new AtributoIlegalException("No puede haber un nombre en blanco");
    }
    this.nombre = nombre;
  }
  /**
   * Obtiene el precioCompra
   * @return precioCompra del articulo
   */
  public double getPrecioCompra() {
    return precioCompra;
  }
  /**
   * Asigna el valor del precioCompra
   * @param precioCompra precioCompra a asignar
   */
  void setPrecioCompra(double precioCompra){
    if(precioCompra < 0) {
      throw new AtributoIlegalException("El precio de compra no puede ser mayor que el de venta o menor a 0");
    }
    this.precioCompra = precioCompra;
  }
  /**
   * Obtiene el precio de venta del Articulo
   * @return precioVenta del articulo
   */
  public double getPrecioVenta() {
    return precioVenta;
  }
  /**
   * Asigna el precio de venta
   * @param precioVenta precioVenta a asignar 
   */
  void setPrecioVenta(double precioVenta){
    if(precioVenta < 0) {
      throw new AtributoIlegalException("El precio de compra no puede ser mayor que el de venta o menor a 0");
    }
    this.precioVenta = precioVenta;
  }
  /**
   * Obtiene el número de unidades del Articulo
   * @return numUnidades del articulo
   */
  public int getNumUnidades() {
    return numUnidades;
  }
  /**
   * Asigna el numero de unidades
   * @param numUnidades numUnidades a asignar
   */
  void setNumUnidades(int numUnidades){
    if(numUnidades < 0) {
      throw new AtributoIlegalException("No puede haber un articulo con el número de unidades negativas");
    }
    this.numUnidades = numUnidades;
  }
  /**
   * Obtiene el codigo del Articulo
   * @return articuloCodigo del articulo
   */
  public int getArticuloCodigo() {
    return articuloCodigo;
  }

  /**
   * Obtiene la marca del Articulo
   * @return marca del articulo
   */
  public String getMarca() {
    return marca;
  }
  /**
   * Asigna la marca
   * @param marca marca a asignar
   */
  void setMarca(String marca){
    if(marca.isBlank()) {
      throw new AtributoIlegalException("No puede haber un nombre en blanco");
    }
    this.marca = marca;
  }
  /**
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hash(articuloCodigo);
  }
  /**
   * Indica si otro articulo es "igual" a este. Para ello se fija en sus codigos
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Articulo other = (Articulo) obj;
    return articuloCodigo == other.articuloCodigo;
  }
/**
 * Devuelve una representación del articulo en forma de cadena.
 * 
 * @see java.lang.Object#toString()
 */
  @Override
  public String toString() {
    return "Articulo [articuloCodigo=" + articuloCodigo + ", nombre=" + nombre + ", marca=" + marca
        + ", precioCompra=" + precioCompra + ", precioVenta=" + precioVenta + ", stockSeguridad="
        + stockSeguridad + ", stockMaximo=" + stockMaximo + ", numUnidades=" + numUnidades + "]";
  }
/**
 * Devuelve un numero menor de 0 si otro articulo es mayor que este.
 * Devuelve igual a 0 si ambos articulos son iguales.
 * Devuelve mayor de 0 si este es objeto es mayor que otro con el que se compara
 */
  @Override
  public int compareTo(Articulo o) {

    return this.articuloCodigo - o.articuloCodigo;
  }
}
