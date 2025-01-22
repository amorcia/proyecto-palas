package dtos;

/**
 * Clase PalaDto que representa una pala en un juego.
 * 
 * @author amorcia
 * @date 22/01/2025
 */
public class PalaDto {
    private VectorDto posicion; // Posición de la pala en el espacio
    private final int ancho;    // Ancho de la pala
    private final int alto;     // Alto de la pala

    /**
     * Constructor de la clase PalaDto.
     * 
     * @param x     La coordenada x de la posición inicial de la pala.
     * @param y     La coordenada y de la posición inicial de la pala.
     * @param ancho El ancho de la pala.
     * @param alto  El alto de la pala.
     * @author amorcia
     * @date 22/01/2025
     */
    public PalaDto(int x, int y, int ancho, int alto) {
        this.posicion = new VectorDto(x, y); // Inicializa la posición de la pala
        this.ancho = ancho; // Asigna el ancho
        this.alto = alto;   // Asigna el alto
    }

    /**
     * Obtiene la posición de la pala.
     * 
     * @return La posición de la pala como un objeto VectorDto.
     * @author amorcia
     * @date 22/01/2025
     */
    public VectorDto getPosicion() {
        return posicion; // Retorna la posición actual
    }

    /**
     * Obtiene el ancho de la pala.
     * 
     * @return El ancho de la pala.
     * @author amorcia
     * @date 22/01/2025
     */
    public int getAncho() {
        return ancho; // Retorna el ancho
    }

    /**
     * Obtiene el alto de la pala.
     * 
     * @return El alto de la pala.
     * @author amorcia
     * @date 22/01/2025
     */
    public int getAlto() {
        return alto; // Retorna el alto
    }

    /**
     * Mueve la pala a una nueva posición en el eje x.
     * 
     * @param x La nueva coordenada x de la pala.
     * @author amorcia
     * @date 22/01/2025
     */
    public void mover(int x) {
        this.posicion.setX(x); // Actualiza la posición en x
    }
}

