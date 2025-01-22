package dtos;

/**
 * Clase que representa un vector en un espacio bidimensional.
 * 
 * @author amorcia
 * @date 22/01/2025
 */
public class VectorDto {
    private int x; // Coordenada X del vector
    private int y; // Coordenada Y del vector

    /**
     * Constructor que inicializa el vector con las coordenadas dadas.
     * 
     * @param x Coordenada X del vector
     * @param y Coordenada Y del vector
     * @author amorcia
     * @date 22/01/2025
     */
    public VectorDto(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Obtiene la coordenada X del vector.
     * 
     * @return Coordenada X
     * @author amorcia
     * @date 22/01/2025
     */
    public int getX() {
        return x;
    }

    /**
     * Obtiene la coordenada Y del vector.
     * 
     * @return Coordenada Y
     * @author amorcia
     * @date 22/01/2025
     */
    public int getY() {
        return y;
    }

    /**
     * Establece la coordenada X del vector.
     * 
     * @param x Nueva coordenada X
     * @author amorcia
     * @date 22/01/2025
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Establece la coordenada Y del vector.
     * 
     * @param y Nueva coordenada Y
     * @author amorcia
     * @date 22/01/2025
     */
    public void setY(int y) {
        this.y = y;
    }
}
