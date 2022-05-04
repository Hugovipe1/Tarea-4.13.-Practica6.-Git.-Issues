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

  Articulo(String nombre, String marca, double precioCompra, double precioVenta,
      int numUnidades, int stockSeguridad, int stockMaximo) throws AtributoIlegalException{
    set(nombre, marca, precioCompra, precioVenta, numUnidades, stockSeguridad, stockMaximo);
    this.articuloCodigo = ++codigoGeneral;
  }
  
  Articulo(int codigo, String nombre, String marca, double precioCompra, double precioVenta,
      int numUnidades, int stockSeguridad, int stockMaximo) throws AtributoIlegalException{
    if(codigo < 0) {
      throw new AtributoIlegalException("No puede haber un código menor que 0");
    }
    this.articuloCodigo = codigo;
    set(nombre, marca, precioCompra, precioVenta, numUnidades, stockSeguridad, stockMaximo);
  }

  Articulo(String nombre, String marca, double precioCompra, double precioVenta,
      int numUnidades, int stockSeguridad) throws AtributoIlegalException{
    this(nombre, marca, precioCompra, precioVenta, numUnidades, stockSeguridad, 0);
  }

  Articulo(String nombre, String marca, double precioCompra, double precioVenta,
      int numUnidades) throws AtributoIlegalException{
    this(nombre, marca, precioCompra, precioVenta, numUnidades, 0, 0);
  }

  Articulo(int codigo){ 
    if (codigo < 0) {
      throw new AtributoIlegalException("No puede haber un código negativo");
    }
    this.articuloCodigo = codigo;
  }

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

  void anadirUnidades(int numUnidades) throws StockIllegalException {
    if(numUnidades < 0) {
      throw new UnidadesNegativasException("No se pueden añadir unidades negativas");
    }
    if((numUnidades + this.numUnidades) > stockMaximo && stockMaximo != 0) {
      throw new StockIllegalException("El número de unidades no puede ser mayor al stock máximo");
    }
    this.numUnidades += numUnidades;
  }

  void eliminarUnidades(int numUnidades) throws StockIllegalException {
    if (numUnidades < 0) {
      throw new UnidadesNegativasException("No se puede eliminar un numero de unidades negativo");
    }
    if ((this.numUnidades - numUnidades) < 0) {
      throw new StockIllegalException("El número de unidades no puede ser menor que 0");
    }
    this.numUnidades -= numUnidades;
  }



  public int getStockSeguridad() {
    return stockSeguridad;
  }

  void setStockSeguridad(int stockSeguridad){
    if(stockSeguridad < 0) {
      throw new AtributoIlegalException("El stock de seguridad no puede ser menor que 0");
    }

    if((stockSeguridad > stockMaximo) && stockMaximo != 0) {
      throw new AtributoIlegalException("No puede haber un stock de seguridad mayor que el stock máximo");
    }
    this.stockSeguridad = stockSeguridad;
  }

  public int getStockMaximo() {
    return stockMaximo;
  }

  void setStockMaximo(int stockMaximo){
    if(stockMaximo < 0) {
      throw new AtributoIlegalException("El stock máximo no puede ser menor que 0");
    }
    
    if((stockSeguridad > stockMaximo)) {
      throw new AtributoIlegalException("No puede haber un stock de seguridad mayor que el stock máximo");
    }

    this.stockMaximo = stockMaximo;
  }

  public String getNombre() {
    return nombre;
  }

  void setNombre(String nombre){
    if(nombre.isBlank()) {
      throw new AtributoIlegalException("No puede haber un nombre en blanco");
    }
    this.nombre = nombre;
  }

  public double getPrecioCompra() {
    return precioCompra;
  }

  void setPrecioCompra(double precioCompra){
    if(precioCompra < 0) {
      throw new AtributoIlegalException("El precio de compra no puede ser mayor que el de venta o menor a 0");
    }
    this.precioCompra = precioCompra;
  }

  public double getPrecioVenta() {
    return precioVenta;
  }

  void setPrecioVenta(double precioVenta){
    if(precioVenta < 0) {
      throw new AtributoIlegalException("El precio de compra no puede ser mayor que el de venta o menor a 0");
    }
    this.precioVenta = precioVenta;
  }

  public int getNumUnidades() {
    return numUnidades;
  }

  void setNumUnidades(int numUnidades){
    if(numUnidades < 0) {
      throw new AtributoIlegalException("No puede haber un articulo con el número de unidades negativas");
    }
    this.numUnidades = numUnidades;
  }

  public int getArticuloCodigo() {
    return articuloCodigo;
  }


  public String getMarca() {
    return marca;
  }

  void setMarca(String marca){
    if(marca.isBlank()) {
      throw new AtributoIlegalException("No puede haber un nombre en blanco");
    }
    this.marca = marca;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(articuloCodigo);
  }

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

  @Override
  public String toString() {
    return "Articulo [articuloCodigo=" + articuloCodigo + ", nombre=" + nombre + ", marca=" + marca
        + ", precioCompra=" + precioCompra + ", precioVenta=" + precioVenta + ", stockSeguridad="
        + stockSeguridad + ", stockMaximo=" + stockMaximo + ", numUnidades=" + numUnidades + "]";
  }

  @Override
  public int compareTo(Articulo o) {
    
    return this.articuloCodigo - o.articuloCodigo;
  }
}
