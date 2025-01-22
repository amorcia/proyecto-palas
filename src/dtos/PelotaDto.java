package dtos;

/**
 * Clase PelotaDto que representa una pelota en un espacio 2D.
 * Contiene información sobre su posición, velocidad y radio.
 * 
 * @author amorcia
 * @date 22/01/2025
 */
public class PelotaDto {
    private VectorDto posicion;  // Posición actual de la pelota
    private VectorDto velocidad;  // Velocidad actual de la pelota
    private final int radio;      // Radio de la pelota

    /**
     * Constructor de PelotaDto.
     * 
     * @param x          Coordenada X de la posición inicial
     * @param y          Coordenada Y de la posición inicial
     * @param radio      Radio de la pelota
     * @param velocidadX Componente X de la velocidad inicial
     * @param velocidadY Componente Y de la velocidad inicial
     * @author amorcia
     * @date 22/01/2025
     */
    public PelotaDto(int x, int y, int radio, double velocidadX, double velocidadY) {
        this.posicion = new VectorDto(x, y); // Inicializa la posición
        this.velocidad = new VectorDto((int) velocidadX, (int) velocidadY); // Inicializa la velocidad
        this.radio = radio; // Asigna el radio
    }

    /**
     * Obtiene la posición actual de la pelota.
     * 
     * @return Posición de la pelota
     * @author amorcia
     * @date 22/01/2025
     */
    public VectorDto getPosicion() {
        return posicion;
    }

    /**
     * Obtiene la velocidad actual de la pelota.
     * 
     * @return Velocidad de la pelota
     * @author amorcia
     * @date 22/01/2025
     */
    public VectorDto getVelocidad() {
        return velocidad;
    }

    /**
     * Mueve la pelota según su velocidad actual.
     * Actualiza la posición de la pelota.
     * 
     * @author amorcia
     * @date 22/01/2025
     */
    public void mover() {
        this.posicion.setX(this.posicion.getX() + velocidad.getX()); // Actualiza coordenada X
        this.posicion.setY(this.posicion.getY() + velocidad.getY()); // Actualiza coordenada Y
    }

    /**
     * Invierte la componente Y de la velocidad, simulando un rebote vertical.
     * 
     * @author amorcia
     * @date 22/01/2025
     */
    public void rebotarVertical() {
        velocidad.setY(-velocidad.getY()); // Invierte la dirección de Y
    }

    /**
     * Invierte la componente X de la velocidad, simulando un rebote horizontal.
     * 
     * @author amorcia
     * @date 22/01/2025
     */
    public void rebotarHorizontal() {
        velocidad.setX(-velocidad.getX()); // Invierte la dirección de X
    }
}